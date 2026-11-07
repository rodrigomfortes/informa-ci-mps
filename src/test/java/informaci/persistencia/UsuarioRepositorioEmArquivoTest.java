package informaci.persistencia;

import informaci.entidade.DadosDoUsuario;
import informaci.entidade.Papel;
import informaci.entidade.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioRepositorioEmArquivoTest {

    private static final String LOGIN = "anasouza";
    private static final String SENHA = "Senha@123";

    @TempDir
    Path pasta;

    private Path arquivo;

    @BeforeEach
    void definirArquivoNaPastaTemporaria() {
        arquivo = pasta.resolve("usuarios.bin");
    }

    @Test
    void deve_iniciar_vazio_quando_arquivo_nao_existe() {
        UsuarioRepositorioEmArquivo repositorio = new UsuarioRepositorioEmArquivo(arquivo);

        assertTrue(repositorio.listarTodos().isEmpty());
    }

    @Test
    void deve_manter_os_usuarios_apos_reabrir_o_repositorio() {
        Usuario usuario = new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        new UsuarioRepositorioEmArquivo(arquivo).salvar(usuario);

        UsuarioRepositorioEmArquivo reaberto = new UsuarioRepositorioEmArquivo(arquivo);

        assertEquals(1, reaberto.listarTodos().size());
        assertEquals(usuario, reaberto.listarTodos().get(0));
        assertTrue(reaberto.existeComEmail("ana@academico.ufpb.br"));
        assertFalse(reaberto.existeComEmail("outro@academico.ufpb.br"));
    }

    @Test
    void deve_rejeitar_quando_usuario_e_nulo() {
        UsuarioRepositorioEmArquivo repositorio = new UsuarioRepositorioEmArquivo(arquivo);

        assertThrows(NullPointerException.class, () -> repositorio.salvar(null));
    }

    @Test
    void deve_informar_que_email_nao_existe_quando_email_e_nulo() {
        UsuarioRepositorioEmArquivo repositorio = new UsuarioRepositorioEmArquivo(arquivo);

        assertFalse(repositorio.existeComEmail(null));
    }

    @Test
    void deve_lancar_persistencia_exception_quando_arquivo_esta_corrompido() throws IOException {
        Files.writeString(arquivo, "isto não é um arquivo serializado");

        assertThrows(PersistenciaException.class, () -> new UsuarioRepositorioEmArquivo(arquivo));
    }

    @Test
    void deve_lancar_persistencia_exception_quando_nao_consegue_gravar() {
        UsuarioRepositorioEmArquivo repositorio =
                new UsuarioRepositorioEmArquivo(pasta.resolve("inexistente/usuarios.bin"));
        Usuario usuario = new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertThrows(PersistenciaException.class, () -> repositorio.salvar(usuario));
    }

    @Test
    void nao_deve_manter_o_usuario_quando_a_gravacao_falha() {
        UsuarioRepositorioEmArquivo repositorio =
                new UsuarioRepositorioEmArquivo(pasta.resolve("inexistente/usuarios.bin"));
        Usuario usuario = new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertThrows(PersistenciaException.class, () -> repositorio.salvar(usuario));

        assertTrue(repositorio.listarTodos().isEmpty());
        assertFalse(repositorio.existeComEmail("ana@academico.ufpb.br"));
    }

    @Test
    void deve_regravar_o_arquivo_sem_deixar_arquivos_temporarios() throws IOException {
        Usuario ana = new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        Usuario bruno = new Usuario("Bruno Lima", "bruno@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        UsuarioRepositorioEmArquivo repositorio = new UsuarioRepositorioEmArquivo(arquivo);
        repositorio.salvar(ana);
        repositorio.salvar(bruno);

        UsuarioRepositorioEmArquivo reaberto = new UsuarioRepositorioEmArquivo(arquivo);

        assertEquals(List.of(ana, bruno), reaberto.listarTodos());
        try (Stream<Path> arquivosDaPasta = Files.list(pasta)) {
            assertEquals(1, arquivosDaPasta.count());
        }
    }

    @Test
    void deve_manter_a_edicao_e_a_remocao_apos_reabrir_o_repositorio() {
        Usuario ana = new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        Usuario bruno = new Usuario("Bruno Lima", "bruno@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        UsuarioRepositorioEmArquivo repositorio = new UsuarioRepositorioEmArquivo(arquivo);
        repositorio.salvar(ana);
        repositorio.salvar(bruno);

        repositorio.salvar(ana.comDados(
                new DadosDoUsuario("Ana Lima", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.PUBLICADOR)));
        repositorio.remover(bruno.getId());

        List<Usuario> lidos = new UsuarioRepositorioEmArquivo(arquivo).listarTodos();
        assertEquals(1, lidos.size());
        assertEquals("Ana Lima", lidos.get(0).getNome());
    }
}
