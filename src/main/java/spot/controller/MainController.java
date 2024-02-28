package spot.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import spot.api.model.Token;
import spot.main.AppMain;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Token accessToken;

    TopTracksController topTracksController;
    TopArtistsController topArtistsController;

    @FXML
    private BorderPane view;

    public MainController(Token accessToken) {
        this.accessToken = accessToken;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        topTracksController = new TopTracksController(accessToken);
        topArtistsController = new TopArtistsController(accessToken);

    }

    public BorderPane getView() {
        return view;
    }

    @FXML
    void onTopArtists(ActionEvent event) {
        try {
            AppMain.getRootController().updateView(topArtistsController.getView());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onTopTracks(ActionEvent event) {
        try {
            AppMain.getRootController().updateView(topTracksController.getView());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
