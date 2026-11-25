package informaci.controle;

/** Comando que informa quantos usuários estão cadastrados. */
final class QuantidadeDeUsuariosComando implements Comando<Integer> {

    private final GerenciadorDeUsuarios usuarios;

    QuantidadeDeUsuariosComando(GerenciadorDeUsuarios usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public Integer executar() {
        return usuarios.quantidade();
    }
}
