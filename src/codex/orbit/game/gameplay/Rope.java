package codex.orbit.game.gameplay;

import codex.orbit.entity.Entity;
import codex.orbit.player.Player;

import static org.lwjgl.opengl.GL11.*;

public class Rope
{
	protected final Anchor anchor;
	protected final Entity holder;
	
	public float length;
	
	public Rope(Anchor anchor, Player holder) {
		if (anchor == null)
			anchor = new Anchor(10,10);
		this.anchor = anchor;
		this.holder = holder;

		length = (float)Math.sqrt(Math.pow(anchor.getX() - holder.getX(), 2) + Math.pow(anchor.getY() - holder.getY(), 2));
	}
	
	public static Rope attach(Anchor anchor, Player holder) {
		return new Rope(anchor, holder);
	}
	public void detach() {
		anchor.detach(this);
	}
	
	public Anchor getAnchor() {
		return anchor;
	}
	public Entity getHolder() {
		return holder;
	}
	
	public float getLength() {
		return length;
	}
	
	public void draw() {
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_LINES);
		{
			System.out.println(anchor.getX() + ", " + anchor.getY() + " <-> " + holder.getX() + ", " + holder.getY());
			glColor3f(1, 1, 1);
			glVertex2f(anchor.getX(), anchor.getY());
			glVertex2f(holder.getX(), holder.getY());
		}
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
}
