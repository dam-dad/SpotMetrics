package spot.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import spot.api.SpotifyApi;
import spot.api.model.toptracks.Item;
import spot.api.model.Token;
import spot.main.AppMain;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TopTracksController implements Initializable {

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

    public TopTracksController(Token accessToken) {
        this.accessToken = accessToken;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TopTracksView.fxml"));
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
            canciones = api.getTopTracks();
            String[] idsCanciones = new String[canciones.size()];

            for (int i = 0; i < canciones.size(); i++) {

                idsCanciones[i] = canciones.get(i).getId();


            }

            for (int i = 0; i < canciones.size(); i++) {

                System.out.println(idsCanciones[i]);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }



            try {
            canciones = api.getTopTracks();

            Label[] namesongs = {namesong1, namesong2, namesong3, namesong4, namesong5};
            Label[] artistnames = {artistname1, artistname2, artistname3, artistname4, artistname5};
            ImageView[] imageViews = {img1, img2, img3, img4, img5};

            for (int i = 0; i < canciones.size(); i++) {
                if (i < namesongs.length) {
                    canciones.get(i).getId();
                    namesongs[i].setText(canciones.get(i).getName());
                    artistnames[i].setText(canciones.get(i).getAlbum().getArtists().get(0).getName());
                    setImageViewFromUrl(imageViews[i], canciones.get(i).getAlbum().getImages().get(0).getUrl());
                    int finalI = i; // Variable final para usar en la expresión lambda
                    imageViews[i].setOnMouseClicked(event -> {
                        try {
                            // Abrir el enlace en el navegador web predeterminado
                            Desktop.getDesktop().browse(new URI(canciones.get(finalI).getExternalUrls().getSpotify()));
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

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
