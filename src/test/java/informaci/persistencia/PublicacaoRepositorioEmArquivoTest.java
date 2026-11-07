package informaci.persistencia;

import informaci.entidade.Categoria;
import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Papel;
import informaci.entidade.Publicacao;
import informaci.entidade.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicacaoRepositorioEmArquivoTest {

    private static final Usuario PUBLICADOR = new Usuario(
            "Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", "Senha@123", Papel.PUBLICADOR);

    @TempDir
    Path pasta;

    private Path arquivo;

    @BeforeEach
    void definirArquivoNaPastaTemporaria() {
        arquivo = pasta.resolve("publicacoes.bin");
    }

    private static Publicacao palestra(String titulo) {
        return new Publicacao(PUBLICADOR, new DadosDaPublicacao(titulo, "Palestra aberta.", Categoria.PALESTRA,
                LocalDateTime.of(2026, 11, 20, 14, 0), "Auditório do CI"));
    }

    @Test
    void deve_iniciar_vazio_quando_arquivo_nao_existe() {
        assertTrue(new PublicacaoRepositorioEmArquivo(arquivo).listarTodas().isEmpty());
    }

    @Test
    void deve_manter_as_publicacoes_apos_reabrir_o_repositorio() {
        Publicacao primeira = palestra("Introdução a Rust");
        Publicacao segunda = palestra("Testes com JUnit");
        PublicacaoRepositorioEmArquivo repositorio = new PublicacaoRepositorioEmArquivo(arquivo);
        repositorio.salvar(primeira);
        repositorio.salvar(segunda);

        List<Publicacao> lidas = new PublicacaoRepositorioEmArquivo(arquivo).listarTodas();

        assertEquals(List.of(primeira, segunda), lidas);
        assertEquals("Auditório do CI", lidas.get(0).getLocal().orElseThrow());
        assertEquals(PUBLICADOR, lidas.get(0).getAutor());
    }

    @Test
    void deve_manter_a_remocao_apos_reabrir_o_repositorio() {
        Publicacao publicacao = palestra("Introdução a Rust");
        PublicacaoRepositorioEmArquivo repositorio = new PublicacaoRepositorioEmArquivo(arquivo);
        repositorio.salvar(publicacao);

        repositorio.remover(publicacao.getId());

        assertTrue(new PublicacaoRepositorioEmArquivo(arquivo).listarTodas().isEmpty());
    }

    @Test
    void deve_lancar_persistencia_exception_quando_arquivo_esta_corrompido() throws IOException {
        Files.writeString(arquivo, "isto não é um arquivo serializado");

        assertThrows(PersistenciaException.class, () -> new PublicacaoRepositorioEmArquivo(arquivo));
    }

    @Test
    void nao_deve_manter_a_publicacao_quando_a_gravacao_falha() {
        PublicacaoRepositorioEmArquivo repositorio =
                new PublicacaoRepositorioEmArquivo(pasta.resolve("inexistente/publicacoes.bin"));

        assertThrows(PersistenciaException.class, () -> repositorio.salvar(palestra("Introdução a Rust")));

        assertTrue(repositorio.listarTodas().isEmpty());
    }
}
