package informaci.controle;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Relatório de acessos em PDF: subclasse concreta do Template Method
 * {@link GeradorDeRelatorioDeAcessos}.
 *
 * <p>O projeto não usa bibliotecas externas (NF-HSW-01), então o PDF é montado
 * à mão, seguindo a estrutura mínima do formato (PDF 1.4): catálogo, árvore
 * de páginas, uma fonte padrão (Courier, que já existe em todo leitor de PDF)
 * e uma página por bloco de {@value #LINHAS_POR_PAGINA} linhas. A fonte
 * monoespaçada permite alinhar as tabelas com espaços.
 */
public class RelatorioDeAcessosEmPdf extends GeradorDeRelatorioDeAcessos {

    static final int LINHAS_POR_PAGINA = 60;

    private static final int MAXIMO_DE_CARACTERES_POR_LINHA = 80;
    private static final int TAMANHO_DA_FONTE = 10;
    private static final int ENTRELINHA = 12;
    private static final int MARGEM_ESQUERDA = 50;
    private static final int TOPO = 800;

    private List<String> linhas;

    @Override
    public String extensao() {
        return "pdf";
    }

    @Override
    protected void iniciarDocumento(String titulo) {
        linhas = new ArrayList<>();
        linhas.add(titulo);
        linhas.add("=".repeat(titulo.length()));
        linhas.add("");
    }

    @Override
    protected void escreverParagrafo(String texto) {
        linhas.add(texto);
    }

    @Override
    protected void escreverTabela(String titulo, List<String> cabecalho, List<List<String>> linhasDaTabela) {
        int[] larguras = new int[cabecalho.size()];
        for (int i = 0; i < larguras.length; i++) {
            larguras[i] = cabecalho.get(i).length();
            for (List<String> linha : linhasDaTabela) {
                larguras[i] = Math.max(larguras[i], linha.get(i).length());
            }
        }
        linhas.add("");
        linhas.add(titulo);
        linhas.add(alinhar(cabecalho, larguras));
        linhas.add("-".repeat(alinhar(cabecalho, larguras).length()));
        for (List<String> linha : linhasDaTabela) {
            linhas.add(alinhar(linha, larguras));
        }
    }

    @Override
    protected byte[] concluirDocumento() {
        return montarPdf(linhas);
    }

    /** Primeira coluna à esquerda, as demais (números) à direita. */
    private static String alinhar(List<String> celulas, int[] larguras) {
        StringBuilder linha = new StringBuilder();
        for (int i = 0; i < celulas.size(); i++) {
            String formato = i == 0 ? "%-" + larguras[i] + "s" : "   %" + larguras[i] + "s";
            linha.append(String.format(formato, celulas.get(i)));
        }
        return linha.toString();
    }

    private static byte[] montarPdf(List<String> linhas) {
        List<List<String>> paginas = new ArrayList<>();
        for (int inicio = 0; inicio < linhas.size(); inicio += LINHAS_POR_PAGINA) {
            paginas.add(linhas.subList(inicio, Math.min(inicio + LINHAS_POR_PAGINA, linhas.size())));
        }

        // Objetos: 1 catálogo, 2 páginas, 3 fonte; depois, por página, a página e o seu conteúdo.
        List<String> objetos = new ArrayList<>();
        StringBuilder filhos = new StringBuilder();
        for (int i = 0; i < paginas.size(); i++) {
            filhos.append(4 + 2 * i).append(" 0 R ");
        }
        objetos.add("<< /Type /Catalog /Pages 2 0 R >>");
        objetos.add("<< /Type /Pages /Kids [" + filhos.toString().trim() + "] /Count " + paginas.size() + " >>");
        objetos.add("<< /Type /Font /Subtype /Type1 /BaseFont /Courier /Encoding /WinAnsiEncoding >>");
        for (int i = 0; i < paginas.size(); i++) {
            String conteudo = conteudoDaPagina(paginas.get(i));
            objetos.add("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] "
                    + "/Resources << /Font << /F1 3 0 R >> >> /Contents " + (5 + 2 * i) + " 0 R >>");
            objetos.add("<< /Length " + conteudo.getBytes(StandardCharsets.ISO_8859_1).length + " >>\nstream\n"
                    + conteudo + "\nendstream");
        }

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        escrever(pdf, "%PDF-1.4\n");
        long[] posicoes = new long[objetos.size()];
        for (int i = 0; i < objetos.size(); i++) {
            posicoes[i] = pdf.size();
            escrever(pdf, (i + 1) + " 0 obj\n" + objetos.get(i) + "\nendobj\n");
        }
        long inicioDaTabela = pdf.size();
        StringBuilder tabela = new StringBuilder("xref\n0 " + (objetos.size() + 1) + "\n0000000000 65535 f \n");
        for (long posicao : posicoes) {
            tabela.append(String.format("%010d 00000 n \n", posicao));
        }
        tabela.append("trailer\n<< /Size ").append(objetos.size() + 1).append(" /Root 1 0 R >>\n")
                .append("startxref\n").append(inicioDaTabela).append("\n%%EOF\n");
        escrever(pdf, tabela.toString());
        return pdf.toByteArray();
    }

    private static String conteudoDaPagina(List<String> linhasDaPagina) {
        StringBuilder conteudo = new StringBuilder()
                .append("BT\n/F1 ").append(TAMANHO_DA_FONTE).append(" Tf\n")
                .append(ENTRELINHA).append(" TL\n")
                .append(MARGEM_ESQUERDA).append(' ').append(TOPO).append(" Td\n");
        for (String linha : linhasDaPagina) {
            conteudo.append('(').append(escapar(linha)).append(") Tj T*\n");
        }
        return conteudo.append("ET").toString();
    }

    /**
     * Escapa os caracteres especiais das strings do PDF e troca por {@code ?}
     * o que a codificação da fonte (WinAnsi, compatível com Latin-1) não tem.
     */
    private static String escapar(String linha) {
        String cortada = linha.length() > MAXIMO_DE_CARACTERES_POR_LINHA
                ? linha.substring(0, MAXIMO_DE_CARACTERES_POR_LINHA) : linha;
        StringBuilder escapada = new StringBuilder();
        for (char caractere : cortada.toCharArray()) {
            if (caractere == '\\' || caractere == '(' || caractere == ')') {
                escapada.append('\\').append(caractere);
            } else if (caractere > 0xFF) {
                escapada.append('?');
            } else {
                escapada.append(caractere);
            }
        }
        return escapada.toString();
    }

    private static void escrever(ByteArrayOutputStream pdf, String texto) {
        pdf.writeBytes(texto.getBytes(StandardCharsets.ISO_8859_1));
    }
}
