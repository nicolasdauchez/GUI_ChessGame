/**
 * 
 */
package Chess;
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
	private eGameState		State;

	public ChessGame()
	{
		State = eGameState.NEXT;
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
		SetState(e);
		Turn = (Turn == eColor.Black) ? eColor.White : eColor.Black;
	}
	/**
	 * Set current Player Loser by abandon
	 */
	public void AskDraw()
	{
		// Set Result to Draw
		//Log the game in the file
	}
	/**
	 * SetCheckMat by Color
	 * @param black
	 */
	private void SetState(eGameState e) {
		State = e;
	}
	/**
	 * Return eColor.None if no CheckMat, or the Color who is in CheckMat
	 * @return
	 */
	public eColor isCheckMat() {
		if (State == eGameState.CHECK_MATE_B)
			return eColor.Black;
		else if (State == eGameState.CHECK_MATE_W)
			return eColor.White;
		return eColor.None;
	}
	/**
	 * Return eColor.None if no CheckKing, or the Color who is in CheckKing
	 * @return
	 */
	public eColor isCheckKing() {
		if (State == eGameState.CHECK_KING_B)
			return eColor.Black;
		else if (State == eGameState.CHECK_KING_W)
			return eColor.White;
		return eColor.None;
	}
	public Boolean isDraw() {
		if (State == eGameState.DRAW)
			return true;
		return false;
	}
	public void	NewGame(String nameWhite, String nameBlack)
	{
		NewGame(nameWhite, nameBlack, "1");
	}

	public void	NewGame(String nameWhite, String nameBlack, String round)
	{
		Header = new ChessDataGame(nameWhite, nameBlack, round);
		log.newGame();
		State = eGameState.NEXT;
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
		if (State != eGameState.NEXT)
			System.out.println("IS Not NORMAL MOVE");
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
