package informaci.entidade;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Usuário cadastrado no sistema.
 *
 * <p>Nenhum usuário existe em estado inválido: o construtor passa cada dado
 * pelo seu validador ({@link ValidadorDeNome}, {@link ValidadorDeEmail},
 * {@link ValidadorDeLogin} e {@link ValidadorDeSenha}), de modo que qualquer
 * instância que chegue às demais camadas já está consistente. As regras ficam
 * nos validadores para poderem ser reaproveitadas e testadas isoladamente.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String nome;
    private final String email;
    private final String login;
    private final String senha;
    private final Papel papel;

    /**
     * @throws NomeInvalidoException  se o nome estiver em branco
     * @throws EmailInvalidoException se o e-mail estiver vazio ou em formato inválido
     * @throws LoginInvalidoException se o login violar alguma regra de formato
     * @throws SenhaInvalidaException se a senha não atender à política de senhas
     */
    public Usuario(String nome, String email, String login, String senha, Papel papel) {
        this.id = UUID.randomUUID();
        this.nome = ValidadorDeNome.validar(nome);
        this.email = ValidadorDeEmail.validar(email);
        this.login = ValidadorDeLogin.validar(login);
        this.senha = ValidadorDeSenha.validar(senha, this.login, this.email);
        this.papel = Objects.requireNonNull(papel, "O papel do usuário é obrigatório.");
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
