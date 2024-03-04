package spot.controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import spot.api.OAuth;
import spot.api.SpotifyApi;
import spot.api.model.Token;
import spot.main.AppMain;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

	@FXML
	private Button ButtonLogin;

	@FXML
	private BorderPane view;

	private Token accessToken;
	
    @FXML
    private ImageView LoadingImage;

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
		// Inicialización del controlador

		// Ocultamos el GIF de carga
		hideLoadingScreen();

		// Manejador de eventos para cuando se muestra la ventana
		EventHandler<WindowEvent> handler = new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				// Obtenemos la referencia al Stage (ventana) actual
				Stage stage = (Stage) view.getScene().getWindow();

				// Desactivamos la capacidad de redimensionamiento de la ventana
				stage.setResizable(false);

				// Deshabilitamos la opción de maximizar la ventana
				stage.setMaximized(false);
			}
		};

		// Ejecutamos el código después de que la aplicación esté completamente iniciada
		Platform.runLater(() -> {
			// Obtenemos la referencia al Stage (ventana) actual
			Stage stage = (Stage) view.getScene().getWindow();

			// Agregamos el manejador de eventos al Stage
			stage.setOnShown(handler);
		});
	}

	@FXML
	void onLogin(ActionEvent event) throws Exception {

		showLoadingScreen();
		OAuth oauth = new OAuth();
		oauth.auth(code -> {
			try {
				System.out.println("code: " + code);
				SpotifyApi spot = new SpotifyApi();
				accessToken = spot.requestAccessToken(code);
				System.out.println("token: " + accessToken);
				System.out.println("usuario: " + spot.getUsername());
				System.out.println("canciones: " + spot.getTopTracks().size());

				AppMain.getRootController().instanceMainController(accessToken);
				AppMain.getRootController().showMain();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}
	
	private void showLoadingScreen() {
        // Muestra el GIF de carga
        LoadingImage.setVisible(true);
    }

    private void hideLoadingScreen() {
        // Oculta el GIF de carga
        LoadingImage.setVisible(false);
    }
	
	public BorderPane getView() {
		return view;
	}

}
