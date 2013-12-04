/**
 * 
 */
package Chess;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
/**
 * @author Lumy-
 *
 */
public class ChessGame {
	public BoardGame	elem;
	private Log imexLog;
	private ChessData Header;

	public ChessGame()
	{
		imexLog = new Log();
		elem = new BoardGame();
		addLinePion(eColor.Black);
		addLinePion(eColor.White);
		addHeadLine(eColor.Black);
		addHeadLine(eColor.White);
		elem.print();
	}
	
	/**
	 * Will Ask For Load a game or for the Name of Player
	 */
	public void Initalize()
	{
		//imexLog.Import("wccc1974.PGN");
		//Header = new ChessData("Toto", "Tata", "TestEvent");
		System.out.println(elem.size());
		//catchEvent(null, null);
	}

	private void add(eColor c, Position p, eClass t) {
		elem.add(new Pion(c, p, t));
	}

	private void addLinePion(eColor c) {
		Position p = new Position();
		p.row = 7;
		if (c == eColor.White)
			p.row = 2;
		p.column = 'a';
		while (p.column != 'i') {
			
			add(c, new Position(p), eClass.Pion);
			p.column += 1;
		}
	}

	private void addHeadLine(eColor c) {
		Position p = new Position();
		p.row = 8;
		if (c == eColor.White)
			p.row = 1;
		p.column = 'a';
		eClass e[] = { eClass.Tower, eClass.Cavalery, eClass.Crazy, eClass.King, eClass.Queen, eClass.Crazy, eClass.Cavalery, eClass.Tower };
		if (c == eColor.White)
			ArrayUtils.reverse(e);
		for (int i = 0; i < e.length; i++) {
			add(c, new Position(p), e[i]);
			p.column += 1;
		}
	}

	public boolean catchEvent(Position firstClick, Position secondClick)
	{
		/*
		 * 
		 */
		
		//
		Pion p = elem.get(1);
		Position t = new Position(p.GetPosition());
		p.print();
		while (t.row != 2)
		{
			t.row -= 1;
		    Rules.DoMovePion(p, t, elem);
		}
	    elem.print();
	    t.row = 2;
	    t.column = 'a';
	    p = elem.get(elem.indexOf(t));
	    t.row = 3;
	    t.column = 'b';
	    if (Rules.DoMovePion(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");
	    elem.print();
	    // On mange et on bouge des ptit pion sans probleme
	    return false;
	}
}
