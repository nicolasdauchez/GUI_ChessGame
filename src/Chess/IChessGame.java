/**
 * 
 */
package Chess;

import java.util.Collection;

import main.Pair;

/**
 * @author Lumy-
 *
 */
public interface IChessGame {
	
	/**
	 * Log Functions
	 * @return
	 */
	public int 											getSizeCurrentElem();
	public Collection<Pair<Position, Position>>			getForward();
	public boolean 										GoBackward();
	public boolean 										goForward(int index);

	/**
	 * ChessGameFunction
	 * @return
	 */
	public void											NewGame(String nameWhite, String nameBlack);
	public void											NewGame(String nameWhite, String nameBlack, String round);
	public BoardGame									getBoardGame();
	public Pair<eMoveState, eGameState>		 			catchEvent(Position firstClick, Position secondClick);
	public eColor 										GetTurn();
	public void						 					AskDraw();
	public eColor 										isCheckMat();
	public eColor 										isCheckKing();
	public boolean 										isDraw();
	public boolean 										isPromotion(Position pos);
	public void											DoPromotion(Position p, ePawns c);
	public void 										DoCastling(Position click1, Position click2);

}
