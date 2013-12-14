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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import main.Main;
import main.Pair;

public class ChessGameWidget extends JComponent implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Inner class which calculs the game logic
	IChessGame game;
	// board game colors
	Color black;
	Color brown_dark;
	Color brown_light;
	Color selectionned;
	// Pieces images list
	Map<ePawns, BufferedImage> pieces_images_black;
	Map<ePawns, BufferedImage> pieces_images_white;
	// Positions of clicked piece and clicked square
	Position posFirstClick;
	Position posSecondClick;
	// message written at the bottom of the board,
	// indicating game state
	String message;
	// container reference
	Main main;
	
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
		
		// update game board
		repaint();
		// adds mouse listener
		addMouseListener(this);
	}

	private void loadPieces() {
		this.pieces_images_white = new Hashtable<ePawns, BufferedImage>();
		this.pieces_images_black = new Hashtable<ePawns, BufferedImage>();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override // not used
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override // not used
	public void mouseEntered(MouseEvent e) {
//		System.out.println("ici");
//		if (getMousePosition(e))
	}

	@Override // not used
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override // not used
	public void mousePressed(MouseEvent e) {

	}

	@Override // the mouse released event is the only one we use
	public void mouseReleased(MouseEvent e) {
		Position clickedPos = getMousePosition(e);
		// mouse was cliked within game board limits
		if (!this.game.getBoardGame().isOutside(clickedPos)) {
			// nothing selected yet : first click
			if (posFirstClick == null) {
				this.posFirstClick = clickedPos;
				// first click doesn't count if it's on an empty square
				// first click doesn't count if not current player's pieces clicked
				if (!this.game.getBoardGame(). contains(this.posFirstClick)
					|| this.game.getBoardGame().getObstacleCase(this.posFirstClick) != this.game.GetTurn())
					this.posFirstClick = null;
					
			}
			// one piece is already selected : second click
			else {
				this.posSecondClick = clickedPos;
				if (!this.posFirstClick.equals(this.posSecondClick))
				{
					// saving some stats
					int eatenPiecesNb = this.game.getBoardGame().GetEaten().size();
					eColor currentPlayer = this.game.GetTurn();
				
					// check move validity
					Pair<eMoveState, eGameState> moveAccepted = this.game.catchEvent(posFirstClick, posSecondClick);

					System.out.println("eMoveState: " + moveAccepted.GetLeft() + " eGameState:" + moveAccepted.GetRight() + " TurnPlayer: " + game.GetTurn());
					
					// update game board (piece moving or text explaining why not)
					handleMove(moveAccepted);
					// updates eaten pieces panel if necessary
					if (this.game.getBoardGame().GetEaten().size() > eatenPiecesNb)
						this.main.updateEatenPieces(currentPlayer, this.game.getBoardGame().GetEaten());
				}
				else
					this.posFirstClick = null;
				this.posSecondClick = null;
			}
			repaint();
		}
	}
	
	private void handleMove(Pair<eMoveState, eGameState> moveAccepted) {
		this.message = "";
		eMoveState moveState = moveAccepted.GetLeft();
		eGameState gameState = moveAccepted.GetRight();
		
		if (moveState == eMoveState.SUCCESS) {
			//reinitialize click positions
			this.posFirstClick = null;

			switch (gameState) {
				case CHECK_KING_B: {
					message = "Black King is in CHECK!";
					break;
				}
				case CHECK_KING_W: {
					message = "White King is in CHECK!";
					break;
				}
				case CHECK_MATE_B: {
					message = "Black King is in CHECK MATE! Game over.";
					break;
				}
				case CHECK_MATE_W: {
					message = "White King is in CHECK MATE! Game over.";
					break;
				}
				case DRAW: {
					System.out.println("STALEMEAMTEMTEAT");
					message = "Stalemate! Game over.";
					break;
				}
				default: 
					break;
			}
			
			// check if a pawn managed to get a Promotion
			handlePromotion();
			
		}
		else {
			switch (moveState) {
				case FAIL_SAME_COLOR_CASE_OCCUPIED: {
					this.posFirstClick = this.posSecondClick;
					break;
				}
				case FAIL_CHECK: {
					message = "You can't move your piece here: your king would be/stay in check.";
					break;
				}
				case FAIL_UNAUTHORIZED: {
					message = "You can't move your piece here: unauthorized move.";
					break;
				}
				case FAIL_PAWNS_BACKWARD: {
					message = "Pawns can't go or eat backward.";
					break;
				}
				case FAIL_PAWNS_EAT_FORWARD: {
					message = "Pawns can't eat forward.";
					break;
				}
				case CASTLING: {
					handleCastling();
					break;
				}
			default:
				break;
			}
		}
	}

	private void handlePromotion() {
		Collection<Pawn> alivePieces = this.game.getBoardGame().getElem();
		if (!alivePieces.isEmpty()) {
			Iterator<Pawn> iterator = alivePieces.iterator();
			while(iterator.hasNext()) {
  		    	Pawn piece = iterator.next();
  		    	ePawns classe = piece.GetClass();
  		    	if (classe == ePawns.PAWN) {
  		    		eColor color = piece.GetColor();
	    			Position pos = piece.GetPosition();  		    			
  		    			if ((color == eColor.Black) && (pos.row == 1)
	    					|| (color == eColor.White) && (pos.row == 8)) {
	    					ePawns newClasse = this.main.askPromotion();
	    					if (newClasse != null)
	    						this.game.DoPromotion(pos, newClasse);
  		    			}
  		    			
  		    	}
			}
		}
	}

	private void handleCastling() {
		boolean castlingWanted = this.main.askCastling();
		if (castlingWanted) {
			this.game.DoCastling(this.posFirstClick, this.posSecondClick);
			this.posFirstClick = null;
		}
		else {
			this.posFirstClick = this.posSecondClick;
		}
	}

	// Return a Position object of the mouse current event
	Position getMousePosition(MouseEvent e) {
		int X = e.getX(); 
		int Y = e.getY();
		
		Position curPos = new Position();
		curPos.row = 8 - (Y / 80);
		curPos.column = (char)('a' + (X / 80));

		return curPos;
	}
	
	// repaints the widget when an update of any kind is made
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;		
		// Updates player turn notification
		main.changePlayerTurn(this.game.GetTurn());		
		// Updates game status message
		main.changeStatutMsg(message);
		// re-draw all game board
		drawGrid(g2d);
		drawPieces(g2d);
	}

	private void drawPieces(Graphics2D g2d) {
		Collection<Pawn> alivePieces = this.game.getBoardGame().getElem();
		if (!alivePieces.isEmpty()) {
			Map<ePawns, BufferedImage> tmpPiecesImg = null;
			Iterator<Pawn> iterator = alivePieces.iterator();

			while(iterator.hasNext()) {
  		    	Pawn piece = iterator.next();
  		    	ePawns piece_class = piece.GetClass();
				eColor piece_color = piece.GetColor();
				Position piece_pos = piece.GetPosition();
				if (piece_color == eColor.Black) tmpPiecesImg = pieces_images_black;
				else tmpPiecesImg = pieces_images_white;
				g2d.drawImage(tmpPiecesImg.get(piece_class), null, (piece_pos.column- 'a')*80, ((8 - piece_pos.row))*80);
			}
		 }
	}

	private void drawGrid(Graphics2D g2d) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				g2d.setColor(black);
				g2d.drawRect(i*80, j*80, 80, 80);
				// light brown or dark brown square
				if ((i % 2 == 0 && j%2 == 0) 
						|| (i % 2 != 0 && j%2 != 0))
					g2d.setColor(brown_light);
				else
					g2d.setColor(brown_dark);
				g2d.fillRect((i*80)+1, (j*80)+1, 79, 79);
			}
		}
		// colors selectionned piece's square
		if (this.posFirstClick != null) {
			g2d.setColor(selectionned);
			g2d.fillRect(((posFirstClick.column - 'a')*80)+1, ((8 - posFirstClick.row)*80), 79, 79);
		}
	}
	
	
	public Image getPieceImage(eColor color, ePawns classe) {
		if (color == eColor.Black)
			return this.pieces_images_black.get(classe);
		return this.pieces_images_white.get(classe);
	}

	public void resetGame() {
		this.game.NewGame("Whites", "Blacks");		
		repaint();
		this.main.changePlayerTurn(this.game.GetTurn());
	}
		
	
	
	
}
