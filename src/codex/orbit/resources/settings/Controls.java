package codex.orbit.resources.settings;

public final class Controls {
	protected static final byte ROPE_KEY = 0, POWER_KEY = 1;
	
	private final char[][] controls;
	
	protected Controls(char[][] controls) {
		this.controls = controls;
	}
	
	public char getRopeKey(int player) throws IllegalArgumentException {
		if (player < 0 || player >= controls.length)
			throw new IllegalArgumentException("No controls found! Not enough mapped controls");
		return controls[player][0];
	}
}
