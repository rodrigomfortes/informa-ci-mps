package informaci.controle;

import informaci.entidade.TipoDeOperacao;
import informaci.entidade.Usuario;

import java.util.List;

/** Comando que lista os usuários e registra o acesso. */
final class ListarUsuariosComando implements Comando<List<Usuario>> {

    private final GerenciadorDeUsuarios usuarios;
    private final GerenciadorDeAcessos acessos;

    ListarUsuariosComando(GerenciadorDeUsuarios usuarios, GerenciadorDeAcessos acessos) {
        this.usuarios = usuarios;
        this.acessos = acessos;
    }

    @Override
    public List<Usuario> executar() {
        List<Usuario> lista = usuarios.listarTodos();
        acessos.registrar(TipoDeOperacao.LISTAGEM_DE_USUARIOS, null);
        return lista;
    }
}
