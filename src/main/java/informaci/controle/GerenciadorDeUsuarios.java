package informaci.controle;

import informaci.entidade.EmailInvalidoException;
import informaci.entidade.LoginInvalidoException;
import informaci.entidade.NomeInvalidoException;
import informaci.entidade.DadosDoUsuario;
import informaci.entidade.Papel;
import informaci.entidade.SenhaInvalidaException;
import informaci.entidade.Usuario;
import informaci.entidade.ValidadorDeEmail;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Coordena os casos de uso de gerenciamento de usuários.
 *
 * <p>Recebe o repositório pelo construtor em vez de instanciá-lo: assim o
 * controle não conhece a implementação concreta de armazenamento, e os testes
 * podem fornecer a que lhes convier.
 *
 * <p>Recebe também um {@link ServicoDeLog} pelo construtor. Graças ao padrão
 * Adapter, esta classe desconhece a biblioteca de log concreta: ela só conhece
 * a interface {@link ServicoDeLog}, e o adapter ({@code JulLogAdapter}) cuida
 * de traduzir as chamadas para a API da JUL — ou qualquer outra lib no futuro.
 */
public class GerenciadorDeUsuarios {

    private final UsuarioRepositorio repositorio;
    private final ServicoDeLog log;

    /**
     * Cria o gerenciador com repositório e serviço de log.
     *
     * @param repositorio repositório de usuários (obrigatório)
     * @param log         serviço de log a usar; use {@link NullLog} para silenciar
     */
    public GerenciadorDeUsuarios(UsuarioRepositorio repositorio, ServicoDeLog log) {
        this.repositorio = Objects.requireNonNull(
                repositorio, "O repositório de usuários é obrigatório.");
        this.log = Objects.requireNonNull(log, "O serviço de log é obrigatório.");
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
     * @throws LoginInvalidoException      se o login violar alguma regra de formato
     * @throws SenhaInvalidaException      se a senha não atender à política de senhas
     * @throws NomeInvalidoException       se o nome estiver em branco
     * @throws EmailInvalidoException      se o e-mail for inválido
     * @throws EmailJaCadastradoException  se o e-mail já pertencer a outro usuário
     */
    public Usuario adicionar(String nome, String email, String login, String senha, Papel papel) {
        Usuario usuario = new Usuario(nome, email, login, senha, papel);

        if (repositorio.existeComEmail(usuario.getEmail())) {
            log.aviso("Cadastro recusado: e-mail já pertence a outro usuário.");
            throw new EmailJaCadastradoException(usuario.getEmail());
        }

        repositorio.salvar(usuario);
        log.info("Usuário adicionado: " + usuario.getLogin() + " (" + usuario.getId() + ")");
        return usuario;
    }

    /**
     * Lista todos os usuários cadastrados no sistema, na ordem de cadastro.
     */
    public List<Usuario> listarTodos() {
        return repositorio.listarTodos();
    }

    /**
     * Busca o usuário pelo e-mail, sem diferenciar maiúsculas de minúsculas.
     *
     * @throws UsuarioNaoEncontradoException se nenhum usuário tiver esse e-mail
     */
    public Usuario buscarPorEmail(String email) {
        String normalizado = ValidadorDeEmail.normalizar(email);
        return repositorio.buscarPorEmail(normalizado)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(normalizado));
    }

    /**
     * @throws UsuarioNaoEncontradoException se não houver usuário com essa identidade
     */
    public Usuario buscarPorId(UUID id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    /**
     * Substitui os dados do usuário, mantendo a identidade e a posição na listagem.
     *
     * @return o usuário editado
     * @throws UsuarioNaoEncontradoException se não houver usuário com essa identidade
     * @throws IllegalArgumentException      (ou subclasse) se algum dado for inválido
     * @throws EmailJaCadastradoException    se o novo e-mail já pertencer a outro usuário
     */
    public Usuario editar(UUID id, DadosDoUsuario novosDados) {
        Usuario editado = buscarPorId(id).comDados(novosDados);
        repositorio.buscarPorEmail(editado.getEmail())
                .filter(outro -> !outro.equals(editado))
                .ifPresent(outro -> {
                    log.aviso("Edição recusada: o novo e-mail do usuário " + editado.getId() + " já pertence a outro usuário.");
                    throw new EmailJaCadastradoException(editado.getEmail());
                });
        repositorio.salvar(editado);
        log.info("Usuário editado: " + editado.getLogin() + " (" + editado.getId() + ")");
        return editado;
    }

    /**
     * @throws UsuarioNaoEncontradoException se não houver usuário com essa identidade
     */
    public void remover(UUID id) {
        buscarPorId(id);
        repositorio.remover(id);
        log.info("Usuário removido: " + id);
    }

    public int quantidade() {
        return repositorio.listarTodos().size();
    }
}
