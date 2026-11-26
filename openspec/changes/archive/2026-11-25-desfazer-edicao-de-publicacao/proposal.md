# Proposta: desfazer a última edição de publicação (Sprint 5)

## Por quê

A Sprint 5 pede o padrão Memento para desfazer uma ação de atualização, e só a última atualização da entidade nova (a Publicação, criada na Sprint 3). Quem edita um aviso por engano, por exemplo trocando a sala ou o horário, precisa voltar ao que estava antes sem redigitar tudo.

## O que muda

- **RF14 — Desfazer a última edição de publicação** (UC25): o Publicador escolhe "5 - Desfazer última edição" no menu de publicações, e a publicação editada por último volta ao estado de antes da edição.
- **RF12:** desfazer uma edição passa a contar como acesso ("Desfazer edição de publicação").

## Fora do escopo

- Desfazer vários passos, refazer (redo) e desfazer cadastros ou remoções: a tarefa pede só a última atualização.
- Guardar o "desfazer" entre execuções: a última edição fica em memória enquanto o sistema está aberto.
- Desfazer a edição de usuário.

## Impacto

- Código: `entidade` (`MementoDePublicacao`; `Publicacao.salvarEstado` e `restaurar`), `controle` (`HistoricoDeEdicaoDePublicacao`, `DesfazerEdicaoDePublicacaoComando`, `NadaParaDesfazerException`; `EditarPublicacaoComando` e `RemoverPublicacaoComando` usam o histórico; `desfazerUltimaEdicaoDePublicacao` na fachada), `fronteira` (opção 5 do menu de publicações).
- Arquitetura: [ADR-0006](../../../../docs/07-arquitetura/adr/0006-memento-desfazer-edicao-de-publicacao.md).
