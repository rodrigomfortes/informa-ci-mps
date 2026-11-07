package informaci.persistencia;

import informaci.controle.UsuarioRepositorio;
import informaci.entidade.Usuario;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Armazena os usuários num arquivo binário (serialização Java), mantendo uma
 * cópia em memória para consulta.
 *
 * <p>No construtor, o arquivo é lido e carregado para a memória; se ele ainda
 * não existir, o repositório começa vazio. A cada {@link #salvar(Usuario)} o
 * arquivo é regravado por inteiro. Falhas de E/S são convertidas em
 * {@link PersistenciaException}.
 */
public class UsuarioRepositorioEmArquivo implements UsuarioRepositorio {

    private final Path arquivo;
    private final Map<UUID, Usuario> usuarios = new LinkedHashMap<>();

    /**
     * @throws PersistenciaException se o arquivo existir mas não puder ser lido
     */
    public UsuarioRepositorioEmArquivo(Path arquivo) {
        this.arquivo = Objects.requireNonNull(arquivo, "O caminho do arquivo é obrigatório.");
        carregar();
    }

    /**
     * Armazena o usuário e regrava o arquivo.
     *
     * <p>Se a gravação falhar, a cópia em memória volta ao estado anterior:
     * um cadastro que não chegou ao arquivo não pode aparecer na listagem nem
     * bloquear uma nova tentativa com o mesmo e-mail.
     */
    @Override
    public void salvar(Usuario usuario) {
        Objects.requireNonNull(usuario, "Não é possível salvar um usuário nulo.");
        alterarEGravar(() -> usuarios.put(usuario.getId(), usuario));
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public void remover(UUID id) {
        alterarEGravar(() -> usuarios.remove(id));
    }

    private void alterarEGravar(Runnable alteracao) {
        Map<UUID, Usuario> estadoAnterior = new LinkedHashMap<>(usuarios);
        alteracao.run();
        try {
            gravar();
        } catch (PersistenciaException e) {
            usuarios.clear();
            usuarios.putAll(estadoAnterior);
            throw e;
        }
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Usuario> listarTodos() {
        return List.copyOf(usuarios.values());
    }

    private void carregar() {
        try {
            for (Usuario usuario : ArquivoBinario.ler(arquivo, Usuario.class)) {
                usuarios.put(usuario.getId(), usuario);
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PersistenciaException("Não foi possível ler os usuários de " + arquivo + ".", e);
        }
    }

    private void gravar() {
        try {
            ArquivoBinario.gravar(arquivo, List.copyOf(usuarios.values()));
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível gravar os usuários em " + arquivo + ".", e);
        }
    }
}
