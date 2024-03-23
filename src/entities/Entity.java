/*
 *   Nom de la Classe: Entity
 * 
 * 	 Description: La class Entity sert à génerer chaque entité dans notre jeu
 * 
 *   Version: 1.0
 *   
 *   Date création: 28/02/2023
 *  
 *   Copyright: STIZ Romain
 *   
 */

package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import main.Game;

/*
 * La class est abstract afin de pouvoir avoir des class enfants tels que 'Ennemie' ou 'Joueur'
 */

@SuppressWarnings("serial")
public abstract class Entity implements Serializable {

	/*
	 * x, y:				Afin d'avoir la position
	 * width, height:		Afin d'avoir la taille de l'entité
	 * Rectangle2D.Float:	Afin d'avoir une hitbox en float
	 * aniTick, aniIndex:   Afin d'avoir le tick d'animation du sprite et son Index
	 * airSpeed:			Afin d'implémenter une gravité
	 * inAir:				Afin desavoir si le joueur chute ou non
	 */
	protected float x,y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	protected int aniTick, aniIndex;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currentHealth;
	protected float walkSpeed = 1.0f * Game.SCALE;
	
	//Attack Box
	protected Rectangle2D.Float attackBox;

	public Entity(float x, float y, int width, int height) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	protected void drawHitbox(Graphics g, int xLvlOffset) {
		//Debug la hitbox
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}
	
	protected void drawAttackBox(Graphics g, int lvlOffset) {
		g.setColor(Color.RED);
		g.drawRect((int)attackBox.x - lvlOffset, (int)attackBox.y,(int)attackBox.width, (int)attackBox.height);
		
	}

	protected void initHitbox(int width, int height) {

		hitbox = new Rectangle2D.Float(x, y , (int) (width * Game.SCALE), (int)(height * Game.SCALE));

	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public int getEnemyState() {
		return state;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}
	
}
