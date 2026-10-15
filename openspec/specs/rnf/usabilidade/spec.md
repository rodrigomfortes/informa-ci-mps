# rnf/usabilidade Specification

## Purpose
Requisitos não funcionais de facilidade de uso: mensagens de recusa compreensíveis e acesso pelo celular.

## Requirements

### Requirement: NF-USA-01 — Mensagens de recusa claras
O sistema DEVE explicar toda recusa de cadastro em português, indicando o campo com problema e a regra violada, sem códigos de erro nem termos técnicos.

**Prioridade:** Importante
**Casos de uso:** UC01

#### Scenario: Login com números
- **WHEN** um cadastro é recusado porque o login "ana123" contém números
- **THEN** a mensagem exibida é "Login inválido: O login não pode conter números.", que nomeia o campo e a regra em 1 frase

#### Scenario: Correção sem ajuda
- **GIVEN** 5 alunos do CI que nunca usaram o sistema
- **WHEN** cada um tem um cadastro recusado e é solicitado a corrigi-lo
- **THEN** pelo menos 4 dos 5 corrigem o dado na tentativa seguinte, sem ajuda

### Requirement: NF-USA-02 — Uso pelo celular
A interface web do sistema DEVE ser utilizável em telas a partir de 360 px de largura, sem rolagem horizontal, porque a maior parte dos acessos acontece pelo celular, entre aulas (entrevista E2).

**Prioridade:** Desejável

#### Scenario: Consulta pelo celular
- **GIVEN** um usuário acessa o sistema em um celular com tela de 360 px de largura
- **WHEN** ele abre qualquer página do sistema
- **THEN** a página é exibida com 0 px de rolagem horizontal
