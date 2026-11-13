package informaci.log;

import informaci.controle.ServicoDeLog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Adapta {@link java.util.logging.Logger} (JUL — Java Util Logging) para a
 * interface {@link ServicoDeLog} do sistema.
 *
 * <p><strong>Padrão Adapter – Adapter:</strong> esta classe é o elo entre o
 * <em>target</em> ({@code ServicoDeLog}) e o <em>adaptee</em>
 * ({@link java.util.logging.Logger}). A camada de controle conhece apenas a
 * interface {@code ServicoDeLog}; a dependência de {@code java.util.logging}
 * fica encapsulada aqui. Para substituir a biblioteca de log basta criar um
 * novo adapter — o restante do código não muda.
 *
 * <pre>
 *   ServicoDeLog ◄──── JulLogAdapter ────► java.util.logging.Logger
 *   (target)             (adapter)              (adaptee)
 * </pre>
 *
 * <p>O {@link java.util.logging.Logger} é o <em>adaptee</em>: uma classe
 * preexistente com interface incompatível com a que o sistema espera. O
 * mapeamento de níveis é:
 * <ul>
 *   <li>{@code info}  → {@link Level#INFO}</li>
 *   <li>{@code aviso} → {@link Level#WARNING}</li>
 *   <li>{@code erro}  → {@link Level#SEVERE}</li>
 * </ul>
 */
public final class JulLogAdapter implements ServicoDeLog {

    /** O adaptee: logger concreto da JUL, com interface incompatível com ServicoDeLog. */
    private final Logger logger;

    /**
     * Cria o adapter com o logger nomeado.
     *
     * @param nome nome do logger, geralmente o nome completo da classe cliente
     * @throws NullPointerException se o nome for nulo
     */
    public JulLogAdapter(String nome) {
        this.logger = Logger.getLogger(Objects.requireNonNull(nome, "O nome do logger é obrigatório."));
    }

    /**
     * Cria o adapter gravando os registros no arquivo informado, e só nele.
     *
     * <p>Por padrão o JUL escreve no console, o que misturaria o log com os
     * menus da interface de console. Aqui a saída padrão é desligada
     * ({@code setUseParentHandlers(false)}) e os registros vão, acrescentados,
     * para o arquivo.
     *
     * @param nome    nome do logger
     * @param arquivo arquivo de log, criado se não existir
     * @throws IOException se o arquivo não puder ser aberto para escrita
     */
    public static JulLogAdapter gravandoEm(String nome, Path arquivo) throws IOException {
        Objects.requireNonNull(arquivo, "O arquivo de log é obrigatório.");
        JulLogAdapter adapter = new JulLogAdapter(nome);
        FileHandler arquivoDeLog = new FileHandler(arquivo.toString(), true);
        arquivoDeLog.setFormatter(new SimpleFormatter());
        arquivoDeLog.setEncoding(StandardCharsets.UTF_8.name());
        adapter.logger.setUseParentHandlers(false);
        adapter.logger.addHandler(arquivoDeLog);
        return adapter;
    }

    /**
     * Adapta {@link ServicoDeLog#info} para {@link Logger#info}.
     */
    @Override
    public void info(String mensagem) {
        logger.info(mensagem);
    }

    /**
     * Adapta {@link ServicoDeLog#aviso} para {@link Logger#warning}.
     *
     * <p>O método {@code aviso} do target corresponde ao nível {@code WARNING}
     * do adaptee: a tradução de nomes é a principal adaptação feita aqui.
     */
    @Override
    public void aviso(String mensagem) {
        logger.warning(mensagem);
    }

    /**
     * Adapta {@link ServicoDeLog#erro} para {@link Logger#log} com {@link Level#SEVERE}.
     *
     * <p>O adaptee aceita a causa como {@code Throwable} no método
     * {@link Logger#log(Level, String, Throwable)}, que mapeia para
     * {@link ServicoDeLog#erro(String, Throwable)}.
     */
    @Override
    public void erro(String mensagem, Throwable causa) {
        logger.log(Level.SEVERE, mensagem, causa);
    }
}
