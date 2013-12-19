package Chess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import main.Main;
import main.Pair;

/**
 * Handles chess board UI. 
 * Uses {@link ChessGame} for game logic.
 * @author NaiKo
 */
public class ChessGameWidget extends JComponent implements MouseListener{
	/**
	 * Class version
	 */
	private static final long serialVersionUID = 1L;
	
	// Constructor
	/**
	 * Constructor, needs a Main class to work
	 * @param _main The JFrame which contains this ChessGameWidget object
	 */
	public ChessGameWidget(Main _main) {
		// initializes game logic
		this.game = new ChessGame();
		// initializes game colors
		this.black = new Color(0, 0, 0);
		this.brown_dark = new Color(184, 115, 51);
		this.brown_light = new Color(222, 184, 135);
		this.selectionned = new Color(0, 0, 255, 75);
		// initializes pieces images
		loadPieces();
		// reference to the Main class
		this.main = _main;
		// creates a map with all game state's messages corresponding to eGameState/eMoveState
		initMessages();
		// update game board
		repaint();
		// adds mouse listener
		addMouseListener(this);
	}

	// Methods
	/**
	 * Initializes all game messages displayed in window
	 */
	private void initMessages() {
		// eGameState messages
		messagesGame = new HashMap<eGameState, String>();
		messagesGame.put(eGameState.CHECK_KING_B, "Black King is in CHECK!");		
		messagesGame.put(eGameState.CHECK_KING_W, "White King is in CHECK!");
		messagesGame.put(eGameState.CHECK_MATE_B, "Black King is in CHECK MATE! Game over.");
		messagesGame.put(eGameState.CHECK_MATE_W, "White King is in CHECK MATE! Game over.");
		messagesGame.put(eGameState.DRAW , "Stalemate! Game over.");
		messagesGame.put(eGameState.NEXT, "");
		messagesGame.put(eGameState.SAME, "");
		// eMoveState messages
		messagesMove = new HashMap<eMoveState, String>();
		messagesMove.put(eMoveState.FAIL_CHECK, "You can't move your piece here: your king would be/stay in check.");
		messagesMove.put(eMoveState.FAIL_PAWNS_BACKWARD, "Pawns can't eat forward.");
		messagesMove.put(eMoveState.FAIL_PAWNS_EAT_FORWARD, "Pawns can't go or eat backward.");
		messagesMove.put(eMoveState.FAIL_UNAUTHORIZED, "You can't move your piece here: unauthorized move.");
		messagesMove.put(eMoveState.CASTLING, "");
		messagesMove.put(eMoveState.FAIL_CLASS_UNKNOWN, "");
		messagesMove.put(eMoveState.SUCCESS, "");
		messagesMove.put(eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED, "");
	}
	/**
	 * Loads all pieces images
	 */
	private void loadPieces() {
		// ePawns to Image hashtables
		this.pieces_images_white = new Hashtable<ePawns, BufferedImage>();
		this.pieces_images_black = new Hashtable<ePawns, BufferedImage>();
		// load all pawns' images
		try {
			this.pieces_images_white.put(ePawns.PAWN, ImageIO.read(new File("src/Images/WhitePawn.png")));
			this.pieces_images_white.put(ePawns.BISHOP, ImageIO.read(new File("src/Images/WhiteBishop.png")));
			this.pieces_images_white.put(ePawns.ROOK, ImageIO.read(new File("src/Images/WhiteRook.png")));
			this.pieces_images_white.put(ePawns.QUEEN, ImageIO.read(new File("src/Images/WhiteQueen.png")));
			this.pieces_images_white.put(ePawns.KING, ImageIO.read(new File("src/Images/WhiteKing.png")));
			this.pieces_images_white.put(ePawns.KNIGHT, ImageIO.read(new File("src/Images/WhiteKnight.png")));
			this.pieces_images_black.put(ePawns.KNIGHT, ImageIO.read(new File("src/Images/BlackKnight.png")));
			this.pieces_images_black.put(ePawns.KING, ImageIO.read(new File("src/Images/BlackKing.png")));
			this.pieces_images_black.put(ePawns.QUEEN, ImageIO.read(new File("src/Images/BlackQueen.png")));
			this.pieces_images_black.put(ePawns.ROOK, ImageIO.read(new File("src/Images/BlackRook.png")));
			this.pieces_images_black.put(ePawns.BISHOP, ImageIO.read(new File("src/Images/BlackBishop.png")));
			this.pieces_images_black.put(ePawns.PAWN, ImageIO.read(new File("src/Images/BlackPawn.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 *  Needed by MouseListener interface, not used here
	 *  @Override
	 */
	public void mouseClicked(MouseEvent e) { }
	/**
	 *  Needed by MouseListener interface, not used here
	 *  @Override
	 */
	public void mouseEntered(MouseEvent e) { }
	/**
	 *  Needed by MouseListener interface, not used here
	 *  @Override
	 */
	public void mouseExited(MouseEvent e) { }
	/**
	 *  Needed by MouseListener interface, not used here
	 *  @Override
	 */
	public void mousePressed(MouseEvent e) { }
	/**
	 *  The mouse released event is the only one used in this chess game widget
	 *  @Override
	 */
	public void mouseReleased(MouseEvent e) {
		// handles click only if current game state is not CheckMate
		if (this.game.isCheckMat() == eColor.None && !this.game.isDraw()) {
			// get click position
			Position clickedPos = getMousePosition(e);
			// mouse was clicked within game board limits
			if (!this.game.getBoardGame().isOutside(clickedPos)) {
				// nothing selected yet : first click
				if (posFirstClick == null) {
					this.posFirstClick = clickedPos;
					// -> first click doesn't count if it's on an empty square
					// -> first click doesn't count if not current player's pieces clicked
					if (!this.game.getBoardGame(). contains(this.posFirstClick)
						|| this.game.getBoardGame().getObstacleCase(this.posFirstClick) != this.game.GetTurn())
						this.posFirstClick = null;						
				}
				// one piece is already selected : second click
				else {
					this.posSecondClick = clickedPos;
					// handles second click only if it's not on same square as first click
					if (!this.posFirstClick.equals(this.posSecondClick))
					{
						// saving some stats
						int eatenPiecesNb = this.game.getBoardGame().GetEaten().size();
						eColor currentPlayer = this.game.GetTurn();					
						// checks move validity
						Pair<eMoveState, eGameState> moveAccepted = this.game.catchEvent(new Position(posFirstClick), new Position(posSecondClick));
						// updates game board (piece moving or text explaining why not)
						handleMove(moveAccepted);
						// updates eaten pieces panel if necessary
						if (this.game.getBoardGame().GetEaten().size() > eatenPiecesNb)
							this.main.updateEatenPieces(currentPlayer, this.game.getBoardGame().GetEaten());
					}
					// second click not handles because it's on same square as firest click
					else
						this.posFirstClick = null;
					// always reset second click
					this.posSecondClick = null;
				}
				// updates game board
				repaint();
			}
		}
	}
	/**
	 * Handles move changes
	 * @param moveAccepted Pair of Enum indicating move & game status
	 */
	private void handleMove(Pair<eMoveState, eGameState> moveAccepted) {
		// gets move status and game status after processing clicks positions
		this.message = "";
		eMoveState moveState = moveAccepted.GetLeft();
		eGameState gameState = moveAccepted.GetRight();
		// move was accepted by game
		if (moveState == eMoveState.SUCCESS) {
			//reinitialize click positions
			this.posFirstClick = null;
			// update game status message
			message = messagesGame.get(gameState);
			// check if a pawn managed to get a Promotion
			if (Rules.OptionalRules.Promotion)
				handlePromotion();
			// enable history's go back back button
			this.main.enableBackwardButton(true);
		}
		// move was not accepted by game
		else {
			// user selected another of his pieces
			if (moveState == eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED)
				this.posFirstClick = this.posSecondClick;
			// user may wants to perform a castling
			else if (moveState == eMoveState.CASTLING)
				handleCastling();
			// update game statut message
			message = messagesMove.get(moveState);
		}
	}
	/**
	 * Promotion detected. Asks user for performing Promotion and does it if he wants so
	 */
	private void handlePromotion() {
		// get all alive pieces
		Collection<Pawn> alivePieces = this.game.getBoardGame().getElem();
		if (!alivePieces.isEmpty()) {
			Iterator<Pawn> iterator = alivePieces.iterator();
			while(iterator.hasNext()) {
  		    	Pawn piece = iterator.next();
  		    	ePawns classe = piece.GetClass();
  		    	// pawn case
  		    	if (classe == ePawns.PAWN) {
  		    		eColor color = piece.GetColor();
	    			Position pos = piece.GetPosition();  		 
	    			// pawn has reached opponents first line in board
	    			if ((color == eColor.Black) && (pos.row == 1)
    					|| (color == eColor.White) && (pos.row == 8)) {
	    				// user can make a promotion, so we ask him if he wants to do so
    					ePawns newClasse = this.main.askPromotion();
    					if (newClasse != null) {
    						// performs selected promotion
    						this.game.DoPromotion(pos, newClasse);
    						checkGameStatus();
    						return;
    					}
	    			}
  		    	}
			}
		}
	}
	/**
	 * Castling detected. Asks user for performing Castling and does it if he wants so
	 */
	private void handleCastling() {
		// asks user if he wants to perform a castling
		boolean castlingWanted = this.main.askCastling();
		// yes indeed
		if (castlingWanted) {
			// perform castling in game
			this.game.DoCastling(this.posFirstClick, this.posSecondClick);
			// enable history's go back back button
			this.main.enableBackwardButton(true);
			this.posFirstClick = null;
		}
		// no castling required, only selected piece changes
		else {
			this.posFirstClick = this.posSecondClick;
		}
	}

	private void checkGameStatus() {
		eColor colorCheck = eColor.None;
		if (this.game.isDraw()) {
			this.message = this.messagesGame.get(eGameState.DRAW);
		}
		else if ((colorCheck = this.game.isCheckKing()) != eColor.None) {
			this.message = this.messagesGame.get(
					(colorCheck == eColor.Black) ? eGameState.CHECK_KING_B : (eGameState.CHECK_KING_W));
		}
		else if ((colorCheck = this.game.isCheckMat()) != eColor.None) {
			this.message = this.messagesGame.get(
					(colorCheck == eColor.Black) ? eGameState.CHECK_MATE_B : (eGameState.CHECK_MATE_W));
		}
	}
	/**
	 *  Return a Position object of the mouse current event
	 * @param e MouseEvent object automatically sent to mouseReleased function
	 * @return Position object with corresponding board square clicked
	 */
	private Position getMousePosition(MouseEvent e) {
		// gets click exact position
		int X = e.getX(); 
		int Y = e.getY();
		// creates new Position object
		Position curPos = new Position();
		// fill Position object with click's corresponding row and column
		curPos.row = 8 - (Y / 80);
		curPos.column = (char)('a' + (X / 80));
		return curPos;
	}
	/**
	 *  Repaints the widget when an update of any kind is made
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;		
		// Updates player turn notification
		main.changePlayerTurn(this.game.GetTurn());		
		// Updates game status message
		main.changeStatutMsg(message);
		// re-draws all game board
		drawGrid(g2d);
		drawPieces(g2d);
	}

	/**
	 * Draw all alive pieces on board
	 * @param g2d The Graphics2D automatically sent to paintComponent, in which everything is drawn
	 */
	private void drawPieces(Graphics2D g2d) {
		// get all alive pieces
		Collection<Pawn> alivePieces = this.game.getBoardGame().getElem();
		if (!alivePieces.isEmpty()) {
			// Map var to reference the current player's piecesImages actual Map var
			Map<ePawns, BufferedImage> tmpPiecesImg = null;
			// draws all alive pieces on board
			Iterator<Pawn> iterator = alivePieces.iterator();
			while(iterator.hasNext()) {
  		    	Pawn piece = iterator.next();
  		    	ePawns piece_class = piece.GetClass();
				eColor piece_color = piece.GetColor();
				Position piece_pos = piece.GetPosition();
				// selects right piecesImages Map var corresponding to current player
				if (piece_color == eColor.Black) tmpPiecesImg = pieces_images_black;
				else tmpPiecesImg = pieces_images_white;
				// draw crrentu piece on board with given position
				g2d.drawImage(tmpPiecesImg.get(piece_class), null, (piece_pos.column- 'a')*80, ((8 - piece_pos.row))*80);
			}
		 }
	}

	/**
	 * Draw the board's grid 
	 * @param g2d The Graphics2D automatically sent to paintComponent, in which everything is drawn
	 */
	private void drawGrid(Graphics2D g2d) {
		// board is 8*8 squares long
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// draw square's black border
				g2d.setColor(black);
				g2d.drawRect(i*80, j*80, 80, 80);
				// selects light brown or dark brown color for current square
				if ((i % 2 == 0 && j%2 == 0) || (i % 2 != 0 && j%2 != 0)) g2d.setColor(brown_light);
				else g2d.setColor(brown_dark);
				// draw square
				g2d.fillRect((i*80)+1, (j*80)+1, 79, 79);
			}
		}
		// colors selectionned piece's square
		if (this.posFirstClick != null) {
			g2d.setColor(selectionned);
			g2d.fillRect(((posFirstClick.column - 'a')*80)+1, ((8 - posFirstClick.row)*80), 79, 79);
		}
	}
	
	/**
	 * Returns image corresponding to given color and pawn
	 * @param color		Required pawn's color
	 * @param classe	Required pawn's class
	 * @return			Image of pawn required
	 */
	public Image getPieceImage(eColor color, ePawns classe) {
		if (color == eColor.Black)
			return this.pieces_images_black.get(classe);
		return this.pieces_images_white.get(classe);
	}
	/**
	 * Resets game
	 */
	public void resetGame() {
		// resets game
		this.game.NewGame("Whites", "Blacks");
		// updates board
		repaint();
		// updates current player turn label
		this.main.changePlayerTurn(this.game.GetTurn());
	}
	/**
	 * Goes back in history
	 * @return Boolean indicating if player can go back again after
	 */
	public boolean goBack() {
		// get number of dead pieces
		int eatenPiecesNb = this.game.getBoardGame().GetEaten().size();
		// get current player turn
		eColor currentPlayer = this.game.GetTurn();
		// game succeeded to go back in history
		if (this.game.goBackward()) {
			// unselects selected piece, if any
			this.posFirstClick = null;
			// updates board
			repaint();
			// enables go forward button
			this.main.enableForwardButton(true);
			// updates eaten pieces panel if necessary
			if (this.game.getBoardGame().GetEaten().size() < eatenPiecesNb)
				this.main.updateEatenPieces((currentPlayer == eColor.Black) ? (eColor.White) : (eColor.Black), this.game.getBoardGame().GetEaten());
		}
		return this.game.canGoBackward();
	}
	/**
	 * Goes forward in history
	 * @return Boolean indicating if player can go forward again after
	 */
	public boolean goForward() {
		// get number of dead pieces
		int eatenPiecesNb = this.game.getBoardGame().GetEaten().size();
		// get current player turn
		eColor currentPlayer = this.game.GetTurn();
		// game succeeded to go back in history
		if (this.game.goForward()) {
			// unselects selected piece, if any
			this.posFirstClick = null;
			// updates board
			repaint();
			// enables go back button
			this.main.enableBackwardButton(true);
			// updates eaten pieces panel if necessary
			if (this.game.getBoardGame().GetEaten().size() > eatenPiecesNb)
				this.main.updateEatenPieces(currentPlayer, this.game.getBoardGame().GetEaten());
		}
		return this.game.canGoForward();
	}
	/**
	 * Goes forward in history
	 * @param index		Index of history forward move wanted (in case of several forward possibles)
	 * @return 			Boolean indicating if player can go forward again after
	 */
	public boolean goForward(int index) {
		// get number of dead pieces
		int eatenPiecesNb = this.game.getBoardGame().GetEaten().size();
		// get current player turn
		eColor currentPlayer = this.game.GetTurn();
		// game succeeded to go back in history
		if (this.game.goForward(index)) {
			// unselects selected piece, if any
			this.posFirstClick = null;
			// updates board
			repaint();
			// enables go back button
			this.main.enableBackwardButton(true);
			// updates eaten pieces panel if necessary
			if (this.game.getBoardGame().GetEaten().size() > eatenPiecesNb)
				this.main.updateEatenPieces(currentPlayer, this.game.getBoardGame().GetEaten());
		}
		return this.game.canGoForward();
	}
	/**
	 * Indicate wether player ca go forward in history
	 * @return
	 */
	public boolean canGoForward() {
		return this.game.canGoForward();
	}
	/**
	 * Indicates if multiple forwards are possible
	 * @return True if multiple forwards are possible, false if only one or zero are possible
	 */
	public boolean hasManyForward() {
		// game has forward history, so we return true if there are many possibilities, false if just one possibility
		if (this.game.getForward() != null)
			return this.game.getForward().size() > 1;
		// game has no forward history
		return false;
	}
	/**
	 * Import saved game to be played immediatly
	 * @param path	Path to saved game file to load
	 */
	public void importGame(String path) {
		// imports game, given user's chosen path
		this.game.Import(path);
		// updates board
		repaint();
		// updates all panels, messages, buttons from imported game
		this.main.updateEatenPieces(eColor.Black, this.game.getBoardGame().GetEaten());
		this.main.updateEatenPieces(eColor.White, this.game.getBoardGame().GetEaten());
		this.main.changePlayerTurn(this.game.GetTurn());
		this.main.enableBackwardButton(this.game.canGoBackward());
		this.main.enableForwardButton(this.game.canGoForward());
		if (this.game.isCheckKing() == eColor.Black)
			message = messagesGame.get(eGameState.CHECK_KING_B);
		else if (this.game.isCheckKing() == eColor.White)
			message = messagesGame.get(eGameState.CHECK_KING_W);
		else if (this.game.isCheckMat() == eColor.Black)
			message = messagesGame.get(eGameState.CHECK_MATE_B);
		else if (this.game.isCheckMat() == eColor.White)
			message = messagesGame.get(eGameState.CHECK_MATE_W);
		this.main.changeStatutMsg(message);
	}
	/**
	 * Export current game to be played later
	 * @param path	File where to save game
	 */
	public void exportGame(String path) {
		this.game.Export(path); 
	}
	/**
	 * Get all forward possibilities
	 * @return List of forward possibilities
	 */
	public Collection<Pair<Position, Position>> getBranches() {
		Collection<Pair<Position, Position>> branches = this.game.getForward();
		return branches;
	}
	/**
	 * Get number of forward possibilities
	 * @return
	 */
	public int getBranchesNb() {
		return this.game.getForward().size();
	}

	// Private Fields
	/**
	 *  Inner class which calculs the game logic
	 */
	IChessGame game;
	/**
	 *  board game colors
	 */
	Color black;
	Color brown_dark;
	Color brown_light;
	Color selectionned;
	/**
	 *  Pieces images list
	 */
	Map<ePawns, BufferedImage> pieces_images_black;
	Map<ePawns, BufferedImage> pieces_images_white;
	/**
	 *  Positions of clicked piece and clicked square
	 */
	Position posFirstClick;
	Position posSecondClick;
	/**
	 *  message written at the bottom of the board, 
	 *  indicating game state
	 */
	String message;
	Map<eGameState, String> messagesGame;
	Map<eMoveState, String> messagesMove;
	/**
	 *  container reference
	 */
	Main main;
	
}
