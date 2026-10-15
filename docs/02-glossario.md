# Glossário

> Seção 3 do modelo. Ordem alfabética.
> Use exatamente estes termos nas specs, nos casos de uso e no código (classes, mensagens). Os termos em *itálico* nas definições também estão definidos aqui.

| Termo | Definição | Sinônimos a evitar | Exemplo |
|---|---|---|---|
| **Achados e Perdidos** | Módulo do sistema em que *membros do CI* registram e consultam *itens perdidos* e *itens encontrados* nas dependências do CI. | mural de objetos | — |
| **Administrador** | *Papel* de quem gerencia as contas: cadastra e lista *usuários*, bloqueia contas, altera *papéis* e concede o papel *Publicador*. | admin, gestor, moderador | — |
| **Armazenamento** | Mecanismo em que os *usuários* ficam guardados, escolhido ao iniciar o sistema: **memória** (os dados se perdem ao encerrar) ou **arquivo binário** (os dados permanecem entre execuções). | banco, base de dados | "Arquivo binário (usuarios.bin)" |
| **Comunicado** | *Publicação* de caráter informativo ou administrativo, como avisos da coordenação, prazos e mudanças de sala. | aviso, informe | "Mudança de sala da disciplina X" |
| **E-mail** | Endereço de e-mail do *usuário*, que o identifica: não podem existir dois usuários com o mesmo e-mail, sem diferenciar maiúsculas de minúsculas (RF01). | endereço, contato | ana@academico.ufpb.br |
| **E-mail institucional** | Endereço de e-mail fornecido pela UFPB a alunos e servidores. A confirmação de que o e-mail de um *usuário* é institucional faz parte da criação de conta (UC05). | e-mail pessoal, contato | ana@academico.ufpb.br |
| **Evento** | *Publicação* que divulga uma atividade com data, horário e local, como seminários, minicursos e maratonas de programação. | atividade, agenda | — |
| **Feed** | Lista das *publicações* do Módulo de Notícias, da mais recente para a mais antiga. | linha do tempo, timeline | — |
| **Item devolvido** | *Item encontrado* ou *item perdido* cujo registro foi encerrado porque o objeto voltou ao dono. | item resolvido, baixado | — |
| **Item encontrado** | Objeto achado nas dependências do CI e registrado no *mural* por quem o encontrou, com o local de retirada. | achado | "Garrafa azul, retirar na secretaria" |
| **Item perdido** | Objeto perdido nas dependências do CI e registrado no *mural* pelo dono. | perdido | "Carregador de notebook, lab. 3" |
| **Login** | Identificador do *usuário* para entrar no sistema: obrigatório, com no máximo 12 caracteres e sem números (RF02). | usuário, username, apelido | anasouza |
| **Membro do CI** | *Usuário* com *papel* Membro: aluno ou servidor do CI que acompanha *publicações* e registra itens no *mural*. | aluno (genérico), cliente | — |
| **Mural** | Página do módulo *Achados e Perdidos* que exibe os *itens perdidos* e *itens encontrados* ainda não devolvidos. | quadro, lista de objetos | — |
| **Palestra** | *Evento* em que um ou mais convidados apresentam um tema à comunidade. | talk, apresentação | — |
| **Papel** | Conjunto de funcionalidades a que um *usuário* tem acesso: **Membro**, **Publicador** ou **Administrador**. Nesta versão estão implementados Membro e Administrador; Publicador chega com o Módulo de Notícias. | perfil, tipo de usuário, cargo | Administrador |
| **Publicação** | Qualquer conteúdo do Módulo de Notícias: notícia, *evento*, *palestra* ou *comunicado*. Sempre identifica o *Publicador* que a criou. | post, postagem | — |
| **Publicador** | *Papel* de quem pode criar, editar e remover *publicações*. É concedido pelo *Administrador*. | divulgador, autor | Coordenação do curso |
| **Registro de auditoria** | Anotação de uma ação administrativa sobre uma conta (bloqueio, troca de *papel*, remoção), com data, hora, o *Administrador* responsável e o *usuário* afetado. | log, histórico | — |
| **Senha** | Segredo que, junto com o *login*, permite entrar no sistema. Segue a política padrão do AWS IAM: de 8 a 128 caracteres, ao menos 3 dos 4 tipos de caractere e diferente do login e do e-mail (RF03). Nunca é exibida (NF-SEG-01). | código, PIN | Senha@123 |
| **Usuário** | Pessoa cadastrada no sistema com nome, *e-mail*, *login*, *senha* e um *papel*. | conta, pessoa, cadastro | — |
| **Visitante** | Pessoa que acessa o sistema sem estar autenticada; só consulta *publicações* e pode criar uma conta. | anônimo, convidado | — |

## Siglas

| Sigla | Significado |
|---|---|
| ADR | *Architecture Decision Record*, registro de decisão de arquitetura |
| AWS IAM | *Identity and Access Management* da Amazon Web Services, cuja política de senha é a referência do RF03 |
| CI | Centro de Informática da UFPB |
| LGPD | Lei Geral de Proteção de Dados Pessoais (Lei nº 13.709/2018) |
| RF / NF | Requisito funcional / requisito não funcional |
| UC | Caso de uso (*use case*) |
| UFPB | Universidade Federal da Paraíba |
