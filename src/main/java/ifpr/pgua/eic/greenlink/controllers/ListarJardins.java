package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioPlantas;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioTarefas;
import io.github.hugoperlin.navigatorfx.BorderPaneRegion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ListarJardins implements Initializable {
    @FXML
    ListView<Jardim> lstJardins;

    private RepositorioJardins repo;
    private RepositorioPlantas repoPlantas;
    private RepositorioTarefas repoTarefas;

    public ListarJardins(RepositorioJardins repo, RepositorioPlantas repoPlantas, RepositorioTarefas repoTarefas) {
        this.repo = repo;
        this.repoPlantas = repoPlantas;
        this.repoTarefas = repoTarefas;
    }

    public ListarJardins(RepositorioJardins repo) {
        this.repo = repo;
    }

    @FXML
    void cadastrarJardim(ActionEvent e) {
        App.changeScreenRegion("MANTERJARDIM", BorderPaneRegion.CENTER);
        atualizarLista();
    }

    @FXML
    void atualizarJardim(MouseEvent e) {
        if (e.getClickCount() > 1) {

            /* TODO: clique duplo para visualizar plantas e atividades do jardim */

            App.changeScreenRegion(
                    "LISTARPLANTASTAREFASJARDIM",
                    BorderPaneRegion.CENTER,
                    o -> new ListarPlantasTarefasJardim(lstJardins.getSelectionModel().getSelectedItem(), repo, repoPlantas, repoTarefas)
            );

            atualizarLista();
        }
    }

    private void atualizarLista() {
        ArrayList<Jardim> lista = repo.listarJardins().comoSucesso().getObj();
        lstJardins.getItems().clear();
        lstJardins.getItems().addAll(lista);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        // lstJardins.setCellFactory(new Callback<ListView<Jardim>,ListCell<Jardim>>() {
        //     @Override
        //     public ListCell<Jardim> call(ListView<Jardim> arg0) {
        //         // XXX Auto-generated method stub
        //         return new ListCell<Jardim>(){
        //             @Override
        //             protected void updateItem(Jardim jardim, boolean arg1) {
        //                 if (jardim != null) {
        //                     setText(jardim.getNome()+"zzz");

        //                 }
        //             }
        //         };
        //     }
        // });

        ArrayList<Jardim> lista = repo.listarJardins().comoSucesso().getObj();
        lstJardins.getItems().addAll(lista);
    }


}
