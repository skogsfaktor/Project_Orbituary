package codex.orbit.game.gamemodes;

import codex.orbit.level.Level;
import codex.orbit.player.Player;

public class TugOfWar extends GameMode
{
	private static class Team {
//		Player[] members;
		
		int points;
		
		Team(Player[] members, int startPoints) {
//			this.members = members;
			points = startPoints;
		}
		
//		boolean canSpawn() {
//			return points > 0;
//		}
	}
	
	protected Player[] players;
	
	protected Team team1, team2;
	
	protected Team winner;
	
	protected TugOfWar(Level level, Player[] team1, Player[] team2, int startPoints) {
		super(level, asOneArray(team1, team2));
		
		players = asOneArray(team1, team2);
		this.team1 = new Team(team1, startPoints);
		this.team2 = new Team(team2, startPoints);
		
		
	}

	@Override
	public void update() {
		if (team2.points <= 0)
			winner = team1;
		else if (team1.points <= 0)
			winner = team2;
	}

	@Override
	public boolean gameOver() {
		return winner != null;
	}
	
	@Override
	public String getWinner() {
		if (winner == team1)
			return "team 1";
		else if (winner == team2)
			return "team 2";
		else
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
