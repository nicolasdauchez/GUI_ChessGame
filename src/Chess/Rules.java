/**
 * 
 */
package Chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import main.Pair;
/**
 * @author Lumy-
 * Rules execute Move
 *  or {@link Rules.Castling} Function tools
 *  or {@link Rules.CheckKing} Function tools
 */
public class Rules {
	/**
	 * Permit to Set OptionalRules
	 * {@link #enPassant}
	 * {@link #Castling}
	 * {@link #Promotion}
	 * @author Lumy-
	 */
	public static class OptionalRules {
		/**
		 * @see <a href="http://en.wikipedia.org/wiki/En_passant">En_passant</a>
		 */
		static boolean enPassant = true;
		/**
		 * @see <a href="http://en.wikipedia.org/wiki/Castling">Castling</a>
		 */
		static boolean Castling = true;
		/**
		 * @see <a href="http://en.wikipedia.org/wiki/Promotion_(chess)">Promotion</a>
		 */
		static boolean Promotion = true;
		/**
		 * Setter {@link #EnPassant}
		 * @param e
		 */
		public static void setEnPassant(boolean e) {
			enPassant = e;
		}
		/**
		 * Setter {@link #Castling}
		 * @param e
		 */
		public static void setCastling(boolean e) {
			Castling = e;
		}
		/**
		 * Setter {@link #Promotion}
		 * @param e
		 */
		public static void setPromotion(boolean e) {
			Promotion = e;
		}
	}
	/**
	 * Castling Rules {@value Rules.OptionalRules#Castling}
	 * Check if you can Castling. Doesn't do it. {@link ChessGame#DoCastling(Position, Position)} to see the movement
	 * @author Lumy-
	 */
	static class Castling {
		/**
		 * Check if the Last Case Create a invalide Move
		 * @param tmp
		 * @param pawn
		 * @param enp
		 * @param elem
		 * @return
		 */
		private static boolean canMoveTheir_Castling(Collection<Pawn> tmp, Pawn pawn, Position enp, BoardGame elem) {
			Collection<Pawn> t = elem.getNewCopie(tmp, elem.get(tmp, elem.indexOf(tmp, pawn.GetPosition())), enp);
			if (Rules.CheckKing.isCheckKing(t, elem, pawn.GetColor()))
					return false;
			return true;
		}
		 /**
		  * Check if the King doesn't put under attack on the way
		  * @param tmp
		  * @param elem
		  * @param pawn
		  * @param enp
		  * @return
		  */
		private static boolean canDoMove_Castling(Collection<Pawn> z, Pawn k, Position endp, BoardGame elem) {
			Position i = new Position(k.GetPosition());
			Position tmp = new Position(k.GetPosition());
			Collection<Pawn> t = null;
			while (tmp.diffColumn(endp) != 0) {
				i.column += (tmp.column < endp.column ? 1 : -1);
				t = elem.getNewCopie((t == null ? z : t), elem.get((t == null ? z : t), elem.indexOf((t == null ? z : t), tmp)), i);
				if (Rules.CheckKing.isCheckKing(t, elem, k.GetColor()))
					return false;
				tmp.column += (tmp.column < endp.column ? 1 : -1);
			}
			return true;
		}		
		/**
		 * Private Function call {@link #isCastling(Collection, Pawn, Position, BoardGame)} if true
		 * return p2.isStartPosition() && p1.isStartPosition();
		 * @param tmp
		 * @param p2
		 * @param p1
		 * @param elem
		 * @return
		 */
		static private boolean _canCastling(Collection<Pawn> tmp, Pawn p2, Pawn p1, BoardGame elem) {
			if (isCastling(tmp, p2, p1.GetPosition(), elem)) // Type and Color
				return p2.isStartPosition() && p1.isStartPosition(); // Start Position
			return false;
		}
		/**
		 * return True if the Player is Actually Castling (Position, Type, Initial Pos)
		 * @param tmp
		 * @param p
		 * @param newPos
		 * @param elem
		 * @return
		 */
		static public boolean isCastling(Collection<Pawn> tmp, Pawn p, Position newPos, BoardGame elem) {
			if (Rules.OptionalRules.Castling == true)
				if (elem.contains(tmp, newPos)) {
					Pawn p2 = elem.get(tmp, elem.indexOf(tmp, newPos));
					return (p2.GetColor() == p.GetColor() && ((p2.GetClass() == ePawns.KING && p.GetClass() == ePawns.ROOK) ||
						(p.GetClass() == ePawns.KING && p2.GetClass() == ePawns.ROOK))); // is King and Rook and Same Color
			}
			return false;
		}
		/**
		 * Return {@link Pair}<{@link Position} King,{@link Position} Tower>
		 * @param click1
		 * @param click2
		 * @return
		 */
		public static Pair<Position, Position>	getPositionKing(Position click1, Position click2) {
			Position k = new Position(click1.row, click2.column < click1.column ? 'c' : 'g');
			Position t = new Position(click1.row, click2.column < click1.column ? 'd' : 'f');
			return new Pair<Position, Position>(k, t);
		}
		/**
		 * return true if the Player can Castling (no Check on the way, no check a end Positon)
		 * @param tmp
		 * @param p2
		 * @param p1
		 * @param elem
		 * @return
		 */
		public static boolean CanTheyCastling(Collection<Pawn> tmp, Pawn p2, Pawn p1, BoardGame elem) { // tmp is her the current BoardGame but don't care
			if (!_canCastling(tmp, p2,p1,elem)) // Start Position and Good Pawns/Color
				return false;
			if (elem.getPositionFirstObstacleColumnRange(tmp, p2, p1.GetPosition()) != null)
				return false;
			if (Rules.CheckKing.isCheckKing(tmp, elem, p1.GetColor()))
				return false;
			Position enp = new Position(p2.GetPosition());
			enp.column = ((p1.GetClass() == ePawns.ROOK ? p1.GetPosition().column : p2.GetPosition().column) == 'a' ? 'c' : 'g');
			if (!(canDoMove_Castling(tmp, (p1.GetClass() == ePawns.KING ? p1 : p2), enp, elem)))
				return false;
			if (!(canMoveTheir_Castling(tmp, (p1.GetClass() == ePawns.KING ? p1 : p2), enp, elem)))
				return false;
			return true;
		}
	}
	/**
	 * Map Functor Contain A Dictionary {@link Rules.Functor}
	 * And some useFull Function. Do the Usual moves.
	 * @author Lumy-
	 *
	 */
	static class MapFunctor {
		/**
		 * call {@link BoardGame#remove(Pawn)} and Set the New {@link Position} of {@link Pawn}p
		 * @param p
		 * @param eaten
		 * @param lp
		 */
		static private void DoEatPawn(Pawn p, Pawn eaten, BoardGame lp) {
			lp.remove(eaten);
			p.SetPosition(eaten.GetPosition());
		}
		/**
		 * Use to Recreate Dictionary of key - PtrFunc
		 * It's an base Abstract class. Each {@link ePawns} will have a specialisation.
		 * @author Lumy-
		 *
		 */
		static public abstract class Functor {
			/**
			 * Default function call {@link #CanMove(Collection, Pawn, Position, BoardGame)} with the Collection {@link BoardGame#getElem()}
			 * @param p
			 * @param newPos
			 * @param elem
			 * @return
			 */
			public eMoveState 					CanMove(Pawn p, Position newPos, BoardGame elem) {
				return CanMove(elem.getElem(), p, newPos, elem);
			}
			/**
			 * Abstract Because each {@link ePawns} move by it's own
			 * @param tmp
			 * @param p
			 * @param newPos
			 * @param elem
			 * @return
			 */
			public abstract eMoveState 			CanMove(Collection<Pawn> tmp, Pawn p, Position newPos, BoardGame elem);
			/**
			 * Return the list of Possible Position For Pawn. Abstract Cause Each {@link ePawns} move by is own
			 * @param p
			 * @param elem
			 * @return
			 */
			public abstract List<Position>		GetListPosition(Pawn p, BoardGame elem);
			/**
			 * call {@link #ShouldMove(Collection, Pawn, Position, BoardGame)} with the Colleciton {@link BoardGame#getElem()}
			 * @param p
			 * @param newPos
			 * @param elem
			 * @return
			 */
			public eGameState 					ShouldMove(Pawn p, Position newPos, BoardGame elem) {				  
				return ShouldMove(elem.getElem(), p, newPos, elem);
			}
			/**
			 * Not Abstract because they shouldn't move if they create an Check for their Team
			 * @param l
			 * @param p
			 * @param newPos
			 * @param elem
			 * @return
			 */
			public eGameState 					ShouldMove(Collection<Pawn> l, Pawn p, Position newPos, BoardGame elem) {
				List<Pawn> tmp = elem.getNewCopie(l, p, newPos);
				eGameState ret = eGameState.NEXT;
				if (Rules.isDraw(tmp, p.GetColor(), elem)) {
					ret = eGameState.DRAW;
					return ret;
				}
				else if (Rules.CheckKing.isCheckKing(tmp, elem, p.GetColor()))
					return (eColor.Black == p.GetColor() ? eGameState.CHECK_KING_B : eGameState.CHECK_KING_W);
				else if (Rules.CheckKing.isCheckKing(tmp, elem, p.GetEnemyColor()))
					return (eColor.Black == p.GetEnemyColor() ? eGameState.CHECK_KING_B : eGameState.CHECK_KING_W);
				return ret;
			}
			/**
			 * Execute the Move
			 * @param p
			 * @param newPos
			 * @param elem
			 */
			public  void execute(Pawn p, Position newPos, BoardGame elem) {
				if (elem.getObstacleCase(newPos) == eColor.None)
					p.SetPosition(newPos);
				else
					MapFunctor.DoEatPawn(p, elem.get(elem.indexOf(newPos)), elem);
		  }
		}
		/**
		 * Execute the move for {@link ePawns#PAWN}.
		 * Check if the {@link Pawn} go in the good direction
		 * Then if the {@link Pawn} try to Eat, is he trying to eat in diagonal
		 * and if not Check if the new {@link Position} is in the same column
		 * @author Lumy-
		 *
		 */
		static private class DoMovePawn extends Functor {
			/**
			 * Private Function to Check if is trying to do {@link Rules.OptionalRules#enPassant} and if he can
			 * @param tmp
			 * @param p
			 * @param newPos
			 * @param elem
			 * @return
			 */
			private boolean isMakeEnPassant(Collection<Pawn> tmp, Pawn p, Position newPos, BoardGame elem) {
				if (!Rules.OptionalRules.enPassant)
					return false;
				Position test = new Position(p.GetPosition().row, newPos.column);
				if (elem.contains(tmp, test) && p.GetColor() != elem.getObstacleCase(tmp,  test))
					if (elem.get(tmp, elem.indexOf(tmp, test)).enPassant()) // check if you can eat like with enPassant rules
						return true;
				return false;
			}
			/**
			 * Private exec_enPassant
			 * @param p
			 * @param newPos
			 * @param elem
			 */
			private void exec_enPassant(Pawn p, Position newPos, BoardGame elem) {
				if (!Rules.OptionalRules.enPassant)
					return ;
				Position test = new Position(p.GetPosition().row, newPos.column);
				MapFunctor.DoEatPawn(p, elem.get(elem.indexOf(test)), elem);
				return ;
				
			}
			/**
			 * Override to Take care of {@link Rules.OptionalRules#enPassant} Rules
			 */
			@Override
			public  void execute(Pawn p, Position newPos, BoardGame elem) {
				if (isMakeEnPassant(elem.getElem(), p, newPos, elem))
					exec_enPassant(p, newPos, elem);
				if (elem.getObstacleCase(newPos) == eColor.None)
					p.SetPosition(newPos);
				else
					MapFunctor.DoEatPawn(p, elem.get(elem.indexOf(newPos)), elem);
			}
			/**
			 * return {@link eMoveState}.
			 */
			@Override
			public eMoveState CanMove(Collection<Pawn> tmp, Pawn p, Position newPos, BoardGame elem) {
				if (elem.getObstacleCase(tmp, newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				if (p.isNotGoodDirection(newPos))
					return eMoveState.FAIL_PAWNS_BACKWARD; // return badPosition enum
				if (newPos.column == p.GetPosition().column) // Forward
					if (elem.contains(newPos)) // enemie forward
						return eMoveState.FAIL_PAWNS_EAT_FORWARD;
					else // No enemy Forward
						{
							if ((newPos.diffRow(p.GetPosition()) == 2 && p.isStartPosition()) ||// try to make 2 move
								 newPos.diffRow(p.GetPosition()) == 1) // try to make one move
									return eMoveState.SUCCESS;
							return eMoveState.FAIL_UNAUTHORIZED; //To much Move
						}
				else if ((newPos.column - 1 == p.GetPosition().column || newPos.column + 1 == p.GetPosition().column) && newPos.diffRow(p.GetPosition()) == 1  && // Move  1 - Diagonal
						((elem.contains(newPos)  && elem.getObstacleCase(newPos) != p.GetColor()) || // Contain Something and not Same Color (Enemy) or
						isMakeEnPassant(tmp, p, newPos, elem))) // make enPassant Rule
							return eMoveState.SUCCESS;
				return eMoveState.FAIL_UNAUTHORIZED;
			}
			@Override
			public List<Position> GetListPosition(final Pawn p, BoardGame elem) {
				@SuppressWarnings("serial")
				List<Position> ret = new ArrayList<Position>() {{
					Position tmp = new Position();
					tmp.SetPosition(p.GetPosition());
					tmp.row += (p.GetColor() == eColor.White ? 1 : -1);
					add(tmp);
					tmp = new Position();
					tmp.SetPosition(p.GetPosition());
					tmp.row += (p.GetColor() == eColor.White ? 1 : -1);
					tmp.column += 1;
					add(tmp);
					tmp = new Position();
					tmp.SetPosition(p.GetPosition());
					tmp.row += (p.GetColor() == eColor.White ? 1 : -1);
					tmp.column -= 1;
					add(tmp);
				
				}};
				List<Position> tmp = new ArrayList<Position>();
				for (Position pos : ret)
					if (elem.isOutside(pos))
						tmp.add(pos);
				for (Position pos : tmp)
					ret.remove(pos);
				return ret;
			}
			
		}
		/**
		 * Execute Move For A Tower Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveTower extends Functor {
			@Override
			public eMoveState CanMove(Pawn p, Position newPos, BoardGame elem) {
				return CanMove(elem.getElem(), p, newPos, elem);
			}
			@Override
			public eMoveState CanMove(Collection<Pawn> tmp, Pawn p, Position newPos, BoardGame elem) {
				Position oldp = p.GetPosition();
				if (Rules.Castling.isCastling(tmp, p, newPos, elem))
					return (Rules.Castling.CanTheyCastling(tmp, p, elem.get(tmp, elem.indexOf(tmp, newPos)), elem) ? eMoveState.CASTLING : eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED);
				else if (elem.getObstacleCase(tmp, newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				else if (oldp.sameRow(newPos) || oldp.sameColumn(newPos))
					if (eColor.None == elem.getObstacleRange(tmp, p, newPos))
						if (elem.getObstacleCase(tmp, newPos) != p.GetColor())
							return eMoveState.SUCCESS;
						else
							return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				return eMoveState.FAIL_UNAUTHORIZED;
			}
			@Override
			public List<Position> GetListPosition(final Pawn p, BoardGame elem) {
				@SuppressWarnings("serial")
				List<Position> ret = new ArrayList<Position>() {{
					Position tmp = new Position();
					tmp.SetPosition(p.GetPosition());
					tmp.row = 1;
					while (tmp.row < p.GetPosition().row)
					{
						Position t = new Position(tmp);
						add(t);	
						tmp.row += 1;
					}
					tmp.SetPosition(p.GetPosition());
					tmp.row = 8;
					while (tmp.row > p.GetPosition().row)
					{
						Position t = new Position(tmp);
						add(t);	
						tmp.row -= 1;
					}
					tmp.SetPosition(p.GetPosition());
					tmp.column = 'a';
					while (tmp.column < p.GetPosition().column)
					{
						Position t = new Position(tmp);
						add(t);	
						tmp.column -= 1;
					}
					tmp.SetPosition(p.GetPosition());
					tmp.column = 'h';
					while (tmp.column > p.GetPosition().column)
					{
						Position t = new Position(tmp);
						add(t);	
						tmp.column += 1;
					}
				}};
				return ret;
			}
		}
		/**
		 * Execute move for a King Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveKing extends Functor {
			@Override
			public eMoveState CanMove(Collection<Pawn> tmp, Pawn p, Position newPos, BoardGame elem) {
				if (Rules.Castling.isCastling(tmp, p, newPos, elem))
					return (Rules.Castling.CanTheyCastling(tmp, p, elem.get(tmp, elem.indexOf(tmp, newPos)), elem) ? eMoveState.CASTLING : eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED);
				else if (elem.getObstacleCase(tmp, newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				else if (newPos.diffMultiple(p.GetPosition()) != 1)
					return eMoveState.FAIL_UNAUTHORIZED; // A King Can't Move More Than One
				return eMoveState.SUCCESS;
			}
			@Override
			public List<Position> GetListPosition(final Pawn p, BoardGame elem) {
				@SuppressWarnings("serial")
				List<Pair<Integer, Integer>> ret = new ArrayList<Pair<Integer, Integer>>() {{
					add(new Pair<Integer, Integer>(0,1));
					add(new Pair<Integer, Integer>(1,1));
					add(new Pair<Integer, Integer>(1,0));
					add(new Pair<Integer, Integer>(1,-1));
					add(new Pair<Integer, Integer>(0,-1));
					add(new Pair<Integer, Integer>(-1,-1));
					add(new Pair<Integer, Integer>(-1,0));
					add(new Pair<Integer, Integer>(-1,1));
				}};
				List<Position> tmp = new ArrayList<Position>();
				for (Pair<Integer, Integer> pos : ret) {
					Position bool = new Position(p.GetPosition());
					bool.row += pos.GetLeft();
					bool.column += pos.GetRight();
					if (!elem.isOutside(bool))
						tmp.add(bool);
				}
				return tmp;
			}
		}
		/**
		 * Execute move for a Crazy Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveCrazy extends Functor {
			@Override
			public eMoveState CanMove(Collection<Pawn> l, Pawn p, Position newPos, BoardGame elem) {
				if (elem.getObstacleCase(l, newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				if (!newPos.DiagonalMove(p.GetPosition()) || //Check if RealDiagonal
					elem.getObstacleRange(l, p, newPos) != eColor.None) // Check if Nothing on the way
					return eMoveState.FAIL_UNAUTHORIZED; // Can't Move there
				return eMoveState.SUCCESS;
			}
			@Override
			public List<Position> GetListPosition(final Pawn p, BoardGame elem) {
				@SuppressWarnings("serial")
				List<Position> ret = new ArrayList<Position>() {{
					Position tmp = new Position();
					tmp.SetPosition(p.GetPosition());
					while (tmp.row < 8 && tmp.column < 'h')
					{
						tmp.column += 1;
						tmp.row += 1; // did because don't want original pos and don't want add more line
						add(new Position(tmp));
					}
					tmp.SetPosition(p.GetPosition());
					while (tmp.row < 8 && tmp.column > 'a')
					{
						tmp.column -= 1;
						tmp.row += 1;
						add(new Position(tmp));
					}
					tmp.SetPosition(p.GetPosition());
					while (tmp.row > 1 && tmp.column > 'a')
					{
						tmp.column -= 1;
						tmp.row -= 1;
						add(new Position(tmp));
					}
					tmp.SetPosition(p.GetPosition());
					while (tmp.row > 1 && tmp.column < 'h')
					{
						tmp.column += 1;
						tmp.row -= 1;
						add(new Position(tmp));
					}
				}};
				return ret;
				}

		}
		/**
		 * Execute move for a Horse Pawwn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveCavalery extends Functor {
			@Override
			public eMoveState CanMove(Collection <Pawn> l, Pawn p, Position newPos, BoardGame elem) {
				if (elem.getObstacleCase(l, newPos) == p.GetColor()) // Occupied Same Color
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				Position oldp = p.GetPosition();
				int d1 = oldp.diffRow(newPos); //GetDiffRow
				if ((d1 == 1 && oldp.diffColumn(newPos) == 2) || //CheckPosition
					(d1 == 2 && oldp.diffColumn(newPos) == 1)) // CheckPosition
					return eMoveState.SUCCESS;
				return eMoveState.FAIL_UNAUTHORIZED;
			}
			@Override
			public List<Position> GetListPosition(Pawn p, BoardGame elem) {			
				@SuppressWarnings("serial")
				List<Pair<Integer, Integer>> l = new ArrayList<Pair<Integer, Integer>>() {{
					add(new Pair<Integer, Integer>(1,2));
					add(new Pair<Integer, Integer>(2,1));
					add(new Pair<Integer, Integer>(-1,2));
					add(new Pair<Integer, Integer>(-2,1));
					add(new Pair<Integer, Integer>(1,-2));
					add(new Pair<Integer, Integer>(2,-1));
					add(new Pair<Integer, Integer>(-1,-2));
					add(new Pair<Integer, Integer>(-2,-1));
				}};
				List<Position> ret = new ArrayList<Position>();
				Position pos = new Position();
				for (Pair<Integer, Integer> a : l) {
					pos.SetPosition(p.GetPosition());
					pos.row += a.GetLeft();
					pos.column += a.GetRight();
					if (!elem.isOutside(pos))
						ret.add(new Position(pos));
				}
				return ret;
			}
		}
		/**
		 * Execute move for a Queen Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveQueen extends Functor {
			@Override
			public eMoveState CanMove(Collection<Pawn> l, Pawn p, Position newPos, BoardGame elem) {
				if (elem.getObstacleCase(l, newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				if (newPos.diffMultiple(p.GetPosition()) == -1 || // Check True Diagonal 
						elem.getObstacleRange(l, p, newPos) != eColor.None) // Check no Obstacle
					return eMoveState.FAIL_UNAUTHORIZED; // Obstacle or no True Diagonal
				return eMoveState.SUCCESS;
			}
			@Override
			public List<Position> GetListPosition(final Pawn p, BoardGame elem) {
				@SuppressWarnings("serial")
				List<Position> ret = new ArrayList<Position>() {{
					Position tmp = new Position();
					tmp.SetPosition(p.GetPosition());
					while (tmp.row < 8 && tmp.column < 'h')	{
						tmp.column += 1;
						tmp.row += 1; // did because don't want original pos and don't want add more line
						add(new Position(tmp));
					}
					tmp.SetPosition(p.GetPosition());
					while (tmp.row < 8 && tmp.column > 'a') {
						tmp.column -= 1;
						tmp.row += 1;
						add(new Position(tmp));
					}
					tmp.SetPosition(p.GetPosition());
					while (tmp.row > 1 && tmp.column > 'a')	{
						tmp.column -= 1;
						tmp.row -= 1;
						add(new Position(tmp));
					}
					tmp.SetPosition(p.GetPosition());
					while (tmp.row > 1 && tmp.column < 'h')	{
						tmp.column += 1;
						tmp.row -= 1;
						add(new Position(tmp));
					}
					tmp = new Position();
					tmp.SetPosition(p.GetPosition());
					tmp.row = 1;
					while (tmp.row < p.GetPosition().row) {
						Position t = new Position(tmp);
						add(t);	
						tmp.row += 1;
					}
					tmp.SetPosition(p.GetPosition());
					tmp.row = 8;
					while (tmp.row > p.GetPosition().row) {
						Position t = new Position(tmp);
						add(t);	
						tmp.row -= 1;
					}
					tmp.SetPosition(p.GetPosition());
					tmp.column = 'a';
					while (tmp.column < p.GetPosition().column) {
						Position t = new Position(tmp);
						add(t);	
						tmp.column -= 1;
					}
					tmp.SetPosition(p.GetPosition());
					tmp.column = 'h';
					while (tmp.column > p.GetPosition().column) {
						Position t = new Position(tmp);
						add(t);	
						tmp.column += 1;
					}
				}};
				return ret;
			}
		}
		static final public Map<ePawns, Functor> MapFunction = createMap();
		static public Map<ePawns, Functor> createMap() {
	        Map<ePawns, Functor> aMap = new Hashtable<ePawns, Functor>();
	        aMap.put(ePawns.PAWN, new DoMovePawn());
	        aMap.put(ePawns.ROOK, new DoMoveTower());
	        aMap.put(ePawns.KING, new DoMoveKing());
	        aMap.put(ePawns.BISHOP, new DoMoveCrazy());
	        aMap.put(ePawns.KNIGHT, new DoMoveCavalery());
	        aMap.put(ePawns.QUEEN, new DoMoveQueen());
	 	    return aMap;
	    }
		/**
		 * Return the Authorized Move to save Your {@link ePawns#KING} !
		 * @param p
		 * @param elem
		 * @return
		 */
		static public List<Pair<Position, Position>> GetPossibleMoveProtect(Pawn p, BoardGame elem) {
			List<Pair<Position, Position>> ret = new ArrayList<Pair<Position, Position>>();
			Position k = elem.getKingPosition(p.GetColor());
			List<Position> a = elem.AllCheckKing(p.GetColor());
			for (Position pos : a) { // Represent All Position With Enemy That Can Eat King in k
				Position postmp = new Position();
				postmp.SetPosition(pos);
					while (!postmp.equals(k)) { // Check All Position Beetween k and pos
						if (Rules.canMove(p, postmp, elem)) {
							List<Pawn> tmp = elem.getNewCopie(p, postmp); // Get a Copy of Board With Move pawn->postmp
							if (!Rules.CheckKing.isCheckKing(tmp, elem, p.GetColor())) // No More CheckKing with New Position
								ret.add(new Pair<Position, Position>(p.GetPosition(), new Position(postmp)));
						}
						MovePosTmp(postmp, k); // Go to K Position
					}
			}
			return ret;
		}
		/**
		 * Private function. make posttmp close k
		 * @param postmp
		 * @param k
		 */
		private static void MovePosTmp(Position postmp, Position k) {
			if (postmp.row != k.row)
				postmp.row += (postmp.row < k.row ? +1 : -1);
			if (postmp.column != k.column)
				postmp.column += (postmp.column < k.column ? +1 : -1);
		}
		/**
		 * Return all Position for Pawn that are inside BoardGame.
		 * And return True a CanMoveHere and [Next || check*_EnemyColor] at ShouldMove
		 * So return all valid Shoot for a Pawn
		 * @param tmp
		 * @param pawn
		 * @param elem
		 * @return
		 */
		public static boolean isPossibleMove(Collection<Pawn> tmp, Pawn pawn, BoardGame elem) {
			List<Position> ret = null;
			for (Map.Entry<ePawns, MapFunctor.Functor> entry : Rules.MapFunctor.MapFunction.entrySet()) {
			    if (pawn.GetClass().equals((ePawns)entry.getKey()))
			    	ret = ((MapFunctor.Functor)entry.getValue()).GetListPosition(pawn, elem);
			    	if (ret != null) 
			    		for (Position pos : ret)
			    			if (((MapFunctor.Functor)entry.getValue()).CanMove(tmp, pawn, pos, elem) == eMoveState.SUCCESS) {
			    				Collection<Pawn> n = elem.getNewCopie(tmp, pawn, pos);
			    				if (!Rules.CheckKing.isCheckKing(n, elem, pawn.GetColor()))
			    					return true;
			    			}
			}
			return false;
		}
	}
	/**
	 * Just test the eGameState with the color of currentPlayer
	 * if Rules.MapFunctor.MapFunction[key].ShouldMove return a CheckMate for current player
	 * or if error pass and if eGameState == SAME (should not arrive there)
	 * @param r2
	 * @param c
	 * @return
	 */
	private static boolean TestState(eGameState r2, eColor c) {
		if (r2 == eGameState.SAME || (r2 == eGameState.CHECK_MATE_B && c == eColor.Black) ||
				(r2 == eGameState.CHECK_MATE_W && c == eColor.White) || (r2 == eGameState.CHECK_KING_B && c == eColor.Black) ||
				(r2 == eGameState.CHECK_KING_W && c == eColor.White))
			return false;
		return true;
	}
	/**
	 * Class To Look if a BoardGame is CheckKing;
	 * @author Lumy-
	 *
	 */
	static class CheckKing
	{
		/**
		 * Launch a getObstacleRange on tmp, store the result in r then return true if something is found (enemy)
		 * else return false (no enemy or a friend);
		 * @param tmp
		 * @param elem
		 * @param pos
		 * @param nps
		 * @param e
		 * @param r
		 * @return
		 */
		private static boolean isCheckKing_LaunchRange(Collection<Pawn> tmp, BoardGame elem, Position pos, Position nps, eColor e, List<Position> r) {
			eColor ret = elem.getObstacleRange(tmp, elem.get(tmp, elem.indexOf(tmp, pos)), nps);
			if (ret != eColor.None && e != ret) // Something not Same Color
			{
				Position a = elem.getPositionFirstObstacleRange(tmp, elem.get(tmp, elem.indexOf(tmp, pos)), nps);
				ePawns p = elem.get(tmp, elem.indexOf(tmp, a)).GetClass();
				switch (p)
				{
				case QUEEN:
					r.add(a);
					return true;
				case ROOK:
					r.add(a);
					return true;
				case KING:
					if (a.diffMultiple(pos) == 1) {
						r.add(a);
						return true;
					}
				default:
					return false;
				}
			}
			return false;
		}
		/**
		 * Launch a getObstacleRange on tmp, but check for diagonal move (so return true only for Queen, crazy and king
		 * Same than isCheckKing_LaunchRange
		 * @param tmp
		 * @param elem
		 * @param pos
		 * @param nps
		 * @param e
		 * @param r
		 * @return
		 */
		private static boolean isCheckKing_LaunchRangeDiagonal(Collection<Pawn> tmp, BoardGame elem, Position pos, Position nps, eColor e, List<Position> r) {
			eColor ret = elem.getObstacleRange(tmp, elem.get(tmp, elem.indexOf(tmp, pos)), nps);
			if (ret != eColor.None && e != ret) // Something not Same Color
			{
				Position a = elem.getPositionFirstObstacleRange(tmp, elem.get(tmp, elem.indexOf(tmp, pos)), nps);
				Pawn p = elem.get(tmp, elem.indexOf(tmp, a));
				switch (p.GetClass())
				{
				case BISHOP:
					r.add(new Position(a));
					return true;
				case QUEEN:
					r.add(new Position(a));
					return true;
				case KING:
					if (a.diffMultiple(pos) == 1)
					{
						r.add(new Position(a));
						return true;
					}
				case PAWN:
					if (a.diffMultiple(pos) == 1 && (p.GetColor() == eColor.White ? a.row < pos.row : a.row > pos.row))
					{
						r.add(new Position(a));
						return true;
					}
				default:
					return false;
				}
			}
			return false;
		}
		/**
		 * Return all Position on the Row with Enemy that can Eat King e
		 * @param tmp
		 * @param elem
		 * @param pos
		 * @param e
		 * @return
		 */
		private static List<Position> isCheckKing_LaunchRow(Collection<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
			List<Position> r = new ArrayList<Position>();
			Position nps = new Position();
			nps.row = pos.row;
			nps.column = 'a' - 1;// Set to 'a' -1 to Check Position 'a'
			isCheckKing_LaunchRange(tmp, elem, pos, nps, e, r);
			nps.row = pos.row; 
			nps.column = 'i';// Set to 'i' to check Position 'h'
			isCheckKing_LaunchRange(tmp, elem, pos, nps, e, r);
			return r;
		}
		/**
		 * Return All Position on the Column with Enemy that can Eat King e
		 * @param tmp
		 * @param elem
		 * @param pos
		 * @param e
		 * @return
		 */
		private static List<Position> isCheckKing_LaunchColumn(Collection<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
			Position nps = new Position();
			nps.row = 0; // Set to 0 to Check Position 1
			List<Position> ret = new ArrayList<Position>();
			nps.column = pos.column;
			isCheckKing_LaunchRange(tmp, elem, pos, nps, e, ret);
			nps.row = 9; // Set to 9 to check Position 8
			nps.column = pos.column;
			isCheckKing_LaunchRange(tmp, elem, pos, nps, e, ret);
			return ret;
	}
		/**
		 * Return All Position on the Diagonal with Enemy That can Eat King e
		 * @param tmp
		 * @param elem
		 * @param pos
		 * @param e
		 * @return
		 */
		private static List<Position> isCheckKing_LaunchDiagonal(Collection<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
			Position nps = new Position();
			List<Position> ret = new ArrayList<Position>();
			nps.SetPosition(pos);
			while (nps.row > 0 && nps.column > 'a' - 1) {
				nps.column -= 1;
				nps.row -= 1;
			}
			isCheckKing_LaunchRangeDiagonal(tmp, elem, pos, nps, e, ret);
			nps.SetPosition(pos);
			while (nps.row < 9 && nps.column < 'i') {
				nps.column += 1;
				nps.row += 1;
			}
			isCheckKing_LaunchRangeDiagonal(tmp, elem, pos, nps, e, ret);
			nps.SetPosition(pos);
			while (nps.row < 9 && nps.column > 'a' - 1) {
				nps.column -= 1;
				nps.row += 1;
			}
			isCheckKing_LaunchRangeDiagonal(tmp, elem, pos, nps, e, ret);
			nps.SetPosition(pos);
			while (nps.row > 0 && nps.column < 'i') {
				nps.column += 1;
				nps.row -= 1;
			}
			isCheckKing_LaunchRangeDiagonal(tmp, elem, pos, nps, e, ret);
			return ret;
		}
		/**
		 * Check if There is the Cavalery on the Position oldp in tmp and if't Not Color e
		 * @param tmp
		 * @param elem
		 * @param oldp
		 * @param e
		 * @return
		 */
		private static boolean _isCheckKing_LaunchCavalery(Collection<Pawn> tmp, BoardGame elem, Position oldp, eColor e) {
			if (elem.contains(tmp, oldp)) {
				Pawn p = elem.get(tmp, elem.indexOf(tmp, oldp));
				if (p.GetColor() != e && p.GetClass() == ePawns.KNIGHT)
					return true;
			}
			return false;
		}
		/**
		 * return All Position Of Cavalery that Can eat the King of Color e in tmp
		 * @param tmp
		 * @param elem
		 * @param pos
		 * @param e
		 * @return
		 */
		private static List<Position> isCheckKing_LaunchCavalery(Collection<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
			Position oldp = new Position();
			@SuppressWarnings("serial")
			List<Pair<Integer, Integer>> l = new ArrayList<Pair<Integer, Integer>>() {{
				add(new Pair<Integer, Integer>(1,2));
				add(new Pair<Integer, Integer>(2,1));
				add(new Pair<Integer, Integer>(-1,2));
				add(new Pair<Integer, Integer>(-2,1));
				add(new Pair<Integer, Integer>(1,-2));
				add(new Pair<Integer, Integer>(2,-1));
				add(new Pair<Integer, Integer>(-1,-2));
				add(new Pair<Integer, Integer>(-2,-1));
			}};
			List<Position> ret = new ArrayList<Position>();
			for (Pair<Integer, Integer> a : l) {
				oldp.SetPosition(pos);
				oldp.row += a.GetLeft();
				oldp.column += a.GetRight();
				if (_isCheckKing_LaunchCavalery(tmp, elem, oldp, e))
					ret.add(new Position(oldp));
			}
			return ret;
		}
		/**
		 * Check if there is a CheckKing Somewhere for color e
		 * Call All the LaunchRow return all the position
		 * @param tmp
		 * @param elem
		 * @param pos
		 * @param e
		 * @return
		 */
		private static List<Position> isCheckKing(Collection<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
			List<Position> ret = new ArrayList<Position>();
			List<Position> r = null;
			r = isCheckKing_LaunchRow(tmp, elem, pos, e);
			if (r.size() != 0)
				ret.addAll(r);
			r = isCheckKing_LaunchColumn(tmp, elem, pos,e);
			if (r.size() != 0)
				ret.addAll(r);
			r = isCheckKing_LaunchDiagonal(tmp, elem, pos,e);
			if (r.size() != 0)
				ret.addAll(r);
			r = isCheckKing_LaunchCavalery(tmp, elem, pos, e);
			if (r.size() != 0)
				ret.addAll(r);
			return ret;
		}
		/**
		 * List Position of All Enemy That Defeat King e
		 * @param tmp
		 * @param elem
		 * @param e
		 * @return
		 */
		public static List<Position> AllCheckKing(Collection<Pawn> tmp, BoardGame elem, eColor e) {
				Position k = elem.getPawnsBoardPosition(e, ePawns.KING, tmp);
				return isCheckKing(tmp, elem, k, e);
		}
		/**
		 * Return True if the King e is in Check
		 * @param tmp
		 * @param elem
		 * @param e
		 * @return
		 */
		public static boolean isCheckKing(Collection<Pawn> tmp, BoardGame elem, eColor e) {
			return AllCheckKing(tmp,  elem, e).size() != 0;
		}
		/**
		 * Return the {@link eColor} player or None if no Check
		 * @param tmp
		 * @param elem
		 * @return
		 */
		public static eColor isCheckKing(Collection<Pawn> tmp, BoardGame elem) {
			if (isCheckKing(tmp, elem, eColor.White))
				return eColor.White;
			if (isCheckKing(tmp, elem, eColor.Black))
				return eColor.Black;
			return eColor.None;
		}
	}
	/**
	 * return True if a Draw occur in tmp
	 * @param tmp
	 * @param c
	 * @param elem
	 * @return
	 */
	public static boolean isDraw(Collection<Pawn> tmp, eColor c, BoardGame elem) {	
			if (ImpossibilityCheckMate(tmp, elem))
				return true;// == eColor.Black ? eColor.White : eColor.Black)
			if (Stalemate(tmp, c, elem)) // Because we Check if the NEXT player can Play
				return true;
			return false;
		}
	/**
	 * Check if tmp is in @see <a href="http://en.wikipedia.org/wiki/Stalemate">StaleMate</a>  
	 * @param tmp
	 * @param e
	 * @param elem
	 * @return
	 */
	private static boolean Stalemate(Collection<Pawn> tmp, eColor e, BoardGame elem) {
		List<Pawn> allColor = elem.getAllColor(tmp, e);
		for (Pawn pawn : allColor) {
			if (Rules.MapFunctor.isPossibleMove(tmp, pawn, elem))
				return false;
		}
		return true;
	}
	/**
	 * Return true if not enough {@link Pawn} for a team.
	 * @see <a href="http://en.wikipedia.org/wiki/Rules_of_chess#Draws">Draw</a>
	 * @param tmp
	 * @param elem
	 * @return
	 */
	private static boolean ImpossibilityCheckMate(Collection<Pawn> tmp, BoardGame elem) {
		if (tmp.size() <= 4) {
			
			if (tmp.size() == 2 && null != elem.getPawnsBoardPosition(eColor.Black, ePawns.KING, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.KING, tmp)) // king versus king Draw !
				return true;
			else if (tmp.size() == 3) {
				if (null != elem.getPawnsBoardPosition(eColor.Black, ePawns.KING, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.KING, tmp) &&
					(null != elem.getPawnsBoardPosition(eColor.Black, ePawns.BISHOP, tmp) || null != elem.getPawnsBoardPosition(eColor.White, ePawns.BISHOP, tmp))) //king and bishop versus king
					return true;
				if (null != elem.getPawnsBoardPosition(eColor.Black, ePawns.KING, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.KING, tmp) &&
				    (null != elem.getPawnsBoardPosition(eColor.Black, ePawns.KNIGHT, tmp) || null != elem.getPawnsBoardPosition(eColor.White, ePawns.KNIGHT, tmp) )) //king and knight versus king
						 	return true;
			}
			else
					if (null != elem.getPawnsBoardPosition(eColor.Black, ePawns.KING, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.KING, tmp) &&
						(null != elem.getPawnsBoardPosition(eColor.Black, ePawns.BISHOP, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.BISHOP, tmp) )) //king and bishop versus king and bishop with the bishops on the same colour.
						if (elem.getPawnsBoardPosition(eColor.Black, ePawns.BISHOP, tmp).GetColorCase() == elem.getPawnsBoardPosition(eColor.White, ePawns.BISHOP, tmp).GetColorCase())
							return true;
			}
		return false;
	}
	/**
	 * Make The move for one Pawn (whatever is the ePawns)
	 * begin by lookin for the key[ePawns] in Rules.MapFunctor.MapFunction
	 * then Call: Value[key].canMove()
	 * 			  Value[key].ShouldMove()
	 * 			  Value[Key].execute()
	 * @param p
	 * @param newPos
	 * @param elem
	 * @return  <- if move not valid eGameState=SAME
	 */
	static public Pair<eMoveState, eGameState> DoMovePawns(Pawn p, Position newPos, BoardGame elem) {
		eMoveState r = eMoveState.FAIL_CLASS_UNKNOWN;
		eGameState r2 = eGameState.SAME;
		if (Rules.isDraw(elem.getElem(), p.GetColor(), elem))
			return new Pair<eMoveState, eGameState>(eMoveState.SUCCESS, eGameState.DRAW);// That MEAN HE COULDN'T PLAY ANYMORE.
		for (Map.Entry<ePawns, MapFunctor.Functor> entry : Rules.MapFunctor.MapFunction.entrySet()) {
		    if (p.GetClass().equals((ePawns)entry.getKey())) {
		    	if ((r = ((MapFunctor.Functor)entry.getValue()).CanMove(p, newPos, elem)) == eMoveState.SUCCESS) {
		    		r2 = ((MapFunctor.Functor)entry.getValue()).ShouldMove(p, newPos, elem);
		    		if (TestState(r2, p.GetColor())) {
		    			((MapFunctor.Functor)entry.getValue()).execute(p, newPos, elem);
		    			return new Pair<eMoveState, eGameState>(r, r2);
		    		}
		    		else {
		    			if (r2 == eGameState.CHECK_KING_B || r2 == eGameState.CHECK_KING_W)
		    				r = eMoveState.FAIL_CHECK;
		    			r2 = eGameState.SAME;
		    		}
		    	}
		    }
		}
  		return new Pair<eMoveState, eGameState>(r, r2);
	}
	/**
	 * Make The move for one Pawn (whatever is the ePawns) if shoot is in l
	 * begin by lookin for the key[ePawns] in Rules.MapFunctor.MapFunction
	 * then Call: Value[key].canMove()
	 * 			  Value[key].ShouldMove()
	 * 			  Value[Key].execute()
	 * @param l
	 * @param p
	 * @param click
	 * @param elem
	 * @return
	 */
	public static Pair<eMoveState, eGameState> DoMovePawns(List<Pair<Position, Position>> l, Pawn p, Position click, BoardGame elem) {
		eMoveState r = eMoveState.FAIL_CHECK;
		eGameState r2 = eGameState.SAME;
		if (elem.getObstacleCase(click) == p.GetColor())
			return new Pair<eMoveState, eGameState>(eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED, r2);
		for (Pair<Position, Position> i : l) {
			if (i.GetLeft().equals(p.GetPosition()) && click.equals(i.GetRight()))
				for (Map.Entry<ePawns, MapFunctor.Functor> entry : Rules.MapFunctor.MapFunction.entrySet()) {
					if (p.GetClass().equals((ePawns)entry.getKey())) {
						if ((r = ((MapFunctor.Functor)entry.getValue()).CanMove(p, click, elem)) == eMoveState.SUCCESS) {
							r2 = ((MapFunctor.Functor)entry.getValue()).ShouldMove(p, click, elem);
							if (TestState(r2, p.GetColor())) {
								((MapFunctor.Functor)entry.getValue()).execute(p, click, elem);
								return new Pair<eMoveState, eGameState>(r, r2);
							}
							else
								r2 = eGameState.SAME;
						}
						return new Pair<eMoveState, eGameState>(r, r2);
					}
				}
			
		}
		return new Pair<eMoveState, eGameState>(r, r2);
	}
	/**
	 * Return True if the Pawn Can Move There
	 * @param pawn
	 * @param postmp
	 * @return
	 */
	public static boolean canMove(Pawn pawn, Position pos, BoardGame elem) {
		for (Map.Entry<ePawns, MapFunctor.Functor> entry : Rules.MapFunctor.MapFunction.entrySet())
		    if (pawn.GetClass().equals((ePawns)entry.getKey()))
		    	if (((MapFunctor.Functor)entry.getValue()).CanMove(pawn, pos, elem) == eMoveState.SUCCESS)
		    		return true;
		    	else
		    		return false;
		return false;
	}
	/**
	 * Return False if all Rules for Promotion are not there.
	 * @param pawn
	 * @return
	 */
	public static boolean isPromotion(Pawn pawn) {
		if (!Rules.OptionalRules.Promotion)
			return false;
		int row = 1;
		if (pawn.GetColor() != eColor.Black)
			row = 8;
		if (pawn.GetClass() == ePawns.PAWN && pawn.GetPosition().row == row)
			return true;
		return false;
	}
}