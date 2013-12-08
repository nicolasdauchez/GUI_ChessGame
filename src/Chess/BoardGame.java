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
	public int indexOf(List<Pawn> p, Position s) {
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
	public Pawn get(List<Pawn> l, int c) {
		return l.get(c);
	}
	public Pawn get(int c) {
		return get(elem, c);
	}
	public boolean contains(List<Pawn> l, Position e) {
		for (Pawn p : l)
			if (p.equals(e))
				return true;
		return false;
	
	}
	public boolean contains(Position e) {
		return contains(elem, e);
	}
	public void add(Pawn pion) {
		elem.add(pion);
	}
	public void remove(Pawn eaten) {
		elem.remove(indexOf(eaten));
	}
 	public void print() {
 		print(elem);
 	}
	public void print(List<Pawn> el) {
		int i = 0;
		for (Pawn p : el) {
			System.out.print(i+": ");
			p.print();
			i++;
		}
	}
	/**
	 *
	 * return eColor.None if no Pawn on the current Case in l
	 * @param l
	 * @param p
	 * @return
	 */
	public eColor getObstacleCase(List<Pawn> l, Position p) {
		int i = -1;
		i = indexOf(l, p);
		if (i != -1)
			return get(l, i).GetColor();
		return eColor.None;
	}
	/**
	 * return eColor.None if no Pawn on the current Case.
	 * @param p
	 * @return
	 */
	public eColor getObstacleCase(Position p) {
		return getObstacleCase(elem, p);
	}
	/**
	 * return eColor.None if Nothing is find on the way Row in l.
	 * Don't Check the Last Case Neither the First (always True position First Pawn
	 * @param l
	 * @param p
	 * @param range
	 * @return
	 */
	public eColor getObstacleRowRange(List<Pawn> l, Pawn p, Position range) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		t.row += (t.row < range.row ? 1 : - 1);
		while (t.row != range.row)
		{
			if (eColor.None != getObstacleCase(l, t))
				return getObstacleCase(l, t);
			t.row += (t.row < range.row ? 1 : - 1);
		}
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
		return getObstacleRowRange(elem, p, range);
	}
	/**
	 * return eColor.None if Nothing is find on the way Column in l.
	 * Don't Check the Last Case Neither the First (always True position First Pawn
	 * @param l
	 * @param p
	 * @param max
	 * @return
	 */
	public eColor getObstacleColumnRange(List<Pawn> l, Pawn p, Position max) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		t.column += (t.column < max.column ? 1 : -1);
		while (t.column != max.column)
		{
			if (eColor.None != getObstacleCase(l, t))
				return getObstacleCase(l, t);
			t.column += (t.column < max.column ? 1 : -1);
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
		return getObstacleColumnRange(elem, p, max);
	}
	/**
	 * Check if it's Range Row,Column,Diagonal in l
	 * and call the good method
	 * @param l
	 * @param p
	 * @param newPos
	 * @return
	 */
	public eColor getObstacleRange(List<Pawn> l, Pawn p, Position newPos) {
		Position oldp = p.GetPosition();
		if (oldp.sameRow(newPos))
			return getObstacleColumnRange(l, p, newPos);
		else if (oldp.sameColumn(newPos))
			return getObstacleRowRange(l, p, newPos);
		else if (oldp.DiagonalMove(newPos)) {
			return getObstacleRangeDiagonal(l, p, newPos);
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
		return getObstacleRange(elem, p, newPos);
	}
	/**
	 * Return Position First Obstacle in l
	 * @param l
	 * @param p
	 * @param newPos
	 * @return
	 */
	public Position getPositionFirstObstacleRange(List<Pawn> l, Pawn p, Position newPos) {
		Position oldp = p.GetPosition();
		if (oldp.sameRow(newPos)) {
			System.out.println("Test Position Row");
			return getPositionFirstObstacleColumnRange(l, p, newPos);
		}
		else if (oldp.sameColumn(newPos)) {
			System.out.println("Test Position Column");
			return getPositionFirstObstacleRowRange(l, p, newPos);
		}
		else if (oldp.DiagonalMove(newPos)) {
			System.out.println("Test Position Diagonal");
			return getPositionFirstObstacleRangeDiagonal(l, p, newPos);
		}
		return null;

	}
	/**
	 * return Position of first find on the way Column in l.
	 * @param l
	 * @param p
	 * @param max
	 * @return
	 */
	public Position getPositionFirstObstacleColumnRange(List<Pawn> l, Pawn p, Position max) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		t.column += (t.column < max.column ? 1 : -1);
		while (t.column != max.column)
		{
			if (eColor.None != getObstacleCase(l, t))
				return get(l, indexOf(l, t)).GetPosition();
			t.column += (t.column < max.column ? 1 : -1);
		}
		return null;
	}
	/**
	 * return Position if Nothing is find on the way Row in l.
	 * @param l
	 * @param p
	 * @param range
	 * @return
	 */
	public Position getPositionFirstObstacleRowRange(List<Pawn> l, Pawn p, Position range) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		t.row += (t.row < range.row ? 1 : - 1);
		while (t.row != range.row)
		{
			if (eColor.None != getObstacleCase(l, t))
				return get(l, indexOf(l, t)).GetPosition();
			t.row += (t.row < range.row ? 1 : - 1);
		}
		return null;
	}
	private Position getPositionFirstObstacleRangeDiagonal(List<Pawn> l, Pawn p, Position newPos) {
		Position	tmp = new Position();
		tmp.SetPosition(p.GetPosition());
		tmp.column += (newPos.column < tmp.column ? -1 : 1);
		tmp.row += (newPos.row < tmp.row ? -1 : 1);
		while (!tmp.sameRow(newPos) && !tmp.sameColumn(newPos)) {
			System.out.print("Check Pos: ");
			tmp.print();
			if (eColor.None != getObstacleCase(l, tmp)) {
				tmp.print();
				return tmp;
			}
			tmp.column += (newPos.column < tmp.column ? -1 : 1);
			tmp.row += (newPos.row < tmp.row ? -1 : 1);
		}
		return null;
	}
	private eColor getObstacleRangeDiagonal(List<Pawn> l, Pawn p, Position newPos) {
		Position	tmp = new Position();
		tmp.SetPosition(p.GetPosition());
		tmp.column += (newPos.column < tmp.column ? -1 : 1);
		tmp.row += (newPos.row < tmp.row ? -1 : 1);
		while (!tmp.sameRow(newPos) && !tmp.sameColumn(newPos)) {
			if (eColor.None != getObstacleCase(l, tmp)) {
				System.out.print("Check Pos:ColorFound ");get(l, indexOf(l, tmp)).print();
				return getObstacleCase(l, tmp);
			}
			tmp.column += (newPos.column < tmp.column ? -1 : 1);
			tmp.row += (newPos.row < tmp.row ? -1 : 1);
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
	public eColor getObstacleRangeDiagonal(Pawn p, Position newPos) {
		return getObstacleRangeDiagonal(elem, p, newPos);
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
		List<Pawn> ret= new ArrayList<Pawn>(elem.size());
	    for(Pawn item: elem) ret.add(item.clone());
		if (-1 != indexOf(ret, newPos))
			ret.remove(indexOf(ret, newPos));
		ret.get(indexOf(ret, p.GetPosition())).SetPosition(newPos);
		return ret;
	}
	public Position getPawnsBoardPosition(eColor e, ePawns c, List<Pawn> l)
	{
		for (Pawn p : l)
    		if (p.GetClass() == c && p.GetColor() == e)
				return p.GetPosition();
		return null;
	}
	private Position getPawnsPosition(eColor e, ePawns c)
	{
		return getPawnsBoardPosition(e, c, elem);
	}
	public Position getKingPosition(eColor e) {
		return getPawnsPosition(e, ePawns.King);
	}

	public List<Pawn> getAllColor(eColor e) {
		List<Pawn> ret = new ArrayList<Pawn>();
	    for(Pawn item: elem)
	    	if (item.GetColor() == e)
	    		ret.add(item.clone());
		return ret;
	}	
}
