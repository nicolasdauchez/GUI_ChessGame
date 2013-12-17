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
	/**
	 * Tree is an Advanced Log tools.
	 * You can {@link Tree#goBackward(BoardGame, eColor)} in your move to try another move
	 * And all is saved. Actually you can create your own Tree of Game !
	 * 
	 * each time we will talk about a move it wil represent
	 * move: {@link Pair}<{@link Position}, {@link Position}>
	 * @author Lumy-
	 *
	 */
	private class Tree {
		/**
		 * is an Elem of the {@link Log.Tree}
		 * Only Simple Structur.
		 * @author Lumy-
		 *
		 */
		private class Elem {
			/**
			 * Represent The Tail of the {@link Log.Tree}
			 */
			public ArrayList<Elem>				elems;
			/**
			 * Use for Specific Action. Castling - Promotion
			 */
			public String						StringAction;
			/**
			 * Represent The move of the Node {@link Log.Tree.Elem} 
			 */
			public Pair<Position, Position>		shoot;
			/**
			 * index is used in intern or in export.
			 * Represent the move that is did in the Node {@link Log.Tree.Elem}
			 * if -1 that mean no move is made. (One End of {@link Log.Tree})
			 */
			public int 							index;
			/**
			 * is different from null if the move make a Eat pieces
			 */
			public ePawns						eaten;
			/**
			 * Store the current {@link eGameState}. in the Current move.
			 */
			public eGameState					eState;
			/**
			 * Private: Represent the Mother. the head. the begining;
			 */
			private Elem						mother;
			/**
			 * Default Constructor. Call for normal Move
			 * @param mother
			 * @param state
			 */
			public Elem(Elem mother, eGameState state) {
				this(mother, state, null, null);
			}
			/**
			 * Constructor Call For Move with Eat
			 * @param mother
			 * @param state
			 * @param played
			 * @param eatn
			 */
			public Elem(Elem mother, eGameState state, Pair<Position, Position> played, ePawns eatn) {
				this(mother, state, played, null, eatn);
			}
			/**
			 * Constructor Call For Special Move eatn or eatn
			 * @param mom
			 * @param state
			 * @param played
			 * @param action
			 * @param eatn
			 */
			public Elem(Elem mom, eGameState state, Pair<Position, Position> played, String action, ePawns eatn) {
				eState = state;
				mother = mom;
				elems = new ArrayList<Elem>();
				shoot = played;
				index = -1;
				StringAction = action;
				eaten = eatn;
			}
		}
		/**
		 * Represent The current Log.Tree.Elem.
		 * will be move like head = head.elems[index] || head = head.mother
		 * {@link Log.Tree.Elem#elems}
		 */
		private Elem head;
		/**
		 * Name Player White
		 */
		public String WhiteName;
		/**
		 * Name Player Black
		 */
		public String BlackName;
		/**
		 * Result at PGN form @see <a href="http://en.wikipedia.org/wiki/Portable_Game_Notation">PGN</a>
		 * 1-1 || 0-1 || 1/2-1/2 || "*"
		 */
		private String Result;
		/**
		 * Constructor. Create a new Head with null mother (the root) no move.(null)Result Undefined ("*") {@link #Result}
		 * 
		 */
		public Tree() {
			head = new Elem(null, eGameState.NEXT);
			Result = "*";
		}
		/**
		 * return the current move
		 * @return
		 */
		public Pair<Position, Position>	getCurrentShoot() {
			return head.shoot;
		}
		/**
		 * return the size {@link Log.Tree.Elem#elems} of {@link Log.Tree#head}
		 * @return
		 */
		public int getSizeElems() {
			return head.elems.size();
		}
		/**
		 * Add the move p and Forward the Log.
		 * @param p
		 * @param c
		 * @param e
		 */
		public void addMoveHead(Pair<Position, Position> p, eGameState c, ePawns e) {
			if (addCurrentHead(p, c, e)) // if False No Need to Move
				goForwardElem();
		}
		/**
		 * add 1 at {@link Log.Tree.Elem#index} of {@link Log.Tree#head} considerting that the default path and add p in head.elems
		 * @param p
		 * @param c
		 * @param e
		 * @return
		 */
		public boolean addCurrentHead(Pair<Position, Position> p, eGameState c, ePawns e) {
			if (null != alreadyExist(p)) {
				head = alreadyExist(p); // Already Exist So Move on It.
				return false; //Already Move
			}
			head.index += 1;
			head.elems.add(new Elem(head, c, p, e));
			return true;
		}
		/**
		 * null if don't find p in {@link Log.Tree.Elem#elems} of {@link Log.Tree#head}
		 * @param p
		 * @return
		 */
		private Elem alreadyExist(Pair<Position, Position> p) {
			for (Elem e : head.elems)
				if (e.shoot != null && e.shoot.equals(p))
					return e;
			return null;
		}
		/**
		 * null if don't find string in {@link Log.Tree.Elem#elems} of {@link Log.Tree#head}
		 * @param string
		 * @return
		 */
		private Elem alreadyExist(String string) {
			for (Elem e : head.elems)
				if (e.StringAction.equals(string))
					return e;
			return null;
		}
		/**
		 * Add +1 at Head index and add String represent {@link Rules.OptionalRules#Castling} (O-O || O-O-O)
		 * Promotion use Position
		 * @param string
		 */
		public void addString(String string, eGameState c) {
			addString(string, null, c);
		}
		/**
		 * Add +1 at Head index and add String represent {@link Rules.OptionalRules#Castling} (O-O || O-O-O) OR {@link Rules.OptionalRules#Promotion}
		 * Castling:[string="O-O[-O]"] OR Promotion:[string={@link ePawns}]
		 * goToNextHead
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
		 * Go on the current {@link Log.Tree.Elem#index} of {@link Log.Tree#head}
		 * @return
		 */
		public boolean goForwardElem() {
			return goForwardElem(head.index);
		}
		/**
		 * Go on one index beetween 0 and {@link Log.Tree.Elem.elems.size()} of {@link Log.Tree#head}
		 * @param i
		 * @return
		 */
		public boolean goForwardElem(int i) {
			if (i >= 0 && i <= head.elems.size()){
				head.index = i;
				head = head.elems.get(i);
				return true;
			}
			return false;
		}
		/**
		 * Go Backward. Need {@link BoardGame} and current {@link eColor} player cause of
		 * {@link BoardGame#UndoCastling(String, eColor)}
		 * {@link BoardGame#UndoPromotion(Position)}
		 * {@link BoardGame#undoRemove(ePawns, Position, eColor)}
		 * @param elem
		 * @param e
		 * @return
		 */
		public boolean goBackward(BoardGame elem, eColor e) {
			if (head.mother == null) // can't go backward
				return false;
			if (head.StringAction != null) { // Special Action
				if (t.head.StringAction.equals("O-O") || t.head.StringAction.equals("O-O-O")) { // Castling
					elem.UndoCastling(t.head.StringAction, e);
					head = head.mother;
					return true;
				}
				else { // Promotion: Just Undo before UndoMove
					elem.UndoPromotion(t.head.shoot.GetRight());
				}
			}
			elem.get(elem.indexOf(head.shoot.GetRight())).SetPosition(head.shoot.GetLeft()); // Switch Position
			if (head.eaten != null) { // Pawn has Eaten
				Position n = new Position(head.shoot.GetRight());
				if (elem.indexOf(elem.GetEaten(), n) == -1) { // EnPassantRules
					n.row += 1;
					if (elem.indexOf(elem.GetEaten(), n) == -1) //EnPassantRules
						n.row -= 2;
				}
				elem.undoRemove(head.eaten, n, (e == eColor.Black ? eColor.White : eColor.Black)); // UndoRemove
			}
			if (!isMouvement(head.shoot.GetLeft())) // if the Pawn has NEVER move
				elem.setInitPos(head.shoot.GetLeft()); // is can be reset to init pos
			head = head.mother;// Backward
			return true;
		}
		/**
		 * return null if there is no Move in Forward
		 * return Forward move from {@link Log.Tree#head} in {@link Log.Tree.Elem#elems}
		 * @return
		 */
		public Collection<Pair<Position, Position>> getAllForwardShoot() {
			if (head.index > -1) {
				ArrayList<Pair<Position, Position>> ret = new ArrayList<Pair<Position,Position>>();
				for (Elem e : head.elems)
					ret.add(e.shoot);
				return ret;
			}
			return null;
		}
		/**
		 * return true if the {@link Position} p match in any mother moves.
		 * That means the Pawn in p is not in his initial pos. 
		 * @param p
		 * @return
		 */
		public boolean isMouvement(Position p) {
			Elem i = (head.mother == null ? head : head.mother); // do this to don't check the last elem if it's not the last.
			while (i.mother != null) {
				if (i.shoot != null && i.shoot.GetRight().equals(p))
					return true;
				i = i.mother;
			}
			return false;
		}
		/**
		 * Use By {@link Log#Export(String)}
		 * Use external library @see <a href="http://xstream.codehaus.org/">xStream</a>
		 * @return
		 */
		public String toXml() {
            XStream xstream = new XStream();
            return xstream.toXML(this);
        }
	}
	
	private Tree		t;
	private XStream xstream;
	
	public Log(String nB, String nW) {
		xstream = new XStream();
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
