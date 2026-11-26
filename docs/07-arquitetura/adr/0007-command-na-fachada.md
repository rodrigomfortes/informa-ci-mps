# ADR-0007: Command nas operações da fachada

- **Status:** aceito
- **Data:** 2026-11-25
- **Requisitos relacionados:** RF01–RF14, NF-MAN-01; Sprint 5 (S5.1)

## Contexto

A Sprint 5 pede que a fachada da camada de negócio passe a usar o padrão Command. Até a Sprint 4, cada método de `FacadeSingletonController` continha a lógica da operação: chamar os gerentes, combinar duas entidades (publicar em nome de um usuário, impedir a remoção de quem tem publicações) e registrar o acesso (RF12). A fachada crescia a cada operação nova.

## Decisão

1. **Command:** a interface genérica `Comando<R>`, com `executar()`, no pacote `controle`. `R` é o tipo do resultado (`Void` quando não há).
2. **Comandos concretos:** uma classe por operação da fachada (adicionar, listar, editar e remover usuário; publicar, listar o feed, editar e remover publicação; gerar o relatório de acessos; as três quantidades; e, com o Memento, desfazer a edição de publicação). Cada comando recebe no construtor os gerentes de que precisa (receptores) e os parâmetros da operação. São `final` e visíveis só no pacote `controle`.
3. **Invoker:** `InvocadorDeComandos` recebe o comando, recusa comando nulo e o executa.
4. **Cliente:** a fachada cria o comando com os parâmetros recebidos e o entrega ao invocador. A interface pública da fachada não mudou, então as telas e os testes existentes continuam iguais.

## Alternativas consideradas

| Alternativa | Por que não |
|---|---|
| Manter a lógica nos métodos da fachada | É o que a sprint pede para mudar; a fachada concentraria todas as regras de combinação |
| Comandos com `desfazer()` (undo pelo Command) | O "desfazer" pedido é só da última edição de publicação e foi feito com Memento ([ADR-0006](0006-memento-desfazer-edicao-de-publicacao.md)); um `desfazer()` em todos os comandos ficaria sem uso |
| Fila ou histórico de comandos no invocador | Não há requisito de agendar, repetir ou registrar comandos; o registro de acessos já é feito pelos próprios comandos |

## Consequências

- **Positivas:** cada operação fica numa classe pequena, testável e fácil de achar; uma operação nova é um comando novo e um método curto na fachada. O comando de edição é o ponto natural para guardar o estado anterior do Memento.
- **Negativas / riscos:** são mais classes (uma por operação) para operações simples, como as quantidades. O invocador hoje só executa o comando; ele passa a valer mais se um dia houver fila, log ou histórico de comandos.
