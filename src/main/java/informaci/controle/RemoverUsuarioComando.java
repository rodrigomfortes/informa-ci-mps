package informaci.controle;

import informaci.entidade.TipoDeOperacao;
import informaci.entidade.Usuario;

import java.util.UUID;

/**
 * Comando que remove um usuário, desde que ele não seja autor de nenhuma
 * publicação.
 *
 * @throws UsuarioComPublicacoesException se o usuário tiver publicações
 * @throws UsuarioNaoEncontradoException  se não houver usuário com essa identidade
 */
final class RemoverUsuarioComando implements Comando<Void> {

    private final GerenciadorDeUsuarios usuarios;
    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;
    private final UUID id;

    RemoverUsuarioComando(GerenciadorDeUsuarios usuarios, GerenciadorDePublicacoes publicacoes,
            GerenciadorDeAcessos acessos, UUID id) {
        this.usuarios = usuarios;
        this.publicacoes = publicacoes;
        this.acessos = acessos;
        this.id = id;
    }

    @Override
    public Void executar() {
        Usuario usuario = usuarios.buscarPorId(id);
        int publicacoesDoUsuario = publicacoes.contarDoAutor(usuario);
        if (publicacoesDoUsuario > 0) {
            throw new UsuarioComPublicacoesException(usuario.getLogin(), publicacoesDoUsuario);
        }
        usuarios.remover(id);
        acessos.registrar(TipoDeOperacao.REMOCAO_DE_USUARIO, usuario.getLogin());
        return null;
    }
}
