/*
 *   Nom de la Classe: KeyBoardInputs
 * 
 * 	 Description: La class KeyBoardInputs permet la msie en place de l'utilisation du clavier
 *   
 *   Version: 1.0
 *  
 *   Copyright: STIZ Romain
 *   
 */

package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.GamePanel;

public class KeyBoardInputs implements KeyListener {

	private GamePanel gamePanel;

	public KeyBoardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		switch(Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyReleased(e);;
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyReleased(e);
			break;
		default:
			break;
		
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);;
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().keyPressed(e);
			break;
		default:
			break;
		
		}
		
	}
}
