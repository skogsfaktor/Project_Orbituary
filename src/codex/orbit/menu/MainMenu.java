package codex.orbit.menu;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import codex.orbit.Main;

public class MainMenu extends AbstractMenu {
	private static final long serialVersionUID = 1L;
	
	private final JButton credits = new JButton("Credits");
	private final JButton exit = new JButton("Exit");
	
	MainMenu() {
		credits.addActionListener(this);
		add(credits);
		
		if (!Main.isApplet()) {
			exit.addActionListener(this);
			add(exit);
		}
		
		setFont();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == credits) {
			String creditsTxt = "Based on the Mac game \"Orbituary\" made by Hugh Rayner \n" +
								"\n" +
								"Design: Christian Callergård, Sadok Habibi Dalin, Jonathan Cohlin \n" +
								"Code: Jonathan Cohlin \n" +
								"Physics: Christian Callergård \n" +
								"Music: Sadok Habibi Dalin \n" +
								"Graphics: Jonathan Cohlin \n" +
								"Website: Matti Lundberg, Simon Magnusson Sundberger, Markus Häregård \n" +
								"\n" +
								"Code written in Java \n" +
								"Libraries used: \n" +
								"LWJGL (www.lwjgl.org) \n" +
								"Slick (www.slick2d.org) \n" +
								"\n" +
								"Programs used: \n" +
								"Eclipse IDE (www.eclipse.org) \n" +
								"Reason 5 (www.propellerheads.se/products/reason) \n" +
								"Paint.NET (www.getpaint.net) \n";
			JOptionPane.showMessageDialog(this, creditsTxt, "The minds behind \"Project Orbituary\"", JOptionPane.PLAIN_MESSAGE);
		}
		else if (src == exit) {
			exit();
		}
	}
}
