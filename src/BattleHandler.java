import pokemon.*;
import java.util.Scanner;

public class BattleHandler {
	private Pokemon left_player;
	private Pokemon right_player;
		
	// Constructor
	public BattleHandler(Pokemon left_player, Pokemon right_player) {
		super();
		this.left_player = left_player;
		this.right_player = right_player;
	}

	public void startBattle()
	{
		// reset stats from a previous battle
		left_player.resetBattleStats();
		right_player.resetBattleStats();
		Battle();
		EndBattle();
	}
	
	public void EndBattle()
	{
		
	}
	/* Calculate damage WIP
	 *  Formula ((((2 * Level / 5 + 2) * AttackStat * AttackPower / DefenseStat) / 50) + 2) * STAB * Weakness/Resistance * RandomNumber / 100 )
	 */
	public int calcDamage(int level, int attack, int defense, int power, double type_bonus)
	{
		int damage = (int) (((((2 * level / 5 + 2) * attack * power / defense) / 50) + 2) * type_bonus );
		return damage;
	}
	
	/* Determine bonus based on move type -> target_type WORK IN PROGRESS
	 *  
	 */
	public double getTypeMultiplier(String move_type, String target_type)
	{
		// Normal 
		// Fire 
		// Water 
		// Electric
		return 1.0;
	}
	
	
	/* Logic for when a pokemon uses a move
	 * 
	 */
	public void useMove(Pokemon user, Pokemon target, Move move)
	{
		/* Determine move success
		 * Basic created formula (user accuracy * move accuracy * target evasion) max value = 100 <-- probability
		 * Randomize number, if random number falls under the success value - move is successful
		 * Note: This is not the official formula
		 */
		
		System.out.println(user.getName() + " used " + move.getName() + "! ");
		
		// Damage success logic
		double chance = (user.getBattle_accuracy() * move.getAccuracy() * target.getBattle_evasion() );
		System.out.println("Move has a " + (chance) + "% to hit!");
		double result = (int) (Math.random() * 100) ;
		System.out.println("Result number: " + result);
		
		// If move missed, return to second half of the turn
		if (result > chance) 
		{
			// Print move outcome
			System.out.println("The move missed!");
			return;
		}
		
		// Print move outcome
		System.out.println("The move hit!");
		
		// WIP
		// Determine weakness/resistance value
		double type_bonus = getTypeMultiplier(move.getType(), target.getType_one());
		
		
		// Determine move class, move class dictates what pokmeon stat to use in calculation
		int damage; // variable holding the damage dealt by move
		
		// If move is an attacking move
		if (move instanceof AttackMove)
		{
			// Physical Attack
			if(move instanceof PhysicalAttack)
			{
				damage = calcDamage(user.getLevel(), user.getBattle_attack(), target.getBattle_defense(), ((PhysicalAttack) move).getPower(), type_bonus);
				System.out.println("Move did " + damage + " damage! ");
				target.takeDamage(damage);
			}
			
			// Special Attack
			if(move instanceof SpecialAttack)
			{
				damage = calcDamage(user.getLevel(), user.getBattle_special_attack(), target.getBattle_special_defense(), ((SpecialAttack) move).getPower(), type_bonus);
				System.out.println("Move did " + damage + " damage! ");
				target.takeDamage(damage);
			}
		}
		
		// If move is a StatModifierMove and targets the enemy pokemon
		if (move instanceof StatModifierMove && ((StatModifierMove) move).getTarget_self() == false)
		{
			// Attack
			if (move instanceof AttackModifier)
			{
				System.out.println(target.getName() + "'s attack was changed!");	
				target.setBattle_attack(((target.getBase_attack() + target.getIv_values().get(1)) * 2 * (target.getLevel() + ((StatModifierMove) move).getLevel_effect()) / 100 ) + 5);
			}
			// Defense
			if (move instanceof DefenseModifier)
			{
				System.out.println(target.getName() + "'s defense was changed!");	
				target.setBattle_defense((((target.getBase_defense() + target.getIv_values().get(2)) * 2 *((StatModifierMove) move).getLevel_effect()) / 100 ) + 5);
			}
			// Special Attack
			if (move instanceof SpecialAttackModifier)
			{
				System.out.println(target.getName() + "'s special attack was changed!");	
				target.setBattle_special_attack((((target.getBase_special_attack() + target.getIv_values().get(3)) * 2 *((StatModifierMove) move).getLevel_effect()) / 100 ) + 5);
			}
			// Special Defense
			if (move instanceof SpecialDefenseModifier)
			{
				System.out.println(target.getName() + "'s special defense was changed!");	
				target.setBattle_special_defense((((target.getBase_special_defense() + target.getIv_values().get(4)) * 2 *((StatModifierMove) move).getLevel_effect()) / 100 ) + 5);
			}
			// Speed
			if (move instanceof SpeedModifier)
			{
				System.out.println(target.getName() + "'s speed was changed!");	
				target.setBattle_speed((((target.getBase_speed() + target.getIv_values().get(5)) * 2 *((StatModifierMove) move).getLevel_effect()) / 100 ) + 5);
			}
			// Accuracy
			
			// Evasion
		}
	}
	
	/* Checks for a fainted pokemon (hp < 0)
	 * Input: 	user 	- Pokemon using the move
	 * 			target 	- Pokemon recieveing the move
	 * Returns: Boolean
	 * 			True 	- Pokmeon has fainted, battle is over
	 * 			False 	- Pokemon still has hp to fight, continue battle
	 */
	public boolean isFainted(Pokemon target)
	{
		if(target.getCurrent_hp() <= 0) return true;
		return false;
	}
	
	/* Logic for each half of a single turn of a pokemon battle
	  * Input: 	first 	- Pokemon using the move
	 * 			second 	- Pokemon recieveing the move
	 * Returns: Integer
	 * 			0 		- Battle is still undecided, move to next turn
	 * 			1		- The pokmeon who went second fainted, the first pokemon wins
	 * 			2 		- The pokemon who went first fainted, the second pokemon wins
	 */
	private int executeTurn(Pokemon first, Pokemon second)
	{
		Scanner kb = new Scanner(System.in);
		int choice;
		Move move_used;
		
		System.out.println();
		// First half of turn
		
		// Display hp
		System.out.println(left_player.getName() + "\tHP: " + left_player.getCurrent_hp() + " / " + left_player.getCurrent_max_hp());
		System.out.println(right_player.getName() + "\tHP: " + right_player.getCurrent_hp() + " / " + right_player.getCurrent_max_hp());
		System.out.println();
		
		// Display moves
		System.out.println("What will " + first.getName() + " do? ");
		first.displayCurrentMoves();
		
		// Get user choice of move to use
		choice = kb.nextInt();
		move_used = first.getCurrent_moves()[choice-1];
		
		// Execute the move used
		useMove(first, second, move_used);
		
		// Check if result of move is a fainted pokemon
		if (isFainted(second) == true)
		{
			kb.close();
			return 1;
		}
		
		System.out.println();
		// Second half of turn
		
		// Display hp
		System.out.println(left_player.getName() + "\tHP: " + left_player.getCurrent_hp() + " / " + left_player.getCurrent_max_hp());
		System.out.println(right_player.getName() + "\tHP: " + right_player.getCurrent_hp() + " / " + right_player.getCurrent_max_hp());
		System.out.println();
		
		// Display moves
		System.out.println("What will " + second.getName() + " do? ");
		second.displayCurrentMoves();
		
		// Get user choice of move to use
		choice = kb.nextInt();
		
		move_used = second.getCurrent_moves()[choice-1];
		
		// Execute the move used
		useMove(second, first, move_used);
		
		// Check if result of move is a fainted pokemon
		if (isFainted(first) == true)
		{
			kb.close();
			return 2;
		}
		
		return 0;
	}
	
	
	public void Battle()
	{
		/* variable to hold the current status of the fight
		 * value is derived from the result of the turn method
		 * 		0 	- Battle is still undecided, move to next turn
		 * 		1	- The pokmeon who went second fainted, the first pokemon wins
		 * 		2 	- The pokemon who went first fainted, the second pokemon wins
		 */
		int battle_status = 0; 
		
		// Keep battling untill one pokemon faints
		while (battle_status == 0)
		{
			// Determine who goes first
			if(left_player.getBattle_speed() > right_player.getBattle_speed()) 
			{
				battle_status = executeTurn(left_player, right_player);
				if(battle_status == 1)
				{
					System.out.println(right_player.getName() + " fainted! " + left_player.getName() + " wins! ");
					return;
				}
			}
			else
			{
				battle_status = executeTurn(right_player, left_player);
				if(battle_status == 2)
				{
					System.out.println(left_player.getName() + " fainted! " + right_player.getName() + " wins! ");
					return;
				}
			}
		}
	}
}
