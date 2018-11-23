package codex.orbit.customMenu;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import codex.orbit.Main;
import codex.orbit.resources.Music;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("deprecation")
public class OGLMenu extends Canvas {
	private static final long serialVersionUID = 1L;
	
	private Thread thread;
	private boolean running;
	
	private TrueTypeFont font;
	
	private Texture background;
	
	private Music music;
	
	private int numberOfPlayers = 2;
	
	public OGLMenu(int width, int height) {
		setSize(width, height);
		setFocusable(true);
		setIgnoreRepaint(true);
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		start();
	}
	public void start() {
		if (running)
			return;
		running = true;
		thread = new Thread() {
			public void run() {
				initOGL();
				requestFocus();
				init();
				while (running) {
					music.update(1);
					glViewport(0, 0, Display.getWidth(), Display.getHeight());
					update();
					render();
				}
				music.stop();
				
				background.release();
				
				Display.destroy();
			}
		};
		thread.start();
	}
	@Override
	public void removeNotify() {
		stop();
		super.removeNotify();
	}
	public void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread = null;
	}
	
	private void initOGL() {
		try {
			Display.setParent(OGLMenu.this);
			Display.create();
			Display.makeCurrent();
		} catch (LWJGLException e) {
			Display.destroy();
			throw new RuntimeException(e);
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1280, 720, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glEnable(GL_TEXTURE_2D);
	}
	
	private void init() {
		try {
			background = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("textures/environment/backgrounds/galaxy_1.jpg"));
			
			try {
				Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("fonts/GOODTIME.ttf"));
				awtFont2 = awtFont2.deriveFont(50f); // set font size
				font = new TrueTypeFont(awtFont2, true);
			} catch (FontFormatException e) {
				e.printStackTrace();
			}
			
			music = new Music("Menu");
			music.start(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void update() {
		while (Keyboard.next()) {
			final boolean state = Keyboard.getEventKeyState();
			switch (Keyboard.getEventKey()) {
			case Keyboard.KEY_UP:
				if (state)
					if (numberOfPlayers < 8)
						numberOfPlayers++;
				break;
			case Keyboard.KEY_DOWN:
				if (state)
					if (numberOfPlayers > 2)
						numberOfPlayers--;
				break;
			case Keyboard.KEY_RETURN:
			case Keyboard.KEY_SPACE:
				if (state)
					Main.startGame(numberOfPlayers, 0);
				break;
			}
		}
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity();
		{
			background.bind();
			glBegin(GL_QUADS);
			{
				glColor3f(1, 1, 1);
				
				glTexCoord2f(0, 0);
				glVertex2i(0, 0);
				glTexCoord2f(0, 1);
				glVertex2i(0, 720);
				glTexCoord2f(1, 1);
				glVertex2i(1280, 720);
				glTexCoord2f(1, 0);
				glVertex2i(1280, 0);
			}
			glEnd();
			
			Color.white.bind();
			{
				String txt = "Orbituary";
				int width = font.getWidth(txt);
				font.drawString(1280 / 2 - width / 2, 80, txt, Color.yellow);
			}
			{
				String txt = "Number of players: " + numberOfPlayers;
				int width = font.getWidth(txt);
				font.drawString(1280 / 2 - width / 2, 630, txt, Color.yellow);
			}
		}
		Display.update();
		Display.sync(60);
	}
}
