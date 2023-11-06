package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Planta;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioPlantas;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioTarefas;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class ListarPlantas implements Initializable {

    @FXML
    ListView<Planta> lstPlantas;

    private RepositorioPlantas repoPlantas;
    private RepositorioTarefas repoTarefas;
    private RepositorioJardins repoJardins;



    public ListarPlantas(RepositorioPlantas repoPlantas, RepositorioTarefas repoTarefas, RepositorioJardins repoJardins) {
        this.repoPlantas = repoPlantas;
        this.repoTarefas = repoTarefas;
        this.repoJardins = repoJardins;
    }

    @FXML
    void atualizarPlanta(MouseEvent e) {
        if (e.getClickCount() > 1) {

            App.changeScreenRegion(
                    "MANTERPLANTA",
                    BorderPaneRegion.CENTER,
                    o -> new ManterPlanta(repoPlantas, repoTarefas, repoJardins, lstPlantas.getSelectionModel().getSelectedItem())
            );

            atualizarLista();
        }
    }

    private void atualizarLista() {
        ArrayList<Planta> lista = repoPlantas.listarPlantas().comoSucesso().getObj();
        lstPlantas.getItems().clear();
        lstPlantas.getItems().addAll(lista);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        /* TODO: mostrar nome da planta e nome do jardim na mesma celula */

        Resultado<ArrayList<Planta>> listagemResultado = repoPlantas.listarPlantas();

        if (listagemResultado.foiErro()) {
            Alert alert = new Alert(AlertType.ERROR, listagemResultado.getMsg());
            alert.showAndWait();
            return;
        }

        ArrayList<Planta> lista = listagemResultado.comoSucesso().getObj();
        lstPlantas.getItems().addAll(lista);
    }
    
}
