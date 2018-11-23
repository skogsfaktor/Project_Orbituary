package codex.orbit.player;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import codex.orbit.entity.Sprite;
import codex.orbit.game.Game;
import codex.orbit.game.gameplay.Rope;
import codex.orbit.resources.Textures;

import chc.orbit.physics.*;

public class Player extends Sprite
{	
	public static final float RADIUS = 0.05f;
	private static final Random rnd = new Random();
	
	private Game g;
	
	private Texture[] sprites;
	
	private Sprite background;
	
	private Controller controller;
	
	public Ball body;
	
	public Rope rope;
	
	private int ind;
	private long lastTime = System.currentTimeMillis();
	private int wait = 500;
	
//	private static int i;
//	private float vx, vy; {
//		final float speed = 0.01f;
//		Vector2f speedVec = new Vector2f((rnd.nextFloat() * 2 - 1), (rnd.nextFloat() * 2 - 1));
//		speedVec.normalise();
//		vx = speedVec.x * speed;
//		vy = speedVec.y * speed;
//	}
//	
	private float backgroundRot;
	
	/** 
	 * Initializes a new AI player.
	 */
	public Player(Game g) {
		super(0, 0, RADIUS, RADIUS);
		controller = new AI(this);
		body = Ball.randomBall(g.physicalSpace);
		this.g = g;
		init();
	}
	/** 
	 * Initializes a new human player with the specified controls.
	 * @param ropeKey The key to press to activate the rope.
	 * @param powerKey The key to press to activate an ability.
	 */
	public Player(Game g, int ropeKey, int powerKey) {
		super(0, 0, RADIUS, RADIUS);
		controller = new Human(this, ropeKey, powerKey);
		body = Ball.randomBall(g.physicalSpace);
		this.g = g;
		init();
	}
	
	private void init() {
		String[] faces = new String[]{
				"happy",
				"angry"
			};
		sprites = new Texture[faces.length];
		try {
			for (int i = 0; i < faces.length; i++)
				sprites[i] = Textures.loadTexture(faces[i], "players/character_" + faces[i] + ".png");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		setTexture(sprites[ind]);
		try {
			setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/players/character_base.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			background = new Sprite(0, 0, RADIUS * 1.3f, RADIUS * 1.3f, Textures.loadTexture("character_background", "players/character_background.png"));
		} catch (IOException e) {
			background = null;
			e.printStackTrace();
		}
	}
	
	private int passedTime;
	@Override
	public void update(final float delta) {
		if (!isAlive() || body.stunned)
			rope = null;
		if (rope != null && rope.getHolder() != this)
			toggleRope();
		
		controller.control();
		
		final long time = System.currentTimeMillis();
		final int deltaTime = (int)(time - lastTime);
		if (deltaTime >= wait) {
			ind = (++ind) % sprites.length;
			lastTime = time;
//			setTexture(sprites[ind]);
		}
		
//		x += vx * delta;
//		y += vy * delta;
//		
//		float width = 16f / 9f;
//		if (x + RADIUS > width) {
//			x = width - RADIUS;
//			vx = -vx;
//		}
//		if (x - RADIUS < -width) {
//			x = -width + RADIUS;
//			vx = -vx;
//		}
//		
//		float height = 1 * 0.9f;
//		if (y + RADIUS > height) {
//			y = height - RADIUS;
//			vy = -vy;
//		}
//		if (y - RADIUS < -height) {
//			y = -height + RADIUS;
//			vy = -vy;
//		}
//		
		color += deltaColor * delta;
		color %= 1;
		passedTime += deltaTime;
		if (passedTime >= 1000) {
			passedTime -= 1000;
			deltaColor = (rnd.nextFloat() * 2 - 1) * 0.005f;
		}
		
		backgroundRot += 3 * delta;
		backgroundRot %= 360;
		
		setX(body.position.x);
		setY(body.position.y);
		
		body.dead = false;
	}
	
	private float color = rnd.nextFloat(), deltaColor;
	@Override
	public void draw() {
		Vector3f col = getColor();
		float r = col.x;
		float g = col.y;
		float b = col.z;
		
		if (rope != null)
			rope.draw();
		
		background.setX(x);
		background.setY(y);
		background.setRotation(-backgroundRot);
		{
			float scale = 1 + (float)(Math.cos(Math.toRadians(backgroundRot * 2.0)) + 1) / 2f * 0.2f;
			float widthR = background.getWidthRadius();
			float heightR = background.getHeightRadius();
			background.setWidthRadius(widthR * scale);
			background.setHeightRadius(heightR * scale);
			background.draw(b, g, r);
			background.setWidthRadius(widthR);
			background.setHeightRadius(heightR);
		}
		background.setRotation(backgroundRot);
		background.draw(b, 1 - g, r);
		
		super.draw(r, g, b);
	}
	
	public Vector3f getColor() {
		Color col = Color.getHSBColor(color, 1, 1);
		float r = col.getRed() / 255f;
		float g = col.getGreen() / 255f;
		float b = col.getBlue() / 255f;
		
		return new Vector3f(r, g, b);
	}
	
	public void toggleRope() {
		if (body.isStunned() || !isAlive())
			return;
		if (hasRope()) {
			body.detach();
			rope = null;
			}
		else {
			Rope r = new Rope(g.getClosestAnchor(this),this);
			if (r != null) {
				rope = r;
				body.attach(rope);
			}
		}
	}
	
	public boolean hasRope() {
		return rope != null;
	}
	
	public boolean isAlive() {
		return !body.dead;
	}
}
