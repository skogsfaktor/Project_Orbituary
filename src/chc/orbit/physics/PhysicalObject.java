package chc.orbit.physics;

import org.lwjgl.util.vector.Vector2f;

public abstract class PhysicalObject {

	PhysicalSpace phy;
	public float mass;
	public boolean collidable = true;
	public boolean affectedByGravity = true;
	public Vector2f velocity = new Vector2f();
	public Vector2f position = new Vector2f();
	
	public PhysicalObject(float mass, float x, float y, PhysicalSpace phy) {
		this.mass = mass; position.x = x; position.y = y; this.phy = phy;
		phy.addObject(this);
	}
	
	public double getX() {
		return position.x;
	}
	
	public double getY() {
		return position.y;
	}
	
	abstract void move();
}