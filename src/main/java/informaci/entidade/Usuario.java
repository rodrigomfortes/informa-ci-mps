package informaci.entidade;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Usuário cadastrado no sistema.
 *
 * <p>É responsabilidade desta classe garantir que não exista usuário em estado
 * inválido: as validações ficam no construtor, de modo que qualquer instância
 * que chegue às demais camadas já está consistente.
 */
public class Usuario {

    private static final Pattern FORMATO_DE_EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final UUID id;
    private final String nome;
    private final String email;
    private final Papel papel;

    public Usuario(String nome, String email, Papel papel) {
        this.id = UUID.randomUUID();
        this.nome = validarNome(nome);
        this.email = validarEmail(email);
        this.papel = Objects.requireNonNull(papel, "O papel do usuário é obrigatório.");
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }
        return nome.trim();
    }

    /**
     * Valida o formato do e-mail e o normaliza para minúsculas.
     *
     * <p>A normalização existe porque o e-mail identifica o usuário: sem ela,
     * {@code Joao@ufpb.br} e {@code joao@ufpb.br} seriam tratados como pessoas
     * diferentes na verificação de duplicidade.
     */
    private static String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail do usuário é obrigatório.");
        }
        String normalizado = email.trim().toLowerCase();
        if (!FORMATO_DE_EMAIL.matcher(normalizado).matches()) {
            throw new IllegalArgumentException("E-mail em formato inválido: " + email);
        }
        return normalizado;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Papel getPapel() {
        return papel;
    }

    /**
     * Dois usuários são o mesmo quando possuem a mesma identidade, ainda que
     * seus demais atributos venham a divergir.
     */
    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Usuario usuario)) {
            return false;
        }
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Usuario{id=%s, nome='%s', email='%s', papel=%s}"
                .formatted(id, nome, email, papel);
    }
}
