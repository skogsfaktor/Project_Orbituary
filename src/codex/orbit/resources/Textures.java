package codex.orbit.resources;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public final class Textures {
	private Textures() {}
	
	private final static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	/**
	 * Loads a texture into memory from a file, which can be accessed later by calling getTexture().
	 * 
	 * @param key A key which is used to identify the texture. Needed when calling getTexture().
	 * @param path The path to the file to be loaded.
	 * @throws IOException Thrown if the texture can't be loaded. Possibly because of wrong path.
	 */
	public static Texture loadTexture(String key, String path) throws IOException {
		return loadTexture(key, path, true);
	}
	public static Texture loadTexture(String key, String path, boolean smoothTextureFiltering) throws IOException {
		if (textures.containsKey(key))
			return getTexture(key);
		Texture tex = TextureLoader.getTexture(path.substring(path.lastIndexOf('.')+1), ResourceLoader.getResourceAsStream("textures/" + path), (smoothTextureFiltering && true ? 9729 : 9728));
		textures.put(key, tex);
		return tex;
	}
	
	public static void unloadTexture(String key) {
		Texture tex = textures.remove(key);
		tex.release();
	}
	public static void releaseMemory() {
		for (String key : textures.keySet())
			textures.get(key).release();
		String[] keys = getTextureKeys();
		for (int i = 0; i < keys.length; i++)
			textures.remove(keys[i]);
	}
	
	/**
	 * Gets a loaded texture.
	 * 
	 * @param key The key to the Texture as specified when loaded.
	 * @return Returns the texture. Returns null if key is invalid.
	 */
	public static Texture getTexture(String key) {
		return textures.get(key);
	}
	
	/**
	 * Gets all the available keys.
	 * 
	 * @return Returns a copy of all the keys.
	 */
	public static String[] getTextureKeys() {
		final String[] texKeys = new String[textures.size()];
		int i = 0;
		for (String s : textures.keySet())
			texKeys[i++] = new String(s);
		return texKeys;
	}
}
