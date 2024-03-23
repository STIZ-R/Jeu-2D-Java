/*
 *   Nom de la Classe: LevelManager
 * 
 * 	 Description: La class LevelManager sert à créer nos niveau (enfin plutôt de les lire)
 * 
 *   Version: 1.0
 *   
 *   Date création: 21/02/2023
 *  
 *   Copyright: STIZ Romain
 *   
 */

package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Gamestate;
import main.Game;
import utilz.LoadSave;
import static utilz.HelpMethods.SetScore;

public class LevelManager {
	
	/*
	 * Game:			afin de le mettre dans notre jeu
	 * BufferedImage:	afin de récupérer nos sprites de niveau
	 * Level:			contient notre premier niveau
	 * 
	 */

	private Game game;
	private BufferedImage[] levelSprite;
	private ArrayList<Level> levels;
	private int lvlIndex = 0;
	
	/*
	 * Game aura attribué le niveau levelOne
	 */

	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		levels = new ArrayList<>();
		buildAllLevels();
	}
	
	/*
	 * Si on complète tout les niveaux, on reprend du début
	 */
	
	public void loadNextlevel() {
		lvlIndex ++;
		if(lvlIndex >= levels.size()) {
			SetScore(0);
			lvlIndex = 0;
			Gamestate.state = Gamestate.MENU;
		}
		
		Level newLevel = levels.get(lvlIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLvlData(newLevel.getlevelData());
		game.getPlaying().setMaxLevelOffset(newLevel.getLvlOffset());
		game.getPlaying().getObjectManager().loadObjects(newLevel);
	}
	
	
	

	private void buildAllLevels() {
		BufferedImage[] allLevels = LoadSave.GetAllsLevels();
		for(BufferedImage img : allLevels)
			levels.add(new Level(img));
		
	}
	/*
	 * permet l'importation dans le tableau de tout nos tiles de niveau
	 */
	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[12];
		for (int j = 0; j < 3; j++)
			for (int i = 0; i < 4; i++) {
				int index = j * 4 + i;
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
			}
	}
	
	/*
	 * draw() permet de nous afficher à chaque pixel de levelOne
	 */

	public void draw(Graphics g) {
		for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
			for (int i = 0; i < levels.get(lvlIndex).getlevelData()[0].length; i++) {
				int index = levels.get(lvlIndex).getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], Game.TILES_SIZE * i, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
	}
	
	
	
	
	/*
	 * GETTER
	 */
	public Level getCurrentLevel() {
		return levels.get(lvlIndex);
	}

	public int getAmountOfLevels() {
		return levels.size();
	}
	
	public int getLevelIndex() {
		return lvlIndex;
	}
}