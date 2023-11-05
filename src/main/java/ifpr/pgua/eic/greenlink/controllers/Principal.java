package ifpr.pgua.eic.greenlink.controllers;

import ifpr.pgua.eic.greenlink.App;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;

public class Principal {

    @FXML
    /* TODO */
    Label lbNomeTela;

    @FXML
    private ButtonBar btAcoes;

    @FXML
    void listarAtividades(ActionEvent event) {

    }

    @FXML
    void listarJardins(ActionEvent event) {
        App.changeScreenRegion("LISTARJARDINS", BorderPaneRegion.CENTER);
    }

    @FXML
    void listarPlantas(ActionEvent event) {
        App.changeScreenRegion("LISTARPLANTAS", BorderPaneRegion.CENTER);
    }

}
