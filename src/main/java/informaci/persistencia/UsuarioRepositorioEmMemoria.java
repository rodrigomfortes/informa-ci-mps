package informaci.persistencia;

import informaci.controle.UsuarioRepositorio;
import informaci.entidade.Usuario;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Armazena os usuários numa coleção mantida em memória RAM.
 *
 * <p>Os dados existem apenas enquanto a aplicação estiver em execução: ao
 * encerrar, tudo se perde. Para mantê-los entre execuções existe
 * {@link UsuarioRepositorioEmArquivo}; as duas implementam
 * {@link UsuarioRepositorio}, então alternar entre elas não exige mudança em
 * nenhuma outra camada.
 *
 * <p>A coleção é um {@link LinkedHashMap} e não um {@link java.util.HashMap}
 * porque ele preserva a ordem de inserção, o que torna a listagem de usuários
 * previsível em vez de arbitrária.
 */
public class UsuarioRepositorioEmMemoria implements UsuarioRepositorio {

    private final Map<UUID, Usuario> usuarios = new LinkedHashMap<>();

    @Override
    public void salvar(Usuario usuario) {
        Objects.requireNonNull(usuario, "Não é possível salvar um usuário nulo.");
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public void remover(UUID id) {
        usuarios.remove(id);
    }

    /** Quantidade de usuários armazenados. Útil para verificação em testes. */
    public int quantidade() {
        return usuarios.size();
    }

    @Override
    public List<Usuario> listarTodos() {
        return List.copyOf(usuarios.values());
    }
}
