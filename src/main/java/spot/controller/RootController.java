package spot.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import spot.api.model.Token;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador raíz que gestiona la navegación entre las diferentes vistas de la aplicación.
 */
public class RootController implements Initializable {

    @FXML
    private BorderPane view;

    private LoginController loginController;
    private MainController mainController;
    private Stage primaryStage;

    /**
     * Constructor del controlador raíz.
     *
     * @param primaryStage El escenario principal de la aplicación.
     */
    public RootController(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
            loader.setController(this);
            loader.load();
            this.primaryStage = primaryStage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginController = new LoginController();
        getView().setCenter(loginController.getView());
    }

    /**
     * Obtiene el panel raíz de la aplicación.
     *
     * @return El panel raíz.
     */
    public BorderPane getView() {
        return view;
    }

    /**
     * Instancia el controlador principal con el token de acceso proporcionado.
     *
     * @param accessToken El token de acceso a la API de Spotify.
     */
    public void instanceMainController(Token accessToken) {
        this.mainController = new MainController(accessToken);
    }

    /**
     * Muestra la vista principal de la aplicación.
     */
    public void showMain() {
        Platform.runLater(() -> updateView(mainController.getView()));
    }

    /**
     * Actualiza la vista actual del panel raíz con el nodo proporcionado.
     *
     * @param node El nodo que se mostrará en la vista.
     */
    public void updateView(Node node) {
        getView().setCenter(node);

        Scene scene = node.getScene();
        if (scene != null) {
            Stage stage = (Stage) scene.getWindow();
            if (stage != null) {
                // Habilitar la capacidad de cambiar el tamaño de la ventana
                stage.setResizable(true);

                // Habilitar la pantalla completa
                stage.setFullScreen(true);
            }
        }
    }

}
