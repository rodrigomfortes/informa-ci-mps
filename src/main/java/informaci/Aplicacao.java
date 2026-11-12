package informaci;

import informaci.controle.FabricaDeRepositorios;
import informaci.controle.FacadeSingletonController;
import informaci.fronteira.TelaPrincipal;
import informaci.persistencia.PersistenciaException;
import informaci.persistencia.TipoDeArmazenamento;

import java.nio.file.Path;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicação.
 *
 * <p>Pergunta onde os dados devem ser armazenados, obtém do
 * {@link TipoDeArmazenamento} escolhido a fábrica de repositórios
 * correspondente e inicializa com ela a fachada única. Não conhece nenhuma
 * classe concreta de repositório: quem as cria são as fábricas (ADR-0003).
 */
public final class Aplicacao {

    /** Pasta onde ficam os arquivos de dados: a pasta de execução. */
    private static final Path PASTA_DE_DADOS = Path.of("");

    private Aplicacao() {
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        FacadeSingletonController fachada = inicializarFachada(entrada);
        if (fachada == null) {
            return;
        }

        new TelaPrincipal(fachada, entrada, System.out).executar();
        FacadeSingletonController.encerrar();
    }

    /**
     * Repete a escolha do armazenamento até conseguir inicializar a fachada.
     *
     * <p>Se um arquivo existir mas não puder ser lido, a falha é informada e a
     * escolha é oferecida de novo, em vez de encerrar a aplicação.
     *
     * @return a fachada inicializada, ou {@code null} se a entrada terminar
     */
    private static FacadeSingletonController inicializarFachada(Scanner entrada) {
        while (true) {
            TipoDeArmazenamento tipo = escolherArmazenamento(entrada);
            if (tipo == null) {
                return null;
            }

            FabricaDeRepositorios fabrica = tipo.criarFabrica(PASTA_DE_DADOS);
            try {
                return FacadeSingletonController.inicializar(fabrica);
            } catch (PersistenciaException e) {
                System.out.println(e.getMessage() + " Causa: " + e.getCause());
            }
        }
    }

    /**
     * Exibe um item de menu por {@link TipoDeArmazenamento} e repete a
     * pergunta até receber uma opção válida.
     *
     * @return o tipo escolhido, ou {@code null} se a entrada terminar
     */
    private static TipoDeArmazenamento escolherArmazenamento(Scanner entrada) {
        TipoDeArmazenamento[] tipos = TipoDeArmazenamento.values();
        while (true) {
            System.out.println("Onde os dados devem ser armazenados?");
            for (int i = 0; i < tipos.length; i++) {
                System.out.println((i + 1) + " - " + tipos[i].descricao(PASTA_DE_DADOS));
            }
            System.out.print("Opção: ");
            if (!entrada.hasNextLine()) {
                return null;
            }

            String opcao = entrada.nextLine().trim();
            for (int i = 0; i < tipos.length; i++) {
                if (opcao.equals(String.valueOf(i + 1))) {
                    return tipos[i];
                }
            }
            System.out.println("Opção inválida.");
        }
    }
}
