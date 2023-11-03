package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class ListarJardins implements Initializable {
    @FXML
    ListView<Jardim> lstJardins;

    private RepositorioJardins repo;

    public ListarJardins(RepositorioJardins repo) {
        this.repo = repo;
    }

    @FXML
    void cadastrarJardim(ActionEvent e) {
        App.changeScreenRegion("MANTERJARDIM", BorderPaneRegion.CENTER);
        atualizarLista();
    }

    @FXML
    void atualizarJardim(MouseEvent e) {
        if (e.getClickCount() > 1) {
            App.changeScreenRegion(
                    "MANTERJARDIM",
                    BorderPaneRegion.CENTER,
                    o -> new ManterJardim(repo, lstJardins.getSelectionModel().getSelectedItem())
            );

            atualizarLista();
        }
    }

    private void atualizarLista() {
        ArrayList<Jardim> lista = repo.listarJardins().comoSucesso().getObj();
        lstJardins.getItems().clear();
        lstJardins.getItems().addAll(lista);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ArrayList<Jardim> lista = repo.listarJardins().comoSucesso().getObj();
        lstJardins.getItems().addAll(lista);
    }


}
