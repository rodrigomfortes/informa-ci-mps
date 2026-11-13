package informaci.persistencia;

import informaci.controle.RegistroDeAcessoRepositorio;
import informaci.entidade.RegistroDeAcesso;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Guarda os registros de acesso num arquivo binário, como os demais
 * repositórios em arquivo: lê no construtor, regrava a cada registro e, se a
 * gravação falhar, desfaz o registro em memória.
 */
public class RegistroDeAcessoRepositorioEmArquivo implements RegistroDeAcessoRepositorio {

    private final Path arquivo;
    private final List<RegistroDeAcesso> registros = new ArrayList<>();

    /**
     * @throws PersistenciaException se o arquivo existir mas não puder ser lido
     */
    public RegistroDeAcessoRepositorioEmArquivo(Path arquivo) {
        this.arquivo = Objects.requireNonNull(arquivo, "O caminho do arquivo é obrigatório.");
        try {
            registros.addAll(ArquivoBinario.ler(arquivo, RegistroDeAcesso.class));
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PersistenciaException("Não foi possível ler os acessos de " + arquivo + ".", e);
        }
    }

    @Override
    public void salvar(RegistroDeAcesso registro) {
        registros.add(Objects.requireNonNull(registro, "Não é possível salvar um registro de acesso nulo."));
        try {
            ArquivoBinario.gravar(arquivo, List.copyOf(registros));
        } catch (IOException e) {
            registros.remove(registros.size() - 1);
            throw new PersistenciaException("Não foi possível gravar os acessos em " + arquivo + ".", e);
        }
    }

    @Override
    public List<RegistroDeAcesso> listarTodos() {
        return List.copyOf(registros);
    }
}
