# [UC01] Cadastrar usuário

Permitir que o Administrador inclua um novo usuário no sistema, com dados válidos e um papel definido.

| Campo | Valor |
|---|---|
| **Ator** | Administrador |
| **Prioridade** | Essencial |
| **Requisitos** | RF01, RF02, RF03, NF-SEG-01, NF-SEG-02, NF-CON-01, NF-USA-01 |
| **Interfaces** | TL01 Menu principal, TL02 Tela de usuários |

**Entradas e pré-condições**
- O sistema foi iniciado e o armazenamento (memória ou arquivo binário) foi escolhido.
- Entradas: nome, e-mail, login, senha e papel (Membro, Administrador ou Publicador).

**Saídas e pós-condições**
- Sucesso: o usuário fica armazenado, com identidade própria e e-mail em minúsculas, e passa a aparecer na listagem (UC02).
- Recusa: nenhum usuário é armazenado, e o sistema informa o campo e a regra violada.

## Fluxo principal

1. O Administrador escolhe "Usuários" no menu principal (TL01) e, em seguida, "Adicionar usuário" (TL02).
2. O sistema pede nome, e-mail, login e senha.
3. O Administrador informa os dados.
4. O sistema pede o papel: 1 - Membro, 2 - Administrador ou 3 - Publicador.
5. O Administrador escolhe o papel.
6. O sistema valida o nome, o e-mail (formato), o login (RF02) e a senha (RF03).
7. O sistema verifica que nenhum usuário tem o mesmo e-mail, sem diferenciar maiúsculas de minúsculas.
8. O sistema armazena o usuário e informa "Usuário adicionado: <login>".
9. O sistema volta ao menu de usuários.

## Fluxos secundários

**FA1 — Papel inválido (passo 5)**
1. O Administrador digita uma opção diferente de 1, 2 ou 3.
2. O sistema informa "Papel inválido." e volta ao passo 4.

**FE1 — Nome em branco (passo 6)**
1. O sistema informa "Não foi possível adicionar: O nome do usuário é obrigatório."
2. Nada é armazenado; o caso de uso volta ao passo 9.

**FE2 — E-mail em formato inválido (passo 6)**
1. O sistema informa "Não foi possível adicionar: E-mail em formato inválido: <e-mail digitado>".
2. Nada é armazenado; o caso de uso volta ao passo 9.

**FE3 — Login inválido (passo 6)**
1. O sistema informa "Login inválido: " seguido da regra violada: "O login é obrigatório.", "O login deve ter no máximo 12 caracteres." ou "O login não pode conter números."
2. Nada é armazenado; o caso de uso volta ao passo 9.

**FE4 — Senha inválida (passo 6)**
1. O sistema informa "Senha inválida: " seguido da regra violada (tamanho de 8 a 128, ao menos 3 tipos de caractere, ou igual ao login ou ao e-mail), sem repetir a senha digitada (NF-SEG-01).
2. Nada é armazenado; o caso de uso volta ao passo 9.

**FE5 — E-mail já cadastrado (passo 7)**
1. O sistema informa "Não foi possível adicionar: Já existe um usuário cadastrado com o e-mail <e-mail>."
2. Nada é armazenado; o caso de uso volta ao passo 9.

**FE6 — Falha ao gravar o arquivo (passo 8)**
1. Com armazenamento em arquivo, a gravação falha.
2. O sistema informa "Falha ao armazenar o usuário: Não foi possível gravar os usuários em <arquivo>." e descarta o cadastro (NF-CON-01).
3. O caso de uso volta ao passo 9, sem encerrar o sistema.
