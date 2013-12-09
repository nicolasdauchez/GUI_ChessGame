/**
 * 
 */
package main;

import javax.swing.JFrame;
import Chess.ChessGameWidget;
/**
 * @author Lumy-
 *
 */
public class Main extends JFrame {

	/**
	 * @param args
	 */
	
	public Main() {		
		// set the window size
		setSize(800,715);
		// set the title of the window
		setTitle("Our ChessGame");
		// set the default close operation
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// creates ChessGame widget and add it to ContentPane
		this.widget = new ChessGameWidget();
		
//		javax.swing.JPanel panel = new javax.swing.JPanel();
//		javax.swing.BoxLayout bxlo = new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS);
//		panel.setLayout(bxlo);
//
//		this.widget.setMaximumSize(new java.awt.Dimension(640,640));
//		this.widget.setMinimumSize(new java.awt.Dimension(640,640));
//		this.widget.setPreferredSize(new java.awt.Dimension(640,640));
//
//		panel.add(this.widget);
//		javax.swing.JLabel label = new javax.swing.JLabel("Blah djflkd jkl  djlkdjkldjld  dl jd  d dgj d dgdfgdgdg dddg");

//		label.setMaximumSize(new java.awt.Dimension(20, 500));
//		label.setMinimumSize(new java.awt.Dimension(20, 500));
//		label.setPreferredSize(new java.awt.Dimension(20, 500));

//		panel.add(label);
//		getContentPane().add(panel);
		getContentPane().add(widget);
	}
	
	public static void main(String[] args) {
		Main window = new Main();
		window.setVisible(true);
	}

	/** private fields **/
	ChessGameWidget widget;	// where the game is being played

}
