package game;

import player.Player;
import player.Trait;
import player.PredictBehavior.behaviorType;

public class Test {
	public static void main(String[] args) {
		
		Game game = new Game();
		Player one = new Player( "a", behaviorType.collective);
		Player two = new Player("b", behaviorType.selfish);
		Player three = new Player("c", behaviorType.collective);
		Player four = new Player("d", behaviorType.individualistic);
		Player five = new Player("e", behaviorType.individualistic);
		Player six = new Player( "f", behaviorType.altruism);
		one.addTrait(Trait.strength);
		three.addTrait(Trait.strength);
		four.addTrait(Trait.intelligence);
		five.addTrait(Trait.intelligence);
		two.addTrait(Trait.intelligence);
		two.addTrait(Trait.strength);
		game.addAgent(one);
		game.addAgent(two);
		game.addAgent(three);
		game.addAgent(four);
		game.addAgent(five);
		game.addAgent(six);
		game.addResources(Food.generateRandomFood(10));
		System.out.println(game);
		game.evolve(100);
		System.out.println(game);
	}

	
}
