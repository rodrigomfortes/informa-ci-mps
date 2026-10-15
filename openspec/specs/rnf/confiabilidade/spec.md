# rnf/confiabilidade Specification

## Purpose
Requisitos não funcionais de integridade dos dados e de continuidade da execução quando o armazenamento em arquivo falha.

## Requirements

### Requirement: NF-CON-01 — Falha de armazenamento tratada
O sistema NÃO DEVE encerrar quando o armazenamento em arquivo falhar ao gravar ou ao ler; DEVE informar ao usuário que a operação não foi concluída e permitir que ele continue usando o sistema.

**Prioridade:** Essencial
**Casos de uso:** UC01

#### Scenario: Falha ao gravar
- **GIVEN** o sistema usa armazenamento em arquivo e o arquivo não pode ser gravado
- **WHEN** o Administrador cadastra um usuário válido
- **THEN** o sistema exibe uma mensagem iniciada por "Falha ao armazenar o usuário: Não foi possível gravar os usuários em"
- **AND** o menu principal volta a ser exibido logo em seguida, com 0 encerramentos do sistema

#### Scenario: Arquivo corrompido na inicialização
- **GIVEN** o arquivo de usuários existe, mas não está no formato esperado
- **WHEN** o sistema é iniciado com armazenamento em arquivo
- **THEN** o sistema exibe uma mensagem iniciada por "Não foi possível ler os usuários de" e pergunta de novo onde armazenar, com 0 encerramentos do sistema

### Requirement: NF-CON-02 — Dados preservados entre execuções
Com armazenamento em arquivo, o sistema DEVE preservar 100% dos usuários cadastrados de uma execução para a seguinte, mantendo a ordem de cadastro.

**Prioridade:** Essencial
**Casos de uso:** UC01, UC02

#### Scenario: Reinício do sistema
- **GIVEN** o sistema foi iniciado com armazenamento em arquivo e 3 usuários foram cadastrados
- **WHEN** o sistema é encerrado e iniciado de novo com armazenamento em arquivo
- **THEN** a listagem exibe os mesmos 3 usuários, na mesma ordem
