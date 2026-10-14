# Visão do sistema — Informa CI

> Equivale às seções 1 (Introdução) e 2 (Descrição geral) do modelo de documento de requisitos.

## 1. Introdução

### 1.1 Propósito deste repositório

Este repositório especifica o **Informa CI**, sistema web que centraliza a comunicação do Centro de Informática (CI) da UFPB: notícias, eventos, palestras e comunicados, além de um mural de achados e perdidos. Ele é destinado à equipe de desenvolvimento, ao professor da disciplina de Métodos e Projeto de Software e aos representantes da comunidade do CI, que podem verificar se suas necessidades foram compreendidas.

A documentação é viva: cada mudança de requisito passa por uma *change* do OpenSpec e fica registrada no histórico do Git.

### 1.2 Organização

| Onde | O quê |
|---|---|
| `docs/01-visao.md` | Este arquivo: motivação, problemas, escopo, usuários, restrições |
| `docs/02-glossario.md` | Termos do domínio |
| `docs/03-elicitacao.md` | Técnicas de elicitação usadas e seus registros |
| `openspec/specs/` | **Requisitos funcionais e não funcionais** (fonte da verdade) |
| `docs/04-casos-de-uso/` | Diagrama de casos de uso e os casos de uso detalhados |
| `docs/05-analise.md` | Diagrama de classes de análise |
| `docs/06-interface/` | Rascunhos de tela |
| `docs/07-arquitetura/` | Arquitetura e decisões (ADRs) |
| `docs/08-projeto.md` | Pacotes, diagrama de classes de projeto e padrões |
| `openspec/changes/` | Mudanças propostas nos requisitos, ainda não incorporadas |

### 1.3 Documentos relacionados

1. [Glossário](02-glossario.md).
2. [Registro da elicitação](03-elicitacao.md): entrevistas e brainstorming realizados entre 27 e 29/09/2026.
3. Definir uma política de senhas de conta para usuários do IAM; Amazon Web Services; <https://docs.aws.amazon.com/pt_br/IAM/latest/UserGuide/id_credentials_passwords_account-policy.html>. Base das regras de senha (RF03).
4. Lei Geral de Proteção de Dados Pessoais (LGPD); Lei nº 13.709, de 14/08/2018; <https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709.htm>.

## 2. Descrição geral

### 2.1 Motivação

O CI tem uma rotina intensa de palestras, seminários, minicursos, editais, avisos da coordenação e eventos organizados por professores, laboratórios e entidades estudantis. Hoje essas informações circulam espalhadas por grupos de WhatsApp e perfis do Instagram, e não existe um canal único onde sejam publicadas e possam ser consultadas depois.

Com os objetos esquecidos ou encontrados no CI acontece algo parecido: como não há um lugar próprio para registrá-los, cada objeto gera um e-mail para toda a comunidade.

O Informa CI substitui esses meios dispersos por uma plataforma única, de acesso simples, em que quem publica é identificado.

### 2.2 Problemas identificados

| # | Problema | Quem é afetado | Evidência |
|---|---|---|---|
| P1 | Avisos espalhados em vários grupos e perfis, sem um canal que reúna todos | Alunos; quem divulga eventos | E1: repete cada aviso em vários canais. E2: acompanha mais de 5 grupos |
| P2 | Avisos perdidos no meio das conversas ou vistos tarde demais | Alunos | E2: perdeu atividades da recepção aos calouros por não ter recebido a divulgação |
| P3 | Só recebe a informação quem está nos grupos certos | Alunos ingressantes | E2: só entrou nos grupos do curso semanas após o início das aulas |
| P4 | Não há histórico: avisos antigos e eventos futuros são difíceis de encontrar | Alunos | E2: nos grupos, os avisos se perdem no meio das conversas e são difíceis de encontrar depois |
| P5 | Avisos repassados de grupo em grupo perdem data, horário e local | Alunos; quem divulga eventos | E1: interessados precisam perguntar o horário e o local nos grupos |
| P6 | Não dá para saber se quem publicou estava autorizado; já circularam golpes por perfis falsos | Toda a comunidade | E1 |
| P7 | Cada objeto perdido ou encontrado vira um e-mail para toda a comunidade | Toda a comunidade | Brainstorming (experiência dos integrantes) |
| P8 | Não se sabe onde retirar um objeto encontrado nem se ele já foi devolvido | Donos de objetos perdidos | Brainstorming |

Os registros completos de E1, E2 e do brainstorming estão em [`03-elicitacao.md`](03-elicitacao.md).

### 2.3 Visão geral do sistema

**O sistema fará:**
- **Gerenciamento de usuários:** cadastro com validação de login e senha, listagem, autenticação e administração das contas (bloqueio, troca de papel, remoção) com registro de auditoria. *É o núcleo especificado em detalhe nesta entrega* (`openspec/specs/usuarios/`).
- **Módulo de Notícias:** publicação de notícias, eventos, palestras e comunicados por usuários com papel Publicador, com data, horário e local padronizados; feed único; busca por categoria e palavra-chave, incluindo publicações antigas.
- **Módulo de Achados e Perdidos:** mural de itens perdidos e encontrados, com local de retirada e indicação de item devolvido.

**O sistema NÃO fará (escopo negativo):**

| Funcionalidade | Motivo |
|---|---|
| Substituir o SIGAA ou os e-mails oficiais da coordenação | O Informa CI complementa esses canais com a informação do dia a dia do CI |
| Guardar fisicamente os objetos ou intermediar a devolução | O sistema só registra e divulga; a devolução é combinada entre as pessoas |
| Troca de mensagens (chat) entre usuários | Fora do objetivo, que é divulgar informação, não conversar |
| Publicar automaticamente no WhatsApp ou no Instagram | Exigiria integrar serviços de terceiros; descartado no brainstorming |
| Aplicativo móvel nativo | O acesso pelo celular será pelo navegador |

**Integração com outros sistemas:** o Informa CI é autocontido, com uma exceção: usará um serviço externo de e-mail para confirmar o e-mail institucional no cadastro e enviar o link de recuperação de senha. Nenhum sistema da UFPB é integrado.

### 2.4 Usuários do sistema

Todos fazem parte da comunidade do CI (alunos, professores e técnicos administrativos) ou são visitantes interessados nas suas atividades. Acessam o sistema de forma rápida e ocasional, quase sempre pelo celular e entre aulas (E2), e esperam encontrar a informação sem treinamento.

| Usuário | Características | Principais dificuldades hoje |
|---|---|---|
| **Visitante** | Pessoa não autenticada; só consulta publicações e pode criar uma conta | Não tem acesso aos grupos onde os avisos circulam |
| **Membro do CI** | Aluno ou servidor com conta; acompanha publicações e registra itens no mural | Avisos dispersos e perdidos (P1–P5); objetos perdidos difíceis de recuperar (P7, P8) |
| **Publicador** | Membro autorizado a publicar em nome de coordenação, laboratório ou entidade estudantil | Precisa repetir cada aviso em vários canais (P1, P5) |
| **Administrador** | Membro que gerencia as contas e concede o papel Publicador | Não há como bloquear contas mal usadas nem saber quem fez cada alteração (P6) |

### 2.5 Suposições e restrições gerais

- **R1** — Desenvolvido em Java 21, sem frameworks de aplicação; a única biblioteca externa é o JUnit, usada só nos testes (NF-HSW-01).
- **R2** — Login e senha seguem regras definidas pela disciplina (RF02) e a política de senha padrão do AWS IAM (RF03); a senha nunca é exibida (NF-SEG-01).
- **R3** — Os usuários podem ser armazenados em memória ou em arquivo binário, escolhido ao iniciar o sistema (NF-MAN-01).
- **R4** — Dados pessoais limitados a nome, e-mail, login e papel (LGPD, NF-SEG-02).
- **R5** — Nesta fase a interface é de console; a interface web vem em sprints posteriores.
- **S1** — Supõe-se que todo membro do CI tem um e-mail institucional ativo.
- **S2** — Supõe-se que o CI designará ao menos um Administrador para gerenciar as contas.
