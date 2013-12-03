/**
 * 
 */
package Chess;

/**
 * @author Lumy-
 *
 */
public class Position {
	public int 		row;
	public char  column;
	public Position()
	{
		row = 0;
		column = '\0';
	}
	public Position(Position p)
	{
		row = p.row;
		column = p.column;
	}
	
	public boolean equals(Object o)
	{
		if (o != null && o.getClass() == this.getClass())
			return (row == ((Position)o).row && Character.toLowerCase(((Position)o).column) == Character.toLowerCase(column));
		return false;
	}
	public boolean sameColumn(Position newPos) {
		return Character.toLowerCase(newPos.column) == Character.toLowerCase(column);
	}
	public boolean sameRow(Position newPos) {
		return (row == newPos.row); 
	}
	public void SetPosition(Position p) {
		row = p.row;
		column = p.column;
	}
}
