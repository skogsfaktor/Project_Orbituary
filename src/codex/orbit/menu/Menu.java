package codex.orbit.menu;

import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;

public class Menu extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	public Menu() {
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setBackground(Color.DARK_GRAY);
		setForeground(Color.RED);
		setFont(AbstractMenu.font);
		for (Menus m : Menus.values()) {
			addTab(m.title, m.menu);
		}
	}
}

