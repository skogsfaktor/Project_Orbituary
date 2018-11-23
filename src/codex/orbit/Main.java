package codex.orbit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.lwjgl.openal.AL;
import org.newdawn.slick.util.ResourceLoader;

import codex.orbit.customMenu.OGLMenu;
import codex.orbit.game.Game;

public class Main extends JApplet {
	private static final long serialVersionUID = 1L;
	
	private static boolean isApplet;
	public static final JPanel window = new JPanel(new BorderLayout());
	private static Game game;
	
	public static void main(String[] args) {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if (info.getName().equalsIgnoreCase("Nimbus")) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		isApplet = false;
		
		final String TITLE = "Project Orbituary";
		final JFrame frame = new JFrame(TITLE);
		{
			final byte size = 64;
			BufferedImage icon;
			try {
				icon = ImageIO.read(ResourceLoader.getResourceAsStream("orbicon.png"));
			} catch (IOException ex) {
				ex.printStackTrace();
				
				icon = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				for (int y = 0; y < size; y++) {
					for (int x = 0; x < size; x++) {
						int a, r, g, b;
						if (Math.sqrt(Math.pow(x - (size / 2.0), 2) + Math.pow(y - (size / 2.0), 2)) >= size / 2.0) {
							a = 128;
							a = 0;
							r = g = b = 0;
						}
						else {
							a = 255;
							Random rnd = new Random();
							
							r = rnd.nextInt(256);
							g = rnd.nextInt(256);
							b = rnd.nextInt(256);
							
							float hue = (float)(Math.atan2(y - size / 2.0, x - size / 2.0) / (2.0 * Math.PI));
							float saturation = (float)Math.sqrt(Math.pow(x - size / 2.0, 2) + Math.pow(y - size / 2.0, 2)) / (size / 2f);
							float brightness = 1f;
							
							int rgb = Color.HSBtoRGB(hue, saturation * (rnd.nextFloat() * 0.75f + 0.25f), brightness);
							
//							a = (int)((1 - saturation) * 255f);
							r = (rgb >> 16) & 0xFF;
							g = (rgb >> 8) & 0xFF;
							b = (rgb >> 0) & 0xFF;
						}
						
						icon.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
					}
				}
			}
			frame.setIconImage(icon);
		}
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.remove(window);
				if (AL.isCreated())
					AL.destroy();
				System.exit(0);
			}
		});
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Container pane = frame.getContentPane();
				frame.setTitle(TITLE + " - " + pane.getWidth() + " x " + pane.getHeight());
			}
		});
		frame.setResizable(true);
		frame.getContentPane().setPreferredSize(new Dimension(640, 480));
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.getContentPane().setPreferredSize(new Dimension(1280, 720));
		frame.add(window);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		startMenu();
	}
	
	@Override
	public void init() {
		isApplet = true;
		setLayout(new BorderLayout());
		add(window);
		setVisible(true);
	}
	public void start() {
		window.setSize(getContentPane().getSize());
		window.setMinimumSize(getContentPane().getSize());
		window.setPreferredSize(getContentPane().getSize());
		
		startMenu();
	}
	@Override
	public void destroy() {
		removeAll();
		if (AL.isCreated())
			AL.destroy();
		super.destroy();
	}
	
	public static boolean isApplet() {
		return isApplet;
	}
	
	public static void startMenu() {
		window.removeAll();
		window.add(new OGLMenu(window.getWidth(), window.getHeight()));
	}
	public static void startGame(final int nrOfPlayers, final int nrOfBots) {
		new Thread() {
			public void run() {
				window.removeAll();
				game = new Game(window.getWidth(), window.getHeight(), nrOfPlayers, 10);
				window.add(game);
			}
		}.start();
	}
}
