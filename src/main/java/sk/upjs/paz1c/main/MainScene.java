package sk.upjs.paz1c.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class MainScene extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		MainSceneController controller = new MainSceneController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScene.fxml"));
		loader.setController(controller);
		Parent parent = loader.load();
		Scene scene = new Scene(parent);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Project Canteen");
		primaryStage.show();
		
		// https://www.tabnine.com/code/java/methods/javafx.stage.Stage/setOnCloseRequest
		primaryStage.setOnCloseRequest(e -> Platform.exit());
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
