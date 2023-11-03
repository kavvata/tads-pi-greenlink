package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.entities.Planta;
import ifpr.pgua.eic.greenlink.models.entities.Tarefa;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioPlantas;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ManterPlanta implements Initializable {

    private Planta antiga;
    private RepositorioJardins repoJardins;
    private RepositorioPlantas repoPlantas;


    @FXML
    private Button btAcao;

    @FXML
    private Button btRemover;

    @FXML
    private ComboBox<Jardim> cbJardins;

    @FXML
    private ListView<Tarefa> lstTarefas;

    @FXML
    private TextArea taDescricao;

    @FXML
    private TextField tfNome;

    public ManterPlanta(RepositorioPlantas repoPlantas, RepositorioJardins repoJardins) {
        this.repoPlantas = repoPlantas;
        this.repoJardins = repoJardins;
    }

    public ManterPlanta(RepositorioPlantas repoPlantas, RepositorioJardins repoJardins, Planta antiga) {
        this.repoPlantas = repoPlantas;
        this.repoJardins = repoJardins;
        this.antiga = antiga;
    }

    @FXML
    void cadastrar(ActionEvent event) {

    }

    @FXML
    void remover(ActionEvent event) {

    }

    @FXML
    void voltar(ActionEvent event) {
        App.changeScreenRegion("LISTARPLANTAS", BorderPaneRegion.CENTER);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("oi mae");
    }

}
