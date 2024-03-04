package spot.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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

public class TopArtistsController implements Initializable {

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
                        } catch (IOException  e) {
                            ((Throwable) e).printStackTrace();
                        } catch (URISyntaxException e) {
							// TODO Auto-generated catch block
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
