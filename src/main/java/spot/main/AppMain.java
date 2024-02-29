package spot.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import spot.controller.RootController;

public class AppMain extends Application {

	private static RootController rootController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		rootController = new RootController();
		primaryStage.setTitle("SpotMetrics");

		// Crear una escena con un Pane como nodo raíz
		Pane rootPane = rootController.getView();
		Scene scene = new Scene(rootPane);

		primaryStage.setScene(scene);

		// Deshabilitar la capacidad de cambiar el tamaño de la ventana
//		primaryStage.setResizable(false);

		primaryStage.show();
	}

	public static RootController getRootController() {
		return rootController;
	}
}
