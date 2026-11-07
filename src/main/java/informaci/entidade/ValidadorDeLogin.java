package informaci.entidade;

/**
 * Regras do login (RF02): obrigatório, com no máximo
 * {@value #TAMANHO_MAXIMO} caracteres e sem números.
 */
public final class ValidadorDeLogin {

    static final int TAMANHO_MAXIMO = 12;

    private ValidadorDeLogin() {
    }

    /**
     * @return o login sem espaços em volta
     * @throws LoginInvalidoException se o login violar alguma regra
     */
    public static String validar(String login) {
        if (login == null || login.isBlank()) {
            throw new LoginInvalidoException("O login é obrigatório.");
        }
        String semEspacos = login.trim();
        if (semEspacos.length() > TAMANHO_MAXIMO) {
            throw new LoginInvalidoException(
                    "O login deve ter no máximo " + TAMANHO_MAXIMO + " caracteres.");
        }
        if (semEspacos.chars().anyMatch(Character::isDigit)) {
            throw new LoginInvalidoException("O login não pode conter números.");
        }
        return semEspacos;
    }
}
