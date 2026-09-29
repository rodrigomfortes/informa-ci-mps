package informaci.entidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioTest {

    @Test
    void deve_criar_usuario_quando_dados_sao_validos() {
        Usuario usuario = new Usuario("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);

        assertNotNull(usuario.getId());
        assertEquals("Ana Souza", usuario.getNome());
        assertEquals("ana@academico.ufpb.br", usuario.getEmail());
        assertEquals(Papel.MEMBRO, usuario.getPapel());
    }

    @Test
    void deve_rejeitar_quando_nome_e_nulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario(null, "ana@academico.ufpb.br", Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_nome_esta_em_branco() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario("   ", "ana@academico.ufpb.br", Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_email_e_nulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario("Ana Souza", null, Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_email_nao_tem_arroba() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario("Ana Souza", "ana.academico.ufpb.br", Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_email_nao_tem_dominio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Usuario("Ana Souza", "ana@ufpb", Papel.MEMBRO));
    }

    @Test
    void deve_rejeitar_quando_papel_e_nulo() {
        assertThrows(NullPointerException.class,
                () -> new Usuario("Ana Souza", "ana@academico.ufpb.br", null));
    }

    @Test
    void deve_remover_espacos_em_volta_do_nome() {
        Usuario usuario = new Usuario("  Ana Souza  ", "ana@academico.ufpb.br", Papel.MEMBRO);

        assertEquals("Ana Souza", usuario.getNome());
    }

    @Test
    void deve_normalizar_email_para_minusculas() {
        Usuario usuario = new Usuario("Ana Souza", "  ANA@Academico.UFPB.br ", Papel.MEMBRO);

        assertEquals("ana@academico.ufpb.br", usuario.getEmail());
    }

    @Test
    void deve_tratar_como_diferentes_dois_usuarios_com_os_mesmos_dados() {
        Usuario primeiro = new Usuario("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);
        Usuario segundo = new Usuario("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);

        assertNotEquals(primeiro, segundo);
    }

    @Test
    void deve_tratar_como_igual_o_usuario_comparado_consigo_mesmo() {
        Usuario usuario = new Usuario("Ana Souza", "ana@academico.ufpb.br", Papel.MEMBRO);

        assertEquals(usuario, usuario);
        assertEquals(usuario.hashCode(), usuario.hashCode());
    }
}
