package spot.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import spot.api.model.Token;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    @FXML
    private BorderPane view;

    private LoginController loginController;
    private MainController mainController;

    public RootController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginController = new LoginController();
        getView().setCenter(loginController.getView());
    }

    public BorderPane getView() {
        return view;
    }

    public void instanceMainController(Token accessToken) {
        this.mainController = new MainController(accessToken);
    }

    public void showMain() {
        Platform.runLater(() -> updateView(mainController.getView()));
    }

    public void updateView(Node node)  {
        getView().setCenter(node);

        Scene scene = node.getScene();

        // Ajustar el ancho de la escena al prefWidth del nodo
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                node.prefWidth(newValue.doubleValue());
            }
        });

        // Ajustar el alto de la escena al prefHeight del nodo
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                node.prefHeight(newValue.doubleValue());
            }
        });
    }
}
