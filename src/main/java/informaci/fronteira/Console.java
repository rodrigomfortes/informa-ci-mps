package informaci.fronteira;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * Entrada e saída de texto compartilhadas pelas telas.
 *
 * <p>Quando a entrada termina, {@link #ler} lança {@link EntradaEncerrada} em
 * vez de devolver {@code null}: assim um formulário interrompido no meio não
 * precisa testar cada campo, e cada tela encerra no seu {@code executar}.
 */
final class Console {

    private final Scanner entrada;
    private final PrintStream saida;

    Console(Scanner entrada, PrintStream saida) {
        this.entrada = Objects.requireNonNull(entrada, "A entrada é obrigatória.");
        this.saida = Objects.requireNonNull(saida, "A saída é obrigatória.");
    }

    /**
     * Exibe o rótulo e lê uma linha, devolvida como digitada, sem {@code trim}:
     * quem normaliza os dados são as entidades, e a senha deve chegar intacta.
     *
     * @throws EntradaEncerrada se a entrada tiver terminado
     */
    String ler(String rotulo) {
        saida.print(rotulo);
        if (!entrada.hasNextLine()) {
            throw new EntradaEncerrada();
        }
        return entrada.nextLine();
    }

    void escrever(String linha) {
        saida.println(linha);
    }

    /** Sinaliza que não há mais nada a ler, por exemplo com Ctrl+D ou fim de arquivo. */
    static final class EntradaEncerrada extends RuntimeException {
    }
}
