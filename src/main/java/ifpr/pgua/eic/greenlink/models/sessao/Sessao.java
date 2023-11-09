package ifpr.pgua.eic.greenlink.models.sessao;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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

    public static byte[] geraSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    public static byte[] geraHash(String senha) {
        return geraHash(senha, geraSalt());
    }

    public static byte[] geraHash(String senha, byte[] salt) {

        /*
         * Referencia: https://adambard.com/blog/3-wrong-ways-to-store-a-password/
         */

        final int ITERACOES = 1000;
        final int KEY_LENGTH = 192;

        char[] senhaChars = senha.toCharArray();

        PBEKeySpec spec = new PBEKeySpec(
                senhaChars,
                salt,
                ITERACOES,
                KEY_LENGTH);

        SecretKeyFactory key = null;
        try {
            key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hashed = null;
        try {
            hashed = key.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return hashed;
    }
}
