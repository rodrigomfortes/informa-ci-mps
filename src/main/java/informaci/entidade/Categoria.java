package informaci.entidade;

/**
 * Tipo de uma {@link Publicacao}, conforme o glossário.
 *
 * <p>Eventos e palestras divulgam uma atividade que acontece num momento e num
 * lugar; por isso só eles exigem data, horário e local.
 */
public enum Categoria {

    NOTICIA("Notícia", false),
    EVENTO("Evento", true),
    PALESTRA("Palestra", true),
    COMUNICADO("Comunicado", false);

    private final String nome;
    private final boolean exigeDataELocal;

    Categoria(String nome, boolean exigeDataELocal) {
        this.nome = nome;
        this.exigeDataELocal = exigeDataELocal;
    }

    /** Nome da categoria como é exibido ao usuário. */
    public String getNome() {
        return nome;
    }

    public boolean exigeDataELocal() {
        return exigeDataELocal;
    }
}
