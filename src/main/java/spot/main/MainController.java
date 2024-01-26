package spot.main;

import java.io.IOException;
import java.net.URISyntaxException;
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

	public static String authorizationCode;
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
				System.out.println(token);
				System.out.println(spot.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// Inicia el servidor web local
//		startLocalServer();

		// Redirecciona al usuario a la URL de autorización de Spotify
//		redirectToSpotifyAuthorization();

		// Espera hasta que se obtenga el código de autorización
//		while (authorizationCode == null) {		
//			System.out.println("Esperando el código de autorización");
//			try {
//				Thread.sleep(1000); // Espera 1 segundo antes de verificar de nuevo
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

		// Muestra el código de autorización
//		System.out.println("Código de Autorización: " + authorizationCode);

		// Intercambia el código de autorización por el token
//		String clientId = "33e34aef0ac34bb2be3ca751252b16ff";
//		String clientSecret = "46e68b7d5ec14b1bb02243d131bc0f4b"; // Reemplaza con tu propio client secret
//		String redirectUri = "http://localhost:8888/callback";

		// Intercambia el código de autorización por el token de acceso
//		String token = exchangeCodeForToken(clientId, clientSecret, redirectUri, authorizationCode);

		// Muestra el token
//		System.out.println("Token de Acceso: " + token);

		// Obtiene y muestra el nombre de usuario
//		String username = getSpotifyUsername(token);
//		System.out.println("Nombre de Usuario: " + username);

//		String[] topTracks = getTopTracks(token);
//		for (String track : topTracks) {
//			System.out.println(track);
//		}

	}

	public BorderPane getView() {
		// TODO Auto-generated method stub
		return view;
	}

}
