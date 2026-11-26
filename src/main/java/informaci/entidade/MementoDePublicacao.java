package informaci.entidade;

import java.util.Objects;
import java.util.UUID;

/**
 * Estado de uma {@link Publicacao} guardado antes de uma edição (padrão
 * <strong>Memento</strong>).
 *
 * <p>A {@link Publicacao} é o originador: só ela cria um memento
 * ({@link Publicacao#salvarEstado()}) e só ela lê o conteúdo dele
 * ({@link Publicacao#restaurar(MementoDePublicacao)}). Por isso os dados
 * guardados são visíveis apenas dentro do pacote {@code entidade}. Quem guarda
 * o memento até o "desfazer" (o cuidador, na camada de controle) só enxerga a
 * identidade da publicação, para saber a qual publicação ele pertence.
 *
 * <p>É imutável: o estado guardado não muda depois de criado.
 */
public final class MementoDePublicacao {

    private final UUID idDaPublicacao;
    private final DadosDaPublicacao dados;

    MementoDePublicacao(UUID idDaPublicacao, DadosDaPublicacao dados) {
        this.idDaPublicacao = Objects.requireNonNull(idDaPublicacao, "A identidade da publicação é obrigatória.");
        this.dados = Objects.requireNonNull(dados, "Os dados da publicação são obrigatórios.");
    }

    /** Identidade da publicação cujo estado foi guardado. */
    public UUID getIdDaPublicacao() {
        return idDaPublicacao;
    }

    /** Estado guardado; só o originador ({@link Publicacao}) o lê. */
    DadosDaPublicacao getDados() {
        return dados;
    }
}
