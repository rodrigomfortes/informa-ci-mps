# rnf/seguranca Specification

## Purpose
Requisitos não funcionais de proteção da senha e dos dados pessoais dos usuários do Informa CI.

## Requirements

### Requirement: NF-SEG-01 — Senha nunca exibida
O sistema NÃO DEVE exibir a senha de um usuário em listagens, mensagens de erro, confirmações ou na representação textual do usuário.

**Prioridade:** Essencial
**Casos de uso:** UC01, UC02

#### Scenario: Erro de validação de senha
- **WHEN** um cadastro é recusado porque a senha "senhafraca" é inválida
- **THEN** a mensagem descreve a regra violada e contém 0 ocorrências do texto "senhafraca"

#### Scenario: Listagem de usuários
- **GIVEN** 2 usuários cadastrados com a senha "Senha@123"
- **WHEN** o Administrador solicita a listagem
- **THEN** a saída contém 0 ocorrências do texto "Senha@123"

### Requirement: NF-SEG-02 — Dados pessoais mínimos
O sistema DEVE coletar de cada usuário apenas os dados necessários ao seu funcionamento (nome, e-mail, login e senha), além do papel, em conformidade com o princípio da necessidade da LGPD.

**Prioridade:** Importante
**Casos de uso:** UC01

#### Scenario: Formulário de cadastro
- **WHEN** o Administrador inicia o cadastro de um usuário
- **THEN** o sistema solicita exatamente 5 informações: nome, e-mail, login, senha e papel
