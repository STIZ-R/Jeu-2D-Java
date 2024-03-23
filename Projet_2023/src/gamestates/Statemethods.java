/*
 *   Nom de l'Interface: Statemethods
 * 
 * 	 Description: L'interface Statemethods permet la création de méthodes contenant les signatures ci-dessous.
 *   
 *   Version: 1.0
 *  
 *   Copyright: STIZ Romain
 *   
 */

package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Statemethods {

	public void update();
	public void draw(Graphics g);
	public void mousClicked(MouseEvent e);
	public void mousePressed(MouseEvent e);
	public void mouseReleased(MouseEvent e);
	public void mouseMoved(MouseEvent e);
	public void keyPressed(KeyEvent e);
	public void keyReleased(KeyEvent e);

}
