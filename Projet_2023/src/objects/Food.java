package objects;

import main.Game;

public class Food extends GameObject{
	
	@SuppressWarnings("unused")
	private int objType;
	
	public Food(int x, int y, int objType) {
		super(x, y, objType);
		this.objType = objType;

		createHitbox();


		//maxHoverOffset = (int) (10 * Game.SCALE);
	}
	
	public void createHitbox() {
		initHitbox(62, 10);
		xDrawOffset = (int) (2 * Game.SCALE);
		yDrawOffset = (int) (20 * Game.SCALE);
		
		hitbox.y += yDrawOffset;
		hitbox.x += xDrawOffset / 2;
	}

	public void update() {
		if(doAnimation)
			updateAnimationTick();
	}
}
