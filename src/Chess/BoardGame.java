/**
 * 
 */
package Chess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lumy-
 *
 */
public class BoardGame {
	private List<Pawn>	elem;

	public BoardGame()
	{
		elem = new ArrayList<Pawn>();
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		return elem.size();
	}
	/**
	 * Private Function Index Of
	 * To use with different List
	 * @param p
	 * @param s
	 * @return
	 */
	private int indexOf(List<Pawn> p, Position s) {
		int c = 0;
		for (Pawn e : p)
		{
			if (e.equals(s))
				return c;
			c++;
		}
		return -1;
	}
	public int indexOf(Position s) {
		return indexOf(elem, s);
	}
	public int indexOf(Pawn p) {
		return indexOf(p.GetPosition());
	}
	public Pawn get(int c) {
		return elem.get(c);
	}
	public boolean contains(Position e) {
		for (Pawn p : elem)
			if (p.equals(e))
				return true;
		return false;
	}
	public void add(Pawn pion) {
		// TODO Auto-generated method stub
		elem.add(pion);
	}
	public void remove(Pawn eaten) {
		elem.remove(indexOf(eaten));
	}
 	public void print() {
		int i = 0;
		for (Pawn p : elem) {
			System.out.print(i+": ");
			p.print();
			i++;
		}
	}

	/**
	 * return eColor.None if no Pawn on the current Case.
	 * @param p
	 * @return
	 */
	public eColor getObstacleCase(Position p) {
		int i = -1;
		i = indexOf(p);
		if (i != -1)
			return get(i).GetColor();
		return eColor.None;
	}
	/**
	 * return eColor.None if Nothing is find on the way Row.
	 * Don't Check the Last Case Neither the First (always True position First Pawn
	 * @param p
	 * @param range
	 * @return
	 */
	public eColor getObstacleRowRange(Pawn p, Position range) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		t.row += (t.row < range.row ? 1 : - 1);
		while (t.row != range.row)
		{
			if (eColor.None != getObstacleCase(t))
				return getObstacleCase(t);
			t.row += (t.row < range.row ? 1 : - 1);
		}
		return eColor.None;
	}
	/**
	 * return eColor.None if Nothing is find on the way Column.
	 * Don't Check the Last Case Neither the First (always True position First Pawn
	 * @param p
	 * @param range
	 * @return
	 */
	public eColor getObstacleColumnRange(Pawn p, Position max) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		t.column += (t.column < max.column ? 1 : -1);
		while (t.column != max.column)
		{
			System.out.print("Case: ");
			t.print();
			max.print();
			if (eColor.None != getObstacleCase(t))
				return getObstacleCase(t);
			t.column += (t.column < max.column ? 1 : -1);
		}
		return eColor.None;
	}
	/**
	 * Check if it's Range Row,Column,Diagonal
	 * and call the good method
	 * @isObstacleColumnRange
	 * @code isObstacleRowRange
	 * @param p
	 * @param newPos
	 * @return
	 */
	public eColor getObstacleRange(Pawn p, Position newPos) {
		Position oldp = p.GetPosition();
		if (oldp.sameRow(newPos))
			return getObstacleColumnRange(p, newPos);
		else if (oldp.sameColumn(newPos))
			return getObstacleRowRange(p, newPos);
		else if (oldp.DiagonalMove(newPos)) {
			System.out.println("DiagonalMove");
			return getObstacleRangeDiagonal(p, newPos);
		}
		return eColor.None;
	}
	/**	
	 * return eColor.None if Nothing is find on the way Column/Row.
	 * Don't Check the Last Case Neither the First (always True position First Pawn
	 * @param p
	 * @param range
	 * @return
	 */
	private eColor getObstacleRangeDiagonal(Pawn p, Position newPos) {
		Position	tmp = new Position();
		tmp.SetPosition(p.GetPosition());
		tmp.column += (newPos.column < tmp.column ? -1 : 1);
		tmp.row += (newPos.row < tmp.row ? -1 : 1);
		while (!tmp.sameRow(newPos) && !tmp.sameColumn(newPos)) {
			if (eColor.None != getObstacleCase(tmp))
				return getObstacleCase(tmp);
			tmp.column += (newPos.column < tmp.column ? -1 : 1);
			tmp.row += (newPos.row < tmp.row ? -1 : 1);
		}
		return eColor.None;
	}

	/**
	 * return True if isOutside of the Game
	 * @param p
	 * @return
	 */
	public boolean isOutside(Position p) {
		if (p.row < 1 || p.row > 8 || Character.toLowerCase(p.column) < 'a' || Character.toLowerCase(p.column) > 'h')
			return true;
		return false;
	}

	/**
	 * return a new copy of a List<Pawn> with new position set
	 * @param p
	 * @param newPos
	 * @return
	 */
	public List<Pawn> getNewCopie(Pawn p, Position newPos) {
		List<Pawn> ret= new ArrayList<Pawn>(elem);
		if (-1 != ret.indexOf(newPos))
			ret.remove(ret.indexOf(newPos));
		ret.get(indexOf(ret, p.GetPosition()));
		return ret;
	}

	
}
