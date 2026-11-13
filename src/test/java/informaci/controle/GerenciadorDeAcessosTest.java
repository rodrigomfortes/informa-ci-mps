package informaci.controle;

import informaci.entidade.RegistroDeAcesso;
import informaci.entidade.TipoDeOperacao;
import informaci.persistencia.RegistroDeAcessoRepositorioEmMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorDeAcessosTest {

    private static final ZoneId FUSO = ZoneOffset.UTC;
    private static final LocalDateTime AGORA = LocalDateTime.of(2026, 11, 12, 10, 30);

    private RegistroDeAcessoRepositorioEmMemoria repositorio;
    private GerenciadorDeAcessos acessos;

    @BeforeEach
    void criarGerenciadorComRelogioFixo() {
        repositorio = new RegistroDeAcessoRepositorioEmMemoria();
        acessos = new GerenciadorDeAcessos(repositorio, new NullLog(), relogioEm(AGORA));
    }

    private static Clock relogioEm(LocalDateTime momento) {
        return Clock.fixed(momento.toInstant(ZoneOffset.UTC), FUSO);
    }

    private static RegistroDeAcesso acesso(LocalDateTime momento, TipoDeOperacao operacao, String login) {
        return new RegistroDeAcesso(momento, operacao, login);
    }

    @Test
    void deve_registrar_a_operacao_com_o_horario_atual_e_o_usuario() {
        acessos.registrar(TipoDeOperacao.PUBLICACAO, "coordenacao");

        assertEquals(List.of(acesso(AGORA, TipoDeOperacao.PUBLICACAO, "coordenacao")), repositorio.listarTodos());
    }

    @Test
    void deve_calcular_estatisticas_vazias_sem_acessos() {
        EstatisticasDeAcesso estatisticas = acessos.calcularEstatisticas();

        assertEquals(0, estatisticas.total());
        assertEquals(AGORA, estatisticas.geradoEm());
        assertEquals(TipoDeOperacao.values().length, estatisticas.porOperacao().size());
        assertTrue(estatisticas.porOperacao().values().stream().allMatch(quantidade -> quantidade == 0));
        assertTrue(estatisticas.porDia().isEmpty());
        assertTrue(estatisticas.usuariosMaisAtivos().isEmpty());
        assertTrue(estatisticas.primeiroAcesso().isEmpty());
    }

    @Test
    void deve_contar_por_operacao_por_dia_e_por_usuario() {
        LocalDateTime ontem = AGORA.minusDays(1);
        List<RegistroDeAcesso> registros = List.of(
                acesso(ontem, TipoDeOperacao.CADASTRO_DE_USUARIO, "ana"),
                acesso(ontem, TipoDeOperacao.CONSULTA_AO_FEED, null),
                acesso(AGORA, TipoDeOperacao.PUBLICACAO, "coordenacao"),
                acesso(AGORA, TipoDeOperacao.PUBLICACAO, "coordenacao"),
                acesso(AGORA, TipoDeOperacao.CONSULTA_AO_FEED, null));

        EstatisticasDeAcesso estatisticas = EstatisticasDeAcesso.calcular(registros, AGORA);

        assertEquals(5, estatisticas.total());
        assertEquals(2, estatisticas.porOperacao().get(TipoDeOperacao.PUBLICACAO));
        assertEquals(2, estatisticas.porOperacao().get(TipoDeOperacao.CONSULTA_AO_FEED));
        assertEquals(0, estatisticas.porOperacao().get(TipoDeOperacao.REMOCAO_DE_USUARIO));
        assertEquals(List.of(LocalDate.of(2026, 11, 11), LocalDate.of(2026, 11, 12)),
                List.copyOf(estatisticas.porDia().keySet()));
        assertEquals(3, estatisticas.porDia().get(LocalDate.of(2026, 11, 12)));
        assertEquals(List.of(new EstatisticasDeAcesso.UsuarioAtivo("coordenacao", 2),
                new EstatisticasDeAcesso.UsuarioAtivo("ana", 1)), estatisticas.usuariosMaisAtivos());
        assertEquals(ontem, estatisticas.primeiroAcesso().orElseThrow());
        assertEquals(AGORA, estatisticas.ultimoAcesso().orElseThrow());
    }

    @Test
    void deve_limitar_os_usuarios_mais_ativos_e_desempatar_pelo_login() {
        List<RegistroDeAcesso> registros = new ArrayList<>();
        for (String login : List.of("fabio", "eva", "davi", "caio", "bia", "ana")) {
            registros.add(acesso(AGORA, TipoDeOperacao.CADASTRO_DE_USUARIO, login));
        }
        registros.add(acesso(AGORA, TipoDeOperacao.EDICAO_DE_USUARIO, "fabio"));

        List<EstatisticasDeAcesso.UsuarioAtivo> maisAtivos =
                EstatisticasDeAcesso.calcular(registros, AGORA).usuariosMaisAtivos();

        assertEquals(EstatisticasDeAcesso.QUANTIDADE_DE_USUARIOS_MAIS_ATIVOS, maisAtivos.size());
        assertEquals(List.of("fabio", "ana", "bia", "caio", "davi"),
                maisAtivos.stream().map(EstatisticasDeAcesso.UsuarioAtivo::login).toList());
    }

    @Test
    void nao_deve_impedir_a_operacao_quando_o_registro_falha() {
        List<String> erros = new ArrayList<>();
        ServicoDeLog logQueGuardaErros = new ServicoDeLog() {
            @Override
            public void info(String mensagem) {
            }

            @Override
            public void aviso(String mensagem) {
            }

            @Override
            public void erro(String mensagem, Throwable causa) {
                erros.add(mensagem);
            }
        };
        RegistroDeAcessoRepositorio repositorioQueFalha = new RegistroDeAcessoRepositorio() {
            @Override
            public void salvar(RegistroDeAcesso registro) {
                throw new IllegalStateException("disco cheio");
            }

            @Override
            public List<RegistroDeAcesso> listarTodos() {
                return List.of();
            }
        };

        new GerenciadorDeAcessos(repositorioQueFalha, logQueGuardaErros, Clock.fixed(Instant.EPOCH, FUSO))
                .registrar(TipoDeOperacao.CONSULTA_AO_FEED, null);

        assertEquals(1, erros.size());
    }
}
