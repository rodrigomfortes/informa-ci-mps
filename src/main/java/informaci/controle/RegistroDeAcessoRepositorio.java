package informaci.controle;

import informaci.entidade.RegistroDeAcesso;

import java.util.List;

/**
 * Contrato de armazenamento dos registros de acesso (padrão Repository).
 *
 * <p>Fica no controle, como os demais repositórios (ADR-0001, ADR-0003), e é
 * criado pela mesma {@link FabricaDeRepositorios} das outras entidades.
 */
public interface RegistroDeAcessoRepositorio {

    /**
     * Acrescenta o registro ao final.
     *
     * @throws NullPointerException se o registro for nulo
     */
    void salvar(RegistroDeAcesso registro);

    /** Todos os registros, na ordem em que foram salvos. */
    List<RegistroDeAcesso> listarTodos();
}
