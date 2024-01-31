package spot.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AppMain extends Application {

	private LoginController loginController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		loginController = new LoginController();

		primaryStage.setTitle("SpotMetrics");

		// Crear una escena con un Pane como nodo raíz
		Pane rootPane = loginController.getView();
		Scene scene = new Scene(rootPane);

		// Establecer tamaño fijo para el nodo raíz (en este caso, el Pane)
		double sceneWidth = 800; // Ancho deseado
		double sceneHeight = 500; // Alto deseado
		rootPane.setPrefSize(sceneWidth, sceneHeight);

		primaryStage.setScene(scene);

		// Deshabilitar la capacidad de cambiar el tamaño de la ventana
		primaryStage.setResizable(false);

		primaryStage.show();
	}

}
