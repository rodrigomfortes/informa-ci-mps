package informaci.entidade;

/**
 * Sinaliza que o login informado viola alguma regra de formato.
 *
 * <p>Estende {@link IllegalArgumentException} para continuar coerente com as
 * demais validações de {@link Usuario}: quem já trata dados inválidos de forma
 * genérica segue funcionando, e quem precisa distinguir o campo com problema
 * pode capturar este tipo específico.
 */
public class LoginInvalidoException extends IllegalArgumentException {

    public LoginInvalidoException(String mensagem) {
        super(mensagem);
    }
}
