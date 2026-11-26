package informaci.controle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Invocador do padrão Command. */
class InvocadorDeComandosTest {

    private final InvocadorDeComandos invocador = new InvocadorDeComandos();

    @Test
    void deve_executar_o_comando_e_devolver_o_resultado() {
        assertEquals("resultado", invocador.executar(() -> "resultado"));
    }

    @Test
    void deve_executar_o_comando_uma_unica_vez() {
        int[] execucoes = {0};

        invocador.executar(() -> ++execucoes[0]);

        assertEquals(1, execucoes[0]);
    }

    @Test
    void deve_aceitar_comando_sem_resultado() {
        Comando<Void> semResultado = () -> null;

        assertNull(invocador.executar(semResultado));
    }

    @Test
    void deve_recusar_comando_nulo() {
        NullPointerException erro = assertThrows(NullPointerException.class, () -> invocador.executar(null));

        assertEquals("O comando é obrigatório.", erro.getMessage());
    }

    @Test
    void deve_repassar_a_excecao_lancada_pelo_comando() {
        NadaParaDesfazerException falha = new NadaParaDesfazerException();

        assertSame(falha, assertThrows(NadaParaDesfazerException.class, () -> invocador.executar(() -> {
            throw falha;
        })));
    }
}
