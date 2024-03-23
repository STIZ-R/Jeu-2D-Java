package objects;


import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class GameObject {

	protected int x, y, objType;
	protected Rectangle2D.Float hitbox;
	protected int xDrawOffset, yDrawOffset;
	protected boolean doAnimation, active = true, fall = false;
	protected int aniTick, aniIndex;
	private float demountSpeed = 0.7f * Game.SCALE;

	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

	public void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(objType)) {
				aniIndex = 0;
				if(objType == TOP_BREAD || objType == STEACK || objType == BOTTOM_BREAD) {
					doAnimation = false;
					active = false;
				}
			}	
		}
	}
	
	protected void fallFood() {
		if(this.fall)
			this.hitbox.y += demountSpeed;
	}
	
	protected void stopFallFood() {
		this.fall = false;
		this.setActive(true);
	}
	
	public void reset() {
		hitbox.x = x + xDrawOffset;
		hitbox.y = y + yDrawOffset;
		aniIndex = 0;
		aniTick = 0;
		active = true;
		fall = false;
		if(objType == TOP_BREAD || objType == STEACK || objType == BOTTOM_BREAD)
			doAnimation = false;
		else
			doAnimation = true;
	}

	public int getObjType() {
		return objType;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean getFall() {
		return fall;
	}
	
	public void setFall(boolean fall) {
		this.fall = fall;
	}

	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}
	public int getAniIndex() {
		return aniIndex;
	}
	
	public void setAnimation(boolean ani) {
		this.doAnimation = ani;
	}

	

}