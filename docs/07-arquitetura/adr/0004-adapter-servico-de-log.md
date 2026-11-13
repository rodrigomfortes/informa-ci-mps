# ADR-0004: Padrão Adapter para isolamento da biblioteca de log

| Campo    | Valor                                              |
|----------|----------------------------------------------------|
| Status   | Aceito                                             |
| Data     | 2026-11-12                                         |
| Autores  | Equipe Informa CI                                  |

## Contexto

O sistema precisava registrar eventos de negócio (criação, edição e remoção de
usuários e publicações) para fins de auditoria e diagnóstico. A abordagem
ingênua seria usar `System.out.println` ou referenciar diretamente
`java.util.logging.Logger` nos gerenciadores. Isso acoplaria a camada de
controle a uma biblioteca concreta: qualquer troca de lib (JUL → SLF4J,
Log4j etc.) exigiria alterações em múltiplas classes de negócio.

## Decisão

Aplicamos o padrão **Adapter** (estrutural, GoF) para interpor uma interface
própria entre a camada de controle e a biblioteca de log concreta.

```
   <<interface>>
   ServicoDeLog          ◄──── JulLogAdapter ────► java.util.logging.Logger
   (Target)                      (Adapter)               (Adaptee)

   NullLog ──────────────────────────────────────► (descarta tudo)
   (implementação nula)
```

### Participantes

| Papel      | Classe                        | Responsabilidade                                           |
|------------|-------------------------------|------------------------------------------------------------|
| **Target** | `informaci.controle.ServicoDeLog` | Interface que a camada de controle conhece e usa       |
| **Adapter**| `informaci.log.JulLogAdapter` | Adapta `java.util.logging.Logger` para `ServicoDeLog`      |
| **Adaptee**| `java.util.logging.Logger`    | Lib preexistente com interface incompatível                 |
| **Nulo**   | `informaci.controle.NullLog`  | Implementação vazia; usada nos testes e como default seguro|
| **Cliente**| `GerenciadorDeUsuarios`,<br>`GerenciadorDePublicacoes` | Dependem apenas de `ServicoDeLog` |

### Mapeamento de níveis

| `ServicoDeLog` | `java.util.logging.Level` |
|----------------|---------------------------|
| `info`         | `INFO`                    |
| `aviso`        | `WARNING`                 |
| `erro`         | `SEVERE`                  |

### Onde fica cada classe

Como os contratos de repositório (ADR-0001), o **Target** (`ServicoDeLog`) e a
implementação nula (`NullLog`) ficam no pacote `controle` (camada business):
os gerentes não importam nada fora de `controle` e `entidade`. O **Adapter**
(`JulLogAdapter`) fica em `informaci.log`, que faz parte da camada **infra**,
pois é ele que conhece a biblioteca concreta.

### Saída do log

Por padrão o JUL escreve no console, o que misturaria o log com os menus da
interface. `JulLogAdapter.gravandoEm(nome, arquivo)` desliga essa saída e grava
os registros só no arquivo `informaci.log` (ignorado pelo Git). Se o arquivo não
puder ser aberto, `Aplicacao` avisa e segue com `NullLog`, sem deixar de
funcionar. As mensagens registram login e identidade, mas não o e-mail, para
não espalhar dados pessoais (NF-SEG-02).

### Ponto de composição

A `Aplicacao` (ponto de entrada) é o único lugar que conhece `JulLogAdapter`.
Ela cria o adapter e o injeta na `FacadeSingletonController`, que o repassa
aos gerenciadores. Os gerenciadores recebem `ServicoDeLog` pelo construtor
(injeção de dependência) e não sabem qual implementação recebem.

## Consequências

**Positivas:**
- Trocar de biblioteca de log exige apenas um novo adapter; os gerenciadores
  não mudam.
- Os testes usam `NullLog` e não produzem saída de log, evitando poluição.
- A interface é pequena e expressa o vocabulário do domínio (`aviso`, `erro`)
  em vez de expor os níveis arbitrários de cada lib.

**Negativas / Restrições:**
- Um nível a mais de indireção para operações de log.
- Funcionalidades avançadas da lib (MDC, filtros, appenders etc.) precisariam
  ser expostas por métodos adicionais na interface ou por um adapter mais rico.
