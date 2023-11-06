package ifpr.pgua.eic.greenlink.models.repositories;

import java.time.LocalDate;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.daos.PlantaDAO;
import ifpr.pgua.eic.greenlink.models.daos.TarefaDAO;
import ifpr.pgua.eic.greenlink.models.entities.Planta;
import ifpr.pgua.eic.greenlink.models.entities.Tarefa;

public class RepositorioTarefas {
    TarefaDAO tarefaDAO;
    PlantaDAO plantaDAO;

    public RepositorioTarefas(TarefaDAO tarefaDAO, PlantaDAO plantaDAO) {
        this.tarefaDAO = tarefaDAO;
        this.plantaDAO = plantaDAO;
    }

    public Resultado<Tarefa> cadastrarTarefa(String nome, String descricao, Planta planta, LocalDate prazo) {
        return tarefaDAO.cadastrarTarefa(new Tarefa(nome, descricao, planta, prazo));
    }

    public Resultado<Tarefa> atualizarTarefa(int id, String nome, String descricao, Planta planta, LocalDate prazo, boolean feito) {
        return tarefaDAO.atualizarTarefa(id, new Tarefa(id, nome, descricao, planta, prazo, feito));
    }

    public Resultado<ArrayList<Tarefa>> listarTarefas() {
        Resultado<ArrayList<Tarefa>> listagemResultado = tarefaDAO.listarTodasTarefas();

        if (listagemResultado.foiErro()) {
            return listagemResultado;
        }

        ArrayList<Tarefa> lista = listagemResultado.comoSucesso().getObj();

        for (Tarefa tarefa : lista) {
            Resultado<Planta> resultado = plantaDAO.buscarPorId(tarefa.getId());

            if (resultado.foiErro()) {
                return Resultado.erro(resultado.getMsg());
            }

            tarefa.setPlanta(resultado.comoSucesso().getObj());
        }

        return listagemResultado;
    }
}
