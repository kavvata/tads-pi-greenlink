package ifpr.pgua.eic.greenlink.models.daos;

import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.models.entities.Tarefa;

public interface TarefaDAO {
    public Resultado<Tarefa> cadastrarTarefa(Tarefa nova);
    public Resultado<Tarefa> atualizarTarefa(int id, Tarefa nova);

    public Resultado<ArrayList<Tarefa>> listarTodasTarefas();
    public Resultado<ArrayList<Tarefa>> listarTarefasPlanta(int idPlanta);

    public Resultado<Tarefa> removerTarefa(Tarefa tarefa);
}
