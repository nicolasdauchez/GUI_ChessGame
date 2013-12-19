/**
 * 
 */
package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import Chess.ChessGameWidget;
import Chess.Pawn;
import Chess.Position;
import Chess.Rules;
import Chess.eColor;
import Chess.ePawns;

/**
 * Program entry point. 
 * Handles general UI.
 * @author NaiKo
 */
public class Main extends JFrame implements ActionListener {
	/**
	 * Class' serial version
	 */
	private static final long serialVersionUID = 1L;
	
	// Constructor
	/**
	 * Default Constructor
	 */
	public Main() {		
		// set the window size
		setSize(860,730);
		// set the title of the window
		setTitle("Our ChessGame");
		// set the default close operation
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// change window look to OS style
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {}
		// Init file chooser
		fc = new JFileChooser();
		// init all panels
		initPanels();
		// handle window's close event
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// true if user still wants to quit game (having exported game or not), false if quit was cancelled
				if (askSaveBeforeQuit())
					System.exit(0);
			}
		});
	}

	// Program's main
	/**
	 * Program entry point
	 * @param args program arguments
	 */
	public static void main(String[] args) {
		Main window = new Main();
		window.setVisible(true);
	}

	// Methods
	/**
	 * Initialazes all panels
	 */
	private void initPanels() {
		// main panel with BorderLayout
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());
		// menu
		createMenu();		
		// creates ChessGame widget and add it to ContentPane
		this.widget = new ChessGameWidget(this);
		this.mainPanel.add(this.widget, BorderLayout.CENTER);
		getContentPane().add(mainPanel);		
		// init all subPanels
		initRightPanel();
		initLeftPanel();
		initBottomPanel();		
	}
	/**
	 * Initializes window's menu
	 */
	private void createMenu() {
		menu = new JMenuBar();
		fileMenu = new JMenu("File");
		importe = new JMenuItem("Import...");
		exporte = new JMenuItem("Export...");
		exit = new JMenuItem("Exit");
		fileMenu.add(importe);
		fileMenu.add(exporte);
		fileMenu.add(exit);
		menu.add(fileMenu);
		importe.addActionListener(this);
		exporte.addActionListener(this);
		exit.addActionListener(this);
		optionsMenu = new JMenu("Options");
		optionCastling = new JCheckBoxMenuItem("Castling");
		optionPromotion = new JCheckBoxMenuItem("Promotion");
		optionEnPassant = new JCheckBoxMenuItem("En passant");
		optionCastling.setSelected(true);
		optionPromotion.setSelected(true);
		optionEnPassant.setSelected(true);
		optionsMenu.add(optionCastling);
		optionsMenu.add(optionPromotion);
		optionsMenu.add(optionEnPassant);
		optionCastling.addActionListener(this);
		optionPromotion.addActionListener(this);
		optionEnPassant.addActionListener(this);
		menu.add(optionsMenu);
		optionsMenu.addSeparator();
		ButtonGroup lookGroup = new ButtonGroup();
		optionRadioNativeLook = new JRadioButtonMenuItem("Set Native Look");
		optionRadioSystemLook = new JRadioButtonMenuItem("Set System Look");
		optionRadioSystemLook.setSelected(true);
		lookGroup.add(optionRadioNativeLook);
		lookGroup.add(optionRadioSystemLook);
		optionsMenu.add(optionRadioNativeLook);
		optionsMenu.add(optionRadioSystemLook);
		optionRadioNativeLook.addActionListener(this);
		optionRadioSystemLook.addActionListener(this);
		mainPanel.add(menu, BorderLayout.PAGE_START);

	}
	/**
	 * Bottom panel with game status message
	 */
	private void initBottomPanel() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		Dimension bottomPanelDim = bottomPanel.getPreferredSize();
		bottomPanelDim.height = 20;
		bottomPanel.setPreferredSize(bottomPanelDim);
		this.mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
		this.gameStatusMsgLabel = new JLabel("", JLabel.CENTER);
		gameStatusMsgLabel.setOpaque(true);
		gameStatusMsgLabel.setForeground(Color.red);
		gameStatusMsgLabel.setFont(new Font("Arial", Font.BOLD, 16));
		bottomPanel.add(gameStatusMsgLabel);
		this.mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
	}
	/**
	 * Left panel with game informations
	 */
	private void initLeftPanel() {
		this.leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
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
	/**
	 * Right panel with dead pieces & history buttons
	 */
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
	/**
	 * Changes game statut message displayed in window
	 * @param message Statut game message to display in window
	 */
	public void changeStatutMsg(String message) {
		this.gameStatusMsgLabel.setText(message);
	}
	/**
	 * Updates player turn label in window
	 * @param color	Current player's color
	 */
	public void changePlayerTurn(eColor color) {
		this.playerTurnLabel.setText((color == eColor.Black) ? ("BLACK") : ("WHITE"));
	}
	/**
	 * Updates player's dead pieces panel
	 * @param currentPlayerColor	Current player's color
	 * @param eatenPawns			List of player's dead pieces
	 */
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
		eatenPanel.revalidate();
		eatenPanel.repaint();
	}
	/**
	 * Asks user if a castling move is required
	 * @return 
	 */
	public boolean askCastling() {
		// open confirm dialog for castling
		int result = JOptionPane.showConfirmDialog(this, "Do you want to perform a castling?", "Castling move detected", JOptionPane.YES_NO_OPTION);
		return (result == 0);
	}
	/**
	 * Asks user if promotion move is required
	 * @return User's chosen piece for promotion
	 */
	public ePawns askPromotion() {
		// Promotion's possible pieces
		Object[] choices = {ePawns.QUEEN, ePawns.ROOK, ePawns.BISHOP, ePawns.KNIGHT};
		// Open dialog asking user which piece to use for promotion move
		ePawns selectedPiece = (ePawns)JOptionPane.showInputDialog(this, 
						"Choose promotion piece for your promoted pawn:",
						"Promotion detected!", 
						JOptionPane.QUESTION_MESSAGE,
						null, 
						choices, choices[0]);
		return selectedPiece;	
	}
	/**
	 * Handles user clicks
	 * @Override
	 */
	public void actionPerformed(ActionEvent arg0) {
		Object objClicked = arg0.getSource();
		// reset button clicked
		if (objClicked == this.resetBtn)
			handleResetClicked();
		// history's go back button clicked
		else if (objClicked == this.goBackBtn)
			handleGoBack();
		// history's go forward button clicked
		else if (objClicked == this.goForwardBtn)
			handleGoForward();
		// import menu clicked
		else if (objClicked == this.importe) {
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
				this.widget.importGame(file.getAbsolutePath());
			}
		}
		// export menu clicked
		else if (objClicked == this.exporte) {
			handleExport();
		}
		// exit menu clicked
		else if (objClicked == this.exit) {
			// true if user still wants to quit game (having exported game or not), false if quit was cancelled
			if (askSaveBeforeQuit())
				System.exit(0);
		}
		// menu's castling option (un)checked
		else if (objClicked == this.optionCastling) {
			Rules.OptionalRules.setCastling(optionCastling.isSelected());
		}
		// menu's promotion option (un)checked
		else if (objClicked == this.optionPromotion) {
			Rules.OptionalRules.setPromotion(optionPromotion.isSelected());
		}
		// menu's en passant option (un)checked
		else if (objClicked == this.optionEnPassant) {
				Rules.OptionalRules.setEnPassant(optionEnPassant.isSelected());
		}
		// menu's native window look option chosen
		else if (objClicked == this.optionRadioNativeLook) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				 SwingUtilities.updateComponentTreeUI(mainPanel);
				 fc.updateUI();
			}
			catch (Exception e) {}	
		}
		// menu's system window look option chosen
		else if (objClicked == this.optionRadioSystemLook) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				 SwingUtilities.updateComponentTreeUI(mainPanel);
				 fc.updateUI();
			}
			catch (Exception e) {}				
		}
	}
	/**
	 * Asks user to export game before quitting
	 * @return 	True if user quits game, false if he cancels quitting
	 */
	private boolean askSaveBeforeQuit() {
		// opens dialog for asking is game importing is required
		int result = JOptionPane.showConfirmDialog(this, "Do you want to save your game?", "Quit", JOptionPane.YES_NO_CANCEL_OPTION);
		if (result == 2)
			return false;
		if (result == 0) {
			// exports game, return false if nothing was selected
			return handleExport();
		}
		return true;
	}
	/**
	 * Asks user in which file game will be exported
	 */
	private boolean handleExport() {
		// open save dialog
		int returnVal = fc.showSaveDialog(this);
		// exports game
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
			this.widget.exportGame(file.getAbsolutePath());
		}
		else if (returnVal == JFileChooser.CANCEL_OPTION)
			return false;
		return true;
	}
	/**
	 * Goes back in game's history
	 */
	private void handleGoBack() {
		boolean canGoBack = this.widget.goBack();
		if (!canGoBack)
			// disable backward button
			this.enableBackwardButton(false);
	}
	/**
	 * Enables or disables back button
	 * @param b
	 */
	public void enableBackwardButton(boolean b) {
		this.goBackBtn.setEnabled(b);
	}
	/**
	 * Goes forward in game's history
	 */
	private void handleGoForward() {
		// when multiple forward is possible
		if (this.widget.hasManyForward()) {
			// get all forward possibilities
			Collection<Pair<Position, Position>> branches = this.widget.getBranches();
			// create usable array for JOptionPaneshowInputDialog()
			ArrayList<String> possibilities = new ArrayList<String>();
			int i = 0;
			for (Pair<Position, Position> pair : branches) {
				possibilities.add(i + ": " + pair.GetLeft().toString() + " -> " + pair.GetRight().toString());
				++i;
			}
			String[] possibilitiesStr = possibilities.toArray(new String[possibilities.size()]);
			// show dialog for choosing forward possibility
		    String ret = (String)JOptionPane.showInputDialog(this, 
		      "Choose which game branch to play:",
		      "History manager",
		      JOptionPane.QUESTION_MESSAGE,
		      null,
		      possibilitiesStr,
		      possibilitiesStr[0]);
		    // user choose one possibility
		    if (ret != null) {
		    	int index = ret.charAt(0) - '0';
		    	this.widget.goForward(index);
		    }
		}
		// when only one forward is possible
		else {
			this.widget.goForward();
		}
		// no more forward posible: disable forward button
		if (!this.widget.canGoForward())
			this.enableForwardButton(false);
	}
	/**
	 * Enables or disables forward button
	 * @param b
	 */
	public void enableForwardButton(boolean b) {
		this.goForwardBtn.setEnabled(b);
	}
	/**
	 * Resets game
	 */
	private void handleResetClicked() {
		// ask user for resetting game
		int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset the game?", "Reset game", JOptionPane.YES_NO_OPTION);
		// reset all window components
		if (result == 0) {
			this.widget.resetGame();
			whiteEatenPanel.removeAll();
			whiteEatenPanel.updateUI();
			blackEatenPanel.removeAll();
			blackEatenPanel.updateUI();
			changeStatutMsg("");
			this.goBackBtn.setEnabled(false);
			this.goForwardBtn.setEnabled(false);
		}
	}

	// Private fields
	/**
	 *  Component where the game is being played
	 */
	ChessGameWidget widget;
	/**
	 *  Game status message
	 */
	JLabel gameStatusMsgLabel;
	/**
	 *  Label "Player turn"
	 */
	JLabel fixedPlayerTurnLabel;
	/**
	 *  Label for displaying which turn is it
	 */
	JLabel playerTurnLabel;
	/**
	 *  Main panel
	 */
	JPanel mainPanel;
	/**
	 *  Left panel with player turn and reset button
	 */
	JPanel leftPanel;
	/**
	 *  Right panel with dead pieces and history buttons
	 */
	JPanel rightPanel;
	/**
	 *  White's dead pieces panel
	 */
	JPanel whiteEatenPanel;
	/**
	 *  Black's dead pieces panel
	 */
	JPanel blackEatenPanel;
	/**
	 *  Reset button
	 */
	JButton resetBtn;
	/**
	 *  Go back button
	 */
	JButton goBackBtn;
	/**
	 *  Go forward button
	 */
	JButton goForwardBtn;
	/**
	 *  File chooser (open and save)
	 */
	JFileChooser fc;
	/**
	 *  Menu bar
	 */
	JMenuBar menu;
	/**
	 *  File menu
	 */
	JMenu fileMenu;
	/**
	 *  File menu's import
	 */
	JMenuItem importe;
	/**
	 *  File menu's export
	 */
	JMenuItem exporte;
	/**
	 *  File menu's exit
	 */
	JMenuItem exit;
	/**
	 *  Options menu
	 */
	JMenu optionsMenu;
	/**
	 *  Options menu's castling checkbox
	 */
	JCheckBoxMenuItem optionCastling;
	/**
	 *  Options menu's promotion checkbox
	 */
	JCheckBoxMenuItem optionPromotion;
	/**
	 *  Options menu's en passant checkbox
	 */
	JCheckBoxMenuItem optionEnPassant;
	/**
	 *  Options menu's native look radio button
	 */
	JRadioButtonMenuItem optionRadioNativeLook;
	/**
	 *  Options menu's system look radio button
	 */
	JRadioButtonMenuItem optionRadioSystemLook;
}

















