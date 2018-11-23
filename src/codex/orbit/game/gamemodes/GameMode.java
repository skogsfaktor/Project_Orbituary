package codex.orbit.game.gamemodes;

import java.util.LinkedList;

import codex.orbit.level.Level;
import codex.orbit.player.Player;

public abstract class GameMode
{
	protected final Level level;
	
	protected final LinkedList<Player> players = new LinkedList<Player>();
	
	protected GameMode(Level level, Player[] players) {
		this.level = level;
		
		for (int i = 0; i < players.length; i++)
			this.players.add(players[i]);
	}
	
	public abstract void update();
	
	public abstract boolean gameOver();
	
	public abstract String getWinner();
}
