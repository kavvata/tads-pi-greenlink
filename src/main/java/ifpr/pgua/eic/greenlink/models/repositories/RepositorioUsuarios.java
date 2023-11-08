package ifpr.pgua.eic.greenlink.models.repositories;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.daos.UsuarioDAO;
import ifpr.pgua.eic.greenlink.models.entities.Usuario;

public class RepositorioUsuarios {
    private UsuarioDAO dao;

    public RepositorioUsuarios(UsuarioDAO dao) {
        this.dao = dao;
    }

    public Resultado<Usuario> cadastrarUsuario(String nome, String senha) {
        return dao.cadastrarUsuario(new Usuario(nome, senha));
    }

    public Resultado<Usuario> autenticaUsuario(String nome, String senha) {
        return dao.autenticaUsuario(new Usuario(nome, senha));
    }
}
