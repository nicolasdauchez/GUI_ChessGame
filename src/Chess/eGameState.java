/**
 * 
 */
package Chess;

/**
 *	Represent The Possible State after a move
 * {@link #SAME} reprensent Invalid Move, Same Player to Play
 * {@link #NEXT} is simple move without specifique State.
 * {@link #CHECK_MATE_B} or _W is for a Mate State. EndGame.
 * {@link #CHECK_KING_B} or _W is for a Check State. Save your {@link ePawns#KING} !
 * @author Lumy-
 */
public enum eGameState {
		NEXT,
		SAME,
		DRAW,
		CHECK_MATE_W,
		CHECK_MATE_B,
		CHECK_KING_W,
		CHECK_KING_B
}
