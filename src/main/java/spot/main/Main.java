package spot.main;

import javafx.application.Application;

/**
 * Clase principal que lanza la aplicación JavaFX.
 */
public class Main {

    /**
     * Método principal que lanza la aplicación JavaFX.
     *
     * @param args Argumentos de la línea de comandos.
     * @throws Exception Posibles excepciones lanzadas durante la inicialización de la aplicación.
     */
    public static void main(String[] args) throws Exception {
        // Lanzar la aplicación JavaFX a través de la clase AppMain
        Application.launch(AppMain.class, args);
    }

}
