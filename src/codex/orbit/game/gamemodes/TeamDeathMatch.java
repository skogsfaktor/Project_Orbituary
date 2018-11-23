package codex.orbit.game.gamemodes;

import codex.orbit.level.Level;
import codex.orbit.player.Player;

public class TeamDeathMatch extends GameMode
{
	private static class Team {
//		Player[] members;
		
//		int remainingLives;
		
		Team(Player[] members, int remainingLives) {
//			this.members = members;
//			this.remainingLives = remainingLives;
		}
		
//		boolean canSpawn() {
//			return remainingLives > 0;
//		}
	}
	
	protected Player[] players;
	
	protected Team team1, team2;
	
	protected TeamDeathMatch(Level level, Player[] team1, Player[] team2, int nrOfLives) {
		super(level, asOneArray(team1, team2));
		
		players = asOneArray(team1, team2);
		this.team1 = new Team(team1, nrOfLives);
		this.team2 = new Team(team2, nrOfLives);
	}

	@Override
	public void update() {
	}

	@Override
	public boolean gameOver() {
		return false;
	}
	
	@Override
	public String getWinner() {
		return null;
	}
	
	protected static Player[] asOneArray(Player[] pa1, Player[] pa2) {
		Player[] pa = new Player[pa1.length + pa2.length];
		int i = 0;
		for (Player p : pa1)
			pa[i++] = p;
		for (Player p : pa2)
			pa[i++] = p;
		return pa;
	}
}
