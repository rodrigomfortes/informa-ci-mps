# rnf/hardware-software Specification

## Purpose
Requisitos não funcionais de plataforma de execução e de tecnologias permitidas no desenvolvimento do Informa CI.

## Requirements

### Requirement: NF-HSW-01 — Java 21 sem frameworks
O sistema DEVE ser desenvolvido em Java 21 e construído com Maven, sem frameworks de aplicação; a única dependência externa permitida é o JUnit, com escopo de teste.

**Prioridade:** Essencial

#### Scenario: Dependências do projeto
- **WHEN** as dependências declaradas no `pom.xml` são inspecionadas
- **THEN** existe exatamente 1 dependência, o JUnit, com escopo `test`

#### Scenario: Execução em sistemas diferentes
- **GIVEN** um computador com Windows e outro com Linux, ambos apenas com o JDK 21 e o Maven instalados
- **WHEN** a equipe executa `mvn test` nos 2
- **THEN** a suíte passa nos 2, sem instalar nenhum outro software
