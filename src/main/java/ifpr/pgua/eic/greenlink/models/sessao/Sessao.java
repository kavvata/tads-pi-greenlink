package ifpr.pgua.eic.greenlink.models.sessao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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

    public static Sessao getInstance() {
        if (instance == null) {
            instance = new Sessao();
            return instance;
        }

        return instance;
    }

    public static String geraHash(String senha) {
        /*
         * Referencia: https://www.baeldung.com/java-password-hashing
         */
        try {
            SecureRandom r = new SecureRandom();
            byte[] salt = new byte[16];
            r.nextBytes(salt);
            String strSalt = new String(salt, StandardCharsets.UTF_8);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashed = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            String strHashed = new String(hashed, StandardCharsets.UTF_8);

            return strSalt + ":" + strHashed;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
