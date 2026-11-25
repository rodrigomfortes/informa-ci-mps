# ADR-0006: Memento para desfazer a última edição de publicação

- **Status:** aceito
- **Data:** 2026-11-25
- **Requisitos relacionados:** RF14, NF-USA-01; Sprint 5 (S5.2)

## Contexto

A Sprint 5 pede o padrão Memento para desfazer uma ação de atualização, e só a última atualização da entidade nova, a `Publicacao`. A publicação é imutável (cada edição gera uma instância nova com a mesma identidade), e a fachada já executa cada operação como um comando (padrão Command, S5.1). O estado anterior precisa ser guardado sem que a camada de controle dependa dos detalhes internos da publicação.

## Decisão

1. **Originador:** a própria `Publicacao`. `salvarEstado()` cria um `MementoDePublicacao` com título, texto, categoria, data e local, e `restaurar(memento)` devolve a publicação com esse estado, mantendo identidade, autor e data de publicação. Um memento de outra publicação é recusado.
2. **Memento:** `MementoDePublicacao`, imutável, no pacote `entidade`. Os dados guardados só podem ser lidos dentro desse pacote, ou seja, pelo originador; fora dele, o memento só expõe a identidade da publicação a que pertence.
3. **Cuidador:** `HistoricoDeEdicaoDePublicacao`, no pacote `controle`, guarda um único memento: cada edição substitui o anterior, porque só a última atualização precisa ser desfeita. Fica em memória, na fachada, enquanto o sistema está aberto.
4. **Ligação com o Command:** `EditarPublicacaoComando` pede o memento antes de editar e só o entrega ao cuidador se a edição der certo. `DesfazerEdicaoDePublicacaoComando` restaura a publicação, grava e só então descarta o memento, para que uma falha de gravação não perca o "desfazer". `RemoverPublicacaoComando` esquece o memento da publicação removida.
5. **Interface:** a fachada expõe `desfazerUltimaEdicaoDePublicacao()`, e o menu de publicações ganha a opção "5 - Desfazer última edição". O desfazer conta como acesso (RF12).

## Alternativas consideradas

| Alternativa | Por que não |
|---|---|
| Guardar a própria `Publicacao` anterior como "memento" | Funciona porque ela é imutável, mas não é o padrão pedido: o cuidador teria acesso a todo o objeto, e não a um estado opaco |
| Comando com `desfazer()` que reaplica os dados antigos (undo pelo Command) | O controle precisaria ler e guardar os campos da publicação; o Memento deixa essa responsabilidade com a própria entidade |
| Pilha de mementos (vários passos de desfazer) | A tarefa pede só a última atualização |
| Guardar o histórico em arquivo | Desfazer é uma ação imediata, logo depois da edição; guardar entre execuções complicaria a persistência sem necessidade |

## Consequências

- **Positivas:** a `Publicacao` continua sendo a única a conhecer e validar o próprio estado; a fachada e as telas só pedem "desfazer". Desfazer também a edição de usuário seria só um memento de `Usuario` e outro cuidador.
- **Negativas / riscos:** o "desfazer" se perde quando o sistema fecha. Como só a última edição é guardada, editar outra publicação substitui a possibilidade de desfazer a primeira. Sem autenticação, qualquer pessoa no menu de publicações pode desfazer a última edição.
