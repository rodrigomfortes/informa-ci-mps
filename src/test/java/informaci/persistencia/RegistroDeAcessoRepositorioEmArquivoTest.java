package informaci.persistencia;

import informaci.entidade.RegistroDeAcesso;
import informaci.entidade.TipoDeOperacao;
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

class RegistroDeAcessoRepositorioEmArquivoTest {

    private static final RegistroDeAcesso CADASTRO = new RegistroDeAcesso(
            LocalDateTime.of(2026, 11, 12, 10, 0), TipoDeOperacao.CADASTRO_DE_USUARIO, "ana");
    private static final RegistroDeAcesso FEED = new RegistroDeAcesso(
            LocalDateTime.of(2026, 11, 12, 10, 5), TipoDeOperacao.CONSULTA_AO_FEED, null);

    @TempDir
    Path pasta;

    @Test
    void deve_manter_os_acessos_na_ordem_apos_reabrir() {
        Path arquivo = pasta.resolve("acessos.bin");
        RegistroDeAcessoRepositorioEmArquivo repositorio = new RegistroDeAcessoRepositorioEmArquivo(arquivo);
        repositorio.salvar(CADASTRO);
        repositorio.salvar(FEED);

        assertEquals(List.of(CADASTRO, FEED), new RegistroDeAcessoRepositorioEmArquivo(arquivo).listarTodos());
    }

    @Test
    void nao_deve_manter_o_acesso_quando_a_gravacao_falha() {
        RegistroDeAcessoRepositorioEmArquivo repositorio =
                new RegistroDeAcessoRepositorioEmArquivo(pasta.resolve("inexistente/acessos.bin"));

        assertThrows(PersistenciaException.class, () -> repositorio.salvar(CADASTRO));
        assertTrue(repositorio.listarTodos().isEmpty());
    }

    @Test
    void deve_lancar_persistencia_exception_quando_arquivo_esta_corrompido() throws IOException {
        Path arquivo = pasta.resolve("acessos.bin");
        Files.writeString(arquivo, "isto não é um arquivo serializado");

        assertThrows(PersistenciaException.class, () -> new RegistroDeAcessoRepositorioEmArquivo(arquivo));
    }

    @Test
    void repositorio_em_memoria_deve_guardar_na_ordem() {
        RegistroDeAcessoRepositorioEmMemoria repositorio = new RegistroDeAcessoRepositorioEmMemoria();
        repositorio.salvar(CADASTRO);
        repositorio.salvar(FEED);

        assertEquals(List.of(CADASTRO, FEED), repositorio.listarTodos());
        assertThrows(NullPointerException.class, () -> repositorio.salvar(null));
    }
}
