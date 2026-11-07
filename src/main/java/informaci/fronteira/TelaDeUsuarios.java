package informaci.fronteira;

import informaci.controle.EmailJaCadastradoException;
import informaci.controle.FacadeSingletonController;
import informaci.controle.UsuarioComPublicacoesException;
import informaci.controle.UsuarioNaoEncontradoException;
import informaci.entidade.DadosDoUsuario;
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
 * Interface de console para o CRUD de usuários: adicionar, listar, editar e remover.
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
            console.escrever("3 - Editar usuário");
            console.escrever("4 - Remover usuário");
            console.escrever("0 - Voltar");
            switch (console.ler("Opção: ").trim()) {
                case "1" -> adicionar();
                case "2" -> listar();
                case "3" -> editar();
                case "4" -> remover();
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

    /** Cada campo deixado em branco mantém o valor atual. */
    private void editar() {
        Usuario atual = escolherUsuario();
        if (atual == null) {
            return;
        }
        console.escrever("Deixe em branco para manter o valor atual.");
        String nome = manterSeEmBranco(console.ler("Nome [" + atual.getNome() + "]: "), atual.getNome());
        String email = manterSeEmBranco(console.ler("E-mail [" + atual.getEmail() + "]: "), atual.getEmail());
        String login = manterSeEmBranco(console.ler("Login [" + atual.getLogin() + "]: "), atual.getLogin());
        String senha = manterSeEmBranco(console.ler("Senha (em branco mantém a atual): "), atual.getSenha());
        Papel papel = lerPapel(atual.getPapel());

        try {
            Usuario editado = fachada.editarUsuario(
                    atual.getId(), new DadosDoUsuario(nome, email, login, senha, papel));
            console.escrever("Usuário editado: " + editado.getLogin());
        } catch (LoginInvalidoException e) {
            console.escrever("Login inválido: " + e.getMessage());
        } catch (SenhaInvalidaException e) {
            console.escrever("Senha inválida: " + e.getMessage());
        } catch (NomeInvalidoException | EmailInvalidoException | EmailJaCadastradoException
                 | UsuarioNaoEncontradoException e) {
            console.escrever("Não foi possível editar: " + e.getMessage());
        } catch (RuntimeException e) {
            console.escrever("Falha ao armazenar o usuário: " + e.getMessage());
        }
    }

    private void remover() {
        Usuario usuario = escolherUsuario();
        if (usuario == null) {
            return;
        }
        String resposta = console.ler("Remover o usuário " + usuario.getLogin() + "? (s/n): ").trim();
        if (!resposta.equalsIgnoreCase("s")) {
            console.escrever("Remoção cancelada.");
            return;
        }
        try {
            fachada.removerUsuario(usuario.getId());
            console.escrever("Usuário removido: " + usuario.getLogin());
        } catch (UsuarioComPublicacoesException | UsuarioNaoEncontradoException e) {
            console.escrever("Não foi possível remover: " + e.getMessage());
        } catch (RuntimeException e) {
            console.escrever("Falha ao armazenar a remoção: " + e.getMessage());
        }
    }

    /**
     * Lista os usuários numerados e devolve o escolhido.
     *
     * @return o usuário escolhido, ou {@code null} se não houver nenhum, se o
     *         usuário cancelar ou se o número for inválido
     */
    private Usuario escolherUsuario() {
        List<Usuario> usuarios = fachada.listarUsuarios();
        if (usuarios.isEmpty()) {
            console.escrever("Nenhum usuário cadastrado.");
            return null;
        }
        for (int i = 0; i < usuarios.size(); i++) {
            console.escrever((i + 1) + ". " + formatar(usuarios.get(i)));
        }
        String digitado = console.ler("Número do usuário (0 cancela): ").trim();
        try {
            int numero = Integer.parseInt(digitado);
            if (numero == 0) {
                return null;
            }
            if (numero >= 1 && numero <= usuarios.size()) {
                return usuarios.get(numero - 1);
            }
        } catch (NumberFormatException e) {
            // Tratado abaixo, junto com números fora da lista.
        }
        console.escrever("Número inválido: " + digitado);
        return null;
    }

    private static String manterSeEmBranco(String digitado, String atual) {
        return digitado.isBlank() ? atual : digitado;
    }

    private static String formatar(Usuario usuario) {
        return "%s | %s | %s | %s".formatted(
                usuario.getLogin(), usuario.getNome(), usuario.getEmail(), usuario.getPapel());
    }

    private Papel lerPapel() {
        return lerPapel(null);
    }

    /** Repete a pergunta até obter um papel; em branco devolve {@code atual}, se houver. */
    private Papel lerPapel(Papel atual) {
        String rotulo = "Papel (1 - Membro, 2 - Administrador, 3 - Publicador)"
                + (atual == null ? "" : " [" + atual + "]") + ": ";
        while (true) {
            String digitado = console.ler(rotulo).trim();
            if (digitado.isEmpty() && atual != null) {
                return atual;
            }
            switch (digitado) {
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
            console.escrever(formatar(usuario));
        }
    }
}
