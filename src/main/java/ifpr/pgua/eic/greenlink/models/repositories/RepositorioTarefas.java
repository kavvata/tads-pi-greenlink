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

    private Resultado<ArrayList<Tarefa>> incluiPlantas(ArrayList<Tarefa> lista) {
        for (Tarefa tarefa : lista) {
            Resultado<Planta> resultado = plantaDAO.buscarPlantaTarefa(tarefa.getId());

            if (resultado.foiErro()) {
                return Resultado.erro(resultado.getMsg());
            }

            tarefa.setPlanta(resultado.comoSucesso().getObj());
        }
        return Resultado.sucesso("Plantas incluidas, listagem com sucesso!", lista);
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
            Resultado<Planta> resultado = plantaDAO.buscarPlantaTarefa(tarefa.getId());

            if (resultado.foiErro()) {
                return Resultado.erro(resultado.getMsg());
            }

            tarefa.setPlanta(resultado.comoSucesso().getObj());
        }

        return listagemResultado;
    }

    public  Resultado<ArrayList<Tarefa>> listaTarefasPlanta(int id) {
        Resultado<ArrayList<Tarefa>> listagemResultado = tarefaDAO.listarTarefasPlanta(id);

        if (listagemResultado.foiErro()) {
            return listagemResultado;
        }

        ArrayList<Tarefa> lista = listagemResultado.comoSucesso().getObj();

        listagemResultado = incluiPlantas(lista);

        return listagemResultado;
    }

    public Resultado<ArrayList<Tarefa>> listaTarefasJardim(int jardimId) {
        Resultado<ArrayList<Tarefa>> listagemResultado = tarefaDAO.listarTarefasJardim(jardimId);

        if (listagemResultado.foiErro()) {
            return listagemResultado;
        }

        ArrayList<Tarefa> lista = listagemResultado.comoSucesso().getObj();

        listagemResultado = incluiPlantas(lista);

        return listagemResultado;
    }

    public Resultado<Tarefa> marcarFeito(Tarefa tarefa) {
        tarefa.setFeito(true);

        return tarefaDAO.atualizarTarefa(tarefa.getId(), tarefa);
    }

    public Resultado<Tarefa> removerTarefa(Tarefa tarefa) {
        return tarefaDAO.removerTarefa(tarefa);
    }
}
