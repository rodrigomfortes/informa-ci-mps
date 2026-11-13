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

    %% PADRÃO 1: REPOSITORY (Item 2)
    note for UsuarioRepositorio "Padrão: Repository\nIsola a camada business da persistência (infra)"
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
    UsuarioRepositorio <|.. UsuarioRepositorioEmMemoria : implementa
    UsuarioRepositorio <|.. UsuarioRepositorioEmArquivo : implementa

    %% PADRÃO 2: FACTORY METHOD (Item 2)
    note for FabricaRepositorio "Padrão: Factory Method\nCria a instância do repositório adequado"
    class FabricaRepositorio {
        <<factory>>
        +criarRepositorioUsuario(): UsuarioRepositorio
    }
    FabricaRepositorio ..> UsuarioRepositorio : instancia

    %% PADRÃO 3: ADAPTER (Item 3)
    note for LogAdapter "Padrão: Adapter\nAdapta uma lib de log externa para uso no sistema"
    class ILogger {
        <<interface>>
        +registrarLog(mensagem: Texto)
    }
    class LogAdapter {
        <<adapter>>
        -libExterna: LibLogExterna
        +registrarLog(mensagem: Texto)
    }
    class LibLogExterna {
        <<external>>
        +log(msg: Texto)
    }
    ILogger <|.. LogAdapter : implementa
    LogAdapter --> LibLogExterna : encapsula/adapta
    ControladorAuditoria --> ILogger : usa

    %% PADRÃO 4: TEMPLATE METHOD (Item 4)
    note for GeradorRelatorioAcessos "Padrão: Template Method\nEsqueleto do algoritmo para geração de estatísticas"
    class GeradorRelatorioAcessos {
        <<template>>
        +gerarRelatorio() 
        #coletarEstatisticasAcesso()*
        #formatarConteudo()*
        #exportar()*
    }
    class GeradorRelatorioHTML {
        #formatarConteudo()
        #exportar()
    }
    class GeradorRelatorioPDF {
        #formatarConteudo()
        #exportar()
    }
    GeradorRelatorioAcessos <|-- GeradorRelatorioHTML : especializa
    GeradorRelatorioAcessos <|-- GeradorRelatorioPDF : especializa

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
        +registrarAuditoria(acao, usuarioAfetado, adminResp)
    }
    class ControladorEstatisticas {
        <<control>>
        +solicitarRelatorio(tipo: Texto)
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
    class TelaRelatorios {
        <<boundary>>
        +solicitarPDF()
        +solicitarHTML()
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
    TelaRelatorios --> ControladorEstatisticas : aciona

    %% Relacionamentos Controle -> Entidade, Persistencia e Template
    ControladorCadastro --> Usuario : instancia
    ControladorCadastro --> UsuarioRepositorio : persiste dados
    ControladorCadastro --> FabricaRepositorio : solicita repositorio
    ControladorAutenticacao --> UsuarioRepositorio : consulta
    ControladorAutenticacao --> TokenRecuperacaoSenha : gera / valida
    ControladorPerfil --> UsuarioRepositorio : atualiza
    ControladorAdministracaoMembros --> UsuarioRepositorio : altera estado
    ControladorAdministracaoMembros --> ControladorAuditoria : aciona
    ControladorAuditoria --> RegistroAuditoria : persiste
    ControladorEstatisticas --> GeradorRelatorioAcessos : aciona

    %% Relacionamentos Excecoes
    class LoginInvalidoException { <<exception>> }
    class SenhaInvalidaException { <<exception>> }
    class PersistenciaException { <<exception>> }
    class EmailJaCadastradoException { <<exception>> }
    
    Usuario ..> LoginInvalidoException : lanca
    Usuario ..> SenhaInvalidaException : lanca
    UsuarioRepositorioEmArquivo ..> PersistenciaException : lanca
    ControladorCadastro ..> EmailJaCadastradoException : lanca

    %% Relacionamentos Entidade
    TokenRecuperacaoSenha "*" --> "1" Usuario : pertenceA
    RegistroAuditoria "*" --> "1" Usuario : adminResponsavel
    RegistroAuditoria "*" --> "1" Usuario : usuarioAfetado
    Usuario ..> VinculoCI : usa
    Usuario ..> PerfilDeAcesso : usa
    Usuario ..> StatusUsuario : usa