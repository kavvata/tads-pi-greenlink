package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Planta;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioPlantas;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class ListarPlantas implements Initializable {

    @FXML
    ListView<Planta> lstPlantas;

    private RepositorioPlantas repoPlantas;
    private RepositorioJardins repoJardins;



    public ListarPlantas(RepositorioPlantas repoPlantas, RepositorioJardins repoJardins) {
        this.repoPlantas = repoPlantas;
        this.repoJardins = repoJardins;
    }

    @FXML
    void atualizarPlanta(MouseEvent e) {
        if (e.getClickCount() > 1) {

            App.changeScreenRegion(
                    "MANTERPLANTA",
                    BorderPaneRegion.CENTER,
                    o -> new ManterPlanta(repoPlantas, repoJardins, lstPlantas.getSelectionModel().getSelectedItem())
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
        ArrayList<Planta> lista = repoPlantas.listarPlantas().comoSucesso().getObj();
        lstPlantas.getItems().addAll(lista);
    }
    
}
