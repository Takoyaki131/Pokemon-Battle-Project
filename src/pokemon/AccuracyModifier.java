package pokemon;

public class AccuracyModifier extends StatModifierMove {

	public AccuracyModifier(int tm_no, String name, String type, int max_pp, double accuracy,
			int level_effect, boolean target_self) {
		super(tm_no, name, type, max_pp, accuracy, level_effect, target_self);
	}
}
