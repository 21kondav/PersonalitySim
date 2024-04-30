package player;

import java.util.Collection;

import game.Resource;

public interface Behavior {

	void collectResource(Resource item);
	void giveResource(Resource item, Player giveTo);
	boolean divideResource(Resource item, Collection<Player> others);
	Player reproduce(Player mate);
	
	
}
