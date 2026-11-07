package informaci.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Conteúdo do Módulo de Notícias: notícia, evento, palestra ou comunicado.
 *
 * <p>Como {@link Usuario}, valida tudo na construção, de modo que não exista
 * publicação em estado inválido. É imutável: editar produz uma nova instância
 * com a mesma identidade ({@link #comDados(DadosDaPublicacao)}), o que deixa o
 * repositório decidir quando a alteração passa a valer.
 */
public class Publicacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int TAMANHO_MAXIMO_DO_TITULO = 100;
    private static final int TAMANHO_MAXIMO_DO_TEXTO = 2000;
    private static final int TAMANHO_MAXIMO_DO_LOCAL = 100;

    private final UUID id;
    private final Usuario autor;
    private final LocalDateTime dataDePublicacao;
    private final String titulo;
    private final String texto;
    private final Categoria categoria;
    private final LocalDateTime dataHora;
    private final String local;

    /**
     * Cria uma publicação nova, datada do momento atual.
     *
     * @throws PublicacaoInvalidaException se o autor não puder publicar ou se
     *                                     algum dado violar as regras
     */
    public Publicacao(Usuario autor, DadosDaPublicacao dados) {
        this(UUID.randomUUID(), validarAutor(autor), LocalDateTime.now(), dados);
    }

    private Publicacao(UUID id, Usuario autor, LocalDateTime dataDePublicacao, DadosDaPublicacao dados) {
        Objects.requireNonNull(dados, "Os dados da publicação são obrigatórios.");
        this.id = id;
        this.autor = autor;
        this.dataDePublicacao = dataDePublicacao;
        this.titulo = validarCampoObrigatorio(dados.titulo(), "título", TAMANHO_MAXIMO_DO_TITULO);
        this.texto = validarCampoObrigatorio(dados.texto(), "texto", TAMANHO_MAXIMO_DO_TEXTO);
        this.categoria = validarCategoria(dados.categoria());
        this.dataHora = dados.dataHora();
        this.local = validarLocal(dados.local());
        exigirDataELocalQuandoNecessario();
    }

    /**
     * Devolve a versão editada desta publicação: mesma identidade, mesmo autor
     * e mesma data de publicação, com os novos dados.
     *
     * @throws PublicacaoInvalidaException se algum dado violar as regras
     */
    public Publicacao comDados(DadosDaPublicacao novosDados) {
        return new Publicacao(id, autor, dataDePublicacao, novosDados);
    }

    /**
     * Devolve esta publicação com os dados atualizados do autor, depois que ele
     * foi editado. Não exige de novo o papel Publicador: o que foi publicado
     * enquanto ele o tinha continua válido.
     *
     * @throws IllegalArgumentException se o usuário informado não for o mesmo autor
     */
    public Publicacao comAutor(Usuario autorAtualizado) {
        if (autorAtualizado == null || !autorAtualizado.equals(autor)) {
            throw new IllegalArgumentException("O autor atualizado deve ser o mesmo usuário.");
        }
        return new Publicacao(id, autorAtualizado, dataDePublicacao, dados());
    }

    private DadosDaPublicacao dados() {
        return new DadosDaPublicacao(titulo, texto, categoria, dataHora, local);
    }

    /** Só quem tem o papel Publicador pode publicar (glossário: Publicador). */
    private static Usuario validarAutor(Usuario autor) {
        if (autor == null) {
            throw new PublicacaoInvalidaException("O autor da publicação é obrigatório.");
        }
        if (autor.getPapel() != Papel.PUBLICADOR) {
            throw new PublicacaoInvalidaException("Apenas usuários com papel Publicador podem publicar.");
        }
        return autor;
    }

    private static String validarCampoObrigatorio(String valor, String campo, int tamanhoMaximo) {
        if (valor == null || valor.isBlank()) {
            throw new PublicacaoInvalidaException("O " + campo + " da publicação é obrigatório.");
        }
        String semEspacos = valor.trim();
        if (semEspacos.length() > tamanhoMaximo) {
            throw new PublicacaoInvalidaException(
                    "O " + campo + " deve ter no máximo " + tamanhoMaximo + " caracteres.");
        }
        return semEspacos;
    }

    private static Categoria validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new PublicacaoInvalidaException("A categoria da publicação é obrigatória.");
        }
        return categoria;
    }

    /** O local é opcional: em branco equivale a não informado. */
    private static String validarLocal(String local) {
        if (local == null || local.isBlank()) {
            return null;
        }
        String semEspacos = local.trim();
        if (semEspacos.length() > TAMANHO_MAXIMO_DO_LOCAL) {
            throw new PublicacaoInvalidaException(
                    "O local deve ter no máximo " + TAMANHO_MAXIMO_DO_LOCAL + " caracteres.");
        }
        return semEspacos;
    }

    private void exigirDataELocalQuandoNecessario() {
        if (!categoria.exigeDataELocal()) {
            return;
        }
        if (dataHora == null) {
            throw new PublicacaoInvalidaException(
                    "Publicações da categoria " + categoria.getNome() + " exigem data e horário.");
        }
        if (local == null) {
            throw new PublicacaoInvalidaException(
                    "Publicações da categoria " + categoria.getNome() + " exigem local.");
        }
    }

    public UUID getId() {
        return id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public LocalDateTime getDataDePublicacao() {
        return dataDePublicacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTexto() {
        return texto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Optional<LocalDateTime> getDataHora() {
        return Optional.ofNullable(dataHora);
    }

    public Optional<String> getLocal() {
        return Optional.ofNullable(local);
    }

    /** Duas publicações são a mesma quando têm a mesma identidade, ainda que editadas. */
    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Publicacao publicacao)) {
            return false;
        }
        return id.equals(publicacao.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Publicacao{id=%s, categoria=%s, titulo='%s', autor='%s'}"
                .formatted(id, categoria, titulo, autor.getLogin());
    }
}
