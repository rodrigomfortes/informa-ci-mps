package informaci.controle;

import informaci.entidade.Categoria;
import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Papel;
import informaci.entidade.Publicacao;
import informaci.entidade.PublicacaoInvalidaException;
import informaci.entidade.Usuario;
import informaci.persistencia.PublicacaoRepositorioEmMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorDePublicacoesTest {

    private static final Usuario PUBLICADOR = new Usuario(
            "Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", "Senha@123", Papel.PUBLICADOR);

    private PublicacaoRepositorioEmMemoria repositorio;
    private GerenciadorDePublicacoes gerenciador;

    @BeforeEach
    void criarGerenciadorComRepositorioVazio() {
        repositorio = new PublicacaoRepositorioEmMemoria();
        gerenciador = new GerenciadorDePublicacoes(repositorio, new NullLog());
    }

    private static DadosDaPublicacao comunicado(String titulo) {
        return new DadosDaPublicacao(titulo, "Texto do comunicado.", Categoria.COMUNICADO, null, null);
    }

    @Test
    void deve_armazenar_a_publicacao_criada() {
        Publicacao publicacao = gerenciador.publicar(PUBLICADOR, comunicado("Mudança de sala"));

        assertEquals(List.of(publicacao), repositorio.listarTodas());
    }

    @Test
    void nao_deve_armazenar_publicacao_invalida() {
        assertThrows(PublicacaoInvalidaException.class, () -> gerenciador.publicar(PUBLICADOR, comunicado("")));

        assertEquals(0, gerenciador.quantidade());
    }

    @Test
    void deve_listar_o_feed_da_mais_recente_para_a_mais_antiga() {
        Publicacao primeira = gerenciador.publicar(PUBLICADOR, comunicado("Primeira"));
        Publicacao segunda = gerenciador.publicar(PUBLICADOR, comunicado("Segunda"));

        assertEquals(List.of(segunda, primeira), gerenciador.listarFeed());
    }

    @Test
    void deve_editar_mantendo_a_posicao_no_feed() {
        Publicacao primeira = gerenciador.publicar(PUBLICADOR, comunicado("Primeira"));
        Publicacao segunda = gerenciador.publicar(PUBLICADOR, comunicado("Segunda"));

        Publicacao editada = gerenciador.editar(primeira.getId(), comunicado("Primeira, corrigida"));

        assertEquals("Primeira, corrigida", gerenciador.buscarPorId(primeira.getId()).getTitulo());
        assertEquals(List.of(segunda, editada), gerenciador.listarFeed());
    }

    @Test
    void nao_deve_alterar_a_publicacao_quando_a_edicao_e_invalida() {
        Publicacao publicacao = gerenciador.publicar(PUBLICADOR, comunicado("Original"));

        assertThrows(PublicacaoInvalidaException.class,
                () -> gerenciador.editar(publicacao.getId(), comunicado("  ")));

        assertEquals("Original", gerenciador.buscarPorId(publicacao.getId()).getTitulo());
    }

    @Test
    void deve_recusar_editar_publicacao_inexistente() {
        assertThrows(PublicacaoNaoEncontradaException.class,
                () -> gerenciador.editar(UUID.randomUUID(), comunicado("Qualquer")));
    }

    @Test
    void deve_remover_a_publicacao() {
        Publicacao publicacao = gerenciador.publicar(PUBLICADOR, comunicado("Aviso"));

        gerenciador.remover(publicacao.getId());

        assertTrue(gerenciador.listarFeed().isEmpty());
    }

    @Test
    void deve_recusar_remover_publicacao_inexistente() {
        assertThrows(PublicacaoNaoEncontradaException.class, () -> gerenciador.remover(UUID.randomUUID()));
    }

    @Test
    void deve_contar_as_publicacoes_cadastradas() {
        gerenciador.publicar(PUBLICADOR, comunicado("Primeira"));
        gerenciador.publicar(PUBLICADOR, comunicado("Segunda"));

        assertEquals(2, gerenciador.quantidade());
    }

    @Test
    void deve_rejeitar_quando_repositorio_e_nulo() {
        assertThrows(NullPointerException.class, () -> new GerenciadorDePublicacoes(null, new NullLog()));
        assertThrows(NullPointerException.class, () -> new GerenciadorDePublicacoes(repositorio, null));
    }
}
