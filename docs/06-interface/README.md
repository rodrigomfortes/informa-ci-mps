# Descrição da interface com o usuário

> Seção 8 do modelo. Os rascunhos (wireframes) das telas dos 3 casos de uso detalhados entram na **Entrega 2 (30/10/2026)**, conforme o [cronograma](../00-entregas.md). Cada tela terá um ID (TLnn) citado nos casos de uso.

## Interface atual (Sprints 1 e 2)

Nesta fase a interface é de console (restrição R5 da [visão](../01-visao.md)). Ao iniciar, o sistema pergunta onde armazenar os usuários e, em seguida, mostra o menu de usuários:

```text
Onde os usuários devem ser armazenados?
1 - Memória RAM (os dados se perdem ao encerrar)
2 - Arquivo binário (.../usuarios.bin)
Opção: 2

1 - Adicionar usuário
2 - Listar usuários
0 - Sair
Opção: 1
Nome: Ana Souza
E-mail: ana@academico.ufpb.br
Login: ana123
Senha: Senha@123
Papel (1 - Membro, 2 - Administrador): 1
Login inválido: O login não pode conter números.
```

> No console, a senha aparece enquanto é digitada; ocultá-la fica para a interface web. Depois de digitada, ela não é repetida em nenhuma saída do sistema (NF-SEG-01).
