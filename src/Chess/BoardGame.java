/**
 * 
 */
package Chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import main.Pair;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Lumy-
 *
 */
public class BoardGame {
	private List<Pawn>	elem;
	private List<Pawn>	Eaten;

	public BoardGame()
	{
		elem = new ArrayList<Pawn>();
		Eaten = new ArrayList<Pawn>();
	}
	
	/**
	 * Return a const Collection with all Pawn Dead.
	 * @return
	 */
	public Collection<Pawn>	GetEaten()
	{
		Collection<Pawn> constVector =
				java.util.Collections.unmodifiableCollection(Eaten);
		return constVector;
	}

	/**
	 * Return A const Iterator To Travel.
	 * @return
	 */
	public Collection<Pawn> getElem()
	{
		Collection<Pawn> constVector =
				java.util.Collections.unmodifiableCollection(elem);
		return constVector;
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
	public int indexOf(Collection<Pawn> p, Position s) {
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
	public Pawn get(Collection<Pawn> l, int c) {
		Pawn ret = CollectionUtils.get(l, c);
		return ret;
	}
	public Pawn get(int c) {
		return get(elem, c);
	}
	public boolean contains(Collection<Pawn> l, Position e) {
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
		Eaten.add(elem.remove(indexOf(eaten)));
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
	public eColor getObstacleCase(Collection<Pawn> l, Position p) {
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
	public eColor getObstacleRowRange(Collection<Pawn> l, Pawn p, Position range) {
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
	public eColor getObstacleColumnRange(Collection<Pawn> l, Pawn p, Position max) {
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
	public eColor getObstacleRange(Collection<Pawn> l, Pawn p, Position newPos) {
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
	public Position getPositionFirstObstacleRange(Collection<Pawn> l, Pawn p, Position newPos) {
		Position oldp = p.GetPosition();
		if (oldp.sameRow(newPos))
			return getPositionFirstObstacleColumnRange(l, p, newPos);
		else if (oldp.sameColumn(newPos))
			return getPositionFirstObstacleRowRange(l, p, newPos);
		else if (oldp.DiagonalMove(newPos))
			return getPositionFirstObstacleRangeDiagonal(l, p, newPos);
		return null;

	}
	/**
	 * return Position of first find on the way Column in l.
	 * @param l
	 * @param p
	 * @param max
	 * @return
	 */
	public Position getPositionFirstObstacleColumnRange(Collection<Pawn> l, Pawn p, Position max) {
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
	public Position getPositionFirstObstacleRowRange(Collection<Pawn> l, Pawn p, Position range) {
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
	private Position getPositionFirstObstacleRangeDiagonal(Collection<Pawn> l, Pawn p, Position newPos) {
		Position	tmp = new Position();
		tmp.SetPosition(p.GetPosition());
		tmp.column += (newPos.column < tmp.column ? -1 : 1);
		tmp.row += (newPos.row < tmp.row ? -1 : 1);
		while (!tmp.sameRow(newPos) && !tmp.sameColumn(newPos)) {
			if (eColor.None != getObstacleCase(l, tmp)) {
				return tmp;
			}
			tmp.column += (newPos.column < tmp.column ? -1 : 1);
			tmp.row += (newPos.row < tmp.row ? -1 : 1);
		}
		return null;
	}
	private eColor getObstacleRangeDiagonal(Collection<Pawn> l, Pawn p, Position newPos) {
		Position	tmp = new Position();
		tmp.SetPosition(p.GetPosition());
		tmp.column += (newPos.column < tmp.column ? -1 : 1);
		tmp.row += (newPos.row < tmp.row ? -1 : 1);
		while (!tmp.sameRow(newPos) && !tmp.sameColumn(newPos)) {
			if (eColor.None != getObstacleCase(l, tmp))
				return getObstacleCase(l, tmp);
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
		return getNewCopie(elem, p, newPos);
	}
	
	/**
	 * return a new copy of a List<Pawn> with new position set for List tmp
	 * @param tmp
	 * @param p
	 * @param newPos
	 * @return
	 */
	public List<Pawn> getNewCopie(Collection<Pawn> tmp, Pawn p, Position newPos) {
		List<Pawn> ret= new ArrayList<Pawn>(elem.size());
		for(Pawn item: tmp) ret.add(item.clone());
		if (-1 != indexOf(ret, newPos))
			ret.remove(indexOf(ret, newPos));
		ret.get(indexOf(ret, p.GetPosition())).SetPosition(newPos);
		return ret;
	}

	public Position getPawnsBoardPosition(eColor e, ePawns c, Collection<Pawn> l)
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
		return getAllColor(elem, e);
	}
	public List<Pawn> getAllColor(List<Pawn> p, eColor e) {
		List<Pawn> ret = new ArrayList<Pawn>();
	    for(Pawn item: p)
	    	if (item.GetColor() == e)
	    		ret.add(item.clone());
		return ret;
	}

	public void newGame() {
		elem.clear();
		Eaten.clear();
		addLinePion(eColor.Black);
		addLinePion(eColor.White);
		addHeadLine(eColor.Black);
		addHeadLine(eColor.White);
	}
	private void add(eColor c, Position p, ePawns t) {
			elem.add(new Pawn(c, p, t));
	}
	private void addLinePion(eColor c) {
		Position p = new Position();
		p.row = 7;
		if (c == eColor.White)
			p.row = 2;
		p.column = 'a';
		while (p.column != 'i') {
			add(c, new Position(p), ePawns.Pawn);
			p.column += 1;
		}
	}
	private void addHeadLine(eColor c) {
		Position p = new Position();
		p.row = 8;
		if (c == eColor.White)
			p.row = 1;
		p.column = 'a';
		ePawns e[] = { ePawns.Tower, ePawns.Cavalery, ePawns.Crazy, ePawns.King, ePawns.Queen, ePawns.Crazy, ePawns.Cavalery, ePawns.Tower };
		//if (c == eColor.White)
		//	ArrayUtils.reverse(e);
		for (int i = 0; i < e.length; i++) {
			add(c, new Position(p), e[i]);
			p.column += 1;
		}
	}
	public List<Position> AllCheckKing(eColor e) {
		return Rules.CheckKing.AllCheckKing(elem,  this, e);
	}

	private void getListPositionPossibleProtectKing(eColor e, List<Pair<Position, Position>> r) {
		for (Pawn p : elem) {
			if (p.GetClass() != ePawns.King && p.GetColor() == e)
			{
				List<Pair<Position, Position>> cp = Rules.MapFunctor.GetPossibleMoveProtect(p, this);
				for (Pair<Position, Position> way : cp)
				{
					List<Pawn> tmp = this.getNewCopie(p, way.GetRight()); //add Position To Eat if Help
					if (!Rules.CheckKing.isCheckKing(tmp,  this , e))
						r.add(way); // Add if Move can Protect.
				} //add Position Obstacle if Help
			}
		}
	}
	private void getListPositionPossibleMoveKing(eColor e, List<Pair<Position, Position>> r) {
		//Position k = getKingPosition(e);
		@SuppressWarnings("serial")
		List<Pair<Integer, Integer>> allPos = new ArrayList<Pair<Integer, Integer>>() {{
			add(new Pair<Integer, Integer>(0,1));
			add(new Pair<Integer, Integer>(1,1));
			add(new Pair<Integer, Integer>(1,0));
			add(new Pair<Integer, Integer>(1,-1));
			add(new Pair<Integer, Integer>(0,-1));
			add(new Pair<Integer, Integer>(-1,-1));
			add(new Pair<Integer, Integer>(-1,0));
			add(new Pair<Integer, Integer>(-1,1));
		}};
		Position k = new Position();
		for (Pair<Integer, Integer> pos : allPos)
		{
			k.SetPosition(getKingPosition(e));
			k.column += pos.GetLeft();
			k.row += pos.GetRight();
			if (!isOutside(k) && this.getObstacleCase(k) != e) {
				List<Pawn> cp = getNewCopie(get(indexOf(getKingPosition(e))), k);
				if (!Rules.CheckKing.isCheckKing(cp, this, e))
					r.add(new Pair<Position, Position>(getKingPosition(e), new Position(k)));
			}
		}
	}
	public List<Pair<Position, Position>> getListPositionPossibleProtectKing(eColor e) {
		List<Pair<Position, Position>> ret = new ArrayList<Pair<Position, Position>>();
		getListPositionPossibleMoveKing(e, ret);
		getListPositionPossibleProtectKing(e, ret);
		return ret;
	}

	public void Promotion(Position pos, ePawns c) {
		Pawn pawn = get(indexOf(pos));
		pawn.Promotion(c);
		return ;
	}

	public void NextTurn() {
		for (Pawn p : elem)
			p.NextTurn();// implemented for enPassant Rule
	}


}	

