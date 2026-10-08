%% Entidades
class Usuario {
    <<entity>>
    -id: Inteiro
    -login: Texto
    -nome: Texto
    -email: Texto
    -senha: Texto
    -vinculo: VinculoCI
    -perfil: PerfilDeAcesso
    -status: StatusUsuario
    -dataCadastro: Data
    +bloquear()
    +desbloquear()
    +alterarPerfil(novoPerfil: PerfilDeAcesso)
    +podePublicar(): Booleano
}
class TokenRecuperacaoSenha {
    <<entity>>
    -token: Texto
    -dataExpiracao: Data
}
class RegistroAuditoria {
    <<entity>>
    -id: Inteiro
    -acao: Texto
    -dataHora: Data
}

%% Excecoes (Tratamento de Erros)
class LoginInvalidoException {
    <<exception>>
}
class SenhaInvalidaException {
    <<exception>>
}
class PersistenciaException {
    <<exception>>
}

%% Persistencia (Mecanismos Chaveados)
class IUsuarioRepository {
    <<interface>>
    +salvar(usuario: Usuario)
    +buscar(id: Inteiro): Usuario
    +listarTodos(): Lista<Usuario>
}
class UsuarioRepositoryRAM {
    <<repository>>
    -usuarios: Lista<Usuario>
    +salvar(usuario: Usuario)
}
class UsuarioRepositoryBinario {
    <<repository>>
    +salvar(usuario: Usuario)
}

%% Controles
class ControladorCadastro {
    <<control>>
    +cadastrar(dados)
    +validarDados(login, senha)
    +validarEmail()
}
class ControladorAutenticacao {
    <<control>>
    +login(credenciais)
    +recuperarSenha()
    +logout()
}
class ControladorPerfil {
    <<control>>
    +editarPerfil(dados)
    +alterarSenha()
    +excluirConta()
}
class ControladorAdministracaoMembros {
    <<control>>
    +listarMembros()
    +bloquearMembro(id)
    +desbloquearMembro(id)
    +alterarPerfilDeAcesso(id, perfil)
    +removerMembro(id)
}
class ControladorAuditoria {
    <<control>>
    +registrarLog(acao, usuarioAfetado, adminResp)
}

%% Fronteiras
class TelaCadastro {
    <<boundary>>
    +nome
    +login
    +email
    +senha
    +vinculo
    +enviarFormulario()
}
class InterfaceServicoEmail {
    <<boundary>>
    +enviarEmailConfirmacao()
    +enviarEmailRecuperacao()
}
class TelaLogin {
    <<boundary>>
    +credenciais
    +enviar()
}
class TelaRecuperacaoSenha {
    <<boundary>>
    +email
    +solicitar()
}
class TelaPerfil {
    <<boundary>>
    +dados
    +salvar()
}
class TelaListaMembros {
    <<boundary>>
    +listarMembros()
}
class TelaGerenciarMembro {
    <<boundary>>
    +perfilSelecionado
    +bloquearDesbloquear()
    +alterarPerfil()
    +removerMembro()
}

%% Relacionamentos Fronteira -> Controle
TelaCadastro --> ControladorCadastro : aciona
ControladorCadastro --> InterfaceServicoEmail : solicita envio
TelaLogin --> ControladorAutenticacao : aciona
TelaRecuperacaoSenha --> ControladorAutenticacao : aciona
ControladorAutenticacao --> InterfaceServicoEmail : solicita envio
TelaPerfil --> ControladorPerfil : aciona
TelaListaMembros --> ControladorAdministracaoMembros : aciona
TelaGerenciarMembro --> ControladorAdministracaoMembros : aciona

%% Relacionamentos Controle -> Entidade e Persistencia
ControladorCadastro --> Usuario : instancia
ControladorCadastro --> IUsuarioRepository : persiste dados
ControladorAutenticacao --> IUsuarioRepository : consulta
ControladorAutenticacao --> TokenRecuperacaoSenha : gera / valida
ControladorPerfil --> IUsuarioRepository : atualiza
ControladorAdministracaoMembros --> IUsuarioRepository : altera estado
ControladorAdministracaoMembros --> ControladorAuditoria : aciona
ControladorAuditoria --> RegistroAuditoria : persiste

%% Relacionamentos Excecoes
ControladorCadastro ..> LoginInvalidoException : lanca
ControladorCadastro ..> SenhaInvalidaException : lanca
IUsuarioRepository ..> PersistenciaException : lanca

%% Implementacoes de Persistencia
IUsuarioRepository <|.. UsuarioRepositoryRAM : implementa
IUsuarioRepository <|.. UsuarioRepositoryBinario : implementa

%% Relacionamentos Entidade
TokenRecuperacaoSenha "*" --> "1" Usuario : pertenceA
RegistroAuditoria "*" --> "1" Usuario : adminResponsavel
RegistroAuditoria "*" --> "1" Usuario : usuarioAfetado
Usuario ..> VinculoCI : usa
Usuario ..> PerfilDeAcesso : usa
Usuario ..> StatusUsuario : usa