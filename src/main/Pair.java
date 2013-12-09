/**
 * Assignement1 Reversi
 * Timothee Menage - Lumy
 * Created - Oct 29, 2013 2:59:19 PM
 */
package main;

/**
 * @author Lumy-
 *
 */
public class Pair<L,R>
{

	  private final L left;
	  private final R right;

	  public Pair(L left, R right)
	  {
	    this.left = left;
	    this.right = right;
	  }

	public L GetLeft()
	  {
		  return left;
	  }
	  public R GetRight()
	  { 
		  return right; 
	  }

	  @Override
	  public int hashCode()
	  {
		  return left.hashCode() ^ right.hashCode();
	  }

	  @SuppressWarnings("unchecked")
	  @Override
	  public boolean equals(Object o)
	  {
	    if (o == null) return false;
	    if (!(o instanceof Pair)) return false;
	    return this.left.equals(((Pair<L, R>)o).GetLeft()) &&
	           this.right.equals(((Pair<L,R>)o).GetRight());
	  }

}