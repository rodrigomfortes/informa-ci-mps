package informaci.persistencia;

/**
 * Sinaliza falha ao ler ou gravar os dados de usuários no armazenamento.
 *
 * <p>Embrulha a {@link java.io.IOException} original como causa, para que a
 * camada de controle não precise lidar com exceções verificadas de E/S.
 */
public class PersistenciaException extends RuntimeException {

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
