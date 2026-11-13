/**
 * Camada de <strong>persistência</strong>.
 *
 * <p>Implementa os contratos de repositório declarados em
 * {@link informaci.controle}. Há duas formas de armazenamento, escolhidas no
 * início da execução:
 * <ul>
 *   <li>{@code UsuarioRepositorioEmMemoria} — coleção em memória RAM; os
 *       dados se perdem ao encerrar a aplicação;</li>
 *   <li>{@code UsuarioRepositorioEmArquivo} — arquivo binário; os dados
 *       sobrevivem entre execuções.</li>
 * </ul>
 *
 * <p>Os repositórios não são instanciados diretamente por quem os usa: cada
 * família de armazenamento tem uma fábrica concreta
 * ({@code FabricaDeRepositoriosEmMemoria}, {@code FabricaDeRepositoriosEmArquivo})
 * que implementa a fábrica abstrata {@code FabricaDeRepositorios} do controle
 * (Abstract Factory), e {@code TipoDeArmazenamento} escolhe qual fábrica criar
 * (Factory Method). Ver ADR-0003.
 *
 * <p>Falhas de leitura ou gravação são convertidas em
 * {@code PersistenciaException}, para que as demais camadas não precisem
 * lidar com exceções verificadas de E/S.
 *
 * <p>Isolar o armazenamento atrás de uma interface é o que permite alternar
 * entre essas implementações, ou acrescentar um banco de dados, sem alterar
 * nenhuma linha das camadas de controle ou entidade.
 *
 * <p><strong>Regra de dependência:</strong> é a única camada que conhece
 * detalhes de armazenamento. Nenhuma outra camada deve importar classes daqui
 * — a exceção é {@code informaci.Aplicacao}, ponto de entrada que escolhe o
 * {@code TipoDeArmazenamento} e entrega a fábrica correspondente ao controle.
 */
package informaci.persistencia;
