/*
 *   Nom de la Classe: UrmButton (Unpause, Replay, Menu)
 * 
 * 	 Description: La class UrmButton sert à mttre en place les 3 boutons 
 * 				  afin de pouvoir rejouer, lancer le menu ou bien enlever la 'pause' quand on est dans le menu.
 *   
 *   Version: 1.0
 *   
 *   Date création: 05/03/2023
 *  
 *   Copyright: STIZ Romain
 *   
 */



package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

public class UrmButton extends PauseButton{
	
	/*
	 * BufferedImage:				afin de stocker les images paint dans un tableau
	 * rowIndex, index:				afin d'avoir accés à la 'sub' image nécessaire
	 * mouseOver, mousePressed:	 	afin de savoir si l'utilisateur clique ou passe sa souris au dessus
	 * 								des boutons afin de changer l'image.
	 */
	private BufferedImage[] imgs;
	private int rowIndex, index;
	private boolean mouseOver, mousePressed;

	/*
	 * Constructeur de la classe permettant d'appeler celui de PauseButton, on récupère de plus l'index du bouton voulus
	 * et on charge les images dans le BufferedImage afin de les 'découper'.
	 * 
	 */
	
	public UrmButton(int x, int y, int width, int height, int rowIndex) {
		super(x, y, width, height);
		this.rowIndex = rowIndex;
		loadImgs();
		
	}
	
	/*
	 * On va parcourir l'image qu'on va séparer en sous-images de taille URM_DEFAULT_SIZE (56px)
	 */
	
	private void loadImgs() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
		imgs = new BufferedImage[3];
		for(int i=0; i <imgs.length; i++)
			imgs[i] = temp.getSubimage(i*URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
		
	}
	
	/*
	 * Méthode permettant d'actualiser à chaque fois le jeu afin d'afficher ce qui est nécessaire
	 */

	public void update() {
		index = 0;
		if(mouseOver)
			index = 1;
		if(mousePressed)
			index = 2;
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
	}
	
	/*
	 * Fonction permettant de reset les booleans de mouseOver et de mousePressed
	 */

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	
	/*
	 * GETTER & SETTER
	 */

	public boolean getMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean getMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
	
	
}
