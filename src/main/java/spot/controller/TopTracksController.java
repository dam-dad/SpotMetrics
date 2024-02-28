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

import java.io.IOException;
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

            // Top 1
            namesong1.setText(canciones.get(0).getName());
            artistname1.setText(canciones.get(0).getAlbum().getArtists().get(0).getName());
            setImageViewFromUrl(img1, canciones.get(0).getAlbum().getImages().get(0).getUrl());

            // Top 2
            namesong2.setText(canciones.get(1).getName());
            artistname2.setText(canciones.get(1).getAlbum().getArtists().get(0).getName());
            setImageViewFromUrl(img2, canciones.get(1).getAlbum().getImages().get(0).getUrl());

            // Top 3
            namesong3.setText(canciones.get(2).getName());
            artistname3.setText(canciones.get(2).getAlbum().getArtists().get(0).getName());
            setImageViewFromUrl(img3, canciones.get(2).getAlbum().getImages().get(0).getUrl());

            // Top 4
            namesong4.setText(canciones.get(3).getName());
            artistname4.setText(canciones.get(3).getAlbum().getArtists().get(0).getName());
            setImageViewFromUrl(img4, canciones.get(3).getAlbum().getImages().get(0).getUrl());

            // Top 5
            namesong5.setText(canciones.get(4).getName());
            artistname5.setText(canciones.get(4).getAlbum().getArtists().get(0).getName());
            setImageViewFromUrl(img5, canciones.get(4).getAlbum().getImages().get(0).getUrl());

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
