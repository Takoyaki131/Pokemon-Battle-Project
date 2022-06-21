package move_tree;

public abstract class Move {
	// Class Attributes
	/* Notes
	 * double accuracy : probability for a move to hit
	 * calculated by (pokemon's accuracy * move accuracy) max value: 1.00
	 */
	protected int tm_no;
	protected String name;
	protected String type;
	protected int current_pp;
	protected int max_pp;
	protected double accuracy;
	
	// CONSTRUCTOR
	public Move(int tm_no, String name, String type, int max_pp, double accuracy) {
		super();
		this.tm_no = tm_no;
		this.name = name;
		this.type = type;
		this.current_pp = max_pp;
		this.max_pp = max_pp;
		this.accuracy = accuracy;
	}

	/* Displays general information about the move
	 * Inputs:
	 * none
	 * Returns:
	 * none
	 */
	public void displayMoveInfo() {
		System.out.println("TM No. " + tm_no + " - " + name);
		System.out.println("Type: " + type + " Accuracy: " + accuracy);
		System.out.println("PP: " + current_pp + " / " + max_pp);
	}
	// GETTERS	
	public int getTmNo() {
		return tm_no;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}

	public int getCurrent_pp() {
		return current_pp;
	}

	public int getMax_pp() {
		return max_pp;
	}

	public double getAccuracy() {
		return accuracy;
	}

	// SETTERS
	public void setType(String type) {
		this.type = type;
	}

	public void setCurrent_pp(int current_pp) {
		this.current_pp = current_pp;
	}

	public void setMax_pp(int max_pp) {
		this.max_pp = max_pp;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
	
}
