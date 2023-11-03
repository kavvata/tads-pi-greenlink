package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Planta;
import ifpr.pgua.eic.greenlink.utils.DBUtils;

public class JDBCPlantaDAO implements PlantaDAO {

    private final String SELECT_SQL = "SELECT * FROM plantas";
    private final String INSERT_SQL = "INSERT INTO plantas(id, nome, descricao, jardim_id) VALUES (?, ?, ?, ?)";
    private FabricaConexoes fabrica;

    public JDBCPlantaDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado<Planta> cadastrarPlanta(Planta nova) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(INSERT_SQL);

            pstm.setInt(1, nova.getId());
            pstm.setString(2, nova.getNome());
            pstm.setString(3, nova.getDescricao());
            pstm.setInt(4, nova.getJardim().getId());

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno > 1) {
                return Resultado.erro("Erro: mais de uma tabela altera: " + valorRetorno + " tabelas alteradas.");
            }

            nova.setId(DBUtils.getLastId(pstm));

            return Resultado.sucesso("Planta cadastrada com sucesso!", nova);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<ArrayList<Planta>> listarPlantasJardim(int idJardim) throws RuntimeException {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_SQL + "WHERE jardim_id=?");
            pstm.setInt(1, idJardim);
            ResultSet rs = pstm.executeQuery();

            ArrayList<Planta> lista = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");

                lista.add(new Planta(id, nome, descricao, null));
            }

            return Resultado.sucesso("listagem com sucesso", lista);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<ArrayList<Planta>> listarTodasPlantas() {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_SQL);
            ResultSet rs = pstm.executeQuery();

            ArrayList<Planta> lista = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");

                /* Jardim sera incluso em RepositorioPlantas */
                lista.add(new Planta(id, nome, descricao, null));
            }

            return Resultado.sucesso("listagem com sucesso", lista);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Planta> buscarPorNome(String nome) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorNome'");
    }

    @Override
    public Resultado<Planta> atualizarPlanta(int id, Planta nova) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Resultado<Planta> removerPlanta(Planta planta) {
        // TODO Auto-generated method stub
        return null;
    }



}
