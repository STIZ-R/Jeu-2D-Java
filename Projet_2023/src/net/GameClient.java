package net;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

import entities.Player;





public class GameClient {
	static int port = 5555;
    static String ip="127.0.0.1";
    static String pseudo;
    static boolean arreter=false;
    
    public static void main(String[] args) throws Exception {
      if (args.length!=0){ 
		ip=args[0];
		port=Integer.parseInt(args[1]);
		pseudo = JOptionPane.showInputDialog("Nom du joueur?","Player 1");
      }
      try {
          // Création d'une nouvelle connexion TCP avec le serveur
          @SuppressWarnings("resource")
		Socket socket = new Socket(ip, port);
          
          // Flux de sortie pour envoyer des données au serveur
          OutputStream outputStream = socket.getOutputStream();
          ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
          
          // Création d'un objet Player
          Player player = new Player(0, 0, 0, 0, null, null);
          //System.out.print(player.getPseudo());
          
          // Envoi de l'objet Player au serveur
          objectOutputStream.writeObject(pseudo);
         
          // Flux d'entrée pour recevoir des données du serveur
          ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
          @SuppressWarnings("unused")
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
          // Récupération des coordonnées du joueur
          while(true) {
        	  @SuppressWarnings("unused")
			String coordinates = player.getHitbox().toString();
        	  Rectangle2D.Float re = (Rectangle2D.Float) in.readObject();
              while (re != null) {
            	  player.setHitbox(re);
                  System.out.println("Coordinates received: " + re);
              }
          }
          
          
          // Fermeture de la connexion
          //socket.close();
      } catch (IOException ex) {
          ex.printStackTrace();
      }
    }
}
