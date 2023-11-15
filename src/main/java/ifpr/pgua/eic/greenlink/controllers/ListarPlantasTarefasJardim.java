package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.application.Platform;
import javafx.concurrent.Task;
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
    private Label lbPlaceholderPlantas;

    @FXML
    private Label lbPlaceholderTarefas;

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
        ArrayList<Tarefa> listaTarefas = new ArrayList<>();

        Resultado<ArrayList<Tarefa>> tarefasResultado = repoTarefas.listaTarefasJardim(jardim.getId());

        if (tarefasResultado.foiErro()) {
            mostraErro(tarefasResultado.getMsg());
        } else {
            listaTarefas.addAll(tarefasResultado.comoSucesso().getObj());
        }

        if (listaTarefas.size() == 0) {
            lbPlaceholderTarefas.setText("+ Clique duplo para criar uma nova Tarefa!");
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
                    o -> {
                        ManterPlanta m = new ManterPlanta(repoPlantas, repoTarefas, repoJardins, lstPlantas.getSelectionModel().getSelectedItem());
                        m.setTelaAnterior("LISTARPLANTASTAREFASJARDIM");
                        return m;
                    }
            );

        }
    }

    @FXML
    void atualizarTarefa(MouseEvent event) {
        if (event.getClickCount() > 1) {
            App.changeScreenRegion(
                "MANTERTAREFA", 
                BorderPaneRegion.CENTER,
                o -> {
                    ManterTarefa m = new ManterTarefa(repoTarefas, repoPlantas, 
                        lstTarefas.getSelectionModel().getSelectedItem());

                    m.setTelaAnterior("LISTARPLANTASTAREFASJARDIM");
                    return m;
                }
            );
        }

    }

    @FXML
    void manterJardim(ActionEvent event) {
        App.changeScreenRegion("MANTERJARDIM", BorderPaneRegion.CENTER, o -> {
            ManterJardim controller = new ManterJardim(repoJardins, jardim);
            controller.setTelaAnterior("LISTARPLANTASTAREFASJARDIM");
            return controller;
        });
    }

    @FXML
    void voltar(ActionEvent event) {
        App.changeScreenRegion("LISTARJARDINS", BorderPaneRegion.CENTER);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // gera cellFactory para o lstTarefas
        lstTarefas.setCellFactory(TarefaListCell.geraCellFactory(
            tarefa -> {
                marcarFeito(tarefa);
                atualizaListaTarefas();
            },
            tarefa -> {
                String str = "";
                str += tarefa.getNome() + " - ";
                str += tarefa.getPlanta().getNome() + " - ";
                str += "<" + tarefa.getPrazo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ">";

                return str;
            }
        ));

        Task<Void> task = new Task<Void>() {

            /* task para carregar listas em outra thread */
            @Override
            protected Void call() throws Exception {

                // puxa lista de plantas
                Resultado<ArrayList<Planta>> plantasResultado = repoPlantas.listarPlantasJardim(jardim.getId());

                if (plantasResultado.foiErro()) {
                    mostraErro(plantasResultado.getMsg());
                    return null;
                }

                ArrayList<Planta> plantas = plantasResultado.comoSucesso().getObj();
                if (plantas.size() == 0) {
                    Platform.runLater(() -> lbPlaceholderPlantas.setText("+ Clique duplo para criar uma nova Planta!"));
                }
                lstPlantas.getItems().addAll(plantas);

                // cria arraylist de tarefas
                ArrayList<Tarefa> tarefas = new ArrayList<>();

                for (Planta p : plantas) {
                    Resultado<ArrayList<Tarefa>> tarefasResultado = repoTarefas.listaTarefasPlanta(p.getId());

                    if (tarefasResultado.foiErro()) {
                        mostraErro(tarefasResultado.getMsg());
                        return null;
                    }

                    tarefas.addAll(tarefasResultado.comoSucesso().getObj());
                }

                if (tarefas.size() == 0) {
                    Platform.runLater(() -> lbPlaceholderTarefas.setText("+ Clique duplo para criar uma nova Tarefa!"));
                }

                // adiciona lista de tarefas para a list view
                lstTarefas.getItems().addAll(tarefas);
                return null;
            }
            
        };

        lbNome.setText(jardim.getNome());
        new Thread(task).start();
    }

}

