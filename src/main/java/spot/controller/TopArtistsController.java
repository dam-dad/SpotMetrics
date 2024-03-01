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

            // Top 1
            namesong1.setText(artistas.get(0).getName());
            artistname1.setText(String.join(", ", artistas.get(0).getGenres()));
            setImageViewFromUrl(img1, artistas.get(0).getImages().get(0).getUrl());
            rating1.setRating(artistas.get(0).getPopularity() / 20.0);

            rating1.setOnMouseClicked(event -> rating1.setRating(artistas.get(0).getPopularity() / 20.0));

            img1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        // IMPORT AWT DESKTOP
                        Desktop.getDesktop().browse(new URI(artistas.get(0).getExternalUrls().getSpotify()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // Cambiar el estado del cursor para que sea seleccionable
            img1.setOnMouseEntered(event -> img1.setCursor(Cursor.HAND));
            img1.setOnMouseExited(event -> img1.setCursor(Cursor.DEFAULT));

            // Top 2
            namesong2.setText(artistas.get(1).getName());
            artistname2.setText(String.join(", ", artistas.get(1).getGenres()));
            setImageViewFromUrl(img2, artistas.get(1).getImages().get(0).getUrl());
            rating2.setRating(artistas.get(1).getPopularity() / 20.0);

            rating2.setOnMouseClicked(event -> rating2.setRating(artistas.get(1).getPopularity() / 20.0));

            img2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        // IMPORT AWT DESKTOP
                        Desktop.getDesktop().browse(new URI(artistas.get(1).getExternalUrls().getSpotify()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            img2.setOnMouseEntered(event -> img2.setCursor(Cursor.HAND));
            img2.setOnMouseExited(event -> img2.setCursor(Cursor.DEFAULT));

            // Top 3
            namesong3.setText(artistas.get(2).getName());
            artistname3.setText(String.join(", ", artistas.get(2).getGenres()));
            setImageViewFromUrl(img3, artistas.get(2).getImages().get(0).getUrl());
            rating3.setRating(artistas.get(2).getPopularity() / 20.0);

            rating3.setOnMouseClicked(event -> rating3.setRating(artistas.get(2).getPopularity() / 20.0));

            img3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        // IMPORT AWT DESKTOP
                        Desktop.getDesktop().browse(new URI(artistas.get(2).getExternalUrls().getSpotify()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            img3.setOnMouseEntered(event -> img3.setCursor(Cursor.HAND));
            img3.setOnMouseExited(event -> img3.setCursor(Cursor.DEFAULT));

            // Top 4
            namesong4.setText(artistas.get(3).getName());
            artistname4.setText(String.join(", ", artistas.get(3).getGenres()));
            setImageViewFromUrl(img4, artistas.get(3).getImages().get(0).getUrl());
            rating4.setRating(artistas.get(3).getPopularity() / 20.0);

            rating4.setOnMouseClicked(event -> rating4.setRating(artistas.get(3).getPopularity() / 20.0));

            img4.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        // IMPORT AWT DESKTOP
                        Desktop.getDesktop().browse(new URI(artistas.get(3).getExternalUrls().getSpotify()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            img4.setOnMouseEntered(event -> img4.setCursor(Cursor.HAND));
            img4.setOnMouseExited(event -> img4.setCursor(Cursor.DEFAULT));

            // Top 5
            namesong5.setText(artistas.get(4).getName());
            artistname5.setText(String.join(", ", artistas.get(4).getGenres()));
            setImageViewFromUrl(img5, artistas.get(4).getImages().get(0).getUrl());
            rating5.setRating(artistas.get(4).getPopularity() / 20.0);

            rating5.setOnMouseClicked(event -> rating5.setRating(artistas.get(4).getPopularity() / 20.0));

            img5.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        // IMPORT AWT DESKTOP
                        Desktop.getDesktop().browse(new URI(artistas.get(4).getExternalUrls().getSpotify()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            img5.setOnMouseEntered(event -> img5.setCursor(Cursor.HAND));
            img5.setOnMouseExited(event -> img5.setCursor(Cursor.DEFAULT));

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
