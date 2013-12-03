/**
 * 
 */
package Chess;

/**
 * @author Lumy-
 *
 */
public class Pion {
	private eColor color;
	private Position position;
	private eClass type;

	public Pion(eColor c, Position p, eClass t) {
		color = c;
		position = p;
		type = t;
	}
	public void print()
	{
		System.out.println((color == eColor.Black ? "black - " : "white - ") + type +  " [" + position.column + "," + position.row+"]");
	}
	public boolean equals(Object t)
	{
		if (t != null && t.getClass() == Position.class)
			return (((Position)t).equals(this.position));
		return false;
	}

	public eClass GetClass() {
		return type;
	}
	public eColor GetColor() {
		return color;
	}
	public void SetPosition(Position p)
	{
		position.SetPosition(p);
	}
	
	public Position GetPosition() {
		return position;
	}
}
