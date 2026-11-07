# Informa CI

[![Validar especificação](https://github.com/rodrigomfortes/informa-ci-mps/actions/workflows/openspec.yml/badge.svg)](https://github.com/rodrigomfortes/informa-ci-mps/actions/workflows/openspec.yml)

Sistema web que centraliza a comunicação do Centro de Informática (CI) da UFPB: **notícias, eventos, palestras e comunicados** num só lugar e um **mural de achados e perdidos**, no lugar dos grupos de WhatsApp, perfis do Instagram e e-mails para toda a comunidade.

Projeto da disciplina **Métodos e Projeto de Software** (UFPB · Centro de Informática), documentado de forma *spec-driven* com o [OpenSpec](https://github.com/Fission-AI/OpenSpec).

**Prazos e o que entregar em cada sprint: [`docs/00-entregas.md`](docs/00-entregas.md).** Regras de branches, commits, PRs e código: [`CONTRIBUTING.md`](CONTRIBUTING.md).

---

## 1. Equipe

**Sistema:** Informa CI

| Integrante | Matrícula | GitHub |
|---|---|---|
| Gabriel de Albuquerque Simplício | 20230089730 | [@simpgda](https://github.com/simpgda) |
| Gabriel Liszt Ferreira de Oliveira | 20230013132 | [@Gabriel-Liszt](https://github.com/Gabriel-Liszt) |
| Gerson Gonçalves de Freitas | 20240007943 | [@gersongfreitas](https://github.com/gersongfreitas) |
| Gustavo de Andrade Lacerda | 20240007961 | [@LacerdaGustavo](https://github.com/LacerdaGustavo) |
| Nicolas Kleiton da Silva Melo | 20240009044 | [@Nicolas-Kleiton](https://github.com/Nicolas-Kleiton) |
| Rodrigo Monteiro Fortes de Oliveira | 20240097664 | [@rodrigomfortes](https://github.com/rodrigomfortes) |

---

## 2. Como executar

Requisitos: JDK 21 e Maven. O projeto não usa frameworks; a única dependência é o JUnit, só nos testes.

```bash
mvn test                                   # roda a suíte de testes
mvn compile
java -cp target/classes informaci.Aplicacao
```

Ao iniciar, o sistema pergunta onde guardar os usuários: em memória (os dados se perdem ao encerrar) ou no arquivo binário `usuarios.bin`.

Para validar a especificação (Node.js 20.19+):

```bash
npm install -g @fission-ai/openspec@1
openspec validate --all
```

O aviso *"should contain SHALL or MUST"* é esperado: escrevemos em português, com **DEVE**.

---

## 3. Onde está cada seção do modelo de documento de requisitos

| Seção do modelo .docx | Neste repositório |
|---|---|
| Capa (nome do projeto e integrantes) | Este `README.md` (seção 1) |
| Histórico de revisões | Histórico do Git: commits, pull requests e `openspec/changes/archive/` |
| 1. Introdução | [`docs/01-visao.md`](docs/01-visao.md) §1 |
| 2. Descrição geral | [`docs/01-visao.md`](docs/01-visao.md) §2 |
| 3. Glossário | [`docs/02-glossario.md`](docs/02-glossario.md) |
| 4. Elicitação de requisitos | [`docs/03-elicitacao.md`](docs/03-elicitacao.md) |
| 5.1 Requisitos funcionais | [`openspec/specs/usuarios/spec.md`](openspec/specs/usuarios/spec.md) |
| 5.2 Requisitos não funcionais | [`openspec/specs/rnf/<categoria>/spec.md`](openspec/specs/rnf/) |
| 6. Especificação de casos de uso e diagrama | [`docs/04-casos-de-uso/`](docs/04-casos-de-uso/) |
| 7. Diagrama de classes de análise | [`docs/05-analise.md`](docs/05-analise.md) |
| 8. Descrição da interface | [`docs/06-interface/`](docs/06-interface/) |
| 9. Diagramas de arquitetura | [`docs/07-arquitetura/`](docs/07-arquitetura/) + ADRs em `adr/` |
| *(novo)* Pacotes, classes de projeto e padrões | [`docs/08-projeto.md`](docs/08-projeto.md) |

---

## 4. Estrutura

```text
.
├── README.md                     ← capa, equipe e mapa
├── CONTRIBUTING.md               ← branches, commits, PRs, clean code
├── pom.xml
├── src/                          ← código Java (fronteira, controle, entidade, persistencia) e testes
├── docs/
│   ├── 00-entregas.md            ← cronograma e checklist por sprint (do professor)
│   ├── 01-visao.md               ← introdução e descrição geral
│   ├── 02-glossario.md
│   ├── 03-elicitacao.md
│   ├── 04-casos-de-uso/          ← diagrama de casos de uso
│   ├── 05-analise.md             ← classes de análise (ICONIX)
│   ├── 06-interface/             ← telas
│   ├── 07-arquitetura/           ← arquitetura e ADRs
│   ├── 08-projeto.md             ← pacotes, classes de projeto, padrões
│   └── _modelos/                 ← modelos em branco (requisito, caso de uso)
├── openspec/
│   ├── config.yaml               ← contexto e regras do projeto
│   ├── specs/                    ← REQUISITOS EM VIGOR (fonte da verdade)
│   │   ├── usuarios/spec.md
│   │   └── rnf/{seguranca,confiabilidade,manutenibilidade,usabilidade,desempenho,hardware-software}/spec.md
│   └── changes/                  ← mudanças propostas, ainda não incorporadas
│       └── archive/              ← mudanças concluídas (histórico)
└── .github/
    └── workflows/openspec.yml    ← valida as specs a cada push e PR
```

---

## 5. Convenções

**Identificadores** são estáveis: nunca renumere nem reaproveite um ID.

| Item | Formato | Exemplo |
|---|---|---|
| Requisito funcional | `RFnn` | RF02 |
| Requisito não funcional | `NF-CAT-nn` (numeração por categoria) | NF-SEG-01 |
| Caso de uso | `UCnn` | UC01 |
| Tela | `TLnn` | TL01 |
| Decisão de arquitetura | `ADR-nnnn` | ADR-0001 |

Todo requisito tem **prioridade** (Essencial, Importante ou Desejável) e **pelo menos um cenário** GIVEN/WHEN/THEN; num RNF, o `THEN` tem número e unidade. Modelo completo em [`docs/_modelos/requisito.md`](docs/_modelos/requisito.md).

Diagramas em [Mermaid](https://mermaid.js.org/), que o GitHub desenha direto na página; PlantUML (`.puml`) quando a notação UML formal for necessária.

---

## 6. Fluxo de trabalho

Trabalho em `feat/…`, `fix/…` ou `docs/…` a partir de `develop`; PR para `develop`; em cada data de entrega, PR `develop` → `main` + tag (`v0.1.0`, `v0.2.0`…). Detalhes no [`CONTRIBUTING.md`](CONTRIBUTING.md).

A partir da Entrega 2, toda mudança de requisito é uma *change* do OpenSpec:

```bash
openspec new change <nome-da-mudanca>   # proposal, delta das specs, design e tasks
openspec archive <nome-da-mudanca>      # ao concluir, aplica o delta em openspec/specs/
```
