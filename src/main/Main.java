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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import sun.java2d.loops.ScaledBlit;
import Chess.ChessGameWidget;
import Chess.Pawn;
import Chess.eColor;
import Chess.ePawns;
/**
 * @author Lumy-
 *
 */
public class Main extends JFrame implements ActionListener {
	
	// program entry point
	public static void main(String[] args) {
		Main window = new Main();
		window.setVisible(true);
	}



	private JButton resetBtn;

	public Main() {		
		// set the window size
		setSize(860,700);
		// set the title of the window
		setTitle("Our ChessGame");
		// set the default close operation
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		initPanels();
	}

	private void initPanels() {
		// main panel with BorderLayout
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());

		// creates ChessGame widget and add it to ContentPane
		this.widget = new ChessGameWidget(this);

//		this.widget.setBorder(BorderFactory.createLineBorder(Color.green));
		
//		this.widget.setMaximumSize(new Dimension(640,640));
//		this.widget.setMinimumSize(new Dimension(640,640));
//		this.widget.setPreferredSize(new Dimension(640,640));
		this.mainPanel.add(this.widget, BorderLayout.CENTER);
		
		initRightPanel();
		initLeftPanel();
		initBottomPanel();
		
		getContentPane().add(mainPanel);		
	}

	private void initBottomPanel() {
		// Game status message
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		Dimension bottomPanelDim = bottomPanel.getPreferredSize();
		bottomPanelDim.height = 20;
		bottomPanel.setPreferredSize(bottomPanelDim);
//		bottomPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		this.mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
		this.gameStatusMsgLabel = new JLabel("", JLabel.CENTER);
		gameStatusMsgLabel.setOpaque(true);
		gameStatusMsgLabel.setForeground(Color.red);
//		msgLabel.setMaximumSize(new Dimension(800, 20));
//		msgLabel.setMinimumSize(new Dimension(800, 20));
//		msgLabel.setPreferredSize(new Dimension(800, 20));
		bottomPanel.add(gameStatusMsgLabel);
		this.mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
	}

	private void initLeftPanel() {
		// left panel with game informations
		this.leftPanel = new JPanel();
//		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//		leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		// "player turn" Panel
		JPanel playerTurnPanel = new JPanel();
		playerTurnPanel.setLayout(new GridBagLayout());
		JPanel subLeftPanel = new JPanel();
		subLeftPanel.setLayout(new BoxLayout(subLeftPanel, BoxLayout.Y_AXIS));
		this.fixedPlayerTurnLabel = new JLabel("Player turn:", SwingConstants.CENTER);
		this.playerTurnLabel = new JLabel("", SwingConstants.CENTER);
		subLeftPanel.add(this.fixedPlayerTurnLabel);
		subLeftPanel.add(this.playerTurnLabel);
		playerTurnPanel.add(subLeftPanel);
		this.leftPanel.add(playerTurnPanel, BorderLayout.CENTER);
		
		// reset button
		this.resetBtn = new JButton("Reset");
		this.resetBtn.addActionListener(this);
		this.leftPanel.add(resetBtn, BorderLayout.PAGE_END);
		
		
		this.mainPanel.add(leftPanel, BorderLayout.WEST);
	}

	
	private void initRightPanel() {
		// right panel with dead pieces
		this.rightPanel = new JPanel();
		BorderLayout rightPanelBL = new BorderLayout();
		rightPanel.setLayout(rightPanelBL);
		rightPanel.setBorder(new EmptyBorder(5,5,5,5));

		this.whiteEatenPanel = new JPanel();
		this.whiteEatenPanel.setPreferredSize(new Dimension(105,250));
		this.whiteEatenPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		Border eatenBorder = new LineBorder(Color.black);
		TitledBorder eatenBorder2 = BorderFactory.createTitledBorder(eatenBorder, "Eaten pieces");
		this.whiteEatenPanel.setBorder(eatenBorder2);
		
		this.blackEatenPanel = new JPanel();
		this.blackEatenPanel.setPreferredSize(new Dimension(105,250));
		this.blackEatenPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		Border eatenBorder3 = new LineBorder(Color.black);
		TitledBorder eatenBorder4 = BorderFactory.createTitledBorder(eatenBorder2, "Eaten pieces");
		this.blackEatenPanel.setBorder(eatenBorder4);
		
		this.rightPanel.add(whiteEatenPanel, BorderLayout.PAGE_END);
		this.rightPanel.add(blackEatenPanel, BorderLayout.PAGE_START);
		
		this.mainPanel.add(rightPanel, BorderLayout.EAST);
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
		int result = JOptionPane.showConfirmDialog(this, "Do you want to perform a castling?", "Castling move detected", JOptionPane.YES_NO_OPTION);
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

	// events
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// reset button clicked
		if (arg0.getSource() == this.resetBtn) {
			handleResetClicked();
		}
	}
	
	private void handleResetClicked() {
		
		int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset the game?", "Reset game", JOptionPane.YES_NO_OPTION);
		
		if (result == 0) {
			this.widget.resetGame();
			whiteEatenPanel.removeAll();
			whiteEatenPanel.updateUI();
			blackEatenPanel.removeAll();
			blackEatenPanel.updateUI();
			changeStatutMsg("");
		}
	}


	/** private fields **/
	ChessGameWidget widget;	// where the game is being played
	JLabel gameStatusMsgLabel;
	JLabel fixedPlayerTurnLabel;
	JLabel playerTurnLabel;
	JPanel mainPanel;
	JPanel leftPanel;
	JPanel rightPanel;
	JPanel whiteEatenPanel;
	JPanel blackEatenPanel;

}

















