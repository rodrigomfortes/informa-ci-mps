package informaci.controle;

import informaci.entidade.Publicacao;
import informaci.entidade.TipoDeOperacao;

import java.util.UUID;

/**
 * Comando que remove uma publicação e registra o acesso em nome do autor dela.
 *
 * @see GerenciadorDePublicacoes#remover
 */
final class RemoverPublicacaoComando implements Comando<Void> {

    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;
    private final UUID id;

    RemoverPublicacaoComando(GerenciadorDePublicacoes publicacoes, GerenciadorDeAcessos acessos, UUID id) {
        this.publicacoes = publicacoes;
        this.acessos = acessos;
        this.id = id;
    }

    @Override
    public Void executar() {
        Publicacao publicacao = publicacoes.buscarPorId(id);
        publicacoes.remover(id);
        acessos.registrar(TipoDeOperacao.REMOCAO_DE_PUBLICACAO, publicacao.getAutor().getLogin());
        return null;
    }
}
