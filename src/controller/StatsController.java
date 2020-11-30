package controller;

import java.text.DecimalFormat;
import java.util.ArrayList;

import application.Categorie;
import application.DownTask;
import application.Liste;
import application.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
 
public class StatsController {
	Liste fullList = new Liste();
	//So we don't need to load from the saveFile
	public void setList(Liste aList) {
		this.fullList = aList;
	}
	public Liste getList() {
		return this.fullList;
	}
	private int compteurCategorie = 0;
	private int compteurTask = 0;
	private int compteurTaskInList = 0;
	private int compteurCategoriesInList = 0;
	Categorie categorieOrphanTask;
	DecimalFormat formatPurcent = new DecimalFormat("#0.00");
	@FXML
	Button nextCategorie;
	@FXML
	Button previousCategorie;
	@FXML
	Button nextTask;
	@FXML
	Button previousTask;
	@FXML
	PieChart pieChartCategorie;
	@FXML
	PieChart pieChartTask;
	@FXML
	Label pieChartHover;
	@FXML
	Label pieChartHoverTask;
	@FXML
	Label timeElapsedOnThisCategorie;
	@FXML
	Label timeElapsedOnThisTask;
	@FXML
	Label errorLabel;
	@FXML
	Label errorLabelTask;
	@FXML
	Tab tabTask;
	@FXML
	Label labelNameCategorie;
	public void initialization() {
		compteurCategoriesInList = fullList.getListCategorie().size();
		compteurTaskInList = fullList.getAllTasks().size();
		pieChartTask.setLegendVisible(false);
		pieChartCategorie.setLegendVisible(false);
		pieChartTask.setLabelLineLength(10);;
		pieChartCategorie.setLabelLineLength(10);
		if(fullList.getListCategorie().isEmpty() && fullList.getOrphanCategorie().getTimeElapsedOfACategorie() == 0) {
			errorLabel.setVisible(true);
			timeElapsedOnThisCategorie.setVisible(false);
			tabTask.setDisable(true);
			nextCategorie.setVisible(false);
		}else if(!fullList.getListCategorieWithoutOrphanCategorie().isEmpty()) {
			if(!fullList.getListCategorieWithoutOrphanCategorie().isEmpty()){
				if(fullList.getListCategorie().size() > 1 ) {
					nextCategorie.setVisible(true);
				}if(fullList.getAllTasks().size() > 1) {
					nextTask.setVisible(true);
				}
				if(!fullList.getAllTasks().isEmpty()) {
				updateChartTasks(fullList.getAllTasks().get(0));
				errorLabel.setVisible(false);
				tabTask.setDisable(false);
				}
				timeElapsedOnThisCategorie.setVisible(true);
				if(fullList.getOrphanCategorie().getTimeElapsedOfACategorie() == 0 && !fullList.getListCategorieWithoutOrphanCategorie().isEmpty()) {
					updateChartCategorie(fullList.getListCategorie().get(compteurCategorie));
					timeElapsedOnThisCategorie.setText("Temps total : " + fullList.getListCategorie().get(compteurCategorie).getTimeElapsedOfACategorieString());
				}else {
					updateChartCategorie(fullList.getOrphanCategorie());
					timeElapsedOnThisCategorie.setText("Temps total : " + fullList.getOrphanCategorie().getTimeElapsedOfACategorieString());
				}
				if(compteurTaskInList == 1) {
					nextTask.setVisible(false);
				}
				if(compteurCategoriesInList == 1) {
					nextCategorie.setVisible(false);
				}
			}
		}else if(fullList.getOrphanCategorie().getListTask().isEmpty() && fullList.getListCategorieWithoutOrphanCategorie().isEmpty()){
			nextCategorie.setVisible(false);
			tabTask.setDisable(true);
		}else {
			compteurTask = fullList.getOrphanTask().size();
			if(compteurTask == 1) {
				nextTask.setVisible(false);
			}
			tabTask.setDisable(false);
			errorLabel.setVisible(false);
			updateChartCategorie(fullList.getOrphanCategorie());
			updateChartTasks(fullList.getOrphanTask().get(0));
			nextCategorie.setVisible(false);
			timeElapsedOnThisCategorie.setVisible(true);
		}
	}
	//Buttons
	/**
	 * When the user press the next button in the categorie pane
	 */
	public void pressedButtonNextCategorie(){
		compteurCategorie++;
		if(compteurCategorie != 0) {
			previousCategorie.setVisible(true);
		}
		if(compteurCategorie == compteurCategoriesInList-1) {
			nextCategorie.setVisible(false);
		}
		updateChartCategorie(fullList.getListCategorie().get(compteurCategorie));

	}
	/**
	 * When the user press the previous button in the categorie pane
	 */
	public void pressedButtonPreviousCategorie(){
		compteurCategorie--;
		if(compteurCategorie == 0) {
			previousCategorie.setVisible(false);
		}

		if(compteurCategorie != compteurCategoriesInList-1) {
			nextCategorie.setVisible(true);
		}
		updateChartCategorie(fullList.getListCategorie().get(compteurCategorie));
	}
	/**
	 * When the user press the next button in the task pane
	 */
	public void pressedButtonNextTask(){
		compteurTask++;
		if(compteurTask != 0) {
			previousTask.setVisible(true);
		}
		if(compteurTask == compteurTaskInList -1 ) {
			nextTask.setVisible(false);
		}
		updateChartTasks(fullList.getAllTasks().get(compteurTask));
	}
	/**
	 * When the user press the previous button in the task pane
	 */
	public void pressedButtonPreviousTask(){
		compteurTask--;
		if(compteurTask == 0) {
			previousTask.setVisible(false);
		}
		if(compteurTask != compteurTaskInList -1 ) {
			nextTask.setVisible(true);
		}
		updateChartTasks(fullList.getAllTasks().get(compteurTask));
	}
	//Methods
	/**
	 * Update the chart of categories with categories in list
	 * @param c the categorie to be loaded in the chart
	 */
	public void updateChartCategorie(Categorie c) {
		if(c.getTimeElapsedOfACategorie() != 0) {
			errorLabel.setVisible(false);
			ArrayList<PieChart.Data> tmp2 = new ArrayList<PieChart.Data>();
			for(Task t : c.getListTask()) {
				tmp2.add(new PieChart.Data(t.getName() + "     "  + String.valueOf(formatPurcent.format((double)t.getElapsedTime()/c.getTimeElapsedOfACategorie() * 100)) + "%",(double)t.getElapsedTime()/c.getTimeElapsedOfACategorie() * 100) ) ;
			}
			ObservableList<PieChart.Data> tmp = FXCollections.observableArrayList(tmp2);
			pieChartCategorie.getData().clear();
			pieChartCategorie.getData().addAll(tmp);
		}else {
			pieChartCategorie.getData().clear();
			errorLabel.setVisible(true);
		}
		pieChartCategorie.setTitle(c.getName());
		timeElapsedOnThisCategorie.setText("Temps total : " + c.getTimeElapsedOfACategorieString());
	}
	//Methods
	/**
	 * Update the chart of tasks with tasks in list
	 * @param t the task to be loaded in the chart
	 */
	public void updateChartTasks(Task t) {
		if(t.getCategorieParent() != null) {
		labelNameCategorie.setText(t.getCategorieParent().getName());
		}else {
			labelNameCategorie.setText("Sans catégorie");
		}
		if(t.getElapsedTime() != 0) {
			errorLabelTask.setVisible(false);
			long tmpTimeNotInSubTask = t.getElapsedTime();
			ArrayList<PieChart.Data> tmp2 = new ArrayList<PieChart.Data>();
			for(DownTask d : t.getListDownTask()) {
				tmpTimeNotInSubTask -= d.getElapsedTime();
				tmp2.add(new PieChart.Data(d.getName()  + "     "  + String.valueOf(formatPurcent.format((double)d.getElapsedTime()/t.getElapsedTime() * 100)) + "%",(double)d.getElapsedTime()/t.getElapsedTime() * 100));
			}
			tmp2.add(new PieChart.Data("Sans sous tâche" + "     "  + String.valueOf(formatPurcent.format((double)tmpTimeNotInSubTask/t.getElapsedTime() * 100)) + "%" ,(double)tmpTimeNotInSubTask/t.getElapsedTime() * 100));
			ObservableList<PieChart.Data> tmp = FXCollections.observableArrayList(tmp2);
			pieChartTask.getData().clear();
			pieChartTask.getData().addAll(tmp);
		}else {
			pieChartTask.getData().clear();
			errorLabelTask.setVisible(true);
		}
		pieChartTask.setTitle(t.getName());
		timeElapsedOnThisTask.setText("Temps total : " + t.getElapsedTimeString());
	}
	/**
	 * UNUSED But it was a good idea...
	 * Set listener for when we mouseHover a pie part so it show how many purcent it represent
	 * @param pie the pie where we are moving
	 */
	/**public void setMouseHoverChart(PieChart pie) {
		for (PieChart.Data data : pie.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,new EventHandler<MouseEvent>() {
				@Override 
				public void handle(MouseEvent e) {
					if( pie == pieChartCategorie) {
						pieChartHover.setVisible(true);
						pieChartHover.setTranslateX(e.getSceneX());
						pieChartHover.setTranslateY(e.getSceneY());
						pieChartHover.setText(String.valueOf(formatPurcent.format(data.getPieValue()) + "%"));
					}else {
						pieChartHoverTask.setVisible(true);
						pieChartHoverTask.setTranslateX(e.getSceneX());
						pieChartHoverTask.setTranslateY(e.getSceneY());
						pieChartHoverTask.setText(String.valueOf(formatPurcent.format(data.getPieValue()) + "%"));
					}
				}
			});
			data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					if( pie == pieChartCategorie) pieChartHover.setVisible(false);
					else  pieChartHoverTask.setVisible(false);
				}
			});
		}**/

}
