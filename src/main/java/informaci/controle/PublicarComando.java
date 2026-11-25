package informaci.controle;

import informaci.entidade.DadosDaPublicacao;
import informaci.entidade.Publicacao;
import informaci.entidade.TipoDeOperacao;
import informaci.entidade.Usuario;

/**
 * Comando que publica em nome do usuário com o e-mail informado e registra o
 * acesso.
 *
 * @throws UsuarioNaoEncontradoException se nenhum usuário tiver esse e-mail
 */
final class PublicarComando implements Comando<Publicacao> {

    private final GerenciadorDeUsuarios usuarios;
    private final GerenciadorDePublicacoes publicacoes;
    private final GerenciadorDeAcessos acessos;
    private final String emailDoAutor;
    private final DadosDaPublicacao dados;

    PublicarComando(GerenciadorDeUsuarios usuarios, GerenciadorDePublicacoes publicacoes,
            GerenciadorDeAcessos acessos, String emailDoAutor, DadosDaPublicacao dados) {
        this.usuarios = usuarios;
        this.publicacoes = publicacoes;
        this.acessos = acessos;
        this.emailDoAutor = emailDoAutor;
        this.dados = dados;
    }

    @Override
    public Publicacao executar() {
        Usuario autor = usuarios.buscarPorEmail(emailDoAutor);
        Publicacao publicacao = publicacoes.publicar(autor, dados);
        acessos.registrar(TipoDeOperacao.PUBLICACAO, autor.getLogin());
        return publicacao;
    }
}
