package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Egg;
import entities.EnemyManager;
import entities.Sausage;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;
import static utilz.HelpMethods.SetScore;

public class ObjectManager {

	private Playing playing;
	private BufferedImage[][] foodImgs;
	private ArrayList<Food> foods;
	private ArrayList<Egg> eggsCpy = new ArrayList<>();
	private ArrayList<Sausage> sausageCpy= new ArrayList<>();
	
	

	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();
	}

	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for (Food p : foods) 
			if (p.getActive()) {
				if (hitbox.intersects(p.getHitbox())) {
					p.setFall(true);
					p.setAnimation(true);
					p.setActive(false);
					return;
				}
			}
	}
	
	public void checkFoodDamage(EnemyManager en) {
		eggsCpy = en.getEggs();
		sausageCpy = en.getSausages();
		for (Food p : foods)
			if (!p.getActive()) {	
				for(int i = 0; i < eggsCpy.size(); i++) {
					if (p.getHitbox().intersects(eggsCpy.get(i).getHitbox())) {
						eggsCpy.get(i).hurt(100);
						SetScore(50);
					}
				}
				
				for(int i = 0; i < sausageCpy.size(); i++) {
					if (p.getHitbox().intersects(sausageCpy.get(i).getHitbox())) {
						sausageCpy.get(i).hurt(100);
						SetScore(50);
					}
				
				}
		}
	}
	
	
		
	public void checkFallFood() {
		for(int i = 0 ; i < foods.size(); i++) {
			for(int j = 0 ; j < foods.size(); j++) {
				foods.get(i).fallFood();
				if(foods.get(i).getHitbox().intersects(foods.get(j).getHitbox()) && foods.get(i).getObjType()<foods.get(j).getObjType() && !foods.get(i).equals(foods.get(j))) {
						SetScore(1);
						foods.get(i).setActive(true);
						foods.get(i).setFall(false);
						foods.get(i).setAnimation(false);
						foods.get(j).setActive(false);
						foods.get(j).setFall(true);
						foods.get(j).fallFood();
						checkFallFood();
						}else if(foods.get(i).getHitbox().y >= 1040 && foods.get(j).getHitbox().y >= 1040) {
							foods.get(i).stopFallFood();			
						}		
					}
			}
		}
			
					
	


	public void loadObjects(Level newLevel) {
		foods = newLevel.getFoods();
	}

	private void loadImgs() {
		BufferedImage foodSprite = LoadSave.GetSpriteAtlas(LoadSave.BURGER_SPRITE);
		foodImgs = new BufferedImage[3][2];

		for (int j = 0; j < foodImgs.length; j++)
			for (int i = 0; i < foodImgs[j].length; i++)
				foodImgs[j][i] = foodSprite.getSubimage(60 * i, 31 * j, 60, 31);

	}

	public void update() {
		for (Food p : foods) 
			if (p.getActive())
				p.update();
		
		
		int cmp = 0;
			for(Food f : foods) 
				if(f.getHitbox().y >= 1000) {
					cmp++;
					
				}
			if(cmp == foods.size()) {
				playing.setLevelCompleted(true);
			}	
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawFoods(g, xLvlOffset);
	}


	private void drawFoods(Graphics g, int xLvlOffset) {
		for (Food p : foods)
			if (p.getActive()) {
				int type = 0;
				if (p.getObjType() == TOP_BREAD)
					type = 0;
				else if (p.getObjType() == STEACK)
					type = 1;
				else if (p.getObjType() == BOTTOM_BREAD)
					type = 2;
				
				g.drawImage(foodImgs[type][0], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), FOOD_WIDTH, FOOD_HEIGHT, null);
				
				/*
				 * outil de debug
				 */
				//p.drawHitbox(g, xLvlOffset);
			}else {
				int type = 0;
				if (p.getObjType() == TOP_BREAD)
					type = 0;
				else if (p.getObjType() == STEACK)
					type = 1;
				else if (p.getObjType() == BOTTOM_BREAD)
					type = 2;
				
				g.drawImage(foodImgs[type][1], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), FOOD_WIDTH, FOOD_HEIGHT, null);
				
				/*
				 * outil de debug
				 */
				//p.drawHitbox(g, xLvlOffset);
			}
				
	}

	public void resetAllObjects() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
		for (Food p : foods)
			p.reset();

	}
	
	public ArrayList<Food> getFoods(){
		return foods;
		
	}

}