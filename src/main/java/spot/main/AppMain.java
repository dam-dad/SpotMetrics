package spot.main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import spot.controller.RootController;

public class AppMain extends Application {

	private static RootController rootController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		rootController = new RootController(primaryStage);
		primaryStage.setTitle("SpotMetrics");

		// Crear una escena con un Pane como nodo raíz
		Pane rootPane = rootController.getView();
		Scene scene = new Scene(rootPane);

		primaryStage.setScene(scene);

		// Deshabilitar la capacidad de cambiar el tamaño de la ventana inicialmente
		primaryStage.setResizable(false);

		// Agregar un listener para detectar cambios de escena
		primaryStage.sceneProperty().addListener(new ChangeListener<Scene>() {
			@Override
			public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
				if (newScene != null) {
					// Cuando se cambia la escena, habilita la capacidad de cambiar el tamaño de la ventana
					primaryStage.setResizable(true);
				}
			}
		});

		primaryStage.show();
	}

	public static RootController getRootController() {
		return rootController;
	}

}
