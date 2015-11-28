package reversi;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

/**
 * Diese Klasse stellt das Frame dar, auf welchem das Spielfeld angezeigt wird
 * und fuehrt alle internen Berechnungen zum Spielverlauf aus.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class ReversiGUI {
	
	/**Dies ist das JFrame, auf welchem das Spielfeld dargestellt wird.*/
	private JFrame frame1 = new JFrame("Reversi");
	/**Dies sind die Zellenelemente, auf die die Steine gesetzt werden.*/
	private Einzelzelle[][] spielfeld = new Einzelzelle[8][8];
	/**Dies ist der Spieler, der aktuell am Zug ist.*/
	private int aktSpieler = 1;
	/**Dies ist die Anzahl der Felder, die noch unbelegt sind.*/
	private int felderUebrig = 60;
	
	public ReversiGUI() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(600,600));
		frame1.setMinimumSize(new Dimension(400,400));
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridLayout(8,8));
		
		for(int x=0;x<8;x++) {
			for(int y=0;y<8;y++) {
				if(x==y && (x==3 || x==4)) {
					spielfeld[x][y] = new Einzelzelle(2);
				} else if(x==3 && y==4 || x==4 && y==3) {
					spielfeld[x][y] = new Einzelzelle(1);
				} else {
					spielfeld[x][y] = new Einzelzelle(0);
				}
				spielfeld[x][y].setBackground(new Color(0x33B200));
				spielfeld[x][y].setBorder(new LineBorder(Color.black, 1));
				spielfeld[x][y].setOpaque(true);
				final int tempX = x;
				final int tempY = y;
				spielfeld[x][y].addMouseListener(new MouseAdapter() {
	            	@Override
					public void mouseClicked(MouseEvent e) {
	            		if(spielfeld[tempX][tempY].setzeStein(aktSpieler)) {
	            			if(steineUmdrehen(aktSpieler,tempX,tempY)>0) {
	            				changeSpieler();
	            				felderUebrig--;
	            			} else {
	            				spielfeld[tempX][tempY].zuruecksetzen();
	            			}
	            		}
	            		if(felderUebrig==0) {
	            			auswertung();
	            			return;
	            		}
	            		if(!steinLegungMoeglich()) {
	            			changeSpieler();
	            			if(!steinLegungMoeglich()) {
	            				JOptionPane.showMessageDialog(null, "Es sind für keinen Spieler mehr gültige Spielzüge übrig."+System.getProperty("line.separator")+"Das Spiel wird hiermit beendet.", "Kein Spielzug möglich", JOptionPane.INFORMATION_MESSAGE);
	            				auswertung();
	            			} else {
	            				JOptionPane.showMessageDialog(null, "Leider ist für Dich gerade kein Spielzug möglich."+System.getProperty("line.separator")+"Der andere Spieler darf nun wieder setzen.", "Kein Spielzug möglich", JOptionPane.INFORMATION_MESSAGE);
	            			}
	            		}
	            	}
	            });
				frame1.add(spielfeld[x][y]);
			}
		}
		
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	/**
	 * Diese Methode wechselt nach Zugende den Spieler, der dran ist mit Setzen.
	 */
	private void changeSpieler() {
		if(aktSpieler==1) {
			aktSpieler++;
		} else {
			aktSpieler = 1;
		}
	}
	
	/**
	 * Diese Methode dreht alle Steine um, die zwischen zwei Steinen positioniert sind.
	 * @param farbeConquerer Die Spielernummer des Zugausfuehrenden.
	 * @param x Der x-Wert des aktuellen Felds.
	 * @param y Der y-Wert des aktuellen Felds.
	 * @return Gibt zurueck, wie viele Steine durch den Spielzug umgedreht wurden.
	 */
	private int steineUmdrehen(int farbeConquerer, int x, int y) {
		int[] xDirArr = {-1, 0, 1};
		int[] yDirArr = {-1, 0, 1};
		int anzUmgedreht = 0;
		
		for(int dirY:yDirArr) {
			for(int dirX:xDirArr) {
				if(!(dirY==0 && dirX==0)) {
					int tempX = x;
					int tempY = y;
					boolean inbounds = true;
					boolean keeprunning = true;
					
					do {
						tempX += dirX;
						tempY += dirY;
						if(tempX < 0 || tempX > 7 || tempY < 0 || tempY > 7) {
							inbounds = false;
						} else if(!(spielfeld[tempX][tempY].getSteinFarbe() != farbeConquerer && spielfeld[tempX][tempY].getSteinFarbe() != 0)) {
							keeprunning = false;
						}
					} while(keeprunning && inbounds);
					
					if(inbounds) {
		                if(spielfeld[tempX][tempY].getSteinFarbe()==farbeConquerer) {
		                    tempX -= dirX;
		                    tempY -= dirY;
		                    
		                    do {
		                    	if(!(tempX==x && tempY==y)) {
		                    		spielfeld[tempX][tempY].umdrehen();
		                    		anzUmgedreht++;
		                    	} else {
		                    		break;
		                    	}
		                        tempX -= dirX;
		                        tempY -= dirY;
		                    } while(spielfeld[tempX][tempY].getSteinFarbe() != farbeConquerer && spielfeld[tempX][tempY].getSteinFarbe() != 0);
		                }
		            }
				}
			}
		}
		return anzUmgedreht;
	}
	
	/**
	 * Diese Methode ueberprueft, ob fuer den aktuellen Spieler Steine setzbar sind.
	 * @return Gibt zurueck, ob eine Legung der Steine ausfuehrbar ist.
	 */
	private boolean steinLegungMoeglich() {
		boolean steinLegungMoeglichBool = false;
		for(int x=0;x<8;x++) {
			for(int y=0;y<8;y++) {
				if(spielfeld[x][y].getSteinFarbe()==0) {
					int[] xDirArr = {-1, 0, 1};
					int[] yDirArr = {-1, 0, 1};
					int anzUmgedreht = 0;
					
					for(int dirY:yDirArr) {
						for(int dirX:xDirArr) {
							if(!(dirY==0 && dirX==0)) {
								int tempX = x;
								int tempY = y;
								boolean inbounds = true;
								boolean keeprunning = true;
								
								do {
									tempX += dirX;
									tempY += dirY;
									if(tempX < 0 || tempX > 7 || tempY < 0 || tempY > 7) {
										inbounds = false;
									} else if(!(spielfeld[tempX][tempY].getSteinFarbe() != aktSpieler && spielfeld[tempX][tempY].getSteinFarbe() != 0)) {
										keeprunning = false;
									}
								} while(keeprunning && inbounds);
								
								if(inbounds) {
					                if(spielfeld[tempX][tempY].getSteinFarbe()==aktSpieler) {
					                    tempX -= dirX;
					                    tempY -= dirY;
					                    
					                    do {
					                    	if(!(tempX==x && tempY==y)) {
					                    		anzUmgedreht++;
					                    	} else {
					                    		break;
					                    	}
					                        tempX -= dirX;
					                        tempY -= dirY;
					                    } while(spielfeld[tempX][tempY].getSteinFarbe() != aktSpieler && spielfeld[tempX][tempY].getSteinFarbe() != 0);
					                }
					            }
							}
						}
					}
					if(anzUmgedreht>0) {
						steinLegungMoeglichBool = true;
						break;
					}
				}
			}
		}
		return steinLegungMoeglichBool;
	}
	
	/**
	 * Diese Methode wertet das Spiel aus und fragt, ob eine neue Partie gestartet werden soll.
	 */
	private void auswertung() {
		int pktSchwarz = 0;
		int pktWeiss = 0;
		for(Einzelzelle[] ezarr:spielfeld) {
			for(Einzelzelle ez:ezarr) {
				if(ez.getSteinFarbe()==1) {
					pktSchwarz++;
				} else if(ez.getSteinFarbe()==2) {
					pktWeiss++;
				}
			}
		}
		if(pktSchwarz>pktWeiss) {
			JOptionPane.showMessageDialog(null, "Schwarz gewinnt die Partei mit "+pktSchwarz+":"+pktWeiss+" Steinen.", "Schwarz gewinnt", JOptionPane.INFORMATION_MESSAGE);
		} else if(pktWeiss>pktSchwarz) {
			JOptionPane.showMessageDialog(null, "Weiß gewinnt die Partei mit "+pktWeiss+":"+pktSchwarz+" Steinen.", "Weiß gewinnt", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Beide Spieler erreichten die gleiche Anzahl an Steinen.", "Unentschieden", JOptionPane.INFORMATION_MESSAGE);
		}
		
		int dialogneustart = JOptionPane.showConfirmDialog(null, "Möchtest Du eine neue Runde starten?", "Neue Runde?", JOptionPane.YES_NO_OPTION);
        if(dialogneustart == 0) {
        	neuesSpiel();
        } else {
        	System.exit(0);
        }
	}
	
	/**
	 * Diese Methode startet ein neues Spiel.
	 */
	private void neuesSpiel() {
		frame1.dispose();
		new ReversiGUI();
	}
	
	public static void main(String[] args) {
		new ReversiGUI();
	}
}