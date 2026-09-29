package informaci.entidade;

/**
 * Papel que um usuário exerce no sistema.
 *
 * <p>Corresponde aos atores do diagrama de casos de uso em
 * {@code docs/specs/casos-de-uso/}. O ator Visitante não aparece aqui porque
 * não possui conta — ele é justamente quem ainda não se cadastrou.
 */
public enum Papel {

    /** Aluno ou servidor do Centro de Informática. */
    MEMBRO,

    /** Responsável pela gestão de contas dos membros. */
    ADMINISTRADOR
}
