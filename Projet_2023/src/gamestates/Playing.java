/*
 *   Nom de la Classe: Playing
 * 
 * 	 Description: La class Playing permet la création des interactons avec le jeu.
 *   
 *   Version: 1.0
 *  
 *   Copyright: STIZ Romain
 *   
 */

package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.HelpMethods.GetScore;

public class Playing extends State implements Statemethods{


	private Player player;
	private boolean doIt = false;
	private ScoreFile sc = new ScoreFile();
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private boolean paused = false;
	private int xLvlOffset;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private boolean gameOver = false;
	private boolean lvlCompleted = false;
	private boolean playerDying = false;
	@SuppressWarnings("unused")
	private int maxLvlOffset;
	
	
	private BufferedImage backgroundImg;

	public Playing(Game game, String pseudo) {
		super(game);
		initClasses();
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMAGE);
		calculLvlOffset();
		loadStartLevel();
	}

	/*
	 * Permet de charger le niveau suivant
	 */
	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextlevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
	}
	
	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
		
	}

	private void calculLvlOffset() {
		maxLvlOffset = levelManager.getCurrentLevel().getLvlOffset();	
	}

	private void initClasses() {

		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);
		player = new Player(167, 0, (int)(64*Game.SCALE), (int) (40 *Game.SCALE), this, JOptionPane.showInputDialog("Nom du joueur?","Player 1"));
		
		player.loadLvlData(levelManager.getCurrentLevel().getlevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
	}

	/*
	 * Si le jeu est en pause alors on n'affiche plus rien de nouveau en dehors du menu
	 */
	
	@Override
	public void update() {
		
		if(paused) {
			pauseOverlay.update();
		}else if(lvlCompleted) {
			levelCompletedOverlay.update();
		}else if(gameOver) {
			//bug musique sans cela
		}else if(playerDying) {
			player.update();
		}
		else if(!gameOver){
			//levelManager.update();
			objectManager.update();
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getlevelData(), player);
		}	
	}
	
	/*
	 * si le jeu est en pause alors on affiche le menu pause sinon non.
	 */

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		levelManager.draw(g);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		objectManager.draw(g,  xLvlOffset);
		
		if(paused)
			pauseOverlay.draw(g);
		else if(gameOver) {
			if( doIt == false) {
				sc.writeScoreToFile(player.getPseudo(), GetScore());
				doIt = true;
			}
			gameOverOverlay.draw(g, sc);
		}
		else if (lvlCompleted)
			levelCompletedOverlay.draw(g);

	}

	@Override
	public void mousClicked(MouseEvent e) {
		if(!gameOver)
			if(e.getButton() == MouseEvent.BUTTON1) 
				player.setAttack(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mousePressed(e);
			else if(lvlCompleted)
				levelCompletedOverlay.mousePressed(e);	
		}

			

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseReleased(e);
			else if(lvlCompleted)
				levelCompletedOverlay.mouseReleased(e);
		}
			

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseMoved(e);
			else if(lvlCompleted)
				levelCompletedOverlay.mouseMoved(e);
		}
			

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gameOver)
			gameOverOverlay.keyPressed(e);
		else
			switch (e.getKeyCode()) {
			case KeyEvent.VK_Z:
				player.setMount(true);
				break;
			case KeyEvent.VK_Q:
				player.setLeft(true);
				break;
			case KeyEvent.VK_S:
				player.setDemount(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;
			}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!gameOver)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_Z:
				player.setMount(false);
				break;
			case KeyEvent.VK_Q:
				player.setLeft(false);
				break;
			case KeyEvent.VK_S:
				player.setDemount(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			}

	}
	
	public void resetAll() {
		sc.writeScoreToFile(player.getPseudo(), GetScore());
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}
	
	public void checkFoodTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}
	
	public void checkFoodFalling() {
		objectManager.checkFallFood();
	}
	
	public void checkFoodDamage() {
		objectManager.checkFoodDamage(enemyManager);
	}
	
	
	
	
	/*
	 * Permet d'enlever le mode pause du jeu
	 */
	
	public void unpauseGame() {
		paused = false;
	}

	public void windowFocusLost() {

		player.resetDirBooleans();

	}

	public Player getPlayer() {
		return player;
	}
	
	public LevelManager getLevelManager() {
		return levelManager;
	}
	
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}
	
	public void setMaxLevelOffset(int lvlOffset) {
		this.maxLvlOffset = lvlOffset;
	}
	
	public void setLevelCompleted(boolean level) {
		this.lvlCompleted = level;
		if(lvlCompleted)
			game.getAudioPlayer().lvlCompleted();
	}

	public void setPlayerDying(boolean b) {
		this.playerDying = b;
		
	}

	




}
