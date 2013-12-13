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
		elem.NextTurn(); // for enPassant rules
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
	
	/**
	 * return if the current position is an Promotion
	 * @param pos
	 * @return
	 */
	public boolean isPromotion(Position pos) {
		if (elem.contains(pos))
			if (Rules.isPromotion(elem.get(elem.indexOf(pos))))
				return true;
		return false;
	}
	public void		DoPromotion(Position p, ePawns c) {
		elem.Promotion(p, c);
		return ;
	}
	public void DoCastling(Position firstClick, Position secondClick) {
		
	}

	public Pair<eMoveState, eGameState> Check_King_Way(Position firstClick, Position secondClick)
	{
		System.out.println("CatcEvent: Before");
		Pair<eMoveState, eGameState> r2;
		List<Pair<Position, Position>> r;
		r = elem.getListPositionPossibleProtectKing((State == eGameState.CHECK_KING_B ? eColor.Black : eColor.White));
		if (r.size() == 0) {
			System.out.println("----------");
			return new Pair<eMoveState, eGameState>(eMoveState.SUCCESS, (eGameState.CHECK_KING_W == State ? eGameState.CHECK_MATE_W : eGameState.CHECK_MATE_B));
		}
		r2 = Rules.DoMovePawns(r, elem.get(elem.indexOf(firstClick)), secondClick, elem);
		if (r2.GetRight() != eGameState.SAME)
		{
			log.add(firstClick, secondClick);
			NextTurn(r2.GetRight());
		}
		System.out.println("----------");
		return r2;
	}
	
	public Pair<eMoveState, eGameState> catchEvent(Position firstClick, Position secondClick)
	{
		Pair<eMoveState, eGameState> r1 = new Pair<eMoveState, eGameState>(eMoveState.FAIL_UNAUTHORIZED, eGameState.SAME);
		if (State == eGameState.DRAW)
			return r1;
		if (State == eGameState.CHECK_KING_B || State == eGameState.CHECK_KING_W)
			return Check_King_Way(firstClick, secondClick);
		System.out.println("CatcEvent: Normal");
		r1 = Rules.DoMovePawns(elem.get(elem.indexOf(firstClick)), secondClick, elem);
		System.out.println("OutDoMovePawns: Normal");
		if (r1.GetRight() != eGameState.SAME)
		{
			log.add(firstClick, secondClick);
			NextTurn(r1.GetRight());
		}
		if ((State == eGameState.CHECK_KING_B || State == eGameState.CHECK_KING_W) &&
		   (elem.getObstacleCase(firstClick) == eColor.Black ? eGameState.CHECK_KING_B : eGameState.CHECK_KING_W) != State)
		{
			System.out.println("CatcEvent: After");
			List<Pair<Position, Position>> r;
			if (State == eGameState.CHECK_KING_B)
				r = elem.getListPositionPossibleProtectKing(eColor.Black);
			else
				r = elem.getListPositionPossibleProtectKing(eColor.White);
			if (r.size() == 0)
				r1 = new Pair<eMoveState, eGameState>(eMoveState.SUCCESS, (eGameState.CHECK_KING_W == State ? eGameState.CHECK_MATE_W : eGameState.CHECK_MATE_B));
		}
		System.out.println("----------");
		return r1;
	}
}
