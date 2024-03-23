/*
 *   Nom de la Classe: MainClass
 * 
 * 	 Description: La class principale sert à lancer notre jeu
 * 
 *   Version: 1.0
 *   
 *   Date création: 20/02/2023
 *  
 *   Copyright: STIZ Romain
 *   
 */


/*
 * Lancer le jeu sans Serveur et Client.
 */


package main;
import java.util.ArrayList;

import entities.Player;
public class MainClass {

	public static void main(String[] args) throws Exception {
		ArrayList<Player> p = new ArrayList<Player>();
		Player p1 = new Player(0, 0, 0, 0, null, null);
		p.add(p1);
		new Game(p);
	}
}


