# Proposta: completar o CRUD de usuários

## Por quê

A Entrega 2 pede o CRUD de duas entidades. Usuário só tinha cadastro (RF01) e listagem (RF04); faltavam a edição e a remoção para completar o CRUD ao lado de Publicação.

## O que muda

- **RF10 — Editar usuário** (UC23, novo): o Administrador altera nome, e-mail, login, senha e papel de um usuário; as publicações dele passam a mostrar os dados novos.
- **RF11 — Remover usuário** (UC12): o Administrador remove um usuário após confirmação, exceto se ele for autor de publicações.

## Fora do escopo

- Registro na auditoria (UC13), que UC12 inclui no diagrama: entra quando a auditoria for implementada.
- Bloqueio de contas (UC10) e edição do próprio perfil pelo Membro (UC07).

## Impacto

- Código: `Usuario.comDados`, `DadosDoUsuario`, `buscarPorId` e `remover` nos repositórios, `editar` e `remover` em `GerenciadorDeUsuarios`, `editarUsuario` e `removerUsuario` na fachada, opções 3 e 4 em `TelaDeUsuarios`.
