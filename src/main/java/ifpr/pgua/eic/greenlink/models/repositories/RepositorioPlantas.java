package ifpr.pgua.eic.greenlink.models.repositories;

import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.daos.JardimDAO;
import ifpr.pgua.eic.greenlink.models.daos.PlantaDAO;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.entities.Planta;

public class RepositorioPlantas {
    PlantaDAO plantaDAO;
    JardimDAO jardimDAO;

    public RepositorioPlantas(PlantaDAO plantaDAO, JardimDAO jardimDAO) {
        this.plantaDAO = plantaDAO;
        this.jardimDAO = jardimDAO;
    }

    public Resultado<Planta> cadastrarPlanta(String nome, String descricao, Jardim jardim) {
        return plantaDAO.cadastrarPlanta(new Planta(nome, descricao, jardim));
    }

    public Resultado<Planta> atualizarPlanta(int id, String nome, String descricao, Jardim jardim) {
        return plantaDAO.atualizarPlanta(id, new Planta(id, nome, descricao, jardim));
    }

    public Resultado<ArrayList<Planta>> listarPlantas() {
        Resultado<ArrayList<Planta>> listagemResultado = plantaDAO.listarTodasPlantas();

        if (listagemResultado.foiErro()) {
            return listagemResultado;
        }

        ArrayList<Planta> lista = listagemResultado.comoSucesso().getObj();

        for (Planta planta : lista) {
            Resultado<Jardim> resultado = jardimDAO.buscarJardimPlanta(planta.getId());

            if (resultado.foiErro()) {
                return Resultado.erro(resultado.getMsg());
            }

            planta.setJardim(resultado.comoSucesso().getObj());
        }

        return listagemResultado;
    }

    public Resultado<ArrayList<Planta>> listarPlantasJardim(int id) {
        Resultado<ArrayList<Planta>> listagemResultado = plantaDAO.listarPlantasJardim(id);

        if (listagemResultado.foiErro()) {
            return listagemResultado;
        }

        ArrayList<Planta> lista = listagemResultado.comoSucesso().getObj();

        for (Planta planta : lista) {
            Resultado<Jardim> resultado = jardimDAO.buscarJardimPlanta(planta.getId());

            if (resultado.foiErro()) {
                return Resultado.erro(resultado.getMsg());
            }

            planta.setJardim(resultado.comoSucesso().getObj());
        }

        return listagemResultado;
    }

    public Resultado<Planta> buscarPorNome(String nome) {
        Resultado<Planta> resultado = plantaDAO.buscarPorNome(nome);

        if (resultado.foiErro()) {
            return resultado;
        }

        Planta planta = resultado.comoSucesso().getObj();

        Resultado<Jardim> jardimResultado = jardimDAO.buscarJardimPlanta(planta.getId());

        if (resultado.foiErro()) {
            return Resultado.erro(jardimResultado.getMsg());
        }

        planta.setJardim(jardimResultado.comoSucesso().getObj());

        return resultado;

    }

    public Resultado<Planta> removerPlanta(Planta planta) {
        return plantaDAO.removerPlanta(planta);
    }


}
