package ifpr.pgua.eic.greenlink.models.daos;

import java.util.ArrayList;

import ifpr.pgua.eic.greenlink.models.entities.Jardim;

public interface JardimDAO {
    public String cadastrarJardim(Jardim novo);
    public ArrayList<Jardim> listarJardins() throws RuntimeException;
    public Jardim buscarPorId(int id) throws RuntimeException;
    public String atualizarJardim(int id, Jardim novo);
    public String removerJardim(Jardim jardim);
}
