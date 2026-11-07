package informaci.entidade;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Regras do e-mail do usuário (RF01): obrigatório, em formato válido e
 * normalizado para minúsculas.
 */
public final class ValidadorDeEmail {

    private static final Pattern FORMATO_DE_EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private ValidadorDeEmail() {
    }

    /**
     * @return o e-mail normalizado
     * @throws EmailInvalidoException se o e-mail estiver vazio ou em formato inválido
     */
    public static String validar(String email) {
        if (email == null || email.isBlank()) {
            throw new EmailInvalidoException("O e-mail do usuário é obrigatório.");
        }
        String normalizado = normalizar(email);
        if (!FORMATO_DE_EMAIL.matcher(normalizado).matches()) {
            throw new EmailInvalidoException("E-mail em formato inválido: " + email);
        }
        return normalizado;
    }

    /**
     * Remove os espaços em volta e passa para minúsculas.
     *
     * <p>O e-mail identifica o usuário: sem a normalização, {@code Joao@ufpb.br}
     * e {@code joao@ufpb.br} seriam pessoas diferentes. Quem procura um usuário
     * pelo e-mail deve usar esta mesma regra.
     *
     * @return o e-mail normalizado, ou {@code null} se o e-mail for nulo
     */
    public static String normalizar(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
