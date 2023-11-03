package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class ManterJardim implements Initializable {

    /* NOTE:
     *
     * Se 'antigo' for instancializado, essa tela vai funcionar 
     * como uma tela de atualizacao de uma entidade ja cadastrada.
     * 
     * Se nao, essa tela vai funcionar como uma tela de cadastro.
     */


    private Jardim antigo;

    private RepositorioJardins repo;

    @FXML
    private TextArea taDescricao;

    @FXML
    private Button btAcao;

    @FXML
    /* TODO */
    private Button btRemover;

    @FXML
    private TextField tfNome;

    public ManterJardim(RepositorioJardins repo) {
        this.repo = repo;
    }

    public ManterJardim(RepositorioJardins repo, Jardim antigo) {
        this.repo = repo;
        this.antigo = antigo;
    }

    @FXML
    void cadastrar(ActionEvent event) {

        if (!camposSaoValidos()) {
            return;
        }

        String nome = tfNome.getText();
        String descricao = taDescricao.getText();
        Alert alert;

        Resultado<Jardim> resultado;

        if (antigo != null) {
            resultado = repo.atualizarJardim(antigo.getId(), nome, descricao);
        } else {

            if (repo.buscarporNome(nome).foiSucesso()) {
                alert = new Alert(AlertType.ERROR, "Nome '" + nome + "' Já cadastrado");
                alert.showAndWait();
                return;
            }

            resultado = repo.cadastrarJardim(nome, descricao);
        }

        if (resultado.foiErro()) {
            alert = new Alert(AlertType.ERROR, resultado.getMsg());
            alert.showAndWait();
            return;
        }

        alert = new Alert(AlertType.INFORMATION, resultado.getMsg());
        alert.showAndWait();

        tfNome.clear();
        taDescricao.clear();
        return;
    }

    @FXML
    void voltar(ActionEvent event) {
        App.popScreen();
    }

    private boolean camposSaoValidos() {
        String nome = tfNome.getText();
        Alert alert;

        if (nome.isEmpty() || nome.isBlank()) {
            alert = new Alert(AlertType.ERROR, "Nome inválido.");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (antigo != null) {
            tfNome.setText(antigo.getNome());
            taDescricao.setText(antigo.getDescricao());

            btAcao.setText("Atualizar");
        }
    }

}
