package informaci.controle;

/**
 * Fábrica abstrata dos repositórios (padrão Abstract Factory).
 *
 * <p>Cada implementação cria uma <em>família</em> de repositórios do mesmo
 * tipo de armazenamento: todos em memória, ou todos em arquivo binário. Assim
 * é impossível misturar, por engano, usuários em arquivo com publicações em
 * memória, e acrescentar um novo armazenamento (por exemplo, banco de dados)
 * é escrever uma nova fábrica, sem mudar nada no controle.
 *
 * <p>Fica no pacote de controle pelo mesmo motivo dos contratos de
 * repositório (ADR-0001, ADR-0003): o controle declara <em>o que</em> precisa
 * e a persistência decide <em>como</em> criar.
 *
 * <p><strong>Falhas:</strong> se o armazenamento existir mas não puder ser
 * lido, a implementação DEVE sinalizar com
 * {@code informaci.persistencia.PersistenciaException}. É essa a exceção que o
 * ponto de entrada trata para oferecer de novo a escolha do armazenamento
 * (NF-CON-01); qualquer outra exceção é considerada erro de programação. A
 * classe é citada só pelo nome, sem import, para que o controle não dependa
 * da persistência.
 */
public interface FabricaDeRepositorios {

    /** Cria o repositório de usuários desta família de armazenamento. */
    UsuarioRepositorio criarRepositorioDeUsuarios();

    /** Cria o repositório de publicações desta família de armazenamento. */
    PublicacaoRepositorio criarRepositorioDePublicacoes();
}
