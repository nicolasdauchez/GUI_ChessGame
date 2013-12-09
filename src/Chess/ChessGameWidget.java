package Chess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DebugGraphics;
import javax.swing.JComponent;
import javax.swing.JLabel;

import main.Pair;

public class ChessGameWidget extends JComponent implements MouseListener{
	// Inner class which calculs the game logic
	ChessGame game;
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
	
	public ChessGameWidget() {
		// initializes game logic
		this.game = new ChessGame();
		// initializes game colors
		this.black = new Color(0, 0, 0);
		this.brown_dark = new Color(184, 115, 51);
		this.brown_light = new Color(222, 184, 135);
		this.selectionned = new Color(0, 0, 255, 75);
		// initializes pieces images
		loadPieces();
		
		
		JLabel msg = new JLabel("BLAH!");
		add(msg);
		
		// update game board
		repaint();
		// adds mouse listener
		addMouseListener(this);
	}

	private void loadPieces() {
		this.pieces_images_white = new Hashtable<ePawns, BufferedImage>();
		this.pieces_images_black = new Hashtable<ePawns, BufferedImage>();
		try {
			this.pieces_images_white.put(ePawns.Pawn, ImageIO.read(new File("src/Images/WhitePawn.png")));
			this.pieces_images_white.put(ePawns.Crazy, ImageIO.read(new File("src/Images/WhiteBishop.png")));
			this.pieces_images_white.put(ePawns.Tower, ImageIO.read(new File("src/Images/WhiteRook.png")));
			this.pieces_images_white.put(ePawns.Queen, ImageIO.read(new File("src/Images/WhiteQueen.png")));
			this.pieces_images_white.put(ePawns.King, ImageIO.read(new File("src/Images/WhiteKing.png")));
			this.pieces_images_white.put(ePawns.Cavalery, ImageIO.read(new File("src/Images/WhiteKnight.png")));
			this.pieces_images_black.put(ePawns.Cavalery, ImageIO.read(new File("src/Images/BlackKnight.png")));
			this.pieces_images_black.put(ePawns.King, ImageIO.read(new File("src/Images/BlackKing.png")));
			this.pieces_images_black.put(ePawns.Queen, ImageIO.read(new File("src/Images/BlackQueen.png")));
			this.pieces_images_black.put(ePawns.Tower, ImageIO.read(new File("src/Images/BlackRook.png")));
			this.pieces_images_black.put(ePawns.Crazy, ImageIO.read(new File("src/Images/BlackBishop.png")));
			this.pieces_images_black.put(ePawns.Pawn, ImageIO.read(new File("src/Images/BlackPawn.png")));
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
		if (!this.game.elem.isOutside(clickedPos)) {
			// nothing selected yet : first click
			if (posFirstClick == null) {
				this.posFirstClick = clickedPos;
				// first click doesn't count if it's on an empty square
				// first click doesn't count if not current player's pieces clicked
				if (!this.game.elem. contains(this.posFirstClick)
					|| this.game.elem.getObstacleCase(this.posFirstClick) != this.game.GetTurn())
					this.posFirstClick = null;
					
			}
			// one piece is already selected : second click
			else {
				this.posSecondClick = clickedPos;
				if (!this.posFirstClick.equals(this.posSecondClick))
				{
				
					// check move validity
					Pair<eMoveState, eGameState> moveAccepted = this.game.catchEvent(posFirstClick, posSecondClick);
					
					// update game board (piece moving or text explaining why not)
					handleMove(moveAccepted);
				}
				else
					this.posFirstClick = null;
				this.posSecondClick = null;
			}
			repaint();
		}
		
//			posFC.row = newY / 80;
//			posFC.column = (char) ('a' + (newX / 80));
//			posSC.row = newY / 80;
//			posSC.column = (char) ('a' + (newX / 80));
//			
		
	}
	
	private void handleMove(Pair<eMoveState, eGameState> moveAccepted) {
		this.message = null;
		eMoveState moveState = moveAccepted.GetLeft();
		eGameState gameState = moveAccepted.GetRight();
		
		if (moveState == eMoveState.SUCCESS) {
			//reinitialize click positions
			this.posFirstClick = null;
		}
		else {
			switch (moveAccepted.GetLeft()) {
				case FAIL_SAME_COLOR_CASE_OCCUPIED: {
					this.posFirstClick = this.posSecondClick;
					break;
				}
				case FAIL_CHECK: {
					message = "You can't move your piece here: your king would be in check.";
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
			default:
				break;
			}
		}
	}

	// Return a Position object of the mouse current event
	Position getMousePosition(MouseEvent e) {
		int X = e.getX(); 
		int Y = e.getY();
		
		Position curPos = new Position();
		curPos.row = (Y / 80) + 1;
		curPos.column = (char)('a' + (X / 80));

		return curPos;
	}
	
	// repaints the widget when an update of any kind is made
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;		
		
		g2d.drawString("Player turn:", 650, 160);
		g2d.drawString((this.game.GetTurn() == eColor.Black) ? ("BLACK") : ("WHITE"), 650, 175);
		
		g2d.drawString("> ", 5, 665);
		if (message != null)
			g2d.drawString(message, 15, 665);
		
		// re-draw all game board
		drawGrid(g2d);
		drawPieces(g2d);
	}

	private void drawPieces(Graphics2D g2d) {
		for (int i = 0; i < this.game.elem.size(); i++) {
			Pawn piece = this.game.elem.get(i);
			if (piece != null) {
				ePawns piece_class = piece.GetClass();
				eColor piece_color = piece.GetColor();
				Position piece_pos = piece.GetPosition();
				if (piece_color == eColor.Black)
					g2d.drawImage(pieces_images_black.get(piece_class), null, (piece_pos.column - 'a')*80, (piece_pos.row - 1)*80);
				else
					g2d.drawImage(pieces_images_white.get(piece_class), null, (piece_pos.column- 'a')*80, (piece_pos.row - 1)*80);
			}
		}
//		g2d.drawImage(pieces_images_black.get(ePawns.Pion), null, 0, 0);
//		g2d.drawImage(pieces_images_black.get(ePawns.Tower), null, 80, 80);
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
			g2d.fillRect(((posFirstClick.column - 'a')*80)+1, ((posFirstClick.row - 1)*80), 79, 79);
		}
	}
		
	
	
	
}
