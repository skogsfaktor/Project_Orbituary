package codex.orbit.menu;

public enum Menus {
	MAIN(new MainMenu(), "Main Menu"), GAME(new GameMenu(), "Game Menu"), OPTIONS(new OptionsMenu(), "Options Menu");
	
	final AbstractMenu menu;
	final String title;
	
	Menus(AbstractMenu m, String t) {
		menu = m;
		title = t;
	}
}
