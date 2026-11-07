package informaci.controle;

import informaci.entidade.DadosDoUsuario;
import informaci.entidade.EmailInvalidoException;
import informaci.entidade.LoginInvalidoException;
import informaci.entidade.NomeInvalidoException;
import informaci.entidade.Papel;
import informaci.entidade.SenhaInvalidaException;
import informaci.entidade.Usuario;
import informaci.persistencia.UsuarioRepositorioEmMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorDeUsuariosTest {

    private static final String LOGIN = "anasouza";
    private static final String SENHA = "Senha@123";

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
                "Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertNotNull(usuario.getId());
        assertEquals("Ana Souza", usuario.getNome());
        assertEquals(Papel.MEMBRO, usuario.getPapel());
    }

    @Test
    void deve_persistir_o_usuario_adicionado() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertEquals(1, repositorio.quantidade());
        assertTrue(repositorio.existeComEmail("ana@academico.ufpb.br"));
    }

    @Test
    void deve_adicionar_usuarios_de_papeis_diferentes() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        gerenciador.adicionar("Bruno Lima", "bruno@academico.ufpb.br", LOGIN, SENHA, Papel.ADMINISTRADOR);

        assertEquals(2, repositorio.quantidade());
    }

    @Test
    void deve_rejeitar_quando_email_ja_esta_cadastrado() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertThrows(EmailJaCadastradoException.class,
                () -> gerenciador.adicionar("Outra Pessoa", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_email_duplicado_que_difere_apenas_por_maiusculas() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertThrows(EmailJaCadastradoException.class,
                () -> gerenciador.adicionar("Outra Pessoa", "ANA@Academico.UFPB.BR", LOGIN, SENHA, Papel.MEMBRO));
    }

    @Test
    void nao_deve_persistir_usuario_quando_email_esta_duplicado() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertThrows(EmailJaCadastradoException.class,
                () -> gerenciador.adicionar("Outra Pessoa", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO));
        assertEquals(1, repositorio.quantidade());
    }

    @Test
    void deve_rejeitar_quando_dados_do_usuario_sao_invalidos() {
        assertThrows(NomeInvalidoException.class,
                () -> gerenciador.adicionar("", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO));
    }

    @Test
    void nao_deve_persistir_usuario_quando_dados_sao_invalidos() {
        assertThrows(EmailInvalidoException.class,
                () -> gerenciador.adicionar("Ana Souza", "email-invalido", LOGIN, SENHA, Papel.MEMBRO));

        assertEquals(0, repositorio.quantidade());
    }

    @Test
    void deve_guardar_login_e_senha_do_usuario_adicionado() {
        Usuario usuario = gerenciador.adicionar(
                "Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertEquals(LOGIN, usuario.getLogin());
        assertEquals(SENHA, usuario.getSenha());
    }

    @Test
    void deve_rejeitar_quando_login_e_invalido() {
        assertThrows(LoginInvalidoException.class,
                () -> gerenciador.adicionar(
                        "Ana Souza", "ana@academico.ufpb.br", "ana123", SENHA, Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_senha_e_invalida() {
        assertThrows(SenhaInvalidaException.class,
                () -> gerenciador.adicionar(
                        "Ana Souza", "ana@academico.ufpb.br", LOGIN, "fraca", Papel.MEMBRO));
    }

    @Test
    void nao_deve_persistir_usuario_quando_login_ou_senha_sao_invalidos() {
        assertThrows(LoginInvalidoException.class,
                () -> gerenciador.adicionar(
                        "Ana Souza", "ana@academico.ufpb.br", "", SENHA, Papel.MEMBRO));
        assertThrows(SenhaInvalidaException.class,
                () -> gerenciador.adicionar(
                        "Ana Souza", "ana@academico.ufpb.br", LOGIN, null, Papel.MEMBRO));

        assertEquals(0, repositorio.quantidade());
    }

    @Test
    void deve_rejeitar_quando_repositorio_e_nulo() {
        assertThrows(NullPointerException.class, () -> new GerenciadorDeUsuarios(null));
    }

    @Test
    void deve_listar_todos_os_usuarios_cadastrados() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        gerenciador.adicionar("Bruno Lima", "bruno@academico.ufpb.br", LOGIN, SENHA, Papel.ADMINISTRADOR);
    
        java.util.List<Usuario> lista = gerenciador.listarTodos();
    
        assertEquals(2, lista.size());
        assertEquals("Ana Souza", lista.get(0).getNome());
        assertEquals("Bruno Lima", lista.get(1).getNome());
    }

    @Test
    void deve_encontrar_usuario_pelo_email_sem_diferenciar_maiusculas() {
        Usuario ana = gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertEquals(ana, gerenciador.buscarPorEmail("  ANA@Academico.UFPB.br "));
    }

    @Test
    void deve_recusar_busca_por_email_nao_cadastrado() {
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> gerenciador.buscarPorEmail("ninguem@academico.ufpb.br"));
    }

    @Test
    void deve_contar_os_usuarios_cadastrados() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertEquals(1, gerenciador.quantidade());
    }

    @Test
    void deve_editar_mantendo_identidade_e_posicao() {
        Usuario ana = gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        gerenciador.adicionar("Bruno Lima", "bruno@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        Usuario editada = gerenciador.editar(ana.getId(),
                new DadosDoUsuario("Ana Souza Lima", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.PUBLICADOR));

        assertEquals(ana, editada);
        assertEquals("Ana Souza Lima", gerenciador.listarTodos().get(0).getNome());
        assertEquals(Papel.PUBLICADOR, gerenciador.listarTodos().get(0).getPapel());
    }

    @Test
    void deve_recusar_editar_para_email_de_outro_usuario() {
        Usuario ana = gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        gerenciador.adicionar("Bruno Lima", "bruno@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertThrows(EmailJaCadastradoException.class, () -> gerenciador.editar(ana.getId(),
                new DadosDoUsuario("Ana Souza", "BRUNO@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO)));
        assertEquals("ana@academico.ufpb.br", gerenciador.buscarPorId(ana.getId()).getEmail());
    }

    @Test
    void nao_deve_alterar_o_usuario_quando_a_edicao_e_invalida() {
        Usuario ana = gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertThrows(LoginInvalidoException.class, () -> gerenciador.editar(ana.getId(),
                new DadosDoUsuario("Ana Souza", "ana@academico.ufpb.br", "ana123", SENHA, Papel.MEMBRO)));
        assertEquals(LOGIN, gerenciador.buscarPorId(ana.getId()).getLogin());
    }

    @Test
    void deve_remover_o_usuario() {
        Usuario ana = gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        gerenciador.remover(ana.getId());

        assertEquals(0, gerenciador.quantidade());
    }

    @Test
    void deve_recusar_remover_usuario_inexistente() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> gerenciador.remover(java.util.UUID.randomUUID()));
    }
}
