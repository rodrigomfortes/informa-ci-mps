package informaci.persistencia;

import informaci.controle.FabricaDeRepositorios;

import java.nio.file.Path;

/**
 * Tipos de armazenamento que o usuário pode escolher ao iniciar o sistema.
 *
 * <p>Cada constante sobrescreve {@link #criarFabrica(Path)} — um
 * <strong>Factory Method</strong> — e devolve a fábrica concreta da sua
 * família de repositórios. Quem escolhe o armazenamento só precisa do tipo;
 * não conhece nenhuma classe concreta de repositório. Acrescentar um novo
 * armazenamento é acrescentar uma constante aqui e uma fábrica nova, sem
 * {@code switch} para atualizar em outro lugar.
 */
public enum TipoDeArmazenamento {

    MEMORIA {
        @Override
        public String descricao(Path pasta) {
            return "Memória RAM (os dados se perdem ao encerrar)";
        }

        @Override
        public FabricaDeRepositorios criarFabrica(Path pasta) {
            return new FabricaDeRepositoriosEmMemoria();
        }
    },

    ARQUIVO {
        @Override
        public String descricao(Path pasta) {
            return "Arquivo binário ("
                    + pasta.resolve(FabricaDeRepositoriosEmArquivo.ARQUIVO_DE_USUARIOS).toAbsolutePath() + " e "
                    + pasta.resolve(FabricaDeRepositoriosEmArquivo.ARQUIVO_DE_PUBLICACOES).toAbsolutePath() + ")";
        }

        @Override
        public FabricaDeRepositorios criarFabrica(Path pasta) {
            return new FabricaDeRepositoriosEmArquivo(pasta);
        }
    };

    /**
     * Texto exibido no menu de escolha do armazenamento.
     *
     * @param pasta pasta de dados, usada pelos tipos que gravam em disco
     *              (ignorada por {@link #MEMORIA})
     */
    public abstract String descricao(Path pasta);

    /**
     * Cria a fábrica de repositórios deste tipo (Factory Method).
     *
     * <p>Só monta a fábrica: os arquivos, se houver, são lidos quando ela
     * cria os repositórios.
     *
     * @param pasta pasta de dados, usada pelos tipos que gravam em disco
     */
    public abstract FabricaDeRepositorios criarFabrica(Path pasta);
}
