package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class ListarJardins implements Initializable {
    @FXML
    ListView<Jardim> lstJardins;

    private RepositorioJardins repo;

    public ListarJardins(RepositorioJardins repo) {
        this.repo = repo;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            ArrayList<Jardim> lista = repo.listarJardins();
            lstJardins.getItems().addAll(lista);
        } catch (RuntimeException e) {

        }
    }


}
