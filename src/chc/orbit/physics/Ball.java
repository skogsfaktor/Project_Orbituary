package chc.orbit.physics;

import org.lwjgl.util.vector.Vector2f;

import codex.orbit.player.Player;
import codex.orbit.game.gameplay.Anchor;
import codex.orbit.game.gameplay.Rope;

import java.util.Random;

public class Ball extends PhysicalObject {

	public float radius;
	private Rope rope;
	public boolean dead = false;
	public boolean bounces = false;
	public float rotationalVelocity = 0.0f;
	//public float rotationalOrientation = 0;
	private float angularVelocity = 0.0f;
	private float angle = 0.0f;
	private float stunTimer = 0;
	private float rl;
	public boolean stunned = false;
	private Vector2f vExit = new Vector2f(1.0f, 1.0f);
	
	static Random rnd = new Random(System.nanoTime());
	
	public Ball(float x, float y, float radius, float mass, PhysicalSpace phy) {
		super(mass, x, y, phy);
		this.radius = radius;
	}
	
	public void kill() {
		detach();
		dead = true;
		position.x = (rnd.nextFloat() * 2 - 1) * phy.widthR * 0.75f; 
		position.y = (rnd.nextFloat() * 2 - 1) * phy.heightR * 0.75f;
		float ran = rnd.nextFloat(); if (rnd.nextInt() % 2 == 0) ran = -ran;
		float ran2 = rnd.nextFloat(); if (rnd.nextInt() % 2 == 0) ran2 = -ran2;
		velocity.x = ran / 500;
		velocity.y = ran / 500;
		
		velocity.x = 0;
		velocity.y = 0.002f;
		
//		dead = true;
//		velocity = new Vector2f();
	}
	
	public void stun(float ms){
		stunTimer += ms;
		stunned = true;
		if(hasRope()){
			detach();
		}
	}
	
	public boolean isStunned(){
		return stunned;
	}

	public void move() {
		if (dead) return;
		double t = phy.elapsedTime;
		if (stunTimer > 0) {
			stunTimer -= t;
		} else
		{
			stunned = false;
			stunTimer = 0;
		}
		//rotationalOrientation += rotationalVelocity * t;
		if (!hasRope()) {
			if (affectedByGravity) { velocity.x += phy.globalForce.x * t; velocity.y += phy.globalForce.y * t; }
			position.x += velocity.x * t; position.y += velocity.y * t;
		} 
		else {
			orbit();
		}
			checkBounds();
	}
	
	private void orbit(){
		float t = phy.elapsedTime;
		angle += angularVelocity * t;
		
		float newx = rope.getAnchor().getX() + (float) (Math.cos(angle) * rope.getLength());
		float newy = rope.getAnchor().getY() + (float) (Math.sin(angle) * rope.getLength());
		
		vExit.x = position.x - newx; vExit.y = position.y - newy;
		
		position.x = newx;
		position.y = newy;
		rope.length += 0.00018f * t;
		rl = rope.getLength();
		float av = 0.000002f * t;
		if (angularVelocity > 0)
			angularVelocity += av;
		else
			angularVelocity -= av;
	}
	
	private void checkBounds(){
		if (position.x + radius > phy.widthR){
			kill();
		}
		if (position.x - radius < -phy.widthR){
			kill();
		}
		if (position.y + radius > phy.heightR){
			kill();
		}
		if (position.y - radius < -phy.heightR){
			kill();
		}
	}
	
	static public Ball randomBall(PhysicalSpace phy){
		Random rnd = new Random(System.nanoTime());
		Ball b = new Ball((rnd.nextFloat() * 2 - 1) * phy.widthR * 0.75f, (rnd.nextFloat() * 2 - 1) * phy.heightR * 0.75f, Player.RADIUS, 2.0f, phy);
		float ran = rnd.nextFloat(); if (rnd.nextInt() % 2 == 0) ran = -ran;
		float ran2 = rnd.nextFloat(); if (rnd.nextInt() % 2 == 0) ran2 = -ran2;
		b.velocity = new Vector2f(ran / 300, ran2 / 500);
		return(b);
	}
	
	public boolean hasRope() {
		return rope != null;
	}
	
	public void attach(Rope r) {
		rope = r;
		Anchor a = rope.getAnchor();
		
		float dx = position.x - a.getX();
		float dy = position.y - a.getY();
		
		angularVelocity = -1.0f;
		
		boolean xdom = false;
		if (Math.abs(velocity.x) > Math.abs(velocity.y))
			xdom = true;
		
		if (dx == 0) {
			if (dy == 0) {
				angle = 0.0f;
			} else {
				if (dy < 0) {
					angle = (float) (-Math.PI/2);
				}
			}
		}
		
		if (dy == 0) {
			if (dx > 0) {
				angle = 0.0f;
			} else
				angle = (float) Math.PI;
		}
					
		if (dx > 0){
			if (dy > 0) {
				if (xdom) {
					if (velocity.x < 0)
						angularVelocity *= -1;
				} else {
					if (velocity.y > 0)
						angularVelocity *= -1;
				}
				angle = (float) Math.atan(dy / dx);
			} else
				if (xdom) {
					if (velocity.x > 0)
						angularVelocity *= -1;
				} else {
					if (velocity.y > 0)
						angularVelocity *= -1; 
				}
				angle = (float) Math.atan(dy / dx);
		} else {
			if (dy > 0) {
				if (xdom) {
					if (velocity.x < 0)
						angularVelocity *= -1;
				} else {
					if (velocity.y < 0)
							angularVelocity *= -1;
					}
					angle = (float) (Math.PI + Math.atan(dy / dx));
				} else
					if (xdom) {
						if (velocity.x > 0)
							angularVelocity *= -1;
					} else {
						if (velocity.y < 0)
							angularVelocity *= -1;
					}
					angle = (float) (Math.PI + Math.atan(dy / dx));
				}
		
		
		angularVelocity *= velocity.length() / rope.getLength();
		
		position.x =  rope.getAnchor().getX() + (float) (Math.cos(angle) * rope.getLength());
		position.y =  rope.getAnchor().getY() + (float) (Math.sin(angle) * rope.getLength());
		
		}
	
	public String toString(){
		return new String(position.x + " " + position.y);
	}

	public void detach() {
		velocity = (Vector2f) vExit.normalise();
		velocity.scale(-Math.abs(angularVelocity) * rl);
		System.out.println(velocity.x + " : " + velocity.y);
		angle = 0.0f;
		angularVelocity = 0.0f;
		rope = null;
	}
}