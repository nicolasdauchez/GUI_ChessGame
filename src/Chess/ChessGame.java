/**
 * 
 */
package Chess;
import java.util.List;

import main.Pair;
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
		elem.newGame();
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
		elem.newGame();
	}

	public void	NewGame(String nameWhite, String nameBlack, String round)
	{
		Header = new ChessDataGame(nameWhite, nameBlack, round);
		log.newGame();
		State = eGameState.NEXT;
		Turn = eColor.White;
	}

	public Pair<eMoveState, eGameState> catchEvent(Position firstClick, Position secondClick)
	{
		if (State == eGameState.CHECK_KING_B || State == eGameState.CHECK_KING_W)
		{
			List<Pair<Position, Position>> r;
			if (State == eGameState.CHECK_KING_B)
				r = elem.getListPositionPossibleProtectKing(eColor.Black);
			else
				r = elem.getListPositionPossibleProtectKing(eColor.White);
			if (r.size() == 0)
				return new Pair<eMoveState, eGameState>(eMoveState.SUCCESS, (eGameState.CHECK_KING_W == State ? eGameState.CHECK_MATE_W : eGameState.CHECK_MATE_B));
			else
				return Rules.DoMovePawns(r, elem.get(elem.indexOf(firstClick)), secondClick, elem);
		}
		Pair<eMoveState, eGameState> r1 = Rules.DoMovePawns(elem.get(elem.indexOf(firstClick)), secondClick, elem);
		if (r1.GetRight() != eGameState.SAME)
		{
			log.add(firstClick, secondClick);
			NextTurn(r1.GetRight());
		}
		return r1;
	}
}
