package ifpr.pgua.eic.greenlink.models.daos;

import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Planta;

public interface PlantaDAO {
    public Resultado<Planta> cadastrarPlanta(Planta nova);
    public Resultado<Planta> atualizarPlanta(int id, Planta nova);

    public Resultado<ArrayList<Planta>> listarPlantasJardim(int idJardim);
    public Resultado<ArrayList<Planta>> listarTodasPlantas();
    public Resultado<Planta> buscarPorId(int id);
    public Resultado<Planta> buscarPorNome(String nome);
    public Resultado<Planta> buscarPlantaTarefa(int idTarefa);

    public Resultado<Planta> removerPlanta(Planta planta);
}
