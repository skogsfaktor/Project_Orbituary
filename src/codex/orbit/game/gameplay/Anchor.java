package codex.orbit.game.gameplay;

import java.io.IOException;
import java.util.LinkedList;

import org.newdawn.slick.opengl.Texture;

import codex.orbit.entity.Sprite;
import codex.orbit.player.Player;
import codex.orbit.resources.Textures;

public class Anchor extends Sprite {
	protected final LinkedList<Rope> ropes = new LinkedList<Rope>();
	
	public Anchor(float x, float y) {
		super(x, y, Player.RADIUS * 0.9f, Player.RADIUS * 0.9f);
		Texture sprite;
		try {
			sprite = Textures.loadTexture("anchor", "entities/anchor.png");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		setTexture(sprite);
		setRotation((float)Math.random()*90);
	}
	
	public Rope attach(Player holder) {
		Rope rope = new Rope(this, holder);
		ropes.add(rope);
		return rope;
	}
	public void detach(Rope r) {
		ropes.remove(r);
	}
	
	@Override
	public void draw() {
//		for (Rope r : ropes)
//			r.draw();
		super.draw(1, 0, 0);
	}
}
