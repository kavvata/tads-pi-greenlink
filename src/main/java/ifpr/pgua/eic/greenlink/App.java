package ifpr.pgua.eic.greenlink;

import io.github.hugoperlin.navigatorfx.BaseAppNavigator;

/**
 * JavaFX App
 */

public class App extends BaseAppNavigator {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public String getHome() {
        return "PRINCIPAL";
    }


    @Override
    public String getAppTitle() {
        return "Aplicativo de Tarefas!";
    }

    @Override
    public void registrarTelas() {
    }

}
