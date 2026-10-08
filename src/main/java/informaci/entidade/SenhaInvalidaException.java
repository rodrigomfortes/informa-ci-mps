package informaci.entidade;

/**
 * Sinaliza que a senha informada não atende à política de senhas.
 *
 * <p>A mensagem descreve a regra violada, mas nunca reproduz a senha: exceções
 * costumam parar em logs e telas de erro, onde ela ficaria exposta.
 */
public class SenhaInvalidaException extends IllegalArgumentException {

    public SenhaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
