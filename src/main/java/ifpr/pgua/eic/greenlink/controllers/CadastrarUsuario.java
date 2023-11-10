package ifpr.pgua.eic.greenlink.controllers;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Usuario;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class CadastrarUsuario {

    RepositorioUsuarios repo;

    @FXML
    private PasswordField pfSenha;

    @FXML
    private PasswordField pfConfirmaSenha;

    @FXML
    private TextField tfNome;

    public CadastrarUsuario(RepositorioUsuarios repo) {
        this.repo = repo;
    }

    private void mostraErro(String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg);
        alert.showAndWait();
    }

    private boolean validaCampos() {
        String nome = tfNome.getText();
        String senha = pfSenha.getText();
        String confirmacao = pfConfirmaSenha.getText();

        if (nome.isEmpty() || nome.isBlank()) {
            mostraErro("Por favor digite seu nome de usuário.");
            return false;
        }

        if (senha.isEmpty() || senha.isBlank()) {
            mostraErro("Por favor digite sua senha.");
            return false;
        }

        if (!senha.equals(confirmacao)) {
            mostraErro("Senhas não coincidem. Certifique-se que foi digitado a mesma senha.");
            return false;
        }
        
        return true;
    }

    @FXML
    void cadastrar(ActionEvent event) {
        if (!validaCampos()) {
            return;
        }

        String nome = tfNome.getText();
        String senha = pfSenha.getText();

        Resultado<Usuario> resultado = repo.cadastrarUsuario(nome, senha);

        if (resultado.foiErro()) {
            mostraErro(resultado.getMsg());
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION, resultado.getMsg());
        alert.showAndWait();

        App.popScreen();
        return;
    }

    @FXML
    void voltar(ActionEvent event) {
        App.popScreen();
    }

}

