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

    private static final int TAMANHO_MAXIMO_DO_LOGIN = 12;

    private static final int TAMANHO_MINIMO_DA_SENHA = 8;
    private static final int TAMANHO_MAXIMO_DA_SENHA = 128;
    private static final int TIPOS_DE_CARACTERE_EXIGIDOS = 3;
    private static final String SIMBOLOS_DA_SENHA = "!@#$%^&*()_+-=[]{}|'";

    private final UUID id;
    private final String nome;
    private final String email;
    private final String login;
    private final String senha;
    private final Papel papel;

    /**
     * @throws LoginInvalidoException   se o login violar alguma regra de formato
     * @throws SenhaInvalidaException   se a senha não atender à política de senhas
     * @throws IllegalArgumentException se o nome ou o e-mail forem inválidos
     */
    public Usuario(String nome, String email, String login, String senha, Papel papel) {
        this.id = UUID.randomUUID();
        this.nome = validarNome(nome);
        this.email = validarEmail(email);
        this.login = validarLogin(login);
        this.senha = validarSenha(senha, this.login, this.email);
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

    /**
     * Valida o login: obrigatório, com no máximo
     * {@value #TAMANHO_MAXIMO_DO_LOGIN} caracteres e sem números.
     */
    private static String validarLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new LoginInvalidoException("O login é obrigatório.");
        }
        String semEspacos = login.trim();
        if (semEspacos.length() > TAMANHO_MAXIMO_DO_LOGIN) {
            throw new LoginInvalidoException(
                    "O login deve ter no máximo " + TAMANHO_MAXIMO_DO_LOGIN + " caracteres.");
        }
        if (semEspacos.chars().anyMatch(Character::isDigit)) {
            throw new LoginInvalidoException("O login não pode conter números.");
        }
        return semEspacos;
    }

    /**
     * Valida a senha segundo a política padrão de senhas do AWS IAM: de
     * {@value #TAMANHO_MINIMO_DA_SENHA} a {@value #TAMANHO_MAXIMO_DA_SENHA}
     * caracteres, combinando ao menos {@value #TIPOS_DE_CARACTERE_EXIGIDOS}
     * dos quatro tipos (maiúsculas, minúsculas, números e símbolos), e
     * diferente do login e do e-mail da conta.
     *
     * <p>Recebe o login e o e-mail já validados porque a última regra depende
     * deles. A senha não passa por {@code trim}: espaços são caracteres
     * legítimos e removê-los alteraria o que o usuário digitou.
     */
    private static String validarSenha(String senha, String login, String email) {
        if (senha == null || senha.isEmpty()) {
            throw new SenhaInvalidaException("A senha é obrigatória.");
        }
        if (senha.length() < TAMANHO_MINIMO_DA_SENHA || senha.length() > TAMANHO_MAXIMO_DA_SENHA) {
            throw new SenhaInvalidaException("A senha deve ter entre "
                    + TAMANHO_MINIMO_DA_SENHA + " e " + TAMANHO_MAXIMO_DA_SENHA + " caracteres.");
        }
        if (contarTiposDeCaractere(senha) < TIPOS_DE_CARACTERE_EXIGIDOS) {
            throw new SenhaInvalidaException("A senha deve combinar ao menos "
                    + TIPOS_DE_CARACTERE_EXIGIDOS + " destes tipos de caractere: letras maiúsculas, "
                    + "letras minúsculas, números e símbolos (" + SIMBOLOS_DA_SENHA + ").");
        }
        if (senha.equalsIgnoreCase(login) || senha.equalsIgnoreCase(email)) {
            throw new SenhaInvalidaException("A senha não pode ser igual ao login nem ao e-mail.");
        }
        return senha;
    }

    private static int contarTiposDeCaractere(String senha) {
        boolean temMaiuscula = senha.chars().anyMatch(c -> c >= 'A' && c <= 'Z');
        boolean temMinuscula = senha.chars().anyMatch(c -> c >= 'a' && c <= 'z');
        boolean temNumero = senha.chars().anyMatch(c -> c >= '0' && c <= '9');
        boolean temSimbolo = senha.chars().anyMatch(c -> SIMBOLOS_DA_SENHA.indexOf(c) >= 0);

        int tipos = 0;
        if (temMaiuscula) {
            tipos++;
        }
        if (temMinuscula) {
            tipos++;
        }
        if (temNumero) {
            tipos++;
        }
        if (temSimbolo) {
            tipos++;
        }
        return tipos;
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

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
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

    /** A senha fica de fora de propósito, para não vazar em logs e mensagens. */
    @Override
    public String toString() {
        return "Usuario{id=%s, nome='%s', email='%s', login='%s', papel=%s}"
                .formatted(id, nome, email, login, papel);
    }
}
