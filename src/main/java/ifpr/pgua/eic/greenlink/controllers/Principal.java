package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.sessao.Sessao;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;

public class Principal implements Initializable {

    @FXML
    Label lbNomeTela;

    @FXML
    private ButtonBar btAcoes;

    @FXML
    void listarAtividades(ActionEvent event) {
        App.changeScreenRegion("LISTARTAREFAS", BorderPaneRegion.CENTER);
        lbNomeTela.setText("Tarefas");
    }

    @FXML
    void listarJardins(ActionEvent event) {
        App.changeScreenRegion("LISTARJARDINS", BorderPaneRegion.CENTER);
        lbNomeTela.setText("Jardins");
    }

    @FXML
    void listarPlantas(ActionEvent event) {
        App.changeScreenRegion("LISTARPLANTAS", BorderPaneRegion.CENTER);
        lbNomeTela.setText("Plantas");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Sessao s = Sessao.getInstance();
        if (s.isLogado()) {
            lbNomeTela.setText("Bem vindo, " + s.getUsuario().getNome() + "!");
        }

        Platform.runLater(() -> listarJardins(new ActionEvent()));
    }
}
