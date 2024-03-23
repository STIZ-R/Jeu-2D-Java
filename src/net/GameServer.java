package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import entities.Player;
import main.Game;

//
//
public class GameServer{
    // Liste des clients connectés au serveur
    private static List<PlayerClientHandler> clients = new ArrayList<>();
    
    public static void main(String[] args) {
        int serverPort = 5555; // Port utilisé par le serveur
        
        try {
            // Création d'un nouveau serveur TCP
            @SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server started on port " + serverPort);
            
            // Attente de nouvelles connexions de clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                
                // Création d'un nouveau gestionnaire de client pour gérer les communications avec ce client
                PlayerClientHandler clientHandler = new PlayerClientHandler(clientSocket);
                
                // Ajout du gestionnaire de client à la liste des clients connectés
                try {
					clients.add(clientHandler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                // Démarrage du traitement de messages pour ce client
                new Thread(clientHandler).start();
                
            }
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }
    
//    // Gestionnaire de client qui traite les messages reçus du client
    private static class PlayerClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private Player player;
        private String pseudo;
        private ArrayList<Player> players = new ArrayList<>();
        
        public PlayerClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        @Override
        public void run() {
            try {
                // Flux de sortie pour envoyer des données au client
                OutputStream outputStream = clientSocket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);
                
                // Flux d'entrée pour recevoir des données du client
                InputStream inputStream = clientSocket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);
                
                // Récupération de l'objet Player envoyé par le client
                pseudo = (String) objectInputStream.readObject();
                player = new Player(0,0,0,0, null, pseudo);
                players.add(player);
                new Game(players);
                System.out.println("New player connected: " + pseudo);
                
                // Envoi des coordonnées du joueur à tous les clients connectés
                sendToAllClients(pseudo + " connected.");
                System.out.println(players.size());
                // Traitement des messages reçus du client
                String coordinates;
                while ((coordinates = (String) objectInputStream.readObject()) != null) {
                    System.out.println("Coordinates received from " + pseudo + ": " + coordinates);
                    
                    // Envoi des coordonnées du joueur à tous les clients connectés
                    sendToAllClients(pseudo + " coordinates: " + coordinates);
                }
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            } finally {
                try {
                    // Fermeture de la connexion et suppression du gestionnaire de client de la liste des clients connectés
                    clients.remove(this);
                    clientSocket.close();
                    
                    // Envoi des coordonnées du joueur à tous les clients connectés
                    sendToAllClients(pseudo + " disconnected.");
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
        private void sendToAllClients(String message) throws IOException {
            for (PlayerClientHandler client : clients) {
                if (client.equals(this)) {
                    client.objectOutputStream.writeObject(message);
                    client.objectOutputStream.flush();
                }
            }
        }
    }
}
    