package informaci.controle;

import java.util.Objects;

/**
 * Comando que gera o relatório de estatísticas de acesso no formato escolhido
 * (RF13), usando a subclasse do Template Method correspondente ao formato.
 */
final class GerarRelatorioDeAcessosComando implements Comando<byte[]> {

    private final GerenciadorDeAcessos acessos;
    private final FormatoDeRelatorio formato;

    GerarRelatorioDeAcessosComando(GerenciadorDeAcessos acessos, FormatoDeRelatorio formato) {
        this.acessos = acessos;
        this.formato = formato;
    }

    @Override
    public byte[] executar() {
        Objects.requireNonNull(formato, "O formato do relatório é obrigatório.");
        return formato.criarGerador().gerar(acessos.calcularEstatisticas());
    }
}
