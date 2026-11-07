package informaci.controle;

import java.util.UUID;

/**
 * Sinaliza que a publicação procurada não está cadastrada, por exemplo porque
 * foi removida entre a listagem e a edição.
 */
public class PublicacaoNaoEncontradaException extends RuntimeException {

    public PublicacaoNaoEncontradaException(UUID id) {
        super("Publicação não encontrada: " + id + ".");
    }
}
