package informaci.controle;

import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Papel;
import informaci.entidade.Publicacao;
import informaci.entidade.Usuario;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Fachada única da camada de negócio (padrões Facade e Singleton).
 *
 * <p><strong>Facade:</strong> a fronteira conversa só com esta classe, que
 * delega aos gerentes de cada caso de uso ({@link GerenciadorDeUsuarios} e
 * {@link GerenciadorDePublicacoes}) e combina os dois quando uma operação
 * envolve mais de uma entidade, como publicar em nome de um usuário.
 *
 * <p><strong>Singleton:</strong> existe uma única instância, para que todas as
 * telas enxerguem os mesmos gerentes e os mesmos dados. Como o armazenamento
 * só é conhecido depois que o usuário o escolhe, a instância é criada uma vez
 * por {@link #inicializar} e obtida depois por {@link #getInstancia()}.
 */
public final class FacadeSingletonController {

    private static FacadeSingletonController instancia;

    private final GerenciadorDeUsuarios usuarios;
    private final GerenciadorDePublicacoes publicacoes;

    private FacadeSingletonController(UsuarioRepositorio usuarios, PublicacaoRepositorio publicacoes) {
        this.usuarios = new GerenciadorDeUsuarios(usuarios);
        this.publicacoes = new GerenciadorDePublicacoes(publicacoes);
    }

    /**
     * Cria a instância única com os repositórios escolhidos.
     *
     * @throws IllegalStateException se a fachada já tiver sido inicializada
     */
    public static synchronized FacadeSingletonController inicializar(
            UsuarioRepositorio usuarios, PublicacaoRepositorio publicacoes) {
        if (instancia != null) {
            throw new IllegalStateException("A fachada já foi inicializada.");
        }
        instancia = new FacadeSingletonController(
                Objects.requireNonNull(usuarios, "O repositório de usuários é obrigatório."),
                Objects.requireNonNull(publicacoes, "O repositório de publicações é obrigatório."));
        return instancia;
    }

    /**
     * @throws IllegalStateException se a fachada ainda não tiver sido inicializada
     */
    public static synchronized FacadeSingletonController getInstancia() {
        if (instancia == null) {
            throw new IllegalStateException("A fachada ainda não foi inicializada.");
        }
        return instancia;
    }

    /**
     * Descarta a instância única. Usado ao encerrar a aplicação e pelos testes,
     * para que cada um comece com uma fachada nova.
     */
    public static synchronized void encerrar() {
        instancia = null;
    }

    // ---- Usuários ----

    /** @see GerenciadorDeUsuarios#adicionar */
    public Usuario adicionarUsuario(String nome, String email, String login, String senha, Papel papel) {
        return usuarios.adicionar(nome, email, login, senha, papel);
    }

    public List<Usuario> listarUsuarios() {
        return usuarios.listarTodos();
    }

    // ---- Publicações ----

    /**
     * Publica em nome do usuário com o e-mail informado.
     *
     * @throws UsuarioNaoEncontradoException se nenhum usuário tiver esse e-mail
     * @see GerenciadorDePublicacoes#publicar
     */
    public Publicacao publicar(String emailDoAutor, DadosDaPublicacao dados) {
        Usuario autor = usuarios.buscarPorEmail(emailDoAutor);
        return publicacoes.publicar(autor, dados);
    }

    public List<Publicacao> listarFeed() {
        return publicacoes.listarFeed();
    }

    /** @see GerenciadorDePublicacoes#editar */
    public Publicacao editarPublicacao(UUID id, DadosDaPublicacao novosDados) {
        return publicacoes.editar(id, novosDados);
    }

    /** @see GerenciadorDePublicacoes#remover */
    public void removerPublicacao(UUID id) {
        publicacoes.remover(id);
    }

    // ---- Estatísticas ----

    public int quantidadeDeUsuarios() {
        return usuarios.quantidade();
    }

    public int quantidadeDePublicacoes() {
        return publicacoes.quantidade();
    }

    /** Total de entidades cadastradas no sistema: usuários mais publicações. */
    public int quantidadeDeEntidadesCadastradas() {
        return quantidadeDeUsuarios() + quantidadeDePublicacoes();
    }
}
