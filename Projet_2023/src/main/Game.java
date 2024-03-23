/*
 *   Nom de la Classe: Game
 * 
 * 	 Description: La class Game sert à mettre
 * 	 en place tout le jeu, en lancant un Thread
 *   afin de gérer le système d'IPS (FPS) et de
 *   UPS.
 *   
 *   Version: 1.0
 *   
 *   Date création: 20/02/2023
 *   
 *   Copyright: STIZ Romain
 *   
 */

package main;

import java.awt.Graphics;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.Player;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;



public class Game implements Runnable {

	
	/*
	 * 	ATTRIBUTS:
	 * 
	 *  GameWindow: 	afin d'avoir la fenêtre du jeu
	 *  GamePanel: 		afin d'avoir le conteneur de la fenêtre 
	 *  Thread: 		afin que le jeu soit lancé par un Thread
	 *                  et que les animations soit définis par UPS
	 *  FPS_SET:		le nombre de IPS(FPS) du jeu (utile selon le pc)    
	 *  UPS_SET:        le nombre d'animations par seconde
	 *                  (nb: dépandant des FPS au cas où le jeu tourne mal 
	 *                  en terme de FPS et que le personnage ralentit)
	 *  Playing & Menu: utile afin de gérer le jeu et les menus
	 * 
	 *  STATIC:
	 *  
	 *  Chaque attributs static servira au cours du code afin d'avoir des normes
	 *  entre chaque class. (e.g SCALE servira à définir par quoi on multiplie le jeu
	 *  afin d'avoir le rendu voulu)
	 * 
	 * 
	 */
	
	
	private ArrayList<Player> players = new ArrayList<>();
	
	
	@SuppressWarnings("unused")
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;
	
	private GameOptions options;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;


	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 38;
	public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	/*
	 * Constructeur auquel on crée un GamePanel et un GameWindow.
	 * On définit l'emplacement de la fenêtre et on démarre le jeu avec une boucle.
	 * 
	 */
	
	public Game(ArrayList<Player> p) {
		for(Player player: p) {
			this.players.add(player);
		}
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
		StartGameLoop();

	}

	/*
	 * Permet d'initialiser les Menus et le jeu.
	 * 
	 */

	private void initClasses() {
		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		options = new GameOptions(this);
		menu = new Menu(this);
		for(Player player: players)
			playing = new Playing(this, player.getPseudo());
		

	}
	
	/*
	 * Lancement du thread permettant l'affichage des FPS et UPS.
	 * 
	 */

	private void StartGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	/*
	 * Permettant de mettre à jour tout le temps et de savoir
	 * sur quelles actions on clique.
	 * 
	 */

	public void update() {

		switch(Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
			options.update();
			break;
		case QUIT:
		default:
			System.exit(0);
			break;

		}

	}
	
	/*
	 * Render nous affiches ce que le l'ordi faisait avec update()
	 * 
	 */

	public void render(Graphics g) {

		switch(Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		case OPTIONS:
			options.draw(g);
			break;
		default:
			break;

		}

	}
	
	/*
	 * Méthode permettant le calcul des FPS en regardant 
	 * si le nombre de FPS/UPS est supérieur à 1 càd qu'il
	 * faut réafficher les objets.
	 * 
	 */

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		long previousTime = System.nanoTime();
		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		double deltaU = 0;
		double deltaF = 0;
		while(true) {
			long currentTime = System.nanoTime();
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			if(deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			if(deltaF >= 1) {
				gamePanel.repaint();
				deltaF--;
				frames++;
			}
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}

	/*
	 * Si on quitte la fenêtre du jeu alors on enlève tout mouvements au joueur afin d'éviter
	 * qu'il continue de courrir dans une direction.
	 * 
	 */
	
	public void windowFocusLost() {
		if(Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}
	
	/*GETTER*/
	
	public Menu getMenu() {
		return menu;
	}
	
	public Playing getPlaying() {
		return playing;
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
	
	public AudioOptions getAudioOptions() {
		return audioOptions;
	}
	public GameOptions getGameOptions() {
		return options;
	}

}
