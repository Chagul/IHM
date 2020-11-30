package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.control.Button;
 
public class Task implements Serializable{
	/**
	 * 
	 */

	private static final long serialVersionUID = 3580179757552316437L;
	private String name;
	private ArrayList<DownTask> listDownTask;
	private transient Timer actualTime;
	private long elapsedTimeSeconds;
	private boolean isRunning;
	private transient Button resume;
	private transient Button stop;
	private String elapsedTimeString;
	private Categorie categorieParent;

	//Constructor
	public Task(String name, Categorie parent) {
		this.name = name;
		this.listDownTask = new ArrayList<DownTask>();
		this.isRunning = false;
		this.resume = (new Button("RESUME"));
		this.stop = (new Button("STOP"));
		this.elapsedTimeSeconds = 0;
		this.elapsedTimeString = "";
		this.actualTime = new Timer();
		this.categorieParent = parent;
	}
	public Task(String name) {
		this.name = name;
		this.listDownTask = new ArrayList<DownTask>();
		this.elapsedTimeSeconds = 0;
		this.elapsedTimeString = "";
		this.actualTime = new Timer();
		this.categorieParent = null;
	}

	//getters and setters

	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public Timer getActualTime() {
		return actualTime;
	}

	public void setActualTime(Timer actualTime) {
		this.actualTime = actualTime;
	}

	public ArrayList<DownTask> getListDownTask() {
		return listDownTask;
	}

	public void setListDownTask(ArrayList<DownTask> listDownTask) {
		this.listDownTask = listDownTask;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void addDownTask(DownTask tmp) {
		this.listDownTask.add(tmp);		
	}
	public Button getResume() {
		return resume;
	}
	public void setResume(Button resume) {
		this.resume = resume;
	}
	public Button getStop() {
		return stop;
	}
	public void setStop(Button stop) {
		this.stop = stop;
	}
	public long getElapsedTime() {
		return elapsedTimeSeconds;
	}
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTimeSeconds = elapsedTime;
	}
	public String getElapsedTimeString() {
		return elapsedTimeString;
	}
	public void setElapsedTimeString(String elapsedTimeString) {
		this.elapsedTimeString = elapsedTimeString;
	}
	public Categorie getCategorieParent() {
		return categorieParent;
	}
	public void setCategorieParent(Categorie parent) {
		this.categorieParent = parent;
	}
	//Methods
	@Override
	public String toString() {
		return this.name;
	}
	/**
	 * 
	 * @return true if this task has no categories attached to, else false
	 */
	public boolean isWithoutCategorie() {
		return categorieParent == null;
	}
	/**
	 * Start the timer if it's not running, stop it if it's running
	 */
	public void resume() {
		Timer actualTime = new Timer();
		actualTime.schedule(new TimerTask() {
			long startTime = System.currentTimeMillis();
			long tmpElapsedTime = getElapsedTime();
			@Override
			public void run() {
				long tmp = new Date().getTime();
				if(isRunning) {
					setElapsedTime(tmpElapsedTime + (tmp - startTime)/1000);
					setElapsedTimeString( elapsedTimeSeconds/3600 + "h" + (elapsedTimeSeconds/60)%60  + "min" + elapsedTimeSeconds%60 + "s");  
				}else {
					setElapsedTime(tmpElapsedTime + (tmp - startTime)/1000);
					actualTime.cancel();
				}
			}

		}, 0,1000);
	}


	/**
	 * 
	 * @return Return the downTask of a task that are running
	 */
	public ArrayList<DownTask> getListDownTaskRunning() {
		ArrayList<DownTask> tmp = new ArrayList<DownTask>();
		for(DownTask d : this.listDownTask) {
			if(d.isRunning()) {
				tmp.add(d);
			}
		}
		return tmp;
	}
}
