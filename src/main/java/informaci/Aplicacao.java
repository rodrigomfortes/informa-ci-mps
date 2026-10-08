package informaci;

import informaci.controle.GerenciadorDeUsuarios;
import informaci.controle.UsuarioRepositorio;
import informaci.fronteira.TelaDeUsuarios;
import informaci.persistencia.PersistenciaException;
import informaci.persistencia.UsuarioRepositorioEmArquivo;
import informaci.persistencia.UsuarioRepositorioEmMemoria;

import java.nio.file.Path;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicação.
 *
 * <p>Pergunta onde os usuários devem ser armazenados, monta o repositório
 * escolhido e o entrega ao controle. É a única classe que conhece todas as
 * camadas ao mesmo tempo, justamente para que nenhuma delas precise conhecer
 * a implementação concreta de armazenamento.
 */
public final class Aplicacao {

    private static final Path ARQUIVO_DE_USUARIOS = Path.of("usuarios.bin");

    private Aplicacao() {
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        UsuarioRepositorio repositorio = escolherRepositorio(entrada);
        if (repositorio == null) {
            return;
        }

        GerenciadorDeUsuarios gerenciador = new GerenciadorDeUsuarios(repositorio);
        new TelaDeUsuarios(gerenciador, entrada, System.out).executar();
    }

    /**
     * Repete a pergunta até obter um repositório utilizável.
     *
     * <p>Se o arquivo existir mas não puder ser lido, a falha é informada e a
     * escolha é oferecida de novo, em vez de encerrar a aplicação.
     *
     * @return o repositório escolhido, ou {@code null} se a entrada terminar
     */
    private static UsuarioRepositorio escolherRepositorio(Scanner entrada) {
        while (true) {
            System.out.println("Onde os usuários devem ser armazenados?");
            System.out.println("1 - Memória RAM (os dados se perdem ao encerrar)");
            System.out.println("2 - Arquivo binário (" + ARQUIVO_DE_USUARIOS.toAbsolutePath() + ")");
            System.out.print("Opção: ");
            if (!entrada.hasNextLine()) {
                return null;
            }

            switch (entrada.nextLine().trim()) {
                case "1" -> {
                    return new UsuarioRepositorioEmMemoria();
                }
                case "2" -> {
                    try {
                        return new UsuarioRepositorioEmArquivo(ARQUIVO_DE_USUARIOS);
                    } catch (PersistenciaException e) {
                        System.out.println(e.getMessage() + " Causa: " + e.getCause());
                    }
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
}
