package informaci.persistencia;

import informaci.controle.PublicacaoRepositorio;
import informaci.entidade.Publicacao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Armazena as publicações num arquivo binário, mantendo uma cópia em memória
 * para consulta.
 *
 * <p>Funciona como {@link UsuarioRepositorioEmArquivo}: lê o arquivo no
 * construtor, regrava-o por inteiro a cada alteração e, se a gravação falhar,
 * restaura a cópia em memória para que ela nunca diga algo diferente do arquivo.
 */
public class PublicacaoRepositorioEmArquivo implements PublicacaoRepositorio {

    private final Path arquivo;
    private final Map<UUID, Publicacao> publicacoes = new LinkedHashMap<>();

    /**
     * @throws PersistenciaException se o arquivo existir mas não puder ser lido
     */
    public PublicacaoRepositorioEmArquivo(Path arquivo) {
        this.arquivo = Objects.requireNonNull(arquivo, "O caminho do arquivo é obrigatório.");
        carregar();
    }

    @Override
    public void salvar(Publicacao publicacao) {
        Objects.requireNonNull(publicacao, "Não é possível salvar uma publicação nula.");
        alterarEGravar(() -> publicacoes.put(publicacao.getId(), publicacao));
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
        alterarEGravar(() -> publicacoes.remove(id));
    }

    private void alterarEGravar(Runnable alteracao) {
        Map<UUID, Publicacao> estadoAnterior = new LinkedHashMap<>(publicacoes);
        alteracao.run();
        try {
            gravar();
        } catch (PersistenciaException e) {
            publicacoes.clear();
            publicacoes.putAll(estadoAnterior);
            throw e;
        }
    }

    private void carregar() {
        try {
            for (Publicacao publicacao : ArquivoBinario.ler(arquivo, Publicacao.class)) {
                publicacoes.put(publicacao.getId(), publicacao);
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PersistenciaException("Não foi possível ler as publicações de " + arquivo + ".", e);
        }
    }

    private void gravar() {
        try {
            ArquivoBinario.gravar(arquivo, List.copyOf(publicacoes.values()));
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível gravar as publicações em " + arquivo + ".", e);
        }
    }
}
