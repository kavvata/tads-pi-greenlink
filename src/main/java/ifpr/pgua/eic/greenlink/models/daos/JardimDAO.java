package ifpr.pgua.eic.greenlink.models.daos;

import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Jardim;

public interface JardimDAO {
    public Resultado<Jardim> cadastrarJardim(Jardim novo);
    public Resultado<Jardim> atualizarJardim(int id, Jardim novo);

    public Resultado<ArrayList<Jardim>> listarJardins();
    public Resultado<Jardim> buscarPorId(int id);
    public Resultado<Jardim> buscarPorNome(String nome);
    public Resultado<Jardim> buscarJardimPlanta(int plantaId);

    public Resultado<Jardim> removerJardim(Jardim jardim);
}
