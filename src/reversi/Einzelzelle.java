package reversi;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Diese Klasse ist die einzelne Zelle, die jedes Feld des Spielbretts repraesentiert.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class Einzelzelle extends JPanel {
	
	/**Dies ist die Farbe des aktuellen Steins: 0=leer, 1=schwarz, 2=weiss*/
	private int steinFarbe;
	
	protected Einzelzelle(int steinFarbe) {
		setzeStein(steinFarbe);
	}
	
	@Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        if(steinFarbe==1) {
        	gr.setColor(Color.black);
        	gr.fillOval(5, 5, getWidth()-10, getHeight()-10);
        } else if(steinFarbe==2) {
        	gr.setColor(Color.white);
        	gr.fillOval(5, 5, getWidth()-10, getHeight()-10);
        }
    }
	
	/**
	 * Diese Methode dreht einen Spielstein um, um die inverse Farbe anzuzeigen.
	 */
	public void umdrehen() {
		if(steinFarbe==1) {
			steinFarbe++;
		} else {
			steinFarbe = 1;
		}
		repaint();
	}
	
	/**
	 * Diese Methode ueberprueft, ob das Feld noch frei ist und setzt den Spielstein dorthin.
	 * @param steinFarbe Nimmt den Farbwert des setzenden Spielers entgegen.
	 * @return Gibt zurueck, ob das Feld noch frei war.
	 */
	public boolean setzeStein(int steinFarbe) {
		if(this.steinFarbe==0) {
			this.steinFarbe = steinFarbe;
			repaint();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Diese Methode entfernt den Stein vom aktuellen Spielfeld.
	 */
	public void zuruecksetzen() {
		steinFarbe = 0;
		repaint();
	}

	public int getSteinFarbe() {
		return steinFarbe;
	}
}