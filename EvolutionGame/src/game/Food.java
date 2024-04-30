package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import player.Player;
import player.Trait;

public class Food extends Resource {
	int quantity;
	Food(int quantity, float value){
		this.quantity = quantity;
		this.value = value;
		isDivisible = true;
	}
	public static Collection<Food> generateRandomFood(int n){
		Collection<Food> food = new ArrayList<Food>();
		Random rand = new Random();
		for(int i = 0; i < n; i++) {
			food.add(new Food(rand.nextInt(0, 20), rand.nextFloat(1, 5)));
		}
		return food;
	}
	@Override
	public Player compete(Player agent, Collection<Player> competition) {
		if(willDivide(competition)) {
			agent.divideResource(this, competition);
			return null;
		}
		int agentFitness = 0;
		for (Trait trait : agent.getTraits()) {
			agentFitness += trait.getFitness();
		}
		for(int i = 0; i < competition.size();i++) {
			ArrayList<Player> newCompet = new ArrayList<>(competition);
			if(newCompet.get(i) == null) continue;
			if(newCompet.get(i).equals(agent)) continue;
			int othersFitness = 0;
			for (Trait trait : newCompet.get(i).getTraits()) {
				othersFitness += trait.getFitness();
			}
			
			Player curr = newCompet.remove(i);
			if(agentFitness >= othersFitness) {
				return agent;
			}
			else compete(curr, newCompet);
		}
		
		return agent;
	}

	@Override
	public Collection<Food> divide(Collection<Player> players) {
		// NOTE Auto-generated method stub
		int ration = quantity%players.size();
		if(ration <= 1)
			return null;
		Collection<Food> divison = new ArrayList<Food>();
		for(int i = 0; i < ration; i++) {
			divison.add(new Food(quantity/ration, value/ration));
		}
		return divison;
	}

}
