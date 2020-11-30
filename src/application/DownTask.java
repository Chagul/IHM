package application;

public class DownTask extends Task{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -6214285540338736432L;
	private Task parent;
	
	public DownTask(String name) {
		super(name);
		this.setName(name);
	}
	
	public void setParent(Task parent) {
		this.parent = parent;
	}
	
	public Task getParent() {
		return this.parent;
	}
}
