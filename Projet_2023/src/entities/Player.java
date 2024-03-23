/*
 *   Nom de la Classe: Player
 * 
 * 	 Description: La class Player sert à mettre
 * 	 en place le joueur, il extend de Entity qui permet
 *   de créer d'autres instances d'entités (ennemies ou alliés)
 *   
 *   Version: 1.0
 *   
 *   Date création: 20/02/2023
 *  
 *   Copyright: STIZ Romain
 *   
 */






package entities;


import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PlayerConstants.IDLE;
import static utilz.Constants.PlayerConstants.MOUNT;
import static utilz.Constants.PlayerConstants.RUNNING;
import static utilz.Constants.PlayerConstants.ATTACK;
import static utilz.Constants.PlayerConstants.DEAD;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;



@SuppressWarnings("serial")
public class Player extends Entity implements Serializable{


	private String name;
	
	//private String pseudo;
	
	/*
	 * 	ATTRIBUTS:
	 * 
	 *  BufferedImage: 						afin d'avoir le fichier correspondant à l'animation du joueur
	 *  
	 *  left, up, right, down:				permet pour plus bas de changer l'action du joueur   
	 *  moving, attacking, mount, demount:  nous précise que le joueur n'est pas entrain de faire ces actions au début
	 *  playerSpeed: 						décris la vitesse du personnage selon l'échelle choisit dans Game
	 *  lvlData:							tableau 2d contenant le niveau sous forme de pixels de couleurs différents
	 *  xOffset, yOffset:  					décale afin de faire correspondre la hitbox du personne au rectangle
	 * 
	 */

	private BufferedImage[][] animations;
	private boolean left, right;
	private boolean moving = false, attacking = false, mount = false, demount = false;
	private int[][] lvlData;
	private float xOffSet = 21 * Game.SCALE;
	private float yOffSet = 4 * Game.SCALE;

	/*
	 * Gravité:
	 * 
	 * mountSpeed:					variable permettant au personnage de remonter une échelle tout en contrant la gravité
	 * demountSpeed:    			on ne veut pas descendre une échelle trop rapidement, n'est-ce pas?
	 * fallSpeedAfterCollision:		vitesse après collision
	 * 
	 */
	private float mountSpeed = -0.7f * Game.SCALE;
	private float demountSpeed = 0.7f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	
	/*
	 * Barre de vie et de power
	 */
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);

	private int healthWidth = healthBarWidth;
	
	private int flipX = 0;
	private int flipW = 1;
	
	private boolean attackChecked = false;
	
	private Playing playing;
	
	private int nbAttack = 30;
	//private LoadSave ls;

	/*
	 * Constructeur de la classe pemrettant de créer notre personnage 
	 * et d'initialliser sa zone de collision
	 * 
	 */
	
	public Player(float x, float y, int width, int height, Playing playing, String name) {
		super(x,  y, width, height);
		this.name = name;
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		loadAnimations();
		initHitbox(20, 27);
		initAttackBox();
	}
	
	
	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}
	
	/*
	 * update() et render() permettent de nous afficher à chaque fois ce qu'il y a de nouveau
	 * 
	 */

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int)(50*Game.SCALE), (int)(20*Game.SCALE));
		
	}

	public void update() {
		updateHealthBar();
		if (currentHealth <= 0) {
			if (state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
			} else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playSong(AudioPlayer.MUSIQUE_GAME2);
			}else
				updateAnimationTick();
			return;
		}
		checkFoodFalling();
		checkFoodDamage();
		updateAttackBox();
		updatePos();
		checkFoodTouched();
		if(attacking)
			checkAttack();
		updateAnimationTick();
		setAnimation();
		
	}
	
	
	
	

	
	
	
	

	
	private void checkFoodDamage() {
		playing.checkFoodDamage();
		
	}

	private void checkFoodFalling() {
		playing.checkFoodFalling();
		
	}

	private void checkFoodTouched() {
		playing.checkFoodTouched(hitbox);
		
	}

	private void checkAttack() {
		if(attackChecked || aniIndex != 1)
			return;
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();
		
		
	}
	
	

	private void updateAttackBox() {
		if(right) {
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE*10);
		}else if(left) {
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE*45);
		}
		attackBox.y = hitbox.y + (Game.SCALE * 10);
		
	}

	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xOffSet) - lvlOffset + flipX, (int) (hitbox.y - yOffSet), width * flipW, height, null);
		
		/*
		 * outil de debug permettant de voir la hitbox du personnage
		 *
		 */
		//drawAttackBox(g, lvlOffset);
		//drawHitbox(g, lvlOffset);
		drawUI(g);
		g.setColor(Color.white);
		g.drawString("Score: "+SCORE, 600, 30);
		if(name!= null) {
			g.setColor(Color.white);
			g.drawString(name, (int)(hitbox.x - name.length()* 2), (int)(hitbox.y - 10));
			g.drawString("v", (int)(hitbox.x + 7), (int)(hitbox.y));
		}

	}
	


	public void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float)maxHealth) * healthBarWidth);
	}

	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		
	}

	/*
	 * On récupère le fichier contenant les 'sprites' de notre joueur
	 * et on charge dans animations chaque sprites du joueur qui a été découper en SubImage
	 * 
	 */
	private void loadAnimations() {

		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

		animations = new BufferedImage[6][6];
		for(int j = 0; j < animations.length; j++)
			for(int i = 0; i < animations[j].length; i++) 
				animations[j][i] = img.getSubimage( i * 64,  j * 40, 64, 40);
		
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}
	
	/*
	 * Permet de charger dans lvlData les informations du niveau
	 * et si on est en l'air alors on fait tomber notre joueur
	 * 
	 */

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}
	
	/*
	 * permet de bloquer notre personnage
	 * 
	 */

	public void resetDirBooleans() {

		left = false;
		right = false;
	}

	/*
	 * On change de 'sprite' selon notre aniSpeed définit
	 * On regarde combien il y a de sprites à afficher et si il est supérieur à ce nombre
	 * alors on retourne à zéro afin de faire une boucle
	 * 
	 */

	private void updateAnimationTick() {

		aniTick++;
		if(aniTick>= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
			}

		}

	}
	
	/*
	 * permet de choisir la bonne animations selon le contexte
	 *
	 */

	private void setAnimation() {
		int startAni = state;
		if(moving)
			state = RUNNING;
		else if(!IsEntityOnLadder(hitbox.x, hitbox.y, lvlData))
			state = IDLE;
		if(inAir) {
			if(airSpeed < 0 && IsEntityOnLadder(hitbox.x, hitbox.y, lvlData))
				state = MOUNT;
			else if(IsEntityOnLadder(hitbox.x, hitbox.y, lvlData)) {
				state = MOUNT;
				inAir = false;
			}
			//Bug animation même quand je marche
			/*else
			playerAction = FALLING;
			 */
		}
		if(attacking) {
			state = ATTACK;
			if(startAni != ATTACK) {
				aniIndex = 1;
				aniTick = 0;
			}
		}
		if(startAni != state) {
			resetAniTick();
		}
	}
	
	/*
	 * permet de reset l'index et le tick
	 * 
	 */

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;

	}
	
	/*
	 * 
	 * Permet de regarder les collisions avec les corners de la hitbox.
	 * de plus regarde si l'on peut monter ou non,...
	 * et regarde si l'on peut tomber ou continuer de tomber
	 * 
	 */

	private void updatePos() {

		moving = false;
		if(mount && IsEntityOnLadder(hitbox.x, hitbox.y, lvlData))
			mount();
		if(demount && IsEntityOnLadder(hitbox.x, hitbox.y, lvlData))
			demount();
		if(!left && !right && !inAir)
			return;

		float xSpeed = 0;


		if(left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if(right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}
		if(!inAir && !IsEntityOnLadder(hitbox.x, hitbox.y, lvlData)) {
			if(!IsEntityOnFloor(hitbox, lvlData)) {
				inAir = true;
			}
		}
		/*
	if(up && !down&&!left && !right) 
		ySpeed = -playerSpeed;
	else if(down && !up &&!left && !right)
		ySpeed = playerSpeed;
		 */

		if(inAir) {
			if(CanMoveHere(hitbox.x, hitbox.y+ airSpeed, hitbox.width, hitbox.height, lvlData)) {

				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			}else {

				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0) 
					resetInAir();
				else 
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
		}else 
			updateXPos(xSpeed);
		moving = true;
	}
	
	/*
	 * 
	 * permet comme son nom l'indique de mettre à jour X si on peut aller ici
	 * 
	 */
	
	private void updateXPos(float xSpeed) {

		if(CanMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {	
			hitbox.x += xSpeed;	
		}else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);

		}

	}
	
	public void changeHealth(int value) {
		currentHealth += value;
		
		if(currentHealth <= 0) {
			currentHealth = 0;
			//gameOver();
		}
		else if( currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

	/*
	 * 
	 * GETTER & SETTER
	 * 
	 */
	
	private void mount() {	
		inAir = true;
		airSpeed = mountSpeed;	
	}

	private void demount() {	
		inAir = true;
		airSpeed = demountSpeed;	
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	public void setAttack(boolean attacking) {
		if(nbAttack >= 0)
			this.attacking = attacking;
		nbAttack -= 1;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	
	public void setMount(boolean mount) {
		this.mount = mount;
	}

	public void setDemount(boolean demount) {
		this.demount = demount;
	}
	
	public void setPlaying(Playing p) {
		this.playing = p;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		state = IDLE;
		currentHealth = maxHealth;
		hitbox.x = x;
		hitbox.y = y;
		nbAttack = 30;
		
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		
	}
	
	public String getPseudo() {
		return name;
	}
	
	public void setHitbox(Rectangle2D.Float rect) {
		this.hitbox = rect;
	}
	
	

	
	
	
	
	
	
	
	
	

}
