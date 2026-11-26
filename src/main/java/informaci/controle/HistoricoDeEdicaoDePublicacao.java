package informaci.controle;

import informaci.entidade.MementoDePublicacao;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Cuidador (caretaker) do padrão <strong>Memento</strong>: guarda o estado da
 * publicação de antes da última edição, para que ela possa ser desfeita.
 *
 * <p>Guarda só um memento, porque basta desfazer a última atualização: cada
 * edição nova substitui o memento anterior. O cuidador não lê o conteúdo do
 * memento; só a {@link informaci.entidade.Publicacao} sabe restaurá-lo.
 *
 * <p>O histórico fica em memória e vale enquanto o sistema está aberto.
 */
public class HistoricoDeEdicaoDePublicacao {

    private MementoDePublicacao ultimaEdicao;

    /** Guarda o estado anterior à edição que acabou de ser feita. */
    public synchronized void guardar(MementoDePublicacao memento) {
        ultimaEdicao = Objects.requireNonNull(memento, "O memento é obrigatório.");
    }

    /** Estado anterior à última edição, se houver uma edição para desfazer. */
    public synchronized Optional<MementoDePublicacao> ultimaEdicao() {
        return Optional.ofNullable(ultimaEdicao);
    }

    /** Esquece a última edição, depois que ela foi desfeita. */
    public synchronized void descartar() {
        ultimaEdicao = null;
    }

    /** Esquece a última edição se ela for da publicação informada (por exemplo, removida). */
    public synchronized void descartarSeFor(UUID idDaPublicacao) {
        if (ultimaEdicao != null && ultimaEdicao.getIdDaPublicacao().equals(idDaPublicacao)) {
            ultimaEdicao = null;
        }
    }
}
