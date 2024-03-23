/*
 *   Nom de la Classe: Level
 * 
 * 	 Description: La class Level sert à mettre
 * 	 en place les niveau de notre jeu
 * 
 *   Version: 1.0
 *   
 *   Date création: 21/02/2023
 *  
 *   Copyright: STIZ Romain
 *   
 */


package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Egg;
import entities.Sausage;
import main.Game;
import objects.Food;
import utilz.HelpMethods;

import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetPlayerSpawn;
import static utilz.HelpMethods.GetEggs;
import static utilz.HelpMethods.GetSausages;

public class Level {
	
	/*
	 * On récupèrera dans un tableau 2d les infos de notre niveau
	 */

	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Sausage> sausages;
	private ArrayList<Egg> eggs;
	
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;
	private ArrayList<Food> foods;

	public Level(BufferedImage img) {
		this.img = img;
		createLevelData();
		createEnemies();
		calculLevelOffsets();
		calculatePlayerSpawn();
		createFoods();
	}
	
	/*
	 * GETTER
	 */

	private void createFoods() {
		foods = HelpMethods.GetFoods(img);
		
	}

	private void calculatePlayerSpawn() {
		playerSpawn = GetPlayerSpawn(img);
		
	}

	private void calculLevelOffsets() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
		
	}

	private void createEnemies() {
		sausages = GetSausages(img);
		eggs = GetEggs(img);
	}

	private void createLevelData() {
		lvlData = GetLevelData(img);
		
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getlevelData(){
		return lvlData;
	}
	
	public int getLvlOffset() {
		return maxLvlOffsetX;
	}
	
	public ArrayList<Sausage> getSausages(){
		return sausages;
	}
	public ArrayList<Egg> getEggs(){
		return eggs;
	}
	
	public Point getPlayerSpawn() {
		return playerSpawn;
	}
	
	public ArrayList<Food> getFoods(){
		return foods;
	}
	

}