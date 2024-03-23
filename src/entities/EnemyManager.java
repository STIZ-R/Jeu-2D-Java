/*
 *   Nom de la Classe: Enemymaanger
 * 
 * 	 Description: La class Enemymaanger permet mettre en place les Ennemis.
 *   
 *   Version: 1.0
 *  
 *   Copyright: STIZ Romain
 *   
 */

package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
	
	/*
	 * 	ATTRIBUTS:
	 * 
	 *  playing: 							afin d'attribuer un jeu
	 *  sausageArr:							stock les images des saucisses
	 *  eggArr:								stock les images des oeufs  
	 *  sausages,eggs:  					stock les saucisses/oeufs dans une ArrayList
	 * 
	 */

	@SuppressWarnings("unused")
	private Playing playing;
	private BufferedImage[][] sausageArr;
	private BufferedImage[][] eggArr;
	private ArrayList<Sausage> sausages = new ArrayList<>();
	private ArrayList<Egg> eggs = new ArrayList<>();

	
	/*
	 * Constructeur de la classe
	 */
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		sausages = level.getSausages();
		eggs = level.getEggs();
		
	}
	
	/*
	 * Méthode permettant de débug en tuant tout les ennemis
	 */
	
	public void killAllEnemies() {
		for(Sausage s:sausages) {
			s.hurt(100);
		}
		for(Egg e: eggs) {
			e.hurt(100);
		}
	}
	
	/*
	 * On regarde si tout les ennemis sont morts, si tel est le cas, les fait réapparaître
	 */
	
	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for(Sausage c : sausages)
			if(c.getActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			}
				
		for(Egg c : eggs)
			if(c.getActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			}
		if(!isAnyActive)
			resetAllEnemies();
			
			
				
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawSausages(g, xLvlOffset);
		drawEggs(g, xLvlOffset);
	}

	private void drawSausages(Graphics g, int xLvlOffset) {
		for (Sausage c : sausages) {
			if (c.getActive()) {

			g.drawImage(sausageArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - SAUSAGE_DRAWOFFSET_X +c.flipX(), (int) c.getHitbox().y - SAUSAGE_DRAWOFFSET_Y, SAUSAGE_WIDTH * c.flipW(), SAUSAGE_HEIGHT, null);
			
			/*
			 * outil de debug permettant de voir la hitbox de l'ennemie
			 *
			 */
			//c.drawHitbox(g, xLvlOffset);
			//c.drawAttackBox(g, xLvlOffset);
			}
		}
	}
	
	
	
	private void drawEggs(Graphics g, int xLvlOffset) {
		for (Egg c : eggs) {
			if (c.getActive()) {

			g.drawImage(eggArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - EGG_DRAWOFFSET_X +c.flipX(), (int) c.getHitbox().y - EGG_DRAWOFFSET_Y, EGG_WIDTH * c.flipW(), EGG_HEIGHT, null);
			
			/*
			 * outil de debug permettant de voir la hitbox de l'ennemie
			 *
			 */
			c.drawHitbox(g, xLvlOffset);
			//c.drawAttackBox(g, xLvlOffset);
			}
		}
	}
	
	
	

	/*
	 * Permet de regarder si l'attaque du joueur est en collision avec la hitbox de l'ennemi et lui fait des dégâts .
	 */

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Sausage c : sausages)
			if (c.getActive())
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(33);
					return;
				}
		for (Egg c : eggs)
			if (c.getActive())
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(33);
					return;
				}
	}

	private void loadEnemyImgs() {
		sausageArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SAUSAGE_SPRITE);
		for (int j = 0; j < sausageArr.length; j++)
			for (int i = 0; i < sausageArr[j].length; i++)
				sausageArr[j][i] = temp.getSubimage(i * SAUSAGE_WIDTH_DEFAULT, j * SAUSAGE_HEIGHT_DEFAULT, SAUSAGE_WIDTH_DEFAULT, SAUSAGE_HEIGHT_DEFAULT);
		
		
		eggArr = new BufferedImage[5][9];
		BufferedImage temp1 = LoadSave.GetSpriteAtlas(LoadSave.EGG_SPRITE);
		for (int j = 0; j < eggArr.length; j++)
			for (int i = 0; i < eggArr[j].length; i++)
				eggArr[j][i] = temp1.getSubimage(i * EGG_WIDTH_DEFAULT, j * EGG_HEIGHT_DEFAULT, EGG_WIDTH_DEFAULT, EGG_HEIGHT_DEFAULT);
	
	}

	public void resetAllEnemies() {
		for(Sausage c : sausages)
			c.resetEnemy();
		for(Egg e : eggs)
			e.resetEnemy();
		
	}
	
	public ArrayList<Egg> getEggs(){
		return eggs;
	}
	public ArrayList<Sausage> getSausages(){
		return sausages;
	}

}