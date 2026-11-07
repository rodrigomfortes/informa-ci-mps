# Tarefas: CRUD de publicações

## 1. Entidade

- [x] 1.1 Papel `PUBLICADOR` em `Papel`
- [x] 1.2 `Publicacao`, `Categoria`, `DadosDaPublicacao` e `PublicacaoInvalidaException`, com testes

## 2. Controle

- [x] 2.1 `buscarPorEmail` e `quantidade` em `GerenciadorDeUsuarios`
- [x] 2.2 `PublicacaoRepositorio` e `GerenciadorDePublicacoes`, com testes
- [x] 2.3 `FacadeSingletonController` com `quantidadeDeEntidadesCadastradas`, com testes

## 3. Persistência

- [x] 3.1 `PublicacaoRepositorioEmMemoria` e `PublicacaoRepositorioEmArquivo`, com testes
- [x] 3.2 Gravação atômica e restauração em caso de falha (`ArquivoBinario`)

## 4. Fronteira

- [x] 4.1 `TelaPrincipal` e `TelaDePublicacoes`; telas usando apenas a fachada, com testes

## 5. Documentação

- [x] 5.1 Diagrama de classes de projeto e tabela de padrões (`docs/08-projeto.md`)
- [x] 5.2 ADR-0002 (fachada única)
- [x] 5.3 Diagrama de casos de uso, diagrama de análise e casos de uso detalhados UC14 e UC16
