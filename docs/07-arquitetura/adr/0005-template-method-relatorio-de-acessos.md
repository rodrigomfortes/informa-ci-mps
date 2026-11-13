# ADR-0005: Template Method para o relatório de acessos em HTML e PDF

- **Status:** aceito
- **Data:** 2026-11-12
- **Requisitos relacionados:** RF12, RF13, NF-HSW-01; Sprint 4 (S4.4)

## Contexto

A Sprint 4 pede, na camada business e usando o padrão Template Method, a geração de relatórios de estatísticas de acesso dos usuários em mais de um formato. Os formatos têm o mesmo conteúdo (total, período, acessos por operação, por dia e usuários mais ativos) e só mudam na forma de escrever cada parte. O sistema também não registrava acessos, e o projeto não pode usar bibliotecas externas (NF-HSW-01), o que inclui bibliotecas de PDF.

## Decisão

1. **Acesso = operação concluída com sucesso** (RF12), registrada pela fachada com data, hora, operação e usuário envolvido em `RegistroDeAcesso`. O armazenamento segue o padrão Repository (`RegistroDeAcessoRepositorio`, em memória e em `acessos.bin`), criado pela mesma `FabricaDeRepositorios` das outras entidades (ADR-0003). Uma falha ao guardar o registro vai para o log (ADR-0004) e não impede a operação.
2. **Template Method:** a classe abstrata `GeradorDeRelatorioDeAcessos`, no pacote `controle` (business), tem o método-modelo `final gerar(EstatisticasDeAcesso)`, que fixa as seções e a ordem e prepara o conteúdo. Os passos `iniciarDocumento`, `escreverParagrafo`, `escreverTabela` e `concluirDocumento` são abstratos.
3. **Subclasses concretas:** `RelatorioDeAcessosEmHtml` (HTML com tabelas e caracteres escapados) e `RelatorioDeAcessosEmPdf`, que monta à mão um PDF 1.4 mínimo: catálogo, páginas, fonte padrão Courier e tabela de referências (`xref`). A fonte monoespaçada alinha as tabelas, e o relatório quebra em páginas de 60 linhas.
4. **Escolha do formato:** o enum `FormatoDeRelatorio` cria o gerador de cada formato. A fachada expõe `gerarRelatorioDeAcessos(formato)` e devolve o conteúdo; quem grava o arquivo é a tela.

## Alternativas consideradas

| Alternativa | Por que não |
|---|---|
| Uma classe por formato sem superclasse comum | Cada formato repetiria a montagem das seções, e um poderia esquecer uma seção |
| Strategy (um "escritor" injetado num gerador único) | Também funcionaria, mas a disciplina pede Template Method, e aqui o algoritmo fixo com passos variáveis é exatamente o caso dele |
| Biblioteca de PDF (iText, PDFBox) | Proibida pela restrição de dependências (NF-HSW-01) |
| CSV ou texto no lugar de PDF | Mais simples, mas o exemplo do professor é HTML e PDF |

## Consequências

- **Positivas:** os dois formatos têm sempre as mesmas seções, na mesma ordem. Um formato novo (CSV, por exemplo) é só uma subclasse com quatro métodos.
- **Negativas / riscos:** o PDF à mão usa a codificação WinAnsi da fonte padrão; caracteres fora do Latin-1 viram "?", e linhas com mais de 80 caracteres são cortadas. Sem autenticação, o "usuário" de um acesso é o usuário envolvido na operação, não necessariamente quem a fez. Cada gerador guarda o documento em construção, por isso um novo é criado a cada relatório.
