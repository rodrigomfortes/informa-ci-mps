package informaci.controle;

import java.util.Objects;

/**
 * Invocador do padrão Command: recebe um {@link Comando} e o executa.
 *
 * <p>A fachada monta o comando com os parâmetros da operação e o entrega
 * a este invocador, sem conhecer os detalhes de como ele é executado.
 */
public class InvocadorDeComandos {

    public <R> R executar(Comando<R> comando) {
        Objects.requireNonNull(comando, "O comando é obrigatório.");
        return comando.executar();
    }
}
