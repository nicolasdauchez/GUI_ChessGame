/**
 * 
 */
package Chess;

/**
 * @author Lumy-
 * Represent Any Pawn. Implement Cloneable to facilite the Copy.
 */
public class Pawn implements Cloneable  {
	/**
	 * {@link eColor} color Pawn
	 */
	private eColor color;
	/**
	 * {@link Position} Position Pawn
	 */
	private Position position;
	/**
	 * {@link ePawns} Class/Type, is it {@link ePawns#KING} or {@link ePawns#QUEEN}
	 */
	private ePawns type;
	/**
	 * Implementation of the rules EnPassant @see <a href='http://en.wikipedia.org/wiki/En_passant'>
	 */
	private boolean enPassant;
	/**
	 * Initial Position or not.
	 */
	private boolean initialePosition;
	/**
	 * To Facilite the Implementation of {@link #enPassant} to do a timer on each Player turn
	 */
	private boolean next;
	/**
	 * Constructor By Copy
	 * @param e
	 */
	public Pawn(Pawn e) {
		color = e.GetColor();
		position = new Position();
		SetPosition(e.GetPosition());
		type = e.GetClass();
		initialePosition = e.isStartPosition();		
		next = false;
	}
	/**
	 * Default Constructor
	 * @param c
	 * @param p
	 * @param t
	 */
	public Pawn(eColor c, Position p, ePawns t) {
		color = c;
		position = p;
		type = t;
		enPassant = false;
		initialePosition = true;
		next = false;
	}
	/**
	 * toString Function.
	 */
	@Override
	public String toString() {
		return (color == eColor.Black ? "black - " : "white - ") + type +  " [" + position.column + "," + position.row+"]";
	}
	/**
	 * Implementation of Cloenable
	 */
	@Override
	public Pawn clone() {
		return new Pawn(this);
	}
	/**
	 * Surcharge of equals. to compare only Position (only one Pawn By Position)
	 */
	@Override
	public boolean equals(Object t) {
		if (t != null && t.getClass() == Position.class)
			return (((Position)t).equals(this.position));
		return false;
	}
	/**
	 * @return
	 */
	public ePawns GetClass() {
		return type;
	}
	/**
	 * @return
	 */
	public eColor GetEnemyColor() {
	 return (color == eColor.Black ? eColor.White : eColor.Black);
	}
	/**
	 * @return
	 */
	public eColor GetColor() {
		return color;
	}
	/**
	 * Set {@link Position} p in {@link #position}.
	 * if Pawn and Move from 2 square and first move enPassant is true
	 * initialPosition to false
	 * @param p
	 */
	public void SetPosition(Position p) {
		if (initialePosition = true && position.diffMultiple(p) == 2 && type == ePawns.PAWN)
			enPassant = true;
		initialePosition = false;
		position.SetPosition(p);
	}
	/**
	 * @return
	 */
	public Position GetPosition() {
		return position;
	}
	/*
	 * Return true if the direction is good
	 * Use only with {@value ePawns#Pawn}
	 */
	public boolean isGoodDirection(Position p) {
		return ((color == eColor.Black && position.row > p.row) ||
				(color == eColor.White && position.row < p.row));
	}
	/*
	 * Return true if the direction is not good
	 * Use only with {@value ePawns#Pawn}
	 */
	public boolean isNotGoodDirection(Position p) {
		return !isGoodDirection(p);
	}
	/**
	 * is {@link #initialePosition}
	 * @return
	 */
	public boolean isStartPosition() {
		return initialePosition;
	}
	/**
	 * Use with EnPassant Rules. Timer Boolean for 1 Turn
	 */
	public void NextTurn() {
		if (enPassant && next)
			enPassant = false;
		else if (enPassant) // permit to wait one turn to off enPassant State
			next = true;
	}
	/**
	 * {@link #enPassant}
	 * @return
	 */
	public boolean enPassant() {
		return enPassant;
	}
	/**
	 * Do a {@link Rules.OptionalRules#Promotion} with the {@link Pawn}
	 * @param c
	 */
	public void Promotion(ePawns c) {
		if (!Rules.OptionalRules.Promotion)
			return ;
		int row = 1;
		if (color != eColor.Black)
			row = 8;
		if (type == ePawns.PAWN || position.row == row)
			type = c;		
	}
	/**
	 * Reset all the Boolean
	 * {@link #enPassant}
	 * {@link #initialePosition}
	 * {@link #next}
	 */
	public void reset() {
		enPassant = false;
		initialePosition = true;
		next = false;
	}
}
