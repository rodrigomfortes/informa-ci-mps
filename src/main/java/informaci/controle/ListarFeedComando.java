package informaci.controle;

import informaci.entidade.Publicacao;
import informaci.entidade.TipoDeOperacao;

import java.util.List;

/** Comando que lista o feed de publicações e registra o acesso. */
final class ListarFeedComando implements Comando<List<Publicacao>> {

    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;

    ListarFeedComando(GerenciadorDePublicacoes publicacoes, GerenciadorDeAcessos acessos) {
        this.publicacoes = publicacoes;
        this.acessos = acessos;
    }

    @Override
    public List<Publicacao> executar() {
        List<Publicacao> feed = publicacoes.listarFeed();
        acessos.registrar(TipoDeOperacao.CONSULTA_AO_FEED, null);
        return feed;
    }
}
