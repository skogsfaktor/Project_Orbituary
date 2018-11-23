package codex.orbit.game;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Canvas;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import chc.orbit.physics.PhysicalSpace;

import codex.orbit.Main;
import codex.orbit.entity.Entity;
import codex.orbit.game.gameplay.Anchor;
import codex.orbit.level.Level;
import codex.orbit.player.Player;
import codex.orbit.resources.Music;
import codex.orbit.resources.Screenshot;
import codex.orbit.resources.Sound;
import codex.orbit.resources.Textures;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private static final byte EXIT_ON_CLOSE = 0, MAIN_MENU_ON_CLOSE = 1;
	
	private byte closeOperation = MAIN_MENU_ON_CLOSE;
	
	private final int targetFPS;
	
	private boolean resized;
	
	private float screenW, screenH;
	private float gameW, gameH; {
		gameW = 4; gameH = 3;
		gameW = 16; gameH = 9;
		gameW = 16; gameH = 10;
		
		DisplayMode desktop = Display.getDesktopDisplayMode();
		gameW = desktop.getWidth(); gameH = desktop.getHeight();
	}
	private float levelW, levelH;
	
	private Thread thread;
	private boolean running;
	private boolean paused = true;
	
	private Sound music;
	
	private Level level;
	private boolean drawBackground = true;
	
	private final int nrOfPlayers, nrOfBots;
	private Player[] players;
	
	public PhysicalSpace physicalSpace;
	
	static private Anchor[] anchors = new Anchor[10];
	
	/**
	 * 
	 * @param width The width of the window.
	 * @param height The height of the window.
	 * @param nrOfPlayers Number of total players in the game.
	 * @param nrOfBots Number of players which are bots.
	 */
	public Game(int width, int height, int nrOfPlayers, int nrOfBots, int nrOfAnchors) {
		if (nrOfBots > nrOfPlayers || nrOfBots < 0)
			throw new IllegalArgumentException("There can't be more bots than allowed players or a negative amount of bots or players");
		setSize(width, height);
		setFocusable(true);
		setIgnoreRepaint(true);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resized = true;
			}
		});
		targetFPS = Display.getDesktopDisplayMode().getFrequency();
		
		this.nrOfPlayers = nrOfPlayers;
		this.nrOfBots = nrOfBots;
		players = new Player[this.nrOfPlayers];
	}
	public Game(int width, int height, int nrOfPlayers, int nrOfAnchors) {
		this(width, height, nrOfPlayers, 0, nrOfAnchors);
	}
	
	private void initDisplay() {
		try {
			Display.setParent(this);
			Display.create();
			Display.makeCurrent();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		requestFocus();
	}
	private void initOGL() {
		resizeScreen();
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glEnable(GL_TEXTURE_2D);
		
		glLineWidth(2);
		glEnable(GL_LINE_SMOOTH);
	}
	private void resizeScreen() {
		// Maintain screen aspect-ratio in-game independent of the size of the window.
		/**
		 * TODO: needs cleaning
		 */
		final float windowW = Display.getWidth();
		final float windowH = Display.getHeight();
		final float windowAspectRatio = windowW / windowH;
		final float gameAspectRatio = gameW / gameH;
		levelW = gameAspectRatio;
		levelH = 1;
		if (windowW >= windowH * gameAspectRatio) {
			screenW = gameAspectRatio / (gameAspectRatio / windowAspectRatio);
			screenH = levelH;
		} else {
			screenW = gameAspectRatio * levelH;
			screenH = gameAspectRatio / windowAspectRatio;
		}
		
		int viewW = (int)(Display.getWidth() / screenW * levelW);
		int viewH = (int)(Display.getHeight() / screenH * levelH);
		glViewport(Display.getWidth() / 2 - viewW / 2, Display.getHeight() / 2 - viewH / 2, viewW, viewH);
		screenW = gameAspectRatio;
		screenH = 1;
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glFrustum(-screenW, screenW, -screenH, screenH, 1, 2);
		glMatrixMode(GL_MODELVIEW);
	}
	private void init() {
		level = new Level(levelW, levelH * 0.9f);
		physicalSpace = new PhysicalSpace(levelW, levelH * 0.9f);
		
		for (int i = 0; i < nrOfPlayers; i++) {
			Player p;
			if (i < nrOfBots)
				p = new Player(this);
			else {
				switch (i - nrOfPlayers) {
					case -1: p = new Player(this, Keyboard.KEY_A, Keyboard.KEY_S); break;
					case -2: p = new Player(this, Keyboard.KEY_K, Keyboard.KEY_L); break;
					default: p = new Player(this, Keyboard.KEY_N, Keyboard.KEY_M);
				}
			}
			players[i] = p;
		}
		
		Random rnd = new Random();
		for (int i = 0; i < anchors.length; i++) {
			anchors[i] = new Anchor((rnd.nextFloat() * 2 - 1) * (levelW * 0.5f + levelW * 0.25f), (rnd.nextFloat() * 2 - 1) * (levelH * 0.5f + levelH * 0.25f));
		}
		
		try {
			int trackNumber = rnd.nextInt(2) + 1;
			String track = "Game_" + trackNumber;
//			track = "Game_2";
			music = new Music(track, 1.0f, 1);
		} catch (IOException e) {
			e.printStackTrace();
			stop();
		}
		
		lastTime = System.currentTimeMillis();
	}
	
	public void addNotify() {
		super.addNotify();
		start();
	}
	public void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this, "Game loop");
		thread.start();
	}
	public void removeNotify() {
		if (!Main.isApplet())
			closeOperation = EXIT_ON_CLOSE;
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
			System.exit(1);
		} finally {
			thread = null;			
		}
	}
	
	public void run() {
		initDisplay();
		initOGL();
		init();
		
		while (running) {
			update();
			render();
		}
		
		try {
			level.finalize();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		for (Player p : players)
			p.setTexture(null);
		for (Anchor a : anchors)
			a.setTexture(null);
		
		Textures.releaseMemory();
		
		try {
			Display.setFullscreen(false);
			Display.setVSyncEnabled(false);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Mouse.setGrabbed(false);
		
		Display.destroy();
		
		switch (closeOperation) {
		case MAIN_MENU_ON_CLOSE:
			Main.startMenu();
			break;
		case EXIT_ON_CLOSE:
		default:
			if (Main.isApplet())
				return;
			AL.destroy();
			System.exit(0);
		}
	}
	private long lastTime;
	public void update() {
		if (resized) {
			resizeScreen();
			resized = false;
		}
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_ESCAPE:
					running = false;
					closeOperation = MAIN_MENU_ON_CLOSE;
					break;
				case Keyboard.KEY_SPACE:
					paused = !paused;
					if (!paused)
						if (!music.isPlaying())
							music.start(true);
					break;
				case Keyboard.KEY_F1:
					paused = true;
					try {
						Display.setFullscreen(!Display.isFullscreen());
//						Display.setVSyncEnabled(Display.isFullscreen());
						Display.setVSyncEnabled(false);
						Mouse.setGrabbed(!Mouse.isGrabbed());
						resizeScreen();
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
					break;
				case Keyboard.KEY_F2:
					if (!Main.isApplet())
						new Screenshot();
					break;
					
				case Keyboard.KEY_F3:
					drawBackground = !drawBackground;
					break;
				}
			}
		}
		final float delta; {
			long time = System.currentTimeMillis();
			delta = (time - lastTime) / (1000f / targetFPS);
			lastTime = time;
		}
		for (Anchor a : anchors)
			a.update(delta);
		if (paused)
			return;
		
		music.update(delta);
		
		physicalSpace.update(delta/5);
		
		for (Player p : players)
			p.update(delta/5);
	}
	public void render() {
		glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
		glLoadIdentity();
		glTranslatef(0, 0, -1);
		glColor3f(1, 1, 1);
		{
			if (drawBackground)
				level.draw();
			
			for (Anchor a : anchors)
				a.draw();
			
			for (Player p : players)
				p.draw();
			
			// UI
			glDisable(GL_TEXTURE_2D);
			glColor3f(0, 0, 0);
			glBegin(GL_QUADS);
			{
				// TOP BAR
				glVertex2f(-levelW, levelH);
				glVertex2f(-levelW, levelH * 0.9f);
				glVertex2f(levelW, levelH * 0.9f);
				glVertex2f(levelW, levelH);
				
				// BOTTOM BAR
				glVertex2f(-levelW, -levelH);
				glVertex2f(levelW, -levelH);
				glVertex2f(levelW, -levelH * 0.9f);
				glVertex2f(-levelW, -levelH * 0.9f);
				
				// Darken screen when paused
				if (paused) {
					glColor4f(0, 0, 0, 0.7f);
					glVertex2f(-levelW, levelH);
					glVertex2f(-levelW, -levelH);
					glVertex2f(levelW, -levelH);
					glVertex2f(levelW, levelH);
				}
			}
			glEnd();
			glEnable(GL_TEXTURE_2D);
		}
		Display.update();
		Display.sync(targetFPS);
	}
	
	public Anchor getClosestAnchor(Entity e) {
		Anchor closest = null;
		double d = 10;
		for (Anchor a : anchors) {
			double ad = Math.pow(e.getX() - a.getX(), 2) + Math.pow(e.getY() - a.getY(), 2);
			if (ad < d) {
				d = ad;
				closest = a;
			}
		}
		return closest;
	}
}
