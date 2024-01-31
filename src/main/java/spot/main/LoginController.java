package spot.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import spot.api.OAuth;
import spot.api.SpotifyApi;
import spot.api.model.Token;

public class LoginController implements Initializable {

	@FXML
	private Button ButtonLogin;

	@FXML
	private BorderPane view;

	private Token accessToken;

	private TopTracksController topTracksController;

	public LoginController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	void onLogin(ActionEvent event) throws Exception {
		OAuth oauth = new OAuth();
		oauth.auth(code -> {
			try {
				System.out.println("code: " + code);
				SpotifyApi spot = new SpotifyApi();
				accessToken = spot.requestAccessToken(code);
				System.out.println("token: " + accessToken);
				System.out.println("usuario: " + spot.getUsername());
				System.out.println("canciones: " + spot.getTopTracks().size());

				// Crear una instancia de TopTracksController pasando el token
				TopTracksController topTracksController = new TopTracksController(accessToken);

				// Cargar la vista TopTracks
				BorderPane topTracksView = topTracksController.getView();

				// Cargar la vista TopTracks en la escena
				Platform.runLater(() -> {
					Stage stage = (Stage) view.getScene().getWindow();
					Scene topTracksScene = new Scene(topTracksView);
					stage.setScene(topTracksScene);
					stage.show();
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public BorderPane getView() {
		return view;
	}

}
