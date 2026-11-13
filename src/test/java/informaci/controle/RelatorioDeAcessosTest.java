package informaci.controle;

import informaci.entidade.RegistroDeAcesso;
import informaci.entidade.TipoDeOperacao;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes do Template Method: a mesma estrutura de relatório em cada formato.
 */
class RelatorioDeAcessosTest {

    private static final LocalDateTime AGORA = LocalDateTime.of(2026, 11, 12, 10, 30);

    private static final EstatisticasDeAcesso ESTATISTICAS = EstatisticasDeAcesso.calcular(List.of(
            new RegistroDeAcesso(AGORA.minusDays(1), TipoDeOperacao.CADASTRO_DE_USUARIO, "ana"),
            new RegistroDeAcesso(AGORA, TipoDeOperacao.PUBLICACAO, "coordenacao"),
            new RegistroDeAcesso(AGORA, TipoDeOperacao.CONSULTA_AO_FEED, null)), AGORA);

    private static String texto(byte[] conteudo) {
        return new String(conteudo, StandardCharsets.ISO_8859_1);
    }

    // ---- Template Method: estrutura comum ----

    @Test
    void deve_seguir_a_mesma_ordem_de_secoes_em_qualquer_formato() {
        List<String> passos = new ArrayList<>();
        GeradorDeRelatorioDeAcessos espiao = new GeradorDeRelatorioDeAcessos() {
            @Override
            public String extensao() {
                return "txt";
            }

            @Override
            protected void iniciarDocumento(String titulo) {
                passos.add("início: " + titulo);
            }

            @Override
            protected void escreverParagrafo(String texto) {
                passos.add("parágrafo: " + texto);
            }

            @Override
            protected void escreverTabela(String titulo, List<String> cabecalho, List<List<String>> linhas) {
                passos.add("tabela: " + titulo);
            }

            @Override
            protected byte[] concluirDocumento() {
                passos.add("fim");
                return new byte[0];
            }
        };

        espiao.gerar(ESTATISTICAS);

        assertEquals(List.of(
                "início: Relatório de acessos - Informa CI",
                "parágrafo: Gerado em 12/11/2026 10:30.",
                "parágrafo: Total de acessos: 3",
                "parágrafo: Período: de 11/11/2026 10:30 a 12/11/2026 10:30.",
                "tabela: Acessos por operação",
                "tabela: Acessos por dia",
                "tabela: Usuários mais ativos",
                "fim"), passos);
    }

    @Test
    void deve_criar_o_gerador_de_cada_formato() {
        assertInstanceOf(RelatorioDeAcessosEmHtml.class, FormatoDeRelatorio.HTML.criarGerador());
        assertInstanceOf(RelatorioDeAcessosEmPdf.class, FormatoDeRelatorio.PDF.criarGerador());
    }

    // ---- HTML ----

    @Test
    void html_deve_trazer_as_estatisticas_em_tabelas() {
        String html = new String(new RelatorioDeAcessosEmHtml().gerar(ESTATISTICAS), StandardCharsets.UTF_8);

        assertTrue(html.startsWith("<!DOCTYPE html>"));
        assertTrue(html.contains("<h1>Relatório de acessos - Informa CI</h1>"));
        assertTrue(html.contains("<p>Total de acessos: 3</p>"));
        assertTrue(html.contains("<tr><td>Publicação</td><td>1</td></tr>"));
        assertTrue(html.contains("<tr><td>12/11/2026</td><td>2</td></tr>"));
        assertTrue(html.contains("<tr><td>ana</td><td>1</td></tr>"));
        assertTrue(html.trim().endsWith("</html>"));
    }

    @Test
    void html_deve_escapar_caracteres_especiais() {
        EstatisticasDeAcesso comLoginEstranho = EstatisticasDeAcesso.calcular(List.of(
                new RegistroDeAcesso(AGORA, TipoDeOperacao.PUBLICACAO, "<script>")), AGORA);

        String html = new String(new RelatorioDeAcessosEmHtml().gerar(comLoginEstranho), StandardCharsets.UTF_8);

        assertTrue(html.contains("&lt;script&gt;"));
        assertTrue(!html.contains("<script>"));
    }

    @Test
    void html_deve_indicar_quando_nao_ha_acessos() {
        String html = new String(new RelatorioDeAcessosEmHtml().gerar(
                EstatisticasDeAcesso.calcular(List.of(), AGORA)), StandardCharsets.UTF_8);

        assertTrue(html.contains("Nenhum acesso registrado até o momento."));
        assertTrue(html.contains("(nenhum acesso)"));
    }

    // ---- PDF ----

    @Test
    void pdf_deve_ter_cabecalho_e_fim_de_arquivo_validos() {
        String pdf = texto(new RelatorioDeAcessosEmPdf().gerar(ESTATISTICAS));

        assertTrue(pdf.startsWith("%PDF-1.4\n"));
        assertTrue(pdf.endsWith("%%EOF\n"));
        assertTrue(pdf.contains("/BaseFont /Courier"));
    }

    @Test
    void pdf_deve_trazer_o_texto_das_estatisticas() {
        String pdf = texto(new RelatorioDeAcessosEmPdf().gerar(ESTATISTICAS));

        assertTrue(pdf.contains("(Relatório de acessos - Informa CI) Tj"));
        assertTrue(pdf.contains("(Total de acessos: 3) Tj"));
        assertTrue(pdf.contains("(Publicação"));
        assertTrue(pdf.contains("(coordenacao"));
    }

    @Test
    void pdf_deve_ter_a_tabela_de_referencias_apontando_para_cada_objeto() {
        String pdf = texto(new RelatorioDeAcessosEmPdf().gerar(ESTATISTICAS));

        int inicioDaTabela = Integer.parseInt(pdf.substring(pdf.lastIndexOf("startxref\n") + 10).split("\n")[0]);
        assertTrue(pdf.startsWith("xref", inicioDaTabela));
        Matcher entrada = Pattern.compile("(\\d{10}) 00000 n ").matcher(pdf);
        int objeto = 1;
        while (entrada.find()) {
            int posicao = Integer.parseInt(entrada.group(1));
            assertTrue(pdf.startsWith(objeto + " 0 obj", posicao), "objeto " + objeto);
            objeto++;
        }
        assertEquals(5, objeto - 1, "catálogo, páginas, fonte, 1 página e o seu conteúdo");
    }

    @Test
    void pdf_deve_quebrar_em_paginas_quando_o_relatorio_e_longo() {
        List<RegistroDeAcesso> registros = new ArrayList<>();
        for (int dia = 0; dia < 90; dia++) {
            registros.add(new RegistroDeAcesso(AGORA.minusDays(dia), TipoDeOperacao.CONSULTA_AO_FEED, null));
        }

        String pdf = texto(new RelatorioDeAcessosEmPdf().gerar(EstatisticasDeAcesso.calcular(registros, AGORA)));

        Matcher contagem = Pattern.compile("/Count (\\d+)").matcher(pdf);
        assertTrue(contagem.find());
        assertTrue(Integer.parseInt(contagem.group(1)) >= 2);
    }

    @Test
    void pdf_deve_escapar_parenteses() {
        EstatisticasDeAcesso comParenteses = EstatisticasDeAcesso.calcular(List.of(
                new RegistroDeAcesso(AGORA, TipoDeOperacao.PUBLICACAO, "a(b)")), AGORA);

        String pdf = texto(new RelatorioDeAcessosEmPdf().gerar(comParenteses));

        assertTrue(pdf.contains("a\\(b\\)"));
    }
}
