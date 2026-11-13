package informaci.controle;

/**
 * Formatos em que o relatório de acessos pode ser gerado.
 *
 * <p>Cada formato cria a subclasse do Template Method
 * {@link GeradorDeRelatorioDeAcessos} que sabe escrevê-lo. Um gerador novo é
 * criado a cada relatório, porque ele guarda o documento em construção.
 */
public enum FormatoDeRelatorio {

    HTML("HTML") {
        @Override
        public GeradorDeRelatorioDeAcessos criarGerador() {
            return new RelatorioDeAcessosEmHtml();
        }
    },

    PDF("PDF") {
        @Override
        public GeradorDeRelatorioDeAcessos criarGerador() {
            return new RelatorioDeAcessosEmPdf();
        }
    };

    private final String nome;

    FormatoDeRelatorio(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract GeradorDeRelatorioDeAcessos criarGerador();
}
