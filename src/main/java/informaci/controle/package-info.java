/**
 * Camada de <strong>Controle</strong> (Control) do modelo de análise.
 *
 * <p>Coordena os casos de uso do sistema, orquestrando entidades e
 * persistência. É aqui que fica declarado o contrato do repositório
 * ({@code UsuarioRepositorio}), implementado pela camada de persistência —
 * de modo que o controle dependa da abstração, nunca do armazenamento
 * concreto.
 *
 * <p><strong>Regra de dependência:</strong> depende apenas de
 * {@link informaci.entidade}. Não conhece detalhes de interface com o
 * usuário nem de armazenamento.
 */
package informaci.controle;
