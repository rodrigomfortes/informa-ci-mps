# [UC14] Publicar publicação

Permitir que um Publicador divulgue uma notícia, um evento, uma palestra ou um comunicado no feed do CI, identificado como autor.

| Campo | Valor |
|---|---|
| **Ator** | Publicador |
| **Prioridade** | Essencial |
| **Requisitos** | RF05, NF-CON-01, NF-MAN-01 |
| **Interfaces** | TL01 Menu principal, TL03 Tela de publicações |

**Entradas e pré-condições**
- O autor está cadastrado com papel Publicador (UC01).
- Entradas: e-mail do autor, título, texto, categoria e, para Evento e Palestra, data e horário (`dd/mm/aaaa hh:mm`) e local.

**Saídas e pós-condições**
- Sucesso: a publicação fica armazenada com autor e data de publicação e aparece no topo do feed (UC15).
- Recusa: nenhuma publicação é armazenada, e o sistema informa o motivo.

## Fluxo principal

1. O Publicador escolhe "Publicações" no menu principal (TL01) e, em seguida, "Publicar" (TL03).
2. O sistema pede o e-mail do autor, o título e o texto.
3. O Publicador informa os dados.
4. O sistema pede a categoria: 1 - Notícia, 2 - Evento, 3 - Palestra ou 4 - Comunicado.
5. O Publicador escolhe a categoria.
6. O sistema pede a data e o horário e, em seguida, o local, ambos opcionais na tela.
7. O Publicador informa a data, o horário e o local, ou os deixa em branco.
8. O sistema localiza o usuário pelo e-mail, sem diferenciar maiúsculas de minúsculas, e verifica que ele tem papel Publicador.
9. O sistema valida título (até 100 caracteres), texto (até 2000) e local (até 100) e, para Evento e Palestra, a presença de data e local.
10. O sistema armazena a publicação com a data atual e informa "Publicação criada: <título>".
11. O sistema volta ao menu de publicações.

## Fluxos secundários

**FA1 — Categoria inválida (passo 5)**
1. O Publicador digita uma opção fora de 1 a 4.
2. O sistema informa "Categoria inválida." e volta ao passo 4.

**FA2 — Data em formato inválido (passo 7)**
1. O Publicador informa uma data inexistente ou fora do formato, como "31/02/2026 10:00".
2. O sistema informa "Data e horário inválidos: use o formato dd/mm/aaaa hh:mm, como 20/11/2026 14:00." e pede a data novamente.

**FE1 — Autor não cadastrado (passo 8)**
1. O sistema informa "Não foi possível publicar: Não existe usuário cadastrado com o e-mail <e-mail>."
2. Nada é armazenado; o caso de uso volta ao passo 11.

**FE2 — Autor sem papel Publicador (passo 8)**
1. O sistema informa "Publicação inválida: Apenas usuários com papel Publicador podem publicar."
2. Nada é armazenado; o caso de uso volta ao passo 11.

**FE3 — Dados inválidos (passo 9)**
1. O sistema informa "Publicação inválida: " seguido da regra violada, por exemplo "O título da publicação é obrigatório." ou "Publicações da categoria Evento exigem data e horário."
2. Nada é armazenado; o caso de uso volta ao passo 11.

**FE4 — Falha ao gravar o arquivo (passo 10)**
1. Com armazenamento em arquivo, a gravação falha.
2. O sistema informa "Falha ao armazenar a publicação: Não foi possível gravar as publicações em <arquivo>." e descarta a publicação (NF-CON-01).
3. O caso de uso volta ao passo 11, sem encerrar o sistema.
