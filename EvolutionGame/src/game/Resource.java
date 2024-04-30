package game;

import java.util.Collection;
import java.util.Random;

import player.Player;

public abstract class Resource {
	boolean isDivisible;
	float value;
	public Player compete(Collection<Player> players) {
		
		//if resources are divisible among them
	
		for (Player player : players) {
			if(player == null) continue;
			Player winner = compete(player, players);
			if(winner == null) continue;

			if(winner.equals( player)) {
				if(willDivide(players)) {
					winner.divideResource(winner, players);
				}
				return player;
			}
			players.remove(player);
		}
		return null;
	}
	public float getValue() {
		return value;
	}
	public boolean isDivisible(){
		return isDivisible;
	}
	//check if the group will divide the rations
	public boolean willDivide(Collection<Player> players) {
		Random rand = new Random();
		boolean willDivide = true;
		if(isDivisible) {
			
			for(Player agent: players) {
				float[] moral = agent.getMorals();
				willDivide &= (moral[0]+moral[2] - (moral[1]+moral[3])) > 0;
			}
			return willDivide;
		}
		return !willDivide;
	}
	public abstract Player compete(Player one, Collection<Player> list);//returns winner of competition
	//returns if the division occured or not
	public abstract Collection<? extends Resource> divide(Collection<Player> players);
}
