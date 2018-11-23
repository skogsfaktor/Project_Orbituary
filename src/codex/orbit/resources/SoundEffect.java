package codex.orbit.resources;

import java.io.IOException;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class SoundEffect extends Sound {
	public SoundEffect() { }
	public SoundEffect(String path) throws IOException {
		this(path, 1, 1);
	}
	public SoundEffect(String path, float gain, float speed) throws IOException {
		super(path, gain, speed);
	}
	
	@Override
	public void setSound(String path) throws IOException {
		reset();
		audio = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("sounds/sfx/" + path + ".ogg"));
	}

	@Override
	public void start(boolean loop) {
		audio.playAsSoundEffect(gain, speed, loop);
	}
}
