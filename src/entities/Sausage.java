/*
 *   Nom de la Classe: Sausage
 * 
 * 	 Description: La class Sausage permet la création d'ennemis dans le jeu
 *   
 *   Version: 1.0
 *  
 *   Copyright: STIZ Romain
 *   
 */

package entities;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;

import java.awt.geom.Rectangle2D;

import main.Game;

@SuppressWarnings("serial")
public class Sausage extends Enemy {
	/*
	 * 	ATTRIBUTS:
	 * 
	 *  attackBoxOffsetX: 						permet de définir un offset sur la hitbox de l'attaque du personnage.
	 * 
	 */
	private int attackBoxOffsetX;
	
	/*
	 * Constructeur de la classe
	 */
	public Sausage(float x, float y) {
		super(x, y, SAUSAGE_WIDTH, SAUSAGE_HEIGHT, SAUSAGE);
		initHitbox(22,22);
		initAttackBox();
	}
	
	/*
	 * Initialise la hitbox de l'attaque
	 */
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int)(60 * Game.SCALE), (int)(29 * Game.SCALE));
		attackBoxOffsetX = (int)(Game.SCALE * 20);
	}

	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
		
	}
	
	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
		
	}
	
	/*
	 * Cette grosse méthode permet de mettre à jour le comportement de l'ennemi selon les critères.
	 * 
	 * firstUpdate: regarde si l'ennemi est en l'air est le fait retombé.
	 * 
	 * Si l'ennemi est immobile (IDLE) alors on le fait courir (RUNNING)
	 * S'il court et s'il voit le joueur -> se tourne vers lui.
	 * S'il est à porter d'attaque -> attaque
	 * Il marche toujours de Droite à gauche sinon.
	 * 
	 * S'il attaque et que son animation est à celle où il tend les bras -> fait des dégats à la cible.
	 */

	private void updateBehavior(int[][] lvlData, Player player) {
		if (firstUpdate) 
			firstUpdateCheck(lvlData);
		

		if (inAir) 
			updateInAir(lvlData);
		 else {
			switch (state) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:	
				
				if(canSeePlayer(lvlData, player)) {
					turnTowardsPlayer(player);
					if(isPlayerCloseForAttack(player))
						newState(ATTACK);
				}
				move(lvlData);
				break;
			case ATTACK:
				if(aniIndex == 0)
					attackChecked = false;
				if(aniIndex == 3 && !attackChecked)
					checkEnemyHit(attackBox, player);
				break;
			case HIT:
				break;
			}
		}
				
			
	}
	
	

	/*
	 * Permet de faire une translation de l'image afin de pouvoir avoir l'ennemi qui nous fixe peu importe le côté.
	 */
	
	public int flipX() {
		if(walkDir == RIGHT)
			return width;
		else return 0;
	}
	public int flipW() {
		if(walkDir == RIGHT)
			return -1;
		else return 1;
		
	}
	



}
