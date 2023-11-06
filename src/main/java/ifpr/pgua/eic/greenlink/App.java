package ifpr.pgua.eic.greenlink;

import ifpr.pgua.eic.greenlink.controllers.ListarJardins;
import ifpr.pgua.eic.greenlink.controllers.ListarPlantas;
import ifpr.pgua.eic.greenlink.controllers.ListarTarefas;
import ifpr.pgua.eic.greenlink.controllers.ManterJardim;
import ifpr.pgua.eic.greenlink.controllers.ManterPlanta;
import ifpr.pgua.eic.greenlink.controllers.ManterTarefa;
import ifpr.pgua.eic.greenlink.controllers.Principal;
import ifpr.pgua.eic.greenlink.models.daos.FabricaConexoes;
import ifpr.pgua.eic.greenlink.models.daos.JDBCJardimDAO;
import ifpr.pgua.eic.greenlink.models.daos.JDBCPlantaDAO;
import ifpr.pgua.eic.greenlink.models.daos.JDBCTarefaDAO;
import ifpr.pgua.eic.greenlink.models.daos.JardimDAO;
import ifpr.pgua.eic.greenlink.models.daos.PlantaDAO;
import ifpr.pgua.eic.greenlink.models.daos.TarefaDAO;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioJardins;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioPlantas;
import ifpr.pgua.eic.greenlink.models.repositories.RepositorioTarefas;
import io.github.hugoperlin.navigatorfx.BaseAppNavigator;
import io.github.hugoperlin.navigatorfx.ScreenRegistryFXML;

/**
 * JavaFX App
 */

public class App extends BaseAppNavigator {

    private JardimDAO jardimDAO = new JDBCJardimDAO(FabricaConexoes.getInstance());
    private RepositorioJardins repositorioJardins = new RepositorioJardins(jardimDAO);

    private PlantaDAO plantaDAO = new JDBCPlantaDAO(FabricaConexoes.getInstance());
    private RepositorioPlantas repositorioPlantas = new RepositorioPlantas(plantaDAO, jardimDAO);

    private TarefaDAO tarefaDAO = new JDBCTarefaDAO(FabricaConexoes.getInstance());
    private RepositorioTarefas repositorioTarefas = new RepositorioTarefas(tarefaDAO, plantaDAO);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public String getHome() {
        return "PRINCIPAL";
    }


    @Override
    public String getAppTitle() {
        return "Greenlink";
    }

    @Override
    public void registrarTelas() {
        registraTela("PRINCIPAL", new ScreenRegistryFXML(App.class, "principal.fxml", o -> new Principal()));

        registraTela("LISTARJARDINS", new ScreenRegistryFXML(App.class, "listar_jardins.fxml", o -> new ListarJardins(repositorioJardins)));
        registraTela("MANTERJARDIM", new ScreenRegistryFXML(App.class, "manter_jardim.fxml", o -> new ManterJardim(repositorioJardins)));

        registraTela("LISTARPLANTAS", new ScreenRegistryFXML(App.class, "listar_plantas.fxml", o -> new ListarPlantas(repositorioPlantas, repositorioJardins)));
        registraTela("MANTERPLANTA", new ScreenRegistryFXML(App.class, "manter_planta.fxml", o -> new ManterPlanta(repositorioPlantas, repositorioJardins)));

        registraTela("LISTARTAREFAS", new ScreenRegistryFXML(App.class, "listar_tarefas.fxml", o -> new ListarTarefas(repositorioTarefas, repositorioPlantas)));
        registraTela("MANTERTAREFA", new ScreenRegistryFXML(App.class, "manter_tarefa.fxml", o -> new ManterTarefa(repositorioTarefas, repositorioPlantas)));
    }

}
