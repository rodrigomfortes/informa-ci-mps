package informaci.controle;

import informaci.entidade.Papel;
import informaci.entidade.Usuario;

import java.util.Objects;

/**
 * Coordena os casos de uso de gerenciamento de usuários.
 *
 * <p>Recebe o repositório pelo construtor em vez de instanciá-lo: assim o
 * controle não conhece a implementação concreta de armazenamento, e os testes
 * podem fornecer a que lhes convier.
 */
public class GerenciadorDeUsuarios {

    private final UsuarioRepositorio repositorio;

    public GerenciadorDeUsuarios(UsuarioRepositorio repositorio) {
        this.repositorio = Objects.requireNonNull(
                repositorio, "O repositório de usuários é obrigatório.");
    }

    /**
     * Adiciona um novo usuário ao sistema.
     *
     * <p>O usuário é construído antes da verificação de duplicidade porque é o
     * construtor de {@link Usuario} que valida e normaliza o e-mail — consultar
     * o repositório com o valor bruto deixaria passar duplicatas que diferem
     * apenas por maiúsculas ou espaços.
     *
     * @return o usuário criado, já com identidade atribuída
     * @throws IllegalArgumentException    se algum dado for inválido
     * @throws EmailJaCadastradoException  se o e-mail já pertencer a outro usuário
     */
    public Usuario adicionar(String nome, String email, Papel papel) {
        Usuario usuario = new Usuario(nome, email, papel);

        if (repositorio.existeComEmail(usuario.getEmail())) {
            throw new EmailJaCadastradoException(usuario.getEmail());
        }

        repositorio.salvar(usuario);
        return usuario;
    }
}
