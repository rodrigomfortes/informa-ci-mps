## MODIFIED Requirements

### Requirement: RF01 — Cadastrar usuário
O sistema DEVE permitir que o Administrador cadastre usuários informando nome, e-mail, login, senha e papel (Membro, Administrador ou Publicador), recusando e-mails já cadastrados sem diferenciar maiúsculas de minúsculas e informando o motivo de qualquer recusa. Um cadastro recusado NÃO DEVE ser armazenado.

**Prioridade:** Essencial
**Casos de uso:** UC01

#### Scenario: Cadastro válido
- **GIVEN** não existe usuário com o e-mail "ana@academico.ufpb.br"
- **WHEN** o Administrador cadastra o nome "Ana Souza", o e-mail "ana@academico.ufpb.br", o login "anasouza", a senha "Senha@123" e o papel Membro
- **THEN** o sistema informa "Usuário adicionado: anasouza" e o usuário passa a aparecer na listagem

#### Scenario: Cadastro de Publicador
- **WHEN** o Administrador cadastra um usuário com dados válidos e escolhe o papel 3 (Publicador)
- **THEN** o usuário é cadastrado com o papel PUBLICADOR e passa a poder publicar (RF05)

#### Scenario: E-mail já cadastrado
- **GIVEN** já existe um usuário com o e-mail "ana@academico.ufpb.br"
- **WHEN** o Administrador cadastra outro usuário com o e-mail "ANA@Academico.UFPB.BR"
- **THEN** o sistema recusa o cadastro informando "Não foi possível adicionar: Já existe um usuário cadastrado com o e-mail ana@academico.ufpb.br."
- **AND** a quantidade de usuários armazenados não muda

#### Scenario: E-mail em formato inválido
- **WHEN** o Administrador informa o e-mail "email-invalido"
- **THEN** o sistema recusa o cadastro informando "Não foi possível adicionar: E-mail em formato inválido: email-invalido"

#### Scenario: Nome vazio
- **WHEN** o Administrador deixa o nome em branco
- **THEN** o sistema recusa o cadastro informando "Não foi possível adicionar: O nome do usuário é obrigatório."
