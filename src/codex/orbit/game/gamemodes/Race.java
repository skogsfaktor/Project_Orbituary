package codex.orbit.game.gamemodes;

import codex.orbit.level.Level;
import codex.orbit.player.Player;

public class Race extends GameMode
{
	protected Race(Level level, Player[] players) {
		super(level, players);
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
}
