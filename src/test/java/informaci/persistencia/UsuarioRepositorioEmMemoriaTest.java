package informaci.persistencia;

import informaci.entidade.Papel;
import informaci.entidade.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioRepositorioEmMemoriaTest {

    private static final String LOGIN = "anasouza";
    private static final String SENHA = "Senha@123";

    private UsuarioRepositorioEmMemoria repositorio;

    @BeforeEach
    void criarRepositorioVazio() {
        repositorio = new UsuarioRepositorioEmMemoria();
    }

    @Test
    void deve_iniciar_vazio() {
        assertEquals(0, repositorio.quantidade());
    }

    @Test
    void deve_armazenar_o_usuario_salvo() {
        repositorio.salvar(new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO));

        assertEquals(1, repositorio.quantidade());
    }

    @Test
    void deve_rejeitar_quando_usuario_e_nulo() {
        assertThrows(NullPointerException.class, () -> repositorio.salvar(null));
    }

    @Test
    void deve_encontrar_email_de_usuario_ja_salvo() {
        repositorio.salvar(new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO));

        assertTrue(repositorio.existeComEmail("ana@academico.ufpb.br"));
    }

    @Test
    void deve_informar_que_email_nao_existe_quando_repositorio_esta_vazio() {
        assertFalse(repositorio.existeComEmail("ana@academico.ufpb.br"));
    }

    @Test
    void deve_informar_que_email_nao_existe_quando_email_e_nulo() {
        assertFalse(repositorio.existeComEmail(null));
    }

    @Test
    void deve_armazenar_usuarios_distintos_separadamente() {
        repositorio.salvar(new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO));
        repositorio.salvar(new Usuario("Bruno Lima", "bruno@academico.ufpb.br", LOGIN, SENHA, Papel.ADMINISTRADOR));

        assertEquals(2, repositorio.quantidade());
    }
}
