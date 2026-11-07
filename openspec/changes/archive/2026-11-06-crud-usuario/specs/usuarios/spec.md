## ADDED Requirements

### Requirement: RF10 — Editar usuário
O sistema DEVE permitir que o Administrador edite nome, e-mail, login, senha e papel de um usuário escolhido pelo número na lista, mantendo a sua identidade e a posição na listagem. Campos deixados em branco DEVEM manter o valor atual, e os novos dados DEVEM seguir as mesmas regras do cadastro (RF01 a RF03). Uma edição recusada NÃO DEVE alterar o usuário. As publicações do usuário DEVEM passar a exibir os dados novos do autor.

**Prioridade:** Essencial
**Casos de uso:** UC23

#### Scenario: Edição do nome e do papel
- **GIVEN** existe o usuário "anasouza" com papel Membro
- **WHEN** o Administrador o escolhe, informa o nome "Ana Souza Lima", escolhe o papel 3 (Publicador) e deixa os demais campos em branco
- **THEN** o sistema informa "Usuário editado: anasouza"
- **AND** o e-mail, o login e a senha continuam os mesmos

#### Scenario: Login inválido na edição
- **WHEN** o Administrador altera o login para "ana123"
- **THEN** o sistema recusa informando "Login inválido: O login não pode conter números."
- **AND** o usuário continua com o login anterior

#### Scenario: E-mail de outro usuário
- **GIVEN** existem os usuários "ana@academico.ufpb.br" e "bruno@academico.ufpb.br"
- **WHEN** o Administrador altera o e-mail de Ana para "BRUNO@academico.ufpb.br"
- **THEN** o sistema recusa informando "Não foi possível editar: Já existe um usuário cadastrado com o e-mail bruno@academico.ufpb.br."

#### Scenario: Autor editado
- **GIVEN** o Publicador "coordenacao" tem 1 publicação
- **WHEN** o Administrador altera o login dele para "coordcc"
- **THEN** a publicação passa a exibir "Por coordcc" no feed

### Requirement: RF11 — Remover usuário
O sistema DEVE permitir que o Administrador remova um usuário escolhido pelo número na lista, após confirmação. O sistema NÃO DEVE remover um usuário que seja autor de publicações.

**Prioridade:** Essencial
**Casos de uso:** UC12

#### Scenario: Remoção confirmada
- **GIVEN** o usuário "anasouza" não tem publicações
- **WHEN** o Administrador o escolhe e confirma com "s"
- **THEN** o sistema informa "Usuário removido: anasouza" e a quantidade de usuários diminui em 1

#### Scenario: Remoção cancelada
- **WHEN** o Administrador escolhe um usuário e responde "n"
- **THEN** o sistema informa "Remoção cancelada." e a quantidade de usuários não muda

#### Scenario: Autor de publicações
- **GIVEN** o usuário "coordenacao" é autor de 1 publicação
- **WHEN** o Administrador tenta removê-lo e confirma
- **THEN** o sistema recusa informando "Não foi possível remover: O usuário coordenacao é autor de 1 publicação(ões); remova-as antes de remover o usuário."
- **AND** a quantidade de usuários não muda
