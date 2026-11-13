package informaci.controle;

import informaci.entidade.TipoDeOperacao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Gera o relatório de estatísticas de acesso (padrão <strong>Template Method</strong>).
 *
 * <p>{@link #gerar(EstatisticasDeAcesso)} é o método-modelo: fixa quais
 * seções o relatório tem e em que ordem (título, resumo, acessos por operação,
 * acessos por dia e usuários mais ativos) e prepara o conteúdo de cada uma.
 * As subclasses só decidem <em>como</em> cada parte é escrita no seu formato,
 * implementando os passos abstratos. Assim todos os formatos trazem
 * exatamente as mesmas informações, e um formato novo é uma subclasse nova.
 *
 * <p>Cada geração começa em {@link #iniciarDocumento} e termina em
 * {@link #concluirDocumento}; uma instância não deve ser usada por duas
 * gerações ao mesmo tempo.
 */
public abstract class GeradorDeRelatorioDeAcessos {

    static final String TITULO = "Relatório de acessos - Informa CI";

    private static final DateTimeFormatter DATA_E_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final List<String> NENHUM_ACESSO = List.of("(nenhum acesso)", "0");

    /**
     * Método-modelo: gera o relatório completo no formato da subclasse.
     *
     * <p>É {@code final} de propósito: a estrutura do relatório é a mesma para
     * todos os formatos.
     *
     * @return o conteúdo do arquivo do relatório
     */
    public final byte[] gerar(EstatisticasDeAcesso estatisticas) {
        Objects.requireNonNull(estatisticas, "As estatísticas são obrigatórias.");

        iniciarDocumento(TITULO);
        escreverParagrafo("Gerado em " + DATA_E_HORA.format(estatisticas.geradoEm()) + ".");
        escreverParagrafo("Total de acessos: " + estatisticas.total());
        escreverParagrafo(descreverPeriodo(estatisticas));
        escreverTabela("Acessos por operação", List.of("Operação", "Acessos"), linhasPorOperacao(estatisticas));
        escreverTabela("Acessos por dia", List.of("Dia", "Acessos"), linhasPorDia(estatisticas));
        escreverTabela("Usuários mais ativos", List.of("Login", "Acessos"), linhasPorUsuario(estatisticas));
        return concluirDocumento();
    }

    /** Extensão do arquivo gerado, sem o ponto (por exemplo, {@code html}). */
    public abstract String extensao();

    /** Passo 1: começa um documento novo com o título informado. */
    protected abstract void iniciarDocumento(String titulo);

    /** Passo repetido: escreve um parágrafo de texto. */
    protected abstract void escreverParagrafo(String texto);

    /** Passo repetido: escreve uma seção com título e uma tabela; {@code linhas} nunca é vazio. */
    protected abstract void escreverTabela(String titulo, List<String> cabecalho, List<List<String>> linhas);

    /** Último passo: fecha o documento e devolve o seu conteúdo. */
    protected abstract byte[] concluirDocumento();

    private static String descreverPeriodo(EstatisticasDeAcesso estatisticas) {
        if (estatisticas.primeiroAcesso().isEmpty()) {
            return "Nenhum acesso registrado até o momento.";
        }
        return "Período: de " + DATA_E_HORA.format(estatisticas.primeiroAcesso().get())
                + " a " + DATA_E_HORA.format(estatisticas.ultimoAcesso().orElseThrow()) + ".";
    }

    private static List<List<String>> linhasPorOperacao(EstatisticasDeAcesso estatisticas) {
        List<List<String>> linhas = new ArrayList<>();
        for (Map.Entry<TipoDeOperacao, Long> entrada : estatisticas.porOperacao().entrySet()) {
            linhas.add(List.of(entrada.getKey().getDescricao(), String.valueOf(entrada.getValue())));
        }
        return linhas;
    }

    private static List<List<String>> linhasPorDia(EstatisticasDeAcesso estatisticas) {
        List<List<String>> linhas = new ArrayList<>();
        for (Map.Entry<LocalDate, Long> entrada : estatisticas.porDia().entrySet()) {
            linhas.add(List.of(DATA.format(entrada.getKey()), String.valueOf(entrada.getValue())));
        }
        return linhas.isEmpty() ? List.of(NENHUM_ACESSO) : linhas;
    }

    private static List<List<String>> linhasPorUsuario(EstatisticasDeAcesso estatisticas) {
        List<List<String>> linhas = new ArrayList<>();
        for (EstatisticasDeAcesso.UsuarioAtivo usuario : estatisticas.usuariosMaisAtivos()) {
            linhas.add(List.of(usuario.login(), String.valueOf(usuario.acessos())));
        }
        return linhas.isEmpty() ? List.of(NENHUM_ACESSO) : linhas;
    }
}
