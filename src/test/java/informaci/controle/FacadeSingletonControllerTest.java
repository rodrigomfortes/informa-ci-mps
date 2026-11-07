package informaci.controle;

import informaci.entidade.Usuario;
import informaci.entidade.DadosDoUsuario;
import informaci.entidade.Categoria;
import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Papel;
import informaci.entidade.Publicacao;
import informaci.entidade.PublicacaoInvalidaException;
import informaci.persistencia.PublicacaoRepositorioEmMemoria;
import informaci.persistencia.UsuarioRepositorioEmMemoria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FacadeSingletonControllerTest {

    private static final String SENHA = "Senha@123";
    private static final DadosDaPublicacao COMUNICADO =
            new DadosDaPublicacao("Mudança de sala", "Aula na sala 101.", Categoria.COMUNICADO, null, null);

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

    @Test
    void deve_devolver_sempre_a_mesma_instancia() {
        assertSame(fachada, FacadeSingletonController.getInstancia());
        assertSame(FacadeSingletonController.getInstancia(), FacadeSingletonController.getInstancia());
    }

    @Test
    void deve_recusar_uma_segunda_inicializacao() {
        assertThrows(IllegalStateException.class, () -> FacadeSingletonController.inicializar(
                new UsuarioRepositorioEmMemoria(), new PublicacaoRepositorioEmMemoria()));
    }

    @Test
    void deve_recusar_obter_a_instancia_antes_de_inicializar() {
        FacadeSingletonController.encerrar();

        assertThrows(IllegalStateException.class, FacadeSingletonController::getInstancia);
    }

    @Test
    void deve_recusar_repositorio_nulo() {
        FacadeSingletonController.encerrar();

        assertThrows(NullPointerException.class,
                () -> FacadeSingletonController.inicializar(null, new PublicacaoRepositorioEmMemoria()));
    }

    @Test
    void deve_publicar_em_nome_do_usuario_com_o_email_informado() {
        fachada.adicionarUsuario("Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", SENHA, Papel.PUBLICADOR);

        Publicacao publicacao = fachada.publicar("  COORDENACAO@ci.ufpb.br ", COMUNICADO);

        assertEquals("coordenacao", publicacao.getAutor().getLogin());
        assertEquals(1, fachada.listarFeed().size());
    }

    @Test
    void deve_recusar_publicar_com_email_nao_cadastrado() {
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> fachada.publicar("ninguem@ci.ufpb.br", COMUNICADO));
    }

    @Test
    void deve_recusar_publicar_por_usuario_que_nao_e_publicador() {
        fachada.adicionarUsuario("Ana Souza", "ana@academico.ufpb.br", "anasouza", SENHA, Papel.MEMBRO);

        assertThrows(PublicacaoInvalidaException.class, () -> fachada.publicar("ana@academico.ufpb.br", COMUNICADO));
        assertEquals(0, fachada.quantidadeDePublicacoes());
    }

    @Test
    void deve_contar_todas_as_entidades_cadastradas() {
        fachada.adicionarUsuario("Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", SENHA, Papel.PUBLICADOR);
        fachada.adicionarUsuario("Ana Souza", "ana@academico.ufpb.br", "anasouza", SENHA, Papel.MEMBRO);
        fachada.publicar("coordenacao@ci.ufpb.br", COMUNICADO);

        assertEquals(2, fachada.quantidadeDeUsuarios());
        assertEquals(1, fachada.quantidadeDePublicacoes());
        assertEquals(3, fachada.quantidadeDeEntidadesCadastradas());
    }

    @Test
    void deve_contar_zero_entidades_quando_nada_foi_cadastrado() {
        assertEquals(0, fachada.quantidadeDeEntidadesCadastradas());
    }

    @Test
    void deve_editar_e_remover_publicacoes() {
        fachada.adicionarUsuario("Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", SENHA, Papel.PUBLICADOR);
        Publicacao publicacao = fachada.publicar("coordenacao@ci.ufpb.br", COMUNICADO);

        fachada.editarPublicacao(publicacao.getId(),
                new DadosDaPublicacao("Sala alterada", "Aula na sala 102.", Categoria.COMUNICADO, null, null));
        assertEquals("Sala alterada", fachada.listarFeed().get(0).getTitulo());

        fachada.removerPublicacao(publicacao.getId());
        assertEquals(0, fachada.quantidadeDePublicacoes());
    }

    @Test
    void deve_recusar_remover_usuario_que_tem_publicacoes() {
        Usuario autor = fachada.adicionarUsuario(
                "Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", SENHA, Papel.PUBLICADOR);
        fachada.publicar("coordenacao@ci.ufpb.br", COMUNICADO);

        assertThrows(UsuarioComPublicacoesException.class, () -> fachada.removerUsuario(autor.getId()));
        assertEquals(1, fachada.quantidadeDeUsuarios());
    }

    @Test
    void deve_remover_usuario_sem_publicacoes() {
        Usuario ana = fachada.adicionarUsuario("Ana Souza", "ana@academico.ufpb.br", "anasouza", SENHA, Papel.MEMBRO);

        fachada.removerUsuario(ana.getId());

        assertEquals(0, fachada.quantidadeDeEntidadesCadastradas());
    }

    @Test
    void deve_mostrar_os_dados_novos_do_autor_no_feed_depois_de_editar() {
        Usuario autor = fachada.adicionarUsuario(
                "Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", SENHA, Papel.PUBLICADOR);
        fachada.publicar("coordenacao@ci.ufpb.br", COMUNICADO);

        fachada.editarUsuario(autor.getId(), new DadosDoUsuario(
                "Coordenação de Computação", "coordenacao@ci.ufpb.br", "coordcc", SENHA, Papel.PUBLICADOR));

        assertEquals("coordcc", fachada.listarFeed().get(0).getAutor().getLogin());
    }
}
