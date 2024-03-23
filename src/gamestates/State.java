/*
 *   Nom de la Classe: State
 *   
 *   Version: 1.0
 *  
 *   Copyright: STIZ Romain
 *   
 */
package gamestates;

import java.awt.event.MouseEvent;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;

public class State {

	protected Game game;

	public State(Game game) {

		this.game = game;
	}

	public boolean isIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
		
	}
	
	public Game getGame() {
		return game;
	}
	
	/*
	 * Permet de définir si on est dans le menu ou dans le jeu et que faire
	 */
	@SuppressWarnings("incomplete-switch")
	public void setGameState(Gamestate state) {
		switch(state) {
		case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MUSIQUE_GAME);
		case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
		}
		
		Gamestate.state = state;
		
	}

}
