## ADDED Requirements

### Requirement: RF05 — Publicar
O sistema DEVE permitir que um usuário com papel Publicador crie uma publicação informando título, texto e categoria (Notícia, Evento, Palestra ou Comunicado), e, para Evento e Palestra, também data, horário e local. A publicação DEVE registrar o autor e a data de publicação. Uma publicação recusada NÃO DEVE ser armazenada.

**Prioridade:** Essencial
**Casos de uso:** UC14

#### Scenario: Palestra válida
- **GIVEN** o usuário "coordenacao@ci.ufpb.br" tem papel Publicador
- **WHEN** ele publica o título "Introdução a Rust", o texto "Palestra aberta a todos.", a categoria Palestra, a data "20/11/2026 14:00" e o local "Auditório do CI"
- **THEN** o sistema informa "Publicação criada: Introdução a Rust" e a publicação passa a aparecer no feed

#### Scenario: Autor sem papel Publicador
- **GIVEN** o usuário "ana@academico.ufpb.br" tem papel Membro
- **WHEN** ele tenta publicar
- **THEN** o sistema recusa informando "Publicação inválida: Apenas usuários com papel Publicador podem publicar."
- **AND** a quantidade de publicações não muda

#### Scenario: Autor não cadastrado
- **WHEN** o e-mail do autor informado é "ninguem@ci.ufpb.br", que não está cadastrado
- **THEN** o sistema recusa informando "Não foi possível publicar: Não existe usuário cadastrado com o e-mail ninguem@ci.ufpb.br."

#### Scenario: Evento sem data
- **WHEN** o Publicador escolhe a categoria Evento e deixa a data em branco
- **THEN** o sistema recusa informando "Publicação inválida: Publicações da categoria Evento exigem data e horário."

#### Scenario: Título vazio
- **WHEN** o Publicador deixa o título em branco
- **THEN** o sistema recusa informando "Publicação inválida: O título da publicação é obrigatório."

#### Scenario: Título longo
- **WHEN** o Publicador informa um título com 101 caracteres
- **THEN** o sistema recusa informando "Publicação inválida: O título deve ter no máximo 100 caracteres."

#### Scenario: Data em formato inválido
- **WHEN** o Publicador informa a data "31/02/2026 10:00"
- **THEN** o sistema informa "Data e horário inválidos: use o formato dd/mm/aaaa hh:mm, como 20/11/2026 14:00." e pede a data novamente

### Requirement: RF06 — Consultar o feed
O sistema DEVE listar as publicações da mais recente para a mais antiga, exibindo para cada uma o número na lista, a categoria, o título, o login do autor, a data de publicação, a data e o local (quando houver) e o texto.

**Prioridade:** Essencial
**Casos de uso:** UC15

#### Scenario: Feed com publicações
- **GIVEN** os comunicados "Primeira" e "Segunda" foram publicados nessa ordem
- **WHEN** o feed é solicitado
- **THEN** o sistema exibe "1. [Comunicado] Segunda" antes de "2. [Comunicado] Primeira"

#### Scenario: Feed vazio
- **GIVEN** não há publicações
- **WHEN** o feed é solicitado
- **THEN** o sistema exibe "Nenhuma publicação cadastrada."

### Requirement: RF07 — Editar publicação
O sistema DEVE permitir editar título, texto, categoria, data e local de uma publicação escolhida pelo número no feed, mantendo o autor, a data de publicação e a posição no feed. Campos deixados em branco DEVEM manter o valor atual, e "-" DEVE apagar a data ou o local. Uma edição recusada NÃO DEVE alterar a publicação.

**Prioridade:** Essencial
**Casos de uso:** UC16

#### Scenario: Edição de título
- **GIVEN** existe o comunicado "Mudança de sala"
- **WHEN** o Publicador o escolhe, informa o título "Mudança de sala da disciplina" e deixa os demais campos em branco
- **THEN** o sistema informa "Publicação editada: Mudança de sala da disciplina"
- **AND** o texto e a categoria continuam os mesmos

#### Scenario: Edição inválida
- **GIVEN** existe a palestra "Introdução a Rust"
- **WHEN** o Publicador apaga a data com "-"
- **THEN** o sistema recusa informando "Publicação inválida: Publicações da categoria Palestra exigem data e horário."
- **AND** a publicação continua com a data anterior

#### Scenario: Número inexistente
- **GIVEN** o feed tem 1 publicação
- **WHEN** o Publicador informa o número 7
- **THEN** o sistema exibe "Número inválido: 7" e nada é alterado

### Requirement: RF08 — Remover publicação
O sistema DEVE permitir remover uma publicação escolhida pelo número no feed, após confirmação.

**Prioridade:** Essencial
**Casos de uso:** UC17

#### Scenario: Remoção confirmada
- **GIVEN** existe o comunicado "Aviso antigo"
- **WHEN** o Publicador o escolhe e confirma com "s"
- **THEN** o sistema informa "Publicação removida: Aviso antigo" e a quantidade de publicações diminui em 1

#### Scenario: Remoção cancelada
- **WHEN** o Publicador escolhe uma publicação e responde "n"
- **THEN** o sistema informa "Remoção cancelada." e a quantidade de publicações não muda

### Requirement: RF09 — Quantidade de entidades cadastradas
O sistema DEVE exibir a quantidade total de entidades cadastradas, que é a soma de usuários e publicações, junto com a quantidade de cada uma.

**Prioridade:** Importante
**Casos de uso:** UC19

#### Scenario: Usuários e publicações cadastrados
- **GIVEN** há 1 usuário e 1 publicação cadastrados
- **WHEN** a quantidade de entidades é solicitada no menu principal
- **THEN** o sistema exibe "Entidades cadastradas: 2 (1 usuários, 1 publicações)"

#### Scenario: Nada cadastrado
- **GIVEN** não há usuários nem publicações
- **WHEN** a quantidade de entidades é solicitada
- **THEN** o sistema exibe "Entidades cadastradas: 0 (0 usuários, 0 publicações)"
