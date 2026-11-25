package informaci.controle;

/**
 * Operação da camada de negócio encapsulada como objeto (padrão Command).
 *
 * <p>Cada operação da fachada vira uma implementação desta interface, que
 * guarda os parâmetros recebidos e sabe se executar.
 *
 * @param <R> tipo do resultado da operação ({@link Void} quando não há)
 */
public interface Comando<R> {
    R executar();
}
