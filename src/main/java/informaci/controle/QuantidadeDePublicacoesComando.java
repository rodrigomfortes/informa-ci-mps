package informaci.controle;

/** Comando que informa quantas publicações estão cadastradas. */
final class QuantidadeDePublicacoesComando implements Comando<Integer> {

    private final GerenciadorDePublicacoes publicacoes;

    QuantidadeDePublicacoesComando(GerenciadorDePublicacoes publicacoes) {
        this.publicacoes = publicacoes;
    }

    @Override
    public Integer executar() {
        return publicacoes.quantidade();
    }
}
