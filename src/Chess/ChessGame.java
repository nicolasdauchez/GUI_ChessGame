/**
 * 
 */
package Chess;
import java.util.Collection;
import java.util.List;

import main.Pair;
/**
 * @author Lumy-
 * Implement {@link IChessGame} More Readable
 */
public class ChessGame implements IChessGame {
	/**
	 * {@link BoardGame}
	 */
	private BoardGame		elem;
	/**
	 * {@link Log}
	 */
	private Log 			log;
	/**
	 * Current Turn of Game
	 */
	private eColor			Turn;
	/**
	 * Current State of Game
	 */
	private eGameState		State;
	/**
	 * Default Constructor
	 */
	public ChessGame() {
		State = eGameState.NEXT;
		Turn = eColor.White;
		log = new Log("User1", "User2");
		elem = new BoardGame();
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
	 * {@link Pair}<{@link String} White, {@link String} Black>
	 * @return
	 */
	@Override
	public Pair<String, String>	GetPlayersName() {
		return log.GetPlayersName();
	}
	/**
	 * Like Next Turn but don't call {@link BoardGame#NextTurn()}
	 * @param e
	 */
	private void PrevTurn(eGameState e) {
		Turn = (Turn == eColor.Black) ? eColor.White : eColor.Black;
		SetState(e);
	}
	/**
	 * Set the End of Game in {@link Log#addResult(String)}
	 */
	private void EndGame() {
		String res = "*";
		if (State == eGameState.CHECK_MATE_B || State == eGameState.CHECK_KING_B)
			res = "0-1";
		else if (State == eGameState.CHECK_MATE_W || State == eGameState.CHECK_KING_W)
			res = "1-0";
		else if (State == eGameState.DRAW)
			res = "1/2-1/2";
		log.addResult(res);
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
	 * Set current Players Lose by Draw
	 */
	@Override
	public void AskDraw() {
		SetState(eGameState.DRAW);
		EndGame();
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
	@Override
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
	@Override
	public eColor isCheckKing() {
		if (State == eGameState.CHECK_KING_B)
			return eColor.Black;
		else if (State == eGameState.CHECK_KING_W)
			return eColor.White;
		return eColor.None;
	}
	/**
	 * Return {@link #State} == {@link eGameState#DRAW}
	 */
	@Override
	public boolean isDraw() {
		return State == eGameState.DRAW;
	}
	private void NewGame(Pair<String, String> p) {
		NewGame(p.GetLeft(), p.GetRight());
	}
	@Override
	public void	NewGame(String nameWhite, String nameBlack) {
		log.newGame(nameWhite, nameBlack);
		elem.newGame();
		State = eGameState.NEXT;
		Turn = eColor.White;
	}
	@Override
	public boolean isPromotion(Position pos) {
		if (elem.contains(pos))
			if (Rules.isPromotion(elem.get(elem.indexOf(pos))))
				return true;
		return false;
	}
	/**
	 * {@link BoardGame#Promotion(Position, ePawns)}
	 * {@link Log#LogPromotion(ePawns)}
	 */
	@Override
	public void		DoPromotion(Position p, ePawns c) {
		if (!Rules.OptionalRules.Promotion)
		return ;
		elem.Promotion(p, c);
		log.LogPromotion(c);
	}
	/**
	 * Assuming that This function it's called after get the CASTLING state that has already check if you've the right
	 * @param firstClick
	 * @param secondClick
	 */
	@Override
	public void DoCastling(Position click1, Position click2) {
		if (!Rules.OptionalRules.Castling)
		return;
		Position kp = elem.getKingPosition(elem.get(elem.indexOf(click1)).GetColor());
		Pair<Position, Position> r = Rules.Castling.getPositionKing(kp, (kp.equals(click2) ? click1 : click2));
		Pawn k = elem.get(elem.indexOf(kp));
		Pawn t = elem.get(elem.indexOf(kp.equals(click1) ? click2 : click1));
		k.SetPosition(r.GetLeft());
		t.SetPosition(r.GetRight());
		log.addString((r.GetLeft().column == 'c' ? "O-O-O" : "O-O"), eGameState.NEXT);
		NextTurn(eGameState.NEXT);
	}
	/**
	 * Private Function called if {@link #State} == {@link eGameState#CHECK_KING_B} || {@link eGameState#CHECK_KING_W}
	 * do The Move if It's in the Possibility of played
	 * @param firstClick
	 * @param secondClick
	 * @return
	 */
	private Pair<eMoveState, eGameState> Check_King_Way(Position firstClick, Position secondClick) {
		Pair<eMoveState, eGameState> r2;
		List<Pair<Position, Position>> r;
		r = elem.getListPositionPossibleProtectKing((State == eGameState.CHECK_KING_B ? eColor.Black : eColor.White));
		if (r.size() == 0) {
			EndGame();
			return new Pair<eMoveState, eGameState>(eMoveState.SUCCESS, (eGameState.CHECK_KING_W == State ? eGameState.CHECK_MATE_W : eGameState.CHECK_MATE_B));
		}
		r2 = Rules.DoMovePawns(r, elem.get(elem.indexOf(firstClick)), secondClick, elem);
		if (r2.GetRight() != eGameState.SAME) {
			log.add(new Position(firstClick), new Position(secondClick), (elem.isEatThing() ? elem.getLastEatThing().GetClass() : null), State);
			NextTurn(r2.GetRight());
		}
		return r2;
	}
	/**
	 * {@link IChessGame#catchEvent(Position, Position)}
	 */
	public Pair<eMoveState, eGameState> catchEvent(Position firstClick, Position secondClick) {
		Pair<eMoveState, eGameState> r1 = new Pair<eMoveState, eGameState>(eMoveState.FAIL_UNAUTHORIZED, eGameState.SAME);
		if (State == eGameState.DRAW) { // Draw make endGame
			EndGame();
			return r1;
		}
		if (State == eGameState.CHECK_KING_B || State == eGameState.CHECK_KING_W) // if Check Do Check_King_Way
			return Check_King_Way(firstClick, secondClick);
		r1 = Rules.DoMovePawns(elem.get(elem.indexOf(firstClick)), secondClick, elem); // Else doMovePawns
		if (r1.GetRight() != eGameState.SAME) { // If not Same so kind of success
			if (r1.GetLeft() != eMoveState.CASTLING) // If Castling not Log there.
				log.add(new Position(firstClick), new Position(secondClick), (elem.isEatThing() ? elem.getLastEatThing().GetClass() : null), State); // Log
			NextTurn(r1.GetRight()); // NextTurn
		}
		if ((State == eGameState.CHECK_KING_B || State == eGameState.CHECK_KING_W) && // After Move one player in check
		   (elem.getObstacleCase(secondClick) == eColor.Black ? eGameState.CHECK_KING_B : eGameState.CHECK_KING_W) != State) { // current player not in check
			List<Pair<Position, Position>> r;
			if (State == eGameState.CHECK_KING_B)
				r = elem.getListPositionPossibleProtectKing(eColor.Black);
			else
				r = elem.getListPositionPossibleProtectKing(eColor.White);
			if (r.size() == 0) { // Is Making a Check Mate
				r1 = new Pair<eMoveState, eGameState>(eMoveState.SUCCESS, (eGameState.CHECK_KING_W == State ? eGameState.CHECK_MATE_W : eGameState.CHECK_MATE_B));
				SetState(r1.GetRight());
				EndGame();
			}
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
	public boolean goBackward() {
		if (!log.goBackward(elem, (Turn == eColor.Black ? eColor.White : eColor.Black)))
			return false;
		PrevTurn(log.GetCurrentState());
		return true;
	}
	@Override
	public boolean goForward(int index) {
		if (!log.goForward(elem, index, (Turn)))
			return false;
		NextTurn(log.GetCurrentState());
		return true;
	}
	@Override
	public boolean goForward() {
		if (!log.goForward(elem, (Turn)))
			return false;
		NextTurn(log.GetCurrentState());
		return true;
	}
	@Override
	public BoardGame getBoardGame() {
		return elem;
	}
	@Override
	public boolean canGoForward() {
		return log.canGoForward();
	}
	@Override
	public boolean canGoBackward() {
		return log.canGoBackward();
	}
	@Override
	public boolean Import(String path) {
		boolean r = log.Import(path);
		NewGame(log.GetPlayersName());
		if (r) while (false != goForward());
		return r;
	}
	@Override
	public boolean Export(String path) {
		while (false != goBackward()); // Log Since the First Move
		boolean r = log.Export(path);
		while (false != goForward()); // Go Back to Last Move
		return r;
	}
}
