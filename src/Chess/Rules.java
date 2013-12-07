/**
 * 
 */
package Chess;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import main.Pair;
/**
 * @author Lumy-
 *
 */
public class Rules {

	static class MapFunctor
	{
		static private void DoEatPawn(Pawn p, Pawn eaten, BoardGame lp)
		{
			lp.remove(eaten);
			p.SetPosition(eaten.GetPosition());
		}
		
		/**
		 * Use to Recreate Dictionary of key - PtrFunc
		 * @author Lumy-
		 *
		 */
		static public abstract class Functor {
			  public abstract eMoveState CanMove(Pawn p, Position newPos, BoardGame elem);
			  public eGameState ShouldMove(Pawn p, Position newPos, BoardGame elem) {
				  List<Pawn> tmp = elem.getNewCopie(p, newPos);
				  eGameState ret = eGameState.NEXT;
				  eColor n = eColor.None;
				  if (Rules.isDraw(tmp))
					  ret = eGameState.DRAW;
				  else if ((n = Rules.isCheckKing(tmp)) != eColor.None)
					  ret = (n == eColor.Black ? eGameState.CHECK_KING_W : eGameState.CHECK_KING_W);
				  else if ((n = Rules.isCheckMat(tmp)) != eColor.None)
					  ret = (n == eColor.Black ? eGameState.CHECK_MATE_W : eGameState.CHECK_MATE_B);
				  return ret;
				  
			}
			  
			  public  void execute(Pawn p, Position newPos, BoardGame elem) {
					if (elem.getObstacleCase(newPos) == eColor.None)
						p.SetPosition(newPos);
					else
						MapFunctor.DoEatPawn(p, elem.get(elem.indexOf(newPos)), elem);
				}
			}
		/**
		 * Execute the move for a normal Pawn Class.
		 * Check if the Pion Go in the good direction
		 * Then if the Pion try to Eat, is he trying to eat in diagonal
		 * and if not Check if the new Position is in the same column
		 * @author Lumy-
		 *
		 */
		static private class DoMovePawn extends Functor {
			public eMoveState CanMove(Pawn p, Position newPos, BoardGame elem) {
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
				if ((newPos.column - 1 == p.GetPosition().column || newPos.column + 1 == p.GetPosition().column) && // Move Diagonal
				    elem.contains(newPos) && elem.getObstacleCase(newPos) != p.GetColor()){ // Contain Something and not Same Color (Enemy)
					if (newPos.diffRow(p.GetPosition()) == 1) // try to make one move
					    return eMoveState.SUCCESS;
				}
				else
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				return eMoveState.FAIL_UNAUTHORIZED;
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
				Position oldp = p.GetPosition();
				if (oldp.sameRow(newPos) || oldp.sameColumn(newPos))
					if (eColor.None == elem.getObstacleRange(p, newPos))
						if (elem.getObstacleCase(newPos) != p.GetColor())
							return eMoveState.SUCCESS;
						else
							return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				return eMoveState.FAIL_UNAUTHORIZED;
			}
		}
		/**
		 * Execute move for a King Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveKing extends Functor {
			@Override
			public eMoveState CanMove(Pawn p, Position newPos, BoardGame elem) {
				if (newPos.diffMultiple(p.GetPosition()) != 1)
					return eMoveState.FAIL_UNAUTHORIZED; // A King Can't Move More Than One
				if (elem.getObstacleCase(newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				return eMoveState.SUCCESS;
			}

		}
		/**
		 * Execute move for a Crazy Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveCrazy extends Functor {
			@Override
			public eMoveState CanMove(Pawn p, Position newPos, BoardGame elem) {
				if (newPos.diffMultiple(p.GetPosition()) == -1 || //Check if RealDiagonal
					elem.getObstacleRange(p, newPos) != eColor.None) // Check if Nothing on the way
					return eMoveState.FAIL_UNAUTHORIZED; // Can't Move there
				if (elem.getObstacleCase(newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED; // can't go on your pawn
				return eMoveState.SUCCESS;
			}
		}
		/**
		 * Execute move for a Horse Pawwn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveCavalery extends Functor {
			@Override
			public eMoveState CanMove(Pawn p, Position newPos, BoardGame elem) {
				Position oldp = p.GetPosition();
				int d1 = oldp.diffRow(newPos); //GetDiffRow
				if ((d1 == 1 && oldp.diffColumn(newPos) == 2) || //CheckPosition
					(d1 == 2 && oldp.diffColumn(newPos) == 1)) // CheckPosition
					if (elem.getObstacleCase(newPos) != p.GetColor()) // Not Occupied or Not Same Color
						return eMoveState.SUCCESS;
					else
						return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				return eMoveState.FAIL_UNAUTHORIZED;
			}
		}
		/**
		 * Execute move for a Queen Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveQueen extends Functor {
			@Override
			public eMoveState CanMove(Pawn p, Position newPos, BoardGame elem) {
				if (newPos.diffMultiple(p.GetPosition()) == -1 || // Check True Diagonal 
						elem.getObstacleRange(p, newPos) != eColor.None) // Check no Obstacle
					return eMoveState.FAIL_CHECK; // Obstacle or no True Diagonal
				if (elem.getObstacleCase(newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				return eMoveState.SUCCESS;
			}
		}
		static final public Map<ePawns, Functor> MapFunction = createMap();
		static public Map<ePawns, Functor> createMap() {
	        Map<ePawns, Functor> aMap = new Hashtable<ePawns, Functor>();
	        aMap.put(ePawns.Pawn, new DoMovePawn());
	        aMap.put(ePawns.Tower, new DoMoveTower());
	        aMap.put(ePawns.King, new DoMoveKing());
	        aMap.put(ePawns.Crazy, new DoMoveCrazy());
	        aMap.put(ePawns.Cavalery, new DoMoveCavalery());
	        aMap.put(ePawns.Queen, new DoMoveQueen());
	 	    return aMap;
	    }
	};

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
				(r2 == eGameState.CHECK_MATE_W && c == eColor.White))
			return false;
		return true;
	}

	public static eColor isCheckMat(List<Pawn> tmp) {
		// TODO Auto-generated method stub
		return eColor.None;
	}

	public static eColor isCheckKing(List<Pawn> tmp) {
		// TODO Auto-generated method stub
		return eColor.None;
	}

	public static boolean isDraw(List<Pawn> tmp) {
		// TODO Auto-generated method stub
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
	static public Pair<eMoveState, eGameState> DoMovePawns(Pawn p, Position newPos, BoardGame elem)
	{
		eMoveState r = eMoveState.FAIL_CLASS_UNKNOWN;
		eGameState r2 = eGameState.SAME;
		for (Map.Entry<ePawns, MapFunctor.Functor> entry : Rules.MapFunctor.MapFunction.entrySet()) {
		    if (p.GetClass().equals((ePawns)entry.getKey())) {
		    	if ((r = ((MapFunctor.Functor)entry.getValue()).CanMove(p, newPos, elem)) == eMoveState.SUCCESS) {
		    		r2 = ((MapFunctor.Functor)entry.getValue()).ShouldMove(p, newPos, elem);
		    		if (TestState(r2, p.GetColor())) {
		    			((MapFunctor.Functor)entry.getValue()).execute(p, newPos, elem);
		    			return new Pair<eMoveState, eGameState>(r, r2);
		    		}
		    		else
		    			r2 = eGameState.SAME;
			    }
		    }
		}
  		return new Pair<eMoveState, eGameState>(r, r2);
	}
};
