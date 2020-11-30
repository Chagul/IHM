package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class WindowAlreadyRunning {
	public WindowAlreadyRunning() throws IOException{
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/AlreadyRunning.fxml"));
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
