package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.entities.Planta;
import ifpr.pgua.eic.greenlink.models.entities.Tarefa;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class ManterPlanta implements Initializable {

    private String chaveTelaAnterior = "LISTARPLANTAS"; /* TODO */

    private Planta antiga;
    private RepositorioJardins repoJardins;
    private RepositorioPlantas repoPlantas;
    private RepositorioTarefas repoTarefas;


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

    public ManterPlanta(RepositorioPlantas repoPlantas, RepositorioTarefas repoTarefas, RepositorioJardins repoJardins, Planta antiga) {
        this.repoPlantas = repoPlantas;
        this.repoTarefas = repoTarefas;
        this.repoJardins = repoJardins;
        this.antiga = antiga;
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

    private void marcarFeito(Tarefa tarefa) {
        repoTarefas.marcarFeito(tarefa);
    }

    private void atualizarLista() {
        Resultado<ArrayList<Tarefa>> tarefasResultado = repoTarefas.listaTarefasPlanta(antiga.getId());

        if (tarefasResultado.foiErro()) {
            mostraErro(tarefasResultado.getMsg());
        }

        lstTarefas.getItems().clear();
        lstTarefas.getItems().addAll(tarefasResultado.comoSucesso().getObj());

    }

    @FXML
    void handleBtAcao(ActionEvent event) {

        if(!validaCampos()) {
            return;
        }

        String nome = tfNome.getText();
        Jardim jardim = cbJardins.getSelectionModel().getSelectedItem();
        String descricao = taDescricao.getText();

        Resultado<Planta> resultado;

        if (!isAtualizacao()) {

            /* cadastra nova planta */

            if (repoPlantas.buscarPorNome(nome).foiSucesso()) {
                mostraErro("Planta com nome '" + nome + "' Já cadastrada");
                return;
            }

            resultado = repoPlantas.cadastrarPlanta(nome, descricao, jardim);
        } else {

            /* atualiza planta ja existente */

            resultado = repoPlantas.atualizarPlanta(antiga.getId(), nome, descricao, jardim);
        }

        if (resultado.foiErro()) {
            mostraErro(resultado.getMsg());
            return;
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
            "Tem certeza que deseja remover esse jardim, suas plantas e as tarefas vinculadas à elas?", 
            btNao, 
            btSim
        );

        confirmacao.showAndWait().ifPresent( resposta -> {
            if (resposta == btSim) {
                Resultado<Planta> resultado = repoPlantas.removerPlanta(antiga);
                Alert alert = new Alert(AlertType.INFORMATION, resultado.getMsg());

                alert.showAndWait();
                voltar(event);    
            }
        });
    }

    @FXML
    void atualizarTarefa(MouseEvent event) {
        System.out.println("atualizarTarefa nao implementado!");
    }

    @FXML
    void voltar(ActionEvent event) {
        /* TODO: lembrar qual foi a ultima tela utilizada */
        App.changeScreenRegion(chaveTelaAnterior, BorderPaneRegion.CENTER);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Resultado<ArrayList<Jardim>> jardimResultado = repoJardins.listarJardins();

        if (jardimResultado.foiErro()) {
            mostraErro(jardimResultado.getMsg());
            return;
        }

        ArrayList<Jardim> listaJardim = jardimResultado.comoSucesso().getObj();
        cbJardins.getItems().addAll(listaJardim);

        if (isAtualizacao()) {
            tfNome.setText(antiga.getNome());
            taDescricao.setText(antiga.getDescricao());


            /* auto-seleciona o jardim da planta antiga. */
            for (int i = 0; i < listaJardim.size(); i++) {
                if (listaJardim.get(i).getId() == antiga.getJardim().getId()) {
                    cbJardins.getSelectionModel().select(i);
                    break;
                }
            }

            Resultado<ArrayList<Tarefa>> tarefasResultado = repoTarefas.listaTarefasPlanta(antiga.getId());

            if (tarefasResultado.foiErro()) {
                mostraErro(tarefasResultado.getMsg());
            }

            lstTarefas.getItems().addAll(tarefasResultado.comoSucesso().getObj());

            lstTarefas.setCellFactory(TarefaListCell.geraCellFactory(
                t -> {
                    marcarFeito(t);
                    atualizarLista();
                },
                t -> {
                    return t.getNome() + " - " + t.getPrazo().toString();
                }
            ));

            btAcao.setText("Atualizar");
            btRemover.setVisible(true);
            lstTarefas.setVisible(true);
        } else {
            cbJardins.getSelectionModel().select(0);
        }
    }
}
