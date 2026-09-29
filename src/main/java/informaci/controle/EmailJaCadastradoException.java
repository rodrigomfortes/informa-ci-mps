package informaci.controle;

/**
 * Sinaliza tentativa de cadastrar um e-mail que já pertence a outro usuário.
 *
 * <p>É uma violação de regra de negócio, não um erro de programação: quem
 * chama decide como tratar (exibir mensagem, pedir outro e-mail, etc.).
 */
public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("Já existe um usuário cadastrado com o e-mail " + email + ".");
    }
}
