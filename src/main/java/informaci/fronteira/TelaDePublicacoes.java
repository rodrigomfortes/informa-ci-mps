package informaci.fronteira;

import informaci.controle.FacadeSingletonController;
import informaci.controle.PublicacaoNaoEncontradaException;
import informaci.controle.UsuarioNaoEncontradoException;
import informaci.entidade.Categoria;
import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Publicacao;
import informaci.entidade.PublicacaoInvalidaException;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Interface de console do Módulo de Notícias: publicar, ver o feed, editar e
 * remover publicações (CRUD de {@link Publicacao}).
 *
 * <p>Como as demais telas, não contém regra de negócio: coleta os dados,
 * repassa à {@link FacadeSingletonController} e traduz as exceções em
 * mensagens. As publicações são escolhidas pelo número com que aparecem na
 * lista, e não pela identidade interna, que o usuário não conhece.
 */
public class TelaDePublicacoes {

    private static final DateTimeFormatter FORMATO_DE_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    /** Digitado na edição de um campo opcional para apagar o valor atual. */
    private static final String APAGAR = "-";

    private final FacadeSingletonController fachada;
    private final Console console;

    public TelaDePublicacoes(FacadeSingletonController fachada, Scanner entrada, PrintStream saida) {
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
            console.escrever("Publicações");
            console.escrever("1 - Publicar");
            console.escrever("2 - Ver feed");
            console.escrever("3 - Editar publicação");
            console.escrever("4 - Remover publicação");
            console.escrever("0 - Voltar");
            switch (console.ler("Opção: ").trim()) {
                case "1" -> publicar();
                case "2" -> exibirFeed();
                case "3" -> editar();
                case "4" -> remover();
                case "0" -> {
                    return;
                }
                default -> console.escrever("Opção inválida.");
            }
        }
    }

    // ---- Publicar ----

    private void publicar() {
        String emailDoAutor = console.ler("E-mail do autor: ");
        String titulo = console.ler("Título: ");
        String texto = console.ler("Texto: ");
        Categoria categoria = lerCategoria("Categoria (" + opcoesDeCategoria() + "): ", null);
        LocalDateTime dataHora = lerDataHora(
                "Data e horário (dd/mm/aaaa hh:mm; em branco se não houver): ", null);
        String local = console.ler("Local (em branco se não houver): ");

        try {
            Publicacao publicacao = fachada.publicar(
                    emailDoAutor, new DadosDaPublicacao(titulo, texto, categoria, dataHora, local));
            console.escrever("Publicação criada: " + publicacao.getTitulo());
        } catch (PublicacaoInvalidaException e) {
            console.escrever("Publicação inválida: " + e.getMessage());
        } catch (UsuarioNaoEncontradoException e) {
            console.escrever("Não foi possível publicar: " + e.getMessage());
        } catch (RuntimeException e) {
            console.escrever("Falha ao armazenar a publicação: " + e.getMessage());
        }
    }

    // ---- Feed ----

    private void exibirFeed() {
        List<Publicacao> feed = fachada.listarFeed();
        if (feed.isEmpty()) {
            console.escrever("Nenhuma publicação cadastrada.");
            return;
        }
        for (int i = 0; i < feed.size(); i++) {
            exibirPublicacao(i + 1, feed.get(i));
        }
    }

    private void exibirPublicacao(int numero, Publicacao publicacao) {
        console.escrever("");
        console.escrever("%d. [%s] %s".formatted(
                numero, publicacao.getCategoria().getNome(), publicacao.getTitulo()));
        console.escrever("   Por %s em %s".formatted(
                publicacao.getAutor().getLogin(), FORMATO_DE_DATA.format(publicacao.getDataDePublicacao())));
        publicacao.getDataHora().ifPresent(
                dataHora -> console.escrever("   Quando: " + FORMATO_DE_DATA.format(dataHora)));
        publicacao.getLocal().ifPresent(local -> console.escrever("   Onde: " + local));
        console.escrever("   " + publicacao.getTexto());
    }

    // ---- Editar ----

    /** Cada campo deixado em branco mantém o valor atual. */
    private void editar() {
        Publicacao atual = escolherPublicacao();
        if (atual == null) {
            return;
        }
        console.escrever("Deixe em branco para manter o valor atual.");
        String titulo = manterSeEmBranco(console.ler("Título [" + atual.getTitulo() + "]: "), atual.getTitulo());
        String texto = manterSeEmBranco(console.ler("Texto: "), atual.getTexto());
        Categoria categoria = lerCategoria(
                "Categoria (" + opcoesDeCategoria() + ") [" + atual.getCategoria().getNome() + "]: ",
                atual.getCategoria());
        LocalDateTime dataHoraAtual = atual.getDataHora().orElse(null);
        LocalDateTime dataHora = lerDataHora(
                "Data e horário [" + formatarOuNenhum(dataHoraAtual) + "] (" + APAGAR + " apaga): ",
                dataHoraAtual);
        String localAtual = atual.getLocal().orElse(null);
        String local = lerOpcional(
                "Local [" + (localAtual == null ? "nenhum" : localAtual) + "] (" + APAGAR + " apaga): ",
                localAtual);

        try {
            Publicacao editada = fachada.editarPublicacao(
                    atual.getId(), new DadosDaPublicacao(titulo, texto, categoria, dataHora, local));
            console.escrever("Publicação editada: " + editada.getTitulo());
        } catch (PublicacaoInvalidaException e) {
            console.escrever("Publicação inválida: " + e.getMessage());
        } catch (PublicacaoNaoEncontradaException e) {
            console.escrever("Não foi possível editar: " + e.getMessage());
        } catch (RuntimeException e) {
            console.escrever("Falha ao armazenar a publicação: " + e.getMessage());
        }
    }

    // ---- Remover ----

    private void remover() {
        Publicacao publicacao = escolherPublicacao();
        if (publicacao == null) {
            return;
        }
        String resposta = console.ler("Remover \"" + publicacao.getTitulo() + "\"? (s/n): ").trim();
        if (!resposta.equalsIgnoreCase("s")) {
            console.escrever("Remoção cancelada.");
            return;
        }
        try {
            fachada.removerPublicacao(publicacao.getId());
            console.escrever("Publicação removida: " + publicacao.getTitulo());
        } catch (PublicacaoNaoEncontradaException e) {
            console.escrever("Não foi possível remover: " + e.getMessage());
        } catch (RuntimeException e) {
            console.escrever("Falha ao armazenar a remoção: " + e.getMessage());
        }
    }

    // ---- Leitura de campos ----

    /**
     * Lista as publicações numeradas e devolve a escolhida.
     *
     * @return a publicação escolhida, ou {@code null} se não houver nenhuma,
     *         se o usuário cancelar ou se o número for inválido
     */
    private Publicacao escolherPublicacao() {
        List<Publicacao> feed = fachada.listarFeed();
        if (feed.isEmpty()) {
            console.escrever("Nenhuma publicação cadastrada.");
            return null;
        }
        for (int i = 0; i < feed.size(); i++) {
            Publicacao publicacao = feed.get(i);
            console.escrever("%d. [%s] %s".formatted(
                    i + 1, publicacao.getCategoria().getNome(), publicacao.getTitulo()));
        }
        String digitado = console.ler("Número da publicação (0 cancela): ").trim();
        try {
            int numero = Integer.parseInt(digitado);
            if (numero == 0) {
                return null;
            }
            if (numero >= 1 && numero <= feed.size()) {
                return feed.get(numero - 1);
            }
        } catch (NumberFormatException e) {
            // Tratado abaixo, junto com números fora da lista.
        }
        console.escrever("Número inválido: " + digitado);
        return null;
    }

    /** Repete a pergunta até obter uma categoria; em branco devolve {@code atual}, se houver. */
    private Categoria lerCategoria(String rotulo, Categoria atual) {
        Categoria[] categorias = Categoria.values();
        while (true) {
            String digitado = console.ler(rotulo).trim();
            if (digitado.isEmpty() && atual != null) {
                return atual;
            }
            try {
                int numero = Integer.parseInt(digitado);
                if (numero >= 1 && numero <= categorias.length) {
                    return categorias[numero - 1];
                }
            } catch (NumberFormatException e) {
                // Tratado abaixo, junto com números fora da lista.
            }
            console.escrever("Categoria inválida.");
        }
    }

    /**
     * Repete a pergunta até obter uma data válida. Em branco devolve
     * {@code atual}; {@value #APAGAR} devolve {@code null}.
     */
    private LocalDateTime lerDataHora(String rotulo, LocalDateTime atual) {
        while (true) {
            String digitado = console.ler(rotulo).trim();
            if (digitado.isEmpty()) {
                return atual;
            }
            if (digitado.equals(APAGAR)) {
                return null;
            }
            try {
                return LocalDateTime.parse(digitado, FORMATO_DE_DATA);
            } catch (DateTimeParseException e) {
                console.escrever("Data e horário inválidos: use o formato dd/mm/aaaa hh:mm, como 20/11/2026 14:00.");
            }
        }
    }

    /** Em branco mantém {@code atual}; {@value #APAGAR} apaga o valor. */
    private String lerOpcional(String rotulo, String atual) {
        String digitado = console.ler(rotulo);
        if (digitado.trim().equals(APAGAR)) {
            return null;
        }
        return manterSeEmBranco(digitado, atual);
    }

    private static String manterSeEmBranco(String digitado, String atual) {
        return digitado.isBlank() ? atual : digitado;
    }

    private static String opcoesDeCategoria() {
        StringBuilder opcoes = new StringBuilder();
        Categoria[] categorias = Categoria.values();
        for (int i = 0; i < categorias.length; i++) {
            if (i > 0) {
                opcoes.append(", ");
            }
            opcoes.append(i + 1).append(" - ").append(categorias[i].getNome());
        }
        return opcoes.toString();
    }

    private static String formatarOuNenhum(LocalDateTime dataHora) {
        return dataHora == null ? "nenhum" : FORMATO_DE_DATA.format(dataHora);
    }
}
