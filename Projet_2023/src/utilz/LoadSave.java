package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;


public class LoadSave {


	public static final String PLAYER_ATLAS = "player_sprites.png";
	public static final String LEVEL_ATLAS = "outside_sprite.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String START_BACKGROUND = "shreck.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String PLAYING_BACKGROUND_IMAGE = "playing_bg.png";
	public static final String SAUSAGE_SPRITE = "sausage_sprite.png";
	public static final String EGG_SPRITE = "egg_sprite.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String BURGER_SPRITE = "food_sprite.png";
	public static final String COMPLETED_IMG = "competed_level.png";
	public static final String SCORE_FILE = "res/scoreList/score.txt";

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;

		File f = new File("res/" + fileName);		
		try {
			img = ImageIO.read(f);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;

	}
	
	public static BufferedImage[] GetAllsLevels() {
		URL url = LoadSave.class.getResource("/lvls");
		File file = null;
		
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {	
			e.printStackTrace();
		}
		
		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];
		
		for(int i = 0; i < filesSorted.length; i++)
			for(int j = 0; j < files.length; j++) {
				if(files[j].getName().equals((i + 1) + ".png"))
					filesSorted[i] = files[j];
			}
		
		
		BufferedImage[] imgs = new BufferedImage[filesSorted.length];
		
		for(int i= 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return imgs;
	}
	
	

	
}
