/**
 * 
 */
package Chess;
import main.Pair;

import java.util.ArrayList;
import java.util.Collection;
/**
 * @author Lumy-
 * Log Import and Export with PGN Norme
 * Also log each Mouvement. You can move in the game (backmove foremove)
 */
public class Log {
	private class Tree {
		private class Elem {
			private Elem						mother; //Backward
			public ArrayList<Elem>				elems; // Heads
			public Pair<Position, Position>		shoot;
			public int 							index;
			
			public Elem(Elem m) {
				this(m, null);
			}
			public Elem(Elem m, Pair<Position, Position> p) {
				mother = m;
				elems = new ArrayList<Elem>();
				shoot = p;
				index = -1;
			}
		}

		private Elem head;
		
		public Tree() {
			head = new Elem(null);
		}
		/**
		 * return the current Pair p
		 * @return
		 */
		public Pair<Position, Position>	getCurrentShoot() {
			return head.shoot;
		}
		/**
		 * return the size of head.Elem
		 * @return
		 */
		public int getSizeElems() {
			return head.index + 1;
		}
		/**
		 * Addthe Pair p and move on this Elem.
		 * @param p
		 */
		public void addMoveHead(Pair<Position, Position> p) {
			addCurrentHead(p);
			goForwardElem();
		}
		/**
		 * add +1 at head index and add p in head.elems
		 * @param p
		 */
		public void addCurrentHead(Pair<Position, Position> p) {
			head.index += 1;
			head.elems.add(new Elem(head, p));
		}
		/**
		 * Go on the current index
		 * @return
		 */
		public boolean goForwardElem() {
			return goForwardElem(head.index);
		}
		/**
		 * Go on one index beetween 0 and head.index
		 * @param i
		 * @return
		 */
		public boolean goForwardElem(int i) {
			if (i >= 0 && i <= head.index)
			{
				head = head.elems.get(i);
				return true;
			}
			return false;
		}
		public Pair<Position, Position> goBackward() {
			if (head.mother == null)
				return null;
			Pair<Position, Position> ret = new Pair<Position,Position>(head.shoot.GetRight(), head.shoot.GetLeft());
			head = head.mother;
			return ret;
		}
		public Collection<Pair<Position, Position>> getAllForwardShoot() {
			if (head.index > -1)
			{
				ArrayList<Pair<Position, Position>> ret = new ArrayList<Pair<Position,Position>>();
				for (Elem e : head.elems)
					ret.add(e.shoot);
				return ret;
			}
			return null;
		}
	}
	
	private Tree		t;
	public Log() {
		t = new Tree();
	//	log = new ArrayList<Pair<Position, Position>>();
	}
	
	public void newGame() {
		//log.clear();
	}

	public void Initialize()
	{		
	}
	public Pair<Position, Position> GoBackward() {
		return t.goBackward();
	}
	public Collection<Pair<Position, Position>>	getForward() {
		return t.getAllForwardShoot();	
	}
	public void add(Position p, Position newp) {
		t.addMoveHead(new Pair<Position,Position>(new Position(p), new Position(newp)));
	}

	public Boolean Export(ChessDataGame header) {
		//Export e = new Export(header, log);
		return false;
	}
	public void Import(String path) {
		Import.ImportPathName(path);
		return ;
	}
}
