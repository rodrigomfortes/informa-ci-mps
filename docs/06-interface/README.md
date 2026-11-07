# Descrição da interface com o usuário

> Seção 8 do modelo. Cada tela tem um ID (TLnn), citado nos casos de uso detalhados.

## Telas

| ID | Tela | Classe | Casos de uso |
|---|---|---|---|
| TL01 | Menu principal (escolha do armazenamento, módulos e quantidade de entidades) | `TelaPrincipal` | UC01, UC14, UC16, UC19 |
| TL02 | Tela de usuários (adicionar, listar, editar e remover) | `TelaDeUsuarios` | UC01, UC02, UC12, UC23 |
| TL03 | Tela de publicações (publicar, feed, editar, remover) | `TelaDePublicacoes` | UC14, UC15, UC16, UC17 |

## Interface atual (Sprints 1 a 3)

Nesta fase a interface é de console (restrição R5 da [visão](../01-visao.md)). Ao iniciar, o sistema pergunta onde armazenar os dados e, em seguida, mostra o menu principal, que leva às telas de usuários e de publicações:

```text
Onde os dados devem ser armazenados?
1 - Memória RAM (os dados se perdem ao encerrar)
2 - Arquivo binário (.../usuarios.bin e publicacoes.bin)
Opção: 2

Informa CI
1 - Usuários
2 - Publicações
3 - Quantidade de entidades cadastradas
0 - Sair
Opção: 1

Usuários
1 - Adicionar usuário
2 - Listar usuários
0 - Voltar
Opção: 1
Nome: Ana Souza
E-mail: ana@academico.ufpb.br
Login: ana123
Senha: Senha@123
Papel (1 - Membro, 2 - Administrador, 3 - Publicador): 1
Login inválido: O login não pode conter números.
```

Tela de publicações (CRUD de `Publicacao`):

```text
Publicações
1 - Publicar
2 - Ver feed
3 - Editar publicação
4 - Remover publicação
0 - Voltar
Opção: 1
E-mail do autor: coordenacao@ci.ufpb.br
Título: Introdução a Rust
Texto: Palestra aberta a todos.
Categoria (1 - Notícia, 2 - Evento, 3 - Palestra, 4 - Comunicado): 3
Data e horário (dd/mm/aaaa hh:mm; em branco se não houver): 20/11/2026 14:00
Local (em branco se não houver): Auditório do CI
Publicação criada: Introdução a Rust

Opção: 2

1. [Palestra] Introdução a Rust
   Por coordenacao em 06/11/2026 21:08
   Quando: 20/11/2026 14:00
   Onde: Auditório do CI
   Palestra aberta a todos.
```

Na edição, cada campo deixado em branco mantém o valor atual, e `-` apaga a data ou o local. A remoção pede confirmação (`s/n`). As publicações são escolhidas pelo número com que aparecem na lista.

> No console, a senha aparece enquanto é digitada; ocultá-la fica para a interface web. Depois de digitada, ela não é repetida em nenhuma saída do sistema (NF-SEG-01).
