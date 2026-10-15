# usuarios Specification

## Purpose
Gerenciamento dos usuários do Informa CI: cadastro com validação de nome, e-mail, login e senha, e listagem dos usuários cadastrados. É o núcleo essencial das Sprints 1 e 2.

## Requirements

### Requirement: RF01 — Cadastrar usuário
O sistema DEVE permitir que o Administrador cadastre usuários informando nome, e-mail, login, senha e papel (Membro ou Administrador), recusando e-mails já cadastrados sem diferenciar maiúsculas de minúsculas e informando o motivo de qualquer recusa. Um cadastro recusado NÃO DEVE ser armazenado.

**Prioridade:** Essencial
**Casos de uso:** UC01

#### Scenario: Cadastro válido
- **GIVEN** não existe usuário com o e-mail "ana@academico.ufpb.br"
- **WHEN** o Administrador cadastra o nome "Ana Souza", o e-mail "ana@academico.ufpb.br", o login "anasouza", a senha "Senha@123" e o papel Membro
- **THEN** o sistema informa "Usuário adicionado: anasouza" e o usuário passa a aparecer na listagem

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

### Requirement: RF02 — Regras de login
O sistema DEVE aceitar apenas logins não vazios, com no máximo 12 caracteres e sem números. Espaços no início e no fim são desconsiderados.

**Prioridade:** Essencial
**Casos de uso:** UC01

#### Scenario: Login vazio
- **WHEN** o Administrador tenta cadastrar um usuário com o login vazio
- **THEN** o sistema recusa o cadastro informando "Login inválido: O login é obrigatório."

#### Scenario: Login com mais de 12 caracteres
- **WHEN** o Administrador informa o login "anasouzalimas" (13 caracteres)
- **THEN** o sistema recusa o cadastro informando "Login inválido: O login deve ter no máximo 12 caracteres."

#### Scenario: Login com exatamente 12 caracteres
- **WHEN** o Administrador informa o login "anasouzalima" (12 caracteres), com os demais dados válidos
- **THEN** o usuário é cadastrado

#### Scenario: Login com números
- **WHEN** o Administrador informa o login "ana123"
- **THEN** o sistema recusa o cadastro informando "Login inválido: O login não pode conter números."

### Requirement: RF03 — Regras de senha
O sistema DEVE aceitar apenas senhas que sigam a política de senha padrão do AWS IAM: de 8 a 128 caracteres, com pelo menos 3 dos 4 tipos (letra maiúscula, letra minúscula, número, símbolo) e diferentes do login e do e-mail do usuário, sem diferenciar maiúsculas de minúsculas nessa comparação.

Símbolos aceitos: `! @ # $ % ^ & * ( ) _ + - = [ ] { } | '`

**Prioridade:** Essencial
**Casos de uso:** UC01

#### Scenario: Senha vazia
- **WHEN** o Administrador tenta cadastrar um usuário sem senha
- **THEN** o sistema recusa o cadastro informando "Senha inválida: A senha é obrigatória."

#### Scenario: Senha curta
- **WHEN** o Administrador informa a senha "Ab#1" (4 caracteres)
- **THEN** o sistema recusa o cadastro informando "Senha inválida: A senha deve ter entre 8 e 128 caracteres."

#### Scenario: Senha longa
- **WHEN** o Administrador informa uma senha com 129 caracteres
- **THEN** o sistema recusa o cadastro informando "Senha inválida: A senha deve ter entre 8 e 128 caracteres."

#### Scenario: Senha com poucos tipos de caractere
- **WHEN** o Administrador informa a senha "senhafraca" (só letras minúsculas)
- **THEN** o sistema recusa o cadastro informando "Senha inválida: A senha deve combinar ao menos 3 destes tipos de caractere: letras maiúsculas, letras minúsculas, números e símbolos (!@#$%^&*()_+-=[]{}|')."

#### Scenario: Senha com três tipos de caractere
- **WHEN** o Administrador informa a senha "senha#2026" (letras minúsculas, símbolo e números)
- **THEN** a senha é aceita

#### Scenario: Senha igual ao login
- **WHEN** o Administrador informa o login "Ana_Souza#" e a senha "Ana_Souza#" (válida nas demais regras)
- **THEN** o sistema recusa o cadastro informando "Senha inválida: A senha não pode ser igual ao login nem ao e-mail."

#### Scenario: Senha igual ao e-mail
- **WHEN** o Administrador informa o e-mail "ana.souza@ufpb.br" e a senha "Ana.Souza@ufpb.br"
- **THEN** o sistema recusa o cadastro informando "Senha inválida: A senha não pode ser igual ao login nem ao e-mail."

### Requirement: RF04 — Listar usuários
O sistema DEVE permitir que o Administrador liste todos os usuários cadastrados, na ordem de cadastro, exibindo login, nome, e-mail e papel, sem exibir a senha.

**Prioridade:** Essencial
**Casos de uso:** UC02

#### Scenario: Listagem com usuários
- **GIVEN** os usuários "anasouza" (Membro) e "brunolima" (Administrador) foram cadastrados nessa ordem
- **WHEN** o Administrador solicita a listagem
- **THEN** o sistema exibe 2 linhas, nesta ordem: "anasouza | Ana Souza | ana@academico.ufpb.br | MEMBRO" e "brunolima | Bruno Lima | bruno@academico.ufpb.br | ADMINISTRADOR"

#### Scenario: Nenhum usuário
- **GIVEN** não há usuários cadastrados
- **WHEN** o Administrador solicita a listagem
- **THEN** o sistema exibe "Nenhum usuário cadastrado."
