package informaci.persistencia;

import informaci.controle.RegistroDeAcessoRepositorio;
import informaci.entidade.RegistroDeAcesso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Guarda os registros de acesso em memória RAM; eles se perdem ao encerrar.
 */
public class RegistroDeAcessoRepositorioEmMemoria implements RegistroDeAcessoRepositorio {

    private final List<RegistroDeAcesso> registros = new ArrayList<>();

    @Override
    public void salvar(RegistroDeAcesso registro) {
        registros.add(Objects.requireNonNull(registro, "Não é possível salvar um registro de acesso nulo."));
    }

    @Override
    public List<RegistroDeAcesso> listarTodos() {
        return List.copyOf(registros);
    }
}
