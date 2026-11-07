# Design: CRUD de publicações

## Decisões

1. **`Publicacao` imutável e validada na construção**, como `Usuario`. Editar gera uma nova instância com a mesma identidade (`comDados`), e o repositório decide quando a alteração passa a valer. Isso evita que uma falha de gravação deixe a cópia em memória diferente do arquivo, e prepara o Memento da Sprint 5 (desfazer a última atualização).
2. **Autor = `Usuario` com papel Publicador.** A entidade recusa outro papel, de modo que a regra vale para qualquer caminho que crie publicações.
3. **Evento e Palestra exigem data, horário e local** (`Categoria.exigeDataELocal`); Notícia e Comunicado os aceitam como opcionais.
4. **Fachada única `FacadeSingletonController`** entre a fronteira e os gerentes, inicializada uma vez com os repositórios escolhidos — ver [ADR-0002](../../../docs/07-arquitetura/adr/0002-fachada-unica.md).
5. **Mesmo armazenamento para as duas entidades.** `PublicacaoRepositorioEmArquivo` reaproveita `ArquivoBinario` (gravação atômica e restauração em caso de falha), do mesmo jeito que o repositório de usuários (ADR-0001).
6. **Feed na ordem inversa de cadastro**; a edição mantém a posição da publicação.

## Limites adotados

| Campo | Regra |
|---|---|
| Título | obrigatório, até 100 caracteres |
| Texto | obrigatório, até 2000 caracteres |
| Local | opcional (obrigatório para Evento e Palestra), até 100 caracteres |
| Data e horário | formato `dd/mm/aaaa hh:mm` na tela; obrigatório para Evento e Palestra |
