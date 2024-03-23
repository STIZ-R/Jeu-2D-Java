/*
 *   Nom de la Classe: Constants
 * 
 * 	 Description: La class Constants sert à répertoirier toutes les constantes que
 * 				  nous utiliserons au sein du projet.
 * 
 *   Version: 1.0
 *   
 *   Date création: 22/02/2023
 *  
 *   Copyright: STIZ Romain
 *   
 */



package utilz;

import main.Game;

public class Constants {
	
	
	public static final float GRAVITY = 0.04f * Game.SCALE;
	public static final float ANI_SPEED = 25;
	
	public static int SCORE = 0;
	
	
	public static class ObjectConstants {

		public static final int TOP_BREAD = 0;
		public static final int STEACK = 1;
		public static final int BOTTOM_BREAD = 2;

		public static final int FOOD_WIDTH_DEFAULT = 62;
		public static final int FOOD_HEIGHT_DEFAULT = 32;
		public static final int FOOD_WIDTH = (int) (Game.SCALE * FOOD_WIDTH_DEFAULT);
		public static final int FOOD_HEIGHT = (int) (Game.SCALE * FOOD_HEIGHT_DEFAULT);

		public static int GetSpriteAmount(int objectType) {
			switch(objectType) {
			case TOP_BREAD, STEACK, BOTTOM_BREAD:
				return 1;
			}
			return 1;
		}
	}
	
	public static class EnemyConstants{
		
		//EGG
		public static final int EGG = 1;
		
		public static final int EGG_WIDTH_DEFAULT = 72;
		public static final int EGG_HEIGHT_DEFAULT = 32;
		public static final int EGG_WIDTH = (int)(EGG_WIDTH_DEFAULT * Game.SCALE);
		public static final int EGG_HEIGHT = (int)(EGG_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int EGG_DRAWOFFSET_X = (int) (26*Game.SCALE);
		public static final int EGG_DRAWOFFSET_Y = (int) (2*Game.SCALE);
		
		
		//SAUSAGE
		public static final int SAUSAGE = 0;
		
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;
		
		public static final int SAUSAGE_WIDTH_DEFAULT = 72;
		public static final int SAUSAGE_HEIGHT_DEFAULT = 32;
		public static final int SAUSAGE_WIDTH = (int)(SAUSAGE_WIDTH_DEFAULT * Game.SCALE);
		public static final int SAUSAGE_HEIGHT = (int)(SAUSAGE_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int SAUSAGE_DRAWOFFSET_X = (int) (26*Game.SCALE);
		public static final int SAUSAGE_DRAWOFFSET_Y = (int) (6*Game.SCALE);
		
		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			switch(enemy_type) {
			case EGG:
				switch (enemy_type) {
				case IDLE:
					return 9;
				case RUNNING:
					return 6;
				case ATTACK:
					return 7;
				case HIT:
					return 4;
				case DEAD:
					return 5;
				}
			case SAUSAGE:
				switch (enemy_state) {
				case IDLE:
					return 9;
				case RUNNING:
					return 6;
				case ATTACK:
					return 7;
				case HIT:
					return 4;
				case DEAD:
					return 5;
				}
			}
			return 0;
		}
		
		public static int GetMaxHealth(int enemy_type) {
			switch(enemy_type) {
			case SAUSAGE:
			case EGG:
				return 10;
			default:
				return 1;
			}
			
		}
		
		public static int GetEnemyDamage(int enemy_type) {
			switch(enemy_type) {
			case SAUSAGE:
			case EGG:
				return 34;
			default:
				return 0;
			}
		}
	}
	
	/*
	 * Class UI permettant d'utiliser des constantes dans l'interface utilisateur 
	 */

	public static class UI{
		
		/*
		 * La class Buttons est utilise pour avoir une taille des boutons généralisées par défaut et actuelle
		 */
		
		public static class Buttons{
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE);
		}
		
		/*
		 * La class PauseButtons est utilisé pour généralisé les valeurs des boutons dans le menu Pause
		 */
		
		public static class PauseButtons{
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE= (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
			
		}
		
		public static class URMButtons{
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
		}
	}
	
	/*
	 * Attribut une valeur à un mouvement
	 */
	
	
	public static class Directions{

		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
	/*
	 * Réprtorie dans une class les différentes valeurs du personnage (courrir, monter, tomber,...)
	 */

	public static class PlayerConstants{

		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int MOUNT = 2;
		public static final int ATTACK = 3;
		public static final int HIT = 4;
		public static final int DEAD = 5;

		/*
		 * Permet de retourner la valeur de l'action voulu
		 */
		
		public static int GetSpriteAmount(int player_action) {

			switch(player_action) {

			case IDLE:
				return 5;
			case RUNNING:
				return 6;
			case MOUNT:
				return 3;
			case HIT:
				return 2;
			case ATTACK:
				return 3;
			case DEAD:
				return 3;
			default:
				return 1;

			}

		}
	}
}
