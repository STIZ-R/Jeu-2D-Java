/*
 *   Nom de la Classe: ScoreFile
 * 
 * 	 Description: La class ScoreFile permet d emettre à jour le score
 *   
 *   Version: 1.0
 *  
 *   Copyright: STIZ Romain
 *   
 */
package gamestates;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import static utilz.LoadSave.SCORE_FILE;

public class ScoreFile{
	
	
	
	/*
	 * Cette méthode écrit dans le fichier le score avec le nom du joueur
	 */
	public synchronized void writeScoreToFile(String playerName, int score) {
		try {
			FileWriter fw = new FileWriter(SCORE_FILE, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(score + " de score : " + playerName + "\n");
			bw.close();
		}	catch(IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Cette méthode retourne un classement des 5 meilleurs joueurs
	 */
	public ArrayList<Long> getTopFivePlayersScore(){
		ArrayList<Long> scores = new ArrayList<>();
		try {
			File file = new File(SCORE_FILE);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(" de score : ");
				long score = Integer.parseInt(parts[0].trim());
				scores.add(score);
			}
			scanner.close();
		}	catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		Collections.sort(scores, Collections.reverseOrder());
		return new ArrayList<>(scores.subList(0,  Math.min(scores.size(), 5)));
	}
	
	public ArrayList<String> getTopFivePlayersName(){
		ArrayList<String> players = new ArrayList<>();
		try {
			File file = new File(SCORE_FILE);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(" de score : ");
				String name = parts[1].trim();
				players.add(name + " : score");
			}
			scanner.close();
		}	catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		Collections.sort(players, Collections.reverseOrder());
		return new ArrayList<>(players.subList(0,  Math.min(players.size(), 5)));
	}
}
