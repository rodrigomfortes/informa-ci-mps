# ADR 0003 — Padrão Adapter para isolamento da biblioteca de log

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
| **Target** | `informaci.log.ServicoDeLog`  | Interface que a camada de controle conhece e usa           |
| **Adapter**| `informaci.log.JulLogAdapter` | Adapta `java.util.logging.Logger` para `ServicoDeLog`      |
| **Adaptee**| `java.util.logging.Logger`    | Lib preexistente com interface incompatível                 |
| **Nulo**   | `informaci.log.NullLog`       | Implementação vazia; usada nos testes e como default seguro|
| **Cliente**| `GerenciadorDeUsuarios`,<br>`GerenciadorDePublicacoes` | Dependem apenas de `ServicoDeLog` |

### Mapeamento de níveis

| `ServicoDeLog` | `java.util.logging.Level` |
|----------------|---------------------------|
| `info`         | `INFO`                    |
| `aviso`        | `WARNING`                 |
| `erro`         | `SEVERE`                  |

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
