/**
 * 
 */
package Chess;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
/**
 * @author Lumy-
 *
 */
public class ChessGame {
	public BoardGame		elem;
	private Log 			log;
	private ChessDataGame 	Header;

	public ChessGame()
	{
		log = new Log();
		elem = new BoardGame();
		addLinePion(eColor.Black);
		addLinePion(eColor.White);
		addHeadLine(eColor.Black);
		addHeadLine(eColor.White);
	}
	
	public void	NewGame(String nameWhite, String nameBlack)
	{
		NewGame(nameWhite, nameBlack, "1");
	}

	public void	NewGame(String nameWhite, String nameBlack, String round)
	{
		Header = new ChessDataGame(nameWhite, nameBlack, round);
		log.newGame();

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
		firstClick.print();
		secondClick.print();
		if (firstClick.equals(secondClick) || elem.isOutside(firstClick) ||
			elem.isOutside(secondClick) || -1 == elem.indexOf(firstClick))
			return false;
		boolean ret = Rules.DoMovePawns(elem.get(elem.indexOf(firstClick)), secondClick, elem);
		if (ret != false)
			log.add(firstClick, secondClick);
		return ret;
	}
}
