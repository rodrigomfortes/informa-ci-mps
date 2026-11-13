package informaci.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Um acesso ao sistema: uma operação concluída, quando ocorreu e qual usuário
 * estava envolvido nela (RF12).
 *
 * <p>Enquanto não houver autenticação (UC03), o usuário envolvido é o usuário
 * afetado pela operação (o cadastrado, editado ou removido) ou o autor da
 * publicação. Consultas que não envolvem um usuário específico, como a
 * listagem e o feed, ficam sem usuário.
 *
 * @param login login do usuário envolvido, ou {@code null} se não houver
 */
public record RegistroDeAcesso(LocalDateTime momento, TipoDeOperacao operacao, String login)
        implements Serializable {

    public RegistroDeAcesso {
        Objects.requireNonNull(momento, "O momento do acesso é obrigatório.");
        Objects.requireNonNull(operacao, "A operação do acesso é obrigatória.");
    }

    public Optional<String> usuario() {
        return Optional.ofNullable(login);
    }
}
