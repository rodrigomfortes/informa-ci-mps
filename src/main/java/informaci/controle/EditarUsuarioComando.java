package informaci.controle;

import informaci.entidade.DadosDoUsuario;
import informaci.entidade.TipoDeOperacao;
import informaci.entidade.Usuario;

import java.util.UUID;

/**
 * Comando que edita um usuário e atualiza o autor nas publicações dele, para
 * que o feed mostre os dados novos.
 */
final class EditarUsuarioComando implements Comando<Usuario> {

    private final GerenciadorDeUsuarios usuarios;
    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;
    private final UUID id;
    private final DadosDoUsuario novosDados;

    EditarUsuarioComando(GerenciadorDeUsuarios usuarios, GerenciadorDePublicacoes publicacoes,
            GerenciadorDeAcessos acessos, UUID id, DadosDoUsuario novosDados) {
        this.usuarios = usuarios;
        this.publicacoes = publicacoes;
        this.acessos = acessos;
        this.id = id;
        this.novosDados = novosDados;
    }

    @Override
    public Usuario executar() {
        Usuario editado = usuarios.editar(id, novosDados);
        publicacoes.atualizarAutor(editado);
        acessos.registrar(TipoDeOperacao.EDICAO_DE_USUARIO, editado.getLogin());
        return editado;
    }
}
