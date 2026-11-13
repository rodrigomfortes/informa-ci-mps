package informaci.controle;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Relatório de acessos em HTML: subclasse concreta do Template Method
 * {@link GeradorDeRelatorioDeAcessos}. Abre em qualquer navegador.
 */
public class RelatorioDeAcessosEmHtml extends GeradorDeRelatorioDeAcessos {

    private static final String ESTILO = "body{font-family:sans-serif;margin:2rem;color:#222}"
            + "table{border-collapse:collapse;margin-bottom:1.5rem}"
            + "th,td{border:1px solid #999;padding:.3rem .8rem;text-align:left}"
            + "th{background:#eee}td:last-child{text-align:right}";

    private StringBuilder html;

    @Override
    public String extensao() {
        return "html";
    }

    @Override
    protected void iniciarDocumento(String titulo) {
        html = new StringBuilder()
                .append("<!DOCTYPE html>\n<html lang=\"pt-BR\">\n<head>\n<meta charset=\"UTF-8\">\n")
                .append("<title>").append(escapar(titulo)).append("</title>\n")
                .append("<style>").append(ESTILO).append("</style>\n</head>\n<body>\n")
                .append("<h1>").append(escapar(titulo)).append("</h1>\n");
    }

    @Override
    protected void escreverParagrafo(String texto) {
        html.append("<p>").append(escapar(texto)).append("</p>\n");
    }

    @Override
    protected void escreverTabela(String titulo, List<String> cabecalho, List<List<String>> linhas) {
        html.append("<h2>").append(escapar(titulo)).append("</h2>\n<table>\n<thead><tr>");
        for (String coluna : cabecalho) {
            html.append("<th>").append(escapar(coluna)).append("</th>");
        }
        html.append("</tr></thead>\n<tbody>\n");
        for (List<String> linha : linhas) {
            html.append("<tr>");
            for (String celula : linha) {
                html.append("<td>").append(escapar(celula)).append("</td>");
            }
            html.append("</tr>\n");
        }
        html.append("</tbody>\n</table>\n");
    }

    @Override
    protected byte[] concluirDocumento() {
        html.append("</body>\n</html>\n");
        return html.toString().getBytes(StandardCharsets.UTF_8);
    }

    /** Impede que um login ou texto com {@code <}, {@code &} etc. quebre o HTML. */
    private static String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
