# Proposta: relatório de estatísticas de acesso (Sprint 4)

## Por quê

A Sprint 4 pede, na camada business e com o padrão Template Method, a geração de relatórios de estatísticas de acesso dos usuários em mais de um formato (por exemplo, HTML e PDF). O sistema ainda não registrava acessos, então é preciso primeiro definir e registrar o que é um acesso.

## O que muda

- **RF12 — Registrar acessos:** cada operação concluída com sucesso (cadastrar, listar, editar e remover usuário; publicar, consultar o feed, editar e remover publicação) é registrada com data, hora, operação e o usuário envolvido. Os registros usam o mesmo armazenamento das outras entidades (memória ou `acessos.bin`).
- **RF13 — Gerar relatório de acessos** (UC24): o Administrador gera, pelo menu principal, o relatório em HTML ou PDF, com total, período, acessos por operação, por dia e os usuários mais ativos.

## Fora do escopo

- Autenticação (UC03): sem login, o "usuário envolvido" é o usuário afetado pela operação ou o autor da publicação; consultas gerais (listagem e feed) ficam sem usuário.
- Filtro por período e outros formatos além de HTML e PDF.
- Restringir a geração do relatório ao papel Administrador (não há autenticação).

## Impacto

- Código: `entidade` (`RegistroDeAcesso`, `TipoDeOperacao`), `controle` (`GerenciadorDeAcessos`, `EstatisticasDeAcesso`, Template Method `GeradorDeRelatorioDeAcessos` com `RelatorioDeAcessosEmHtml` e `RelatorioDeAcessosEmPdf`, `FormatoDeRelatorio`, `RegistroDeAcessoRepositorio`), `persistencia` (repositórios de acesso e fábricas), `fronteira` (opção 4 do menu principal).
- Arquitetura: [ADR-0005](../../../docs/07-arquitetura/adr/0005-template-method-relatorio-de-acessos.md).
