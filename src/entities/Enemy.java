/*
 *   Nom de la Classe: Enemy
 * 
 * 	 Description: La class Enemy permet de construire les ennemis.
 *   
 *   Version: 1.0
 *  
 *   Copyright: STIZ Romain
 *   
 */
package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;

import main.Game;

@SuppressWarnings("serial")
public abstract class Enemy extends Entity {

	/*
	 * 	ATTRIBUTS:
	 * 
	 *  enemyType: 							afin de connaître le type d'ennemi
	 *  firstUpdate:						savoir la position de l'ennemi dans le jeu au début
	 *  walkSpeed:							permet de définir la vitesse de marche de l'ennemi  
	 *  mountSpeed,demountSpeed:  			permet de définir la vitesse de monter/descente de l'ennemi 
	 *  mountDir: 							décris la direction de l'ennemi
	 *  attackDistance:						la distance d'attaque de l'ennemi
	 * 
	 */
	
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected float walkSpeed;
	@SuppressWarnings("unused")
	private float mountSpeed = -0.9f * Game.SCALE;
	private float demountSpeed = 0.9f * Game.SCALE;
	protected int walkDir = LEFT;
	protected int mountDir;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean attackChecked;

	/*
	 * Constructeur de la class Enemy
	 */
	
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
		walkSpeed = Game.SCALE * 0.9f;
	}
	
	/*
	 * Regarde si l'ennemi apparaît dans les airs et le fait descendre
	 */

	protected void firstUpdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	/*
	 * On regarde quand on touche le sol
	 */
	protected void updateInAir(int lvlData[][]) {
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
		}
	}
	
	/*
	 * Permet de faire bouger l'ennemi de droite à gauche et de changer de directions
	 * quand il touche un rebord du niveau.
	 */

	protected void move(int[][] lvlData) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloorForEnemy(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		changeWalkDir();
	}

	
	/*
	 * Permet de se tourner vers le joueur suivant s'il est à sa droite ou sa gauche.
	 */
	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}
	
	/*
	 * Regarde si l'ennemi est à porter d'attaque du joueur selon sa distance 
	 */

	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance;
	}
	
	/*
	 * Si l'ennemi est sur le même ordonné que le joueur et qu'il est à porter d'attaque et qu'il n'y a rien que le gêne entre lui et le joueur,
	 * retourne true sinon false.
	 */

	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
		if (playerTileY == tileY)
			if (isPlayerInRange(player)) {
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}
		return false;
	}
	
	/*
	 * Regarde si le joueur est dans le ligne de vision de l'ennemi.
	 */

	private boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}

	/*
	 * Méthode permettant de remettre à 0 l'animation quand on l'a change afin de ne
	 * pas commencer en fin d'animation
	 */
	protected void newState(int enemyState) {
		this.state = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	/*
	 * Permet de faire des dégâts à l'ennemi et s'il meurt alors on gagne du score et on le fait disparaître
	 */

	public void hurt(int amount) {
		currentHealth -= amount;
		if (currentHealth <= 0) {
			newState(DEAD);
			SetScore(50);
		} else
			newState(HIT);
	}
	
	/*
	 * On regarde si l'ennemi a toucher un joueur avec son attaqueBox
	 */

	protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.hitbox))
			player.changeHealth(-GetEnemyDamage(enemyType));
		attackChecked = true;

	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, state)) {
				aniIndex = 0;
				switch (state) {
				case ATTACK, HIT -> state = IDLE;
				case DEAD -> active = false;
				}
			}
		}
	}

	protected void changeWalkDir() {
		if (walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;

	}
	
	

	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		airSpeed = 0;
	}

	public boolean getActive() {
		return active;
	}

	protected void changeMountDir() {
		if (mountDir == UP)
			mountDir = DOWN;
		else
			mountDir = UP;

	}
	
	/*				Cette méthode contient des bouts de code qui sont entre guillemets, mais j'ai décidé de les garder car ils sont tout de même intéressants
	 * 
	 * 				le if(true) est un vestige de mon ancienne méthode, ne pas faire attention
	 * 
	 * 				On fait bouger l'ennemi,
	 * 				
	 * 				Si l'ennemi est en dessous du joueur, qu'il est sur une échelle mais pas au dessus, on le fait grimper celle-ci.
	 * 					Sinon, si l'ennemi est à droite du joueur, il partira sur la droite /inversement
	 * 				Sinon (c'est qu'il est au dessus)
	 * 					S'il est en haut de l'échelle, pour éviter qu'il tombe avec la gravité, on le fait sauter l'échelle
	 * 					S'il n'est pas en haut de l'échelle mais pas non plus en bas, alors il descends
	 * 					S'il est en bas alors part à droite ou à gauche selon la position du joueur.
	 * 
	 */

	protected void moveIntoPlayer(int[][] lvlData, Player player) {

		inAir = false;
		if (/*(int) (hitbox.y - 5) != (int) (player.getHitbox().y)*/true) {
			move(lvlData);
			if (player.getHitbox().y < hitbox.y) {
				if (IsEntityOnLadder(hitbox.x, hitbox.y, lvlData))
					if (!IsEntityOnTopLadder(hitbox.x, hitbox.y, lvlData)) {
						if (CanMoveHere(hitbox.x, hitbox.y - 1, hitbox.width, hitbox.height, lvlData))
							hitbox.y -= 1;
					} else {
						if (player.getHitbox().x < hitbox.x) {
							hitbox.x -= 40;
							inAir = true;
							return;
						}
						else {
							hitbox.x += 40;
							inAir = true;
							return;
						}
					}

			} else {
				if (IsEntityOnTopLadder(hitbox.x, hitbox.y, lvlData)) {
					if (CanMoveHere(hitbox.x - 10, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
						hitbox.x -= 10;
					}
					if (CanMoveHere(hitbox.x + 10, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
						hitbox.x += 10;
					}

				}
				if (IsEntityOnLadder(hitbox.x, hitbox.y, lvlData))
					if (!IsEntityOnBottomLadder(hitbox.x, hitbox.y + 30, lvlData)) {
						if (CanMoveHere(hitbox.x, hitbox.y + 1, hitbox.width, hitbox.height, lvlData)) {
							hitbox.y += demountSpeed;
						}
					} else {
						if (player.getHitbox().x < hitbox.x) {
							inAir = true;
							hitbox.x -= 40;
							return;
						} else{
							inAir = true;
							hitbox.x += 40;
							return;
						}
					}
			}

		} //else {
//			inAir = true;
//			if (canSeePlayer(lvlData, player)) {
//
//				turnTowardsPlayer(player);
//				if (walkDir == LEFT)
//					hitbox.x -= 1;
//				else
//					hitbox.x += 1;
//			} else {
//				move(lvlData);
//				if (IsEntityOnLadder(hitbox.x, hitbox.y, lvlData))
//					if (IsEntityOnBottomLadder(hitbox.x, hitbox.y + 50, lvlData)) {
//						if (player.getHitbox().x < hitbox.x) {
//							inAir = true;
//							hitbox.x -= 19;
//						} else if (player.getHitbox().x > hitbox.x) {
//							inAir = true;
//							hitbox.x += 19;
//						}
//					}
//			}
//		}
	}

		/*
		 * Autres possibilités envisagées qui ne fonctionne pas trop bien aussi
		 */
		
		
		
		
		/*
		 * if(CanMoveHere(hitbox.x + Game.SCALE * 0.9f, hitbox.y, hitbox.width,
		 * hitbox.height, lvlData) && hitbox.x < player.getHitbox().x &&
		 * ((player.getHitbox().y +10) <(hitbox.y) || player.getHitbox().y + 10>
		 * hitbox.y)) { if (IsFloorForEnemy(hitbox, Game.SCALE * 0.9f, lvlData)) {
		 * //System.out.print("player : " + player.getHitbox().y + " egg : " +
		 * hitbox.y); inAir = true; turnTowardsPlayer(player); hitbox.x += Game.SCALE *
		 * 0.9f; }
		 * 
		 * }else if(IsEntityOnLadder(hitbox.x, hitbox.y, lvlData)) hitbox.y +=
		 * mountSpeed; else if(CanMoveHere(hitbox.x - Game.SCALE * 0.9f, hitbox.y,
		 * hitbox.width, hitbox.height, lvlData)) { //turnTowardsPlayer(player);
		 * hitbox.x -= Game.SCALE * 0.9f; }
		 */
//		float xSpeed = 0;
//		if (walkDir == LEFT)
//			xSpeed = -walkSpeed;
//		else
//			xSpeed = walkSpeed;
//
//		float ySpeed = 0;
//		if (hitbox.y <= player.getHitbox().y)
//			ySpeed = demountSpeed;
//		else
//			ySpeed = mountSpeed;
//
//		inAir = false;
//		if(canSeePlayer(lvlData, player)) {
//			//turnTowardsPlayer(player);
//			if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
//				//if (IsFloorForEnemy(hitbox, xSpeed, lvlData)) {
//					//System.out.print("a ");
//					hitbox.x -= xSpeed;
//					return;
//				//}
//			}
//				
//		}else {
//			if(IsEntityOnLadder(hitbox.x, hitbox.y, lvlData)) {
//				if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData))
//					//if (IsEntityOnTopLadder(hitbox.x, hitbox.y, lvlData)) {
//					//if (IsFloorForEnemy(hitbox, xSpeed, lvlData))  {
//						//System.out.print("b ");
//						inAir=false;
//						hitbox.y += ySpeed;
//						return;
//					/*}else {
//						if (CanMoveHere(hitbox.x  + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
//							//if (IsFloorForEnemy(hitbox, xSpeed, lvlData)) {
//								System.out.print("c ");
//								inAir=true;
//								hitbox.x += xSpeed;
//						}
//					}
//					
//					/*}else {
//						System.out.print("b1 ");
//					}*/
//			}else {
//				if (CanMoveHere(hitbox.x  + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
//					//if (IsFloorForEnemy(hitbox, xSpeed, lvlData)) {
//						//System.out.print("c ");
//						inAir=true;
//						hitbox.x += xSpeed;
//						
//						return;
//					//}
//				}else{
//					//System.out.print("c1 ");
//					//inAir=true;
//					changeWalkDir();
//					return;
//				}
//					
//			}
//		}
//		float direction;
//				
//		// Calculer la distance entre l'ennemi et le joueur
//	      float distanceX = player.getHitbox().x - hitbox.x;
//	      float distanceY = player.getHitbox().y - hitbox.y;
//	      float distance = (float)Math.sqrt(distanceX * distanceX + distanceY * distanceY);
//	      
//	      // Calculer l'angle entre l'ennemi et le joueur
//	      float angle = (float)Math.atan2(distanceY, distanceX);
//	      
//	      // Modifier la direction de l'ennemi pour qu'il se déplace vers le joueur
//	      direction = angle;
//	      
//	      // Vérifier si l'ennemi est proche d'une échelle
//	      if (IsEntityOnLadder(hitbox.x, hitbox.y, lvlData)) {
//	    	  			//inAir = false;
//	    	         // Modifier la position de l'ennemi en fonction de sa direction et de sa vitesse
//	    	         hitbox.y += Math.sin(direction) * 1;
//	    	      }
//	    	      else {
//	    	         // Se déplacer horizontalement à la même vitesse
//	    	    	  inAir = true;
//	    	         hitbox.x += Math.cos(direction) * 1;
//	    	      }

//			
//		float distanceX = player.getHitbox().x - hitbox.x;
//	    float distanceY = player.getHitbox().y - hitbox.y;
//	    
//	    if (IsEntityOnLadder(hitbox.x, hitbox.y, lvlData)) {
//	    	inAir = false;
//	    	if(IsEntityOnTopLadder(hitbox.x, hitbox.y, lvlData)) {
//	      	  inAir = false;
//	      	  if(distanceX > 0) {
//	      		  //if (CanMoveHere(hitbox.x  + 1, hitbox.y, hitbox.width, hitbox.height, lvlData))  
//	      		  	hitbox.y +=14;
//	      	         hitbox.x += 1;
////	      		  else
////	      			  changeWalkDir();
//	      	  }
//	      	  else {
//	      		 // if (CanMoveHere(hitbox.x  - 1, hitbox.y, hitbox.width, hitbox.height, lvlData))
//	      		  	  hitbox.y +=14;
//	      			  hitbox.x -= 1;
//	        }}else {
//	        	inAir = false;
//	            // Modifier la position de l'ennemi en fonction de sa direction et de sa vitesse
//	   	    	if(hitbox.y > player.getHitbox().y)
//	   	    		hitbox.y -= 1;
//	   	    	else
//	   	    		hitbox.y += 1;
//	        }

//	    	if(!IsEntityOnTopLadder(hitbox.x, hitbox.y, lvlData)) {
//	    		inAir = false;
//            // Modifier la position de l'ennemi en fonction de sa direction et de sa vitesse
//   	    	if(hitbox.y > player.getHitbox().y)
//   	    		hitbox.y -= 1;
//   	    	else
//   	    		hitbox.y += 1;
//	    	}else {
//	    		
//		      	  if(distanceX > 0) {
//		      		  //if (CanMoveHere(hitbox.x  + 10, hitbox.y - 14, hitbox.width, hitbox.height, lvlData)) {
//		      			hitbox.y -=14;
//		      	         hitbox.x += 10;
//		      		  
//		      		  	
////		      		  else
////		      			  changeWalkDir();
//		      	  }
//		      	  else {
//		      		  //if (CanMoveHere(hitbox.x  - 10, hitbox.y -14, hitbox.width, hitbox.height, lvlData)) {
//		      			hitbox.y -=14;
//		      	         hitbox.x -= 10;
//		      		  
//		        }
//		      	inAir = false;
//	    	}
//  			
//	    
//	    }
////      }if(IsEntityOnTopLadder(hitbox.x, hitbox.y, lvlData)) {
////    	  inAir = false;
////    	  if(distanceX > 0) {
////    		  if (CanMoveHere(hitbox.x  + 1, hitbox.y, hitbox.width, hitbox.height, lvlData))   
////    	         hitbox.x += 1;
//////    		  else
//////    			  changeWalkDir();
////    	  }
////    	  else {
////    		  if (CanMoveHere(hitbox.x  - 1, hitbox.y, hitbox.width, hitbox.height, lvlData))
////    			  hitbox.x -= 1;
////      }
////      }
//      else {
//    	  inAir = true;
//         // Se déplacer horizontalement à la même vitesse
//    	  if(distanceX > 0) {
//    		  if (CanMoveHere(hitbox.x  + 1, hitbox.y, hitbox.width, hitbox.height, lvlData))   
//    	         hitbox.x += 1;
////    		  else
////    			  changeWalkDir();
//    	  }
//    	  else {
//    		  if (CanMoveHere(hitbox.x  - 1, hitbox.y, hitbox.width, hitbox.height, lvlData))
//    			  hitbox.x -= 1;
////    		  else
////    			  changeWalkDir();
//    	  }

//  }	

//		double dist = Math.abs(Math.sqrt(Math.pow(((int) (player.getHitbox().x / Game.TILES_SIZE)) - hitbox.x, 2) + Math.pow(((int) (player.getHitbox().y / Game.TILES_SIZE)) - hitbox.y, 2 )));
//		
//		
		/*
		 * if(mountDir == UP) ySpeed = mountSpeed; else ySpeed = demountSpeed;
		 */
//		double distx1 = Math.abs(Math.sqrt(Math.pow(((int) (player.getHitbox().x / Game.TILES_SIZE)) - (hitbox.x + xSpeed), 2) + Math.pow(((int) (player.getHitbox().y / Game.TILES_SIZE)) - (hitbox.x), 2 )));
//		double disty1 = Math.abs(Math.sqrt(Math.pow(((int) (player.getHitbox().x / Game.TILES_SIZE)) - (hitbox.x), 2) + Math.pow(((int) (player.getHitbox().y / Game.TILES_SIZE)) - (hitbox.x + ySpeed), 2 )));
//		
//		
//		//System.out.println("dist " + dist);
//		//System.out.println("dist1 " + dist1);
//		if((distx1) >= dist && disty1 > distx1) 
//			if(CanMoveHereForEnemy(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
//				if (IsFloorForEnemy(hitbox, xSpeed, lvlData)) 
//					hitbox.x += xSpeed;
//		else if((disty1) >= dist)
//			if(CanMoveHereForEnemy(hitbox.x, hitbox.y + demountSpeed, hitbox.width, hitbox.height, lvlData))
//				if (IsFloorForEnemy(hitbox, xSpeed, lvlData)) 
//					hitbox.y += demountSpeed;
//			

		/*
		 * 
		 * if((dist1) >= dist) { if(IsEntityOnLadder(hitbox.x, hitbox.y, lvlData) &&
		 * player.hitbox.y < hitbox.y) while(IsEntityOnLadder(hitbox.x, hitbox.y,
		 * lvlData)) if (CanMoveHere(hitbox.x + ySpeed, hitbox.y, hitbox.width,
		 * hitbox.height, lvlData)) if (IsFloorForEnemy(hitbox, ySpeed, lvlData)) {
		 * System.out.println("oui"); hitbox.y += ySpeed; return; } else
		 * if(IsEntityOnLadder(hitbox.x, hitbox.y, lvlData) && player.hitbox.y >
		 * hitbox.y) while(IsEntityOnLadder(hitbox.x, hitbox.y, lvlData)) if
		 * (CanMoveHere(hitbox.x + ySpeed, hitbox.y, hitbox.width, hitbox.height,
		 * lvlData)) if (IsFloorForEnemy(hitbox, ySpeed, lvlData)) { hitbox.y += ySpeed;
		 * return; } else { hitbox.x += xSpeed; System.out.println("ouii"); }
		 * 
		 * 
		 * }else if((dist1) >= dist) if(IsEntityOnLadder(hitbox.x, hitbox.y, lvlData) &&
		 * player.hitbox.y < hitbox.y) while(IsEntityOnLadder(hitbox.x, hitbox.y,
		 * lvlData)) if (CanMoveHere(hitbox.x + ySpeed, hitbox.y, hitbox.width,
		 * hitbox.height, lvlData)) if (IsFloorForEnemy(hitbox, ySpeed, lvlData)) {
		 * hitbox.y += ySpeed; return; } else if(IsEntityOnLadder(hitbox.x, hitbox.y,
		 * lvlData) && player.hitbox.y > hitbox.y) while(IsEntityOnLadder(hitbox.x,
		 * hitbox.y, lvlData)) if (CanMoveHere(hitbox.x + ySpeed, hitbox.y,
		 * hitbox.width, hitbox.height, lvlData)) if (IsFloorForEnemy(hitbox, ySpeed,
		 * lvlData)) { hitbox.y += ySpeed; return; }
		 * 
		 * else hitbox.x += xSpeed;
		 */

		// changeWalkDir();

		

		

}
