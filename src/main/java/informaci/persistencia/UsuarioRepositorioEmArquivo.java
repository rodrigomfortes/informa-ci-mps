package informaci.persistencia;

import informaci.controle.UsuarioRepositorio;
import informaci.entidade.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Armazena os usuários num arquivo binário (serialização Java), mantendo uma
 * cópia em memória para consulta.
 *
 * <p>No construtor, o arquivo é lido e carregado para a memória; se ele ainda
 * não existir, o repositório começa vazio. A cada {@link #salvar(Usuario)} o
 * arquivo é regravado por inteiro. Falhas de E/S são convertidas em
 * {@link PersistenciaException}.
 */
public class UsuarioRepositorioEmArquivo implements UsuarioRepositorio {

    private final Path arquivo;
    private final Map<UUID, Usuario> usuarios = new LinkedHashMap<>();

    /**
     * @throws PersistenciaException se o arquivo existir mas não puder ser lido
     */
    public UsuarioRepositorioEmArquivo(Path arquivo) {
        this.arquivo = Objects.requireNonNull(arquivo, "O caminho do arquivo é obrigatório.");
        carregar();
    }

    @Override
    public void salvar(Usuario usuario) {
        Objects.requireNonNull(usuario, "Não é possível salvar um usuário nulo.");
        usuarios.put(usuario.getId(), usuario);
        gravar();
    }

    @Override
    public boolean existeComEmail(String email) {
        if (email == null) {
            return false;
        }
        return usuarios.values().stream()
                .anyMatch(usuario -> usuario.getEmail().equals(email));
    }

    @Override
    public List<Usuario> listarTodos() {
        return List.copyOf(usuarios.values());
    }

    private void carregar() {
        if (!Files.exists(arquivo)) {
            return;
        }
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(arquivo))) {
            List<?> lidos = (List<?>) entrada.readObject();
            for (Object objeto : lidos) {
                Usuario usuario = (Usuario) objeto;
                usuarios.put(usuario.getId(), usuario);
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PersistenciaException("Não foi possível ler os usuários de " + arquivo + ".", e);
        }
    }

    private void gravar() {
        try (ObjectOutputStream saida = new ObjectOutputStream(Files.newOutputStream(arquivo))) {
            saida.writeObject(new ArrayList<>(usuarios.values()));
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível gravar os usuários em " + arquivo + ".", e);
        }
    }
}
