package informaci.fronteira;

import informaci.controle.EmailJaCadastradoException;
import informaci.controle.GerenciadorDeUsuarios;
import informaci.entidade.LoginInvalidoException;
import informaci.entidade.Papel;
import informaci.entidade.SenhaInvalidaException;
import informaci.entidade.Usuario;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Interface de console para adicionar e listar usuários.
 *
 * <p>Não contém regra de negócio: apenas coleta os dados, repassa ao
 * {@link GerenciadorDeUsuarios} e traduz as exceções lançadas em mensagens
 * para o usuário. Entrada e saída são recebidas pelo construtor para que a
 * tela possa ser exercitada em testes sem um terminal de verdade.
 */
public class TelaDeUsuarios {

    private final GerenciadorDeUsuarios gerenciador;
    private final Scanner entrada;
    private final PrintStream saida;

    public TelaDeUsuarios(GerenciadorDeUsuarios gerenciador, Scanner entrada, PrintStream saida) {
        this.gerenciador = Objects.requireNonNull(gerenciador, "O gerenciador de usuários é obrigatório.");
        this.entrada = Objects.requireNonNull(entrada, "A entrada é obrigatória.");
        this.saida = Objects.requireNonNull(saida, "A saída é obrigatória.");
    }

    /** Exibe o menu até que o usuário escolha sair ou a entrada termine. */
    public void executar() {
        while (true) {
            saida.println();
            saida.println("1 - Adicionar usuário");
            saida.println("2 - Listar usuários");
            saida.println("0 - Sair");
            String opcao = ler("Opção: ");
            if (opcao == null) {
                return;
            }
            switch (opcao.trim()) {
                case "1" -> adicionar();
                case "2" -> listar();
                case "0" -> {
                    return;
                }
                default -> saida.println("Opção inválida.");
            }
        }
    }

    /**
     * Coleta os dados e tenta adicionar o usuário.
     *
     * <p>A ordem dos {@code catch} importa: {@link LoginInvalidoException} e
     * {@link SenhaInvalidaException} estendem {@link IllegalArgumentException}
     * e por isso precisam vir antes dela. O último {@code catch} cobre falhas
     * de armazenamento, que chegam como exceção não verificada porque a
     * fronteira não conhece a camada de persistência.
     */
    private void adicionar() {
        String nome = ler("Nome: ");
        String email = ler("E-mail: ");
        String login = ler("Login: ");
        String senha = ler("Senha: ");
        Papel papel = lerPapel();
        if (papel == null) {
            return;
        }

        try {
            Usuario usuario = gerenciador.adicionar(nome, email, login, senha, papel);
            saida.println("Usuário adicionado: " + usuario.getLogin());
        } catch (LoginInvalidoException e) {
            saida.println("Login inválido: " + e.getMessage());
        } catch (SenhaInvalidaException e) {
            saida.println("Senha inválida: " + e.getMessage());
        } catch (EmailJaCadastradoException | IllegalArgumentException e) {
            saida.println("Não foi possível adicionar: " + e.getMessage());
        } catch (RuntimeException e) {
            saida.println("Falha ao armazenar o usuário: " + e.getMessage());
        }
    }

    private Papel lerPapel() {
        while (true) {
            String opcao = ler("Papel (1 - Membro, 2 - Administrador): ");
            if (opcao == null) {
                return null;
            }
            switch (opcao.trim()) {
                case "1" -> {
                    return Papel.MEMBRO;
                }
                case "2" -> {
                    return Papel.ADMINISTRADOR;
                }
                default -> saida.println("Papel inválido.");
            }
        }
    }

    private void listar() {
        List<Usuario> usuarios = gerenciador.listarTodos();
        if (usuarios.isEmpty()) {
            saida.println("Nenhum usuário cadastrado.");
            return;
        }
        for (Usuario usuario : usuarios) {
            saida.println("%s | %s | %s | %s".formatted(
                    usuario.getLogin(), usuario.getNome(), usuario.getEmail(), usuario.getPapel()));
        }
    }

    /**
     * Lê uma linha da entrada, ou devolve {@code null} se ela tiver terminado.
     *
     * <p>A linha é devolvida como digitada, sem {@code trim}: quem normaliza
     * nome, e-mail e login é {@link Usuario}, e a senha deve chegar intacta.
     */
    private String ler(String rotulo) {
        saida.print(rotulo);
        if (!entrada.hasNextLine()) {
            return null;
        }
        return entrada.nextLine();
    }
}
