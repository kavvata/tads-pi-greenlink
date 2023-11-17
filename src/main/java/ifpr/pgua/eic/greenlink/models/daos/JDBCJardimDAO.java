package ifpr.pgua.eic.greenlink.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.sessao.Sessao;
import ifpr.pgua.eic.greenlink.utils.DBUtils;

public class JDBCJardimDAO implements JardimDAO {

    final String INSERT_SQL = "INSERT INTO jardins(nome,descricao,usuario_id) VALUES (?,?,?)";
    final String SELECT_SQL = "SELECT * FROM jardins WHERE ativo=1";

    private FabricaConexoes fabrica;
    private Sessao sessao;

    public JDBCJardimDAO(FabricaConexoes fabrica, Sessao sessao) {
        this.fabrica = fabrica;
        this.sessao = sessao;
    }

    @Override
    public Resultado<Jardim> cadastrarJardim(Jardim novo) {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);

            if(!sessao.isLogado()) {
                return Resultado.erro("Sua sessão expirou, Faça login novamente!");
            }

            pstm.setString(1, novo.getNome());
            pstm.setString(2, novo.getDescricao());
            pstm.setInt(3, sessao.getUserId());

            int valorRetorno = pstm.executeUpdate();

            

            if (valorRetorno != 1) {

                return Resultado.erro("Erro! mais de uma tabela alterada: " + valorRetorno + " tabelas alteradas.");

            }

            novo.setId(DBUtils.getLastId(pstm));
            pstm.setString(1, novo.getNome());
            return Resultado.sucesso("Jardim cadastrado com sucesso!", novo);

        } catch(SQLException e) {
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
    public Resultado<ArrayList<Jardim>> listarJardins() {
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement("call listar_jardins_usuario(?)");

            if(!sessao.isLogado()) {
                return Resultado.erro("Sessao expirou! faca login novamente.");
            }

            pstm.setInt(1, sessao.getUserId());

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
    public Resultado<Jardim> buscarPorId(int id) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_SQL + " AND id=?");
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            boolean sucesso = rs.next();

            if (!sucesso) {
                return Resultado.erro("Jardim nao encontrado.");
            }

            String nome = rs.getString("nome");
            String descricao = rs.getString("descricao");

            return Resultado.sucesso("Jardim encontrado!", new Jardim(id, nome, descricao));

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
    public Resultado<Jardim> buscarPorNome(String nome) {
        try (Connection con = fabrica.getConnection()) {

            //                           buscar_jardim_nome(varchar nome, int usuario_id)
            PreparedStatement pstm = con.prepareStatement("call buscar_jardim_nome(?,?)");

            if(!sessao.isLogado()) {
                return Resultado.erro("Sua sessão expirou, Faça login novamente!");
            }
            
            pstm.setString(1, nome);
            pstm.setInt(2, sessao.getUserId());

            ResultSet rs = pstm.executeQuery();

            boolean sucesso = rs.next();

            if (!sucesso) {
                return Resultado.erro("Jardim nao encontrado.");
            }

            int id = rs.getInt("id");
            String descricao = rs.getString("descricao");

            return Resultado.sucesso("jardim encontrado!", new Jardim(id, nome, descricao));

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
   @Override
    public Resultado<Jardim> buscarJardimPlanta(int plantaId) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement("call buscar_jardim_planta(?)");
            pstm.setInt(1, plantaId);

            ResultSet rs = pstm.executeQuery();
            boolean sucesso = rs.next();

            if (!sucesso) {
                return Resultado.erro("Jardim nao encontrado.");
            }

            int id = rs.getInt("id");
            String nome = rs.getString("nome");
            String descricao = rs.getString("descricao");

            return Resultado.sucesso("jardim da planta encontrado!", new Jardim(id, nome, descricao));

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Jardim> removerJardim(Jardim jardim) {
        final String DELETE_SQL = "UPDATE jardins SET ativo=0 WHERE id = ?";

        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(DELETE_SQL);
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
}
