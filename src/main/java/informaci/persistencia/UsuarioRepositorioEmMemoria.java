package informaci.persistencia;

import informaci.controle.UsuarioRepositorio;
import informaci.entidade.Usuario;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
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
    public boolean existeComEmail(String email) {
        if (email == null) {
            return false;
        }
        return usuarios.values().stream()
                .anyMatch(usuario -> usuario.getEmail().equals(email));
    }

    /** Quantidade de usuários armazenados. Útil para verificação em testes. */
    public int quantidade() {
        return usuarios.size();
    }

    @Override
    public java.util.List<Usuario> listarTodos() {
        return java.util.List.copyOf(usuarios.values());
    }
}
