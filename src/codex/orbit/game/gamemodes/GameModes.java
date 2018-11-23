package codex.orbit.game.gamemodes;

public enum GameModes {
	DEATH_MATCH(GameMode.class.getClass().asSubclass(DeathMatch.class)), // Last man standing wins
	TEAM_DEATH_MATCH(GameMode.class.getClass().asSubclass(TeamDeathMatch.class)), // Last team standing wins
	RACE(GameMode.class.getClass().asSubclass(Race.class)), // First to collect all flags wins
	TUG_OF_WAR(GameMode.class.getClass().asSubclass(TugOfWar.class)); // First team to have all points wins
	
	public final Class<? extends GameMode> mode;
	
	private final String description;
	
	GameModes(Class<? extends GameMode> mode) {
		this.mode = mode;
		
		StringBuilder sb = new StringBuilder();
		boolean upperCase = true;
		for (char c : super.toString().toCharArray()) {
			if (c == '_') {
				sb.append(" ");
				upperCase = true;
			} else {
				if (upperCase)
					sb.append(c);
				else
					sb.append(Character.toLowerCase(c));
				upperCase = false;
			}
		}
		description =  sb.toString();
	}
	
	@Override
	public String toString() {
		return description;
	}
	
	public String getDescription() {
		return toString();
	}
}
