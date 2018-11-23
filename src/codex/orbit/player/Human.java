package codex.orbit.player;

import codex.orbit.input.Input;

public class Human extends Controller
{
	private final int ropeKey, powerKey;
	
	public Human(Player p, int ropeKey, int powerKey) {
		super(p);
		this.ropeKey = ropeKey;
		this.powerKey = powerKey;
	}
	
	public void control() {
		if (Input.isKeyDown(ropeKey) && !player.hasRope()) {
			player.toggleRope();
		} else if (!Input.isKeyDown(ropeKey) && player.hasRope())
			player.toggleRope();
		if (Input.keyPressed(powerKey)) {
		}
	}
}
