package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.sessao.Sessao;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class Principal implements Initializable {

    private Map<String, String> nomeTelas;

    @FXML
    private BorderPane rootPrincipal;

    @FXML
    private Label lbNomeTela;


    public Principal() {
        registraNomeTelas();
    }

    private void registraNomeTelas() {
        nomeTelas = new HashMap<>();

        nomeTelas.put("rootListarJardins", "Listar jardins");
        nomeTelas.put("rootListarPlantas", "Listar plantas");
        nomeTelas.put("rootListarTarefas", "Listar tarefas");
        nomeTelas.put("rootPlantasTarefasJardim", "Plantas e Tarefas");
        nomeTelas.put("rootManterJardim", "Manter jardim");
        nomeTelas.put("rootManterPlanta", "Manter planta");
        nomeTelas.put("rootManterTarefa", "Manter tarefa");
    }

    @FXML
    void listarAtividades(ActionEvent event) {
        App.changeScreenRegion("LISTARTAREFAS", BorderPaneRegion.CENTER);
    }

    @FXML
    void listarJardins(ActionEvent event) {
        App.changeScreenRegion("LISTARJARDINS", BorderPaneRegion.CENTER);
    }

    @FXML
    void listarPlantas(ActionEvent event) {
        App.changeScreenRegion("LISTARPLANTAS", BorderPaneRegion.CENTER);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Sessao s = Sessao.getInstance();
        if (s.isLogado()) {
            lbNomeTela.setText("Bem vindo, " + s.getUsuario().getNome() + "!");
        }

        /*
         * Handler que atualiza o indicador de qual tela está sendo mostrada
         */

        EventHandler<Event> handlerMudaLabel = event -> {
            String id = rootPrincipal.getCenter().getId();

            if (!nomeTelas.containsKey(id)) {
                return;
            }

            lbNomeTela.setText(nomeTelas.get(id));
        };

        rootPrincipal.addEventHandler(ActionEvent.ACTION, handlerMudaLabel);
        rootPrincipal.addEventHandler(MouseEvent.ANY, handlerMudaLabel);

        Platform.runLater(() -> listarJardins(new ActionEvent()));
    }
}
