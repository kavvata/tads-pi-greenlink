package ifpr.pgua.eic.greenlink.models.daos;

import java.util.ArrayList;

import ifpr.pgua.eic.greenlink.models.entities.Jardim;

public interface JardimDAO {
    public String cadastrarJardim(Jardim novo);
    public ArrayList<Jardim> listarJardins();
    public String removerJardim(Jardim jardim);
}
