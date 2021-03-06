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
 * BoardGame contains all {@link Pawn} alive and Dead.
 */
public class BoardGame {
	/**
	 * {@link List}<{@link Pawn}> Alive
	 */
	private List<Pawn>	elem;
	/**
	 * {@link List}<{@link Pawn}> Dead
	 */
	private List<Pawn>	Eaten;
	/**
	 * Just use to Log Eate.
	 */
	private boolean			EatenTurn;
	/**
	 * Default Constructor
	 */
	public BoardGame()
	{
		EatenTurn = false;
		elem = new ArrayList<Pawn>();
		Eaten = new ArrayList<Pawn>();
	}
	/**
	 * Return a const Collection with all Pawn Dead.
	 * @return
	 */
	public Collection<Pawn>	GetEaten() {
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
	 * Elem Alive Size
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
		return indexOf(p, s, null);
	}
	/**
	 * Index Of On a Collection with Color to be shure
	 * @param p
	 * @param s
	 * @param c
	 * @return
	 */
	public int indexOf(Collection<Pawn> p, Position s, eColor c) {
		int i = 0;
		for (Pawn e : p) {
			if (e.equals(s)) {
				if (c != null && e.GetColor() == c)
					return i;
				else if (c == null)
					return i;
			}
			i++;
		}
		return -1;
	}
	/**
	 * {@link #indexOf(Collection, Position)}
	 * @param s
	 * @return
	 */
	public int indexOf(Position s) {
		return indexOf(elem, s);
	}
	/**
	 * {@link #indexOf(Position)}
	 * @param p
	 * @return
	 */
	public int indexOf(Pawn p) {
		return indexOf(p.GetPosition());
	}
	/**
	 * return {@link Pawn} at index c int l
	 * @param l
	 * @param c
	 * @return
	 */
	public Pawn get(Collection<Pawn> l, int c) {
		Pawn ret = CollectionUtils.get(l, c);
		return ret;
	}
	/**
	 * {@link #get(Collection, int)}
	 * @param c
	 * @return
	 */
	public Pawn get(int c) {
		return get(elem, c);
	}
	/**
	 * Return tru if l Contain a {@link Pawn} with {@link Position} e
	 * @param l
	 * @param e
	 * @return
	 */
	public boolean contains(Collection<Pawn> l, Position e) {
		for (Pawn p : l)
			if (p.equals(e))
				return true;
		return false;
	
	}
	/**
	 * {@link #contains(Collection this.elem, Position)}
	 * @param e
	 * @return
	 */
	public boolean contains(Position e) {
		return contains(elem, e);
	}
	/**
	 * add {@link Pawn} to Alive List {@link #elem}
	 * @param pion
	 */
	public void add(Pawn pion) {
		elem.add(pion);
	}
	/**
	 * Remove a {@link Pawn} from {@link #elem} and set Eaten Turn to True
	 * @param eaten
	 */
	public void remove(Pawn eaten) {
		Eaten.add(elem.remove(indexOf(eaten)));
		EatenTurn = true;
	}
	/**
	 * Undo a Remove
	 * @param e
	 * @param p
	 * @param c
	 */
	public void undoRemove(ePawns e, Position p, eColor c) {
		if (contains(Eaten, p)) {
			elem.add(Eaten.remove(indexOf(Eaten, p, c)));
		}
	}
	/**
	 * Redo a Move
	 * @param p
	 */
	public void RedoMove(Pair<Position, Position> p) {
		if (indexOf(p.GetRight()) != -1)
			remove(get(indexOf(p.GetRight())));
		if (indexOf(p.GetLeft()) != -1)
			get(indexOf(p.GetLeft())).SetPosition(p.GetRight());
	}
	/**
	 * return {@link eColor#None} if no Pawn on the current Case in l
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
	 * return {@link eColor#None} if no Pawn on the current Case.
	 * @param p
	 * @return
	 */
	public eColor getObstacleCase(Position p) {
		return getObstacleCase(elem, p);
	}
	/**
	 * return {@link eColor#None} if Nothing is find on the way Row in l.
	 * Don't Check the Last Case Neither the First (always True position First Pawn)
	 * @param l
	 * @param p
	 * @param range
	 * @return
	 */
	public eColor getObstacleRowRange(Collection<Pawn> l, Pawn p, Position range) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		t.row += (t.row < range.row ? 1 : - 1);
		while (t.row != range.row) {
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
		while (t.column != max.column) {
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
		else if (oldp.DiagonalMove(newPos))
			return getObstacleRangeDiagonal(l, p, newPos);
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
		while (t.row != range.row) {
			if (eColor.None != getObstacleCase(l, t))
				return get(l, indexOf(l, t)).GetPosition();
			t.row += (t.row < range.row ? 1 : - 1);
		}
		return null;
	}
	/**
	 * Return null if Nothing find on the Wat Diagonal
	 * @param l
	 * @param p
	 * @param newPos
	 * @return
	 */
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
	/**
	 * return eColor of the First Obstacle
	 * @param l
	 * @param p
	 * @param newPos
	 * @return
	 */
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
		List<Pawn> ret= new ArrayList<Pawn>(tmp.size());
		for(Pawn item: tmp) ret.add(item.clone());
		if (-1 != indexOf(ret, newPos))
			ret.remove(indexOf(ret, newPos));
		ret.get(indexOf(ret, p.GetPosition())).SetPosition(newPos);
		return ret;
	}
	/**
	 * return the first position of c and e in l
	 * @param e
	 * @param c
	 * @param l
	 * @return
	 */
	public Position getPawnsBoardPosition(eColor e, ePawns c, Collection<Pawn> l) {
		for (Pawn p : l)
    		if (p.GetClass() == c && p.GetColor() == e)
				return p.GetPosition();
		return null;
	}
	/**
	 * {@link #getPawnsBoardPosition(eColor, ePawns, Collection)}
	 * @param e
	 * @param c
	 * @return
	 */
	private Position getPawnsPosition(eColor e, ePawns c) {
		return getPawnsBoardPosition(e, c, elem);
	}
	/**
	 * Return The Position of The King e
	 * @param e
	 * @return
	 */
	public Position getKingPosition(eColor e) {
		return getPawnsPosition(e, ePawns.KING);
	}
	/**
	 * Return all Elem of one Color e
	 * @param e
	 * @return
	 */
	public List<Pawn> getAllColor(eColor e) {
		return getAllColor(elem, e);
	}
	/**
	 * Return all Pawn of one Color e in Collection tmp
	 * @param tmp
	 * @param e
	 * @return
	 */
	public List<Pawn> getAllColor(Collection<Pawn> tmp, eColor e) {
		List<Pawn> ret = new ArrayList<Pawn>();
	    for(Pawn item: tmp)
	    	if (item.GetColor() == e)
	    		ret.add(item.clone());
		return ret;
	}
	/**
	 * New Game Reset All.
	 */
	public void newGame() {
		elem.clear();
		Eaten.clear();
		EatenTurn = false;
		addLinePawn(eColor.Black);
		addLinePawn(eColor.White);
		addHeadLine(eColor.Black);
		addHeadLine(eColor.White);
	
	}
	/**
	 * Create a Pawn in Elem
	 * @param c
	 * @param p
	 * @param t
	 */
	private void add(eColor c, Position p, ePawns t) {
			elem.add(new Pawn(c, p, t));
	}
	/**
	 * Add Pawn {@link ePawns#PAWN} Line for color C.
	 * @param c
	 */
	private void addLinePawn(eColor c) {
		Position p = new Position();
		p.row = 7;
		if (c == eColor.White)
			p.row = 2;
		p.column = 'a';
		while (p.column != 'i') {
			add(c, new Position(p), ePawns.PAWN);
			p.column += 1;
		}
	}
	/**
	 * Add the head lne for the Color c
	 * @param c
	 */
	private void addHeadLine(eColor c) {
		Position p = new Position();
		p.row = 8;
		if (c == eColor.White)
			p.row = 1;
		p.column = 'a';
		ePawns e[] = { ePawns.ROOK, ePawns.KNIGHT, ePawns.BISHOP, ePawns.QUEEN, ePawns.KING, ePawns.BISHOP, ePawns.KNIGHT, ePawns.ROOK };
		for (int i = 0; i < e.length; i++) {
			add(c, new Position(p), e[i]);
			p.column += 1;
		}
	}
	/**
	 * {@link Rules.CheckKing#AllCheckKing(Collection, BoardGame, eColor)}
	 * @param e
	 * @return
	 */
	public List<Position> AllCheckKing(eColor e) {
		return Rules.CheckKing.AllCheckKing(elem,  this, e);
	}
	/**
	 * Add to r Possible Move to Protect King e
	 * @param e
	 * @param r
	 */
	private void getListPositionPossibleProtectKing(eColor e, List<Pair<Position, Position>> r) {
		for (Pawn p : elem) {
			if (p.GetClass() != ePawns.KING && p.GetColor() == e)
			{
				List<Pair<Position, Position>> cp = Rules.MapFunctor.GetPossibleMoveProtect(p, this);
				for (Pair<Position, Position> way : cp)
				{
					List<Pawn> tmp = this.getNewCopie(p, way.GetRight());
					if (!Rules.CheckKing.isCheckKing(tmp,  this , e))
						r.add(way); // Add if Move can Protect.
				} 
			}
		}
	}
	/**
	 * add to r Possible Move King e
	 * @param e
	 * @param r
	 */
	private void getListPositionPossibleMoveKing(eColor e, List<Pair<Position, Position>> r) {
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
	/**
	 * {@link #getListPositionPossibleProtectKing(eColor, List)} and {@link #getListPositionPossibleMoveKing(eColor, List)}
	 * @param e
	 * @return
	 */
	public List<Pair<Position, Position>> getListPositionPossibleProtectKing(eColor e) {
		List<Pair<Position, Position>> ret = new ArrayList<Pair<Position, Position>>();
		getListPositionPossibleMoveKing(e, ret);
		getListPositionPossibleProtectKing(e, ret);
		return ret;
	}
	/**
	 * Do A {@link Rules.OptionalRules#Promotion}
	 * Get The Pawn and set The New {@link ePawns}
	 * @param pos
	 * @param c
	 */
	public void Promotion(Position pos, ePawns c) {
		Pawn pawn = get(indexOf(pos));
		pawn.Promotion(c);
		return ;
	}
	/**
	 * Call each {@link Pawn#NextTurn()}
	 * then set EatenTurn to False (end of the turn...)
	 */
	public void NextTurn() {
		for (Pawn p : elem)
			p.NextTurn();// implemented for enPassant Rule
		EatenTurn = false;
	}
	/**
	 * return {@link #EatenTurn}
	 * @return
	 */
	public boolean isEatThing() {
		return EatenTurn;
	}
	/**
	 * return null if nothing had been Eaten
	 * @return
	 */
	public Pawn getLastEatThing() {
		if (Eaten.size() != 0)
			return Eaten.get(Eaten.size() - 1);
		return null;
	}
	/**
	 * Redo The Castling do by e
	 * @param stringAction
	 * @param e
	 */
	public void RedoCastling(String stringAction, eColor e) {
		int row = (e == eColor.Black ? 8 : 1);
		Position pK = new Position('e', row);
		Position pT = null;
		Position nK = null;
		Position nT = null;
		if (stringAction.equals("O-O")) {
			pT = new Position('h', row);
			nK = new Position('g', row);
			nT = new Position('f', row);
		}
		else {
			nK = new Position('c', row);
			nT = new Position('d', row);
			pT = new Position('a', row);
		}
		if (indexOf(pK) == -1 || indexOf(pT) == -1)
			return;
		get(indexOf(pK)).SetPosition(nK);
		get(indexOf(pT)).SetPosition(nT);
	}
	/**
	 * Undo the Castling do by Color e
	 * @param stringAction
	 * @param e
	 */
	public void UndoCastling(String stringAction, eColor e) {
		int row = (e == eColor.Black ? 8 : 1);
		Position pK = null;
		Position pT = null;
		Position nK = new Position('e', row);
		Position nT = null;
		if (stringAction.equals("O-O")) {
			nT = new Position('h', row);
			pK = new Position('g', row);
			pT = new Position('f', row);
		}
		else {
			pK = new Position('c', row);
			pT = new Position('d', row);
			nT = new Position('a', row);
		}
		if (indexOf(pK) == -1 || indexOf(pT) == -1)
			return;
		get(indexOf(pK)).SetPosition(nK);
		get(indexOf(pT)).SetPosition(nT);
		setInitPos(nK);
		setInitPos(nT);
	}
	/**
	 * Redo Promotion at p
	 * @param p
	 * @param stringAction
	 */
	public void RedoPromotion(Position p, String stringAction) {
		ePawns e = ePawns.PAWN;
		System.out.println(stringAction);
		switch (stringAction) {
		case "BISHOP":
			e = ePawns.BISHOP;
			break;
		case "KNIGHT":
			e = ePawns.KNIGHT;
			break;
		case "QUEEN":
			e = ePawns.QUEEN;
			break;
		case "ROOK":
			e = ePawns.ROOK;
			break;
		}
		get(indexOf(p)).Promotion(e);
	}
	/**
	 * Undo Promotion at P (unpromoted)
	 * @param p
	 */
	public void UndoPromotion(Position p) {
		get(indexOf(p)).Promotion(ePawns.PAWN);
	}
	/**
	 * Reset Pawn at p
	 * @param p
	 */
	public void setInitPos(Position p) {
		if (contains(p))
			get(indexOf(p)).reset();
	}
}	

