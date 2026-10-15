# rnf/desempenho Specification

## Purpose
Requisitos não funcionais de tempo de resposta das operações de usuário com o armazenamento em arquivo binário.

## Requirements

### Requirement: NF-DES-01 — Tempo de cadastro e listagem
Com até 1.000 usuários cadastrados em arquivo binário, o sistema DEVE concluir um cadastro em até 500 ms e exibir a listagem em até 1 s, inclusive logo depois de ser iniciado.

**Prioridade:** Importante
**Casos de uso:** UC01, UC02

#### Scenario: Arquivo com 1.000 usuários
- **GIVEN** o arquivo de usuários tem 1.000 usuários, num computador com processador Intel Core i3-10100F (4 núcleos), 16 GB de RAM e SSD
- **WHEN** um novo usuário é cadastrado e, em seguida, o sistema é reiniciado e a listagem é solicitada
- **THEN** o cadastro termina em até 500 ms e a listagem é exibida em até 1 s
