/**
 * 
 */
package Chess;

import java.util.Collection;

import main.Pair;

/**
 * Interface for easy use/read of {@link ChessGame}
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
	public boolean 										goBackward();
	public boolean 										goForward(int index);
	public boolean 										goForward();
	public boolean 										canGoBackward();
	public boolean 										canGoForward();
	public boolean 										Import(String path);
	public boolean 										Export(String path);

	/**
	 * ChessGameFunction
	 * @return
	 */
	public void											NewGame(String nameWhite, String nameBlack);
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
