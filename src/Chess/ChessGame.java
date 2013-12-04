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
	}
	
	/**
	 * Will Ask For Load a game or for the Name of Player
	 */
	public void Initalize()
	{
		//imexLog.Import("wccc1974.PGN");
		//Header = new ChessData("Toto", "Tata", "TestEvent");
		System.out.println(elem.size());
		catchEvent(null, null);
	}

	private void add(eColor c, Position p, ePawns t) {
		elem.add(new Pawn(c, p, t));
	}

	private void addLinePion(eColor c) {
		Position p = new Position();
		p.row = 7;
		if (c == eColor.White)
			p.row = 2;
		p.column = 'a';
		while (p.column != 'i') {
			add(c, new Position(p), ePawns.Pawn);
			p.column += 1;
		}
	}

	private void addHeadLine(eColor c) {
		Position p = new Position();
		p.row = 8;
		if (c == eColor.White)
			p.row = 1;
		p.column = 'a';
		ePawns e[] = { ePawns.Tower, ePawns.Cavalery, ePawns.Crazy, ePawns.King, ePawns.Queen, ePawns.Crazy, ePawns.Cavalery, ePawns.Tower };
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
		Pawn p = elem.get(1);
		Position t = new Position(p.GetPosition());
		p.print();
		while (t.row != 2)
		{
			t.row -= 1;
		  Rules.DoMovePawns(p, t, elem);
		}
		 t.row = 4;
		 t.column = 'b';
		 if (Rules.DoMovePawns(p, t, elem))
		   	System.out.println("bad");
		 else
		   	System.out.println("cool");
	    p.print();
	    t.row = 2;
	    t.column = 'a';

	    p = elem.get(elem.indexOf(t));
	    t.row = 3;
	    t.column = 'b';
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");
	    elem.print();
	    t.row = 1;
	    t.column = 'a';
	    p = elem.get(elem.indexOf(t));
	    t.row = 5;
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");
	    elem.print();
	    p.print();
	    t.row = 5;
	    t.column = 'b';
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");
	    p.print();
	    t.row = 6;
	    t.column = 'c';
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Bad");
	    else
	    	System.out.println("Cool");
	    p.print();
	    t.row = 7;
	    t.column = 'b';
	    Rules.DoMovePawns(p, t, elem);
	    t.column = 'a';
	    System.out.println(elem.size());
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");
	    System.out.println(elem.size());
	    elem.print();
	    t.row = 2;
	    t.column = 'e';
	    p = elem.get(elem.indexOf(t));
	    t.row = 4;
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");
	    t.row = 6;
	    if (!Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");
	    t.row = 5;
	    Rules.DoMovePawns(p, t, elem);
	    t.row = 1;
	    t.column = 'e';
	    p = elem.get(elem.indexOf(t));
	    t.row = 2;
	    t.column = 'e';
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");	    
	    t.column = 'd';
	    t.row = 3;
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");	    
	    t.row = 5;
	    if (!Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");	    
	    t.row = 4;
	    Rules.DoMovePawns(p, t, elem);
	    t.row = 5;
	    Rules.DoMovePawns(p, t, elem);
	    t.row = 6;
	    Rules.DoMovePawns(p, t, elem);
	    t.row = 7;
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");
	    System.out.println(elem.size());
	    elem.print();
	    t.row = 8;
	    t.column = 'c';
	    p = elem.get(elem.indexOf(t));
	    t.row = 6;
	    t.column = 'a';
	    p.print();
	    if (Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Cool");
	    else
	    	System.out.println("Bad");
	    p.print();
	    t.row = 4;
	    t.column = 'c';
	    if (Rules.DoMovePawns(p, t, elem))
		    p.print();
	    t.row = 7;
	    t.column = 'f';
	    if (Rules.DoMovePawns(p, t, elem))
		    System.out.println("Bad!!");
	    elem.print();
	    t.row = 1;
	    t.column = 'f';
	    if (!Rules.DoMovePawns(p, t, elem))
		    System.out.println("Bad!!");
	    else
	    	System.out.println("Cool :)");
	    elem.print();
	    t.row = 8;
	    t.column = 'b';
	    p = elem.get(elem.indexOf(t));
	    t.row = 6;
	    t.column = 'c';
	    if (!Rules.DoMovePawns(p, t, elem))
		    System.out.println("Bad!!");
	    else
	    	System.out.println("Cool :)");
	    t.row = 4;
	    t.column = 'd';
	    if (!Rules.DoMovePawns(p, t, elem))
		    System.out.println("Bad!!");
	    t.row = 2;
	    t.column = 'c';
	    if (!Rules.DoMovePawns(p, t, elem))
	    	System.out.println("Bad!!");
	    else
	    	System.out.println("Cool Before!!");
	    elem.print();
	    
	    t.row = 1;
	    t.column = 'd';
	    p = elem.get(elem.indexOf(t));
	    t.row = 2;
	    t.column = 'c';
	    if (!Rules.DoMovePawns(p, t, elem))
		    System.out.println("Bad!!");
	    else
	    	System.out.println("Cool Eat :)");
	    t.row = 4;
	    t.column = 'a';
	    if (!Rules.DoMovePawns(p, t, elem))
		    System.out.println("Bad!!");
	    else
	    	System.out.println("Cool :)");
	    t.row = 4;
	    t.column = 'h';
	    if (!Rules.DoMovePawns(p, t, elem))
		    System.out.println("Bad!!");
	    else
	    	System.out.println("Cool :)");
	    t.row = 6;
	    t.column = 'h';
	    if (!Rules.DoMovePawns(p, t, elem))
		    System.out.println("Bad!!");
	    else
	    	System.out.println("Cool :)");
	    System.out.println(elem.size());
	    // On mange et on bouge des ptit pion sans probleme
	    return false;
	}
}
