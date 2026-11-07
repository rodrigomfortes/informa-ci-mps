package informaci.fronteira;

import informaci.controle.FacadeSingletonController;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * Menu inicial do sistema: leva às telas de usuários e de publicações e
 * mostra quantas entidades estão cadastradas.
 */
public class TelaPrincipal {

    private final FacadeSingletonController fachada;
    private final Console console;
    private final TelaDeUsuarios telaDeUsuarios;
    private final TelaDePublicacoes telaDePublicacoes;

    public TelaPrincipal(FacadeSingletonController fachada, Scanner entrada, PrintStream saida) {
        this.fachada = Objects.requireNonNull(fachada, "A fachada é obrigatória.");
        this.console = new Console(entrada, saida);
        this.telaDeUsuarios = new TelaDeUsuarios(fachada, entrada, saida);
        this.telaDePublicacoes = new TelaDePublicacoes(fachada, entrada, saida);
    }

    /** Exibe o menu até que o usuário escolha sair ou a entrada termine. */
    public void executar() {
        try {
            exibirMenu();
        } catch (Console.EntradaEncerrada e) {
            // Sem mais entrada: encerra.
        }
    }

    private void exibirMenu() {
        while (true) {
            console.escrever("");
            console.escrever("Informa CI");
            console.escrever("1 - Usuários");
            console.escrever("2 - Publicações");
            console.escrever("3 - Quantidade de entidades cadastradas");
            console.escrever("0 - Sair");
            switch (console.ler("Opção: ").trim()) {
                case "1" -> telaDeUsuarios.executar();
                case "2" -> telaDePublicacoes.executar();
                case "3" -> exibirQuantidade();
                case "0" -> {
                    return;
                }
                default -> console.escrever("Opção inválida.");
            }
        }
    }

    private void exibirQuantidade() {
        console.escrever("Entidades cadastradas: %d (%d usuários, %d publicações)".formatted(
                fachada.quantidadeDeEntidadesCadastradas(),
                fachada.quantidadeDeUsuarios(),
                fachada.quantidadeDePublicacoes()));
    }
}
