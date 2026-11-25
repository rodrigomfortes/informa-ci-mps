package informaci.controle;

/**
 * Sinaliza que não há edição de publicação para desfazer: nenhuma foi feita
 * desde que o sistema abriu, ou a última já foi desfeita.
 */
public class NadaParaDesfazerException extends RuntimeException {

    public NadaParaDesfazerException() {
        super("Não há edição de publicação para desfazer.");
    }
}
