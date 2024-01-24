package spot.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain  extends Application{

	private MainController mainController;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainController = new MainController();	
		
		primaryStage.setTitle("StatsMetrics");
		primaryStage.setScene(new Scene(mainController.getView(), 800, 600));
		primaryStage.show();

		primaryStage.minWidthProperty().bind(mainController.getView().prefWidthProperty());
		primaryStage.minHeightProperty().bind(mainController.getView().prefHeightProperty());
		
	}

}
