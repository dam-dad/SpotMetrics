package spot.controller;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import spot.api.SpotifyApi;
import spot.api.model.recommendations.Track;
import spot.api.model.toptracks.Item;
import spot.api.model.Token;
import spot.main.AppMain;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador para la vista de las canciones recomendadas.
 */
public class RecommendedTracksController implements Initializable {

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
    private HBox background1;

    @FXML
    private HBox background2;

    @FXML
    private GridPane background3;

    @FXML
    private GridPane background4;

    @FXML
    private GridPane background5;

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
    private BorderPane view;

    private List<Item> canciones;

    private Token accessToken;

    /**
     * Constructor del controlador de canciones recomendadas.
     *
     * @param accessToken El token de acceso a la API de Spotify.
     */
    public RecommendedTracksController(Token accessToken) {
        this.accessToken = accessToken;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RecommendedTracksView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpotifyApi api = new SpotifyApi();

        api.setToken(accessToken);

        // Aplicar el efecto de sombra a cada ImageView
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10); // Ajusta el radio de la sombra según lo deseado
        dropShadow.setColor(Color.WHITE); // Ajusta el color de la sombra según lo deseado

        background1.setOnMouseEntered(event -> background1.setEffect(dropShadow));
        background1.setOnMouseExited(event -> background1.setEffect(null));

        background2.setOnMouseEntered(event -> background2.setEffect(dropShadow));
        background2.setOnMouseExited(event -> background2.setEffect(null));

        background3.setOnMouseEntered(event -> background3.setEffect(dropShadow));
        background3.setOnMouseExited(event -> background3.setEffect(null));

        background4.setOnMouseEntered(event -> background4.setEffect(dropShadow));
        background4.setOnMouseExited(event -> background4.setEffect(null));

        background5.setOnMouseEntered(event -> background5.setEffect(dropShadow));
        background5.setOnMouseExited(event -> background5.setEffect(null));

        try {
            canciones = api.getTopTracks();
            List<String> idsCanciones = new ArrayList<>();
            for (int i = 0; i < canciones.size(); i++) {
                idsCanciones.add(canciones.get(i).getId());
            }

            List<Track> recomendaciones = api.getRecommendations(idsCanciones);

            for (int i = 0; i < recomendaciones.size(); i++) {
                if (i < 5) {
                    Track track = recomendaciones.get(i);
                    Label[] artistNames = {artistname1, artistname2, artistname3, artistname4, artistname5};
                    Label[] songNames = {namesong1, namesong2, namesong3, namesong4, namesong5};
                    ImageView[] artistImages = {img1, img2, img3, img4, img5};

                    songNames[i].setText(track.getName());
                    artistNames[i].setText(String.join(", ", track.getAlbum().getArtists().get(0).getName()));
                    setImageViewFromUrl(artistImages[i], track.getAlbum().getImages().get(0).getUrl());

                    final int index = i;
                    artistImages[i].setOnMouseClicked(event -> {
                        try {
                            // Abrir el enlace en el navegador web predeterminado
                            Desktop.getDesktop().browse(new URI(track.getExternalUrls().getSpotify()));
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    });

                    // Cambiar el estado del cursor para que sea seleccionable
                    artistImages[i].setOnMouseEntered(event -> artistImages[index].setCursor(Cursor.HAND));
                    artistImages[i].setOnMouseExited(event -> artistImages[index].setCursor(Cursor.DEFAULT));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Maneja el evento de volver a la vista principal.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onVolver(ActionEvent event) {
        AppMain.getRootController().showMain();
    }

}
