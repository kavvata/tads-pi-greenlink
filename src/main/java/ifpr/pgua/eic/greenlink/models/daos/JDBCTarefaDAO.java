package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Tarefa;
import ifpr.pgua.eic.greenlink.utils.DBUtils;

public class JDBCTarefaDAO implements TarefaDAO {

    final String INSERT_SQL = "INSERT INTO tarefas(nome, descricao, planta_id, prazo) VALUES (?,?,?,?)";
    final String UPDATE_SQL = "UPDATE tarefas SET nome=?, descricao=?, planta_id=?, prazo=?, feito=? WHERE id=?";
    final String SELECT_SQL = "SELECT * FROM tarefas";

    FabricaConexoes fabrica;

    public JDBCTarefaDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado<Tarefa> cadastrarTarefa(Tarefa nova) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(INSERT_SQL);

            pstm.setString(1, nova.getNome());
            pstm.setString(2, nova.getDescricao());
            pstm.setInt(3, nova.getPlanta().getId());
            pstm.setString(4, nova.getPrazo().toString());

            int valorRetorno = pstm.executeUpdate();


            if (valorRetorno > 1) {
                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");
            }

            nova.setId(DBUtils.getLastId(pstm));

            return Resultado.sucesso("Tarefa cadastrada com sucesso!", nova);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Tarefa> atualizarTarefa(int id, Tarefa nova) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(UPDATE_SQL);

            pstm.setString(1, nova.getNome());
            pstm.setString(2, nova.getDescricao());
            pstm.setInt(3, nova.getPlanta().getId());
            pstm.setString(4, nova.getPrazo().toString());
            pstm.setBoolean(5, nova.isFeito());
            pstm.setInt(6, id);

            int valorRetorno = pstm.executeUpdate();


            if (valorRetorno > 1) {
                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");
            }

            nova.setId(DBUtils.getLastId(pstm));

            return Resultado.sucesso("Tarefa atualizada", nova);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<ArrayList<Tarefa>> listarTodasTarefas() {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_SQL + " WHERE feito=0");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Tarefa> lista = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String prazoString = rs.getString("prazo");
                boolean feito = rs.getBoolean("feito");

                /* NOTE: Categoria sera incluida em RepositorioTarefas */
                lista.add(new Tarefa(id, nome, descricao, null, LocalDate.parse(prazoString), feito));

            }

            return Resultado.sucesso("Listagem com sucesso", lista);
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<ArrayList<Tarefa>> listarTarefasPlanta(int idPlanta) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_SQL + " WHERE planta_id=? and feito=0");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Tarefa> lista = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String prazoString = rs.getString("prazo");
                boolean feito = rs.getBoolean("feito");

                /* NOTE: Categoria sera incluida em RepositorioTarefas */
                lista.add(new Tarefa(id, nome, descricao, null, LocalDate.parse(prazoString), feito));

            }

            return Resultado.sucesso("Listagem com sucesso", lista);
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Tarefa> removerTarefa(Tarefa tarefa) {
        final String DELETE_SQL = "DELETE FROM tarefas WHERE id = ?";

        try (Connection con = fabrica.getConnection()) {
            
            PreparedStatement pstm = con.prepareStatement(DELETE_SQL);

            int valorRetorno = pstm.executeUpdate();

            if (valorRetorno > 1) {
                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");
            }

            return Resultado.sucesso("Tarefa removida", tarefa);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
}
