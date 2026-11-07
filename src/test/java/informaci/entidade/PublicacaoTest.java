package informaci.entidade;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicacaoTest {

    private static final LocalDateTime DATA_DO_EVENTO = LocalDateTime.of(2026, 11, 20, 14, 0);

    private static final Usuario PUBLICADOR = new Usuario(
            "Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", "Senha@123", Papel.PUBLICADOR);

    private static DadosDaPublicacao comunicado(String titulo, String texto) {
        return new DadosDaPublicacao(titulo, texto, Categoria.COMUNICADO, null, null);
    }

    private static DadosDaPublicacao palestra(LocalDateTime dataHora, String local) {
        return new DadosDaPublicacao("Introdução a Rust", "Palestra aberta.", Categoria.PALESTRA, dataHora, local);
    }

    private static String mensagemAoPublicar(Usuario autor, DadosDaPublicacao dados) {
        return assertThrows(PublicacaoInvalidaException.class, () -> new Publicacao(autor, dados)).getMessage();
    }

    @Test
    void deve_criar_publicacao_quando_dados_sao_validos() {
        Publicacao publicacao = new Publicacao(PUBLICADOR, palestra(DATA_DO_EVENTO, "Auditório do CI"));

        assertNotNull(publicacao.getId());
        assertNotNull(publicacao.getDataDePublicacao());
        assertEquals(PUBLICADOR, publicacao.getAutor());
        assertEquals("Introdução a Rust", publicacao.getTitulo());
        assertEquals(Categoria.PALESTRA, publicacao.getCategoria());
        assertEquals(DATA_DO_EVENTO, publicacao.getDataHora().orElseThrow());
        assertEquals("Auditório do CI", publicacao.getLocal().orElseThrow());
    }

    @Test
    void deve_aceitar_comunicado_sem_data_e_sem_local() {
        Publicacao publicacao = new Publicacao(PUBLICADOR, comunicado("Mudança de sala", "Aula na sala 101."));

        assertTrue(publicacao.getDataHora().isEmpty());
        assertTrue(publicacao.getLocal().isEmpty());
    }

    @Test
    void deve_remover_espacos_em_volta_do_titulo_e_do_texto() {
        Publicacao publicacao = new Publicacao(PUBLICADOR, comunicado("  Mudança de sala  ", "  Sala 101.  "));

        assertEquals("Mudança de sala", publicacao.getTitulo());
        assertEquals("Sala 101.", publicacao.getTexto());
    }

    @Test
    void deve_recusar_autor_nulo() {
        assertEquals("O autor da publicação é obrigatório.",
                mensagemAoPublicar(null, comunicado("Aviso", "Texto")));
    }

    @Test
    void deve_recusar_autor_que_nao_e_publicador() {
        Usuario membro = new Usuario("Ana Souza", "ana@academico.ufpb.br", "anasouza", "Senha@123", Papel.MEMBRO);

        assertEquals("Apenas usuários com papel Publicador podem publicar.",
                mensagemAoPublicar(membro, comunicado("Aviso", "Texto")));
    }

    @Test
    void deve_recusar_titulo_em_branco() {
        assertEquals("O título da publicação é obrigatório.",
                mensagemAoPublicar(PUBLICADOR, comunicado("   ", "Texto")));
    }

    @Test
    void deve_recusar_titulo_com_mais_de_100_caracteres() {
        assertEquals("O título deve ter no máximo 100 caracteres.",
                mensagemAoPublicar(PUBLICADOR, comunicado("a".repeat(101), "Texto")));
    }

    @Test
    void deve_aceitar_titulo_com_exatamente_100_caracteres() {
        String titulo = "a".repeat(100);

        assertEquals(titulo, new Publicacao(PUBLICADOR, comunicado(titulo, "Texto")).getTitulo());
    }

    @Test
    void deve_recusar_texto_vazio() {
        assertEquals("O texto da publicação é obrigatório.",
                mensagemAoPublicar(PUBLICADOR, comunicado("Aviso", null)));
    }

    @Test
    void deve_recusar_texto_com_mais_de_2000_caracteres() {
        assertEquals("O texto deve ter no máximo 2000 caracteres.",
                mensagemAoPublicar(PUBLICADOR, comunicado("Aviso", "a".repeat(2001))));
    }

    @Test
    void deve_recusar_categoria_nula() {
        assertEquals("A categoria da publicação é obrigatória.",
                mensagemAoPublicar(PUBLICADOR, new DadosDaPublicacao("Aviso", "Texto", null, null, null)));
    }

    @Test
    void deve_recusar_palestra_sem_data() {
        assertEquals("Publicações da categoria Palestra exigem data e horário.",
                mensagemAoPublicar(PUBLICADOR, palestra(null, "Auditório do CI")));
    }

    @Test
    void deve_recusar_evento_sem_local() {
        DadosDaPublicacao evento = new DadosDaPublicacao(
                "Maratona", "Maratona de programação.", Categoria.EVENTO, DATA_DO_EVENTO, "  ");

        assertEquals("Publicações da categoria Evento exigem local.", mensagemAoPublicar(PUBLICADOR, evento));
    }

    @Test
    void deve_recusar_local_com_mais_de_100_caracteres() {
        assertEquals("O local deve ter no máximo 100 caracteres.",
                mensagemAoPublicar(PUBLICADOR, palestra(DATA_DO_EVENTO, "a".repeat(101))));
    }

    @Test
    void deve_manter_identidade_autor_e_data_de_publicacao_ao_editar() {
        Publicacao original = new Publicacao(PUBLICADOR, comunicado("Aviso", "Texto"));

        Publicacao editada = original.comDados(palestra(DATA_DO_EVENTO, "Auditório do CI"));

        assertEquals(original, editada);
        assertEquals(original.getAutor(), editada.getAutor());
        assertEquals(original.getDataDePublicacao(), editada.getDataDePublicacao());
        assertEquals("Introdução a Rust", editada.getTitulo());
        assertEquals("Aviso", original.getTitulo());
    }

    @Test
    void deve_validar_os_novos_dados_ao_editar() {
        Publicacao original = new Publicacao(PUBLICADOR, comunicado("Aviso", "Texto"));

        assertThrows(PublicacaoInvalidaException.class, () -> original.comDados(comunicado("", "Texto")));
    }
}
