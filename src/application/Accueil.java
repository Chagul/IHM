package application;


import java.io.File; 
import java.io.IOException;

import controller.AccueilController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Accueil extends Application{

	private static Stage primaryStage;
	public static Liste fullListe;
	public final static File saveFile = new File("." + File.separator + "save");
	public final static File gardefou = new File("." + File.separator + "gardefou");

	private static void setFullList(Liste alist) {
		fullListe = alist;
	}
	private void setPrimaryStage(Stage stage) {
		Accueil.primaryStage = stage;
	} 

	static public Stage getPrimaryStage() {
		return Accueil.primaryStage;
	}
	@Override
	public void start(Stage primaryStage) {
		if(gardefou.exists()) {
			try {
				new WindowAlreadyRunning();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				gardefou.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				setPrimaryStage(primaryStage);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/Accueil.fxml"));
				Parent root = loader.load();

				Scene scene = new Scene(root);
				primaryStage.setTitle("Accueil");
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.show();
				//So we don't launch 2 instances of the application to avoid incoherents datas 
				AccueilController accueilController = loader.getController();
				accueilController.setPrimaryStage(primaryStage);
				accueilController.setList(fullListe);

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void stop() {
		gardefou.delete();
		for(Task t : fullListe.getAllTasks()) {
			for(DownTask d : t.getListDownTask()) {
				d.setRunning(false);
				d.resume();
			}
			t.setRunning(false);
			t.resume();
		}
		Platform.exit();
		System.exit(0);
	}
	public static void main(String[] args) throws ClassNotFoundException {
		if(saveFile.exists()) {
			//System.out.println("INITIALIZE");
			try {
				Accueil.setFullList(Liste.loadListe(saveFile));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}else {
			Accueil.setFullList(new Liste());
		}
		launch(args);
	}

}
