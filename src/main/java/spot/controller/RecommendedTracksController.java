package spot.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import spot.api.SpotifyApi;
import spot.api.model.Token;
import spot.api.model.recommendations.Track;
import spot.api.model.toptracks.Item;
import spot.main.AppMain;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
                            try {
								Desktop.getDesktop().browse(new URI(track.getExternalUrls().getSpotify()));
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        } catch (IOException  e) {
                            e.printStackTrace();
                        }
                    });

                    // Cambiar el estado del cursor para que sea seleccionable
                    artistImages[i].setOnMouseEntered(event -> artistImages[index].setCursor(Cursor.HAND));
                    artistImages[i].setOnMouseExited(event -> artistImages[index].setCursor(Cursor.DEFAULT));
                }
            }
            
            
            
            
            System.out.println("La recomendacion es : " + recomendaciones.get(0).getName());

        } catch (IOException e) {
            e.printStackTrace();
       }
    }

    public BorderPane getView() {
        return view;
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

    @FXML
    void onVolver(ActionEvent event) {
        AppMain.getRootController().showMain();
    }

}
