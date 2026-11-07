package informaci.entidade;

/**
 * Sinaliza que o nome informado para o usuário é inválido.
 *
 * <p>Como as demais exceções de validação, estende
 * {@link IllegalArgumentException}: quem trata dados inválidos de forma
 * genérica segue funcionando, e quem precisa saber qual campo falhou captura
 * este tipo específico.
 */
public class NomeInvalidoException extends IllegalArgumentException {

    public NomeInvalidoException(String mensagem) {
        super(mensagem);
    }
}
