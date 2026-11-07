# [UC16] Editar publicação

Permitir que um Publicador corrija ou atualize uma publicação já divulgada, como uma mudança de sala, data ou horário, sem perder o autor nem a posição no feed.

| Campo | Valor |
|---|---|
| **Ator** | Publicador |
| **Prioridade** | Essencial |
| **Requisitos** | RF07, NF-CON-01 |
| **Interfaces** | TL01 Menu principal, TL03 Tela de publicações |

**Entradas e pré-condições**
- Existe ao menos uma publicação cadastrada (UC14).
- Entradas: o número da publicação no feed e os novos valores de título, texto, categoria, data e horário e local.

**Saídas e pós-condições**
- Sucesso: a publicação passa a ter os novos dados e mantém identidade, autor, data de publicação e posição no feed.
- Recusa ou cancelamento: a publicação continua exatamente como estava.

## Fluxo principal

1. O Publicador escolhe "Publicações" no menu principal (TL01) e, em seguida, "Editar publicação" (TL03).
2. O sistema lista as publicações numeradas, da mais recente para a mais antiga (inclui UC15).
3. O Publicador informa o número da publicação.
4. O sistema informa "Deixe em branco para manter o valor atual." e pede cada campo, mostrando entre colchetes o valor atual do título, da categoria, da data e do local.
5. O Publicador informa os campos que quer alterar e deixa os demais em branco.
6. O sistema valida os novos dados com as mesmas regras de UC14 (passo 9).
7. O sistema substitui a publicação e informa "Publicação editada: <título>".
8. O sistema volta ao menu de publicações.

## Fluxos secundários

**FA1 — Cancelar a escolha (passo 3)**
1. O Publicador informa 0.
2. O caso de uso termina sem alterações (passo 8).

**FA2 — Apagar a data ou o local (passo 5)**
1. O Publicador digita "-" na data ou no local.
2. O campo fica vazio; o fluxo segue no passo 6.

**FA3 — Categoria ou data em formato inválido (passo 5)**
1. O sistema informa "Categoria inválida." ou "Data e horário inválidos: use o formato dd/mm/aaaa hh:mm, como 20/11/2026 14:00." e pede o campo novamente.

**FE1 — Nenhuma publicação cadastrada (passo 2)**
1. O sistema informa "Nenhuma publicação cadastrada." e o caso de uso termina (passo 8).

**FE2 — Número inválido (passo 3)**
1. O Publicador informa um número fora da lista ou um texto.
2. O sistema informa "Número inválido: <valor digitado>" e o caso de uso termina sem alterações (passo 8).

**FE3 — Dados inválidos (passo 6)**
1. O sistema informa "Publicação inválida: " seguido da regra violada, por exemplo "Publicações da categoria Palestra exigem data e horário." ao apagar a data de uma palestra.
2. A publicação não é alterada; o caso de uso volta ao passo 8.

**FE4 — Publicação removida durante a edição (passo 7)**
1. A publicação deixou de existir entre a escolha e a gravação.
2. O sistema informa "Não foi possível editar: Publicação não encontrada: <identidade>." e o caso de uso volta ao passo 8.

**FE5 — Falha ao gravar o arquivo (passo 7)**
1. Com armazenamento em arquivo, a gravação falha.
2. O sistema informa "Falha ao armazenar a publicação: Não foi possível gravar as publicações em <arquivo>." e mantém a versão anterior (NF-CON-01).
3. O caso de uso volta ao passo 8, sem encerrar o sistema.
