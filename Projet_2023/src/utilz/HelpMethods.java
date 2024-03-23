package utilz;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Egg;
import entities.Sausage;

import static utilz.Constants.EnemyConstants.EGG;
import static utilz.Constants.EnemyConstants.SAUSAGE;
import static utilz.Constants.SCORE;
import static utilz.Constants.ObjectConstants.*;

import main.Game;
import objects.Food;

public class HelpMethods {
	
	public static int GetScore() {
		return SCORE;
	}
	
	public static void SetScore(int score) {
		SCORE += score;
	}

	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {

		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x + width, y, lvlData))
					if (!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}

	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		if (x < 0 || x >= Game.GAME_WIDTH)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}
	
	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];

		if ((value >= 18 || value < 0) || (value != 3 && value != 4 && value != 5 && value != 7 ))
			return true;
		return false;
	}
	
	private static boolean IsSolidForEnemy(float x, float y, int[][] lvlData) {
		if (x < 0 || x >= Game.GAME_WIDTH)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolidForEnemy((int) xIndex, (int) yIndex, lvlData);
	}
	public static boolean CanMoveHereForEnemy(float x, float y, float width, float height, int[][] lvlData) {

		if (!IsSolidForEnemy(x, y, lvlData))
			if (!IsSolidForEnemy(x + width, y + height, lvlData))
				if (!IsSolidForEnemy(x + width, y, lvlData))
					if (!IsSolidForEnemy(x, y + height, lvlData))
						return true;
		return false;
	}
	
	public static boolean IsTileSolidForEnemy(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];

		if ((value >= 12 || value < 0) || (value != 3))
			return true;
		return false;
	}
	
	/*
	 * Ces méthodes permettent de regarder dans les coins de l'hitbox
	 */

	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			// Droite
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else
			// Gauche
			return currentTile * Game.TILES_SIZE;
	}

	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			return currentTile * Game.TILES_SIZE;

	}
	
	/*
	 * Ces méthodes permettent de savoir si les entités sont sur le sol, les échelles, le bas d'une échelle, ou le haut.
	 */

	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;

		return true;

	}

	public static boolean IsEntityOnLadder(float x, float y, int[][] lvlData) {

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		int value = lvlData[(int) yIndex][(int) xIndex];

		if(value == 4 || value == 5 || value == 7 )
			return true;
		return false;
	}
	
	public static boolean IsEntityOnTopLadder(float x, float y, int[][] lvlData) {

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		int value = lvlData[(int) yIndex][(int) xIndex];

		if(value == 7)
			return true;
		return false;
	}
	
	public static boolean IsEntityOnBottomLadder(float x, float y, int[][] lvlData) {

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = (y) / Game.TILES_SIZE;
		int value = lvlData[(int) yIndex][(int) xIndex];

		if(value == 6)
			return true;
		return false;
	}
	
	/*
	 * Ces méthodes permettent de définir le sol pour un ennemi ou pour un joueur
	 */
	
	
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if(xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}
	
	public static boolean IsFloorForEnemy(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if(xSpeed > 0)
			return IsSolidForEnemy(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolidForEnemy(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}
	
	/*
	 * Regarde si chaque tile du niveau est solid entre l'intervalle xStart et xEnd
	 */
	
	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		for(int i = 0; i < xStart - xEnd; i++) {
			if(IsTileSolid(xEnd + i, y, lvlData))
				return false;
			if(!IsTileSolid(xEnd + i, y + 1, lvlData))
				return false;
		
		}
		return true;
	}
	
	/*
	 * On regarde si la valeur entre la 1er tile et la 2nd est supérieur afin de faire une boucle
	 * permettant de parcourir les pixels pour voir s'il y a des 'obstacles' entre les deux hitboxs
	 * et inversement sinon
	 */
	public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);

	}
	
	/*
	 * Permet de regarder la valeur Bleue de la tuile est de retourner un ennemi si elle est égale à l'un des trois objets.
	 */
	public static ArrayList<Food> GetFoods(BufferedImage img) {
		ArrayList<Food> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == TOP_BREAD || value == STEACK || value == BOTTOM_BREAD)
					list.add(new Food(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}

		return list;
	}
	
	/*
	 * La méthode va parcourir l’image et regarder la composante rouge de chaque pixel.
	 *	Cette composante correspond à une tile de notre image.
     *  Par exemple, si la composante du premier pixel est égale à 1, alors on retournera l’image 1. Et ainsi de suite.
	 */
	
	public static int[][] GetLevelData(BufferedImage img){
		int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
		for(int j = 0; j< img.getHeight(); j++)
			for(int i = 0; i< img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if(value>= 12)
					value = 0;
				lvlData[j][i] = value;
			}
		return lvlData;

	}
	
	/*
	 * Permet de regarder la valeur verte de la tuile est de retourner un ennemi si elle est égale à la valeur de SAUSAGE
	 */
	public static ArrayList<Sausage> GetSausages(BufferedImage img){
		ArrayList<Sausage> list = new ArrayList<>();
		
		for(int j = 0; j< img.getHeight(); j++)
			for(int i = 0; i< img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if(value == SAUSAGE)
					list.add(new Sausage(i* Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;
	}
	
	/*
	 * Permet de regarder la valeur Bleue de la tuile est de retourner un ennemi si elle est égale à la valeur de EGG
	 */
	public static ArrayList<Egg> GetEggs(BufferedImage img){
		ArrayList<Egg> list = new ArrayList<>();
		
		for(int j = 0; j< img.getHeight(); j++)
			for(int i = 0; i< img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if(value == EGG)
					list.add(new Egg(i* Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;
	}
	
	/*
	 * Permet de regarder la valeur Verte de la tuile est de retourner le popint d'apparition du joueur si c'est égale à 100
	 */
	
	public static Point GetPlayerSpawn(BufferedImage img) {
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == 100)
					return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
			}
		return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
	}
	
	
}
