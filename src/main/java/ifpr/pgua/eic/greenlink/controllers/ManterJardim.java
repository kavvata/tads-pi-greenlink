package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

    private boolean isAtualizacao() {
        return antigo != null ? true : false;
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

        if (!validaCampos()) {
            return;
        }

        String nome = tfNome.getText();
        String descricao = taDescricao.getText();

        Resultado<Jardim> resultado;

        if (!isAtualizacao()) {

            /* cadastra novo jardim */

            if (repo.buscarporNome(nome).foiSucesso()) {
                mostraErro("Jardim com nome '" + nome + "' Já cadastrado");
                return;
            }

            resultado = repo.cadastrarJardim(nome, descricao);
        } else {

            /* atualiza jardim ja existente */

            resultado = repo.atualizarJardim(antigo.getId(), nome, descricao);
        }

        if (resultado.foiErro()) {
            mostraErro(resultado.getMsg());
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION, resultado.getMsg());
        alert.showAndWait();

        tfNome.clear();
        taDescricao.clear();
        return;
    }

    @FXML
    void remover(ActionEvent e) {

        ButtonType btSim = new ButtonType("Remover");
        ButtonType btNao = new ButtonType("Cancelar");

        Alert confirmacao = new Alert(
            AlertType.CONFIRMATION, 
            "Tem certeza que deseja remover esse jardim, suas plantas e as tarefas vinculadas à elas?", 
            btNao, 
            btSim
        );

        confirmacao.showAndWait().ifPresent( resposta -> {
            if (resposta == btSim) {
                Resultado<Jardim> resultado = repo.removerJardim(antigo);
                Alert alert = new Alert(AlertType.INFORMATION, resultado.getMsg());

                alert.showAndWait();
                voltar(e);    
            }
        });

        
    }

    @FXML
    void voltar(ActionEvent event) {
        App.changeScreenRegion("LISTARJARDINS", BorderPaneRegion.CENTER);
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (isAtualizacao()) {
            tfNome.setText(antigo.getNome());
            taDescricao.setText(antigo.getDescricao());

            btAcao.setText("Atualizar");
            btRemover.setVisible(true);
        }
    }

}
