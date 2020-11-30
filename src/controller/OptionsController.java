package controller;

import java.io.FileNotFoundException;

import application.Accueil;
import application.Categorie;
import application.DownTask;
import application.Liste;
import application.Task;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
 
public class OptionsController{
	@FXML
	ComboBox<Categorie> categorieList;
	@FXML
	ComboBox<Categorie> categorieListCreation;
	@FXML
	TextField categorieName;
	@FXML
	TextField renameField;
	@FXML 
	ComboBox<String> listTemporalite;
	@FXML
	TextField taskName;
	@FXML
	ListView<Task> orphanTaskListView;
	@FXML 
	ComboBox<Task> listTask;
	@FXML 
	TextField downTaskName;
	@FXML
	Button buttonMoveCategorie; 
	@FXML
	ComboBox<Categorie> categorieManage;
	@FXML
	Button buttonDelete;
	@FXML
	ComboBox<Task> taskManaging;
	@FXML
	ComboBox<DownTask> downTaskManaging;
	@FXML
	Button buttonRename;
	@FXML
	Button buttonAddDownTask;
	@FXML
	Button buttonCreateCategorie;
	@FXML 
	Button buttonAddTask;
	@FXML
	ComboBox<Categorie> categorieListFilter;
	Categorie categorieOrphanTask;

	Liste fullListe;
	//So we don't have to load from the saveFile everyTime
	public void setListe(Liste aList) {
		this.fullListe = aList;
	}
	public Liste getListe(Liste aList) {
		return this.fullListe;

	}
	/**
	 * 
	 * @return the categorie selected in the comboBox correspondent
	 */
	public Categorie categorieSelected() {
		return 	categorieManage.getSelectionModel().getSelectedItem();
	}
	/**
	 * 
	 * @return the Task selected in the comboBox correspondent
	 */
	public Task taskSelected() {
		return taskManaging.getSelectionModel().getSelectedItem();
	}
	/**
	 * 
	 * @return the DownTask selected in the comboBox correspondent
	 */
	public DownTask downTaskSelected() {
		return downTaskManaging.getSelectionModel().getSelectedItem();
	}
	/**
	 * 
	 * @return the Text in the renameField
	 */
	public String getTextRenameField() {
		return renameField.getText();
	}
	/**
	 * Refresh list and comboBox after a change
	 */
	public void refresh() {
		taskManaging.getItems().clear();
		categorieManage.getItems().clear();
		downTaskManaging.getItems().clear();
		buttonDelete.setDisable(true);
		buttonMoveCategorie.setDisable(true);
		categorieList.setDisable(true);
		downTaskManaging.setDisable(true);
		taskManaging.setDisable(true);
		categorieManage.getItems().addAll(fullListe.getListCategorie());
	}
	/**
	 * Refresh all the items concerned by a Categorie change
	 */
	public void refreshCategorieChanged() {
		refreshTaskChanged();
		categorieListCreation.getItems().clear();
		categorieListCreation.setItems(FXCollections.observableArrayList(fullListe.getListCategorieWithoutOrphanCategorie()));
		categorieList.getItems().clear();
		categorieList.setItems(FXCollections.observableArrayList(fullListe.getListCategorieWithoutOrphanCategorie()));
		categorieManage.getItems().clear();
		categorieManage.setItems(FXCollections.observableArrayList(fullListe.getListCategorie()));
		orphanTaskListView.getItems().clear();
		orphanTaskListView.setItems(FXCollections.observableArrayList(fullListe.getOrphanTask()));
	}
	/**
	 * Refresh all the items concerned by a Task change
	 */
	public void refreshTaskChanged() {
		orphanTaskListView.getItems().clear();
		orphanTaskListView.setItems(FXCollections.observableArrayList(fullListe.getOrphanTask()));
		listTask.getItems().clear();
		listTask.setItems(FXCollections.observableArrayList(fullListe.getAllTasks()));
		categorieManage.getItems().clear();
		categorieManage.setItems(FXCollections.observableArrayList(fullListe.getListCategorie()));
		if(!fullListe.getOrphanTask().isEmpty()) {
			taskManaging.getItems().clear();
			taskManaging.setItems(FXCollections.observableArrayList(fullListe.getAllTasks()));
		}else {
			taskManaging.getItems().clear();
		}
	}
	/**
	 * Refresh all the items concerned by a downTask change
	 */
	private void refreshDownTaskChanged() {
		downTaskManaging.getItems().clear();
		if(!taskSelected().getListDownTask().isEmpty()) {
			downTaskManaging.setItems(FXCollections.observableArrayList(taskSelected().getListDownTask()));
		}

	}
	/**
	 * Load everything
	 */
	public void initialization() {
		categorieOrphanTask = new Categorie("Sans categorie");
		categorieOrphanTask.setOrphanCategorie(true);
		if(!fullListe.getListCategorie().contains(fullListe.getOrphanCategorie())) {
		fullListe.addCategorie(categorieOrphanTask);
		}
		listTask.setItems(FXCollections.observableArrayList(fullListe.getAllTasks()));
		orphanTaskListView.setItems(FXCollections.observableArrayList(fullListe.getOrphanTask()));
		orphanTaskListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		categorieListFilter.setItems(FXCollections.observableArrayList(fullListe.getListCategorie()));
		categorieListCreation.setItems(FXCollections.observableArrayList(fullListe.getListCategorieWithoutOrphanCategorie()));
		categorieManage.setItems(FXCollections.observableArrayList(fullListe.getListCategorie()));
		listTask.setItems(FXCollections.observableArrayList(fullListe.getAllTasks()));
		categorieList.getItems().clear();
		categorieList.setItems(FXCollections.observableArrayList(fullListe.getListCategorie()));
		taskManaging.getItems().clear();
		taskManaging.setItems(FXCollections.observableArrayList(fullListe.getOrphanTask()));
		listTask.valueProperty().addListener(new ChangeListener<Task>() {
			@Override
			public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
				if(newValue == null){
					buttonAddDownTask.setDisable(true);
				}else {
					buttonAddDownTask.setDisable(false);
				}
			}

		});
		// Set the button correspondant to text field to disable if there is no input
		categorieName.textProperty().addListener(Observable ->{
			if(!categorieName.getText().isEmpty()) {
				buttonCreateCategorie.setDisable(false);
			}else { 
				buttonCreateCategorie.setDisable(true);
			}

		});
		taskName.textProperty().addListener(Observable ->{
			if(!taskName.getText().isEmpty()) {
				buttonAddTask.setDisable(false);
			}else { 
				buttonAddTask.setDisable(true);
			}

		});
		downTaskName.textProperty().addListener(Observable ->{
			if(!downTaskName.getText().isEmpty() && !listTask.getSelectionModel().isEmpty()) {
				buttonAddDownTask.setDisable(false);
			}else { 
				buttonAddDownTask.setDisable(true);
			}

		});
		renameField.textProperty().addListener(Observable ->{
			if(!renameField.getText().isEmpty()) {
				buttonRename.setDisable(false);
			}else { 
				buttonRename.setDisable(true);
			}

		});
	}


	/**
	 * Create a new categorie and save it in the principal list
	 * If something is selected in the orphanTaskListView, these tasks are binded to the new categorie
	 */
	public void pressedButtoncreateCategorie(ActionEvent event){
		Categorie tmp = new Categorie(categorieName.getText());
		if(!orphanTaskListView.getSelectionModel().isEmpty()) {
			for(Task t : orphanTaskListView.getSelectionModel().getSelectedItems()) {
				t.setCategorieParent(tmp);
				tmp.addTask(t);
			}
			fullListe.getOrphanCategorie().removeAll(orphanTaskListView.getSelectionModel().getSelectedItems());
			fullListe.getOrphanTask().removeAll(orphanTaskListView.getSelectionModel().getSelectedItems());
		}
		System.out.println(tmp);
		fullListe.addCategorie(tmp);
		categorieName.clear();
		refreshCategorieChanged();
	}
	/**
	 * Add a new task with the name typed by the user in the taskField, if a categorie is selected(not mandatory), attach this Task to this categorie, else it will be added to the OrphanTasks
	 */
	public void pressedButtonAddTask(ActionEvent event) throws FileNotFoundException{
		Task tmp = new Task(taskName.getText());
		if(!categorieListCreation.getSelectionModel().isEmpty()) {
			tmp.setCategorieParent(categorieListCreation.getSelectionModel().getSelectedItem());
			fullListe.setTaskOfaCategorie(categorieListCreation.getSelectionModel().getSelectedItem(),tmp);
			taskName.clear();
		}else {
			System.out.println("Creating orphan task");
			tmp.setCategorieParent(null);
			fullListe.getOrphanCategorie().addTask(tmp);
			taskName.clear();
		}
		categorieListCreation.getSelectionModel().clearSelection();
		refreshTaskChanged();
	}
	/**
	 *Add a new downtask with the name typed by the user in the downTaskField, attach this downTask to a Task selected by the users
	 */
	public void ButtonPressedAddDownTask(ActionEvent event) {
		DownTask tmp = new DownTask(downTaskName.getText());
		if(listTask.getValue() != null) {
			tmp.setParent(listTask.getValue());
			listTask.getValue().addDownTask(tmp);
			downTaskName.clear();
			listTask.getSelectionModel().clearSelection();
		}
		refresh();
	}
	/**
	 * Remove in the principal list what is selected by the user
	 * 
	 */
	public void pressedButtonDelete(ActionEvent event){
		//If we chose just a categorie
		if(taskManaging.getSelectionModel().isEmpty()  && downTaskManaging.getSelectionModel().isEmpty()) {
			fullListe.getListCategorie().remove(categorieSelected());
			refreshCategorieChanged();
			//If we choose just and orphan Task
		}else if(categorieManage.getSelectionModel().isEmpty() && !taskManaging.getSelectionModel().isEmpty() && downTaskManaging.getSelectionModel().isEmpty()) {
			fullListe.removeATask(taskSelected());
			refreshTaskChanged();
			taskManaging.getSelectionModel().clearSelection();
			System.out.println(fullListe.getAllTasks().toString());
			categorieManage.requestFocus();
			//Else if we chose a categorie and a task
		}else if(downTaskManaging.getSelectionModel().isEmpty()) {
			fullListe.removeTaskOfaCategorie(categorieSelected(),taskSelected());
			taskManaging.getSelectionModel().clearSelection();
			refreshTaskChanged();
			categorieManage.requestFocus();
			//else a categorie, a task and a downTask is selected
		}else {
			fullListe.removeADownTaskFromATaskThatIsInACategorie(categorieSelected(),taskSelected(), downTaskSelected());
			downTaskManaging.getSelectionModel().clearSelection();
			refreshDownTaskChanged();
			taskManaging.requestFocus();
		}
	}
	/**
	 * Move a task and its possible downTask from a categorie to another
	 */
	public void ButtonPressedMoveCategorie(ActionEvent event) {
		if(!categorieList.getSelectionModel().isEmpty()) {
			if(!categorieManage.getSelectionModel().isEmpty() && !taskManaging.getSelectionModel().isEmpty() && downTaskManaging.getSelectionModel().isEmpty()) {
				fullListe.moveATaskFromACategorieToAnother(categorieSelected(), categorieList.getSelectionModel().getSelectedItem() , taskSelected());
				//else we choose an orphan task
			}else {
				fullListe.moveATaskFromOrphanToAnother(categorieList.getSelectionModel().getSelectedItem(), taskSelected());
			}
			taskManaging.getSelectionModel().clearSelection();
			refreshTaskChanged();
			categorieManage.requestFocus();
		}
	}

	/**
	 * Rename a categorie from what the user typed in the renameField and update the principal list
	 * 
	 */

	public void pressedButtonRename(ActionEvent event){
		//If we chose just a categorie
		if(taskManaging.getSelectionModel().isEmpty()  && downTaskManaging.getSelectionModel().isEmpty()) {
			fullListe.renameCategorie(categorieSelected(), getTextRenameField());
			refreshCategorieChanged();
			//If we choose just and orphan Task
		}else if(categorieManage.getSelectionModel().isEmpty() && !taskManaging.getSelectionModel().isEmpty() && downTaskManaging.getSelectionModel().isEmpty()) {
			fullListe.renameATask(categorieSelected(), taskSelected(), getTextRenameField());
			refreshTaskChanged();
			taskManaging.getSelectionModel().clearSelection();
			categorieManage.requestFocus();
			//Else if we chose a categorie and a task
		}else if(downTaskManaging.getSelectionModel().isEmpty()) {
			fullListe.renameATask(categorieSelected(), taskSelected(), getTextRenameField());
			taskManaging.getSelectionModel().clearSelection();
			refreshTaskChanged();
			categorieManage.requestFocus();
			//else a categorie, a task and a downTask is selected
		}else {
			fullListe.renameADowntask(categorieSelected(), taskSelected(), downTaskSelected(), getTextRenameField());
			downTaskManaging.getSelectionModel().clearSelection();
			refreshDownTaskChanged();
			taskManaging.requestFocus();
		}
		renameField.clear();
	}

	/**
	 * Set the list of task of a categorie, and unlock the features that can be executed on a categorie
	 * @param event : a categorie has been choosed
	 */
	public void pressedChooseCategorie(ActionEvent event) {
		buttonDelete.setDisable(false);
		buttonRename.setDisable(false);
		renameField.setDisable(false);
		taskManaging.setDisable(false);
		taskManaging.getItems().clear();
		downTaskManaging.getItems().clear();
		if(!fullListe.getListCategorie().isEmpty() && !categorieManage.getSelectionModel().isEmpty() && !categorieSelected().getListTask().isEmpty()) {
			taskManaging.getItems().setAll(categorieSelected().getListTask());
		}
	}

	/**
	 * Load the list of downtask from Task selected and unlock the features that can be executed on a task
	 * @param event : a task has been selected
	 */
	public void pressedChooseTask(ActionEvent event) {
		if(!categorieManage.getSelectionModel().isEmpty() ) {
			buttonDelete.setDisable(false);
			buttonMoveCategorie.setDisable(false);
			categorieList.setDisable(false);
			downTaskManaging.setDisable(false);
			if(!categorieManage.getSelectionModel().isEmpty() && !taskManaging.getSelectionModel().isEmpty() && !taskSelected().getListDownTask().isEmpty()) {
				downTaskManaging.setItems(FXCollections.observableArrayList(taskSelected().getListDownTask()));
			}
		}else if(categorieManage.getSelectionModel().isEmpty() && fullListe.getOrphanTask().contains(taskSelected())){
			buttonMoveCategorie.setDisable(false);
			buttonDelete.setDisable(false);
			buttonRename.setDisable(false);
			categorieList.setDisable(false);
			downTaskManaging.setDisable(true);
		}
	}
	/**
	 * Unlock the features that can be executed on a Downtask
	 * @param event : a downTask has been selected
	 */
	public void pressedChooseDownTask(ActionEvent event) {
		buttonMoveCategorie.setDisable(true);
		categorieList.setDisable(true);
	}
	
	public void pressedChooseCategorieInAddDownTask(ActionEvent event) {
		if(!categorieListFilter.getSelectionModel().isEmpty()) {
			listTask.setItems(FXCollections.observableArrayList(categorieListFilter.getSelectionModel().getSelectedItem().getListTask()));
		}
	}
	/**
	 * When the window is closed, it will save all the modifications done
	 */
	public void shutdown() {
		try {
			Liste.saveListe(fullListe, Accueil.saveFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println("Shutdown");
	}
}
