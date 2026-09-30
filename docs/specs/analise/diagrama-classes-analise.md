classDiagram
    %% Enumerações
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

    %% Relacionamentos Controle -> Entidade
    ControladorCadastro --> Usuario : instancia / persiste
    ControladorAutenticacao --> Usuario : consulta
    ControladorAutenticacao --> TokenRecuperacaoSenha : gera / valida
    ControladorPerfil --> Usuario : atualiza
    ControladorAdministracaoMembros --> Usuario : altera estado
    ControladorAdministracaoMembros --> ControladorAuditoria : aciona
    ControladorAuditoria --> RegistroAuditoria : persiste

    %% Relacionamentos Entidade
    TokenRecuperacaoSenha "*" --> "1" Usuario : pertenceA
    RegistroAuditoria "*" --> "1" Usuario : adminResponsavel
    RegistroAuditoria "*" --> "1" Usuario : usuarioAfetado
    Usuario ..> VinculoCI : usa
    Usuario ..> PerfilDeAcesso : usa
    Usuario ..> StatusUsuario : usa