package informaci.entidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioTest {

    private static final String LOGIN = "anasouza";
    private static final String SENHA = "Senha@123";

    private static Usuario usuarioComLogin(String login) {
        return new Usuario("Ana Souza", "ana@academico.ufpb.br", login, SENHA, Papel.MEMBRO);
    }

    private static Usuario usuarioComSenha(String senha) {
        return new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, senha, Papel.MEMBRO);
    }

    @Test
    void deve_criar_usuario_quando_dados_sao_validos() {
        Usuario usuario = new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertNotNull(usuario.getId());
        assertEquals("Ana Souza", usuario.getNome());
        assertEquals("ana@academico.ufpb.br", usuario.getEmail());
        assertEquals(LOGIN, usuario.getLogin());
        assertEquals(SENHA, usuario.getSenha());
        assertEquals(Papel.MEMBRO, usuario.getPapel());
    }

    @Test
    void deve_rejeitar_quando_login_e_nulo() {
        assertThrows(LoginInvalidoException.class, () -> usuarioComLogin(null));
    }

    @Test
    void deve_rejeitar_quando_login_esta_vazio() {
        assertThrows(LoginInvalidoException.class, () -> usuarioComLogin(""));
    }

    @Test
    void deve_rejeitar_quando_login_esta_em_branco() {
        assertThrows(LoginInvalidoException.class, () -> usuarioComLogin("   "));
    }

    @Test
    void deve_rejeitar_quando_login_tem_mais_de_12_caracteres() {
        assertThrows(LoginInvalidoException.class, () -> usuarioComLogin("abcdefghijklm"));
    }

    @Test
    void deve_aceitar_login_com_exatamente_12_caracteres() {
        assertEquals("abcdefghijkl", usuarioComLogin("abcdefghijkl").getLogin());
    }

    @Test
    void deve_rejeitar_quando_login_tem_numeros() {
        assertThrows(LoginInvalidoException.class, () -> usuarioComLogin("ana123"));
    }

    @Test
    void deve_remover_espacos_em_volta_do_login() {
        assertEquals("anasouza", usuarioComLogin("  anasouza  ").getLogin());
    }

    @Test
    void deve_rejeitar_quando_senha_e_nula() {
        assertThrows(SenhaInvalidaException.class, () -> usuarioComSenha(null));
    }

    @Test
    void deve_rejeitar_quando_senha_esta_vazia() {
        assertThrows(SenhaInvalidaException.class, () -> usuarioComSenha(""));
    }

    @Test
    void deve_rejeitar_quando_senha_tem_menos_de_8_caracteres() {
        assertThrows(SenhaInvalidaException.class, () -> usuarioComSenha("Sen@123"));
    }

    @Test
    void deve_aceitar_senha_com_exatamente_8_caracteres() {
        assertEquals("Senh@123", usuarioComSenha("Senh@123").getSenha());
    }

    @Test
    void deve_rejeitar_quando_senha_tem_mais_de_128_caracteres() {
        String senha = "Aa1@" + "x".repeat(125);

        assertThrows(SenhaInvalidaException.class, () -> usuarioComSenha(senha));
    }

    @Test
    void deve_aceitar_senha_com_exatamente_128_caracteres() {
        String senha = "Aa1@" + "x".repeat(124);

        assertEquals(senha, usuarioComSenha(senha).getSenha());
    }

    @Test
    void deve_rejeitar_quando_senha_tem_apenas_um_tipo_de_caractere() {
        assertThrows(SenhaInvalidaException.class, () -> usuarioComSenha("senhafraca"));
    }

    @Test
    void deve_rejeitar_quando_senha_tem_apenas_dois_tipos_de_caractere() {
        assertThrows(SenhaInvalidaException.class, () -> usuarioComSenha("senhafraca123"));
    }

    @Test
    void deve_aceitar_senha_com_tres_tipos_de_caractere() {
        assertEquals("SenhaForte123", usuarioComSenha("SenhaForte123").getSenha());
        assertEquals("senha@forte123", usuarioComSenha("senha@forte123").getSenha());
        assertEquals("Senha@Forte", usuarioComSenha("Senha@Forte").getSenha());
        assertEquals("SENHA@123", usuarioComSenha("SENHA@123").getSenha());
    }

    @Test
    void deve_rejeitar_quando_senha_e_igual_ao_login() {
        assertThrows(SenhaInvalidaException.class,
                () -> new Usuario("Ana Souza", "ana@academico.ufpb.br", "Ana-Souza", "Ana-Souza", Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_senha_e_igual_ao_email() {
        assertThrows(SenhaInvalidaException.class,
                () -> new Usuario("Ana Souza", "Ana1@ufpb.br", LOGIN, "Ana1@ufpb.br", Papel.MEMBRO));
    }

    @Test
    void nao_deve_expor_a_senha_na_representacao_textual() {
        assertFalse(usuarioComSenha(SENHA).toString().contains(SENHA));
    }

    @Test
    void deve_rejeitar_quando_nome_e_nulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario(null, "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_nome_esta_em_branco() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario("   ", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_email_e_nulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario("Ana Souza", null, LOGIN, SENHA, Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_email_nao_tem_arroba() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario("Ana Souza", "ana.academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_email_nao_tem_dominio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario("Ana Souza", "ana@ufpb", LOGIN, SENHA, Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_papel_e_nulo() {
        assertThrows(NullPointerException.class,
                () -> new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, null));
    }

    @Test
    void deve_remover_espacos_em_volta_do_nome() {
        Usuario usuario = new Usuario("  Ana Souza  ", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertEquals("Ana Souza", usuario.getNome());
    }

    @Test
    void deve_normalizar_email_para_minusculas() {
        Usuario usuario = new Usuario("Ana Souza", "  ANA@Academico.UFPB.br ", LOGIN, SENHA, Papel.MEMBRO);

        assertEquals("ana@academico.ufpb.br", usuario.getEmail());
    }

    @Test
    void deve_tratar_como_diferentes_dois_usuarios_com_os_mesmos_dados() {
        Usuario primeiro = new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);
        Usuario segundo = new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertNotEquals(primeiro, segundo);
    }

    @Test
    void deve_tratar_como_igual_o_usuario_comparado_consigo_mesmo() {
        Usuario usuario = new Usuario("Ana Souza", "ana@academico.ufpb.br", LOGIN, SENHA, Papel.MEMBRO);

        assertEquals(usuario, usuario);
        assertEquals(usuario.hashCode(), usuario.hashCode());
    }
}
