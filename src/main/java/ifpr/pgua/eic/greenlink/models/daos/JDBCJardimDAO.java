package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ifpr.pgua.eic.greenlink.models.entities.Jardim;

public class JDBCJardimDAO implements JardimDAO {

    final String INSERT_SQL = "INSERT INTO jardins(nome,descricao) ";
    final String SELECT_SQL = "SELECT * FROM  jardins ";

    private FabricaConexoes fabrica;

    public JDBCJardimDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public String cadastrarJardim(Jardim novo) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(INSERT_SQL + "values (?, ?)");
            pstm.setString(1, novo.getNome());
            pstm.setString(2, novo.getDescricao());

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno > 1) {
                return "Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.";
            }

            return "Jardim cadastrado com sucesso!";

        } catch(SQLException e) {
            return e.getMessage();
        }
    }

    @Override
    public ArrayList<Jardim> listarJardins() {
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

            return lista;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public String removerJardim(Jardim jardim) {
        final String DROPSQL = "DELETE FROM jardins WHERE id = ?";

        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(DROPSQL);
            pstm.setInt(1, jardim.getId());

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno > 1) {
                return "Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.";
            }

            return "Jardim removido com sucesso.";

        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    @Override
    public String atualizarJardim(int id, Jardim novo) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement("UPDATE jardins SET nome=?, descricao=? WHERE id=?");

            pstm.setString(1, novo.getNome());
            pstm.setString(2, novo.getDescricao());
            pstm.setInt(3, id);

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno > 1) {
                return "Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.";
            }

            return "Jardim atualizado!";

        } catch (SQLException e) {
            return e.getMessage();
        }
    }
    
}
