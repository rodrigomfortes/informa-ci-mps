package informaci.persistencia;

import informaci.controle.FabricaDeRepositorios;
import informaci.controle.PublicacaoRepositorio;
import informaci.controle.RegistroDeAcessoRepositorio;
import informaci.controle.UsuarioRepositorio;

/**
 * Fábrica concreta da família de repositórios em memória RAM (Abstract Factory).
 *
 * <p>Os dados existem só enquanto a aplicação estiver em execução.
 */
public class FabricaDeRepositoriosEmMemoria implements FabricaDeRepositorios {

    @Override
    public UsuarioRepositorio criarRepositorioDeUsuarios() {
        return new UsuarioRepositorioEmMemoria();
    }

    @Override
    public PublicacaoRepositorio criarRepositorioDePublicacoes() {
        return new PublicacaoRepositorioEmMemoria();
    }

    @Override
    public RegistroDeAcessoRepositorio criarRepositorioDeAcessos() {
        return new RegistroDeAcessoRepositorioEmMemoria();
    }
}
