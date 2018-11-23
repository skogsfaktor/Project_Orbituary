package codex.orbit.resources;

import java.io.IOException;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;

public abstract class Sound {
	protected Audio audio;
	
	protected float gain = 1;
	protected float speed = 1;
	
	Sound() { }
	Sound(String path) throws IOException {
		this(path, 1, 1);
	}
	Sound(String path, float gain, float speed) throws IOException {
		this.gain = gain;
		this.speed = speed;
		setSound(path);
	}
	
	public abstract void setSound(String path) throws IOException;
	
	public void reset() {
		if (audio == null)
			return;
		stop();
		audio = null;
	}
	
//	private double thetaPitch;
	public void update(final double delta) {
		SoundStore ss = SoundStore.get();
//		float pitch;
//		pitch = 1 + (float)(Math.sin(Math.PI / 2d * thetaPitch) * 0.2 + 0.4);
//		pitch = 1 + (float)(Math.sin(Math.PI * 2d * thetaPitch) * 0.08 + 0.3) - 0.3f;
//		thetaPitch += delta * 0.008 * 10;
//		thetaPitch %= 0.5;
////		thetaPitch = 1;
////		pitch = 0.8f;
////		pitch = 1.4f;
////		pitch = 1;
//		ss.setMusicPitch(pitch);
//		System.out.println(pitch);
		ss.poll(0);
	}
	public void destroy() {
		if (!AL.isCreated())
			return;
		reset();
		AL.destroy();
	}
	
	public float getGain() {
		return gain;
	}
	public void setGain(float gain) {
		this.gain = gain;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public abstract void start(boolean loop);
	
	public void stop() {
		audio.stop();
	}
	
	public boolean isPlaying() {
		return audio.isPlaying();
	}
}
