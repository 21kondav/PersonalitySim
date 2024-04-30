package player;

import java.util.Random;

public enum Trait {
	strength(3), intelligence(2);
	float inheritability;
	float spontaniousChance;
	//specifies if a trait is 'good'
	int fitnessScore;
	Trait(int fitness) {
		Random rand = new Random();
		inheritability = rand.nextFloat(0, 1);
		spontaniousChance = rand.nextFloat(0, 1);
		fitnessScore = fitness;
	}
	public int getFitness() {
		return fitnessScore;
	}
	
}
