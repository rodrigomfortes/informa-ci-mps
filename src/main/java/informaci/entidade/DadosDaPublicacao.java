package informaci.entidade;

import java.time.LocalDateTime;

/**
 * Dados que quem publica informa ao criar ou editar uma {@link Publicacao}.
 *
 * <p>Agrupa os campos editáveis para que criação e edição recebam um único
 * parâmetro. Não valida nada: as regras ficam em {@link Publicacao}, que é
 * quem garante que nenhuma publicação inválida exista.
 *
 * @param dataHora data e horário do evento; {@code null} se não houver
 * @param local    onde o evento acontece; {@code null} ou em branco se não houver
 */
public record DadosDaPublicacao(
        String titulo, String texto, Categoria categoria, LocalDateTime dataHora, String local) {
}
