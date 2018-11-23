package codex.orbit.input;

import org.lwjgl.input.Keyboard;

public final class Input
{
	private static final boolean[] keys = new boolean[65536];
	
	/**
	 * Returns true if the specified key has been pressed. The key has to be released before returning true again.
	 * 
	 * @param key The key to test if it has been pressed.
	 * @return Returns true if the key has been pressed. Returns false if the key is still down or if the key hasn't been pressed.
	 */
	public static boolean keyPressed(int key) {
		if (key < 0 || key >= keys.length) {
			System.err.println(key + " out of bounds!");
			return false;
		}
		if (isKeyDown(key)) {
			if (keys[key]) {
				return false;
			} else {
				keys[key] = true;
				return true;
			}
		} else {
			keys[key] = false;
			return false;
		}
	}
	
	/**
	 * Return true or false depending on if the specified key is pressed down. Same as calling Keyboard.isKeyDown(key).
	 * 
	 * @param key The key to test if it is down.
	 * @return Returns true or false depending on if the specified key is pressed down.
	 */
	public static boolean isKeyDown(int key) {
		return Keyboard.isKeyDown(key);
	}
}
