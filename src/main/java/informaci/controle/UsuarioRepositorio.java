package informaci.controle;

import informaci.entidade.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrato de armazenamento de usuários.
 *
 * <p>Declarado na camada de controle, e não na de persistência, de propósito:
 * é o controle que define <em>o que</em> precisa do armazenamento, enquanto a
 * persistência decide <em>como</em> atender. Essa inversão é o que permite
 * trocar a coleção em memória por um banco de dados sem tocar no controle.
 */
public interface UsuarioRepositorio {

    /**
     * Armazena o usuário informado. Se já houver um com a mesma identidade, ele
     * é substituído e mantém a sua posição na ordem de cadastro.
     *
     * @throws NullPointerException se o usuário for nulo
     */
    void salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    /** Remove o usuário com a identidade indicada, se existir. */
    void remover(UUID id);

    /**
     * Busca o usuário com o e-mail indicado.
     *
     * <p>Espera receber o e-mail já normalizado por {@link informaci.entidade.ValidadorDeEmail#normalizar(String)}.
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Informa se já existe usuário armazenado com o e-mail indicado.
     *
     * <p>Espera receber o e-mail já normalizado por {@link Usuario}.
     */
    default boolean existeComEmail(String email) {
        return buscarPorEmail(email).isPresent();
    }

    /**
     * Retorna todos os usuários armazenados no sistema, na ordem de cadastro.
     */
    List<Usuario> listarTodos();
}
