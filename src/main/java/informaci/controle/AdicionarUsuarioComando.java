package informaci.controle;

import informaci.entidade.Papel;
import informaci.entidade.TipoDeOperacao;
import informaci.entidade.Usuario;

/** Comando que cadastra um usuário e registra o acesso. */
final class AdicionarUsuarioComando implements Comando<Usuario> {

    private final GerenciadorDeUsuarios usuarios;
    private final GerenciadorDeAcessos acessos;
    private final String nome;
    private final String email;
    private final String login;
    private final String senha;
    private final Papel papel;

    AdicionarUsuarioComando(GerenciadorDeUsuarios usuarios, GerenciadorDeAcessos acessos,
            String nome, String email, String login, String senha, Papel papel) {
        this.usuarios = usuarios;
        this.acessos = acessos;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.papel = papel;
    }

    @Override
    public Usuario executar() {
        Usuario usuario = usuarios.adicionar(nome, email, login, senha, papel);
        acessos.registrar(TipoDeOperacao.CADASTRO_DE_USUARIO, usuario.getLogin());
        return usuario;
    }
}
