# Arquitetura

> Seção 9 do modelo. Os diagramas de arquitetura **lógica** e **física** e a tabela completa entram na **Entrega 2 (30/10/2026)**, conforme o [cronograma](../00-entregas.md).
> Decisões importantes ficam registradas como ADRs em [`adr/`](adr/).

## Decisões registradas

| ADR | Decisão | Requisitos |
|---|---|---|
| [ADR-0001](adr/0001-persistencia-substituivel.md) | Tornar o mecanismo de persistência substituível | NF-MAN-01, NF-CON-01, NF-CON-02 |

## Como cada requisito não funcional é atendido (parcial — Sprints 1 e 2)

| RNF | Decisão / tática | Onde |
|---|---|---|
| NF-SEG-01 | `Usuario.toString` e a listagem não incluem a senha; as mensagens de validação descrevem a regra sem repetir o valor digitado | `entidade`, `fronteira` |
| NF-CON-01 | `IOException` é convertida em `PersistenciaException`, capturada pela tela ou pela inicialização, que informa o usuário e continuam em execução | `persistencia`, `fronteira`, `Aplicacao` |
| NF-CON-02 | Arquivo binário regravado a cada cadastro, guardando os usuários na ordem de inserção | `UsuarioRepositorioEmArquivo` |
| NF-MAN-01 | Interface `UsuarioRepositorio` no pacote `controle`; implementação escolhida em `Aplicacao` | [ADR-0001](adr/0001-persistencia-substituivel.md) |
| NF-MAN-02 | Testes JUnit por cenário das specs | [`08-projeto.md` §4](../08-projeto.md#4-rastreabilidade-cenário--teste) |
| NF-HSW-01 | Só a biblioteca padrão do Java 21; JUnit com escopo de teste no `pom.xml` | `pom.xml` |
