package informaci.log;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do padrão Adapter de log.
 *
 * <p>Verificam que:
 * <ol>
 *   <li>{@link NullLog} nunca lança exceção (implementação nula segura).</li>
 *   <li>{@link JulLogAdapter} delega corretamente ao {@link java.util.logging.Logger}
 *       com os níveis mapeados: {@code info} → INFO, {@code aviso} → WARNING,
 *       {@code erro} → SEVERE.</li>
 * </ol>
 */
class LogAdapterTest {

    // ---- NullLog ----

    @Test
    void nullLog_naoLancaExcecaoParaInfo() {
        ServicoDeLog log = new NullLog();
        assertDoesNotThrow(() -> log.info("mensagem"));
    }

    @Test
    void nullLog_naoLancaExcecaoParaAviso() {
        ServicoDeLog log = new NullLog();
        assertDoesNotThrow(() -> log.aviso("aviso"));
    }

    @Test
    void nullLog_naoLancaExcecaoParaErro() {
        ServicoDeLog log = new NullLog();
        assertDoesNotThrow(() -> log.erro("erro", new RuntimeException("causa")));
    }

    // ---- JulLogAdapter ----

    @Test
    void julLogAdapter_infoRegistraComNivelInfo() {
        List<LogRecord> registros = capturarRegistros("informaci.log.teste.info", () -> {
            ServicoDeLog log = new JulLogAdapter("informaci.log.teste.info");
            log.info("operação concluída");
        });

        assertEquals(1, registros.size());
        assertEquals(Level.INFO, registros.get(0).getLevel());
        assertEquals("operação concluída", registros.get(0).getMessage());
    }

    @Test
    void julLogAdapter_avisoRegistraComNivelWarning() {
        List<LogRecord> registros = capturarRegistros("informaci.log.teste.aviso", () -> {
            ServicoDeLog log = new JulLogAdapter("informaci.log.teste.aviso");
            log.aviso("situação inesperada");
        });

        assertEquals(1, registros.size());
        assertEquals(Level.WARNING, registros.get(0).getLevel());
        assertEquals("situação inesperada", registros.get(0).getMessage());
    }

    @Test
    void julLogAdapter_erroRegistraComNivelSevereECausa() {
        RuntimeException causa = new RuntimeException("falha interna");
        List<LogRecord> registros = capturarRegistros("informaci.log.teste.erro", () -> {
            ServicoDeLog log = new JulLogAdapter("informaci.log.teste.erro");
            log.erro("falha ao salvar", causa);
        });

        assertEquals(1, registros.size());
        assertEquals(Level.SEVERE, registros.get(0).getLevel());
        assertEquals("falha ao salvar", registros.get(0).getMessage());
        assertSame(causa, registros.get(0).getThrown());
    }

    @Test
    void julLogAdapter_nomeNuloLancaExcecao() {
        assertThrows(NullPointerException.class, () -> new JulLogAdapter(null));
    }

    // ---- auxiliar ----

    /**
     * Instala um handler temporário no logger nomeado, executa o bloco e
     * devolve os registros capturados.
     */
    private List<LogRecord> capturarRegistros(String nomeDoLogger, Runnable bloco) {
        Logger logger = Logger.getLogger(nomeDoLogger);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);

        List<LogRecord> registros = new ArrayList<>();
        Handler handler = new Handler() {
            @Override public void publish(LogRecord r) { registros.add(r); }
            @Override public void flush() {}
            @Override public void close() {}
        };
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);

        try {
            bloco.run();
        } finally {
            logger.removeHandler(handler);
        }

        return registros;
    }
}
