package informaci;

import informaci.controle.FacadeSingletonController;
import informaci.controle.PublicacaoRepositorio;
import informaci.controle.UsuarioRepositorio;
import informaci.fronteira.TelaPrincipal;
import informaci.log.JulLogAdapter;
import informaci.log.ServicoDeLog;
import informaci.persistencia.PersistenciaException;
import informaci.persistencia.PublicacaoRepositorioEmArquivo;
import informaci.persistencia.PublicacaoRepositorioEmMemoria;
import informaci.persistencia.UsuarioRepositorioEmArquivo;
import informaci.persistencia.UsuarioRepositorioEmMemoria;

import java.nio.file.Path;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicação.
 *
 * <p>Pergunta onde os dados devem ser armazenados, monta os repositórios
 * escolhidos e inicializa com eles a fachada única. É a única classe que
 * conhece todas as camadas ao mesmo tempo, justamente para que nenhuma delas
 * precise conhecer a implementação concreta de armazenamento.
 *
 * <p>Também é o único ponto que conhece a biblioteca de log concreta: cria o
 * {@link JulLogAdapter} e o injeta na fachada, que o repassa aos gerenciadores.
 * Graças ao padrão Adapter, o restante do sistema depende apenas de
 * {@link ServicoDeLog}.
 */
public final class Aplicacao {

    private static final Path ARQUIVO_DE_USUARIOS = Path.of("usuarios.bin");
    private static final Path ARQUIVO_DE_PUBLICACOES = Path.of("publicacoes.bin");

    private Aplicacao() {
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        ServicoDeLog log = new JulLogAdapter(Aplicacao.class.getName());

        Armazenamento armazenamento = escolherArmazenamento(entrada);
        if (armazenamento == null) {
            return;
        }

        FacadeSingletonController fachada =
                FacadeSingletonController.inicializar(armazenamento.usuarios(), armazenamento.publicacoes(), log);
        new TelaPrincipal(fachada, entrada, System.out).executar();
        FacadeSingletonController.encerrar();
    }

    /** Os repositórios escolhidos para cada entidade. */
    private record Armazenamento(UsuarioRepositorio usuarios, PublicacaoRepositorio publicacoes) {
    }

    /**
     * Repete a pergunta até obter um armazenamento utilizável.
     *
     * <p>Se um arquivo existir mas não puder ser lido, a falha é informada e a
     * escolha é oferecida de novo, em vez de encerrar a aplicação.
     *
     * @return o armazenamento escolhido, ou {@code null} se a entrada terminar
     */
    private static Armazenamento escolherArmazenamento(Scanner entrada) {
        while (true) {
            System.out.println("Onde os dados devem ser armazenados?");
            System.out.println("1 - Memória RAM (os dados se perdem ao encerrar)");
            System.out.println("2 - Arquivo binário (" + ARQUIVO_DE_USUARIOS.toAbsolutePath()
                    + " e " + ARQUIVO_DE_PUBLICACOES.getFileName() + ")");
            System.out.print("Opção: ");
            if (!entrada.hasNextLine()) {
                return null;
            }

            switch (entrada.nextLine().trim()) {
                case "1" -> {
                    return new Armazenamento(new UsuarioRepositorioEmMemoria(), new PublicacaoRepositorioEmMemoria());
                }
                case "2" -> {
                    try {
                        return new Armazenamento(
                                new UsuarioRepositorioEmArquivo(ARQUIVO_DE_USUARIOS),
                                new PublicacaoRepositorioEmArquivo(ARQUIVO_DE_PUBLICACOES));
                    } catch (PersistenciaException e) {
                        System.out.println(e.getMessage() + " Causa: " + e.getCause());
                    }
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
}
