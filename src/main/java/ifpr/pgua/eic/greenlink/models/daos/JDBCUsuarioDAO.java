package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Usuario;
import ifpr.pgua.eic.greenlink.models.sessao.Sessao;
import ifpr.pgua.eic.greenlink.utils.DBUtils;

public class JDBCUsuarioDAO implements UsuarioDAO {

    private final String SELECT_SQL = "SELECT * FROM usuarios WHERE ativo=1";

    private FabricaConexoes fabrica;
    private Sessao sessao;


    public JDBCUsuarioDAO(FabricaConexoes fabrica, Sessao sesao) {
        this.fabrica = fabrica;
        this.sessao = sesao;
    }

    @Override
    public Resultado<Usuario> cadastrarUsuario(Usuario novo) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(
                "INSERT INTO usuarios(nome, salt, hash) VALUES (?,?,?)", 
                Statement.RETURN_GENERATED_KEYS
            );

            byte[] salt = Sessao.geraSalt();
            byte[] hash = Sessao.geraHash(novo.getSenha(), salt);

            pstm.setString(1, novo.getNome());
            pstm.setBytes(2, salt);
            pstm.setBytes(3, hash);

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno != 1) {
                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");
            }

            int id = DBUtils.getLastId(pstm);
            novo.setId(id);

            return Resultado.sucesso("Usuario cadastrado com sucesso!", novo);

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) { /* CODIGO DE ERRO SQL PARA VALOR DUPLICADO */
                return Resultado.erro("Nome de usuário já cadastrado.");
            }

            System.out.println(e.getErrorCode()) ;
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Usuario> autenticaUsuario(Usuario usuario) {
        final String mensagemErro = "Nome de usuário ou senha incorretos.";
        try (Connection con = fabrica.getConnection()) {

            /* TODO:funcao compara_hash(in varbinary hash) returns boolean */
            PreparedStatement pstm = con.prepareStatement(SELECT_SQL + " AND nome=?");
            pstm.setString(1, usuario.getNome());

            ResultSet rs = pstm.executeQuery();

            boolean sucesso = rs.next();

            if(!sucesso) {
                return Resultado.erro(mensagemErro);
            }

            int id = rs.getInt("id");
            byte[] salt = rs.getBytes("salt");
            byte[] hash = Sessao.geraHash(usuario.getSenha(), salt);

            if (!Arrays.equals(rs.getBytes("hash"), hash)) {
                return Resultado.erro(mensagemErro);
            }

            sessao.setUsuario(new Usuario(id, usuario.getNome(), usuario.getSenha()));
            return Resultado.sucesso("Autenticado com sucesso", sessao.getUsuario());

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
}
