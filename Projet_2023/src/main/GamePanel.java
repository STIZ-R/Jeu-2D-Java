package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyBoardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private Game game;

	public GamePanel(Game game) {

		mouseInputs = new MouseInputs(this);
		this.game = game;
		setpanelSize();
		addKeyListener(new KeyBoardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}


	private void setpanelSize() {

		Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
		setPreferredSize(size);

	}


	public void updateGame() {


	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}
}
