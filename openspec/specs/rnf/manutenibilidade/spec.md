# rnf/manutenibilidade Specification

## Purpose
Requisitos não funcionais de facilidade de modificar e testar o sistema, como a troca do mecanismo de armazenamento.

## Requirements

### Requirement: NF-MAN-01 — Persistência substituível
O sistema DEVE permitir escolher, ao iniciar, o armazenamento dos usuários entre memória e arquivo binário, sem que as classes de entidade e de controle dependam da implementação escolhida.

**Prioridade:** Essencial
**Casos de uso:** UC01, UC02

#### Scenario: Execução em memória
- **GIVEN** o sistema é iniciado com o armazenamento em memória
- **WHEN** 2 usuários são cadastrados e o sistema é reiniciado, de novo em memória
- **THEN** a listagem exibe 0 usuários

#### Scenario: Execução com arquivo binário
- **GIVEN** o sistema é iniciado com o armazenamento em arquivo binário
- **WHEN** 2 usuários são cadastrados e o sistema é reiniciado, de novo em arquivo
- **THEN** a listagem exibe os 2 usuários

#### Scenario: Dependências do código
- **WHEN** os imports dos pacotes `informaci.entidade` e `informaci.controle` são inspecionados
- **THEN** há 0 referências ao pacote `informaci.persistencia`

### Requirement: NF-MAN-02 — Regras cobertas por testes automatizados
O sistema DEVE ter ao menos 1 teste automatizado para cada cenário dos requisitos RF01 a RF04, e a suíte completa DEVE passar antes de cada entrega.

**Prioridade:** Importante
**Casos de uso:** UC01, UC02

#### Scenario: Execução da suíte
- **WHEN** a equipe executa `mvn test`
- **THEN** 100% dos testes passam e cada cenário de RF01 a RF04 é verificado por pelo menos 1 teste
