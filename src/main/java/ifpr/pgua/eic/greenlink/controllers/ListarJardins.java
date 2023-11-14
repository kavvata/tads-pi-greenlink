package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioPlantas;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioTarefas;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class ListarJardins implements Initializable {
    @FXML
    ListView<Jardim> lstJardins;

    @FXML
    Label lbPlaceholder;

    private RepositorioJardins repo;
    private RepositorioPlantas repoPlantas;
    private RepositorioTarefas repoTarefas;

    public ListarJardins(RepositorioJardins repo, RepositorioPlantas repoPlantas, RepositorioTarefas repoTarefas) {
        this.repo = repo;
        this.repoPlantas = repoPlantas;
        this.repoTarefas = repoTarefas;
    }

    public ListarJardins(RepositorioJardins repo) {
        this.repo = repo;
    }

    @FXML
    void cadastrarJardim(ActionEvent e) {
        App.changeScreenRegion("MANTERJARDIM", BorderPaneRegion.CENTER);
    }

    @FXML
    void listarPlantasTarefas(MouseEvent e) {
        Jardim jardim;

        if (e.getClickCount() > 1) {

            jardim = lstJardins.getSelectionModel().getSelectedItem();

            if (jardim == null) {
                cadastrarJardim(null);
                return;
            }

            App.changeScreenRegion(
                    "LISTARPLANTASTAREFASJARDIM",
                    BorderPaneRegion.CENTER,
                    o -> new ListarPlantasTarefasJardim(jardim, repo, repoPlantas, repoTarefas)
            );
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        /* task do javafx para inicializar a listview em outra thread */
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                Resultado<ArrayList<Jardim>> listagemResultado = repo.listarJardins();

                if (listagemResultado.foiErro()) {
                    System.out.println(listagemResultado.getMsg());
                    return null;
                }

                ArrayList<Jardim> lista = listagemResultado.comoSucesso().getObj();
                /* muda o texto de 'carregando' para o de lista vazia */
                if (lista.size() == 0) {
                    Platform.runLater(() -> lbPlaceholder.setText("+ Clique duplo para criar novo jardim!"));;
                }
                lstJardins.getItems().addAll(lista);
                return null;
            }

        };

        new Thread(task).start();
    }


}
