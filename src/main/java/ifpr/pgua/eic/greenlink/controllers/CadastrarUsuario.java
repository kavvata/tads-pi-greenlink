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

    private boolean senhasSaoIguais() {
        String senha = pfSenha.getText();
        String confirmacao = pfConfirmaSenha.getText();

        return senha.equals(confirmacao) ? true : false;
    }
    @FXML
    void cadastrar(ActionEvent event) {
        if (!senhasSaoIguais()) {
            mostraErro("Senhas não coincidem.");
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

