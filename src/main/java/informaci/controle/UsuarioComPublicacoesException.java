package informaci.controle;

/**
 * Sinaliza a tentativa de remover um usuário que é autor de publicações.
 *
 * <p>Removê-lo deixaria publicações sem autor, e toda publicação identifica
 * quem a criou (glossário: Publicação).
 */
public class UsuarioComPublicacoesException extends RuntimeException {

    public UsuarioComPublicacoesException(String login, int quantidadeDePublicacoes) {
        super("O usuário " + login + " é autor de " + quantidadeDePublicacoes
                + " publicação(ões); remova-as antes de remover o usuário.");
    }
}
