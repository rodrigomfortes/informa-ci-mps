package informaci.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import informaci.entidade.Papel;
import informaci.entidade.Usuario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class UsuarioRepositorioEmArquivoTest {

    @TempDir
    Path pasta;

    private Usuario novoUsuario() {
        return new Usuario("Maria", "maria@ufpb.br", Papel.MEMBRO);
    }

    @Test
    void comecaVazioQuandoOArquivoNaoExiste() {
        var repositorio = new UsuarioRepositorioEmArquivo(pasta.resolve("usuarios.bin"));

        assertTrue(repositorio.listarTodos().isEmpty());
    }

    @Test
    void dadosSobrevivemAReaberturaDoRepositorio() {
        Path arquivo = pasta.resolve("usuarios.bin");
        Usuario usuario = novoUsuario();
        new UsuarioRepositorioEmArquivo(arquivo).salvar(usuario);

        var reaberto = new UsuarioRepositorioEmArquivo(arquivo);

        assertEquals(1, reaberto.listarTodos().size());
        assertEquals(usuario, reaberto.listarTodos().get(0));
        assertTrue(reaberto.existeComEmail("maria@ufpb.br"));
        assertFalse(reaberto.existeComEmail("outro@ufpb.br"));
    }

    @Test
    void arquivoCorrompidoLancaPersistenciaException() throws IOException {
        Path arquivo = pasta.resolve("usuarios.bin");
        Files.writeString(arquivo, "isto não é um arquivo serializado");

        assertThrows(PersistenciaException.class, () -> new UsuarioRepositorioEmArquivo(arquivo));
    }

    @Test
    void falhaDeGravacaoLancaPersistenciaException() {
        var repositorio = new UsuarioRepositorioEmArquivo(pasta.resolve("inexistente/usuarios.bin"));

        assertThrows(PersistenciaException.class, () -> repositorio.salvar(novoUsuario()));
    }
}
