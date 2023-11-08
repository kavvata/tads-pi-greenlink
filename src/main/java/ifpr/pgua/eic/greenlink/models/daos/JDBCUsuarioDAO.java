package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                "INSERT INTO usuarios(nome, hash_senha) VALUES (?,?)", 
                Statement.RETURN_GENERATED_KEYS
            );

            pstm.setString(1, novo.getNome());
            pstm.setString(2, novo.getSenha());

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno != 1) {
                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");
            }

            int id = DBUtils.getLastId(pstm);
            novo.setId(id);

            return Resultado.sucesso("usuario Cadastrado!", novo);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Usuario> autenticaUsuario(Usuario usuario) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_SQL + " AND nome=?");
            pstm.setString(1, usuario.getNome());

            ResultSet rs = pstm.executeQuery();

            boolean sucesso = rs.next();

            if(!sucesso) {
                return Resultado.erro("Usuario nao encontrado");
            }

            int id = rs.getInt("id");
            
            if (!usuario.getSenha().equals(rs.getString("hash_senha"))) {
                return Resultado.erro("Senha incorreta!");
            }

            sessao.setUsuario(new Usuario(id, usuario.getNome(), usuario.getSenha()));
            return Resultado.sucesso("Autenticado com sucesso", sessao.getUsuario());

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
}
