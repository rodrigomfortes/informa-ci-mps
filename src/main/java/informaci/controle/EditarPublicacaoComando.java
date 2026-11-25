package informaci.controle;

import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Publicacao;
import informaci.entidade.TipoDeOperacao;

import java.util.UUID;

/**
 * Comando que edita uma publicação e registra o acesso.
 *
 * <p>Fica numa classe própria para que o "desfazer" da edição (padrão Memento)
 * possa ser acrescentado aqui sem mexer na fachada.
 *
 * @see GerenciadorDePublicacoes#editar
 */
final class EditarPublicacaoComando implements Comando<Publicacao> {

    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;
    private final UUID id;
    private final DadosDaPublicacao novosDados;

    EditarPublicacaoComando(GerenciadorDePublicacoes publicacoes, GerenciadorDeAcessos acessos,
            UUID id, DadosDaPublicacao novosDados) {
        this.publicacoes = publicacoes;
        this.acessos = acessos;
        this.id = id;
        this.novosDados = novosDados;
    }

    @Override
    public Publicacao executar() {
        Publicacao editada = publicacoes.editar(id, novosDados);
        acessos.registrar(TipoDeOperacao.EDICAO_DE_PUBLICACAO, editada.getAutor().getLogin());
        return editada;
    }
}
