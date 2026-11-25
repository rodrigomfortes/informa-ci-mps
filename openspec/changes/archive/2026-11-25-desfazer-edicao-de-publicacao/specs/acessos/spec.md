## MODIFIED Requirements

### Requirement: RF12 — Registrar acessos
O sistema DEVE registrar como acesso cada operação concluída com sucesso: cadastro, listagem, edição e remoção de usuário; publicação, consulta ao feed, edição, remoção de publicação e desfazer edição de publicação. Cada registro DEVE guardar a data e a hora, a operação e o login do usuário envolvido (o usuário cadastrado, editado ou removido, ou o autor da publicação), ou nenhum usuário nas consultas gerais. Uma operação recusada NÃO DEVE ser registrada, e uma falha ao guardar o registro NÃO DEVE impedir a operação.

**Prioridade:** Importante
**Casos de uso:** UC24

#### Scenario: Operações concluídas
- **GIVEN** não há acessos registrados
- **WHEN** um usuário Publicador "coordenacao" é cadastrado, publica um comunicado e o feed é consultado
- **THEN** o relatório de acessos informa "Total de acessos: 3", com 1 acesso de cada operação e 2 acessos do usuário "coordenacao"

#### Scenario: Operação recusada
- **WHEN** alguém tenta publicar com o e-mail "ninguem@ci.ufpb.br", que não está cadastrado
- **THEN** a publicação é recusada e o relatório continua informando "Total de acessos: 0"

#### Scenario: Acessos preservados entre execuções
- **GIVEN** o sistema usa armazenamento em arquivo
- **WHEN** 2 acessos são registrados e o sistema é reiniciado
- **THEN** os 2 acessos continuam registrados, na mesma ordem
