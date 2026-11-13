package informaci.entidade;

/**
 * Operação do sistema que conta como um acesso (RF12).
 */
public enum TipoDeOperacao {

    CADASTRO_DE_USUARIO("Cadastro de usuário"),
    LISTAGEM_DE_USUARIOS("Listagem de usuários"),
    EDICAO_DE_USUARIO("Edição de usuário"),
    REMOCAO_DE_USUARIO("Remoção de usuário"),
    PUBLICACAO("Publicação"),
    CONSULTA_AO_FEED("Consulta ao feed"),
    EDICAO_DE_PUBLICACAO("Edição de publicação"),
    REMOCAO_DE_PUBLICACAO("Remoção de publicação");

    private final String descricao;

    TipoDeOperacao(String descricao) {
        this.descricao = descricao;
    }

    /** Nome da operação como aparece nos relatórios. */
    public String getDescricao() {
        return descricao;
    }
}
