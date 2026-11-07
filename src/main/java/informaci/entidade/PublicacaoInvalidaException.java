package informaci.entidade;

/**
 * Sinaliza que os dados de uma {@link Publicacao} violam alguma regra.
 *
 * <p>Estende {@link IllegalArgumentException}, como as demais exceções de
 * validação de entidade, para que quem trata dados inválidos de forma
 * genérica continue funcionando.
 */
public class PublicacaoInvalidaException extends IllegalArgumentException {

    public PublicacaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
