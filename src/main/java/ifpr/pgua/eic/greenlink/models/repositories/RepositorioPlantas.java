package ifpr.pgua.eic.greenlink.models.repositories;

import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.daos.JardimDAO;
import ifpr.pgua.eic.greenlink.models.daos.PlantaDAO;
import ifpr.pgua.eic.greenlink.models.entities.Planta;

public class RepositorioPlantas {
    PlantaDAO plantaDAO;
    JardimDAO jardimDAO;

    public RepositorioPlantas(PlantaDAO plantaDAO, JardimDAO jardimDAO) {
        this.plantaDAO = plantaDAO;
        this.jardimDAO = jardimDAO;
    }

    public Resultado<ArrayList<Planta>> listarPlantas() {
        return plantaDAO.listarTodasPlantas();
    }
}
