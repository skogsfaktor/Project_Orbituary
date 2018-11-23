package codex.orbit.game.gamemodes;

import codex.orbit.level.Level;
import codex.orbit.player.Player;

public class DeathMatch extends GameMode
{
	private static class Contestant {
//		Player player;
		
//		int remainingLives;
		
		Contestant(Player player, int remainingLives) {
//			this.player = player;
//			this.remainingLives = remainingLives;
		}
		
//		boolean canSpawn() {
//			return remainingLives > 0;
//		}
	}
	
	protected Contestant[] contestants;
	
	private Player winner;
	
	protected DeathMatch(Level level, Player[] players, int nrOfLives) {
		super(level, players);
		contestants = new Contestant[players.length];
		for (int i = 0; i < players.length; i++)
			contestants[i] = new Contestant(players[i], nrOfLives);
	}

	@Override
	public void update() {
		if (gameOver())
			return;
//		for (Contestant con : contestants) {
//			if (con.canSpawn() && !con.player.isAlive())
//				contestants.remove(con);
//		}
//		if (contestants.size() == 1)
//			winner = contestants.getFirst().player;
	}

	@Override
	public boolean gameOver() {
		return winner != null;
	}

	@Override
	public String getWinner() {
		return winner.toString();
	}
}
