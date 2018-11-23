package codex.orbit.menu;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

public class OptionsMenu extends AbstractMenu {
	private static final long serialVersionUID = 1L;
	
	private JButton changeRes = new JButton("Change Resolution");
	
	OptionsMenu() {
		changeRes.addActionListener(this);
		add(changeRes);
		
		setFont();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == changeRes) {
			System.out.println("change res");
		}
	}
}
