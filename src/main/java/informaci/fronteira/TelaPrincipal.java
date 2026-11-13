package informaci.fronteira;

import informaci.controle.FacadeSingletonController;
import informaci.controle.FormatoDeRelatorio;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

/**
 * Menu inicial do sistema: leva às telas de usuários e de publicações e
 * mostra quantas entidades estão cadastradas. Também gera o relatório de
 * acessos (RF13), gravado como {@value #NOME_DO_RELATORIO}.html ou .pdf.
 */
public class TelaPrincipal {

    private static final String NOME_DO_RELATORIO = "relatorio-de-acessos";

    private final FacadeSingletonController fachada;
    private final Console console;
    private final TelaDeUsuarios telaDeUsuarios;
    private final TelaDePublicacoes telaDePublicacoes;
    private final Path pastaDosRelatorios;

    public TelaPrincipal(FacadeSingletonController fachada, Scanner entrada, PrintStream saida) {
        this(fachada, entrada, saida, Path.of(""));
    }

    /**
     * @param pastaDosRelatorios pasta onde o relatório de acessos é gravado
     */
    TelaPrincipal(FacadeSingletonController fachada, Scanner entrada, PrintStream saida, Path pastaDosRelatorios) {
        this.pastaDosRelatorios = pastaDosRelatorios;
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
            console.escrever("4 - Relatório de acessos");
            console.escrever("0 - Sair");
            switch (console.ler("Opção: ").trim()) {
                case "1" -> telaDeUsuarios.executar();
                case "2" -> telaDePublicacoes.executar();
                case "3" -> exibirQuantidade();
                case "4" -> gerarRelatorioDeAcessos();
                case "0" -> {
                    return;
                }
                default -> console.escrever("Opção inválida.");
            }
        }
    }

    /** Pede o formato, gera o relatório e o grava na pasta dos relatórios. */
    private void gerarRelatorioDeAcessos() {
        FormatoDeRelatorio formato = lerFormato();
        byte[] relatorio = fachada.gerarRelatorioDeAcessos(formato);
        Path arquivo = pastaDosRelatorios.resolve(
                NOME_DO_RELATORIO + "." + formato.criarGerador().extensao()).toAbsolutePath();
        try {
            Files.write(arquivo, relatorio);
            console.escrever("Relatório gerado: " + arquivo);
        } catch (IOException e) {
            console.escrever("Não foi possível gravar o relatório em " + arquivo + ": " + e.getMessage());
        }
    }

    private FormatoDeRelatorio lerFormato() {
        FormatoDeRelatorio[] formatos = FormatoDeRelatorio.values();
        StringBuilder rotulo = new StringBuilder("Formato (");
        for (int i = 0; i < formatos.length; i++) {
            rotulo.append(i > 0 ? ", " : "").append(i + 1).append(" - ").append(formatos[i].getNome());
        }
        rotulo.append("): ");
        while (true) {
            String digitado = console.ler(rotulo.toString()).trim();
            for (int i = 0; i < formatos.length; i++) {
                if (digitado.equals(String.valueOf(i + 1))) {
                    return formatos[i];
                }
            }
            console.escrever("Formato inválido.");
        }
    }

    private void exibirQuantidade() {
        console.escrever("Entidades cadastradas: %d (%d usuários, %d publicações)".formatted(
                fachada.quantidadeDeEntidadesCadastradas(),
                fachada.quantidadeDeUsuarios(),
                fachada.quantidadeDePublicacoes()));
    }
}
