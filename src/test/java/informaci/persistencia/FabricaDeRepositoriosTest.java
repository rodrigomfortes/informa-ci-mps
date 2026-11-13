package informaci.persistencia;

import informaci.controle.FabricaDeRepositorios;
import informaci.controle.UsuarioRepositorio;
import informaci.entidade.Categoria;
import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Papel;
import informaci.entidade.Publicacao;
import informaci.entidade.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes do Abstract Factory de repositórios e do Factory Method de {@link TipoDeArmazenamento}. */
class FabricaDeRepositoriosTest {

    private static final String SENHA = "Senha@123";

    @TempDir
    Path pasta;

    // ---- Factory Method: TipoDeArmazenamento ----

    @Test
    void tipo_memoria_deve_criar_a_fabrica_em_memoria() {
        assertInstanceOf(FabricaDeRepositoriosEmMemoria.class, TipoDeArmazenamento.MEMORIA.criarFabrica(pasta));
    }

    @Test
    void tipo_arquivo_deve_criar_a_fabrica_em_arquivo() {
        assertInstanceOf(FabricaDeRepositoriosEmArquivo.class, TipoDeArmazenamento.ARQUIVO.criarFabrica(pasta));
    }

    @Test
    void tipo_arquivo_deve_descrever_onde_os_dados_ficam() {
        String descricao = TipoDeArmazenamento.ARQUIVO.descricao(pasta);

        assertTrue(descricao.contains(pasta.resolve("usuarios.bin").toAbsolutePath().toString()));
        assertTrue(descricao.contains(pasta.resolve("publicacoes.bin").toAbsolutePath().toString()));
    }

    // ---- Abstract Factory: família em memória ----

    @Test
    void fabrica_em_memoria_deve_criar_repositorios_em_memoria() {
        FabricaDeRepositorios fabrica = new FabricaDeRepositoriosEmMemoria();

        assertInstanceOf(UsuarioRepositorioEmMemoria.class, fabrica.criarRepositorioDeUsuarios());
        assertInstanceOf(PublicacaoRepositorioEmMemoria.class, fabrica.criarRepositorioDePublicacoes());
    }

    @Test
    void fabrica_em_memoria_deve_criar_repositorios_independentes_a_cada_chamada() {
        FabricaDeRepositorios fabrica = new FabricaDeRepositoriosEmMemoria();
        UsuarioRepositorio primeiro = fabrica.criarRepositorioDeUsuarios();
        primeiro.salvar(novoUsuario());

        UsuarioRepositorio segundo = fabrica.criarRepositorioDeUsuarios();

        assertNotSame(primeiro, segundo);
        assertTrue(segundo.listarTodos().isEmpty());
    }

    // ---- Abstract Factory: família em arquivo ----

    @Test
    void fabrica_em_arquivo_deve_criar_repositorios_em_arquivo() {
        FabricaDeRepositorios fabrica = new FabricaDeRepositoriosEmArquivo(pasta);

        assertInstanceOf(UsuarioRepositorioEmArquivo.class, fabrica.criarRepositorioDeUsuarios());
        assertInstanceOf(PublicacaoRepositorioEmArquivo.class, fabrica.criarRepositorioDePublicacoes());
    }

    @Test
    void fabrica_em_arquivo_deve_gravar_cada_entidade_no_seu_arquivo_da_pasta() {
        FabricaDeRepositorios fabrica = new FabricaDeRepositoriosEmArquivo(pasta);
        Usuario autor = novoUsuario();

        fabrica.criarRepositorioDeUsuarios().salvar(autor);
        fabrica.criarRepositorioDePublicacoes().salvar(novaPublicacao(autor));

        assertTrue(Files.exists(pasta.resolve("usuarios.bin")));
        assertTrue(Files.exists(pasta.resolve("publicacoes.bin")));
    }

    @Test
    void fabrica_em_arquivo_deve_recuperar_os_dados_gravados_por_outra_fabrica_na_mesma_pasta() {
        Usuario autor = novoUsuario();
        FabricaDeRepositorios primeiraExecucao = new FabricaDeRepositoriosEmArquivo(pasta);
        primeiraExecucao.criarRepositorioDeUsuarios().salvar(autor);
        primeiraExecucao.criarRepositorioDePublicacoes().salvar(novaPublicacao(autor));

        FabricaDeRepositorios segundaExecucao = new FabricaDeRepositoriosEmArquivo(pasta);

        assertEquals(1, segundaExecucao.criarRepositorioDeUsuarios().listarTodos().size());
        assertEquals(1, segundaExecucao.criarRepositorioDePublicacoes().listarTodas().size());
    }

    @Test
    void fabrica_em_arquivo_deve_lancar_persistencia_exception_quando_arquivo_esta_corrompido() throws IOException {
        Files.writeString(pasta.resolve("usuarios.bin"), "conteúdo que não é uma lista serializada");
        FabricaDeRepositorios fabrica = new FabricaDeRepositoriosEmArquivo(pasta);

        assertThrows(PersistenciaException.class, fabrica::criarRepositorioDeUsuarios);
    }

    @Test
    void fabrica_em_arquivo_deve_recusar_pasta_nula() {
        assertThrows(NullPointerException.class, () -> new FabricaDeRepositoriosEmArquivo(null));
    }

    private static Usuario novoUsuario() {
        return new Usuario("Coordenação", "coordenacao@ci.ufpb.br", "coordenacao", SENHA, Papel.PUBLICADOR);
    }

    private static Publicacao novaPublicacao(Usuario autor) {
        return new Publicacao(autor,
                new DadosDaPublicacao("Mudança de sala", "Aula na sala 101.", Categoria.COMUNICADO, null, null));
    }
}
