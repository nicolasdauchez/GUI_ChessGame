/**
 * 
 */
package Chess;

/**
 * @author Lumy-
 * Represent a Position in the Chess Game
 * By {@value #column} and {@value #row}
 */
public class Position {
	/**
	 * Row: Int. from 1 to 8
	 */
	public int 		row;
	/**
	 * column: char. from 'a'-'h'
	 */
	public char  column;
	/**
	 * Default Constructor.
	 * euuuuh Deprecated.
	 */
	public Position()
	{
		row = 0;
		column = '\0';
	}
	/**
	 * Constructor
	 * Should use
	 * @param c
	 * @param i
	 */
	public Position(char c, int i)
	{
		this(i,c);
	}
	/**
	 * Constructor
	 * Should use
	 * @param i
	 * @param c
	 */
	public Position(int i, char c)
	{
		row = i;
		column = c;
	}
	/**
	 * Constructor by Copy
	 * @param p
	 */
	public Position(Position p)
	{
		row = p.row;
		column = p.column;
	}
	/**
	 * {@link #row} = p.{@link #row}
	 * {@link #column} = p.{@link #column}
	 * @param p
	 */
	public void SetPosition(Position p) {
		row = p.row;
		column = p.column;
	}
	/**
	 * toString Function
	 */
	@Override
	public String toString() {
		return "R,C[" + row+ "," +column+"]";
	}
	/**
	 * Compare 
	 * {@link #row} = o.{@link #row}
	 * {@link #column} = o.{@link #column}
	 */
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
	/**
	 * return the Difference of Row beetwen this and p.
	 * @param p
	 * @return
	 */
	public int diffRow(Position p) {
		int res = p.row - row;
		return (res < 0 ? res * -1 : res);
	}
	/**
	 * return the Difference of Row beetwen this and p.
	 * @param p
	 * @return
	 */
	public int diffColumn(Position p) {
		int res = Character.toLowerCase(p.column) - Character.toLowerCase(column);
		return (res < 0 ? res * -1 : res);
	}
	/**
	 * Check for {@link #diffRow(Position)} or {@link #diffColumn(Position)}
	 * else do checkDiagonal
	 * @param p
	 * @return
	 */
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
