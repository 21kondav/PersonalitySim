package player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import game.Resource;
import player.PredictBehavior.behaviorType;

public class Player extends Resource implements Behavior, Comparable<Player>{
	PredictBehavior prediction;
	boolean isMale;
	Collection<Trait> traits;
	Player mom;
	Player dad;
	Collection<Resource> inventory;
	String name;
	int age;
	Player(String name, Player mom, Player dad){
		age = 0;
		this.name = name;
		this.mom = mom;
		this.dad = dad;
		prediction = new PredictBehavior(mom.prediction, dad.prediction);
		Random rand = new Random();
		Collection<Trait> selection = new ArrayList<Trait>(mom.traits);
		traits = new ArrayList<Trait>();
		selection.addAll(dad.traits);
		//include only traits that are inheritable
		selection.removeIf(trait -> trait.inheritability > .01);
		//perform mendelian like computations, deeply flawed
		selection.forEach(item ->{
			boolean momHas = mom.traits.contains(item);
			boolean dadHas = dad.traits.contains(item);
			//both have it
			if(momHas && dadHas) 
				if(item.inheritability <= rand.nextFloat(0, 1)) traits.add(item);
			else if(!(momHas || dadHas)) //neither has it
				if(item.spontaniousChance<=rand.nextFloat(0, 1)) traits.add(item);
			else 
				if( item.inheritability/2 <=rand.nextFloat(0,1)) traits.add(item);
		});
		isMale = rand.nextFloat(0, 1) >= .5;
		inventory = new ArrayList<Resource>();
		
	}
	public boolean randomDeath() {
		Random rand = new Random();
		return age == rand.nextInt(age, 130);
	}
	public void increaseAge() {
		age++;
	}
	public Player(String name, behaviorType behave){
		//no parents to start
		age = 0;
		mom= null;
		this.name = name;
		dad = null;
		prediction = PredictBehavior.predictorFactory(behave);
		Random rand = new Random();
		traits = new ArrayList<Trait>();
		isMale = rand.nextFloat(0, 1) >= .5;
		inventory = new ArrayList<Resource>();
	}
	Player(){
		
	}
	public void addTrait(Trait trait) {
		traits.add(trait);
	}
	//(altruism, selfishness, collectivism, individualism)
	public float[] getMorals() {
		return prediction.getValues();
	}
	public float getValue() {
		float value = 0;
		for(Resource item: inventory) {
			value+=item.getValue();
		}
		return value;
	}
	@Override
	public void collectResource(Resource item) {

		inventory.add(item);
		
	}
	@Override
	public void giveResource(Resource item, Player giveTo) {
		// NOTE Auto-generated method stub
		if(inventory.remove(item)) {
			giveTo.inventory.add(giveTo);
		}
	}
	@Override
	public boolean divideResource(Resource item, Collection<Player> others) {
		// NOTE Auto-generated method stub
		Random rand = new Random();
		//determines if we should divide
		boolean collectiveDivision = prediction.act(behaviorType.collective);
		if(collectiveDivision) {
			others.add(this);
			ArrayList<Resource> division = new ArrayList<Resource> (item.divide(others));
			
			Iterator<Player> players = new Iterator<Player>() {
				ArrayList<Player> players = new ArrayList<Player>(others);
				int location = 0;
				@Override 
				public Player next() {
					// NOTE Auto-generated method stub
					if(location == players.size()) location = 0;
					return players.get(location++);
				}
				
				@Override
				public boolean hasNext() {
					// NOTE Auto-generated method stub
					return true;
				}
			};
			boolean altruistic = prediction.act(behaviorType.altruism);
			boolean selfish = prediction.act(behaviorType.altruism);
			int counter = 0;
			if(selfish && !altruistic) {
				inventory.addAll(division.subList(0, division.size()/5));
			}
			for (Resource resource : division) {
				Player curr = players.next();
				if(altruistic && !selfish && resource.isDivisible()) {
					divideResource(resource, others);
				}
				curr.inventory.add(resource);
			}
		}
		return collectiveDivision;
	}
	@Override
	public Player reproduce(Player mate) {
		// NOTE Auto-generated method stub
		//reduce inbreeding
		if(mate.equals(mom) || mate.equals(dad)) return null;
		else if(equals(mate.mom) || equals(mate.dad)) return null;
		if(mate.isMale != isMale) {
			return new Player("("+name+"+"+mate.name+")", this, mate);
		}
		return null;
	}
	public Collection<Trait> getTraits(){
		return traits;
	}
	//treats current instance as a resources

	//assume heterosexual competition
	@Override
	public Player compete(Player agent, Collection<Player> competition) {
		Random rand = new Random();
		ArrayList<Player> newCompet = new ArrayList<>(competition);
		//remove same sex competitors
		if(newCompet.size() == 0) return agent;
		if(agent == null) return null;
		newCompet.removeIf(player -> player == null || isMale == player.isMale);
		int agentFitness = 0;
		for (Trait trait : traits) {
			agentFitness += trait.fitnessScore;
		}
		
		for(int i = 0; i < newCompet.size();i++) {
			//if(newCompet.size() == 0) continue;
			//if(newCompet.get(i) == null) continue;
			//if(newCompet.get(i).equals(agent)) continue;
			int othersFitness = 0;
			for (Trait trait : newCompet.get(i).traits) {
				othersFitness += trait.fitnessScore;
			}
			if(agentFitness > 0)
				agentFitness = rand.nextInt(0, agentFitness);
			if(othersFitness > 0)
			othersFitness = rand.nextInt(0, othersFitness);
			if(agentFitness >= othersFitness) {
				return agent;
			}
			else compete(newCompet.remove(i), newCompet);
		}
		
		return agent;
	}
	//treats current instance as a resources
	@Override
	public Collection<Resource> divide(Collection<Player> players) {
		// NOTE Auto-generated method stub
		return null;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name:"+name+",");
		sb.append("Sex:");
		if(isMale) sb.append("Male,");
		else sb.append("female,");
		sb.append("Value:"+getValue()+",");
		sb.append("Traits:");
		for (Trait trait : traits) {
			sb.append(trait+",");
		}
		return sb.toString(); 
	}
	@Override
	public int compareTo(Player o) {
		// NOTE Auto-generated method stub
		return (int)(this.getValue() - o.getValue());
	}

}
