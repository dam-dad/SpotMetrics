package spot.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import spot.api.SpotifyApi;
import spot.api.model.Token;
import spot.main.AppMain;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ImageView UserImage;

    @FXML
    private BorderPane view;

    private Token accessToken;

    TopTracksController topTracksController;
    TopArtistsController topArtistsController;
    RecommendedTracksController recommendedTracksController;

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
        recommendedTracksController = new RecommendedTracksController(accessToken);

        SpotifyApi spot = new SpotifyApi();

        spot.setToken(accessToken);

        try {

            String userImageUrl = spot.getUserImage();

            setImageViewFromUrl(UserImage, userImageUrl);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UserImage.setOnMouseClicked(event -> {
            try {
                // Abrir el enlace en el navegador web predeterminado
                Desktop.getDesktop().browse(new URI(spot.getUserUrl()));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });


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

    @FXML
    void onRecommended(ActionEvent event) {
        try {
            AppMain.getRootController().updateView(recommendedTracksController.getView());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImageViewFromUrl(ImageView imageView, String imageUrl) {
        try {
            // Cargar la imagen desde la URL
            Image image = new Image(imageUrl);
            // Establecer la imagen en el ImageView
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
