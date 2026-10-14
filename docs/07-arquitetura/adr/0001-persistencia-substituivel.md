# ADR-0001: Tornar o mecanismo de persistência substituível

- **Status:** aceito
- **Data:** 2026-10-07
- **Requisitos relacionados:** NF-MAN-01, NF-CON-01, NF-CON-02

## Contexto

Na Sprint 1 os usuários ficam só em memória. Na Sprint 2 é preciso guardá-los também em arquivo binário ou banco de dados, escolhendo o mecanismo ao iniciar o sistema (NF-MAN-01), sem reescrever as regras de cadastro e listagem. Falhas de leitura ou gravação não podem derrubar o sistema (NF-CON-01), e o projeto não pode usar frameworks (NF-HSW-01).

## Decisão

As classes de controle dependem de uma interface, `UsuarioRepositorio` (`salvar`, `existeComEmail`, `listarTodos`), declarada no próprio pacote `controle`. Há duas implementações no pacote `persistencia`: `UsuarioRepositorioEmMemoria` e `UsuarioRepositorioEmArquivo`, que usa serialização Java (`ObjectOutputStream`). A escolha é feita uma única vez, em `Aplicacao`, que pergunta ao usuário o armazenamento ao iniciar. As `IOException` são convertidas em `PersistenciaException`, uma exceção não verificada que a tela transforma em mensagem.

## Alternativas consideradas

| Alternativa | Por que não |
|---|---|
| `if (modo == ARQUIVO)` espalhado pelo controle | Duplica a decisão e mistura regra de negócio com detalhe de armazenamento |
| Interface no pacote `persistencia` | O controle passaria a importar a persistência; com a interface no `controle`, a dependência fica invertida desde já (NF-MAN-01, cenário "Dependências do código") |
| Banco de dados desde o início | Exigiria driver JDBC e um servidor de banco antes de haver regra de negócio para testar; o arquivo binário só usa a biblioteca padrão do Java |
| Propagar `IOException` (exceção verificada) | Obrigaria o controle a conhecer detalhes de E/S e a declarar `throws` em todas as operações |

## Consequências

- **Positivas:** os testes de negócio rodam em memória, sem arquivos; trocar ou acrescentar um mecanismo (por exemplo, banco de dados) não altera `controle` nem `entidade`. Prepara os padrões Repository e Factory da Sprint 4.
- **Negativas / riscos:** a implementação em arquivo regrava o arquivo inteiro a cada cadastro. Com 1.000 usuários isso levou cerca de 80 ms, dentro do limite de NF-DES-01, mas não escala para volumes muito maiores. Arquivos gravados com uma versão incompatível da classe `Usuario` deixam de ser lidos; a falha é tratada (NF-CON-01), mas os dados antigos não são migrados.
