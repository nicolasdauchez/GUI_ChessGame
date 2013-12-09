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
	public void print() {
		System.out.print("R,C[" + row+ "," +column+"]" );
	}
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
	public void SetPosition(Position p) {
		row = p.row;
		column = p.column;
	}
	@Override
	public boolean equals(Object o)
	{
		if (o != null && o.getClass() == this.getClass()) {
			return (row == ((Position)o).row && Character.toLowerCase(((Position)o).column) == Character.toLowerCase(column));
		}
		return false;
	}
	/**
	 * Check if Same Column
	 * @param newPos
	 * @return
	 */
	public boolean sameColumn(Position newPos) {
		return Character.toLowerCase(newPos.column) == Character.toLowerCase(column);
	}
	/**
	 * Check if Same Row
	 * @param newPos
	 * @return
	 */
	public boolean sameRow(Position newPos) {
		return (row == newPos.row); 
	}
	/**
	 * Check if Is a good diagonal move
	 * @param newPos
	 * @return
	 */
	public boolean DiagonalMove(Position p) {
		int r1 = diffColumn(p);
		if (r1 != diffRow(p))
			return false;
		return true;
	}
	public int diffRow(Position p) {
		int res = p.row - row;
		return (res < 0 ? res * -1 : res);
	}
	public int diffColumn(Position p) {
		int res = Character.toLowerCase(p.column) - Character.toLowerCase(column);
		return (res < 0 ? res * -1 : res);
	}
	public int diffMultiple(Position p) {
		if (p.row != row && p.column == column)
			return diffRow(p);
		else if (p.row == row && p.column != column)
			return diffColumn(p);
		int r1 = diffColumn(p);
		if (r1 != diffRow(p))
			return -1;
		return r1;
	}
	/**
	 * Return White or Black
	 * @return
	 */
	public eColor GetColorCase()
	{
		if ((row % 2 == 0 && column%2 == 0) 
			|| (row % 2 != 0 && column%2 != 0))
			return eColor.Black;
		return eColor.White;
	}
}
