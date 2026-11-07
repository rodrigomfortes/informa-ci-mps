package informaci.entidade;

/**
 * Sinaliza que o e-mail informado para o usuário está vazio ou em formato inválido.
 *
 * <p>Estende {@link IllegalArgumentException}, como as demais exceções de validação.
 */
public class EmailInvalidoException extends IllegalArgumentException {

    public EmailInvalidoException(String mensagem) {
        super(mensagem);
    }
}
