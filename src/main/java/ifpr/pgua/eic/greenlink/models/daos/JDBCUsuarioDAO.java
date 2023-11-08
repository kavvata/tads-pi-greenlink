package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

            String hash = Sessao.geraHash(novo.getSenha());
            if (hash.isEmpty() || hash.isBlank()) {
                return Resultado.erro("Erro ao cadastrar senha. cheque logs");
            }

            PreparedStatement pstm = con.prepareStatement("INSERT INTO usuarios(nome, hash_senha) VALUES (?,?)");

            pstm.setString(1, novo.getNome());
            pstm.setString(2, hash);

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno != 1) {
                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");
            }

            DBUtils.getLastId(pstm);

            return Resultado.sucesso("usuario Cadastrado!", novo);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Usuario> buscarPorId(int id) {
        // XXX Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
    }

    @Override
    public Resultado<Usuario> buscarPorNome(String nome) {
        // XXX Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorNome'");

    }
}
