# ADR-0002: Fachada única (Facade + Singleton) para a camada de negócio

- **Status:** aceito
- **Data:** 2026-11-06
- **Requisitos relacionados:** NF-MAN-01; Sprint 3 (S3.2)

## Contexto

Na Sprint 3 o sistema ganha uma segunda entidade, `Publicacao`, com o seu próprio gerente (`GerenciadorDePublicacoes`). Sem uma fachada, cada tela precisaria conhecer os gerentes de que depende, e operações que envolvem duas entidades ficariam na fronteira. É o caso de publicar em nome de um usuário: localizar o autor pelo e-mail e depois criar a publicação. A disciplina pede uma fachada única, `FacadeSingletonController`, com um método que retorne a quantidade de entidades cadastradas.

O armazenamento (memória ou arquivo) só é conhecido depois que o usuário o escolhe ao iniciar (NF-MAN-01, ADR-0001). Por isso a fachada não pode criar os repositórios sozinha.

## Decisão

`FacadeSingletonController`, no pacote `controle`, é a única classe de negócio que a fronteira usa:

- **Facade:** delega a `GerenciadorDeUsuarios` e `GerenciadorDePublicacoes` e combina os dois em `publicar(emailDoAutor, dados)` e em `quantidadeDeEntidadesCadastradas()`, que soma usuários e publicações.
- **Singleton:** o construtor é privado. A instância é criada uma única vez por `inicializar(usuarios, publicacoes)`, chamado por `Aplicacao` depois da escolha do armazenamento, e obtida por `getInstancia()`. Inicializar duas vezes, ou pedir a instância antes de inicializar, lança `IllegalStateException`. `encerrar()` descarta a instância, ao fim da execução e entre testes.

Os gerentes continuam existindo e testáveis isoladamente: a fachada não contém regra de negócio, só coordena.

## Alternativas consideradas

| Alternativa | Por que não |
|---|---|
| Singleton preguiçoso (`getInstancia()` cria a instância na primeira chamada) | Teria de escolher o armazenamento por conta própria, desfazendo a escolha feita ao iniciar (ADR-0001) |
| Telas recebendo os gerentes diretamente | A fronteira passaria a conhecer cada gerente, e operações entre entidades ficariam nas telas |
| Fachada sem Singleton, passada por construtor | Atende ao Facade, mas não ao requisito da disciplina de uma instância única |

## Consequências

- **Positivas:** a fronteira depende de uma só classe de negócio; novas entidades entram como novos gerentes atrás da fachada. Prepara a fachada baseada em Command da Sprint 5.
- **Negativas / riscos:** estado global. Os testes precisam chamar `encerrar()` ao terminar, senão a próxima `inicializar` falha. A fachada cresce a cada entidade; se ficar grande demais, pode ser dividida por módulo mantendo uma única entrada.
