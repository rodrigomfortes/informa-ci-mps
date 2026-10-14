# Elicitação de requisitos

> Seção 4 do modelo. Para cada técnica: **por que** foi escolhida e **quando, onde e com quem** foi aplicada.

A elicitação aconteceu entre 27 e 29/09/2026 e combinou duas técnicas complementares: **entrevistas semiestruturadas** com alunos, que vivem o problema no dia a dia, e um **brainstorming** da equipe, que transformou os relatos em funcionalidades.

| Técnica | Data e horário | Local | Participantes |
|---|---|---|---|
| Entrevista E1 | 27/09/2026 (domingo), 19h30–20h10 | On-line (Google Meet) | Estudante de graduação, membro de centro acadêmico. Entrevistador: Rodrigo |
| Entrevista E2 | 28/09/2026 (segunda), 10h00–10h30 | Área de convivência do CI | Estudante de graduação ingressante. Entrevistador: Rodrigo |
| Brainstorming | 29/09/2026 (terça), 14h00–16h30 | Sala de estudos do CI | Os 6 integrantes da equipe |

## 4.1 Entrevistas semiestruturadas com alunos

| Campo | Registro |
|---|---|
| Datas e horários | E1: 27/09/2026, 19h30–20h10 · E2: 28/09/2026, 10h00–10h30 |
| Local | E1: on-line, pelo Google Meet · E2: área de convivência do CI, no intervalo entre aulas |
| Participantes | E1: estudante de graduação, membro de um centro acadêmico e responsável pela divulgação dos eventos da entidade · E2: estudante de graduação ingressante · Entrevistador: Rodrigo Monteiro Fortes de Oliveira |
| Registro | Anotações do entrevistador. O áudio foi gravado com autorização, usado só para conferir as anotações e apagado depois da consolidação. Os entrevistados são identificados apenas pelo perfil |

**Por que esta técnica:** a entrevista permite entender, pelo relato de quem vive o problema, como a informação do CI circula hoje e por que se perde. Os entrevistados representam duas situações diferentes: quem **divulga** eventos de uma entidade estudantil (E1) e quem **acabou de chegar** ao CI (E2). O formato semiestruturado permite abordar os mesmos temas com os dois estudantes e, ao mesmo tempo, aprofundar o que surgisse na conversa. As conversas foram on-line ou em local informal, conforme a disponibilidade de cada estudante, para não atrapalhar a rotina de aulas.

**Principais achados:**

- **E1** — divulga os eventos da entidade no Instagram e em grupos de WhatsApp, precisa repetir cada aviso em vários canais e não sabe se ele chega a todos → **P1**, Módulo de Notícias (visão §2.3).
- **E1** — avisos repassados de grupo em grupo perdem o horário e o local, e os interessados perguntam nos próprios grupos; defende publicações com formato padronizado (data, horário, local) → **P5**, Módulo de Notícias.
- **E1** — os grupos já receberam golpes enviados por perfis falsos; defende que só pessoas autorizadas publiquem em nome do CI e que o cadastro exija o e-mail institucional → **P6**, papel Publicador, UC04 e UC05 (criação de conta com confirmação do e-mail institucional).
- **E1** — sugere um responsável capaz de bloquear contas mal usadas e saber quem fez cada alteração → **P6**, papel Administrador, UC10 e UC13.
- **E2** — só entrou nos grupos do curso semanas após o início das aulas e perdeu atividades da recepção aos calouros → **P2**, **P3**.
- **E2** — acompanha mais de 5 grupos, onde os avisos se perdem no meio das conversas e são difíceis de achar depois → **P1**, **P4**.
- **E2** — consulta os grupos quase sempre pelo celular, nos intervalos entre aulas; pediu um único lugar, acessível pelo celular, com os eventos passados e os próximos → **P4**, **NF-USA-02**.

## 4.2 Brainstorming com a equipe

| Campo | Registro |
|---|---|
| Data e horário | 29/09/2026 (terça), 14h00–16h30 |
| Local | Sala de estudos do CI |
| Participantes | Gabriel de Albuquerque Simplício, Gabriel Liszt Ferreira de Oliveira, Gerson Gonçalves de Freitas, Gustavo de Andrade Lacerda, Nicolas Kleiton da Silva Melo e Rodrigo Monteiro Fortes de Oliveira |
| Registro | Ideias registradas em notas adesivas no quadro e consolidadas pela equipe ao fim da sessão |

**Por que esta técnica:** o brainstorming junta os resultados das entrevistas à experiência dos próprios integrantes, que também são alunos do CI, e os transforma em funcionalidades com a participação de todos. É rápido, adequado a equipes pequenas e estimula gerar ideias antes de julgá-las.

**Procedimento:** a sessão teve três etapas.
1. O entrevistador apresentou os resultados de E1 e E2, e cada integrante relatou a própria experiência com os canais atuais do CI.
2. Os integrantes propuseram livremente ideias de funcionalidades em notas adesivas, sem críticas.
3. As ideias foram agrupadas por afinidade e classificadas como essencial, importante, desejável ou fora do escopo, sempre com uma justificativa.

**Principais achados:**

- A experiência dos integrantes revelou um problema que as entrevistas não cobriram: objetos perdidos e encontrados são divulgados por e-mail para toda a comunidade, sem dizer onde retirá-los nem quando foram devolvidos → **P7**, **P8**, Módulo de Achados e Perdidos.
- O sistema foi organizado em dois módulos principais, Notícias e Achados e Perdidos, apoiados pelo gerenciamento de usuários → visão §2.3.
- Foram definidos os papéis Membro, Publicador e Administrador → glossário, visão §2.4.
- Chat entre usuários, publicação automática no Instagram e aplicativo nativo ficaram fora do escopo → escopo negativo da visão §2.3.
- O gerenciamento de usuários foi classificado como essencial e base para os demais módulos → `openspec/specs/usuarios/`.

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
