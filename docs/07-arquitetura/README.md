# Arquitetura

> Seção 9 do modelo. Os diagramas de arquitetura **lógica** e **física** e a tabela completa entram na **Entrega 2 (30/10/2026)**, conforme o [cronograma](../00-entregas.md).
> Decisões importantes ficam registradas como ADRs em [`adr/`](adr/).

## Decisões registradas

| ADR | Decisão | Requisitos |
|---|---|---|
| [ADR-0001](adr/0001-persistencia-substituivel.md) | Tornar o mecanismo de persistência substituível | NF-MAN-01, NF-CON-01, NF-CON-02 |
| [ADR-0002](adr/0002-fachada-unica.md) | Fachada única (Facade + Singleton) para a camada de negócio | NF-MAN-01 |
| [ADR-0003](adr/0003-repository-e-fabrica-de-repositorios.md) | Repository para todas as entidades, criado por Abstract Factory escolhida por Factory Method | NF-MAN-01, NF-CON-01 |
| [ADR-0004](adr/0004-adapter-servico-de-log.md) | Adapter para isolar a biblioteca de log (`java.util.logging`) | NF-MAN-01, NF-SEG-02 |
| [ADR-0005](adr/0005-template-method-relatorio-de-acessos.md) | Template Method para o relatório de acessos em HTML e PDF | RF12, RF13, NF-HSW-01 |

## Como cada requisito não funcional é atendido (parcial — Sprints 1 e 2)

| RNF | Decisão / tática | Onde |
|---|---|---|
| NF-SEG-01 | `Usuario.toString` e a listagem não incluem a senha; as mensagens de validação descrevem a regra sem repetir o valor digitado | `entidade`, `fronteira` |
| NF-CON-01 | `IOException` é convertida em `PersistenciaException`, capturada pela tela ou pela inicialização, que informa o usuário e continuam em execução | `persistencia`, `fronteira`, `Aplicacao` |
| NF-CON-02 | Arquivo binário regravado a cada cadastro, guardando os usuários na ordem de inserção | `UsuarioRepositorioEmArquivo` |
| NF-MAN-01 | Interfaces `UsuarioRepositorio`, `PublicacaoRepositorio` e `FabricaDeRepositorios` no pacote `controle`; a fábrica concreta é escolhida em `Aplicacao` por `TipoDeArmazenamento` | [ADR-0001](adr/0001-persistencia-substituivel.md), [ADR-0003](adr/0003-repository-e-fabrica-de-repositorios.md) |
| NF-MAN-02 | Testes JUnit por cenário das specs | [`08-projeto.md` §4](../08-projeto.md#4-rastreabilidade-cenário--teste) |
| NF-HSW-01 | Só a biblioteca padrão do Java 21; JUnit com escopo de teste no `pom.xml` | `pom.xml` |
