package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class DeadsPanel extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeadsPanel() {
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		BufferedImage img = null;
		BufferedImage img2 = null;
		BufferedImage img3 = null;
		BufferedImage img4 = null;
		try {
			img = ImageIO.read(new File("src/Images/BlackPawn.png"));
			img2 = ImageIO.read(new File("src/Images/BlackBishop.png"));
			img3 = ImageIO.read(new File("src/Images/WhiteQueen.png"));
			img4 = ImageIO.read(new File("src/Images/BlackKing.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		g2d.setColor(new Color(255,0,0));
//		g2d.fillRect(0, 0, 80, 80);
		
		

//		System.out.println("la");
		
		double xScaleFactor = 0.35;
		double yScaleFactor = 0.35;
		
		int newW = (int)(img.getWidth() * xScaleFactor);
        int newH = (int)(img.getHeight() * yScaleFactor);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, newW, newH, null);
        g2d.drawImage(img2, 1 * newW, 0, newW, newH, null);
        g2d.drawImage(img3, 2 * newW, 0, newW, newH, null);
        g2d.drawImage(img4, 3 * newW, 0, newW, newH, null);
	}
	
}
