# acessos Specification

## Purpose
Registro dos acessos ao Informa CI (operações concluídas) e geração do relatório de estatísticas de acesso em HTML e PDF. Núcleo essencial da Sprint 4.

## Requirements

### Requirement: RF12 — Registrar acessos
O sistema DEVE registrar como acesso cada operação concluída com sucesso: cadastro, listagem, edição e remoção de usuário; publicação, consulta ao feed, edição e remoção de publicação. Cada registro DEVE guardar a data e a hora, a operação e o login do usuário envolvido (o usuário cadastrado, editado ou removido, ou o autor da publicação), ou nenhum usuário nas consultas gerais. Uma operação recusada NÃO DEVE ser registrada, e uma falha ao guardar o registro NÃO DEVE impedir a operação.

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

### Requirement: RF13 — Gerar relatório de acessos
O sistema DEVE gerar, pelo menu principal, o relatório de estatísticas de acesso em HTML ou em PDF, à escolha do usuário, gravado como "relatorio-de-acessos.html" ou "relatorio-de-acessos.pdf". Os dois formatos DEVEM trazer as mesmas seções, nesta ordem: título, data de geração, total de acessos, período, acessos por operação, acessos por dia e até 5 usuários mais ativos.

**Prioridade:** Importante
**Casos de uso:** UC24

#### Scenario: Relatório em HTML
- **GIVEN** há 1 acesso registrado
- **WHEN** o usuário escolhe "4 - Relatório de acessos" e o formato 1 (HTML)
- **THEN** o sistema informa "Relatório gerado: <caminho>/relatorio-de-acessos.html" e o arquivo contém "Total de acessos: 1"

#### Scenario: Relatório em PDF
- **WHEN** o usuário escolhe o formato 2 (PDF)
- **THEN** o sistema grava "relatorio-de-acessos.pdf", um PDF válido que pode ser aberto em qualquer leitor, com as mesmas seções do HTML

#### Scenario: Formato inválido
- **WHEN** o usuário informa o formato 9
- **THEN** o sistema informa "Formato inválido." e pede o formato novamente

#### Scenario: Sem acessos
- **GIVEN** não há acessos registrados
- **WHEN** o relatório é gerado
- **THEN** ele informa "Nenhum acesso registrado até o momento." e as tabelas de dias e usuários mostram "(nenhum acesso)"

#### Scenario: Falha ao gravar
- **WHEN** a pasta do relatório não permite gravar o arquivo
- **THEN** o sistema informa "Não foi possível gravar o relatório em" seguido do caminho e continua em execução
