# Proposta: CRUD de publicações (Sprint 3)

## Por quê

O Módulo de Notícias é o núcleo da visão do Informa CI (P1 a P5): reunir num só lugar as notícias, os eventos, as palestras e os comunicados do CI. A Sprint 3 pede o CRUD de uma entidade relacionada a Usuário e uma fachada única (Facade + Singleton) com a quantidade de entidades cadastradas.

## O que muda

- **Nova capacidade `publicacoes`:** publicar (RF05), consultar o feed (RF06), editar (RF07) e remover (RF08) publicações, e exibir a quantidade de entidades cadastradas (RF09).
- **`usuarios` / RF01 modificado:** o cadastro passa a oferecer o papel **Publicador**, exigido para publicar.
- **Projeto:** a fronteira passa a falar só com `FacadeSingletonController` (ADR-0002); publicações usam o mesmo armazenamento escolhido para os usuários (memória ou `publicacoes.bin`).

## Fora do escopo

- Autenticação (UC03): nesta versão, quem publica informa o e-mail do autor, e não há restrição de quem edita ou remove uma publicação.
- Busca por categoria e palavra-chave (UC18), Achados e Perdidos (UC20 a UC22) e a interface web.
- Concessão do papel Publicador por um Administrador (UC11): por ora o papel é escolhido no cadastro.

## Impacto

- Casos de uso: UC14 a UC17 e UC19 (novos), UC01 (papel Publicador).
- Código: `entidade` (Publicacao, Categoria, DadosDaPublicacao, Papel.PUBLICADOR), `controle` (GerenciadorDePublicacoes, FacadeSingletonController), `persistencia` (repositórios de publicações), `fronteira` (TelaPrincipal, TelaDePublicacoes).
