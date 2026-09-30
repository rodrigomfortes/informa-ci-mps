/**
 * Camada de <strong>Fronteira</strong> (Boundary) do modelo de análise.
 *
 * <p>Responsável pela interação entre o sistema e os atores externos
 * (Visitante, Membro do CI e Administrador, conforme o diagrama de casos de
 * uso em {@code docs/specs/casos-de-uso/}).
 *
 * <p><strong>Estado nesta sprint:</strong> a camada existe na estrutura para
 * manter o código coerente com o modelo de análise, mas ainda não possui
 * classes. Por decisão da equipe, as funcionalidades de adição e listagem de
 * usuários são exercitadas diretamente pelos testes automatizados. A interface
 * de fato (console ou HTTP) fica para uma sprint posterior.
 *
 * <p><strong>Regra de dependência:</strong> quando existir, deve conversar
 * apenas com {@link informaci.controle}. Nenhuma classe desta camada
 * pode conter regra de negócio.
 */
package informaci.fronteira;
