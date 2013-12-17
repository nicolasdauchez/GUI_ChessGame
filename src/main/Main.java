/**
 * 
 */
package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
import Chess.Position;
import Chess.eColor;
import Chess.ePawns;
/**
 * @author Lumy-
 *
 */
public class Main extends JFrame implements ActionListener {



	public Main() {		
		// set the window size
		setSize(860,730);
		// set the title of the window
		setTitle("Our ChessGame");
		// set the default close operation
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Create a file chooser
		fc = new JFileChooser();
		
		initPanels();
	}

	// program entry point
	public static void main(String[] args) {
		Main window = new Main();
		window.setVisible(true);
	}
	
	private void initPanels() {
		// main panel with BorderLayout
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());

		// menu
		menu = new JMenuBar();
		item = new JMenu("File");
		importe = new JMenuItem("Import");
		exporte = new JMenuItem("Export");
		item.add(importe);
		item.add(exporte);
		menu.add(item);
		importe.addActionListener(this);
		exporte.addActionListener(this);
		
//		//Create a file chooser
//		final JFileChooser fc = new JFileChooser();
//		int returnVal = fc.showOpenDialog(aComponent);
		
		mainPanel.add(menu, BorderLayout.PAGE_START);
		
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

	// Bottom panel with game status message
	private void initBottomPanel() {
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

	// left panel with game informations
	private void initLeftPanel() {
		this.leftPanel = new JPanel();
//		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//		leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.mainPanel.add(leftPanel, BorderLayout.WEST);
		
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
	}

	// right panel with dead pieces & history buttons
	private void initRightPanel() {
		this.rightPanel = new JPanel();
		BorderLayout rightPanelBL = new BorderLayout();
		rightPanel.setLayout(rightPanelBL);
		rightPanel.setBorder(new EmptyBorder(5,5,5,5));
		this.mainPanel.add(rightPanel, BorderLayout.EAST);

		// dead pieces
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
		TitledBorder eatenBorder4 = BorderFactory.createTitledBorder(eatenBorder3, "Eaten pieces");
		this.blackEatenPanel.setBorder(eatenBorder4);
		
		this.rightPanel.add(whiteEatenPanel, BorderLayout.PAGE_END);
		this.rightPanel.add(blackEatenPanel, BorderLayout.PAGE_START);
		
		// history buttons
		JPanel historyPanel = new JPanel();
		Dimension historyPanelDim = historyPanel.getPreferredSize();
		historyPanelDim.width = 105;
		historyPanel.setPreferredSize(historyPanelDim);
		historyPanel.setLayout(new GridBagLayout());

		this.rightPanel.add(historyPanel, BorderLayout.CENTER);
		Border historyBorder = new LineBorder(Color.orange);
		TitledBorder historyBorder2 = BorderFactory.createTitledBorder(historyBorder, "History");
		historyPanel.setBorder(historyBorder2);
		
		goBackBtn = new JButton("<<");
		goBackBtn.setEnabled(false);
		goBackBtn.addActionListener(this);
		
		goForwardBtn = new JButton(">>");
		goForwardBtn.setEnabled(false);
		goForwardBtn.addActionListener(this);
		
		GridBagConstraints c = new GridBagConstraints();		
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 3, 20);
		historyPanel.add(goBackBtn, c);
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(3, 20, 0, 0);
		historyPanel.add(goForwardBtn, c);
	}

	public void changeStatutMsg(String message) {
		this.gameStatusMsgLabel.setText(message);
	}

	public void changePlayerTurn(eColor color) {
		this.playerTurnLabel.setText((color == eColor.Black) ? ("BLACK") : ("WHITE"));
	}
	
	public void updateEatenPieces(eColor currentPlayerColor, Collection<Pawn> eatenPawns) {
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
		Object objClicked = arg0.getSource();
		// reset button clicked
		if (objClicked == this.resetBtn)
			handleResetClicked();
		else if (objClicked == this.goBackBtn)
			handleGoBack();
		else if (objClicked == this.goForwardBtn)
			handleGoForward();			
		else if (objClicked == this.importe) {
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
				this.widget.importGame(file.getAbsolutePath());
			}
		}
		else if (objClicked == this.exporte) {
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
				this.widget.exportGame(file.getAbsolutePath());
			}
		}
	}
	
	private void handleGoBack() {
		boolean canGoBack = this.widget.goBack();
		if (!canGoBack)
			this.enableBackwardButton(false);
	}

	public void enableBackwardButton(boolean b) {
		this.goBackBtn.setEnabled(b);
	}

	private void handleGoForward() {
		boolean canGoForward = false;

		if (this.widget.hasManyForward()) {
			Collection<Pair<Position, Position>> branches = this.widget.getBranches();
//			int branchesNb = this.widget.getBranchesNb();
			ArrayList<String> possibilities = new ArrayList<String>();
			
			int i = 0;
			for (Pair<Position, Position> pair : branches) {
				possibilities.add(i + ": " + pair.GetLeft().toString() + " -> " + pair.GetRight().toString());
				++i;
			}
			String[] possibilitiesStr = possibilities.toArray(new String[possibilities.size()]);
			
		    String ret = (String)JOptionPane.showInputDialog(this, 
		      "Choose which game branch to play:",
		      "History manager",
		      JOptionPane.QUESTION_MESSAGE,
		      null,
		      possibilitiesStr,
		      possibilitiesStr[0]);
//		      branches,
//		      branches[0]);
		    
		    if (ret != null) {
		    	int index = ret.charAt(0) - '0';
		    	canGoForward = this.widget.goForward(index);
		    }
		}
		else {
			canGoForward = this.widget.goForward();
		}
			
		if (!canGoForward)
			this.enableForwardButton(false);
	}

	public void enableForwardButton(boolean b) {
		this.goForwardBtn.setEnabled(b);
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
	JButton resetBtn;
	JButton goBackBtn;
	JButton goForwardBtn;
	JFileChooser fc;
	JMenuItem importe;
	JMenuItem exporte;
	JMenuBar menu;
	JMenu item;
	

}

















