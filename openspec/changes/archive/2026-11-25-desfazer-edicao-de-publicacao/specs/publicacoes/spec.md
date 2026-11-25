## ADDED Requirements

### Requirement: RF14 — Desfazer a última edição de publicação
O sistema DEVE permitir desfazer a última edição de publicação concluída com sucesso, devolvendo a publicação editada por último ao título, texto, categoria, data e local de antes da edição, com o mesmo autor, a mesma data de publicação e a mesma posição no feed. Só a última edição DEVE poder ser desfeita, e uma única vez. Uma edição recusada NÃO DEVE substituir a última edição válida. Se a publicação editada for removida, NÃO DEVE haver edição para desfazer.

**Prioridade:** Importante
**Casos de uso:** UC25

#### Scenario: Desfazer a edição
- **GIVEN** o comunicado "Mudança de sala" foi editado para "Mudança de sala da disciplina"
- **WHEN** o Publicador escolhe "5 - Desfazer última edição"
- **THEN** o sistema informa "Edição desfeita: Mudança de sala" e o feed volta a mostrar o título "Mudança de sala"

#### Scenario: Só a última edição
- **GIVEN** um comunicado foi editado de "Versão 1" para "Versão 2" e depois para "Versão 3"
- **WHEN** o Publicador desfaz a última edição
- **THEN** o título volta a ser "Versão 2"
- **AND** um novo "desfazer" informa "Não há edição de publicação para desfazer."

#### Scenario: Nada para desfazer
- **GIVEN** nenhuma publicação foi editada desde que o sistema abriu
- **WHEN** o Publicador escolhe "5 - Desfazer última edição"
- **THEN** o sistema informa "Não há edição de publicação para desfazer." e nada é alterado

#### Scenario: Publicação removida
- **GIVEN** um comunicado foi editado e depois removido
- **WHEN** o Publicador escolhe "5 - Desfazer última edição"
- **THEN** o sistema informa "Não há edição de publicação para desfazer."
