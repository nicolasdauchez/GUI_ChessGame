/**
 * 
 */
package Chess;

/**
 * @author Lumy-
 *
 */
public class Pawn implements Cloneable  {
	private eColor color;
	private Position position;
	private ePawns type;
	private boolean enPassant; // implem rules EnPassant http://en.wikipedia.org/wiki/En_passant
	private boolean initialePosition;
	private boolean next;
	
	public Pawn(Pawn e) {
		color = e.GetColor();
		position = new Position();
		SetPosition(e.GetPosition());
		type = e.GetClass();
		initialePosition = e.isStartPosition();		
		next = false;
	}
	public Pawn(eColor c, Position p, ePawns t) {
		color = c;
		position = p;
		type = t;
		enPassant = false;
		initialePosition = true;
	}
	public void print()
	{
		System.out.println((color == eColor.Black ? "black - " : "white - ") + type +  " [" + position.column + "," + position.row+"]");
	}
	@Override
	public Pawn clone() {
		return new Pawn(this);
	}
	@Override
	public boolean equals(Object t)
	{
		if (t != null && t.getClass() == Position.class)
			return (((Position)t).equals(this.position));
		return false;
	}

	public ePawns GetClass() {
		return type;
	}
	public eColor GetEnemyColor() {
	 return (color == eColor.Black ? eColor.White : eColor.Black);
	}
	public eColor GetColor() {
		return color;
	}
	public void SetPosition(Position p)
	{
		if (initialePosition = true && position.diffMultiple(p) == 2 && type == ePawns.Pawn)
			enPassant = true;
		initialePosition = false;
		position.SetPosition(p);
	}
	
	public Position GetPosition() {
		return position;
	}
	/*
	 * Return true if the direction is good
	 * Use only with Pion
	 */
	public boolean isGoodDirection(Position p) {
		return ((color == eColor.Black && position.row > p.row) ||
				(color == eColor.White && position.row < p.row));
	}
	/*
	 * Return true if the direction is not good
	 * Use only with Pion
	 */
	public boolean isNotGoodDirection(Position p) {
		return !isGoodDirection(p);
	}
	public boolean isStartPosition() {
		return initialePosition;
	}
	public void NextTurn() {
		if (enPassant && next)
			enPassant = false;
		else if (enPassant) // permit to wait one turn to off enPassant State
			next = true;
	}
	public boolean enPassant() {
		return enPassant;
	}
	public void Promotion(ePawns c) {
		if (!Rules.OptionalRules.Promotion)
			return ;
		int row = 1;
		if (color != eColor.Black)
			row = 8;
		if (type == ePawns.Pawn || position.row == row)
			type = c;		
	}
}
