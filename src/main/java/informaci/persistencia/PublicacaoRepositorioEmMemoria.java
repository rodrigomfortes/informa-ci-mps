package informaci.persistencia;

import informaci.controle.PublicacaoRepositorio;
import informaci.entidade.Publicacao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Armazena as publicações numa coleção em memória RAM; os dados se perdem ao
 * encerrar a aplicação.
 *
 * <p>{@link LinkedHashMap} preserva a ordem de cadastro, da qual o feed
 * depende, e mantém a posição de uma publicação quando ela é editada.
 */
public class PublicacaoRepositorioEmMemoria implements PublicacaoRepositorio {

    private final Map<UUID, Publicacao> publicacoes = new LinkedHashMap<>();

    @Override
    public void salvar(Publicacao publicacao) {
        Objects.requireNonNull(publicacao, "Não é possível salvar uma publicação nula.");
        publicacoes.put(publicacao.getId(), publicacao);
    }

    @Override
    public Optional<Publicacao> buscarPorId(UUID id) {
        return Optional.ofNullable(publicacoes.get(id));
    }

    @Override
    public List<Publicacao> listarTodas() {
        return List.copyOf(publicacoes.values());
    }

    @Override
    public void remover(UUID id) {
        publicacoes.remove(id);
    }
}
