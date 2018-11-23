package codex.orbit.resources;

import java.io.IOException;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Music extends Sound {
	public Music() { }
	public Music(String path) throws IOException {
		this(path, 1, 1);
	}
	public Music(String path, float gain, float speed) throws IOException {
		super(path, gain, speed);
	}
	
	@Override
	public void setSound(String path) throws IOException {
		reset();
		audio = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("sounds/music/" + path + ".ogg"));
	}

	@Override
	public void start(boolean loop) {
		audio.playAsMusic(gain, speed, loop);
	}
}
