package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import player.Player;

public class Game{
	ArrayList<Player> agents;
	ArrayList<Resource> resources;
	public Game() {
		agents = new ArrayList<Player>();
		resources = new ArrayList<Resource>();
	}
	public void evolve(int nsteps) {
		Random rand = new Random();
		
		while(nsteps > 0) {
			resources.removeIf(agent -> agent == null);
			agents.removeIf(agent -> agent == null);
			randomDiscovery();
			addResources(Food.generateRandomFood(rand.nextInt(0, 5)));
			nsteps--;
			Collection<Player> deathList = new ArrayList<>();
			for (Player player : agents) {
				if(player == null) continue;
				if(player.randomDeath()) {
					deathList.add(player);
				}
			}
			removeAgents(deathList);
			
		}
		
	}
	public void randomDiscovery() {
		Random rand = new Random();
		int randnum = rand.nextInt(-1, resources.size());
		int randPlayer = rand.nextInt(-1, agents.size());
		if(randnum != -1 && randPlayer != -1) {
			Resource discovered = resources.get(randnum);
			Player curr = agents.get(randPlayer);
			if(discovered == null) return;
			Player winner = discovered.compete(curr, agents);
			if(winner == null) return ;
			if(discovered instanceof Player) {
				addAgent(winner.reproduce((Player)discovered));
			}else if(discovered.willDivide(agents)) {
				winner.divideResource(discovered, agents);
			}
			else {
				winner.collectResource(discovered);
				resources.remove(discovered);
			}
		}
	}
	public void addAgents(Collection<Player> agents) {
		this.agents.addAll(agents);
		resources.addAll(agents);
	}
	public void addAgent(Player agent) {
		agents.add(agent);
		resources.add(agent);
	}
	public void addResource(Resource resource) {
		resources.add(resource);
		if(resource instanceof Player){
			agents.add((Player) resource);
		}
	}
	public void addResources(Collection<? extends Resource> resources) {
		this.resources.addAll(resources);
	}
	public void removeAgents(Collection<Player> agents) {
		this.agents.removeAll(agents);
		resources.removeAll(agents);
	}
	public void removeResources(Collection<Resource> resources) {
		this.resources.removeAll(resources);
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Number of Agents " + agents.size()+"\n");
		//average moral values (altruism, selfishness, collectivism, and individualism)
		float[] averages = {0, 0, 0, 0};
		agents.removeIf(agent -> agent == null);
		agents.forEach(agent ->{
			float[] moral = agent.getMorals();
			averages[0] += moral[0];
			averages[1] += moral[1];
			averages[2] += moral[2];
			averages[3] += moral[3];
		});
		averages[0] /=agents.size();
		averages[1] /=agents.size();
		averages[2] /=agents.size();
		averages[3] /=agents.size();
		sb.append("Avg altruism:"+ averages[0]+ "\nAvg selfishness:" + averages[1]+ "\nAvg collectivism:"+averages[2] + "\nAvg individualism: "+averages[3]+"\n");
		sb.append("\nWealth Rankings\n");
		int i = 1;
		for(Player agent: agents) {
			sb.append(i+". "+agent.toString()+"\n");
			i++;
		}
		return sb.toString();
	}
}
