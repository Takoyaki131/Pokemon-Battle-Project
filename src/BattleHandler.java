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
		left_player.resetBattleStats();
		right_player.resetBattleStats();
	}
	
	
	/* Calculate damage WIP
	 *  Formula ((((2 * Level / 5 + 2) * AttackStat * AttackPower / DefenseStat) / 50) + 2) * STAB * Weakness/Resistance * RandomNumber / 100 )
	 */
	private int calcDamage(int level, int attack, int defense, int power, double type_bonus)
	{
		int damage = (int) (((((2 * level / 5 + 2) * attack * power / defense) / 50) + 2) * type_bonus );
		
		return damage;
	}
	
	/* Determine bonus based on move type -> target_type WORK IN PROGRESS
	 *  
	 */
	private double getTypeMultiplier(String move_type, String target_type)
	{
		// Normal 
		// Fire 
		// Water 
		// Electric
		return 1.0;
	}
	
	/* Checks if a pokemon is able to use a move through confusion or status condition
	 * 		true - the pokemon passes both conditions
	 * 		false - the pokemon has failed either conditions, the move fails and moves on to next move
	 * Status conditions that can rpevent a pokemon from using a move
	 * 		- paralyze 	25% chance to not move
	 * 		- frozen   	20% chance to thaw out each turn
	 * 		- sleep		last randomly 1-7 turns
	 */
	private boolean canUseMove(Pokemon user)
	{
		/* Check for confusion 
		 * 	- 50% chance to hurt itself
		 *  - damage is 40 power typeless physical attack
		 */
		if (user.getIs_confused() == true)
		{
			int chance_to_hurt = (int) (Math.random() * 2);
			if (chance_to_hurt != 1) return true;
			// hurt itself in confusion
			else
			{
				System.out.println(user.getName() + " hurt itself in confusion");
				int damage = (int) (((((2 * user.getLevel() / 5 + 2) * user.getBattle_attack() * 40 / user.getBattle_defense()) / 50) + 2));
				user.takeDamage(damage);
			}
		}
		// Paralyze condition - 25% chance to not move, speed reduced by 25%
		if (user.getBattle_status() == "PAR")
		{
			int chance_to_be_paralyzed = (int) (Math.random() * 4);
			System.out.println("Chance Number: " + chance_to_be_paralyzed);
			if (chance_to_be_paralyzed != 1) return true;
			else
				System.out.println(user.getName() + " is paralyzed! It can't move!");
				return false;
		}
		
		// Frozen condition - 20% chance to thaw out each turn
		if (user.getBattle_status() == "FRZ")
		{
			int chance_to_thaw = (int) (Math.random() * 5);
			System.out.println("Chance Number: " + chance_to_thaw);
			if (chance_to_thaw == 1)
			{
				System.out.println(user.getName() + " thawed out!");
				return true;
			}
			else
				System.out.println(user.getName() + " is frozen! It can't move!");
				return false;
		}
		
		// Sleep condition
		if (user.getBattle_status() == "SLP")
		{
			user.setSleep_turns(user.getSleep_turns() - 1);
			if (user.getSleep_turns() != 0)
			{
				System.out.println(user.getName() + " is asleep!");
				return false;
			}
			else
			{
				System.out.println(user.getName() + " woke up!");
				return true;
			}
		}
		return true;
	}
	
	// Logic for when a pokemon uses a move
	private void useMove(Pokemon user, Pokemon target, Move move)
	{
		
		// Check confusion and status
		if (canUseMove(user) == false) return;
		
		// Check for move success, i.e the move hit or missed
		System.out.println(user.getName() + " used " + move.getName() + "! ");
		
		// Damage success logic
		double chance = (user.getBattle_accuracy() * move.getAccuracy() * target.getBattle_evasion() );
		double result = (Math.random() ) ;
		
		/* Extra information on move success
		System.out.println("Move has a " + (chance) + "% to hit!");
		System.out.println("Result number: " + result);
		*/
		
		// If move missed, return to second half of the turn
		if (result > chance) 
		{
			// Print move outcome
			System.out.println("The move missed!");
			return;
		}
		
		// Determine weakness/resistance value WIP
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
				/* Check if there is a burn status of user pokem
				 * 	- burn 		1/16 of maximun hp and halves physical attack damage
				 */
				if (user.getBattle_status() == "BRN")
				{
					damage /= 2;
				}
				
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
			return;
		}
		
		// If move is a StatModifierMove and targets the enemy pokemon
		if (move instanceof StatModifierMove && ((StatModifierMove) move).getTarget_self() == false)
		{
			StatModifierMove temp = (StatModifierMove) move;
			String effect = "";
			System.out.println("Level Effect" + temp.getLevel_effect());
			
			// Attack
			if (move instanceof AttackModifier)
			{
				effect += (target.getName() + "'s attack was ");
				if (temp.getLevel_effect() > 0) effect += "raised!";
				else
					effect += "lowered!";
				System.out.println(effect);	
				target.setBattle_attack(((target.getBase_attack() + target.getIv_values().get(1)) * 2 * (target.getLevel() + temp.getLevel_effect()) / 100 ) + 5);
			}
			// Defense
			if (move instanceof DefenseModifier)
			{
				effect += (target.getName() + "'s defense was ");
				if (temp.getLevel_effect() > 0) effect += "raised!";
				else
					effect += "lowered!";
				System.out.println(effect);	
				target.setBattle_defense(((target.getBase_defense() + target.getIv_values().get(2)) * 2 * (target.getLevel() + temp.getLevel_effect()) / 100 ) + 5);
			}
			// Special Attack
			if (move instanceof SpecialAttackModifier)
			{
				effect += (target.getName() + "'s special attack was ");
				if (temp.getLevel_effect() > 0) effect += "raised!";
				else
					effect += "lowered!";
				System.out.println(effect);		
				target.setBattle_special_attack(((target.getBase_special_attack() + target.getIv_values().get(3)) * 2 * (target.getLevel() + temp.getLevel_effect()) / 100 ) + 5);
			}
			// Special Defense
			if (move instanceof SpecialDefenseModifier)
			{
				effect += (target.getName() + "'s special defense was ");
				if (temp.getLevel_effect() > 0) effect += "raised!";
				else
					effect += "lowered!";
				System.out.println(effect);		
				target.setBattle_special_defense(((target.getBase_special_defense() + target.getIv_values().get(4)) * 2 * (target.getLevel() + temp.getLevel_effect()) / 100 ) + 5);
			}
			// Speed
			if (move instanceof SpeedModifier)
			{
				effect += (target.getName() + "'s speed was ");
				if (temp.getLevel_effect() > 0) effect += "raised!";
				else
					effect += "lowered!";
				System.out.println(effect);	
				target.setBattle_speed(((target.getBase_speed() + target.getIv_values().get(5)) * 2 * (target.getLevel() + temp.getLevel_effect()) / 100 ) + 5);
			}
			return;
		}
		
		// Check if target pokemon is already under a status condition
		if (!(target.getBattle_status() == null))
		{
			String status = "The target is already ";
			if (target.getBattle_status().equals("PAR")) status += "paralyzed!";
			if (target.getBattle_status().equals("PSN")) status += "poisoned!";
			if (target.getBattle_status().equals("BRN")) status += "burned!";
			if (target.getBattle_status().equals("SLP")) status += "asleep!";
			if (target.getBattle_status().equals("FRZ")) status += "frozen!";
			System.out.println(status);
		}
		else
		{
			// Move puts a status condition on the target pokemon
			if (move instanceof StatusMove)
			{
				StatusMove temp_move = (StatusMove) move;
				
				// Paralyze
				if (temp_move.getStatus_type().equals("PAR"))
				{
					target.setBattle_status("PAR");
					System.out.println("Enemy " + target.getName() + " was paralyzed! ");
				}
				
				// Poison
				if (temp_move.getStatus_type().equals("PSN"))
				{
					target.setBattle_status("PSN");
					System.out.println("Enemy " + target.getName() + " was poisoned! ");
				}
				
				// Burn
				if (temp_move.getStatus_type().equals("BRN"))
				{
					target.setBattle_status("BRN");
					System.out.println("Enemy " + target.getName() + " was burned! ");
				}
				
				// Sleep
				if (temp_move.getStatus_type().equals("SLP"))
				{
					target.setBattle_status("SLP");
					System.out.println("Enemy " + target.getName() + " was put to sleep! ");
				}
				
				// Frozen
				if (temp_move.getStatus_type().equals("FRZ"))
				{
					target.setBattle_status("FRZ");
					System.out.println("Enemy " + target.getName() + " was frozen solid! ");
				}
			}
		}
	}
	
	// Checks for a fainted pokemon (hp < 0)
	private boolean isFainted(Pokemon target)
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
		
		
		// First half of turn
		// Display hp
		System.out.println();
		System.out.println(left_player.getName() + "\tHP: " + left_player.getCurrent_hp() + " / " + left_player.getCurrent_max_hp());
		System.out.println(right_player.getName() + "\tHP: " + right_player.getCurrent_hp() + " / " + right_player.getCurrent_max_hp());
		System.out.println();
		
		// Display move options for first pokemon
		first.displayCurrentMoves();
		System.out.println("What will " + first.getName() + " do? ");
		
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
		
		// Second half of turn
		// Display hp
		System.out.println();
		System.out.println(left_player.getBattle_status() + "\t" + left_player.getName() + "\tHP: " + left_player.getCurrent_hp() + " / " + left_player.getCurrent_max_hp());
		System.out.println(right_player.getBattle_status() + "\t" + right_player.getName() + "\tHP: " + right_player.getCurrent_hp() + " / " + right_player.getCurrent_max_hp());
		System.out.println();
		
		// Display moves
		second.displayCurrentMoves();
		System.out.println("What will " + second.getName() + " do? ");
		
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
	
	
	private void Battle()
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
