/**
 * 
 */
package main;

import javax.swing.JFrame;

import Chess.ChessGame;
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
		setSize(655,675);
		// set the title of the window
		setTitle("Our ChessGame");
		// set the default close operation
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// creates ChessGame widget and add it to ContentPane
		this.widget = new ChessGameWidget();
		getContentPane().add(this.widget);
	}
	
	public static void main(String[] args) {
		Main window = new Main();
		window.setVisible(true);
	}

	/** private fields **/
	ChessGameWidget widget;	// where the game is being played

}
