/**
 * 
 */
package Chess;
import java.util.Date;
import main.Pair;
import org.apache.commons.lang3.ArrayUtils;
/**
 * @author Lumy-
 *
 */
public class ChessGame {
	public BoardGame		elem;
	private Log 			log;
	private ChessDataGame 	Header;
	private eColor			Turn;
	private eGameState		check;
	
	public ChessGame()
	{
		check = eGameState.NEXT;
		Turn = eColor.White;
		log = new Log();
		elem = new BoardGame();
		log.Initialize();
		addLinePion(eColor.Black);
		addLinePion(eColor.White);
		addHeadLine(eColor.Black);
		addHeadLine(eColor.White);
	}
	/**
	 * Return the Color of current Player
	 * @return
	 */
	public eColor GetTurn() {
		return Turn;
	}
	/**
	 * Set The Next Player and setCheckMat
	 * if eGameSate e
	 * @param GameState
	 */
	private void NextTurn(eGameState e) {
		if (e == eGameState.CHECK_MATE_B || e == eGameState.CHECK_MATE_W)
			SetCheckMat(e);
		Turn = (Turn == eColor.Black) ? eColor.White : eColor.Black;
	}
	/**
	 * SetCheckMat by Color
	 * @param black
	 */
	private void SetCheckMat(eGameState e) {
		check = e;
	}
	/**
	 * Return eColor.None if no CheckMat, or the Color who is in CheckMat
	 * @return
	 */
	public eColor isCheckMat() {
		if (check == eGameState.CHECK_MATE_B)
			return eColor.Black;
		else if (check == eGameState.CHECK_MATE_W)
			return eColor.White;
		return eColor.None;
	}
	public void	NewGame(String nameWhite, String nameBlack)
	{
		NewGame(nameWhite, nameBlack, "1");
	}

	public void	NewGame(String nameWhite, String nameBlack, String round)
	{
		Header = new ChessDataGame(nameWhite, nameBlack, round);
		log.newGame();
		check = eGameState.NEXT;
		Turn = eColor.White;
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
	public Pair<eMoveState, eGameState> catchEvent(Position firstClick, Position secondClick)
	{
		if (-1 == elem.indexOf(firstClick))
			return new Pair<eMoveState, eGameState>(eMoveState.FAIL_CHECK, eGameState.SAME);
		Pair<eMoveState, eGameState> r1 = Rules.DoMovePawns(elem.get(elem.indexOf(firstClick)), secondClick, elem);
		if (r1.GetRight() != eGameState.SAME)
		{
			log.add(firstClick, secondClick);
			NextTurn(r1.GetRight());
		}
		System.out.println("eMoveState" + r1.GetLeft() + " eGameState:" + r1.GetRight() + " TurnPlayer" + GetTurn());
		return r1;
	}
}
