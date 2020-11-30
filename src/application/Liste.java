package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
 
public class Liste implements Serializable{
	//attributes
	private static final long serialVersionUID = -5207930753423101606L;
	private ArrayList<Categorie> listCategorie;

	//constructors
	public Liste(ArrayList<Categorie> aListCategorie) {
		this.listCategorie.addAll(aListCategorie);
	}

	public Liste() {
		this.listCategorie = new ArrayList<Categorie>();
	}
	//getter and setter

	public ArrayList<Categorie> getListCategorie() {
		return listCategorie;
	}

	public void setListCategorie(ArrayList<Categorie> listCategorie) {
		this.listCategorie = listCategorie;
	}
	//methods

	public String toString() {
		for(Categorie c : listCategorie) {
			System.out.println(c.toString());
			for(Task t : c.getListTask()) {
				System.out.println(t.toString());
				for(DownTask d : t.getListDownTask()) {
					System.out.println(d.toString());
				}
				System.out.println("\n");
			}
			System.out.println("\n");
		}
		return "";
	}

	/**
	 * Add a categorie to the arrayList of categories
	 * @param acategorie the categorie that will be added
	 */
	public void addCategorie(Categorie acategorie) {
		this.listCategorie.add(acategorie);
	}

	/**
	 * Save the list
	 * @param listeGenerale the list to be saved
	 * @param saveFile the file that will be used for saving
	 * @throws FileNotFoundException
	 */
	public static void saveListe(Liste listeGenerale, File saveFile) throws FileNotFoundException {
		try {
			ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(saveFile));
			//System.out.println("saving file");
			save.writeObject(listeGenerale);
			save.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param saveFile the file that will be loaded
	 * @return the list loaded
	 * @throws ClassNotFoundException
	 */
	public static Liste loadListe(File saveFile) throws ClassNotFoundException {
		Liste tmpListe = new Liste();
		try{
			ObjectInputStream save = new ObjectInputStream(new FileInputStream(saveFile));
			//System.out.println("reading file");
			tmpListe = (Liste) save.readObject();
			save.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmpListe;
	}
	/**
	 * Create a categorie and add all the orphanTasks to it
	 * @return A categorie with all the orphanTasks
	 */
	public Categorie categorieOrphanTask() {
		Categorie categorieOrphanTask = new Categorie("Non classés");
		for(Task t : getAllDownTask()) {
			categorieOrphanTask.addTask(t);
		}
		return categorieOrphanTask;
	}
	/**
	 * 
	 * @return the list of all categories except the orphan one
	 */
	public ArrayList<Categorie> getListCategorieWithoutOrphanCategorie() {
		ArrayList<Categorie> listCategorieWithoutOrphanCategorie = new ArrayList<Categorie>();
		for(Categorie c : listCategorie) {
			listCategorieWithoutOrphanCategorie.add(c);
		}
		//System.out.println(listCategorieWithoutOrphanCategorie);
		listCategorieWithoutOrphanCategorie.remove(getOrphanCategorie());
		//System.out.println(listCategorieWithoutOrphanCategorie);
		return listCategorieWithoutOrphanCategorie;
	}
	/**
	 * 
	 * @return an arrayList of all tasks
	 */
	public ArrayList<Task> getAllTasks() {
		ArrayList<Task> tmp = new ArrayList<Task>();
		if(!listCategorie.isEmpty())
			for(Categorie c : listCategorie) {
				if(!c.getListTask().isEmpty())
					for(Task t : c.getListTask()) {
						tmp.add(t);
					}
			}
		return tmp;
	}
	public ArrayList<Task> getOrphanTask(){
		ArrayList<Task> tmp = new ArrayList<Task>();
		if(!listCategorie.isEmpty()) {
			for(Categorie c : listCategorie) {
				for(Task t : c.getListTask()) {
					if(t.getCategorieParent() == null) tmp.add(t);
				}
			}
		}
		return tmp;
	}
	/**
	 * 
	 * @param c the categorie where we search the task
	 * @return the list of tasks of this categorie
	 */
	public ArrayList<Task> getTaskOfaCategorie(Categorie c){
		ArrayList<Task> tmp = new ArrayList<Task>();
		if(listCategorie.lastIndexOf(c) != -1) {
			tmp.addAll(listCategorie.get(listCategorie.lastIndexOf(c)).getListTask());
		}
		return tmp;
	}
	/**
	 * Add a task to a particular categorie
	 * @param c the categorie where the task will be added
	 * @param t the task to be added
	 */
	public void setTaskOfaCategorie(Categorie c,Task t){
		listCategorie.get(listCategorie.lastIndexOf(c)).getListTask().add(t);
	}
	/**
	 * Remove a task from a particular categorie
	 * @param c the categorie where the task will be removed
	 * @param t the task to be removed
	 */
	public void removeTaskOfaCategorie(Categorie c,Task t){
		listCategorie.get(listCategorie.lastIndexOf(c)).getListTask().remove(t);
	}

	/**
	 * Add a task to the orphanTask arrayList
	 * @param t The task added
	 */
	/*public void addTaskToOrphanTask(Task t) {
		this.orphanTask.add(t);
	}*/
	/**
	 * Remove a downtask
	 * @param aDownTask to be removed
	 */
	public void removeADownTaskFromATaskThatIsInACategorie(Categorie c, Task aTask,DownTask aDownTask) {
		int indexC = listCategorie.indexOf(c);
		int indexT = listCategorie.get(indexC).getListTask().indexOf(aTask);
		listCategorie.get(indexC).getListTask().get(indexT).getListDownTask().remove(aDownTask);
	}
	/**
	 * 
	 * @return ALL downtasks
	 */
	public ArrayList<DownTask> getAllDownTask() {
		ArrayList<DownTask> tmp = new ArrayList<DownTask>();
		for(Categorie c : listCategorie) {
			for(Task t : c.getListTask()) {
				for(DownTask d : t.getListDownTask()) {
					tmp.add(d);
				}
			}
		}
		return tmp;
	}

	/**
	 * 
	 * @return all tasks currently running
	 */
	public ArrayList<Task> getAllTaskRunning() {
		ArrayList<Task> tmp = new ArrayList<Task>();
		for(Categorie c : listCategorie) {
			for(Task t : c.getListTask()) {
				if(t.isRunning())
					tmp.add(t);
			}
		}
		return tmp;
	}

	/*
	 * @return the list of all tasks currently not running
	 */
	public ArrayList<Task> getAllTaskNotRunning() {
		ArrayList<Task> tmp = new ArrayList<Task>();
		for(Categorie c : listCategorie) {
			for(Task t : c.getListTask()) {
				if(!t.isRunning())
					tmp.add(t);
			}
		}
		return tmp;
	}
	/**
	 * Add a downTask to a Task specified
	 * @param selectedItem the task where we add the downTask
	 * @param aDownTaskToAdd The downTask added to the selected Task
	 */
	public void addADownTaskToATask(Task selectedItem, DownTask aDownTaskToAdd) {
		for(Categorie c : listCategorie) {
			for(Task t : c.getListTask()) {
				if(t.equals(selectedItem)) {
					t.addDownTask(aDownTaskToAdd);
				}
			}
		}
	}
	/**
	 * remove a task from all the list
	 * @param selectedItem the task to be removed
	 * 
	 */
	public boolean removeATask(Task selectedItem) {
		for(int i = 0; i < listCategorie.size(); i++) {
			if(listCategorie.get(i).getListTask().contains(selectedItem)) {
				listCategorie.get(i).getListTask().remove(selectedItem);
				return true;
			}
		}
		return false;
	}
	/**
	 * Move a task from a categorie to another
	 * @param start the initial categorie
	 * @param finish the categorie where the task will be moved
	 * @param taskMove the task that will be moved
	 */
	public void moveATaskFromACategorieToAnother(Categorie start, Categorie finish, Task taskMove) {
		start.removeTask(taskMove);
		finish.addTask(taskMove);
	}
	/**
	 * Move a task from orphanList to a categorie
	 * @param finish the categorie where the task will be moved
	 * @param taskMove the task that will be moved
	 */
	public void moveATaskFromOrphanToAnother(Categorie finish, Task taskMove) {
		getOrphanCategorie().removeTask(taskMove);
		taskMove.setCategorieParent(finish);
		finish.addTask(taskMove);
	}
	/**
	 * Rename a categorie specified
	 * @param toBeRenamed the categorie that will be renamed
	 * @param newName the newName of the categorie
	 */
	public void renameCategorie(Categorie toBeRenamed, String newName) {
		listCategorie.get(listCategorie.indexOf(toBeRenamed)).setName(newName);
	}
	/**
	 * Rename a task specified
	 * @param whereTheTaskIs the categorie that contains the task
	 * @param toBeRenamed the task that will be renamed
	 * @param newName the new name of the task
	 */
	public void renameATask(Categorie whereTheTaskIs, Task toBeRenamed, String newName) {
		listCategorie.get(listCategorie.indexOf(whereTheTaskIs)).getListTask().get(listCategorie.get(listCategorie.indexOf(whereTheTaskIs)).getListTask().indexOf(toBeRenamed)).setName(newName);
	}
	/**
	 * Rename a downtask specified
	 * @param whereTheTaskIs the categorie that contains the task
	 * @param whereTheDownTaskIs the task where the downTask is
	 * @param toBeRenamed the downtask that will be renamed
	 * @param newName the new name of the task
	 */
	public void renameADowntask(Categorie whereTheTaskIs,Task whereTheDownTaskIs, DownTask toBeRenamed, String newName) {
		int indexC = listCategorie.indexOf(whereTheTaskIs);
		int indexT = listCategorie.get(indexC).getListTask().indexOf(whereTheDownTaskIs);
		int indexD = listCategorie.get(indexC).getListTask().get(indexT).getListDownTask().indexOf(toBeRenamed);
		listCategorie.get(indexC).getListTask().get(indexT).getListDownTask().get(indexD).setName(newName);
	}
	/**
	 * Rename a task contained in the orphanTask categorie
	 * @param toRename the task that will be renamed
	 * @param newName the new name of the task
	 */
	
	public Categorie getOrphanCategorie() {
		for(Categorie c : listCategorie) {
			if(c.isOrphanCategorie())
				return c;
		}
		return new Categorie("Sans catégorie");
	}
	/*public void renameAnOrphanTask(Task toRename, String newName) {
		this.orphanTask.get(this.orphanTask.indexOf(toRename)).setName(newName);
	}*/

}