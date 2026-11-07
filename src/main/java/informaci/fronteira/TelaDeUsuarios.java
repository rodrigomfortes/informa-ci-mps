package informaci.fronteira;

import informaci.controle.EmailJaCadastradoException;
import informaci.controle.FacadeSingletonController;
import informaci.entidade.EmailInvalidoException;
import informaci.entidade.LoginInvalidoException;
import informaci.entidade.NomeInvalidoException;
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
 * <p>Não contém regra de negócio: apenas coleta os dados, repassa à
 * {@link FacadeSingletonController} e traduz as exceções lançadas em mensagens
 * para o usuário. Entrada e saída são recebidas pelo construtor para que a
 * tela possa ser exercitada em testes sem um terminal de verdade.
 */
public class TelaDeUsuarios {

    private final FacadeSingletonController fachada;
    private final Console console;

    public TelaDeUsuarios(FacadeSingletonController fachada, Scanner entrada, PrintStream saida) {
        this.fachada = Objects.requireNonNull(fachada, "A fachada é obrigatória.");
        this.console = new Console(entrada, saida);
    }

    /** Exibe o menu até que o usuário escolha voltar ou a entrada termine. */
    public void executar() {
        try {
            exibirMenu();
        } catch (Console.EntradaEncerrada e) {
            // Sem mais entrada: volta a quem chamou.
        }
    }

    private void exibirMenu() {
        while (true) {
            console.escrever("");
            console.escrever("Usuários");
            console.escrever("1 - Adicionar usuário");
            console.escrever("2 - Listar usuários");
            console.escrever("0 - Voltar");
            switch (console.ler("Opção: ").trim()) {
                case "1" -> adicionar();
                case "2" -> listar();
                case "0" -> {
                    return;
                }
                default -> console.escrever("Opção inválida.");
            }
        }
    }

    /**
     * Coleta os dados e tenta adicionar o usuário.
     *
     * <p>Cada exceção de negócio vira uma mensagem que nomeia o campo com
     * problema (NF-USA-01). O último {@code catch} cobre falhas
     * de armazenamento, que chegam como exceção não verificada porque a
     * fronteira não conhece a camada de persistência.
     */
    private void adicionar() {
        String nome = console.ler("Nome: ");
        String email = console.ler("E-mail: ");
        String login = console.ler("Login: ");
        String senha = console.ler("Senha: ");
        Papel papel = lerPapel();

        try {
            Usuario usuario = fachada.adicionarUsuario(nome, email, login, senha, papel);
            console.escrever("Usuário adicionado: " + usuario.getLogin());
        } catch (LoginInvalidoException e) {
            console.escrever("Login inválido: " + e.getMessage());
        } catch (SenhaInvalidaException e) {
            console.escrever("Senha inválida: " + e.getMessage());
        } catch (NomeInvalidoException | EmailInvalidoException | EmailJaCadastradoException e) {
            console.escrever("Não foi possível adicionar: " + e.getMessage());
        } catch (RuntimeException e) {
            console.escrever("Falha ao armazenar o usuário: " + e.getMessage());
        }
    }

    private Papel lerPapel() {
        while (true) {
            switch (console.ler("Papel (1 - Membro, 2 - Administrador, 3 - Publicador): ").trim()) {
                case "1" -> {
                    return Papel.MEMBRO;
                }
                case "2" -> {
                    return Papel.ADMINISTRADOR;
                }
                case "3" -> {
                    return Papel.PUBLICADOR;
                }
                default -> console.escrever("Papel inválido.");
            }
        }
    }

    private void listar() {
        List<Usuario> usuarios = fachada.listarUsuarios();
        if (usuarios.isEmpty()) {
            console.escrever("Nenhum usuário cadastrado.");
            return;
        }
        for (Usuario usuario : usuarios) {
            console.escrever("%s | %s | %s | %s".formatted(
                    usuario.getLogin(), usuario.getNome(), usuario.getEmail(), usuario.getPapel()));
        }
    }
}
