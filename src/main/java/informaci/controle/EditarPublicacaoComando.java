package informaci.controle;

import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.MementoDePublicacao;
import informaci.entidade.Publicacao;
import informaci.entidade.TipoDeOperacao;

import java.util.UUID;

/**
 * Comando que edita uma publicação, registra o acesso e guarda o estado
 * anterior para o "desfazer" (padrão Memento).
 *
 * <p>O memento é pedido à publicação antes da edição e só é entregue ao
 * {@link HistoricoDeEdicaoDePublicacao} se a edição der certo: uma edição
 * recusada não altera a publicação e, por isso, não deve ser desfeita.
 *
 * @see GerenciadorDePublicacoes#editar
 */
final class EditarPublicacaoComando implements Comando<Publicacao> {

    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;
    private final HistoricoDeEdicaoDePublicacao historico;
    private final UUID id;
    private final DadosDaPublicacao novosDados;

    EditarPublicacaoComando(GerenciadorDePublicacoes publicacoes, GerenciadorDeAcessos acessos,
            HistoricoDeEdicaoDePublicacao historico, UUID id, DadosDaPublicacao novosDados) {
        this.publicacoes = publicacoes;
        this.acessos = acessos;
        this.historico = historico;
        this.id = id;
        this.novosDados = novosDados;
    }

    @Override
    public Publicacao executar() {
        MementoDePublicacao estadoAnterior = publicacoes.buscarPorId(id).salvarEstado();
        Publicacao editada = publicacoes.editar(id, novosDados);
        historico.guardar(estadoAnterior);
        acessos.registrar(TipoDeOperacao.EDICAO_DE_PUBLICACAO, editada.getAutor().getLogin());
        return editada;
    }
}
