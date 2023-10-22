package ifpr.pgua.eic.greenlink.models.daos;

import java.util.ArrayList;

import ifpr.pgua.eic.greenlink.models.entities.Jardim;

public interface JardimDAO {
    public Jardim cadastrarJardim(Jardim novo);
    public ArrayList<Jardim> listarJardins();
    public Jardim buscarJardim(String nome);
    public boolean removerJardim(Jardim jardim);
}
