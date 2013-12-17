package Chess;

/**
 * Represent a State Move
 * {@link #SUCCESS} is if all is good, and the game is keep continuing
 * {@link #CASTLING} is when the player click for a {@link Rules.Castling}. Need user approval
 * {@link #FAIL_CHECK} is when the player is in {@link eGameState#CHECK_KING_B} and can't do the mouvement.
 * {@link #FAIL_CLASS_UNKNOWN} should never happen. the {@link ePawns} of the Pawn is not Known
 * {@link #FAIL_PAWNS_EAT_FORWARD} is when a {@link ePawns#PAWN} try to eat forward
 * {@link #FAIL_PAWNS_BACKWARD} is when a {@link ePawns#PAWN} try to go backward
 * {@link #FAIL_SAME_COLOR_CASE_OCCUPIED} is when the secondClick of Player is occupied by the same {@link eColor}
 * {@link #FAIL_UNAUTHORIZED} is an when a {@link ePawns} try to go somewhere he can't.
 * @author Lumy-
 *
 */
public enum eMoveState {
	SUCCESS,
	CASTLING,
	FAIL_UNAUTHORIZED,
	FAIL_CHECK,
	FAIL_PAWNS_BACKWARD,
	FAIL_PAWNS_EAT_FORWARD,
	FAIL_CLASS_UNKNOWN,
	FAIL_SAME_COLOR_CASE_OCCUPIED
}
