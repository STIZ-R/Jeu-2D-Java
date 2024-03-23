/*
 *   Nom de la Classe: AudioPlayer
 * 
 * 	 Description: La class qui sert à avoir de sons dans le jeu.
 * 
 *   Version: 1.0
 *   
 *   Date création: 20/03/2023
 *  
 *   Copyright: STIZ Romain
 *   
 */

package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

	/*
	 * 	ATTRIBUTS STATIC:
	 * 
	 *  MUSIQUE_GAME: 						Musique du menu
	 *  MUSIQUE_GAME1: 						Musique du jeu
	 *  MUSIQUE_GAME2: 						Musique de défaite
	 * 	MUSIQUE_GAME3: 						Musique du jeu
	 *  MUSIQUE_GAME4: 						Musique de victoire
	 *  
	 *  ATTACKING:							Son attaque du joueur
	 *  ATTACKING1:							Son attaque du joueur
	 *  
	 *  Clip:								Permet d'importer des sons dans le jeu
	 *  currentSongID:						Permet de savoir quelle est la musqiue jouée	
	 *  volume:								Définit le volume global
	 *  songMute, effectMute:				Définit si oui ou non le son est produit.		
	 */
	
	public static int MUSIQUE_GAME = 0;
	public static int MUSIQUE_GAME1 = 1;
	public static int MUSIQUE_GAME2 = 2;
	public static int MUSIQUE_GAME3 = 3;
	public static int MUSIQUE_GAME4 = 4;
	
	
	public static int ATTACKING = 0;
	public static int ATTACKING1 = 1;
	
	private Clip[] songs, effects;
	private int currentSongID;
	private float volume = 0.8f;
	private boolean songMute, effectMute;
	private Random rand = new Random();
	
	/*
	 * Constructeur de la classe 
	 */
	
	public AudioPlayer(){
		loadSongs();
		loadEffects();
		playSong(MUSIQUE_GAME);
	}
	
	/*
	 * Mets dans le Clip songs[] toutes les musiques du jeu.
	 */
	
	private void loadSongs() {
		String[] names = {"Erika", "Burger_Time", "souliko", "katyusha", "Srbija_jaka"};
		songs = new Clip[names.length];
		for(int i = 0; i < songs.length; i++) 
			songs[i] = getClip(names[i]);
	}
	
	/*
	 * Mets dans le Clip effects[] touts les effets du jeu.
	 */
	
	private void loadEffects() {
		String[] effectsNames = {"Throwing","bonk"};
		effects = new Clip[effectsNames.length];
		for(int i = 0; i < effects.length; i++)
			effects[i] = getClip(effectsNames[i]);
		
		updateEffectsVolume();
	}
	
	/*
	 * Permet de récupérer le fichier .wav dans les dossiers src/audio.
	 */
	
	private Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name +".wav");
		AudioInputStream audio;
		
		try {
			audio = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(audio);
			return c;
		}catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Permet de définir la musique du niveau.
	 */
	
	public void setLevelSong(int lvlIndex) {
		if (lvlIndex % 2 == 0)
			playSong(MUSIQUE_GAME1);
		else
			playSong(MUSIQUE_GAME3);
	}
	
	/*
	 * Coupe la musique quand on gagne et joue la musique MUSIQUE_GAME4
	 */
	
	public void lvlCompleted() {
		stopSong();
		playSong(MUSIQUE_GAME4);
	}
	
	/*
	 * Joue un son quand on attaque que quand le random retourne 99 ou 2. Ce sont des sons rares car sinon cela devient énèrvant de les entendre
	 */
	
	public void playAttackSound() {
		int start = rand.nextInt(100);
		if(start == 99)
			playEffect(0);
		if(start == 2)
			playEffect(1);
		else
			return;
	}
	
	public void playEffect(int effect) {
		
		effects[effect].setMicrosecondPosition(0);
		effects[effect].start();
	}
	
	public void stopSong() {
		if(songs[currentSongID].isActive())
			songs[currentSongID].stop();
	}
	
	public void playSong(int song) {
		if(songs[currentSongID].isActive())
			songs[currentSongID].stop();
		
		currentSongID = song;
		updateSongVolume();
		songs[currentSongID].setMicrosecondPosition(0);
		songs[currentSongID].loop(Clip.LOOP_CONTINUOUSLY);
			
	}
	
	public void toggleSongMute() {
		
		this.songMute = !songMute;
		for(Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
		
	}
	public void toggleEffectMute() {
		
		this.effectMute = !effectMute;
		for(Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(effectMute);
		}
		if(!effectMute)
			playEffect(ATTACKING1);
	}
	
	private void updateSongVolume() {
		
		FloatControl gainC = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainC.getMaximum() - gainC.getMinimum();
		float gain = (range * volume) + gainC.getMinimum();
		gainC.setValue(gain);
	}
	
	private void updateEffectsVolume() {
		
		for(@SuppressWarnings("unused") Clip c : effects) {
		FloatControl gainC = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainC.getMaximum() - gainC.getMinimum();
		float gain = (range * volume) + gainC.getMinimum();
		gainC.setValue(gain);
		}
	}
	

	
	
	
	
	
	
	
	
}
