# ADR-0003: Repository para todas as entidades, criado por Abstract Factory escolhida por Factory Method

- **Status:** aceito
- **Data:** 2026-11-12
- **Requisitos relacionados:** NF-MAN-01, NF-CON-01; Sprint 4 (S4.2)

## Contexto

A Sprint 4 pede o padrão **Repository** para separar a camada **business** (`controle` + `entidade`) da **infra** (`persistencia`) em todas as entidades, e um **Factory Method** ou **Abstract Factory** para criar as entidades ou escolher o tipo de repositório.

Desde a ADR-0001 cada entidade tem uma interface de repositório no pacote `controle` (`UsuarioRepositorio`, `PublicacaoRepositorio`), com uma implementação em memória e outra em arquivo binário. Mas a escolha do armazenamento era feita em `Aplicacao` com um `switch` que instanciava os quatro repositórios concretos. Isso tinha três problemas:

- `Aplicacao` precisava conhecer cada classe concreta de repositório. Cada entidade nova exigiria mais um `new` em cada `case`.
- Nada garantia que as entidades usassem o mesmo armazenamento. Era possível, por engano, montar usuários em arquivo e publicações em memória, e então uma publicação gravada apontaria para um autor que some ao reiniciar.
- Um armazenamento novo, como banco de dados, exigiria editar o `switch` e o texto do menu.

## Decisão

1. **Repository (mantido e documentado como padrão):** os gerentes de `business` só conhecem as interfaces `UsuarioRepositorio` e `PublicacaoRepositorio`. As implementações ficam em `infra`, que é quem depende de `business`, nunca o contrário.
2. **Abstract Factory:** a interface `FabricaDeRepositorios`, declarada em `controle`, tem um método de criação por entidade (`criarRepositorioDeUsuarios`, `criarRepositorioDePublicacoes`). Há uma fábrica concreta por família de armazenamento em `persistencia`: `FabricaDeRepositoriosEmMemoria` e `FabricaDeRepositoriosEmArquivo`. Esta última também concentra os nomes dos arquivos (`usuarios.bin`, `publicacoes.bin`) dentro de uma pasta. A fachada passa a ter `inicializar(FabricaDeRepositorios)` e pede à fábrica os repositórios de todas as entidades.
3. **Factory Method:** o enum `TipoDeArmazenamento` (`MEMORIA`, `ARQUIVO`) declara o método abstrato `criarFabrica(Path pasta)`. Cada constante o sobrescreve e devolve a sua fábrica concreta. `Aplicacao` monta o menu a partir de `TipoDeArmazenamento.values()`, obtém a fábrica do tipo escolhido e inicializa a fachada com ela.

A versão `inicializar(UsuarioRepositorio, PublicacaoRepositorio)` passou a ser visível só dentro do pacote `controle`, onde os testes da camada de negócio a usam. Fora dele, a única entrada é `inicializar(FabricaDeRepositorios)`. Assim, nenhum código consegue montar a fachada com repositórios de famílias diferentes.

Se a fábrica não conseguir ler o armazenamento, ela DEVE lançar `PersistenciaException`. É essa exceção que `Aplicacao` trata para oferecer a escolha de novo.

## Alternativas consideradas

| Alternativa | Por que não |
|---|---|
| Manter o `switch` com `new` em `Aplicacao` | Repete a decisão por entidade e não impede misturar armazenamentos |
| Uma fábrica estática por entidade (`RepositorioDeUsuariosFactory.criar(tipo)`) | Teria um `switch` por entidade e não garantiria que as entidades fossem da mesma família |
| Factory Method para criar as entidades (`Usuario`, `Publicacao`) | As entidades já se validam no próprio construtor e não têm variações de tipo; a variação real do projeto está no armazenamento |
| Fábrica abstrata declarada em `persistencia` | O controle (a fachada) passaria a importar a infra, violando o cenário "Dependências do código" de NF-MAN-01 |

## Consequências

- **Positivas:** `Aplicacao` e a fachada não instanciam nem importam repositórios concretos. As entidades sempre compartilham o mesmo armazenamento. Um armazenamento novo, como banco de dados, se resume a uma nova `FabricaDeRepositorios` e a uma constante em `TipoDeArmazenamento`, sem mudar `business`.
- **Negativas / riscos:** uma entidade nova exige um método a mais na interface da fábrica e em todas as fábricas concretas. É o custo conhecido do Abstract Factory. Se um arquivo estiver corrompido, a falha (`PersistenciaException`) aparece quando a fachada pede o repositório. A fachada então continua não inicializada e `Aplicacao` oferece de novo a escolha (NF-CON-01).
