package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.ScoreFile;
import main.Game;

public class GameOverOverlay {
	
	private Playing playing;
	
	public GameOverOverlay(Playing playing) {
		this.playing = playing;
	}
	
	public void draw(Graphics g, ScoreFile sc) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.setColor(Color.white);
		g.drawString("Classement : " , Game.GAME_WIDTH / 3, 320);
		g.drawString(sc.getTopFivePlayersScore().get(0).toString() , Game.GAME_WIDTH / 2, 350);
		g.drawString(sc.getTopFivePlayersScore().get(1).toString() , Game.GAME_WIDTH / 2, 370);
		g.drawString(sc.getTopFivePlayersScore().get(2).toString() , Game.GAME_WIDTH / 2, 390);
		g.drawString(sc.getTopFivePlayersScore().get(3).toString() , Game.GAME_WIDTH / 2, 410);
		g.drawString(sc.getTopFivePlayersScore().get(4).toString() , Game.GAME_WIDTH / 2, 430);
		g.drawString(sc.getTopFivePlayersName().get(0) , Game.GAME_WIDTH / 3, 350);
		g.drawString(sc.getTopFivePlayersName().get(1) , Game.GAME_WIDTH / 3, 370);
		g.drawString(sc.getTopFivePlayersName().get(2) , Game.GAME_WIDTH / 3, 390);
		g.drawString(sc.getTopFivePlayersName().get(3) , Game.GAME_WIDTH / 3, 410);
		g.drawString(sc.getTopFivePlayersName().get(4) , Game.GAME_WIDTH / 3, 430);
		g.drawString("FIN", Game.GAME_WIDTH / 3, 150);
		g.drawString("Appuis sur 'esc' pour retourner au Menu!", Game.GAME_WIDTH / 3, 300);

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playing.resetAll();
			playing.setGameState(Gamestate.MENU);
		}
	}
}
