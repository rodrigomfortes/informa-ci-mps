package informaci.controle;

import informaci.entidade.Categoria;
import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Papel;
import informaci.entidade.Publicacao;
import informaci.entidade.PublicacaoInvalidaException;
import informaci.persistencia.PublicacaoRepositorioEmMemoria;
import informaci.persistencia.RegistroDeAcessoRepositorioEmMemoria;
import informaci.persistencia.UsuarioRepositorioEmMemoria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Desfazer a última edição de publicação pela fachada (padrão Memento). */
class DesfazerEdicaoDePublicacaoTest {

    private static final String EMAIL = "coordenacao@ci.ufpb.br";
    private static final LocalDateTime DATA = LocalDateTime.of(2026, 11, 20, 14, 0);
    private static final DadosDaPublicacao PALESTRA =
            new DadosDaPublicacao("Introdução a Rust", "Palestra aberta.", Categoria.PALESTRA, DATA, "Auditório do CI");

    private FacadeSingletonController fachada;

    @BeforeEach
    void inicializarFachada() {
        fachada = FacadeSingletonController.inicializar(new UsuarioRepositorioEmMemoria(),
                new PublicacaoRepositorioEmMemoria(), new RegistroDeAcessoRepositorioEmMemoria());
        fachada.adicionarUsuario("Coordenação", EMAIL, "coordenacao", "Senha@123", Papel.PUBLICADOR);
    }

    @AfterEach
    void encerrarFachada() {
        FacadeSingletonController.encerrar();
    }

    private static DadosDaPublicacao comunicado(String titulo) {
        return new DadosDaPublicacao(titulo, "Texto do comunicado.", Categoria.COMUNICADO, null, null);
    }

    private Publicacao unicaDoFeed() {
        return fachada.listarFeed().get(0);
    }

    @Test
    void deve_voltar_a_publicacao_ao_estado_de_antes_da_edicao() {
        Publicacao original = fachada.publicar(EMAIL, PALESTRA);
        fachada.editarPublicacao(original.getId(), comunicado("Palestra cancelada"));

        Publicacao restaurada = fachada.desfazerUltimaEdicaoDePublicacao();

        assertEquals(original.getId(), restaurada.getId());
        assertEquals("Introdução a Rust", unicaDoFeed().getTitulo());
        assertEquals(Categoria.PALESTRA, unicaDoFeed().getCategoria());
        assertEquals(DATA, unicaDoFeed().getDataHora().orElseThrow());
        assertEquals("Auditório do CI", unicaDoFeed().getLocal().orElseThrow());
    }

    @Test
    void deve_desfazer_apenas_a_ultima_edicao() {
        Publicacao publicacao = fachada.publicar(EMAIL, comunicado("Versão 1"));
        fachada.editarPublicacao(publicacao.getId(), comunicado("Versão 2"));
        fachada.editarPublicacao(publicacao.getId(), comunicado("Versão 3"));

        fachada.desfazerUltimaEdicaoDePublicacao();

        assertEquals("Versão 2", unicaDoFeed().getTitulo());
        assertThrows(NadaParaDesfazerException.class, fachada::desfazerUltimaEdicaoDePublicacao);
        assertEquals("Versão 2", unicaDoFeed().getTitulo());
    }

    @Test
    void deve_desfazer_a_ultima_edicao_mesmo_que_seja_de_outra_publicacao() {
        Publicacao primeira = fachada.publicar(EMAIL, comunicado("Primeira"));
        Publicacao segunda = fachada.publicar(EMAIL, comunicado("Segunda"));
        fachada.editarPublicacao(primeira.getId(), comunicado("Primeira editada"));
        fachada.editarPublicacao(segunda.getId(), comunicado("Segunda editada"));

        Publicacao restaurada = fachada.desfazerUltimaEdicaoDePublicacao();

        assertEquals(segunda.getId(), restaurada.getId());
        assertEquals("Segunda", restaurada.getTitulo());
        assertEquals("Primeira editada", fachada.listarFeed().get(1).getTitulo());
    }

    @Test
    void deve_recusar_quando_nao_houve_edicao() {
        fachada.publicar(EMAIL, comunicado("Aviso"));

        NadaParaDesfazerException erro =
                assertThrows(NadaParaDesfazerException.class, fachada::desfazerUltimaEdicaoDePublicacao);
        assertEquals("Não há edição de publicação para desfazer.", erro.getMessage());
    }

    @Test
    void edicao_recusada_nao_deve_substituir_a_ultima_edicao_valida() {
        Publicacao publicacao = fachada.publicar(EMAIL, comunicado("Original"));
        fachada.editarPublicacao(publicacao.getId(), comunicado("Editada"));
        assertThrows(PublicacaoInvalidaException.class,
                () -> fachada.editarPublicacao(publicacao.getId(), comunicado("")));

        fachada.desfazerUltimaEdicaoDePublicacao();

        assertEquals("Original", unicaDoFeed().getTitulo());
    }

    @Test
    void deve_esquecer_a_edicao_quando_a_publicacao_e_removida() {
        Publicacao publicacao = fachada.publicar(EMAIL, comunicado("Aviso"));
        fachada.editarPublicacao(publicacao.getId(), comunicado("Aviso editado"));
        fachada.removerPublicacao(publicacao.getId());

        assertThrows(NadaParaDesfazerException.class, fachada::desfazerUltimaEdicaoDePublicacao);
    }

    @Test
    void deve_manter_a_edicao_quando_outra_publicacao_e_removida() {
        Publicacao editada = fachada.publicar(EMAIL, comunicado("Aviso"));
        Publicacao outra = fachada.publicar(EMAIL, comunicado("Outro"));
        fachada.editarPublicacao(editada.getId(), comunicado("Aviso editado"));
        fachada.removerPublicacao(outra.getId());

        assertEquals("Aviso", fachada.desfazerUltimaEdicaoDePublicacao().getTitulo());
    }

    @Test
    void deve_registrar_o_acesso_do_desfazer() {
        Publicacao publicacao = fachada.publicar(EMAIL, comunicado("Aviso"));
        fachada.editarPublicacao(publicacao.getId(), comunicado("Aviso editado"));

        fachada.desfazerUltimaEdicaoDePublicacao();

        String relatorio = new String(
                fachada.gerarRelatorioDeAcessos(FormatoDeRelatorio.HTML), StandardCharsets.UTF_8);
        assertTrue(relatorio.contains("<tr><td>Desfazer edição de publicação</td><td>1</td></tr>"));
    }
}
