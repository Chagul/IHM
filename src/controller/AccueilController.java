package controller;

import java.io.IOException;

import application.Liste;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
public class AccueilController{
	private Stage primaryStage;
	private Liste fullList = new Liste();
	//because we need the primaryStage hide and re open it 
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	public void setList(Liste aList) {
		this.fullList = aList;
	}
	public Liste getList() {
		return this.fullList;
	}


	/**
	 * Launch a window Chronometer
	 * @param event mouseClick
	 */
	public void pressedButtonChronometer(ActionEvent event) {
		try {
			primaryStage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/Chronometer.fxml"));
			Parent root = loader.load();
			root.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());

			Stage chronoStage = new Stage();
			chronoStage.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(root);
			chronoStage.setTitle("Vos chronomètres");
			chronoStage.setScene(scene);
			chronoStage.show();

			//Get controller of chronometerScene so we can transfer the list loaded
			ChronometerController chronometerController = loader.getController();
			chronometerController.setList(fullList);
			chronometerController.initialization();
			chronoStage.setOnHidden(e -> {
				chronometerController.shutDown();
				primaryStage.show();
			});
		}catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Launch a window Stats
	 * @param event mouseCLick
	 */
	public void pressedButtonStats(ActionEvent event) {
		try {
			primaryStage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/Stats.fxml"));
			Parent root = loader.load();

			//Get controller of statsScene so we can transfer the list loaded
			StatsController statsController = loader.getController();
			statsController.setList(getList());
			statsController.initialization();

			Stage statsStage = new Stage();
			Scene scene = new Scene(root);
			statsStage.setTitle("Vos statistiques");
			statsStage.setScene(scene);
			statsStage.setOnHidden(e ->{
				primaryStage.show();
			});
			statsStage.show();


		}catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Launch a window Option
	 * @param event mouseclick
	 */
	public void pressedButtonOptions(ActionEvent event) {
		try {
			primaryStage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/Options.fxml"));
			Parent root = loader.load();
			//Get controller of optionsScene so we can transfer the list loaded
			OptionsController optionsController = loader.getController();
			optionsController.setListe(fullList);
			optionsController.initialization();

			Stage optionsStage = new Stage();
			Scene scene = new Scene(root);
			optionsStage.setTitle("Vos chronomètres");
			optionsStage.setScene(scene);
			optionsStage.setOnHidden(e -> {
				optionsController.shutdown();
				primaryStage.show();
			});
			optionsStage.show();

		}catch (IOException e) {
			e.printStackTrace();
		}

	}

}
