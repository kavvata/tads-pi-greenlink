package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.utils.DBUtils;

public class JDBCJardimDAO implements JardimDAO {

    final String INSERT_SQL = "INSERT INTO jardins(nome,descricao) ";
    final String SELECT_SQL = "SELECT * FROM  jardins ";

    private FabricaConexoes fabrica;

    public JDBCJardimDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado<Jardim> cadastrarJardim(Jardim novo) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(INSERT_SQL + "values (?, ?)");
            pstm.setString(1, novo.getNome());
            pstm.setString(2, novo.getDescricao());

            int valorRetorno = pstm.executeUpdate();

            

            if (valorRetorno > 1) {

                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");

            }

            novo.setId(DBUtils.getLastId(pstm));

            return Resultado.sucesso("Jardim cadastrado com sucesso!", novo);

        } catch(SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<ArrayList<Jardim>> listarJardins() {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(SELECT_SQL);

            ResultSet rs = pstm.executeQuery();

            ArrayList<Jardim> lista = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");

                lista.add(new Jardim(id, nome, descricao));
            }

            return Resultado.sucesso("listagem com sucesso", lista);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Jardim> removerJardim(Jardim jardim) {
        final String DROPSQL = "DELETE FROM jardins WHERE id = ?";

        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(DROPSQL);
            pstm.setInt(1, jardim.getId());

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno > 1) {
                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");
            }

            return Resultado.sucesso("Jardim removido com sucesso.", jardim);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Jardim> atualizarJardim(int id, Jardim novo) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement("UPDATE jardins SET nome=?, descricao=? WHERE id=?");

            pstm.setString(1, novo.getNome());
            pstm.setString(2, novo.getDescricao());
            pstm.setInt(3, id);

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno > 1) {
                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");
            }

            return Resultado.sucesso("Jardim atualizado!", novo);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Jardim> buscarPorId(int id) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_SQL + "WHERE id=?");
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            boolean sucesso = rs.next();

            if (!sucesso) {
                return Resultado.erro("Jardim nao encontrado.");
            }

            String nome = rs.getString("nome");
            String descricao = rs.getString("descricao");

            return Resultado.sucesso(descricao, new Jardim(id, nome, descricao));

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
    
}
