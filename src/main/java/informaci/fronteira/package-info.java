/**
 * Camada de <strong>Fronteira</strong> (Boundary) do modelo de análise.
 *
 * <p>Responsável pela interação entre o sistema e os atores externos
 * (Visitante, Membro do CI e Administrador, conforme o diagrama de casos de
 * uso em {@code docs/specs/casos-de-uso/}).
 *
 * <p>Por ora a interface é de console ({@code TelaDeUsuarios}), com as
 * opções de adicionar e listar usuários. Ela é iniciada por
 * {@code informaci.Aplicacao}, que antes pergunta onde os usuários devem ser
 * armazenados.
 *
 * <p><strong>Regra de dependência:</strong> conversa apenas com
 * {@link informaci.controle}, usando os tipos de {@link informaci.entidade}
 * que o controle expõe. Nunca importa classes da persistência. Nenhuma classe
 * desta camada pode conter regra de negócio.
 */
package informaci.fronteira;
