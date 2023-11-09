package ifpr.pgua.eic.greenlink.controllers;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Usuario;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class Autenticar {

    private RepositorioUsuarios repo;

    public Autenticar(RepositorioUsuarios repo) {
        this.repo = repo;
    }

    @FXML
    private ButtonBar btAcoes;

    @FXML
    private PasswordField pfSenha;

    @FXML
    private TextField tfNome;

    private boolean validaCampos() {
        String nome = tfNome.getText();
        String senha = pfSenha.getText();

        if (nome.isEmpty() || nome.isBlank()) {
            mostraErro("Por favor digite seu nome de usuário.");
            return false;
        }

        if(senha.isBlank() || senha.isEmpty()) {
            mostraErro("Por favor digite sua senha.");
            return false;
        }

        return true;
    }
    private void mostraErro(String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg);
        alert.showAndWait();
    }

    @FXML
    void autenticar(ActionEvent event) {
        if(!validaCampos()) {
            return;
        }

        String nome = tfNome.getText();
        String senha = pfSenha.getText();

        Resultado<Usuario> resultado = repo.autenticaUsuario(nome, senha);
        if (resultado.foiErro()) {
            mostraErro(resultado.getMsg());
            return;
        }

        App.pushScreen("PRINCIPAL");
    }

    @FXML
    void mostrarCadastro(ActionEvent event) {
        App.pushScreen("CADASTRARUSUARIO");
    }

}
