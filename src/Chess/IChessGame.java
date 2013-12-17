/**
 * 
 */
package Chess;

import java.util.Collection;

import main.Pair;

/**
 * Interface for easy use/read of {@link ChessGame}
 * It's allow you to use some function connected to {@link Log}. Features Log allow you to move in a tree of played, and to make new move.
 * Also some Function In {@link ChessGame}
 * @author Lumy-
 *
 */
public interface IChessGame {
	/**
	 * {@link Log#getSizeCurrentElem()}
	 * @return
	 */
	public int 											getSizeCurrentElem();
	/**
	 * {@link Log#getForward()}
	 * @return
	 */
	public Collection<Pair<Position, Position>>			getForward();
	/**
	 * Go Backward in the Game. false is your at the begining of the game.
	 * @return
	 */
	public boolean 										goBackward();
	/**
	 * Go Forward in the Game. False: index doesn't exist
	 * @param index
	 * @return
	 */
	public boolean 										goForward(int index);
	/**
	 * Go Forward in the Game, with Default index. false is your at the end.
	 * @return
	 */
	public boolean 										goForward();
	/**
	 * return True if you can Backward
	 * @return
	 */
	public boolean 										canGoBackward();
	/**
	 * return true if you can Forward
	 * @return
	 */
	public boolean 										canGoForward();
	/**
	 * {@link Log#Import(String)}
	 * @param path
	 * @return
	 */
	public boolean 										Import(String path);
	/**
	 * {@link Log#Export(String)}
	 * @param path
	 * @return
	 */
	public boolean 										Export(String path);

	/**
	 * Create a new Game.
	 * @return
	 */
	public void											NewGame(String nameWhite, String nameBlack);
	/**
	 * return the BoardGame. that you can iterate over each elem.
	 * @return
	 */
	public BoardGame									getBoardGame();
	/**
	 * Main Function: do the move @param firstClick to @paramsecondClick
	 * @param firstClick
	 * @param secondClick
	 * @return
	 */
	public Pair<eMoveState, eGameState>		 			catchEvent(Position firstClick, Position secondClick);
	/**
	 * Return Player Turn
	 * @return
	 */
	public eColor 										GetTurn();
	/**
	 * If Both player want a {@link eGameState#DRAW}
	 * Set end Game.
	 */
	public void						 					AskDraw();
	/**
	 * return {@link eColor#None} if None is CheckMate
	 * @return
	 */
	public eColor 										isCheckMat();
	/**
	 * return {@link eColor#None} if None is Check King
	 * @return
	 */
	public eColor 										isCheckKing();
	/**
	 * return True if the Game make a Draw
	 * @return
	 */
	public boolean 										isDraw();
	/**
	 * return True is the {@link Position} pos is a {@link ePawns#PAWN} and can Promod
	 * @param pos
	 * @return
	 */
	public boolean 										isPromotion(Position pos);
	/**
	 * Do The Promotion. if {@link Rules.OptionalRules#Promotion}
	 * @param p
	 * @param c
	 */
	public void											DoPromotion(Position p, ePawns c);
	/**
	 * Do a Castling if {@link Rules.OptionalRules#Castling}
	 * @param click1
	 * @param click2
	 */
	public void 										DoCastling(Position click1, Position click2);
}
