package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Planta;
import ifpr.pgua.eic.greenlink.models.entities.Tarefa;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioPlantas;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioTarefas;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class ManterTarefa implements Initializable {
    private String telaAnterior = "LISTARTAREFAS";

    private Tarefa antiga;

    private RepositorioTarefas repoTarefas;
    private RepositorioPlantas repoPlantas;

    @FXML
    private Button btAcao;

    @FXML
    private Button btRemover;

    @FXML
    private ComboBox<Planta> cbPlantas;

    @FXML
    private DatePicker dpPrazo;

    @FXML
    private TextArea taDescricao; 

    @FXML
    private TextField tfNome;

    public ManterTarefa(RepositorioTarefas repoTarefas, RepositorioPlantas repoPlantas) {
        this.repoTarefas = repoTarefas;
        this.repoPlantas = repoPlantas;
    }

    public ManterTarefa(RepositorioTarefas repoTarefas, RepositorioPlantas repoPlantas, Tarefa antiga) {
        this.repoTarefas = repoTarefas;
        this.repoPlantas = repoPlantas;
        this.antiga = antiga;
    }

    public void setTelaAnterior(String telaAnterior) {
        this.telaAnterior = telaAnterior;
    }

    private boolean isAtualizacao() {
        return antiga != null ? true : false;
    }

    private void mostraErro(String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg);
        alert.showAndWait();
    }

    private boolean validaCampos() {
        String nome = tfNome.getText();

        if (nome.isEmpty() || nome.isBlank()) {
            mostraErro("Nome inválido.");
            return false;
        }

        return true;

    }

    @FXML
    void handleBtAcao(ActionEvent event) {

        if(!validaCampos()) {
            if (dpPrazo.getValue() == null) {
                mostraErro("Data inválida.");
            }
            return;
        }

        String nome = tfNome.getText();
        String descricao = taDescricao.getText();
        Planta planta = cbPlantas.getSelectionModel().getSelectedItem();
        LocalDate prazo = dpPrazo.getValue();

        Resultado<Tarefa> resultado;

        if (!isAtualizacao()) {
            resultado = repoTarefas.cadastrarTarefa(nome, descricao, planta, prazo);
        } else {
            resultado = repoTarefas.atualizarTarefa(antiga.getId(), nome, descricao, planta, prazo, antiga.isFeito());
        }

        Alert alert = new Alert(AlertType.INFORMATION, resultado.getMsg());
        alert.showAndWait();

        tfNome.clear();
        taDescricao.clear();
    }

    @FXML
    void remover(ActionEvent event) {
        ButtonType btSim = new ButtonType("Remover");
        ButtonType btNao = new ButtonType("Cancelar");

        Alert confirmacao = new Alert(
            AlertType.CONFIRMATION, 
            "Tem certeza que deseja remover essa tarefa?", 
            btNao, 
            btSim
        );

        confirmacao.showAndWait().ifPresent( resposta -> {
            if (resposta == btSim) {
                Resultado<Tarefa> resultado = repoTarefas.removerTarefa(antiga);
                Alert alert = new Alert(AlertType.INFORMATION, resultado.getMsg());

                alert.showAndWait();
                voltar(event);    
            }
        });

    }

    @FXML
    void voltar(ActionEvent event) {
        App.changeScreenRegion(telaAnterior, BorderPaneRegion.CENTER);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Resultado<ArrayList<Planta>> listagemResultado = repoPlantas.listarPlantas();

        if (listagemResultado.foiErro()) {
            mostraErro(listagemResultado.getMsg());
            return;
        }

        ArrayList<Planta> plantas = listagemResultado.comoSucesso().getObj();

        if (plantas.size() == 0) {
            mostraErro("Para cadastrar uma tarefa, é necessário possuir pelo menos uma planta cadastrada.");
            App.pushScreen("PRINCIPAL");
            return;
        }

        cbPlantas.getItems().addAll(plantas);

        if(isAtualizacao()) {
            tfNome.setText(antiga.getNome());
            taDescricao.setText(antiga.getDescricao());
            dpPrazo.setValue(antiga.getPrazo());

            for (int i = 0; i < plantas.size(); i++) {
                if (plantas.get(i).getId() == antiga.getPlanta().getId()) {
                    cbPlantas.getSelectionModel().select(i);
                    break;
                }
            }

            btAcao.setText("Atualizar");
            btRemover.setVisible(true);
        } else {
            cbPlantas.getSelectionModel().select(0);
            dpPrazo.setValue(LocalDate.now());
        }
    }
}