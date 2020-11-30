package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

public class Categorie implements Serializable{ 


	//attributes
	private static final long serialVersionUID = 3688363494479728700L;
	private String name;
	private ArrayList<Task> listTask;
	private long Time;
	private boolean isOrphanCategorie;
	
	//Constructors
	public Categorie(String name, List<Task> listTache) {
		this.name = name;
		this.listTask = (ArrayList<Task>) listTache;
		this.isOrphanCategorie = false;
	}

	public Categorie(String name) {
		this.name = name;
		this.listTask = new ArrayList<Task>();
		this.isOrphanCategorie = false;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	//getter and setters
	public ArrayList<Task> getListTask() {
		return listTask;
	}
	
	public void setListTask(List<Task> listTache) {
		this.listTask = (ArrayList<Task>) listTache;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getTime() {
		return Time;
	}

	public void setTime(long time) {
		Time = time;
	}
	

	public boolean isOrphanCategorie() {
		return isOrphanCategorie;
	}

	public void setOrphanCategorie(boolean isOrphanCategorie) {
		this.isOrphanCategorie = isOrphanCategorie;
	}

	//methods
	/**
	 * Remove a categorie
	 * @param c the list of all categories
	 * @param toBeRemoved the categorie to be removed
	 */
	public static void removeCategorie(ArrayList<Categorie> c, Categorie toBeRemoved) {
		c.remove(toBeRemoved);
	}
	/**
	 * add multiple tasks to a categorie
	 * @param observableList the list of all tasks that will be added
	 */
	public void addTask(ObservableList<Task> observableList) {
		this.listTask.addAll(observableList);
	}
	/**
	 * Add only one task to a categorie
	 * @param taskMove the task added
	 */
	public void addTask(Task taskAdded) {
		this.listTask.add(taskAdded);
	}

	/**
	 * Remove a task from a categorie
	 * @param taskMove the task removed
	 */
	public void removeTask(Task taskMove) {
		this.listTask.remove(taskMove);	
	}
	/**
	 * Get the total of time passed on a categorie
	 * @return the total
	 */
	public long getTimeElapsedOfACategorie() {
		Long timeElapsed = 0L;
		for(Task t : this.getListTask()) {
			timeElapsed += t.getElapsedTime();
		}
		return timeElapsed;
	}
	/**
	 * Get the total of time passed on a categorie on format String
	 * @return the total in format hours minutes seconds
	 */
	public String getTimeElapsedOfACategorieString() {
		Long timeElapsed = 0L;
		for(Task t : this.getListTask()) {
			timeElapsed += t.getElapsedTime();
		}
		return (timeElapsed/3600 + "h" + (timeElapsed/60)%60  + "min" + timeElapsed%60 + "s");
	}

	public ArrayList<Task> getListTaskNotRunning() {
		ArrayList<Task> taskNotRunning = new ArrayList<Task>();
		for(Task t : listTask) {
			if(!t.isRunning() ) taskNotRunning.add(t);
		}
		return taskNotRunning;
	}

	public void removeAll(ObservableList<Task> selectedItems) {
		this.listTask.removeAll(selectedItems);
		
	}
}