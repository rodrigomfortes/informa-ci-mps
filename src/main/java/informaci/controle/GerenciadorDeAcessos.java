package informaci.controle;

import informaci.entidade.RegistroDeAcesso;
import informaci.entidade.TipoDeOperacao;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registra os acessos ao sistema (RF12) e calcula as suas estatísticas (RF13).
 *
 * <p>Recebe um {@link Clock} para que os testes controlem o horário dos
 * registros.
 */
public class GerenciadorDeAcessos {

    private final RegistroDeAcessoRepositorio repositorio;
    private final ServicoDeLog log;
    private final Clock relogio;

    public GerenciadorDeAcessos(RegistroDeAcessoRepositorio repositorio, ServicoDeLog log, Clock relogio) {
        this.repositorio = Objects.requireNonNull(repositorio, "O repositório de acessos é obrigatório.");
        this.log = Objects.requireNonNull(log, "O serviço de log é obrigatório.");
        this.relogio = Objects.requireNonNull(relogio, "O relógio é obrigatório.");
    }

    /**
     * Registra que a operação foi concluída agora.
     *
     * <p>Uma falha ao guardar o registro não desfaz nem impede a operação, que
     * já foi concluída: ela vai para o log e o sistema segue normalmente.
     *
     * @param login login do usuário envolvido, ou {@code null} se não houver
     */
    public void registrar(TipoDeOperacao operacao, String login) {
        RegistroDeAcesso registro = new RegistroDeAcesso(LocalDateTime.now(relogio), operacao, login);
        try {
            repositorio.salvar(registro);
        } catch (RuntimeException e) {
            log.erro("Não foi possível registrar o acesso " + operacao + ".", e);
        }
    }

    /** Estatísticas de todos os acessos registrados, calculadas agora. */
    public EstatisticasDeAcesso calcularEstatisticas() {
        return EstatisticasDeAcesso.calcular(repositorio.listarTodos(), LocalDateTime.now(relogio));
    }
}
