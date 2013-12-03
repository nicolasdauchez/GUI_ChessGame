/**
 * 
 */
package Chess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Lumy-
 *
 */
public class BoardGame {
	private List<Pion>	elem;

	public BoardGame()
	{
		elem = new ArrayList<Pion>();
	}
	
	public int size() {
		return elem.size();
	}

	public int indexOf(Position s) {
		int c = 0;
		for (Pion p : elem)
		{
			if (p.equals(s))
				return c;
			c++;
		}
		return -1;
	}
	public int indexOf(Pion p) {
		return indexOf(p.GetPosition());
	}

	public Pion get(int c) {
		return elem.get(c);
	}

	public boolean contains(Position e) {
		for (Pion p : elem)
			if (p.equals(e))
				return true;
		return false;
	}

	public void add(Pion pion) {
		// TODO Auto-generated method stub
		elem.add(pion);
	}

	public void remove(Pion eaten) {
		System.out.println(elem.size());
		System.out.println(elem.remove(indexOf(eaten)));
			System.out.println(elem.size());
	}
 
	public void print() {
		int i = 0;
		for (Pion p : elem) {
			System.out.print(i+": ");
			p.print();
			i++;
		}
	}

	/*
	 * return true if the next row is Adversery Pion
	 */
	public boolean isObstacleRowFirst(Pion p) {
		int i = -1;
		Position tmp = new Position();
		tmp.SetPosition(p.GetPosition());
		tmp.row += 1;
		i = indexOf(tmp);
		if (i != -1)
			return get(i).GetColor() != p.GetColor();
		return false;
	}
	/*
	 * return true if the next column is Adversery Pion
	 */
	public boolean isObstacleColumnFirst(Pion p) {
		int i = -1;
		Position tmp = new Position();
		tmp.SetPosition(p.GetPosition());
		tmp.column += 1;
		i = indexOf(tmp);
		if (i != -1)
			return get(i).GetColor() != p.GetColor();
		return false;
	}

	/*
	 * return true if the first obstacle is Adversery Pion
	 */
	public boolean isObstacleRow(Pion p) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		t.row = 9;
		return isObstacleRowRange(p, t);
	}
	public boolean isObstacleRowRange(Pion p, Position range) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		while (t.row < range.row)
		{
			if (isObstacleRowFirst(p))
				return true;
			t.row += 1;
		}
		return false;
	}
	/*
	 * return true if the first obstacle is Adversery Pion
	 */
	public boolean isObstacleColumn(Pion p) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		t.column = 'i';
		return isObstacleColumnRange(p, t);
	}

	public boolean isObstacleColumnRange(Pion p, Position max) {
		Position t = new Position();
		t.SetPosition(p.GetPosition());
		while (t.column != max.column)
		{
			if (isObstacleColumnFirst(p))
				return true;
			t.column += 1;
		}
		return false;
	}

	public boolean isNoObstacle(Pion p, Position newPos) {
		Position oldp = p.GetPosition();
		if (oldp.sameRow(newPos))
			return !isObstacleRowRange(p, newPos);
		else if (oldp.sameColumn(newPos))
			return !isObstacleColumnRange(p, newPos);
		return false ;
	}

	public boolean isObstacle(Pion p, Position newPos) {
		Position oldp = p.GetPosition();
		if (oldp.sameRow(newPos))
			return isObstacleRowRange(p, newPos);
		else if (oldp.sameColumn(newPos))
			return isObstacleColumnRange(p, newPos);
		return false ;
	}

	
}
