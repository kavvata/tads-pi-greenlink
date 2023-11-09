package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Planta;
import ifpr.pgua.eic.greenlink.models.sessao.Sessao;
import ifpr.pgua.eic.greenlink.utils.DBUtils;

public class JDBCPlantaDAO implements PlantaDAO {

    private final String SELECT_SQL = "SELECT * FROM plantas WHERE ativo=1";
    private final String INSERT_SQL = "INSERT INTO plantas(nome, descricao, jardim_id) VALUES (?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE plantas SET nome=?, descricao=?, jardim_id=? WHERE id=?";

    private FabricaConexoes fabrica;
    private Sessao sessao;

    public JDBCPlantaDAO(FabricaConexoes fabrica, Sessao sessao) {
        this.fabrica = fabrica;
        this.sessao = sessao;
    }

    @Override
    public Resultado<Planta> cadastrarPlanta(Planta nova) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, nova.getNome());
            pstm.setString(2, nova.getDescricao());
            pstm.setInt(3, nova.getJardim().getId());

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
    public Resultado<Planta> atualizarPlanta(int id, Planta nova) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(UPDATE_SQL);

            pstm.setString(1, nova.getNome());
            pstm.setString(2, nova.getDescricao());
            pstm.setInt(3, nova.getJardim().getId());

            pstm.setInt(4, id);

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno > 1) {

                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");

            }

            return Resultado.sucesso("Jardim atualizado!", nova);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<ArrayList<Planta>> listarPlantasJardim(int idJardim) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_SQL + " AND jardim_id=?");
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

            PreparedStatement pstm = con.prepareStatement("call listar_plantas_usuario(?)");

            if(!sessao.isLogado()) {
                return Resultado.erro("Sessao expirou! faca login novamente.");
            }

            pstm.setInt(1, sessao.getUsuario().getId());

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
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Planta> buscarPorId(int id) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_SQL + " AND id=?");
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            boolean sucesso = rs.next();

            if (!sucesso) {
                return Resultado.erro("Planta nao encontrada.");
            }

            String nome = rs.getString("nome");
            String descricao = rs.getString("descricao");

            return Resultado.sucesso("Planta encontrada!", new Planta(id, nome, descricao, null));

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Planta> buscarPorNome(String nome) {
        try (Connection con = fabrica.getConnection()) {

            //                            buscar_planta_nome(varchar nome, int usuario_id)
            PreparedStatement pstm = con.prepareStatement("call buscar_planta_nome(?, ?)");
            if (!sessao.isLogado()) {
                return Resultado.erro("Sessao expirou! faca login novamente.");
            }

            pstm.setString(1, nome);
            pstm.setInt(1, sessao.getUserId());

            ResultSet rs = pstm.executeQuery();

            boolean sucesso = rs.next();

            if (!sucesso) {
                return Resultado.erro("Planta nao encontrada.");
            }

            int id = rs.getInt("id");
            String descricao = rs.getString("descricao");

            return Resultado.sucesso("Planta encontrada!", new Planta(id, nome, descricao, null));
            
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Planta> buscarPlantaTarefa(int idTarefa) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement("call buscar_planta_tarefa(?)");
            pstm.setInt(1, idTarefa);

            ResultSet rs = pstm.executeQuery();
            boolean sucesso = rs.next();

            if (!sucesso) {
                return Resultado.erro("Planta nao encontrada.");
            }

            int id = rs.getInt("id");
            String nome = rs.getString("nome");
            String descricao = rs.getString("descricao");

            return Resultado.sucesso("Planta da tarefa encontrada!", new Planta(id, nome, descricao, null));

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
    
    @Override
    public Resultado<Planta> removerPlanta(Planta planta) {
        final String DELETESQL = "UPDATE plantas SET ativo=0 WHERE id=?";

        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(DELETESQL);
            pstm.setInt(1, planta.getId());

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno > 1) {
                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");
            }

            return Resultado.sucesso("Planta removida com sucesso.", planta);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }

    }
}
