package informaci.controle;

import informaci.entidade.MementoDePublicacao;
import informaci.entidade.Publicacao;
import informaci.entidade.TipoDeOperacao;

/**
 * Comando que desfaz a última edição de publicação (padrão Memento): pede ao
 * {@link HistoricoDeEdicaoDePublicacao} o estado anterior e o devolve à
 * publicação.
 *
 * <p>O memento só é descartado depois que a publicação restaurada é gravada;
 * se a gravação falhar, a edição continua podendo ser desfeita. Desfazer duas
 * vezes seguidas não volta mais um passo: só a última edição é desfeita.
 *
 * @throws NadaParaDesfazerException       se não houver edição para desfazer
 * @throws PublicacaoNaoEncontradaException se a publicação não existir mais
 */
final class DesfazerEdicaoDePublicacaoComando implements Comando<Publicacao> {

    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;
    private final HistoricoDeEdicaoDePublicacao historico;

    DesfazerEdicaoDePublicacaoComando(GerenciadorDePublicacoes publicacoes, GerenciadorDeAcessos acessos,
            HistoricoDeEdicaoDePublicacao historico) {
        this.publicacoes = publicacoes;
        this.acessos = acessos;
        this.historico = historico;
    }

    @Override
    public Publicacao executar() {
        MementoDePublicacao estadoAnterior = historico.ultimaEdicao()
                .orElseThrow(NadaParaDesfazerException::new);
        Publicacao restaurada = publicacoes.restaurar(estadoAnterior);
        historico.descartar();
        acessos.registrar(TipoDeOperacao.DESFAZER_EDICAO_DE_PUBLICACAO, restaurada.getAutor().getLogin());
        return restaurada;
    }
}
