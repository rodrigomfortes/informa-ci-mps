package informaci.controle;

/**
 * Implementação de {@link ServicoDeLog} que descarta todos os registros.
 *
 * <p>Usada pelos testes e em qualquer contexto em que o registro de eventos
 * seja indesejável. Elimina a necessidade de passar {@code null} como
 * dependência e evita NullPointerException nas classes clientes.
 */
public final class NullLog implements ServicoDeLog {

    @Override
    public void info(String mensagem) {
        // Descartado intencionalmente.
    }

    @Override
    public void aviso(String mensagem) {
        // Descartado intencionalmente.
    }

    @Override
    public void erro(String mensagem, Throwable causa) {
        // Descartado intencionalmente.
    }
}
