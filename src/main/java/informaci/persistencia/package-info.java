/**
 * Camada de <strong>persistência</strong>.
 *
 * <p>Implementa os contratos de repositório declarados em
 * {@link informaci.controle}. Nesta sprint o armazenamento é feito
 * inteiramente em memória RAM, através de uma coleção — sem banco de dados.
 *
 * <p>Isolar o armazenamento atrás de uma interface é o que permite trocar a
 * coleção em memória por um banco relacional numa sprint futura sem alterar
 * nenhuma linha das camadas de controle ou entidade.
 *
 * <p><strong>Regra de dependência:</strong> é a única camada que conhece
 * detalhes de armazenamento. Nenhuma outra camada deve importar classes daqui.
 */
package informaci.persistencia;
