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
	Map<eClass, BufferedImage> pieces_images_black;
	Map<eClass, BufferedImage> pieces_images_white;
	
	public ChessGameWidget() {
		
		this.game = new ChessGame();
		this.game.Initalize();
		
		// initializes game colors
		this.black = new Color(0, 0, 0);
		this.brown_dark = new Color(184, 115, 51);
		this.brown_light = new Color(222, 184, 135);
		// Initializes pieces images
		loadPieces();
		// update game board
		repaint();
		// adds mouse listener
		addMouseListener(this);
	}

	private void loadPieces() {
		this.pieces_images_white = new Hashtable<eClass, BufferedImage>();
		this.pieces_images_black = new Hashtable<eClass, BufferedImage>();
		try {
			this.pieces_images_white.put(eClass.Pion, ImageIO.read(new File("src/Images/WhitePawn.png")));
			this.pieces_images_white.put(eClass.Crazy, ImageIO.read(new File("src/Images/WhiteBishop.png")));
			this.pieces_images_white.put(eClass.Tower, ImageIO.read(new File("src/Images/WhiteRook.png")));
			this.pieces_images_white.put(eClass.Queen, ImageIO.read(new File("src/Images/WhiteQueen.png")));
			this.pieces_images_white.put(eClass.King, ImageIO.read(new File("src/Images/WhiteKing.png")));
			this.pieces_images_white.put(eClass.Cavalery, ImageIO.read(new File("src/Images/WhiteKnight.png")));
			this.pieces_images_black.put(eClass.Cavalery, ImageIO.read(new File("src/Images/BlackKnight.png")));
			this.pieces_images_black.put(eClass.King, ImageIO.read(new File("src/Images/BlackKing.png")));
			this.pieces_images_black.put(eClass.Queen, ImageIO.read(new File("src/Images/BlackQueen.png")));
			this.pieces_images_black.put(eClass.Tower, ImageIO.read(new File("src/Images/BlackRook.png")));
			this.pieces_images_black.put(eClass.Crazy, ImageIO.read(new File("src/Images/BlackBishop.png")));
			this.pieces_images_black.put(eClass.Pion, ImageIO.read(new File("src/Images/BlackPawn.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
			Pion piece = this.game.elem.get(i);
			if (piece != null) {
				eClass piece_class = piece.GetClass();
				eColor piece_color = piece.GetColor();
				Position piece_pos = piece.GetPosition();
				if (piece_color == eColor.Black)
					g2d.drawImage(pieces_images_black.get(piece_class), null, (piece_pos.column - 'a')*80, (piece_pos.row - 1)*80);
				else
					g2d.drawImage(pieces_images_white.get(piece_class), null, (piece_pos.column- 'a')*80, (piece_pos.row - 1)*80);
			}
		}
//		g2d.drawImage(pieces_images_black.get(eClass.Pion), null, 0, 0);
//		g2d.drawImage(pieces_images_black.get(eClass.Tower), null, 80, 80);
//		g2d.drawImage(pieces_images_black.get(eClass.Cavalery), null, 160, 160);
//		g2d.drawImage(pieces_images_black.get(eClass.Crazy), null, 240, 240);
//		g2d.drawImage(pieces_images_black.get(eClass.King), null, 320, 320);
//		g2d.drawImage(pieces_images_black.get(eClass.Queen), null, 400, 400);
//		g2d.drawImage(pieces_images_white.get(eClass.Queen), null, 480, 480);
//		g2d.drawImage(pieces_images_white.get(eClass.King), null, 560, 560);
//		g2d.drawImage(pieces_images_white.get(eClass.Crazy), null, 0, 240);
//		g2d.drawImage(pieces_images_white.get(eClass.Cavalery), null, 320, 800);
//		g2d.drawImage(pieces_images_white.get(eClass.Tower), null, 240, 0);
//		g2d.drawImage(pieces_images_white.get(eClass.Pion), null, 320, 0);
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
