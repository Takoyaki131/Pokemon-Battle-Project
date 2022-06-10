import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import pokemon.*;


public class FileLoadingMethods {
	
	// Functions related to creating the moveList array
	public static ArrayList<Move> createMoveList()
	{
		ArrayList<Move> moveList = new ArrayList<>();
		// Load in file for movelist
		Scanner sc = null;
		try {
			sc = new Scanner(new File("sampledata/movelist.txt"));
			
			while (sc.hasNextLine())
			{
				String str = sc.nextLine();
				FileLoadingMethods.addMove(str, moveList);
			}
		}
		catch (IOException exp)
		{
			exp.printStackTrace();
		}
		// close the file
		finally
		{
			if (sc != null)
				sc.close();
		}
		return moveList;
	};
	
	public static void addMove(String str, ArrayList<Move> list) {
		String move_category;	// Defines what type of move to create
		/* movecodes found in the string
		 * PhysAtt = PhysicalAttack
		 * SpAtt = SpecialAttack
		 * AttMod = AttackModifier
		 * SpAtt = SpecialAttackModifier
		 * DefMod = DefenseModifier
		 * SpDefMod = SpecialDefenseModifier
		 * SpdMod = SpeedModifier
		 * AccMod = AccuracyModifier
		 * EvMod = EvasionModifier
		 * Status = StatusMove
		 */
		// Attributes for general moves
		int tm_no;
		String name;
		String type;
		int max_pp;
		double accuracy;
		
		// attack move attributes
		int power;
		
		// Stat modifier attributes
		int level_effect;
		boolean target_self;
		
		// Status move attributes
		String status_type;
		
		// Weather move attributes
		String target_weather;
		
		Scanner lineScanner = new Scanner(str);
		lineScanner.useDelimiter("/");
		while(lineScanner.hasNext())
		{
			/* Read order
			 * 
			 */
			// determine the type of move to be created
			move_category = lineScanner.next();
			
			// Set the variables from the abstract move class attributes
			tm_no = Integer.parseInt(lineScanner.next());
			name = lineScanner.next();
			type = lineScanner.next();
			max_pp = Integer.parseInt(lineScanner.next());
			accuracy = Double.parseDouble(lineScanner.next());
			
			// Create the specific move object
			// ATTACKMOVES
			if (move_category.equals("PhysAtt"))
			{
				power = Integer.parseInt(lineScanner.next());
				list.add(new PhysicalAttack(tm_no, name, type, max_pp, accuracy, power));
			}
			
			if (move_category.equals("SpAtt"))
			{
				power = Integer.parseInt(lineScanner.next());
				list.add(new SpecialAttack(tm_no, name, type, max_pp, accuracy, power));
			}
			
			// STATMODIIFERS
			if (move_category.equals("AttMod"))
			{
				level_effect = Integer.parseInt(lineScanner.next());
				target_self = Boolean.parseBoolean(lineScanner.next());
				list.add(new AttackModifier(tm_no, name, type, max_pp, accuracy, level_effect, target_self));
			}
			if (move_category.equals("SpAttMod"))
			{
				level_effect = Integer.parseInt(lineScanner.next());
				target_self = Boolean.parseBoolean(lineScanner.next());
				list.add(new SpecialAttackModifier(tm_no, name, type, max_pp, accuracy, level_effect, target_self));
			}
			if (move_category.equals("DefMod"))
			{
				level_effect = Integer.parseInt(lineScanner.next());
				target_self = Boolean.parseBoolean(lineScanner.next());
				list.add(new DefenseModifier(tm_no, name, type, max_pp, accuracy, level_effect, target_self));
			}
			if (move_category.equals("SpDefMod"))
			{
				level_effect = Integer.parseInt(lineScanner.next());
				target_self = Boolean.parseBoolean(lineScanner.next());
				list.add(new SpecialDefenseModifier(tm_no, name, type,max_pp, accuracy, level_effect, target_self));
			}
			if (move_category.equals("SpdMod"))
			{
				level_effect = Integer.parseInt(lineScanner.next());
				target_self = Boolean.parseBoolean(lineScanner.next());
				list.add(new SpeedModifier(tm_no, name, type, max_pp, accuracy, level_effect, target_self));
			}
			if (move_category.equals("AccMod"))
			{
				level_effect = Integer.parseInt(lineScanner.next());
				target_self = Boolean.parseBoolean(lineScanner.next());
				list.add(new AccuracyModifier(tm_no, name, type, max_pp, accuracy, level_effect, target_self));
			}
			if (move_category.equals("EvMod"))
			{
				level_effect = Integer.parseInt(lineScanner.next());
				target_self = Boolean.parseBoolean(lineScanner.next());
				list.add(new EvasionModifier(tm_no, name, type, max_pp, accuracy, level_effect, target_self));
			}
			
			// STATUSMOVES
			if (move_category.equals("Status"))
			{
				status_type = lineScanner.next();
				list.add(new StatusMove(tm_no, name, type, max_pp, accuracy, status_type));
			}
			
			// WEATHERMOVES
			if (move_category.equals("Weather"))
			{
				target_weather = lineScanner.next();
				list.add(new WeatherMove(tm_no, name, type, max_pp, accuracy, target_weather));
			}
			
			// Print out the move read
			System.out.println("Move found... TM_NO: " + tm_no + "\tName: " + name );
		}
		lineScanner.close();
	}
	
	/* Create the list of pokemon available based on input file
	 * 
	 */
	public static ArrayList<Pokemon> createPokemonList()
	{
		ArrayList<Pokemon> pokeList = new ArrayList<>();
		// Load in file for movelist
		Scanner sc = null;
		try {
			sc = new Scanner(new File("sampledata/pokemon.txt"));
			
			while (sc.hasNextLine())
			{
				// reading in data for the first part of pokemon data
				String poke_stats = sc.nextLine(); 
				addPokemonPartOne(poke_stats, pokeList);
				// reading in data for the second part of pokemon data
				String learnable_moves = sc.nextLine();
				addPokemonPartTwo(learnable_moves, pokeList.get(pokeList.size() - 1));
				
			}
		}
		
		catch (IOException exp)
		{
			exp.printStackTrace();
		}
		// close the file
		finally
		{
			if (sc != null)
				sc.close();
		}
		return pokeList;
	}
	
	/* Set up the pokmeon with basic stats
	 * 
	 */
	public static void addPokemonPartOne(String str, ArrayList<Pokemon> list) {
		/* NOTE: For each pokemon, there are two lines of data to read in
		 * the first line contains
		 * 
		 */
		// Attributes for pokemon class 
		String name, type_one, type_two;
		int max_health_points, attack, defense, special_attack, special_defense, speed;
		
		Scanner lineScanner = new Scanner(str);
		lineScanner.useDelimiter("/");
		while(lineScanner.hasNext())
		{
			name = lineScanner.next();
			type_one = lineScanner.next();
			type_two = lineScanner.next();
			max_health_points = Integer.parseInt(lineScanner.next());
			attack = Integer.parseInt(lineScanner.next());
			defense = Integer.parseInt(lineScanner.next());
			special_attack = Integer.parseInt(lineScanner.next());
			special_defense = Integer.parseInt(lineScanner.next());
			speed = Integer.parseInt(lineScanner.next());
			
			// Add pokemon to the list
			list.add(new Pokemon(name, type_one, type_two, max_health_points, 
					attack, defense, special_attack, special_defense, speed));
			// Print out the pokemon entered
			System.out.println("Pokemon found... Name: " + name);
		}
		lineScanner.close();
	}
	
	/* Set up the pokmeon's learnable moves
	 * 
	 */
	public static void addPokemonPartTwo(String str, Pokemon pokemon)
	{
		ArrayList<Integer> learnable_moves = new ArrayList<>(); // Temporary list to hold file stream of integers
		Scanner lineScanner = new Scanner(str);
		lineScanner.useDelimiter("/");
		
		while(lineScanner.hasNext())
		{
			learnable_moves.add(lineScanner.nextInt());
		}
		lineScanner.close();
		
		// Set the pokemons learnable moves
		pokemon.setLearnable_moves(learnable_moves);
	}
}
