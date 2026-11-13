package informaci.controle;

import informaci.entidade.RegistroDeAcesso;
import informaci.entidade.TipoDeOperacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Estatísticas de acesso calculadas a partir dos registros (RF13).
 *
 * @param porOperacao        acessos por operação, com todas as operações (inclusive as com 0)
 * @param porDia             acessos por dia, em ordem cronológica
 * @param usuariosMaisAtivos até {@value #QUANTIDADE_DE_USUARIOS_MAIS_ATIVOS} usuários,
 *                           do mais ativo para o menos ativo (empate: ordem alfabética do login)
 */
public record EstatisticasDeAcesso(
        LocalDateTime geradoEm,
        long total,
        Map<TipoDeOperacao, Long> porOperacao,
        SortedMap<LocalDate, Long> porDia,
        List<UsuarioAtivo> usuariosMaisAtivos,
        Optional<LocalDateTime> primeiroAcesso,
        Optional<LocalDateTime> ultimoAcesso) {

    public static final int QUANTIDADE_DE_USUARIOS_MAIS_ATIVOS = 5;

    /** Um usuário e quantos acessos o envolveram. */
    public record UsuarioAtivo(String login, long acessos) {
    }

    public EstatisticasDeAcesso {
        Objects.requireNonNull(geradoEm, "A data de geração é obrigatória.");
        porOperacao = Collections.unmodifiableMap(new EnumMap<>(porOperacao));
        porDia = Collections.unmodifiableSortedMap(new TreeMap<>(porDia));
        usuariosMaisAtivos = List.copyOf(usuariosMaisAtivos);
    }

    /** Calcula as estatísticas dos registros informados. */
    public static EstatisticasDeAcesso calcular(List<RegistroDeAcesso> registros, LocalDateTime geradoEm) {
        Map<TipoDeOperacao, Long> porOperacao = new EnumMap<>(TipoDeOperacao.class);
        for (TipoDeOperacao operacao : TipoDeOperacao.values()) {
            porOperacao.put(operacao, 0L);
        }
        SortedMap<LocalDate, Long> porDia = new TreeMap<>();
        Map<String, Long> porUsuario = new HashMap<>();

        for (RegistroDeAcesso registro : registros) {
            porOperacao.merge(registro.operacao(), 1L, Long::sum);
            porDia.merge(registro.momento().toLocalDate(), 1L, Long::sum);
            registro.usuario().ifPresent(login -> porUsuario.merge(login, 1L, Long::sum));
        }

        List<UsuarioAtivo> maisAtivos = porUsuario.entrySet().stream()
                .map(entrada -> new UsuarioAtivo(entrada.getKey(), entrada.getValue()))
                .sorted(Comparator.comparingLong(UsuarioAtivo::acessos).reversed()
                        .thenComparing(UsuarioAtivo::login))
                .limit(QUANTIDADE_DE_USUARIOS_MAIS_ATIVOS)
                .toList();

        return new EstatisticasDeAcesso(geradoEm, registros.size(), porOperacao, porDia, maisAtivos,
                registros.stream().map(RegistroDeAcesso::momento).min(Comparator.naturalOrder()),
                registros.stream().map(RegistroDeAcesso::momento).max(Comparator.naturalOrder()));
    }
}
