package informaci.entidade;

/**
 * Dados editáveis de um {@link Usuario}, agrupados num único parâmetro para a
 * edição. Não valida nada: quem garante as regras é {@link Usuario}.
 */
public record DadosDoUsuario(String nome, String email, String login, String senha, Papel papel) {

    /** Os dados atuais do usuário, ponto de partida de uma edição. */
    public static DadosDoUsuario de(Usuario usuario) {
        return new DadosDoUsuario(usuario.getNome(), usuario.getEmail(), usuario.getLogin(),
                usuario.getSenha(), usuario.getPapel());
    }
}
