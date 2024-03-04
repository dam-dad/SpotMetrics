package spot.main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import spot.controller.RootController;

/**
 * Clase principal de la aplicación.
 */
public class AppMain extends Application {

    private static RootController rootController;

    /**
     * Método de inicio de la aplicación JavaFX.
     *
     * @param primaryStage El escenario principal de la aplicación.
     * @throws Exception Posibles excepciones lanzadas durante la inicialización de la aplicación.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        rootController = new RootController(primaryStage);
        primaryStage.setTitle("SpotMetrics");

        // Crear una escena con un Pane como nodo raíz
        Pane rootPane = rootController.getView();
        Scene scene = new Scene(rootPane);

        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image("/images/Spotify-icon.png"));

        // Deshabilitar la capacidad de cambiar el tamaño de la ventana inicialmente
        primaryStage.setResizable(false);

        // Agregar un listener para detectar cambios de escena
        primaryStage.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
                if (newScene != null) {
                    // Cuando se cambia la escena, habilita la capacidad de cambiar el tamaño de la ventana
                    primaryStage.setResizable(true);
                }
            }
        });

        primaryStage.show();
    }

    /**
     * Método estático para obtener el controlador principal.
     *
     * @return El controlador principal de la aplicación.
     */
    public static RootController getRootController() {
        return rootController;
    }

}
