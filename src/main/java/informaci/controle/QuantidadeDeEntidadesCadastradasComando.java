package informaci.controle;

/** Comando que informa o total de entidades cadastradas: usuários mais publicações. */
final class QuantidadeDeEntidadesCadastradasComando implements Comando<Integer> {

    private final GerenciadorDeUsuarios usuarios;
    private final GerenciadorDePublicacoes publicacoes;

    QuantidadeDeEntidadesCadastradasComando(GerenciadorDeUsuarios usuarios, GerenciadorDePublicacoes publicacoes) {
        this.usuarios = usuarios;
        this.publicacoes = publicacoes;
    }

    @Override
    public Integer executar() {
        return usuarios.quantidade() + publicacoes.quantidade();
    }
}
