package informaci.persistencia;

import informaci.controle.FabricaDeRepositorios;
import informaci.controle.PublicacaoRepositorio;
import informaci.controle.UsuarioRepositorio;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Fábrica concreta da família de repositórios em arquivo binário (Abstract Factory).
 *
 * <p>Concentra num só lugar onde fica cada arquivo: todos na pasta informada,
 * com um nome fixo por entidade. Os dados sobrevivem entre execuções.
 */
public class FabricaDeRepositoriosEmArquivo implements FabricaDeRepositorios {

    /** Nome do arquivo de usuários dentro da pasta de dados. */
    public static final String ARQUIVO_DE_USUARIOS = "usuarios.bin";

    /** Nome do arquivo de publicações dentro da pasta de dados. */
    public static final String ARQUIVO_DE_PUBLICACOES = "publicacoes.bin";

    private final Path pasta;

    /**
     * @param pasta pasta onde os arquivos de dados são lidos e gravados
     */
    public FabricaDeRepositoriosEmArquivo(Path pasta) {
        this.pasta = Objects.requireNonNull(pasta, "A pasta dos arquivos é obrigatória.");
    }

    /**
     * @throws PersistenciaException se o arquivo de usuários existir mas não puder ser lido
     */
    @Override
    public UsuarioRepositorio criarRepositorioDeUsuarios() {
        return new UsuarioRepositorioEmArquivo(arquivoDeUsuarios());
    }

    /**
     * @throws PersistenciaException se o arquivo de publicações existir mas não puder ser lido
     */
    @Override
    public PublicacaoRepositorio criarRepositorioDePublicacoes() {
        return new PublicacaoRepositorioEmArquivo(arquivoDePublicacoes());
    }

    public Path arquivoDeUsuarios() {
        return pasta.resolve(ARQUIVO_DE_USUARIOS);
    }

    public Path arquivoDePublicacoes() {
        return pasta.resolve(ARQUIVO_DE_PUBLICACOES);
    }
}
