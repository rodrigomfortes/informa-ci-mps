package informaci.fronteira;

import informaci.controle.FacadeSingletonController;
import informaci.entidade.Categoria;
import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Papel;
import informaci.entidade.Publicacao;
import informaci.persistencia.FabricaDeRepositoriosEmMemoria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TelaDePublicacoesTest {

    private static final String EMAIL_DO_PUBLICADOR = "coordenacao@ci.ufpb.br";

    private FacadeSingletonController fachada;

    @BeforeEach
    void criarFachadaComUmPublicador() {
        fachada = FacadeSingletonController.inicializar(new FabricaDeRepositoriosEmMemoria());
        fachada.adicionarUsuario("Coordenação", EMAIL_DO_PUBLICADOR, "coordenacao", "Senha@123", Papel.PUBLICADOR);
    }

    @AfterEach
    void encerrarFachada() {
        FacadeSingletonController.encerrar();
    }

    /** Executa a tela com as linhas informadas como entrada e devolve o que ela escreveu. */
    private String executar(String... linhas) {
        Scanner entrada = new Scanner(String.join("\n", linhas) + "\n");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream saida = new PrintStream(bytes, true, StandardCharsets.UTF_8);

        new TelaDePublicacoes(fachada, entrada, saida).executar();

        return bytes.toString(StandardCharsets.UTF_8);
    }

    private Publicacao publicarComunicado(String titulo) {
        return fachada.publicar(EMAIL_DO_PUBLICADOR,
                new DadosDaPublicacao(titulo, "Texto do comunicado.", Categoria.COMUNICADO, null, null));
    }

    @Test
    void deve_publicar_palestra_com_data_e_local() {
        String saida = executar("1", EMAIL_DO_PUBLICADOR, "Introdução a Rust", "Palestra aberta.",
                "3", "20/11/2026 14:00", "Auditório do CI", "0");

        Publicacao publicacao = fachada.listarFeed().get(0);
        assertTrue(saida.contains("Publicação criada: Introdução a Rust"));
        assertEquals(Categoria.PALESTRA, publicacao.getCategoria());
        assertEquals(LocalDateTime.of(2026, 11, 20, 14, 0), publicacao.getDataHora().orElseThrow());
    }

    @Test
    void deve_pedir_a_data_novamente_quando_formato_e_invalido() {
        String saida = executar("1", EMAIL_DO_PUBLICADOR, "Maratona", "Maratona de programação.",
                "2", "31/02/2026 10:00", "20/11/2026 10:00", "Lab. 3", "0");

        assertTrue(saida.contains("Data e horário inválidos"));
        assertEquals(1, fachada.quantidadeDePublicacoes());
    }

    @Test
    void deve_exibir_mensagem_quando_evento_nao_tem_data() {
        String saida = executar("1", EMAIL_DO_PUBLICADOR, "Maratona", "Maratona de programação.",
                "2", "", "Lab. 3", "0");

        assertTrue(saida.contains("Publicação inválida: Publicações da categoria Evento exigem data e horário."));
        assertEquals(0, fachada.quantidadeDePublicacoes());
    }

    @Test
    void deve_exibir_mensagem_quando_autor_nao_esta_cadastrado() {
        String saida = executar("1", "ninguem@ci.ufpb.br", "Aviso", "Texto.", "4", "", "", "0");

        assertTrue(saida.contains(
                "Não foi possível publicar: Não existe usuário cadastrado com o e-mail ninguem@ci.ufpb.br."));
    }

    @Test
    void deve_exibir_o_feed_da_mais_recente_para_a_mais_antiga() {
        publicarComunicado("Primeira");
        publicarComunicado("Segunda");

        String saida = executar("2", "0");

        assertTrue(saida.indexOf("1. [Comunicado] Segunda") < saida.indexOf("2. [Comunicado] Primeira"));
        assertTrue(saida.contains("Por coordenacao em"));
    }

    @Test
    void deve_informar_quando_nao_ha_publicacoes() {
        assertTrue(executar("2", "0").contains("Nenhuma publicação cadastrada."));
    }

    @Test
    void deve_editar_mantendo_os_campos_deixados_em_branco() {
        Publicacao original = publicarComunicado("Mudança de sala");

        String saida = executar("3", "1", "Mudança de sala da disciplina", "", "", "", "", "0");

        Publicacao editada = fachada.listarFeed().get(0);
        assertTrue(saida.contains("Publicação editada: Mudança de sala da disciplina"));
        assertEquals(original.getId(), editada.getId());
        assertEquals("Texto do comunicado.", editada.getTexto());
        assertEquals(Categoria.COMUNICADO, editada.getCategoria());
    }

    @Test
    void deve_remover_apos_confirmacao() {
        publicarComunicado("Aviso antigo");

        String saida = executar("4", "1", "s", "0");

        assertTrue(saida.contains("Publicação removida: Aviso antigo"));
        assertEquals(0, fachada.quantidadeDePublicacoes());
    }

    @Test
    void nao_deve_remover_sem_confirmacao() {
        publicarComunicado("Aviso");

        String saida = executar("4", "1", "n", "0");

        assertTrue(saida.contains("Remoção cancelada."));
        assertEquals(1, fachada.quantidadeDePublicacoes());
    }

    @Test
    void deve_avisar_quando_numero_da_publicacao_e_invalido() {
        publicarComunicado("Aviso");

        String saida = executar("4", "7", "0");

        assertTrue(saida.contains("Número inválido: 7"));
        assertEquals(1, fachada.quantidadeDePublicacoes());
    }

    @Test
    void deve_encerrar_quando_a_entrada_termina_no_meio_do_formulario() {
        String saida = executar("1", EMAIL_DO_PUBLICADOR, "Título");

        assertTrue(saida.contains("Texto: "));
        assertEquals(0, fachada.quantidadeDePublicacoes());
    }
}
