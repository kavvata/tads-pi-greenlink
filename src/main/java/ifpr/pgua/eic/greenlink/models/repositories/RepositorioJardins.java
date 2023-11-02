package ifpr.pgua.eic.greenlink.models.repositories;

import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.daos.JardimDAO;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;

public class RepositorioJardins {
    JardimDAO dao;

    public RepositorioJardins(JardimDAO dao) {
        this.dao = dao;
    }

    public Resultado<Jardim> cadastrarJardim(String nome, String descricao) {
        return dao.cadastrarJardim(new Jardim(nome, descricao));

    }

    public Resultado<ArrayList<Jardim>> listarJardins() throws RuntimeException {
        return dao.listarJardins();
    }

    public Resultado<Jardim> atualizarJardim(int id, String nome, String descricao) {
        return dao.atualizarJardim(id, new Jardim(id, nome, descricao));
    }

    public Resultado<Jardim> removerJardim(Jardim jardim) {
        return dao.removerJardim(jardim);
    }
}
