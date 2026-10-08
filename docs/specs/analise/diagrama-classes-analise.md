classDiagram
    %% Enumeracoes
    class VinculoCI {
        <<enumeration>>
        Aluno
        Servidor
    }
    class PerfilDeAcesso {
        <<enumeration>>
        Comum
        Publicador
        Administrador
    }
    class StatusUsuario {
        <<enumeration>>
        Pendente
        Ativo
        Bloqueado
    }

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

    %% Excecoes
    class LoginInvalidoException {
        <<exception>>
    }
    class SenhaInvalidaException {
        <<exception>>
    }
    class PersistenciaException {
        <<exception>>
    }
    class EmailJaCadastradoException {
        <<exception>>
    }

    %% Persistencia
    class UsuarioRepositorio {
        <<interface>>
        +salvar(usuario: Usuario)
        +existeComEmail(email: Texto): Booleano
        +listarTodos(): Lista~Usuario~
    }
    class UsuarioRepositorioEmMemoria {
        <<repository>>
        -usuarios: Lista~Usuario~
        +salvar(usuario: Usuario)
        +existeComEmail(email: Texto): Booleano
        +listarTodos(): Lista~Usuario~
    }
    class UsuarioRepositorioEmArquivo {
        <<repository>>
        -arquivo: Caminho
        +salvar(usuario: Usuario)
        +existeComEmail(email: Texto): Booleano
        +listarTodos(): Lista~Usuario~
        -carregar()
        -gravar()
    }

    %% Controles
    class ControladorCadastro {
        <<control>>
        +cadastrar(dados)
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
    ControladorCadastro --> UsuarioRepositorio : persiste dados
    ControladorAutenticacao --> UsuarioRepositorio : consulta
    ControladorAutenticacao --> TokenRecuperacaoSenha : gera / valida
    ControladorPerfil --> UsuarioRepositorio : atualiza
    ControladorAdministracaoMembros --> UsuarioRepositorio : altera estado
    ControladorAdministracaoMembros --> ControladorAuditoria : aciona
    ControladorAuditoria --> RegistroAuditoria : persiste

    %% Relacionamentos Excecoes
    Usuario ..> LoginInvalidoException : lanca
    Usuario ..> SenhaInvalidaException : lanca
    UsuarioRepositorioEmArquivo ..> PersistenciaException : lanca
    ControladorCadastro ..> EmailJaCadastradoException : lanca

    %% Implementacoes de Persistencia
    UsuarioRepositorio <|.. UsuarioRepositorioEmMemoria : implementa
    UsuarioRepositorio <|.. UsuarioRepositorioEmArquivo : implementa

    %% Relacionamentos Entidade
    TokenRecuperacaoSenha "*" --> "1" Usuario : pertenceA
    RegistroAuditoria "*" --> "1" Usuario : adminResponsavel
    RegistroAuditoria "*" --> "1" Usuario : usuarioAfetado
    Usuario ..> VinculoCI : usa
    Usuario ..> PerfilDeAcesso : usa
    Usuario ..> StatusUsuario : usa