package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Tarefa;
import ifpr.pgua.eic.greenlink.models.sessao.Sessao;
import ifpr.pgua.eic.greenlink.utils.DBUtils;

public class JDBCTarefaDAO implements TarefaDAO {

    final String INSERT_SQL = "INSERT INTO tarefas(nome, descricao, planta_id, prazo) VALUES (?,?,?,?)";
    final String UPDATE_SQL = "UPDATE tarefas SET nome=?, descricao=?, planta_id=?, prazo=?, feito=? WHERE id=?";

    private FabricaConexoes fabrica;
    private Sessao sessao;

    public JDBCTarefaDAO(FabricaConexoes fabrica, Sessao sessao) {
        this.fabrica = fabrica;
        this.sessao = sessao;
    }

    @Override
    public Resultado<Tarefa> cadastrarTarefa(Tarefa nova) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);

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

            return Resultado.sucesso("Tarefa atualizada", nova);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<ArrayList<Tarefa>> listarTodasTarefas() {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement("call listar_tarefas_usuario(?)");

            if(!sessao.isLogado()) {
                return Resultado.erro("Sessao expirou! faca login novamente.");
            }

            pstm.setInt(1, sessao.getUserId());

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
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<ArrayList<Tarefa>> listarTarefasPlanta(int idPlanta) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement("call listar_tarefas_planta(?)");
            pstm.setInt(1, idPlanta);

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
    public Resultado<ArrayList<Tarefa>> listarTarefasJardim(int idJardim) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement("call listar_tarefas_jardim(?)");
            pstm.setInt(1, idJardim);

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
        final String DELETE_SQL = "UPDATE tarefas SET ativo=0 WHERE id = ?";

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
