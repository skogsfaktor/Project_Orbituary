package chc.orbit.physics;

import java.util.ArrayList;
import org.lwjgl.util.vector.Vector2f;

public class PhysicalSpace {

	private long lastTime = System.currentTimeMillis();
	public float elapsedTime;
	public float timeScale = 1;
//	private boolean running;
	float widthR, heightR;
	Vector2f globalForce = new Vector2f(0.0f, -0.00002f);
	ArrayList<PhysicalObject> objects = new ArrayList<PhysicalObject>();
	ArrayList<Vector2f> anchorPoints = new ArrayList<Vector2f>();
	ArrayList<Vector2f> collisionPoints = new ArrayList<Vector2f>();
	
	public PhysicalSpace(float widthR, float heightR) {
		this.widthR = widthR; this.heightR = heightR;
//		running = false;
		System.out.println(widthR + " x " + heightR);
	}
	
	public void addObject(PhysicalObject o){
		if (o != null)
			objects.add(o);
	}
	
//	public void run(){
//		running = true;
//	}
	
	public void update(float delta){
//		if (!running){ return; }
		
		elapsedTime = (System.currentTimeMillis() - lastTime) * timeScale * delta;
		lastTime = System.currentTimeMillis();
		
		for (PhysicalObject o : objects){
			o.move();
		}
		for (int i = 0; i < objects.size(); i++){
				for (int y = i + 1; y < objects.size(); y++){
					collide(objects.get(i), objects.get(y));
				}
		}
	}
	
	public void collide(PhysicalObject o1, PhysicalObject o2){
		if (!(o1.collidable) || !(o2.collidable))
			return;
		if (o1 instanceof Ball && o2 instanceof Ball){
			Ball b1 = (Ball)o1; Ball b2 = (Ball)o2;
			Vector2f posd = new Vector2f();
			Vector2f.sub(b1.position, b2.position, posd);
			double radiiSquared = Math.pow((b1.radius + b2.radius), 2);
			if (posd.lengthSquared() <= radiiSquared){
				//b1.kill(); b2.kill();
				b1.stun(300); b2.stun(300);
				float dl = posd.length();
				Vector2f d;
				if (dl == 0.0f)
				{
					posd = new Vector2f(b1.radius + b2.radius, 0.0f);
					dl = b1.radius + b2.radius - 1.0f;
					
					d = new Vector2f(posd);
					d.scale((((b1.radius + b2.radius)-dl)/dl));
					
				} else {
					d = new Vector2f(posd);
					d.scale((((b1.radius + b2.radius)-dl)/dl));
				}

				float m1 = 1 / b1.mass; float m2 = 1 / b2.mass;
				Vector2f td1 = new Vector2f(d); td1.scale(m1 / (m1 + m2));
				Vector2f td2 = new Vector2f(d); td2.scale(m2 / (m1 + m2));
				Vector2f.add(td1, b1.position, b1.position);
				Vector2f.sub(b2.position, td2, b2.position);
				
				float cpx = (b1.position.x * b2.radius + b2.position.x * b1.radius) / (b1.radius + b2.radius); 
				float cpy = (b1.position.y * b2.radius + b2.position.y * b1.radius) / (b1.radius + b2.radius); 
				collisionPoints.add(new Vector2f(cpx, cpy));
				
				Vector2f v = new Vector2f(); Vector2f dn = new Vector2f();
				Vector2f.sub(b1.velocity, b2.velocity, v);
				d.normalise(dn);
				float vv = Vector2f.dot(dn, v);
				if (vv > 0.0f)
					return;
				
				dn.scale(vv * -2.0f / (m1 + m2));
				Vector2f d1 = new Vector2f(dn); Vector2f d2 = new Vector2f(dn);
				d1.scale(m1); d2.scale(m2);
				Vector2f.add(d1, b1.velocity, b1.velocity);
				Vector2f.sub(b2.velocity, d2, b2.velocity);
				
				float temprv = b1.rotationalVelocity;
				b1.rotationalVelocity += b2.rotationalVelocity;
				b2.rotationalVelocity += temprv;
			}
		}
	}
}
