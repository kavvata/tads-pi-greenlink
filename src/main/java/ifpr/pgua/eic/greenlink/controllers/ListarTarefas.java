package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Tarefa;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioPlantas;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioTarefas;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class ListarTarefas implements Initializable {

    private RepositorioTarefas repoTarefas;
    private RepositorioPlantas repoPlantas;


    @FXML
    private ListView<Tarefa> lstTarefas;

    @FXML
    private Label lbPlaceholder;

    public ListarTarefas(RepositorioTarefas repoTarefas, RepositorioPlantas repoPlantas) {
        this.repoTarefas = repoTarefas;
        this.repoPlantas = repoPlantas;
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

    private void atualizarLista() {
        ArrayList<Tarefa> lista = repoTarefas.listarTarefas().comoSucesso().getObj();
        lstTarefas.getItems().clear();
        lstTarefas.getItems().addAll(lista);
    }

    private void marcarFeito(Tarefa tarefa) {
        repoTarefas.marcarFeito(tarefa);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        /*
         * criação de cells com checkbox interagível e string do 
         * conteudo mostrado na cell
         */
        lstTarefas.setCellFactory(TarefaListCell.geraCellFactory(
            tarefa -> {
                marcarFeito(tarefa);
                atualizarLista();
            },
            tarefa -> {
                String str = "";
                str += tarefa.getNome() + " - ";
                str += tarefa.getPlanta().getNome() + " - ";
                str += tarefa.getPrazo().toString();

                return str;
            }
        ));

        /* task do javafx para inicializar a listview em outra thread */
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                /* gera lista de tarefas */
                Resultado<ArrayList<Tarefa>> listagemResultado = repoTarefas.listarTarefas();
                if (listagemResultado.foiErro()) {
                    Alert alert = new Alert(AlertType.ERROR, listagemResultado.getMsg());
                    alert.showAndWait();
                    return null;
                }


                ArrayList<Tarefa> lista = listagemResultado.comoSucesso().getObj();
                /* muda o texto de 'carregando' para o de lista vazia */
                if (lista.size() == 0) {
                    Platform.runLater(() -> lbPlaceholder.setText("+ Clique duplo para criar nova tarefa!"));
                }
                lstTarefas.getItems().addAll(lista);
                return null;
            }
        };

        new Thread(task).start();
    }

}

