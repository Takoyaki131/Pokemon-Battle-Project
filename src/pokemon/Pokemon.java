package pokemon;
import java.util.ArrayList;

/* Class representation of a pokemon
 * 
 */
public class Pokemon {
	
	// Generic & non battle related attributes
	private String name;
	private String nickname;
	private String type_one;
	private String type_two;
	
	// Current moves and learnable move arrays
	private Move [] current_moves = new Move[4];
	private ArrayList<Integer> learnable_moves = new ArrayList<>();
	
	// Base statistics
	private int level;
	private int base_max_hp;
	private int base_attack;
	private int base_defense;
	private int base_special_attack;
	private int base_special_defense;
	private int base_speed;
	
	// IV Values -- randomized at pokemon creation
	private ArrayList<Integer> iv_values = new ArrayList<>();
	
	// Stats adjusted to the current level of the pokemon
	private int current_hp;
	private int current_max_hp;
	private int current_attack;
	private int current_defense;
	private int current_special_attack;
	private int current_special_defense;
	private int current_speed;
	
	/* Battle based statistics
	 * Since stats are changed during battle these stats will be copied from the current values before a battle
	 */
	private int battle_attack;
	private int battle_defense;
	private int battle_special_attack;
	private int battle_special_defense;
	private int battle_speed;
	
	// Attributes to determine move success
	private double battle_accuracy;
	private double battle_evasion;
	
	// Constructor
	public Pokemon(String name, String type_one, String type_two, int base_max_hp, int base_attack, int base_defense,
			int base_special_attack, int base_special_defense, int base_speed) 
	{
		super();
		this.name = name;
		this.type_one = type_one;
		this.type_two = type_two;
		this.base_max_hp = base_max_hp;
		this.base_attack = base_attack;
		this.base_defense = base_defense;
		this.base_special_attack = base_special_attack;
		this.base_special_defense = base_special_defense;
		this.base_speed = base_speed;
		
		// Randomize the IV values (Between 0 and 31)
		for (int i = 0; i < 6; i++)
		{
			this.iv_values.add((int) (Math.random() * 31));
		}
	}
	
	/* Displays all information about the specific pokemon
	 * Inputs:
	 * none
	 * Returns:
	 * none
	 */
	public void displayPokemonInfo()
	{
		System.out.println("Name: " + name);
		System.out.print(type_one);
		if (!type_two.equals(""))
		{
			System.out.print(" / " + type_two + "\n");
		}
		else
		{
			System.out.print("\n");
		}
		System.out.println("Level: " + level);
		System.out.println("HP: " + current_hp + " / " + current_max_hp);
		System.out.println("Attack:\t\t" +  current_attack);
		System.out.println("Defense:\t" + current_defense);
		System.out.println("Sp. Atk:\t" + current_special_attack);
		System.out.println("Sp. Def:\t" + current_special_defense);
		System.out.println("Speed:\t\t" + current_speed);
	}
	

	/* Update the pokemons stats acording to its current level
	 * Inputs: 	int level - level to set the pokemon at (EX: set to level 50 or 100 for rental battles)
	 * Returns: none
	 */
	public void setLevel(int level)
	{
		this.level = level;
		
		// update the current stats of the pokemon
		/* Note: using GEN 1 and GEN 2 formula
		 * Reference: https://bulbapedia.bulbagarden.net/wiki/Stat#Generations_I_and_II 
		 * Parts missing from later games
		 * 				- nature
		 * 				- ev values (bonus from raising a pokmeon through battle)
		 */
		
		current_hp = current_max_hp =  ( ( (base_max_hp + this.iv_values.get(0)) * 2 * this.level) / 100 ) + this.level + 10;
		
		current_attack = ( ( (base_attack + this.iv_values.get(1)) * 2 * this.level) / 100 ) + 5;
		current_defense = ( ( (base_defense + this.iv_values.get(2)) * 2 * this.level) / 100 ) + 5;
		current_special_attack = ( ( (base_special_attack + this.iv_values.get(3)) * 2 * this.level) / 100 ) + 5;
		current_special_defense = ( ( (base_special_defense + this.iv_values.get(4)) * 2 * this.level) / 100 ) + 5;
		current_speed = ( ( (base_speed + this.iv_values.get(5)) * 2 * this.level) / 100 ) + 5;;
		
		// update the battle stats
		resetBattleStats();
	}
	
	/* Returns the battle stats to its current stats /  stats will be changed during course of a battle
	 * Inputs:
	 * none
	 * Returns:
	 * none
	 */
	public void resetBattleStats()
	{
		this.battle_attack = current_attack;
		this.battle_special_attack = current_special_attack;
		this.battle_defense = current_defense;
		this.battle_special_defense = current_special_defense;
		this.battle_speed = current_speed;
		this.battle_accuracy = 1.0;
		this.battle_evasion = 1.0;
	}
	
	// Displays the 4 moves a pokmeon currently knows
	public void displayCurrentMoves()
	{
		for (int i = 0; i < 4; i++)
		{
			if (this.current_moves[i] == null)
			{
				System.out.println((i+1) + ". -- ");
			}
			else
			{
				System.out.println((i + 1) + ". " + this.current_moves[i].getName());
			}
		}
	}
	
	/* Sets the array containing the tm numbers of moves a pokemon can learn
	 * Is called during the file loading of a pokemon entry
	 * Inputs:
	 * ArrayList<Integer> learnable_moves	- temporary created int array from a line in file
	 * Returns:
	 * none
	 */
	public void setLearnableMoves(ArrayList<Integer> learnable_moves)
	{
		this.learnable_moves = learnable_moves;
	}
	
	/* Sets up the pokemon with 4 random moves it can learn
	 * 
	 */
	public void setRandomMoves(ArrayList<Move> moveList)
	{
		int [] tm_set = new int[4]; // 4 element array to hold the the first 4 tm moves a pokmeon can learn
		for (int i = 0; i < 4; i++)
		{
			tm_set[i] = this.learnable_moves.get(i);
		}
		
		
		for (int i = 0; i < 4; i++)
		{
			// Find the move according to tm_set
			for(Move move : moveList)
			{
				// add move to current_moves when found
				if(move.getTmNo() == tm_set[i])
				{
					System.out.println(move.getName() + " added to " + this.getName() + "'s moveset");
					this.current_moves[i] = move;
				}	
			}
		}
	}
	
	// Applies new hp value based on input damage
	public void takeDamage(int damage)
	{
		this.current_hp = this.current_hp - damage;
	}
	
	public ArrayList<Integer> getIv_values() {
		return iv_values;
	}

	public void setIv_values(ArrayList<Integer> iv_values) {
		this.iv_values = iv_values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getType_one() {
		return type_one;
	}

	public void setType_one(String type_one) {
		this.type_one = type_one;
	}

	public String getType_two() {
		return type_two;
	}

	public void setType_two(String type_two) {
		this.type_two = type_two;
	}

	public Move[] getCurrent_moves() {
		return current_moves;
	}

	public void setCurrent_moves(Move[] current_moves) {
		this.current_moves = current_moves;
	}

	public ArrayList<Integer> getLearnable_moves() {
		return learnable_moves;
	}

	public void setLearnable_moves(ArrayList<Integer> learnable_moves) {
		this.learnable_moves = learnable_moves;
	}

	public int getBase_max_hp() {
		return base_max_hp;
	}

	public void setBase_max_hp(int base_max_hp) {
		this.base_max_hp = base_max_hp;
	}

	public int getBase_attack() {
		return base_attack;
	}

	public void setBase_attack(int base_attack) {
		this.base_attack = base_attack;
	}

	public int getBase_defense() {
		return base_defense;
	}

	public void setBase_defense(int base_defense) {
		this.base_defense = base_defense;
	}

	public int getBase_special_attack() {
		return base_special_attack;
	}

	public void setBase_special_attack(int base_special_attack) {
		this.base_special_attack = base_special_attack;
	}

	public int getBase_special_defense() {
		return base_special_defense;
	}

	public void setBase_special_defense(int base_special_defense) {
		this.base_special_defense = base_special_defense;
	}

	public int getBase_speed() {
		return base_speed;
	}

	public void setBase_speed(int base_speed) {
		this.base_speed = base_speed;
	}

	public int getCurrent_hp() {
		return current_hp;
	}

	public void setCurrent_hp(int current_hp) {
		this.current_hp = current_hp;
	}

	public int getCurrent_max_hp() {
		return current_max_hp;
	}

	public void setCurrent_max_hp(int current_max_hp) {
		this.current_max_hp = current_max_hp;
	}

	public int getCurrent_attack() {
		return current_attack;
	}

	public void setCurrent_attack(int current_attack) {
		this.current_attack = current_attack;
	}

	public int getCurrent_defense() {
		return current_defense;
	}

	public void setCurrent_defense(int current_defense) {
		this.current_defense = current_defense;
	}

	public int getCurrent_special_attack() {
		return current_special_attack;
	}

	public void setCurrent_special_attack(int current_special_attack) {
		this.current_special_attack = current_special_attack;
	}

	public int getCurrent_special_defense() {
		return current_special_defense;
	}

	public void setCurrent_special_defense(int current_special_defense) {
		this.current_special_defense = current_special_defense;
	}

	public int getCurrent_speed() {
		return current_speed;
	}

	public void setCurrent_speed(int current_speed) {
		this.current_speed = current_speed;
	}

	public int getBattle_attack() {
		return battle_attack;
	}

	public void setBattle_attack(int battle_attack) {
		this.battle_attack = battle_attack;
	}

	public int getBattle_defense() {
		return battle_defense;
	}

	public void setBattle_defense(int battle_defense) {
		this.battle_defense = battle_defense;
	}

	public int getBattle_special_attack() {
		return battle_special_attack;
	}

	public void setBattle_special_attack(int battle_special_attack) {
		this.battle_special_attack = battle_special_attack;
	}

	public int getBattle_special_defense() {
		return battle_special_defense;
	}

	public void setBattle_special_defense(int battle_special_defense) {
		this.battle_special_defense = battle_special_defense;
	}

	public int getBattle_speed() {
		return battle_speed;
	}

	public void setBattle_speed(int battle_speed) {
		this.battle_speed = battle_speed;
	}

	public double getBattle_accuracy() {
		return battle_accuracy;
	}

	public void setBattle_accuracy(double battle_accuracy) {
		this.battle_accuracy = battle_accuracy;
	}

	public double getBattle_evasion() {
		return battle_evasion;
	}

	public void setBattle_evasion(double battle_evasion) {
		this.battle_evasion = battle_evasion;
	}

	public int getLevel() {
		return level;
	}
	
}
