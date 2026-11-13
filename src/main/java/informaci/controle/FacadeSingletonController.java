package informaci.controle;

import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.DadosDoUsuario;
import informaci.entidade.Papel;
import informaci.entidade.Publicacao;
import informaci.entidade.TipoDeOperacao;
import informaci.entidade.Usuario;

import java.time.Clock;
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
 *
 * <p>Recebe também um {@link ServicoDeLog} e o repassa aos gerentes. A
 * biblioteca de log concreta fica isolada atrás desse contrato (padrão
 * Adapter, ADR-0004) e só é conhecida pelo ponto de entrada.
 */
public final class FacadeSingletonController {

    private static FacadeSingletonController instancia;

    private final GerenciadorDeUsuarios usuarios;
    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;

    private FacadeSingletonController(
            UsuarioRepositorio usuarios, PublicacaoRepositorio publicacoes,
            RegistroDeAcessoRepositorio acessos, ServicoDeLog log) {
        this.usuarios = new GerenciadorDeUsuarios(usuarios, log);
        this.publicacoes = new GerenciadorDePublicacoes(publicacoes, log);
        this.acessos = new GerenciadorDeAcessos(acessos, log, Clock.systemDefaultZone());
    }

    /**
     * Cria a instância única pedindo à fábrica os repositórios de todas as
     * entidades (Abstract Factory). A fachada não sabe se eles guardam os dados
     * em memória ou em arquivo, só que vêm da mesma família.
     *
     * <p>Se a fábrica falhar ao criar um repositório (por exemplo, um arquivo
     * ilegível), a exceção é propagada e a fachada continua não inicializada,
     * podendo ser inicializada de novo com outra fábrica.
     *
     * <p>A verificação de "já inicializada" vem antes de pedir os repositórios,
     * para que uma segunda inicialização não leia arquivos à toa.
     *
     * @param log serviço de log repassado aos gerentes (padrão Adapter)
     * @throws IllegalStateException se a fachada já tiver sido inicializada
     */
    public static synchronized FacadeSingletonController inicializar(
            FabricaDeRepositorios fabrica, ServicoDeLog log) {
        Objects.requireNonNull(fabrica, "A fábrica de repositórios é obrigatória.");
        Objects.requireNonNull(log, "O serviço de log é obrigatório.");
        exigirNaoInicializada();
        return criarInstancia(fabrica.criarRepositorioDeUsuarios(), fabrica.criarRepositorioDePublicacoes(),
                fabrica.criarRepositorioDeAcessos(), log);
    }

    /**
     * Cria a instância única sem registro de log ({@link NullLog}), por
     * exemplo nos testes.
     *
     * @throws IllegalStateException se a fachada já tiver sido inicializada
     */
    public static synchronized FacadeSingletonController inicializar(FabricaDeRepositorios fabrica) {
        return inicializar(fabrica, new NullLog());
    }

    /**
     * Cria a instância única com os repositórios informados.
     *
     * <p>Visível só no pacote {@code controle}, para os testes da camada de
     * negócio que precisam montar a fachada com repositórios específicos. Fora
     * dele, a única entrada é {@link #inicializar(FabricaDeRepositorios)}, o que
     * garante que todas as entidades usem a mesma família de armazenamento
     * (ADR-0003).
     *
     * @throws IllegalStateException se a fachada já tiver sido inicializada
     */
    static synchronized FacadeSingletonController inicializar(
            UsuarioRepositorio usuarios, PublicacaoRepositorio publicacoes, RegistroDeAcessoRepositorio acessos) {
        exigirNaoInicializada();
        return criarInstancia(usuarios, publicacoes, acessos, new NullLog());
    }

    private static void exigirNaoInicializada() {
        if (instancia != null) {
            throw new IllegalStateException("A fachada já foi inicializada.");
        }
    }

    private static FacadeSingletonController criarInstancia(
            UsuarioRepositorio usuarios, PublicacaoRepositorio publicacoes,
            RegistroDeAcessoRepositorio acessos, ServicoDeLog log) {
        instancia = new FacadeSingletonController(
                Objects.requireNonNull(usuarios, "O repositório de usuários é obrigatório."),
                Objects.requireNonNull(publicacoes, "O repositório de publicações é obrigatório."),
                Objects.requireNonNull(acessos, "O repositório de acessos é obrigatório."),
                log);
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
        Usuario usuario = usuarios.adicionar(nome, email, login, senha, papel);
        acessos.registrar(TipoDeOperacao.CADASTRO_DE_USUARIO, usuario.getLogin());
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = usuarios.listarTodos();
        acessos.registrar(TipoDeOperacao.LISTAGEM_DE_USUARIOS, null);
        return lista;
    }

    /**
     * Edita o usuário e atualiza o autor nas publicações dele, para que o feed
     * mostre os dados novos.
     *
     * @see GerenciadorDeUsuarios#editar
     */
    public Usuario editarUsuario(UUID id, DadosDoUsuario novosDados) {
        Usuario editado = usuarios.editar(id, novosDados);
        publicacoes.atualizarAutor(editado);
        acessos.registrar(TipoDeOperacao.EDICAO_DE_USUARIO, editado.getLogin());
        return editado;
    }

    /**
     * Remove o usuário, desde que ele não seja autor de nenhuma publicação.
     *
     * @throws UsuarioComPublicacoesException se o usuário tiver publicações
     * @throws UsuarioNaoEncontradoException  se não houver usuário com essa identidade
     */
    public void removerUsuario(UUID id) {
        Usuario usuario = usuarios.buscarPorId(id);
        int publicacoesDoUsuario = publicacoes.contarDoAutor(usuario);
        if (publicacoesDoUsuario > 0) {
            throw new UsuarioComPublicacoesException(usuario.getLogin(), publicacoesDoUsuario);
        }
        usuarios.remover(id);
        acessos.registrar(TipoDeOperacao.REMOCAO_DE_USUARIO, usuario.getLogin());
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
        Publicacao publicacao = publicacoes.publicar(autor, dados);
        acessos.registrar(TipoDeOperacao.PUBLICACAO, autor.getLogin());
        return publicacao;
    }

    public List<Publicacao> listarFeed() {
        List<Publicacao> feed = publicacoes.listarFeed();
        acessos.registrar(TipoDeOperacao.CONSULTA_AO_FEED, null);
        return feed;
    }

    /** @see GerenciadorDePublicacoes#editar */
    public Publicacao editarPublicacao(UUID id, DadosDaPublicacao novosDados) {
        Publicacao editada = publicacoes.editar(id, novosDados);
        acessos.registrar(TipoDeOperacao.EDICAO_DE_PUBLICACAO, editada.getAutor().getLogin());
        return editada;
    }

    /** @see GerenciadorDePublicacoes#remover */
    public void removerPublicacao(UUID id) {
        Publicacao publicacao = publicacoes.buscarPorId(id);
        publicacoes.remover(id);
        acessos.registrar(TipoDeOperacao.REMOCAO_DE_PUBLICACAO, publicacao.getAutor().getLogin());
    }

    // ---- Relatório de acessos ----

    /**
     * Gera o relatório de estatísticas de acesso no formato escolhido (RF13),
     * usando a subclasse do Template Method correspondente ao formato.
     *
     * @return o conteúdo do arquivo do relatório
     */
    public byte[] gerarRelatorioDeAcessos(FormatoDeRelatorio formato) {
        Objects.requireNonNull(formato, "O formato do relatório é obrigatório.");
        return formato.criarGerador().gerar(acessos.calcularEstatisticas());
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
