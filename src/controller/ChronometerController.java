package controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import application.Accueil;
import application.Categorie;
import application.DownTask;
import application.Liste;
import application.Task;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ChronometerController {
	@FXML
	TableView<Task> tableViewTaskRunning;
	@FXML
	ComboBox<Task> comboBoxAllTaskNotRunning;
	@FXML
	ComboBox<DownTask> comboBoxAllDownTaskNotRunning;
	@FXML
	ComboBox<Categorie> comboBoxCategorie;
	@FXML
	TableColumn<Task,String> columnTaskName;
	@FXML
	TableColumn<Task,String> columnProgress;
	@FXML
	TableColumn<Task,Button> columnResume;
	@FXML
	TableColumn<Task,Button> columnStop;
	@FXML
	TextField textFieldFastTask;
	@FXML 
	ComboBox<Task> comboBoxFastTask;
	@FXML
	Button buttonFastTaskCreateAndLaunch;
	@FXML
	TreeView<Liste> treeView;
	@FXML
	TableView<DownTask> tableViewDownTaskRunning;
	@FXML
	TableColumn<DownTask,String> columnDownTaskName;
	@FXML
	TableColumn<DownTask,String> columnDProgress;
	@FXML
	TableColumn<DownTask,Button> columnDResume;
	@FXML 
	TableColumn<DownTask, Button> columnDStop;
	@FXML
	TabPane tabPane;
	@FXML
	Tab tabFastLaunch;
	@FXML
	Button buttonLaunchTask;
	@FXML
	Button buttonResetSelection;
	@FXML
	Label labelCategorie;
	@FXML
	Tab tabRunningTask;
	Task taskStillRunning;
	Categorie categorieOrphanTask;
	private Liste fullList = new Liste();

	public void setList(Liste aList) {
		this.fullList = aList;
	}
	private void setTaskStillRunning(Task aTask) {
		this.taskStillRunning = aTask;
	}
	/**
	 * So we don't load the list everytime
	 * @return the list with all elements
	 */
	public Liste getList() {
		return this.fullList;
	}

	ObservableList<Task> listTasks;
	ObservableList<Task> listTasksRunning;
	ObservableList<Task> listTasksNotRunning;
	ObservableList<DownTask> listDownTasksRunning;
	ObservableList<DownTask> listDownTasksNotRunning;
	//Timer is used to refresh the tableView so we can see the chrono running
	Timer refresh = new Timer();
	{
		refresh.schedule(new TimerTask() {

			@Override
			public void run() {
				tableViewTaskRunning.refresh();
				tableViewDownTaskRunning.refresh();
			}
		}, 1000,1000);

	}
	/**
	 * 
	 * @return the task selected in the comboBox of task not running
	 */
	private Categorie getCategorie() {
		return comboBoxCategorie.getSelectionModel().getSelectedItem();
	}
	/**
	 * 
	 * @return the task selected in the comboBox of task not running
	 */
	private Task getTask() {
		return comboBoxAllTaskNotRunning.getSelectionModel().getSelectedItem();
	}
	/**
	 * 
	 * @return the downTask selected in the comboBox of downTask not running
	 */
	private DownTask getDownTask() {
		return comboBoxAllDownTaskNotRunning.getSelectionModel().getSelectedItem();
	}
	/**
	 * 
	 * @return the task selected in the comboBox of fastTask
	 */
	private Task getFastTask() {
		return comboBoxFastTask.getSelectionModel().getSelectedItem();
	}
	/**
	 * 
	 * @return the task selected in the tableView of task
	 */
	private Task getTaskTableView(){
		return tableViewTaskRunning.getSelectionModel().getSelectedItem();
	}
	/**
	 * When a categorie is selected, update the comboBox with matching tasks
	 */
	public void categorieSelected() {
		if(!comboBoxCategorie.getSelectionModel().isEmpty()) {
			if(!getCategorie().getListTask().isEmpty()) {
				comboBoxAllTaskNotRunning.setDisable(false);
				comboBoxAllTaskNotRunning.getItems().clear();
				comboBoxAllTaskNotRunning.getItems().addAll(FXCollections.observableArrayList(getCategorie().getListTaskNotRunning()));
				buttonLaunchTask.setDisable(false);
			}else {
				comboBoxAllTaskNotRunning.setDisable(true);
				buttonLaunchTask.setDisable(true);
			}

		}
	}
	/**
	 * When an item is selected in the comboBox, update the comboBox of matching downTasks
	 */
	public void taskSelected() {
		if(!comboBoxAllTaskNotRunning.getSelectionModel().isEmpty()) {
			if(!getTask().getListDownTask().isEmpty()) {
				comboBoxAllDownTaskNotRunning.getItems().clear();
				comboBoxAllDownTaskNotRunning.getItems().addAll(getTask().getListDownTask());
				comboBoxAllDownTaskNotRunning.setDisable(false);
			}else {
				comboBoxAllDownTaskNotRunning.setDisable(true);
			}
			buttonLaunchTask.setDisable(false);
		}
	}
	/**
	 * When the user decide to launch an existent task 
	 */
	public void pressedButtonLaunchTask() {
		tableViewTaskRunning.getItems().clear();
		if(!comboBoxCategorie.getSelectionModel().isEmpty() && comboBoxAllTaskNotRunning.getSelectionModel().isEmpty()) {
			if(!tableViewTaskRunning.getItems().isEmpty() && getCategorie().equals(tableViewTaskRunning.getItems().get(0).getCategorieParent())){
				setAllTaskAndDownTaskToNotRunning();
			}
			for(Task t : getCategorie().getListTask()) {
				t.setRunning(true);
			}
		}
		//If we choose a task
		else if(comboBoxAllDownTaskNotRunning.getSelectionModel().isEmpty() && !comboBoxAllTaskNotRunning.getSelectionModel().isEmpty()) {
			setAllTaskAndDownTaskToNotRunning();
			getTask().setRunning(true);
			getTask().resume();
			//If we choose a downTask
		}else{
			//System.out.println("select downtask");
			for(DownTask d : fullList.getAllDownTask()) {
				d.setRunning(false);
			}
			for(Task t : fullList.getAllTasks()) {
				t.setRunning(false);
			}
			getDownTask().setRunning(true);
			getDownTask().getParent().setRunning(true);
			getDownTask().resume();
			getDownTask().getParent().resume();			
		}
		comboBoxCategorie.requestFocus();
		comboBoxAllDownTaskNotRunning.setDisable(true);
		comboBoxAllTaskNotRunning.setDisable(true);
		refreshListTasks();
	}
	/**
	 * When the user create and launch a new task
	 */
	public void pressedButtonCreateAndLaunch(){
		//if it's just a new task
		if(comboBoxFastTask.getSelectionModel().isEmpty()) {
			Task tmpTask = new Task(textFieldFastTask.getText());
			tmpTask.setCategorieParent(null);
			initializeATask(tmpTask);
			fullList.getOrphanCategorie().addTask(tmpTask);
			fullList.getAllTasks().add(tmpTask);
			setAllTaskAndDownTaskToNotRunning();
			tmpTask.setRunning(true);
			tmpTask.resume();
			//if it's a downTask with a Task specified
		}else {
			DownTask tmpDownTask = new DownTask(textFieldFastTask.getText());
			tmpDownTask.setParent(getFastTask());
			setAllTaskAndDownTaskToNotRunning();
			tmpDownTask.setRunning(true);
			tmpDownTask.resume();
			getFastTask().addDownTask(tmpDownTask);
			getFastTask().setRunning(true);
			getFastTask().resume();
			getFastTask().setCategorieParent(null);
		}
		refreshListTasks();
		comboBoxFastTask.getSelectionModel().clearSelection();
		textFieldFastTask.clear();

	}



	/**
	 * refresh everything concerned by the tasks when something new is running or stopped
	 */
	private void refreshListTasks() {
		if(fullList.getListCategorie().isEmpty()) {
			categorieOrphanTask = new Categorie("Sans categorie");
			categorieOrphanTask.setOrphanCategorie(true);
			if(!fullList.getListCategorie().contains(fullList.getOrphanCategorie())) {
				fullList.addCategorie(categorieOrphanTask);
			}
		}
		ArrayList<Task> running = new ArrayList<Task>();
		ArrayList<Task> notRunning = new ArrayList<Task>();
		listTasksRunning = FXCollections.emptyObservableList();
		for(Categorie c : fullList.getListCategorie()) {
			for(Task t : c.getListTask()) {
				if(t.isRunning()) running.add(t);
				else notRunning.add(t);
			}
		}
		listTasksRunning = FXCollections.observableArrayList(running);
		listTasksNotRunning = FXCollections.observableArrayList(notRunning);
		comboBoxCategorie.getItems().clear();
		comboBoxCategorie.getItems().addAll(fullList.getListCategorie());
		comboBoxAllTaskNotRunning.getItems().clear();
		comboBoxAllTaskNotRunning.getItems().addAll(notRunning);
		comboBoxAllDownTaskNotRunning.getItems().clear();
		comboBoxAllDownTaskNotRunning.getItems().addAll(fullList.getAllDownTask());
		tableViewTaskRunning.getItems().clear();
		tableViewTaskRunning.setItems(listTasksRunning);
		comboBoxFastTask.getItems().clear();
		comboBoxFastTask.getItems().addAll(fullList.getAllTasks());
		tabPane.getSelectionModel().select(0);
		if(!listTasksRunning.isEmpty()) {
			tableViewTaskRunning.requestFocus();
			tableViewTaskRunning.getSelectionModel().select(0);
			tableViewTaskRunning.getFocusModel().focus(0);		
			labelCategorie.setVisible(true);
			if(listTasksRunning.get(0).isWithoutCategorie()) {
				labelCategorie.setText("Tâches sans catégorie");
			}else {
				labelCategorie.setText("Tâches de la catégorie: " + listTasksRunning.get(0).getCategorieParent().getName());
			}
		}

	}
	/**
	 * Refresh the comboBox in the launchTab
	 */
	private void refreshTabLaunch() {
		comboBoxCategorie.getSelectionModel().clearSelection();
		comboBoxAllTaskNotRunning.getSelectionModel().clearSelection();
		comboBoxAllDownTaskNotRunning.getSelectionModel().clearSelection();
		comboBoxAllDownTaskNotRunning.setDisable(true);
		comboBoxAllTaskNotRunning.setDisable(true);

	}
	/**
	 * Initialize the tableview and comboBox with the information from the Liste
	 */
	public void initialization() {
		listTasks = FXCollections.observableArrayList(fullList.getAllTasks());
		tableViewTaskRunning.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
			@Override
			public void changed(ObservableValue<? extends Task> observable, Task oldValue,Task newValue) {
				if(newValue != null) {
					initializeListDownTask(getTaskTableView());
					tableViewDownTaskRunning.setItems(FXCollections.observableArrayList(getTaskTableView().getListDownTask()));
				}else{
					tableViewDownTaskRunning.getItems().clear();
					tableViewDownTaskRunning.refresh();
				}

			}

		});
		initializeListTask();
		//So we can watch the evolution of the value in the tableviews
		columnTaskName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnProgress.setCellValueFactory(new PropertyValueFactory<>("elapsedTimeString"));
		columnResume.setCellValueFactory(new PropertyValueFactory<>("resume"));
		columnStop.setCellValueFactory(new PropertyValueFactory<>("stop"));
		columnDownTaskName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnDProgress.setCellValueFactory(new PropertyValueFactory<>("elapsedTimeString"));
		columnDResume.setCellValueFactory(new PropertyValueFactory<>("resume"));
		columnDStop.setCellValueFactory(new PropertyValueFactory<>("stop"));


	}
	/**
	 * Initialize aTask with the buttons and their handler
	 * @param atask the task that will be initialized
	 */
	public void initializeATask(Task atask) {
		Button resume = new Button("Resume");
		if(atask.isRunning()) {
			atask.resume();
		}
		resume.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) {
				if(atask.isRunning()) {
					for(DownTask d : atask.getListDownTask()) {
						d.setRunning(false);
					}
					atask.setRunning(false);
					atask.resume();
				}else {
					setAllTaskAndDownTaskToNotRunning();
					atask.setRunning(true);
					setTaskStillRunning(atask);
					atask.resume();
				}
			}
		});
		Button stop = new Button("Stop");
		stop.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) {
				atask.setRunning(false);
				for(DownTask d : atask.getListDownTask()) {
					d.setRunning(false);
				}
				listTasksRunning.remove(atask);
			}			
		});
		atask.setResume(resume);
		atask.setStop(stop);
	}
	/**
	 * Set the corresponding downTasks in the tableView of downTasks
	 * @param task : the parent task
	 */
	protected void initializeListDownTask(Task task) {
		for(DownTask d : task.getListDownTask()) {
			initializeADownTask(d, task);
		}
		tableViewDownTaskRunning.setItems(FXCollections.observableArrayList(getTaskTableView().getListDownTask()));
	}


	/**
	 * Create the buttons and their handler of a downTask
	 * @param d the downTask initialized
	 * @param task the parent task of the down task
	 */
	public void initializeADownTask(DownTask d, Task task) {
		Button resume = new Button("Resume");
		if(d.isRunning()) {
			d.resume();
		}
		resume.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) {
				if(d.isRunning()) {
					d.setRunning(false);
					d.resume();
				}else {
					setAllTaskAndDownTaskToNotRunning();
					d.setRunning(true);
					task.setRunning(true);
					task.resume();
					setTaskStillRunning(task);
					d.resume();
				}
			}
		});
		Button stop = new Button("Stop");
		stop.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) {
				d.setRunning(false);
				tableViewDownTaskRunning.getItems().remove(d);
				System.out.println("Arret de la sous tâche");	
			}			
		});
		d.setResume(resume);
		d.setStop(stop);
	}
	/**
	 * Create the buttons and handler of the tasks
	 */
	public void initializeListTask() {
		for(Task t : listTasks) {
			initializeATask(t);
		}
		textFieldFastTask.textProperty().addListener(Observable ->{
			if(!textFieldFastTask.getText().isEmpty()) {
				buttonFastTaskCreateAndLaunch.setDisable(false);
				comboBoxFastTask.setDisable(false);
			}else { 
				buttonFastTaskCreateAndLaunch.setDisable(true);
				comboBoxFastTask.setDisable(true);
			}

		});
		refreshListTasks();
	}
	/**
	 * When we push the reset button, it reset the comboBox
	 */
	public void pressedButtonResetSelection() {
		refreshTabLaunch();
	}
	public void setAllTaskAndDownTaskToNotRunning() {
		for(Task t : fullList.getAllTasks()) {
			t.setRunning(false);
			t.resume();
			for(DownTask d : t.getListDownTask()) {
				d.setRunning(false);
				d.resume();
			}
		}
	}

	/**
	 * Save the list on shutDown used when the main window is closed
	 */
	public void shutDown() {
		setAllTaskAndDownTaskToNotRunning();
		try {
			Liste.saveListe(fullList, Accueil.saveFile);
			//we need to kill the thread else the JVM will run even after closing window
			refresh.cancel();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println("Shutdown");
	}

}

