/**
 * 
 */
package Chess;

/**
 * @author Lumy-
 *
 */
public class Pawn {
	private eColor color;
	private Position position;
	private ePawns type;
	private boolean initialePosition;
	private boolean isSeeingAdverseKing;

	public Pawn(eColor c, Position p, ePawns t) {
		color = c;
		position = p;
		type = t;
		initialePosition = true;
	}
	public void print()
	{
		System.out.println((color == eColor.Black ? "black - " : "white - ") + type +  " [" + position.column + "," + position.row+"]");
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
	public eColor GetColor() {
		return color;
	}
	public void SetPosition(Position p)
	{
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
}
