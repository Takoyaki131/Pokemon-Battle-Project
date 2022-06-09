package pokemon;

public abstract class StatModifierMove extends Move{
	private int level_effect;
	private boolean target_self;
	public StatModifierMove(int tm_no, String name, String type, int max_pp, double accuracy, int level_effect, boolean target_self) {
		super(tm_no, name, type, max_pp, accuracy);
		this.level_effect = level_effect;
		this.target_self = target_self;
	}
	
	public int getLevel_effect()
	{
		return level_effect;
	}
	
	public boolean getTarget_self()
	{
		return target_self;
	}
}
