package ifpr.pgua.eic.greenlink.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ifpr.pgua.eic.greenlink.App;
import ifpr.pgua.eic.greenlink.models.entities.Jardim;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class ListarJardins implements Initializable {
    @FXML
    ListView<Jardim> lstJardins;

    private RepositorioJardins repo;

    public ListarJardins(RepositorioJardins repo) {
        this.repo = repo;
    }

    @FXML
    void cadastrarJardim(ActionEvent e) {
        App.pushScreen("MANTERJARDIM");
        /* FIXME: lista atualizando antes de retornar para essa tela */
        atualizarLista();
    }

    @FXML
    void atualizarJardim(MouseEvent e) {
        if (e.getClickCount() > 1) {
            App.pushScreen("MANTERJARDIM",
                    o -> new ManterJardim(repo, lstJardins.getSelectionModel().getSelectedItem()));

            /* FIXME: lista atualizando antes de retornar para essa tela */
            atualizarLista();
        }
    }

    private void atualizarLista() {
        ArrayList<Jardim> lista = repo.listarJardins().comoSucesso().getObj();
        lstJardins.getItems().clear();
        lstJardins.getItems().addAll(lista);

        System.out.println("Lista atualizada:");
        for(Jardim j : lista) {
            System.out.println("nome: " + j);
            System.out.println("descricao: " + j.getDescricao());
            System.out.println("---");
        }

        System.out.println();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ArrayList<Jardim> lista = repo.listarJardins().comoSucesso().getObj();
        lstJardins.getItems().addAll(lista);
    }


}
