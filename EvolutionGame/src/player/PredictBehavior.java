package player;

import java.util.Random;

public class PredictBehavior {
	private float altruismChance;
	private float selfishChance;
	private float collectivismChance;
	private float individualChance;
	public PredictBehavior(float altruismChance, float selfishChance, float collectivismChance, float indivualismChance) {
		// NOTE Auto-generated constructor stub
		this.altruismChance =altruismChance;
		this.collectivismChance = collectivismChance;
		this.selfishChance = selfishChance;
		this.individualChance = indivualismChance;
	}
	//reproducing
	public PredictBehavior(PredictBehavior mom, PredictBehavior dad) {
		float altruism = (mom.altruismChance+ dad.altruismChance)/2;
		float selfish = (mom.selfishChance+ dad.selfishChance)/2;
		float collective = (mom.collectivismChance+ dad.collectivismChance)/2;
		float individual = (mom.collectivismChance+ dad.collectivismChance)/2;
		
		altruismChance = altruism - selfish;
		selfishChance = selfish - altruism;
		collectivismChance = collective - individual;
		individualChance = individual - collective;
	}
	public float[] getValues() {
		float[] vals = {altruismChance, selfishChance, collectivismChance, individualChance};
		return vals;
	}
	 public static PredictBehavior predictorFactory(behaviorType type){
		Random rand = new Random();
		PredictBehavior start = new PredictBehavior(rand.nextFloat(0, 1), rand.nextFloat(0, 1), rand.nextFloat(0, 1), rand.nextFloat(0, 1));
		switch (type) {
		case altruism: {
			start.altruismChance += .3;
			start.selfishChance -= .3;
			start.collectivismChance += .1;
			start.individualChance -= .1;
			return start;
		}
		case selfish: {
			start.altruismChance -= .3;
			start.selfishChance += .3;
			start.collectivismChance -= .1;
			start.individualChance += .1;
			return start;
		}
		case collective: {
			start.altruismChance += .1;
			start.selfishChance -= .1;
			start.collectivismChance += .3;
			start.individualChance -= .3;
			return start;
		}
		case individualistic: {
			start.altruismChance -= .1;
			start.selfishChance += .1;
			start.collectivismChance -= .3;
			start.individualChance += .3;
			return start;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
		// NOTE Auto-generated constructor stub
	}
	public boolean act(behaviorType behave) {
		Random rand = new Random();
		float roll = rand.nextFloat(0, 1);
		switch (behave) {
		case altruism: {
			
			return roll <= altruismChance;
		}
		case selfish: {
			return roll <= selfishChance;
		}
		case collective: {
			return roll <= collectivismChance;
		
		}
		case individualistic: {
			return roll <= individualChance;
		
		}
		default:
			throw new IllegalArgumentException("Unexpected behavior: " + behave);
		}
	}

	public enum behaviorType{
		altruism,
		selfish,
		collective,
		individualistic
	}
}
