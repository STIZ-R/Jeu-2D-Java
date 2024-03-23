package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

public class PauseOverlay {
	
	private Playing playing;
	private BufferedImage backgroundImg;
	private int bgX, bgY, bgW, bgH;
	private AudioOptions audioOptions;
	private UrmButton menuButton, replayButton, unpauseButton;
	

	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadBackground();
		audioOptions = playing.getGame().getAudioOptions();
		createUrmButtons();
		
	}
	/*
	 * On souhaite affecter un x à chaque bouton
	 * 
	 */
	
	private void createUrmButtons() {
		int menuX = (int) (313 * Game.SCALE);
		int replayX = (int) (387 * Game.SCALE);
		int unpauseX = (int) (462 * Game.SCALE);
		int bY = (int) (325 * Game.SCALE);
		
		unpauseButton = new UrmButton(unpauseX, bY,URM_SIZE, URM_SIZE, 0);
		replayButton = new UrmButton(replayX, bY,URM_SIZE, URM_SIZE, 1);
		menuButton = new UrmButton(menuX, bY,URM_SIZE, URM_SIZE, 2);
	}


	
	/*
	 * Permet de charger l'image du Menu
	 */

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
		bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (50 * Game.SCALE);
		
	}

	public void update() {
		
		//Boutons URM
		menuButton.update();
		replayButton.update();
		unpauseButton.update();
		audioOptions.update();

	}
	
	public void draw(Graphics g) {
		//Background
		g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
		
		
		//Boutons URM
		menuButton.draw(g);
		replayButton.draw(g);
		unpauseButton.draw(g);
		
		audioOptions.draw(g);
		
	}
	
	public void mouseDragged(MouseEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuButton))
			menuButton.setMousePressed(true);
		else if (isIn(e, replayButton))
			replayButton.setMousePressed(true);
		else if (isIn(e, unpauseButton))
			unpauseButton.setMousePressed(true);
		else
			audioOptions.mousePressed(e);
	}
	
	/*
	 * Méthode permettant de pouvoir changer la fonction émise lors du clique.
	 * On regarde si on est sur le bouton et si on clique dessus, ensuite, on fait la fonction voulu.
	 * Enfin, on reset le statut des boutons afin de pouvoir réappuyer dessus!
	 */

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuButton)) {
			if (menuButton.getMousePressed()) {
				// old
				// Gamestate.state = Gamestate.MENU;
				// new
				playing.setGameState(Gamestate.MENU);
				playing.unpauseGame();
			}
		} else if (isIn(e, replayButton)) {
			if (replayButton.getMousePressed()) {
				playing.resetAll();
				playing.unpauseGame();
			}
		} else if (isIn(e, unpauseButton)) {
			if (unpauseButton.getMousePressed())
				playing.unpauseGame();
		} else
			audioOptions.mouseReleased(e);

		menuButton.resetBools();
		replayButton.resetBools();
		unpauseButton.resetBools();

	}
	
	/*
	 * on regarde où se situe la souris et si on est sur une case, alors
	 * on appelle setMosueOver afin de changer le 'sprite' du bouton
	 */

	public void mouseMoved(MouseEvent e) {
		menuButton.setMouseOver(false);
		replayButton.setMouseOver(false);
		unpauseButton.setMouseOver(false);

		if (isIn(e, menuButton))
			menuButton.setMouseOver(true);
		else if (isIn(e, replayButton))
			replayButton.setMouseOver(true);
		else if (isIn(e, unpauseButton))
			unpauseButton.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);
	}
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
		
	}


}
