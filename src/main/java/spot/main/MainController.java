package spot.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import spot.api.OAuth;
import spot.api.SpotifyApi;
import spot.api.model.Token;

public class MainController implements Initializable {

	@FXML
	private Button ButtonLogin;

	@FXML
	private BorderPane view;

	public MainController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InterfazLogin.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@FXML
	void onLogin(ActionEvent event) throws Exception {

		System.out.print("Metodo Login");
			    
		OAuth oauth = new OAuth();
		oauth.auth(code -> {
			try {
				System.out.println("code: " + code);
				SpotifyApi spot = new SpotifyApi();
				Token token = spot.requestAccessToken(code);
				System.out.println("token: " + token);
				System.out.println("usuario: " + spot.getUsername());
				System.out.println("canciones: " + spot.getTopTracks().size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	public BorderPane getView() {
		return view;
	}

}
