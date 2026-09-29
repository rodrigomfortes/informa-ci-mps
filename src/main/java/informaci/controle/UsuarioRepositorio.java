package informaci.controle;

import informaci.entidade.Usuario;

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
     * Armazena o usuário informado.
     *
     * @throws NullPointerException se o usuário for nulo
     */
    void salvar(Usuario usuario);

    /**
     * Informa se já existe usuário armazenado com o e-mail indicado.
     *
     * <p>Espera receber o e-mail já normalizado por {@link Usuario}.
     */
    boolean existeComEmail(String email);
}
