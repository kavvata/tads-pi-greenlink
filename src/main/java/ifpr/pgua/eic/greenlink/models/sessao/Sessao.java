package ifpr.pgua.eic.greenlink.models.sessao;
import ifpr.pgua.eic.greenlink.models.entities.Usuario;

public class Sessao {
    private Usuario usuario;

    private static Sessao instance;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getUserId() {
        return usuario.getId();
    }

    public boolean isLogado() {
        return usuario != null ? true : false;
    }

    public static Sessao getInstance() {
        if (instance == null) {
            instance = new Sessao();
            return instance;
        }

        return instance;
    }

    public static String geraHash(String senha) {
        /* TODO */
        return senha;
    }
    public static String geraHash(String senha, String strSalt) {
        /* TODO */
        return senha;
    }
}
