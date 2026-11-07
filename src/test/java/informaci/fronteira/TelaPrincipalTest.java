package informaci.fronteira;

import informaci.controle.FacadeSingletonController;
import informaci.entidade.Categoria;
import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Papel;
import informaci.persistencia.PublicacaoRepositorioEmMemoria;
import informaci.persistencia.UsuarioRepositorioEmMemoria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TelaPrincipalTest {

    private FacadeSingletonController fachada;

    @BeforeEach
    void inicializarFachada() {
        fachada = FacadeSingletonController.inicializar(
                new UsuarioRepositorioEmMemoria(), new PublicacaoRepositorioEmMemoria());
    }

    @AfterEach
    void encerrarFachada() {
        FacadeSingletonController.encerrar();
    }

    private String executar(String... linhas) {
        Scanner entrada = new Scanner(String.join("\n", linhas) + "\n");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream saida = new PrintStream(bytes, true, StandardCharsets.UTF_8);

        new TelaPrincipal(fachada, entrada, saida).executar();

        return bytes.toString(StandardCharsets.UTF_8);
    }

    @Test
    void deve_exibir_a_quantidade_de_entidades_cadastradas() {
        fachada.adicionarUsuario("Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", "Senha@123", Papel.PUBLICADOR);
        fachada.publicar("coordenacao@ci.ufpb.br",
                new DadosDaPublicacao("Aviso", "Texto.", Categoria.COMUNICADO, null, null));

        String saida = executar("3", "0");

        assertTrue(saida.contains("Entidades cadastradas: 2 (1 usuários, 1 publicações)"));
    }

    @Test
    void deve_navegar_ate_as_telas_de_usuarios_e_de_publicacoes() {
        String saida = executar("1", "2", "0", "2", "2", "0", "0");

        assertTrue(saida.contains("Nenhum usuário cadastrado."));
        assertTrue(saida.contains("Nenhuma publicação cadastrada."));
    }

    @Test
    void deve_encerrar_quando_a_entrada_termina_dentro_de_uma_subtela() {
        String saida = executar("2", "2");

        assertTrue(saida.contains("Nenhuma publicação cadastrada."));
    }
}
