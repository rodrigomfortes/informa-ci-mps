package informaci.entidade;

/**
 * Regra do nome do usuário (RF01): obrigatório, sem espaços em volta.
 */
public final class ValidadorDeNome {

    private ValidadorDeNome() {
    }

    /**
     * @return o nome sem espaços em volta
     * @throws NomeInvalidoException se o nome for nulo ou estiver em branco
     */
    public static String validar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new NomeInvalidoException("O nome do usuário é obrigatório.");
        }
        return nome.trim();
    }
}
