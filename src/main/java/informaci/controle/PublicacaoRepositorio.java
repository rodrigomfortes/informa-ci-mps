package informaci.controle;

import informaci.entidade.Publicacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrato de armazenamento de publicações.
 *
 * <p>Fica no pacote de controle pelo mesmo motivo de {@link UsuarioRepositorio}
 * (ADR-0001): o controle declara o que precisa e a persistência implementa.
 */
public interface PublicacaoRepositorio {

    /**
     * Armazena a publicação. Se já houver uma com a mesma identidade, ela é
     * substituída e mantém a sua posição na ordem de cadastro.
     *
     * @throws NullPointerException se a publicação for nula
     */
    void salvar(Publicacao publicacao);

    Optional<Publicacao> buscarPorId(UUID id);

    /** Retorna todas as publicações, na ordem de cadastro. */
    List<Publicacao> listarTodas();

    /** Remove a publicação com a identidade indicada, se existir. */
    void remover(UUID id);
}
