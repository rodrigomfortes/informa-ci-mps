package informaci.entidade;

/**
 * Regras da senha (RF03), segundo a política padrão do AWS IAM: de
 * {@value #TAMANHO_MINIMO} a {@value #TAMANHO_MAXIMO} caracteres, combinando ao
 * menos {@value #TIPOS_DE_CARACTERE_EXIGIDOS} dos quatro tipos (maiúsculas,
 * minúsculas, números e símbolos), e diferente do login e do e-mail da conta.
 *
 * <p>A mensagem de erro descreve a regra violada, mas nunca reproduz a senha
 * (NF-SEG-01).
 */
public final class ValidadorDeSenha {

    static final int TAMANHO_MINIMO = 8;
    static final int TAMANHO_MAXIMO = 128;
    static final int TIPOS_DE_CARACTERE_EXIGIDOS = 3;
    static final String SIMBOLOS = "!@#$%^&*()_+-=[]{}|'";

    private ValidadorDeSenha() {
    }

    /**
     * Recebe o login e o e-mail já validados porque a última regra depende
     * deles. A senha não passa por {@code trim}: espaços são caracteres
     * legítimos e removê-los alteraria o que o usuário digitou.
     *
     * @return a senha, inalterada
     * @throws SenhaInvalidaException se a senha violar alguma regra
     */
    public static String validar(String senha, String login, String email) {
        if (senha == null || senha.isEmpty()) {
            throw new SenhaInvalidaException("A senha é obrigatória.");
        }
        if (senha.length() < TAMANHO_MINIMO || senha.length() > TAMANHO_MAXIMO) {
            throw new SenhaInvalidaException("A senha deve ter entre "
                    + TAMANHO_MINIMO + " e " + TAMANHO_MAXIMO + " caracteres.");
        }
        if (contarTiposDeCaractere(senha) < TIPOS_DE_CARACTERE_EXIGIDOS) {
            throw new SenhaInvalidaException("A senha deve combinar ao menos "
                    + TIPOS_DE_CARACTERE_EXIGIDOS + " destes tipos de caractere: letras maiúsculas, "
                    + "letras minúsculas, números e símbolos (" + SIMBOLOS + ").");
        }
        if (senha.equalsIgnoreCase(login) || senha.equalsIgnoreCase(email)) {
            throw new SenhaInvalidaException("A senha não pode ser igual ao login nem ao e-mail.");
        }
        return senha;
    }

    private static int contarTiposDeCaractere(String senha) {
        int tipos = 0;
        if (senha.chars().anyMatch(c -> c >= 'A' && c <= 'Z')) {
            tipos++;
        }
        if (senha.chars().anyMatch(c -> c >= 'a' && c <= 'z')) {
            tipos++;
        }
        if (senha.chars().anyMatch(c -> c >= '0' && c <= '9')) {
            tipos++;
        }
        if (senha.chars().anyMatch(c -> SIMBOLOS.indexOf(c) >= 0)) {
            tipos++;
        }
        return tipos;
    }
}
