package ifpr.pgua.eic.greenlink.models.daos;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Usuario;

public interface UsuarioDAO {
    public Resultado<Usuario> cadastrarUsuario(Usuario novo);
    public Resultado<Usuario> buscarPorId(int id);
    public Resultado<Usuario> buscarPorNome(String nome);
}
