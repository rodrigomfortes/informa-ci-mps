package informaci.controle;

/**
 * Sinaliza que não existe usuário cadastrado com o e-mail procurado.
 */
public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(String email) {
        super("Não existe usuário cadastrado com o e-mail " + email + ".");
    }
}
