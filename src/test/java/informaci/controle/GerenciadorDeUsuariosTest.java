package informaci.controle;

import informaci.entidade.Papel;
import informaci.entidade.Usuario;
import informaci.persistencia.UsuarioRepositorioEmMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorDeUsuariosTest {

    private UsuarioRepositorioEmMemoria repositorio;
    private GerenciadorDeUsuarios gerenciador;

    @BeforeEach
    void criarGerenciadorComRepositorioVazio() {
        repositorio = new UsuarioRepositorioEmMemoria();
        gerenciador = new GerenciadorDeUsuarios(repositorio);
    }

    @Test
    void deve_adicionar_usuario_quando_dados_sao_validos() {
        Usuario usuario = gerenciador.adicionar(
                "Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);

        assertNotNull(usuario.getId());
        assertEquals("Ana Souza", usuario.getNome());
        assertEquals(Papel.MEMBRO, usuario.getPapel());
    }

    @Test
    void deve_persistir_o_usuario_adicionado() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);

        assertEquals(1, repositorio.quantidade());
        assertTrue(repositorio.existeComEmail("ana@academico.ufpb.br"));
    }

    @Test
    void deve_adicionar_usuarios_de_papeis_diferentes() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);
        gerenciador.adicionar("Bruno Lima", "bruno@academico.ufpb.br", Papel.ADMINISTRADOR);

        assertEquals(2, repositorio.quantidade());
    }

    @Test
    void deve_rejeitar_quando_email_ja_esta_cadastrado() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);

        assertThrows(EmailJaCadastradoException.class,
                () -> gerenciador.adicionar("Outra Pessoa", "ana@academico.ufpb.br", Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_email_duplicado_que_difere_apenas_por_maiusculas() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);

        assertThrows(EmailJaCadastradoException.class,
                () -> gerenciador.adicionar("Outra Pessoa", "ANA@Academico.UFPB.BR", Papel.MEMBRO));
    }

    @Test
    void nao_deve_persistir_usuario_quando_email_esta_duplicado() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);

        assertThrows(EmailJaCadastradoException.class,
                () -> gerenciador.adicionar("Outra Pessoa", "ana@academico.ufpb.br", Papel.MEMBRO));
        assertEquals(1, repositorio.quantidade());
    }

    @Test
    void deve_rejeitar_quando_dados_do_usuario_sao_invalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> gerenciador.adicionar("", "ana@academico.ufpb.br", Papel.MEMBRO));
    }

    @Test
    void nao_deve_persistir_usuario_quando_dados_sao_invalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> gerenciador.adicionar("Ana Souza", "email-invalido", Papel.MEMBRO));

        assertEquals(0, repositorio.quantidade());
    }

    @Test
    void deve_rejeitar_quando_repositorio_e_nulo() {
        assertThrows(NullPointerException.class, () -> new GerenciadorDeUsuarios(null));
    }

    @Test
    void deve_listar_todos_os_usuarios_cadastrados() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);
        gerenciador.adicionar("Bruno Lima", "bruno@academico.ufpb.br", Papel.ADMINISTRADOR);
    
        java.util.List<Usuario> lista = gerenciador.listarTodos();
    
        assertEquals(2, lista.size());
        assertEquals("Ana Souza", lista.get(0).getNome());
        assertEquals("Bruno Lima", lista.get(1).getNome());
    }
}
