# Elicitação de requisitos

> Seção 4 do modelo. Para cada técnica: **por que** foi escolhida e **quando, onde e com quem** foi aplicada.

A elicitação aconteceu entre 27 e 29/09/2026 e combinou duas técnicas complementares: **entrevistas semiestruturadas** com alunos, que vivem o problema no dia a dia, e um **brainstorming** da equipe, que transformou os relatos em funcionalidades.

| Técnica | Data e horário | Local | Participantes |
|---|---|---|---|
| Entrevista E1 | 27/09/2026 (domingo), 19h30–20h10 | On-line (Google Meet) | Estudante de graduação, membro de centro acadêmico. Entrevistador: Rodrigo |
| Entrevista E2 | 28/09/2026 (segunda), 10h00–10h30 | Área de convivência do CI | Estudante de graduação ingressante. Entrevistador: Rodrigo |
| Brainstorming | 29/09/2026 (terça), 14h00–14h30 | Sala de aula do CI | Os 6 integrantes da equipe |

## 4.1 Entrevistas semiestruturadas com alunos

| Campo | Registro |
|---|---|
| Datas e horários | E1: 27/09/2026, 19h30–20h10 · E2: 28/09/2026, 10h00–10h30 |
| Local | E1: on-line, pelo Google Meet · E2: área de convivência do CI, no intervalo entre aulas |
| Participantes | E1: estudante de graduação, membro de um centro acadêmico e responsável pela divulgação dos eventos da entidade · E2: estudante de graduação ingressante · Entrevistador: Rodrigo Monteiro Fortes de Oliveira |
| Registro | Anotações do entrevistador. O áudio foi gravado com autorização, usado só para conferir as anotações e apagado depois da consolidação. Os entrevistados são identificados apenas pelo perfil |

**Perguntas-guia:**
1. Como você fica sabendo das notícias, eventos e avisos do CI hoje?
2. Quantos grupos ou perfis você acompanha para isso?
3. Já perdeu algum evento ou aviso importante? Por quê?
4. Quem pode publicar avisos nesses grupos?
5. Por onde você costuma ver esses avisos (celular, computador)?
6. O que um lugar único para essas informações precisaria ter para você usar?

**Por que esta técnica:** a entrevista permite entender, pelo relato de quem vive o problema, como a informação do CI circula hoje e por que se perde. Os entrevistados representam duas situações diferentes: quem **divulga** eventos de uma entidade estudantil (E1) e quem **acabou de chegar** ao CI (E2). O formato semiestruturado permite abordar os mesmos temas com os dois estudantes e, ao mesmo tempo, aprofundar o que surgisse na conversa. As conversas foram on-line ou em local informal, conforme a disponibilidade de cada estudante, para não atrapalhar a rotina de aulas.

**Principais achados:**

- **E1** — divulga os eventos da entidade no Instagram e em grupos de WhatsApp, precisa repetir cada aviso em vários canais e não sabe se ele chega a todos → **P1**, Módulo de Notícias (visão §2.3).
- **E1** — avisos repassados de grupo em grupo perdem o horário e o local, e os interessados perguntam nos próprios grupos; defende publicações com formato padronizado (data, horário, local) → **P5**, Módulo de Notícias.
- **E1** — nos grupos de avisos, só os administradores publicam; defende que o novo canal mantenha esse controle, com publicação apenas por pessoas autorizadas, e que o cadastro exija o e-mail institucional → **P6**, papel Publicador, UC04 e UC05 (criação de conta com confirmação do e-mail institucional).
- **E1** — sugere um responsável capaz de bloquear contas mal usadas e saber quem fez cada alteração → **P6**, papel Administrador, UC10 e UC13.
- **E2** — só entrou nos grupos do curso semanas após o início das aulas e perdeu atividades da recepção aos calouros → **P2**, **P3**.
- **E2** — acompanha mais de 5 grupos, onde os avisos se perdem no meio das conversas e são difíceis de achar depois → **P1**, **P4**.
- **E2** — consulta os grupos quase sempre pelo celular, nos intervalos entre aulas; pediu um único lugar, acessível pelo celular, com os eventos passados e os próximos → **P4**, **NF-USA-02**.

## 4.2 Brainstorming com a equipe

| Campo | Registro |
|---|---|
| Data e horário | 29/09/2026 (terça), 14h00–14h30 |
| Local | Sala de aula do CI |
| Participantes | Gabriel de Albuquerque Simplício, Gabriel Liszt Ferreira de Oliveira, Gerson Gonçalves de Freitas, Gustavo de Andrade Lacerda, Nicolas Kleiton da Silva Melo e Rodrigo Monteiro Fortes de Oliveira |
| Registro | Os resultados da sessão estão consolidados na tabela "Resultados" abaixo |

**Por que esta técnica:** é rápida e adequada a uma equipe pequena. Permitiu juntar os resultados das entrevistas à experiência dos próprios integrantes, que também são alunos do CI.

**Como foi feito:** o entrevistador resumiu E1 e E2, cada integrante sugeriu ideias livremente e, no fim, a equipe decidiu o que entraria no sistema.

**Resultados:**

| Ideia | Decisão |
|---|---|
| Um feed único com notícias, eventos e comunicados | Entra → Módulo de Notícias (P1–P5) |
| Só pessoas autorizadas publicam em nome do CI | Entra → papel Publicador (P6) |
| Mural de objetos perdidos e encontrados, com local de retirada | Entra → Módulo de Achados e Perdidos (P7, P8) |
| Cadastro de usuários com login, senha e papel | Entra, como base dos demais módulos → `openspec/specs/usuarios/` |
| Chat entre usuários | Fora do escopo |
| Publicar automaticamente no WhatsApp ou no Instagram | Fora do escopo |
| Aplicativo nativo para celular | Fora do escopo; o acesso será pelo navegador |

O mural de achados e perdidos veio da experiência dos integrantes, não das entrevistas: hoje cada objeto vira um e-mail para toda a comunidade.

## 4.3 Considerações finais

As técnicas se complementaram. As entrevistas mostraram como os problemas afetam quem divulga eventos e quem acabou de chegar ao CI. O brainstorming somou a experiência da equipe, revelou o problema dos achados e perdidos e converteu os achados no escopo do sistema.

**Limitações:** a elicitação durou três dias e ouviu diretamente só dois alunos, complementados pela experiência dos integrantes. Professores, técnicos administrativos e visitantes não foram consultados. Por isso, os requisitos ligados à publicação em nome do CI e à administração das contas serão validados com servidores ao longo das sprints, e este arquivo será atualizado a cada nova atividade de elicitação.

### Rastreabilidade problema → requisito

Nesta entrega só o gerenciamento de usuários tem requisitos detalhados (ver [cronograma](00-entregas.md)). Os problemas atendidos pelos demais módulos apontam para a visão e serão detalhados nas próximas changes.

| Problema | Requisitos / onde é atendido |
|---|---|
| P1, P2, P3, P4, P5 | Módulo de Notícias (visão §2.3), NF-USA-02 |
| P6 | Papéis Publicador e Administrador, UC05, UC10, UC13 |
| P7, P8 | Módulo de Achados e Perdidos (visão §2.3) |
