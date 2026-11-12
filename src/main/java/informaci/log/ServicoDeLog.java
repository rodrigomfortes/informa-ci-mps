package informaci.log;

/**
 * Contrato de registro de eventos do sistema.
 *
 * <p><strong>Padrão Adapter – Target:</strong> interface que a camada de controle
 * enxerga e usa. Nenhum gerenciador depende de uma biblioteca de log concreta;
 * ele apenas chama os métodos desta interface. Uma implementação real (como
 * {@link JulLogAdapter}) adapta a lib concreta; uma implementação nula
 * ({@link NullLog}) silencia os registros, o que convém nos testes.
 *
 * <p>Os três níveis cobrem as necessidades do sistema:
 * <ul>
 *   <li>{@link #info} – confirmação de operações concluídas com sucesso;</li>
 *   <li>{@link #aviso} – situações inesperadas que não impedem a operação;</li>
 *   <li>{@link #erro} – falhas que interrompem uma operação.</li>
 * </ul>
 */
public interface ServicoDeLog {

    /**
     * Registra uma mensagem informativa.
     *
     * @param mensagem texto com as informações do evento
     */
    void info(String mensagem);

    /**
     * Registra um aviso sobre uma situação inesperada mas recuperável.
     *
     * @param mensagem texto descrevendo o aviso
     */
    void aviso(String mensagem);

    /**
     * Registra um erro associado a uma exceção.
     *
     * @param mensagem texto descrevendo o contexto do erro
     * @param causa    exceção que originou o erro
     */
    void erro(String mensagem, Throwable causa);
}
