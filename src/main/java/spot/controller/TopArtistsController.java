package spot.controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.controlsfx.control.Rating;
import spot.api.SpotifyApi;
import spot.api.model.Token;
import spot.api.model.topartists.Item;
import spot.main.AppMain;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador para la vista de los artistas principales.
 */
public class TopArtistsController implements Initializable {

    @FXML
    private Label SpotMetrics;

    @FXML
    private Label artistname1;

    @FXML
    private Label artistname2;

    @FXML
    private Label artistname3;

    @FXML
    private Label artistname4;

    @FXML
    private Label artistname5;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private ImageView img3;

    @FXML
    private ImageView img4;

    @FXML
    private ImageView img5;

    @FXML
    private Label namesong1;

    @FXML
    private Label namesong2;

    @FXML
    private Label namesong3;

    @FXML
    private Label namesong4;

    @FXML
    private Label namesong5;

    @FXML
    private Rating rating1;

    @FXML
    private Rating rating2;

    @FXML
    private Rating rating3;

    @FXML
    private Rating rating4;

    @FXML
    private Rating rating5;

    @FXML
    private BorderPane view;

    private List<Item> artistas;

    private Token accessToken;

    /**
     * Constructor del controlador de los artistas principales.
     *
     * @param accessToken El token de acceso a la API de Spotify.
     */
    public TopArtistsController(Token accessToken) {
        this.accessToken = accessToken;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TopArtistsView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpotifyApi api = new SpotifyApi();
        api.setToken(accessToken);

        try {
            artistas = api.getTopArtists();

            Label[] artistNames = {artistname1, artistname2, artistname3, artistname4, artistname5};
            Label[] songNames = {namesong1, namesong2, namesong3, namesong4, namesong5};
            Rating[] ratings = {rating1, rating2, rating3, rating4, rating5};
            ImageView[] artistImages = {img1, img2, img3, img4, img5};

            for (int i = 0; i < artistas.size(); i++) {
                if (i < artistNames.length) {
                    Item artist = artistas.get(i);
                    songNames[i].setText(artist.getName());
                    artistNames[i].setText(String.join(", ", artist.getGenres()));
                    setImageViewFromUrl(artistImages[i], artist.getImages().get(0).getUrl());
                    ratings[i].setRating(artist.getPopularity() / 20.0);
                    int finalI = i; // Variable final para usar en la expresión lambda
                    artistImages[i].setOnMouseClicked(event -> {
                        try {
                            // Abrir el enlace en el navegador web predeterminado
                            Desktop.getDesktop().browse(new URI(artist.getExternalUrls().getSpotify()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    });
                    // Cambiar el estado del cursor para que sea seleccionable
                    artistImages[i].setOnMouseEntered(event -> artistImages[finalI].setCursor(Cursor.HAND));
                    artistImages[i].setOnMouseExited(event -> artistImages[finalI].setCursor(Cursor.DEFAULT));
                }

                rating1.setOnMouseClicked(mouseEvent -> rating1.setRating(artistas.get(0).getPopularity() / 20.0));
                rating2.setOnMouseClicked(mouseEvent -> rating2.setRating(artistas.get(1).getPopularity() / 20.0));
                rating3.setOnMouseClicked(mouseEvent -> rating3.setRating(artistas.get(2).getPopularity() / 20.0));
                rating4.setOnMouseClicked(mouseEvent -> rating4.setRating(artistas.get(3).getPopularity() / 20.0));
                rating5.setOnMouseClicked(mouseEvent -> rating5.setRating(artistas.get(4).getPopularity() / 20.0));

                // Aplicar el efecto de sombra a cada ImageView
                DropShadow dropShadow = new DropShadow();
                dropShadow.setRadius(10); // Ajusta el radio de la sombra según lo deseado
                dropShadow.setColor(Color.WHITE); // Ajusta el color de la sombra según lo deseado

                img1.setEffect(dropShadow);
                img2.setEffect(dropShadow);
                img3.setEffect(dropShadow);
                img4.setEffect(dropShadow);
                img5.setEffect(dropShadow);

                addHoverEffect(img1);
                addHoverEffect(img2);
                addHoverEffect(img3);
                addHoverEffect(img4);
                addHoverEffect(img5);

                // Aplicar la fuente a todos los Label
                SpotMetrics.getStyleClass().add("fuentetit");

                artistname1.getStyleClass().add("fuentesub");
                artistname2.getStyleClass().add("fuentesub");
                artistname3.getStyleClass().add("fuentesub");
                artistname4.getStyleClass().add("fuentesub");
                artistname5.getStyleClass().add("fuentesub");

                namesong1.getStyleClass().add("fuente");
                namesong2.getStyleClass().add("fuente");
                namesong3.getStyleClass().add("fuente");
                namesong4.getStyleClass().add("fuente");
                namesong5.getStyleClass().add("fuente");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la vista del controlador.
     *
     * @return La vista del controlador.
     */
    public BorderPane getView() {
        return view;
    }

    /**
     * Establece la imagen del ImageView desde una URL.
     *
     * @param imageView El ImageView para el que se establecerá la imagen.
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
     * Maneja el evento de volver al controlador principal.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onVolver(ActionEvent event) {
        AppMain.getRootController().showMain();
    }

    /**
     * Agrega el efecto de desplazamiento en el cambio de tamaño de las imágenes.
     *
     * @param imageView El ImageView al que se aplicará el efecto.
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
