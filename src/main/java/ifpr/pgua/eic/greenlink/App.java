package ifpr.pgua.eic.greenlink;

import ifpr.pgua.eic.greenlink.controllers.ListarJardins;
import ifpr.pgua.eic.greenlink.models.daos.FabricaConexoes;
import ifpr.pgua.eic.greenlink.models.daos.JDBCJardimDAO;
import ifpr.pgua.eic.greenlink.models.daos.JardimDAO;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import io.github.hugoperlin.navigatorfx.BaseAppNavigator;
import io.github.hugoperlin.navigatorfx.ScreenRegistryFXML;

/**
 * JavaFX App
 */

public class App extends BaseAppNavigator {

    private JardimDAO jardimDAO = new JDBCJardimDAO(FabricaConexoes.getInstance());
    private RepositorioJardins repositorioJardins = new RepositorioJardins(jardimDAO);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public String getHome() {
        return "LISTARJARDINS";
    }


    @Override
    public String getAppTitle() {
        return "Greenlink";
    }

    @Override
    public void registrarTelas() {
        registraTela("LISTARJARDINS", new ScreenRegistryFXML(App.class, "listar_jardins.fxml", o -> new ListarJardins(repositorioJardins)));
    }

}
