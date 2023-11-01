package ifpr.pgua.eic.greenlink.models.repositories;

import java.util.ArrayList;

import ifpr.pgua.eic.greenlink.models.daos.JardimDAO;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;

public class RepositorioJardins {
    JardimDAO dao;

    public RepositorioJardins(JardimDAO dao) {
        this.dao = dao;
    }

    public String cadastrarJardim(String nome, String descricao) {
        return dao.cadastrarJardim(new Jardim(nome, descricao));

    }

    public ArrayList<Jardim> listarJardins() {
        return dao.listarJardins();
    }

    public String removerJardim(Jardim jardim) {
        return dao.removerJardim(jardim);
    }
}
