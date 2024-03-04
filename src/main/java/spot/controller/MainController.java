package spot.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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
    
    @FXML
    private ImageView ImageTopArtists;

    @FXML
    private ImageView ImageTopTracks;
    
    @FXML
    private ImageView ImageSongs;


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
            if (userImageUrl != null && !userImageUrl.isEmpty()) {
                setImageViewFromUrl(UserImage, userImageUrl);
                
                UserImage.setOnMouseClicked(event -> {
                    try {
                        // Abrir el enlace en el navegador web predeterminado
                        Desktop.getDesktop().browse(new URI(spot.getUserUrl()));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            // Manejo de la excepción IOException
            System.err.println("Error al cargar la imagen del usuario");
            // Podrías mostrar un mensaje de error al usuario
        } catch (Exception e) {
            // Manejo de otras excepciones
        	 System.err.println("Error al cargar la imagen del usuario");
        }
        
        // Aplicar el efecto de sombra a cada ImageView
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20); // Ajusta el radio de la sombra según lo deseado
        dropShadow.setColor(Color.GRAY); // Ajusta el color de la sombra según lo deseado

        ImageTopArtists.setEffect(dropShadow);
        ImageTopTracks.setEffect(dropShadow);
        ImageSongs.setEffect(dropShadow);
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
