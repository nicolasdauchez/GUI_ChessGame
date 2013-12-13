/**
 * 
 */
package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import sun.java2d.loops.ScaledBlit;
import Chess.ChessGameWidget;
import Chess.Pawn;
import Chess.eColor;
import Chess.ePawns;
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
		rightPanel.setBorder(new EmptyBorder(5,5,5,5));

		this.whiteEatenPanel = new JPanel();
		this.whiteEatenPanel.setPreferredSize(new Dimension(95,250));
		this.whiteEatenPanel.setLayout(new FlowLayout());
		this.whiteEatenPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.whiteEatenPanel.setBackground(Color.LIGHT_GRAY);
		
		this.blackEatenPanel = new JPanel();
		this.blackEatenPanel.setPreferredSize(new Dimension(95,250));
		this.blackEatenPanel.setLayout(new FlowLayout());
		this.blackEatenPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.blackEatenPanel.setBackground(Color.LIGHT_GRAY);
		
		this.rightPanel.add(whiteEatenPanel, BorderLayout.PAGE_END);
		this.rightPanel.add(blackEatenPanel, BorderLayout.PAGE_START);
		
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

		this.leftPanel.add(subLeftPanel);
		
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
	
	public void updateEatenPieces(eColor currentPlayerColor, Collection<Pawn> eatenPawns) {
		if (eatenPawns.size() > 0) {
			JPanel eatenPanel = (currentPlayerColor == eColor.Black) ? (blackEatenPanel) : (whiteEatenPanel);
			Iterator<Pawn> it = eatenPawns.iterator();
			eatenPanel.removeAll();
			while (it.hasNext()) {
				Pawn curEatenPawn = it.next();
				eColor eatenColor = curEatenPawn.GetColor();
				ePawns eatenClasse = curEatenPawn.GetClass();	
				if (eatenColor != currentPlayerColor) {
					
					Image curEatenPawnImg = this.widget.getPieceImage(eatenColor, eatenClasse);
					Image scaledImg = curEatenPawnImg.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
					ImageIcon icon = new ImageIcon(scaledImg);
					JLabel imgLbl = new JLabel(icon);
					eatenPanel.add(imgLbl);
				}
			}
		}
	}
	
	public boolean askCastling() {
		int result = JOptionPane.showConfirmDialog(this, "Du you want to perform a castling?", "Castling move detected", JOptionPane.YES_NO_OPTION);
		return (result == 0);
	}

	public ePawns askPromotion() {

		Object[] choices = {ePawns.QUEEN, ePawns.ROOK, ePawns.BISHOP, ePawns.KNIGHT};
		ePawns selectedPiece = (ePawns)JOptionPane.showInputDialog(this, 
						"Choose promotion piece for your promoted pawn:",
						"Promotion detected!", 
						JOptionPane.QUESTION_MESSAGE,
						null, 
						choices, choices[0]);
		return selectedPiece;	
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
	JPanel whiteEatenPanel;
	JPanel blackEatenPanel;
		
}

















