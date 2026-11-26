package informaci.controle;

import informaci.entidade.Publicacao;
import informaci.entidade.TipoDeOperacao;

import java.util.UUID;

/**
 * Comando que remove uma publicação e registra o acesso em nome do autor dela.
 *
 * <p>Se a última edição guardada para o "desfazer" for desta publicação, ela é
 * esquecida, já que não há mais o que restaurar.
 *
 * @see GerenciadorDePublicacoes#remover
 */
final class RemoverPublicacaoComando implements Comando<Void> {

    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;
    private final HistoricoDeEdicaoDePublicacao historico;
    private final UUID id;

    RemoverPublicacaoComando(GerenciadorDePublicacoes publicacoes, GerenciadorDeAcessos acessos,
            HistoricoDeEdicaoDePublicacao historico, UUID id) {
        this.publicacoes = publicacoes;
        this.acessos = acessos;
        this.historico = historico;
        this.id = id;
    }

    @Override
    public Void executar() {
        Publicacao publicacao = publicacoes.buscarPorId(id);
        publicacoes.remover(id);
        historico.descartarSeFor(id);
        acessos.registrar(TipoDeOperacao.REMOCAO_DE_PUBLICACAO, publicacao.getAutor().getLogin());
        return null;
    }
}
