package informaci.persistencia;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Lê e grava listas de objetos num arquivo binário (serialização Java).
 *
 * <p>Reúne o que os repositórios em arquivo têm em comum, para que cada um
 * cuide apenas da sua coleção e das mensagens de erro do seu domínio.
 */
final class ArquivoBinario {

    private ArquivoBinario() {
    }

    /**
     * Lê a lista gravada no arquivo.
     *
     * @return os objetos lidos, na ordem em que foram gravados, ou uma lista
     *         vazia se o arquivo ainda não existir
     * @throws ClassCastException se o conteúdo não for uma lista do tipo esperado
     */
    static <T> List<T> ler(Path arquivo, Class<T> tipo) throws IOException, ClassNotFoundException {
        if (!Files.exists(arquivo)) {
            return List.of();
        }
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(arquivo))) {
            List<?> lidos = (List<?>) entrada.readObject();
            List<T> objetos = new ArrayList<>(lidos.size());
            for (Object objeto : lidos) {
                objetos.add(tipo.cast(objeto));
            }
            return objetos;
        }
    }

    /**
     * Grava a lista no arquivo, substituindo o conteúdo anterior.
     *
     * <p>Escreve primeiro num arquivo temporário da mesma pasta e só então o
     * move para o lugar do original. Assim, se a gravação falhar no meio, o
     * arquivo anterior continua intacto em vez de ficar truncado.
     */
    static void gravar(Path arquivo, List<?> objetos) throws IOException {
        Path pasta = arquivo.toAbsolutePath().getParent();
        Path temporario = Files.createTempFile(pasta, arquivo.getFileName().toString(), ".tmp");
        try {
            try (ObjectOutputStream saida = new ObjectOutputStream(Files.newOutputStream(temporario))) {
                saida.writeObject(new ArrayList<>(objetos));
            }
            substituir(temporario, arquivo);
        } finally {
            Files.deleteIfExists(temporario);
        }
    }

    private static void substituir(Path origem, Path destino) throws IOException {
        try {
            Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
