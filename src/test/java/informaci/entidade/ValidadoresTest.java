package informaci.entidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Exercita os validadores isoladamente, sem construir um {@link Usuario}.
 * As regras completas continuam cobertas por cenário em {@link UsuarioTest}.
 */
class ValidadoresTest {

    @Test
    void deve_recusar_nome_em_branco_com_mensagem_do_rf01() {
        NomeInvalidoException erro = assertThrows(NomeInvalidoException.class, () -> ValidadorDeNome.validar("  "));

        assertEquals("O nome do usuário é obrigatório.", erro.getMessage());
    }

    @Test
    void deve_recusar_email_em_formato_invalido_com_mensagem_do_rf01() {
        EmailInvalidoException erro =
                assertThrows(EmailInvalidoException.class, () -> ValidadorDeEmail.validar("email-invalido"));

        assertEquals("E-mail em formato inválido: email-invalido", erro.getMessage());
    }

    @Test
    void deve_normalizar_email_sem_validar_o_formato() {
        assertEquals("ana@ufpb.br", ValidadorDeEmail.normalizar("  ANA@UFPB.br "));
        assertNull(ValidadorDeEmail.normalizar(null));
    }

    @Test
    void deve_validar_login_isoladamente() {
        assertEquals("anasouza", ValidadorDeLogin.validar(" anasouza "));
        assertThrows(LoginInvalidoException.class, () -> ValidadorDeLogin.validar("ana123"));
    }

    @Test
    void deve_validar_senha_isoladamente() {
        assertEquals("Senha@123", ValidadorDeSenha.validar("Senha@123", "anasouza", "ana@ufpb.br"));
        assertThrows(SenhaInvalidaException.class, () -> ValidadorDeSenha.validar("anasouza", "anasouza", "ana@ufpb.br"));
    }
}
