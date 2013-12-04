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

public class ChessGameWidget extends JComponent implements MouseListener{
	// Inner class which calculs the game logic
	ChessGame game;
	// board game colors
	Color black;
	Color brown_dark;
	Color brown_light;
	// Pieces images list
	Map<ePawns, BufferedImage> pieces_images_black;
	Map<ePawns, BufferedImage> pieces_images_white;
	// Positions of clicked piece and clicked square
	Position posFirstClick;
	Position posSecondClick;
	
	public ChessGameWidget() {
		// initializes game logic
		this.game = new ChessGame();
		this.game.Initalize();
		// initializes game colors
		this.black = new Color(0, 0, 0);
		this.brown_dark = new Color(184, 115, 51);
		this.brown_light = new Color(222, 184, 135);		
		// initializes pieces images
		loadPieces();
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
		// TODO Auto-generated method stub
		
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
		int X = e.getX(); 
		int Y = e.getY();
		
		// mouse was cliked within game board limits
		if (X >= 0 && X <= 640 
				&& Y >= 0 && Y <= 640) {
			// nothing selected yet : first click
			if (posFirstClick == null) {
				this.posFirstClick = new Position();
				this.posFirstClick.row = Y / 80;
				this.posFirstClick.column = (char)('a' + (X / 80));

				// first click doesn't count if it's on an empty square
				if (this.game.elem.contains(this.posFirstClick))
					this.posFirstClick = null;
			}
			// one piece is already selected : second click
			else {
				this.posSecondClick = new Position();
				this.posSecondClick.row = Y / 80;
				this.posSecondClick.column = (char)('a' + (X / 80));
				
				if (!this.posFirstClick.equals(this.posSecondClick))
				{
				
					// check move validity
					boolean moveAccepted = this.game.catchEvent(posFirstClick, posSecondClick);
					
					// update game board (piece moving or text explaining why not)
					handleMove(moveAccepted);
					
					//reinitialize click positions
					this.posFirstClick = null;
				}
				this.posSecondClick = null;
			}
		}
		
//			posFC.row = newY / 80;
//			posFC.column = (char) ('a' + (newX / 80));
//			posSC.row = newY / 80;
//			posSC.column = (char) ('a' + (newX / 80));
//			
		
	}
	
	private void handleMove(boolean moveAccepted) {
		// TODO Auto-generated method stub
		if (moveAccepted) {
			repaint();
		}
		else {
			
		}
	}

	// repaints the widget when an update of any kind is made
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;		
		
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
					g2d.setColor(brown_dark);
				else
					g2d.setColor(brown_light);
				g2d.fillRect((i*80)+1, (j*80)+1, 79, 79);
			}
		}
	}
	
	
	
	
}
