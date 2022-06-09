import pokemon.*;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		final int RENTAL_LEVEL = 50;
		final int TUTORIAL_LEVEL = 5;
		// Load file for move list
		System.out.println("LOADING MOVES...");
		ArrayList<Move> moveList = FileLoadingMethods.createMoveList();
		
		// Load file for pokemon
		System.out.println("LOADING POKEMON...");
		ArrayList<Pokemon> pokemonList = FileLoadingMethods.createPokemonList();
		
		// Set up two pokemon
		pokemonList.get(0).setLevel(TUTORIAL_LEVEL);
		pokemonList.get(0).setRandomMoves(moveList);
		
		pokemonList.get(1).setLevel(TUTORIAL_LEVEL);
		pokemonList.get(1).setRandomMoves(moveList);
		
		// display
		/*
		pokemonList.get(0).displayPokemonInfo();
		pokemonList.get(0).displayCurrentMoves();
		System.out.println();
		pokemonList.get(1).displayPokemonInfo();
		pokemonList.get(1).displayCurrentMoves();
		System.out.println();
		*/
		
		BattleHandler battle = new BattleHandler(pokemonList.get(0), pokemonList.get(1));
		battle.startBattle();
	}

}
