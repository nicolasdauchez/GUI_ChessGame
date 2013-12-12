/**
 * 
 */
package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import Chess.ChessGameWidget;
import Chess.eColor;
/**
 * @author Lumy-
 *
 */
public class Main extends JFrame {
	
	public Main() {		
		// set the window size
		setSize(850,715);
		// set the title of the window
		setTitle("Our ChessGame");
		// set the default close operation
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// main panel with BorderLayout
		JPanel mainPanel = new JPanel();
		BorderLayout bdrl = new BorderLayout();
		mainPanel.setLayout(bdrl);

		// creates ChessGame widget and add it to ContentPane
		this.widget = new ChessGameWidget(this);
//		this.widget.setMaximumSize(new Dimension(640,640));
//		this.widget.setMinimumSize(new Dimension(640,640));
//		this.widget.setPreferredSize(new Dimension(640,640));
		mainPanel.add(this.widget, BorderLayout.CENTER);

		// [Label] Game status message
		this.gameStatusMsgLabel = new JLabel(" ", JLabel.CENTER);
//		msgLabel.setMaximumSize(new Dimension(800, 20));
//		msgLabel.setMinimumSize(new Dimension(800, 20));
//		msgLabel.setPreferredSize(new Dimension(800, 20));
		mainPanel.add(gameStatusMsgLabel, BorderLayout.SOUTH);

		
		// right panel with dead pieces
		this.rightPanel = new JPanel();
		BorderLayout rightPanelBL = new BorderLayout();
		rightPanel.setLayout(rightPanelBL);

//		JPanel whiteDeadsPanel = new JPanel();
		JPanel blackDeadsPanel = new JPanel();
//		rightPanel.add(whiteDeadsPanel, BorderLayout.PAGE_START);
		rightPanel.add(blackDeadsPanel, BorderLayout.PAGE_END);
				
//		JLabel image = new JLabel(new ImageIcon( "src/Images/WhitePawn.png"));
		JLabel image2 = new JLabel(new ImageIcon( "src/Images/WhitePawn.png"));
//		whiteDeadsPanel.add(image);
		blackDeadsPanel.add(image2);
		
		DeadsPanel deads = new DeadsPanel();
		rightPanel.add(deads, BorderLayout.CENTER);
		mainPanel.add(rightPanel, BorderLayout.EAST);
		
		
		// left panel with game informations
		this.leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JPanel subLeftPanel = new JPanel();
		subLeftPanel.setLayout(new BoxLayout(subLeftPanel, BoxLayout.Y_AXIS));
		
		this.fixedPlayerTurnLabel = new JLabel("Player turn:", SwingConstants.CENTER);
		this.playerTurnLabel = new JLabel(" ", SwingConstants.CENTER);
		
		subLeftPanel.add(this.fixedPlayerTurnLabel);
		subLeftPanel.add(this.playerTurnLabel);

		this.leftPanel.add (subLeftPanel);
		
		mainPanel.add(leftPanel, BorderLayout.WEST);
		
		
		getContentPane().add(mainPanel);
//		getContentPane().add(widget);
	}

	public void changeStatutMsg(String message) {
		this.gameStatusMsgLabel.setText(message);
	}

	public void changePlayerTurn(eColor color) {
		this.playerTurnLabel.setText((color == eColor.Black) ? ("BLACK") : ("WHITE"));
	}
	
	public static void main(String[] args) {
		Main window = new Main();
		window.setVisible(true);
	}

	/** private fields **/
	ChessGameWidget widget;	// where the game is being played
	JLabel gameStatusMsgLabel;
	JLabel fixedPlayerTurnLabel;
	JLabel playerTurnLabel;
	JPanel leftPanel;
	JPanel rightPanel;
		
}

















