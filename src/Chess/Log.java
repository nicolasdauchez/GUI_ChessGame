/**
 * 
 */
package Chess;
import main.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.thoughtworks.xstream.XStream;
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
			public String						StringAction;
			public Pair<Position, Position>		shoot;
			public int 							index;
			public ePawns						eaten;
			public eGameState					eState;

			public Elem(Elem m, eGameState stte) {
				this(m, stte, null, null);
			}
			public Elem(Elem m, eGameState stte, Pair<Position, Position> p, ePawns e) {
				this(m, stte, p, null, e);
			}
			public Elem(Elem m, eGameState stte, Pair<Position, Position> p, String f, ePawns e) {
				eState = stte;
				mother = m;
				elems = new ArrayList<Elem>();
				shoot = p;
				index = -1;
				StringAction = f;
				eaten = e;
			}
		}

		private Elem head;
		public String WhiteName;
		public String BlackName;
		private String Result;
		public Tree() {
			xstream = new XStream();
			head = new Elem(null, eGameState.NEXT);
			Result = "*";
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
		public void addMoveHead(Pair<Position, Position> p, eGameState c, ePawns e) {
			if (addCurrentHead(p, c, e)) // if False No Need to Move
				goForwardElem();
		}
		/**
		 * add +1 at head index and add p in head.elems
		 * @param p
		 */
		public boolean addCurrentHead(Pair<Position, Position> p, eGameState c, ePawns e) {
			if (null != alreadyExist(p))
				{
					head = alreadyExist(p);
					return false; //Already Move
				}
			head.index += 1;
			head.elems.add(new Elem(head, c, p, e));
			return true;
		}
		private Elem alreadyExist(Pair<Position, Position> p) {
			for (Elem e : head.elems)
				if (e.shoot != null && e.shoot.equals(p))
					return e;
			return null;
		}
		private Elem alreadyExist(String string) {
			for (Elem e : head.elems)
				if (e.StringAction.equals(string))
					return e;
			return null;
		}
		/**
		 * Add +1 at Head index and add String represent Castling (O-O || O-O-O)
		 * Promotion use Position
		 * @param string
		 */
		public void addString(String string, eGameState c) {
			addString(string, null, c);
		}
		/**
		 * Add +1 at Head index and add String represent Castling (O-O || O-O-O) OR Promotion
		 * Castling p = null OR String = pos.print() + ePawns Or null
		 * @param string
		 * @param p
		 */
		public void addString(String string, Pair<Position, Position> p, eGameState c) {
			if (null != alreadyExist(string)) {
				head = alreadyExist(string);
				return;
			}
			head.index += 1;
			head.elems.add(new Elem(head, c, p, string, null));
			goForwardElem();
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
			if (i >= 0 && i <= head.elems.size())
			{
				head.index = i;
				head = head.elems.get(i);
				return true;
			}
			return false;
		}
		public boolean goBackward(BoardGame elem, eColor e) {
			if (head.mother == null)
				return false;
			if (head.StringAction != null) {
				//Action
				if (t.head.StringAction.equals("O-O") || t.head.StringAction.equals("O-O-O")) {
					elem.UndoCastling(t.head.StringAction, e);
					head = head.mother;
					return true;
				}
				else { // Promotion
					elem.UndoPromotion(t.head.shoot.GetRight());
				}
			}
			elem.get(elem.indexOf(head.shoot.GetRight())).SetPosition(head.shoot.GetLeft());
			if (head.eaten != null) {
				Position n = new Position(head.shoot.GetRight());
				if (elem.indexOf(elem.GetEaten(), n) == -1) {
					n.row += 1;
					if (elem.indexOf(elem.GetEaten(), n) == -1)
						n.row -= 2;
				}
				elem.undoRemove(head.eaten, n, (e == eColor.Black ? eColor.White : eColor.Black));
			}
			if (!isMouvement(head.shoot.GetLeft()))
				elem.setInitPos(head.shoot.GetLeft());
			head = head.mother;
			return true;
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
		public boolean isMouvement(Position p) {
			Elem i = (head.mother == null ? head : head.mother);
			p.print();
			while (i.mother != null)
			{
				if (i.shoot != null && i.shoot.GetRight().equals(p))
					return true;
				i = i.mother;
			}
			return false;
		}
		public String toXml() {
            XStream xstream = new XStream();
            return xstream.toXML(this);
        }
	}
	
	private Tree		t;
	private XStream xstream;
	
	public Log(String nB, String nW) {
		t = new Tree();
		t.BlackName = nB;
		t.WhiteName = nW;
	}

	public void newGame(String nW, String nB) {
		t = new Tree();
		t.BlackName = nB;
		t.WhiteName = nW;
	}

	public boolean goBackward(BoardGame elem, eColor turn) {
		return t.goBackward(elem, turn);
	}

	public boolean canGoBackward() {
		return t.head.mother != null;
	}
	public boolean canGoForward() {
		return t.head.index != -1;
	}
	public boolean goForward(BoardGame elem, int index, eColor e){
		if (!t.goForwardElem(index))
			return false;
		if (t.head.StringAction == null) {
			Pair<Position, Position> p = t.getCurrentShoot();
			if (t.head.eaten != null && elem.indexOf(p.GetRight()) == -1) {
				Position tmppos = new Position(p.GetRight());
				tmppos.row += 1;
				if (elem.indexOf(tmppos) == -1 ||
					elem.get(elem.indexOf(tmppos)).GetClass() != ePawns.PAWN)
					tmppos.row -= 2;
				elem.remove(elem.get(elem.indexOf(tmppos))); // enPassant Eat/Rule
			}	
			elem.RedoMove(p);
		}
		else // action
		{
			if (t.head.StringAction.equals("O-O") || t.head.StringAction.equals("O-O-O"))
				elem.RedoCastling(t.head.StringAction, e);
			else { // Promotion
				elem.RedoPromotion(t.head.shoot.GetLeft(), t.head.StringAction);
				elem.RedoMove(t.head.shoot);
			}
		}
		return true;
	}
	public boolean goForward(BoardGame elem, eColor e){
		return goForward(elem, t.head.index, e);
	}
	public Collection<Pair<Position, Position>>	getForward() {
		return t.getAllForwardShoot();	
	}
	/**
	 * return the Size of Node.
	 * 0 mean you didn't played in this node
	 * other mean you have some played in this node.
	 * @return
	 */
	public int getSizeCurrentElem() {
		return t.getSizeElems();
	}
	public void add(Position p, Position newp, ePawns e, eGameState c) {
		t.addMoveHead(new Pair<Position,Position>(new Position(p), new Position(newp)), c, e);
	}
	public void addString(String string, Pair<Position, Position> p, eGameState c) {
		t.addString(string, p, c);
	}
	public void addString(String string, eGameState c) {
		t.addString(string, c);
	}
	public void LogPromotion(ePawns c) {
		t.head.StringAction = "" + c;
	}
	public Pair<Position, Position>	getCurrentShoot() {
		return t.getCurrentShoot();
	}
	public void addResult(String res) {
		if (t.Result.equals("*"))
		{
			t.Result = res;
			t.head.eState = (res.equals("0-1") ? eGameState.CHECK_MATE_B : (res.equals("1-0") ? eGameState.CHECK_MATE_W : eGameState.DRAW));
		}
	}
	private boolean Write(String xml, String name) {
		if (name == null)
			name = "TMPFILE";
		File file = new File(name + ".xml");
		try {
		if (!file.exists()) {
				file.createNewFile();
			}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(xml);
		bw.close();
		return true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean Export(String path) {
		return Export(path, null, null);
	}
	public boolean Export(String path, String nW, String nB) {
		if (nW == null)
			nW = t.WhiteName;
		if (nB == null)
			nB = t.BlackName;
		String xml = t.toXml();
		return Write(xml, path);
	}

	public boolean Import(String p) {
	    Path path = Paths.get(p);
	    List<String> slist;
		try {
			slist = Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return false;
		}
	    StringBuilder sb = new StringBuilder();
	    for (String s : slist)
	    	sb.append(s);
	    t = (Log.Tree)xstream.fromXML(sb.toString());
		//first = t.head;
		return true;
	}
	public eGameState GetCurrentState() {
		return t.head.eState;
	}
}
