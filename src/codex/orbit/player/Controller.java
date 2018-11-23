package codex.orbit.player;

import java.util.LinkedList;

public abstract class Controller
{
	protected static final LinkedList<Player> players = new LinkedList<Player>();
	
	protected Player player;
	
	protected Controller(Player p) {
		players.add(p);
		player = p;
	}
			
	public abstract void control();
}
