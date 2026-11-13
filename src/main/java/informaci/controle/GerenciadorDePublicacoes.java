package informaci.controle;

import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Publicacao;
import informaci.entidade.PublicacaoInvalidaException;
import informaci.entidade.Usuario;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Coordena os casos de uso do Módulo de Notícias: publicar, consultar o feed,
 * editar e remover publicações.
 *
 * <p>Recebe o autor já resolvido: localizar o usuário é responsabilidade de
 * {@link GerenciadorDeUsuarios}, e quem combina os dois é a fachada.
 *
 * <p>Recebe também um {@link ServicoDeLog} pelo construtor. Graças ao padrão
 * Adapter, esta classe desconhece a biblioteca de log concreta.
 */
public class GerenciadorDePublicacoes {

    private final PublicacaoRepositorio repositorio;
    private final ServicoDeLog log;

    /**
     * Cria o gerenciador com repositório e serviço de log.
     *
     * @param repositorio repositório de publicações (obrigatório)
     * @param log         serviço de log a usar; use {@link NullLog} para silenciar
     */
    public GerenciadorDePublicacoes(PublicacaoRepositorio repositorio, ServicoDeLog log) {
        this.repositorio = Objects.requireNonNull(
                repositorio, "O repositório de publicações é obrigatório.");
        this.log = Objects.requireNonNull(log, "O serviço de log é obrigatório.");
    }


    /**
     * @return a publicação criada, já com identidade e data de publicação
     * @throws PublicacaoInvalidaException se o autor não puder publicar ou os dados forem inválidos
     */
    public Publicacao publicar(Usuario autor, DadosDaPublicacao dados) {
        Publicacao publicacao = new Publicacao(autor, dados);
        repositorio.salvar(publicacao);
        log.info("Publicação criada: " + publicacao.getId() + " por " + autor.getLogin());
        return publicacao;
    }

    /** Lista as publicações da mais recente para a mais antiga (glossário: Feed). */
    public List<Publicacao> listarFeed() {
        return repositorio.listarTodas().reversed();
    }

    /**
     * @throws PublicacaoNaoEncontradaException se não houver publicação com essa identidade
     */
    public Publicacao buscarPorId(UUID id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new PublicacaoNaoEncontradaException(id));
    }

    /**
     * Substitui os dados da publicação, mantendo identidade, autor e data de publicação.
     *
     * @return a publicação editada
     * @throws PublicacaoNaoEncontradaException se não houver publicação com essa identidade
     * @throws PublicacaoInvalidaException      se os novos dados forem inválidos
     */
    public Publicacao editar(UUID id, DadosDaPublicacao novosDados) {
        Publicacao editada = buscarPorId(id).comDados(novosDados);
        repositorio.salvar(editada);
        log.info("Publicação editada: " + editada.getId());
        return editada;
    }

    /**
     * @throws PublicacaoNaoEncontradaException se não houver publicação com essa identidade
     */
    public void remover(UUID id) {
        buscarPorId(id);
        repositorio.remover(id);
        log.info("Publicação removida: " + id);
    }

    /** Quantas publicações têm o usuário informado como autor. */
    public int contarDoAutor(Usuario autor) {
        return (int) repositorio.listarTodas().stream()
                .filter(publicacao -> publicacao.getAutor().equals(autor))
                .count();
    }

    /** Atualiza, nas publicações do autor, os dados dele depois de uma edição. */
    public void atualizarAutor(Usuario autorEditado) {
        for (Publicacao publicacao : repositorio.listarTodas()) {
            if (publicacao.getAutor().equals(autorEditado)) {
                repositorio.salvar(publicacao.comAutor(autorEditado));
            }
        }
    }

    public int quantidade() {
        return repositorio.listarTodas().size();
    }
}
