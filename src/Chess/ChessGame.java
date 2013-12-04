/**
 * 
 */
package Chess;
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
	    return false;
	}
}
