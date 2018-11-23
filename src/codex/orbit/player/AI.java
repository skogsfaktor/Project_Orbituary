package codex.orbit.player;

public class AI extends Controller
{
	public AI(Player p) {
		super(p);
	}
	
	public void control() {
		if (!player.hasRope()) {
			player.toggleRope();
		}
	}
}
