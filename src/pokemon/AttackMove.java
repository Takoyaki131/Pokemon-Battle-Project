package pokemon;

public abstract class AttackMove extends Move{
	protected int power;

	public AttackMove(int tm_no, String name, String type, int max_pp, double accuracy, int power) {
		super(tm_no, name, type, max_pp, accuracy);
		this.power = power;
	}
	
	public int getPower()
	{
		return power;
	}
}
