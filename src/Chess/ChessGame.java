/**
 * 
 */
package Chess;
import java.util.Collection;
import java.util.List;

import main.Pair;
/**
 * @author Lumy-
 *
 */
public class ChessGame implements IChessGame {
	private BoardGame		elem;
	private Log 			log;
	private ChessDataGame 	Header;
	private eColor			Turn;
	private eGameState		State;

	public ChessGame()
	{
		State = eGameState.NEXT;
		Turn = eColor.Black;
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
	public boolean isDraw() {
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
	/**
	 * Assuming that This function it's called after get the CASTLING state that has already check if you've the right
	 * @param firstClick
	 * @param secondClick
	 */
	public void DoCastling(Position click1, Position click2) {
		Position kp = elem.getKingPosition(elem.get(elem.indexOf(click1)).GetColor());
		Pair<Position, Position> r = Rules.Castling.getPositionKing(kp, (kp.equals(click2) ? click1 : click2));
		Pawn k = elem.get(elem.indexOf(kp));
		Pawn t = elem.get(elem.indexOf(kp.equals(click1) ? click2 : click1));
		k.SetPosition(r.GetLeft());
		t.SetPosition(r.GetRight());
		log.addCastling(k.GetColor(), (r.GetLeft().column == 'c' ? "O-O-O" : "O-O"));
		NextTurn(eGameState.NEXT);
	}

	private Pair<eMoveState, eGameState> Check_King_Way(Position firstClick, Position secondClick)
	{
		Pair<eMoveState, eGameState> r2;
		List<Pair<Position, Position>> r;
		r = elem.getListPositionPossibleProtectKing((State == eGameState.CHECK_KING_B ? eColor.Black : eColor.White));
		if (r.size() == 0) {
			return new Pair<eMoveState, eGameState>(eMoveState.SUCCESS, (eGameState.CHECK_KING_W == State ? eGameState.CHECK_MATE_W : eGameState.CHECK_MATE_B));
		}
		r2 = Rules.DoMovePawns(r, elem.get(elem.indexOf(firstClick)), secondClick, elem);
		if (r2.GetRight() != eGameState.SAME)
		{
			log.add(firstClick, secondClick);
			NextTurn(r2.GetRight());
		}
		return r2;
	}
	
	public Pair<eMoveState, eGameState> catchEvent(Position firstClick, Position secondClick)
	{
		Pair<eMoveState, eGameState> r1 = new Pair<eMoveState, eGameState>(eMoveState.FAIL_UNAUTHORIZED, eGameState.SAME);
		if (State == eGameState.DRAW)
			return r1;
		if (State == eGameState.CHECK_KING_B || State == eGameState.CHECK_KING_W)
			return Check_King_Way(firstClick, secondClick);
		r1 = Rules.DoMovePawns(elem.get(elem.indexOf(firstClick)), secondClick, elem);
		if (r1.GetRight() != eGameState.SAME)
		{
			log.add(firstClick, secondClick);
			NextTurn(r1.GetRight());
		}
		if ((State == eGameState.CHECK_KING_B || State == eGameState.CHECK_KING_W) &&
		   (elem.getObstacleCase(firstClick) == eColor.Black ? eGameState.CHECK_KING_B : eGameState.CHECK_KING_W) != State)
		{
			List<Pair<Position, Position>> r;
			if (State == eGameState.CHECK_KING_B)
				r = elem.getListPositionPossibleProtectKing(eColor.Black);
			else
				r = elem.getListPositionPossibleProtectKing(eColor.White);
			if (r.size() == 0)
				r1 = new Pair<eMoveState, eGameState>(eMoveState.SUCCESS, (eGameState.CHECK_KING_W == State ? eGameState.CHECK_MATE_W : eGameState.CHECK_MATE_B));
		}
		return r1;
	}
	@Override
	public int getSizeCurrentElem() {
		return log.getSizeCurrentElem();
	}
	@Override
	public Collection<Pair<Position, Position>> getForward() {
		return log.getForward();
	}
	@Override
	public boolean GoBackward() {
		Pair<Position, Position> p = null;
		if (null == (p = log.GoBackward()))
			return false;
		elem.get(elem.indexOf(p.GetLeft())).SetPosition(p.GetRight());
		return true;
	}
	@Override
	public boolean goForward(int index) {
		return log.goForward(index);
	}
	@Override
	public BoardGame getBoardGame() {
		return elem;
	}
}
