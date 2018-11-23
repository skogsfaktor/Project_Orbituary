package codex.orbit.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import org.lwjgl.openal.AL;

public abstract class AbstractMenu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public static final Font font = new Font("BAUHAUS 93", 0, 40);
	
	AbstractMenu() {
		setBorder(new MatteBorder(5, 5, 15, 15, Color.RED));
		setBackground(Color.BLACK);
		addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					exit();
			}
		});
		requestFocus();
	}
	
	protected void setFont() {
		for (Component com : getComponents()) {
			if (com instanceof JPanel)
				setFont(((JPanel)com).getComponents());
			if (com instanceof JButton) {
				if (!UIManager.getLookAndFeel().getName().equalsIgnoreCase("Nimbus"))
					((JButton)com).setBorder(new LineBorder(Color.WHITE, 2));
			}
			com.setBackground(Color.BLACK);
			com.setForeground(Color.RED);
			com.setFont(font);
		}
	}
	protected void setFont(Component... coms) {
		for (Component com : coms) {
			com.setBackground(Color.BLACK);
			com.setForeground(Color.RED);
			com.setFont(font);
		}
	}
	
	public abstract void actionPerformed(ActionEvent e);
	
	protected void exit() {
		Toolkit.getDefaultToolkit().beep();
		int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Quit program and return to desktop?", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			if (AL.isCreated())
				AL.destroy();
			System.exit(0);
		}
	}
}
