package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.entities.Planta;
import ifpr.pgua.eic.greenlink.models.entities.Tarefa;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioPlantas;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioTarefas;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class ListarPlantasTarefasJardim implements Initializable {

    private Jardim jardim;
    private RepositorioJardins repoJardins;
    private RepositorioPlantas repoPlantas;
    private RepositorioTarefas repoTarefas;

    @FXML
    private Label lbNome;

    @FXML
    private ListView<Planta> lstPlantas;

    @FXML
    private ListView<Tarefa> lstTarefas;

    public ListarPlantasTarefasJardim(Jardim jardim, RepositorioJardins repoJardins, RepositorioPlantas repoPlantas,
            RepositorioTarefas repoTarefas) {
        this.jardim = jardim;
        this.repoJardins = repoJardins;
        this.repoPlantas = repoPlantas;
        this.repoTarefas = repoTarefas;
    }

    private void marcarFeito(Tarefa tarefa) {
        repoTarefas.marcarFeito(tarefa);
    }

    private void mostraErro(String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg);
        alert.showAndWait();
    }

    private void atualizaListaTarefas() {
        List<Planta> listaPlantas = lstPlantas.getItems();

        ArrayList<Tarefa> listaTarefas = new ArrayList<>();

        for (Planta p : listaPlantas) {
            Resultado<ArrayList<Tarefa>> tarefasResultado = repoTarefas.listaTarefasPlanta(p.getId());

            if (tarefasResultado.foiErro()) {
                mostraErro(tarefasResultado.getMsg());
            }
            
            listaTarefas.addAll(tarefasResultado.comoSucesso().getObj());
        }

        lstTarefas.getItems().clear();
        lstTarefas.getItems().addAll(listaTarefas);
    }

    @FXML
    void atualizarPlanta(MouseEvent event) {
        if (event.getClickCount() > 1) {

            App.changeScreenRegion(
                    "MANTERPLANTA",
                    BorderPaneRegion.CENTER,
                    o -> new ManterPlanta(repoPlantas, repoTarefas, repoJardins, lstPlantas.getSelectionModel().getSelectedItem())
            );

        }
    }

    @FXML
    void atualizarTarefa(MouseEvent event) {
        if (event.getClickCount() > 1) {
            App.changeScreenRegion(
                "MANTERTAREFA", 
                BorderPaneRegion.CENTER,
                o -> new ManterTarefa(repoTarefas, repoPlantas, lstTarefas.getSelectionModel().getSelectedItem())
            );
        }

    }

    @FXML
    void manterJardim(ActionEvent event) {
        App.changeScreenRegion("MANTERJARDIM", BorderPaneRegion.CENTER, o -> new ManterJardim(repoJardins, jardim));
    }

    @FXML
    void voltar(ActionEvent event) {
        App.changeScreenRegion("LISTARJARDINS", BorderPaneRegion.CENTER);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        lbNome.setText(jardim.getNome());

        // gera cellFactory para o lstTarefas
        lstTarefas.setCellFactory(TarefaListCell.geraCellFactory(
            tarefa -> {
                marcarFeito(tarefa);
                atualizaListaTarefas();
            },
            tarefa -> {
                return tarefa.getNome() + " - " + tarefa.getPrazo().toString();
            }
        ));

        // puxa lista de plantas
        Resultado<ArrayList<Planta>> plantasResultado = repoPlantas.listarPlantasJardim(jardim.getId());

        if (plantasResultado.foiErro()) {
            mostraErro(plantasResultado.getMsg());
            return;
        }

        ArrayList<Planta> plantas = plantasResultado.comoSucesso().getObj();

        lstPlantas.getItems().addAll(plantas);

        // cria arraylist de tarefas 
        ArrayList<Tarefa> tarefas = new ArrayList<>();

        for (Planta p : plantas) {
            Resultado<ArrayList<Tarefa>> tarefasResultado = repoTarefas.listaTarefasPlanta(p.getId());

            if (tarefasResultado.foiErro()) {
                mostraErro(tarefasResultado.getMsg());
                return;
            }
            
            tarefas.addAll(tarefasResultado.comoSucesso().getObj());
        }

        // adiciona lista de tarefas para a list view
        lstTarefas.getItems().addAll(tarefas);
    }

}

