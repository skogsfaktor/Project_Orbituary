package codex.orbit.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import codex.orbit.Main;

public class GameMenu extends AbstractMenu {
	private static final long serialVersionUID = 1L;
	
	private JButton newGame = new JButton("Start New Game");
	private JSlider nrOfPlayers = new JSlider(2, 8, 2);
	private JSlider nrOfBots = new JSlider(0, 2, 0);
	
	private ButtonGroup gamemodes = new ButtonGroup();
	private JRadioButton deathmatch = new JRadioButton("Deathmatch", true);
	private JRadioButton teamDeathmatch = new JRadioButton("Team Deathmatch");
	
	GameMenu() {
		setLayout(new BorderLayout());
		
		JPanel sliderPanel = new JPanel(new GridLayout(2, 1));
		{
			nrOfPlayers.setPaintTicks(true);
			nrOfPlayers.setPaintLabels(true);
			nrOfPlayers.setSnapToTicks(true);
			nrOfPlayers.setMajorTickSpacing(1);
			nrOfPlayers.setBorder(new TitledBorder("Nr of players"));
			((TitledBorder)nrOfPlayers.getBorder()).setTitleColor(Color.WHITE);
			((TitledBorder)nrOfPlayers.getBorder()).setTitleFont(font);
			nrOfPlayers.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					nrOfBots.setMaximum(nrOfPlayers.getValue());
				}
			});
			sliderPanel.add(nrOfPlayers);
			
			nrOfBots.setPaintTicks(true);
			nrOfBots.setPaintLabels(true);
			nrOfBots.setSnapToTicks(true);
			nrOfBots.setMajorTickSpacing(1);
			nrOfBots.setBorder(new TitledBorder("Nr of bots"));
			((TitledBorder)nrOfBots.getBorder()).setTitleColor(Color.WHITE);
			((TitledBorder)nrOfBots.getBorder()).setTitleFont(font);
			sliderPanel.add(nrOfBots);
		}
		add(sliderPanel);
		
		JPanel gamemodesPanel = new JPanel();
		gamemodesPanel.setBorder(new TitledBorder("Gamemode"));
		((TitledBorder)gamemodesPanel.getBorder()).setTitleColor(Color.WHITE);
		((TitledBorder)gamemodesPanel.getBorder()).setTitleFont(font);
		{
			gamemodes.add(deathmatch);
			gamemodesPanel.add(deathmatch);
			
			gamemodes.add(teamDeathmatch);
			gamemodesPanel.add(teamDeathmatch);
		}
		add(BorderLayout.NORTH, gamemodesPanel);
		
		newGame.addActionListener(this);
		add(BorderLayout.SOUTH, newGame);
		
		setFont();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == newGame) {
			Main.startGame(nrOfPlayers.getValue(), nrOfBots.getValue());
		}
	}
}
