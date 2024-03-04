package spot.controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import spot.api.SpotifyApi;
import spot.api.model.Token;
import spot.main.AppMain;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la vista principal de la aplicación.
 */
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

    private TopTracksController topTracksController;
    private TopArtistsController topArtistsController;
    private RecommendedTracksController recommendedTracksController;

    /**
     * Constructor del controlador principal.
     *
     * @param accessToken Token de acceso a la API de Spotify.
     */
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

        addHoverEffect(ImageTopArtists);
        addHoverEffect(ImageTopTracks);
        addHoverEffect(ImageSongs);

        ImageTopArtists.setEffect(dropShadow);
        ImageTopTracks.setEffect(dropShadow);
        ImageSongs.setEffect(dropShadow);

        ImageTopTracks.setOnMouseClicked(event -> {
            AppMain.getRootController().updateView(topTracksController.getView());
        });

        ImageTopArtists.setOnMouseClicked(event -> {
            AppMain.getRootController().updateView(topArtistsController.getView());
        });

        ImageSongs.setOnMouseClicked(event -> {
            AppMain.getRootController().updateView(recommendedTracksController.getView());
        });
    }

    /**
     * Obtiene la vista asociada al controlador.
     *
     * @return La vista del controlador.
     */
    public BorderPane getView() {
        return view;
    }

    /**
     * Establece la imagen en un ImageView desde una URL.
     *
     * @param imageView El ImageView donde se establecerá la imagen.
     * @param imageUrl  La URL de la imagen.
     */
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

    /**
     * Agrega el efecto de hover a un ImageView.
     *
     * @param imageView El ImageView al que se agregará el efecto de hover.
     */
    private void addHoverEffect(ImageView imageView) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), imageView);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), imageView);
        translateTransition.setToY(-10);

        imageView.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
            translateTransition.playFromStart();
        });

        imageView.setOnMouseExited(event -> {
            scaleTransition.stop();
            translateTransition.stop();
            imageView.setScaleX(1.0);
            imageView.setScaleY(1.0);
            imageView.setTranslateY(0);
        });
    }
}
