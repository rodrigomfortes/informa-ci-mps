package informaci.persistencia;

import informaci.entidade.Categoria;
import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Papel;
import informaci.entidade.Publicacao;
import informaci.entidade.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicacaoRepositorioEmMemoriaTest {

    private static final Usuario PUBLICADOR = new Usuario(
            "Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", "Senha@123", Papel.PUBLICADOR);

    private PublicacaoRepositorioEmMemoria repositorio;

    @BeforeEach
    void criarRepositorioVazio() {
        repositorio = new PublicacaoRepositorioEmMemoria();
    }

    private static Publicacao comunicado(String titulo) {
        return new Publicacao(PUBLICADOR,
                new DadosDaPublicacao(titulo, "Texto.", Categoria.COMUNICADO, null, null));
    }

    @Test
    void deve_iniciar_vazio() {
        assertTrue(repositorio.listarTodas().isEmpty());
    }

    @Test
    void deve_listar_na_ordem_de_cadastro() {
        Publicacao primeira = comunicado("Primeira");
        Publicacao segunda = comunicado("Segunda");
        repositorio.salvar(primeira);
        repositorio.salvar(segunda);

        assertEquals(List.of(primeira, segunda), repositorio.listarTodas());
    }

    @Test
    void deve_substituir_a_publicacao_de_mesma_identidade() {
        Publicacao original = comunicado("Original");
        repositorio.salvar(original);

        repositorio.salvar(original.comDados(
                new DadosDaPublicacao("Editada", "Texto.", Categoria.COMUNICADO, null, null)));

        assertEquals(1, repositorio.listarTodas().size());
        assertEquals("Editada", repositorio.buscarPorId(original.getId()).orElseThrow().getTitulo());
    }

    @Test
    void deve_remover_a_publicacao() {
        Publicacao publicacao = comunicado("Aviso");
        repositorio.salvar(publicacao);

        repositorio.remover(publicacao.getId());

        assertTrue(repositorio.buscarPorId(publicacao.getId()).isEmpty());
    }

    @Test
    void deve_devolver_vazio_ao_buscar_identidade_inexistente() {
        assertTrue(repositorio.buscarPorId(UUID.randomUUID()).isEmpty());
    }

    @Test
    void deve_rejeitar_quando_publicacao_e_nula() {
        assertThrows(NullPointerException.class, () -> repositorio.salvar(null));
    }
}
