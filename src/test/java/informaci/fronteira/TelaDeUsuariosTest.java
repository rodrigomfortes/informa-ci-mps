package informaci.fronteira;

import informaci.controle.GerenciadorDeUsuarios;
import informaci.entidade.Papel;
import informaci.persistencia.UsuarioRepositorioEmMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TelaDeUsuariosTest {

    private UsuarioRepositorioEmMemoria repositorio;
    private GerenciadorDeUsuarios gerenciador;

    @BeforeEach
    void criarGerenciadorComRepositorioVazio() {
        repositorio = new UsuarioRepositorioEmMemoria();
        gerenciador = new GerenciadorDeUsuarios(repositorio);
    }

    /** Executa a tela com as linhas informadas como entrada e devolve o que ela escreveu. */
    private String executar(String... linhas) {
        Scanner entrada = new Scanner(String.join("\n", linhas) + "\n");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream saida = new PrintStream(bytes, true, StandardCharsets.UTF_8);

        new TelaDeUsuarios(gerenciador, entrada, saida).executar();

        return bytes.toString(StandardCharsets.UTF_8);
    }

    @Test
    void deve_adicionar_usuario_quando_dados_sao_validos() {
        String saida = executar(
                "1", "Ana Souza", "ana@academico.ufpb.br", "anasouza", "Senha@123", "1", "0");

        assertEquals(1, repositorio.quantidade());
        assertEquals(Papel.MEMBRO, repositorio.listarTodos().get(0).getPapel());
        assertTrue(saida.contains("Usuário adicionado: anasouza"));
    }

    @Test
    void deve_exibir_mensagem_quando_login_e_invalido() {
        String saida = executar(
                "1", "Ana Souza", "ana@academico.ufpb.br", "ana123", "Senha@123", "1", "0");

        assertEquals(0, repositorio.quantidade());
        assertTrue(saida.contains("Login inválido: O login não pode conter números."));
    }

    @Test
    void deve_exibir_mensagem_quando_senha_e_invalida() {
        String saida = executar(
                "1", "Ana Souza", "ana@academico.ufpb.br", "anasouza", "fraca", "1", "0");

        assertEquals(0, repositorio.quantidade());
        assertTrue(saida.contains("Senha inválida:"));
    }

    @Test
    void deve_exibir_mensagem_quando_email_ja_esta_cadastrado() {
        String saida = executar(
                "1", "Ana Souza", "ana@academico.ufpb.br", "anasouza", "Senha@123", "1",
                "1", "Outra Pessoa", "ana@academico.ufpb.br", "outra", "Senha@123", "1",
                "0");

        assertEquals(1, repositorio.quantidade());
        assertTrue(saida.contains("Não foi possível adicionar: Já existe um usuário cadastrado"));
    }

    @Test
    void deve_pedir_o_papel_novamente_quando_opcao_e_invalida() {
        String saida = executar(
                "1", "Bruno Lima", "bruno@academico.ufpb.br", "brunolima", "Senha@123", "9", "2", "0");

        assertTrue(saida.contains("Papel inválido."));
        assertEquals(Papel.ADMINISTRADOR, repositorio.listarTodos().get(0).getPapel());
    }

    @Test
    void deve_listar_os_usuarios_cadastrados() {
        gerenciador.adicionar("Ana Souza", "ana@academico.ufpb.br", "anasouza", "Senha@123", Papel.MEMBRO);

        String saida = executar("2", "0");

        assertTrue(saida.contains("anasouza | Ana Souza | ana@academico.ufpb.br | MEMBRO"));
    }

    @Test
    void deve_informar_quando_nao_ha_usuarios_cadastrados() {
        String saida = executar("2", "0");

        assertTrue(saida.contains("Nenhum usuário cadastrado."));
    }

    @Test
    void deve_encerrar_quando_a_entrada_termina() {
        String saida = executar("2");

        assertTrue(saida.contains("Nenhum usuário cadastrado."));
    }
}
