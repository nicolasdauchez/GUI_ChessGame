/**
 * 
 */
package Chess;

import java.util.Iterator;
import java.util.List;
import java.util.Hashtable;
import java.util.Map;
import java.util.Collections;
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
			  public abstract boolean CanMove(Pawn p, Position newPos, BoardGame elem);
			  public abstract boolean ShouldMove(Pawn p, Position newPos, BoardGame elem);
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
			public boolean CanMove(Pawn p, Position newPos, BoardGame elem) {
				if (p.isNotGoodDirection(newPos))
					return false; // return badPosition enum
				if (elem.contains(newPos)) // we can do this because we overload Equal to compare with Position
					if (newPos.column == p.GetPosition().column ||
					(newPos.column - 1 != p.GetPosition().column && newPos.column + 1 != p.GetPosition().column))
						if (elem.getObstacleCase(newPos) != p.GetColor())
							return false; // return BadEat enum
				if ( (newPos.diffRow(p.GetPosition()) == 2 &&
					p.isStartPosition()) || newPos.diffRow(p.GetPosition()) == 1)
						return true;
					return false;
			}
			@Override
			public boolean ShouldMove(Pawn p, Position newPos, BoardGame elem) {
				// TODO Auto-generated method stub
				return true;
			}
		}
		/**
		 * Execute Move For A Tower Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveTower extends Functor {
			@Override
			public boolean CanMove(Pawn p, Position newPos, BoardGame elem) {
				Position oldp = p.GetPosition();
				if (oldp.sameRow(newPos) || oldp.sameColumn(newPos))
					if (eColor.None == elem.getObstacleRange(p, newPos) &&
					elem.getObstacleCase(newPos) != p.GetColor())
						return true;
				return false;
			}
			@Override
			public boolean ShouldMove(Pawn p, Position newPos, BoardGame elem) {
				// TODO Auto-generated method stub
				return true;
			}
			
		}
		/**
		 * Execute move for a King Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveKing extends Functor {
			@Override
			public boolean CanMove(Pawn p, Position newPos, BoardGame elem) {
				if (newPos.diffMultiple(p.GetPosition()) != 1 || elem.getObstacleCase(newPos) == p.GetColor())
					return false; // A King Can't Move More Than One
				return true;
			}
			@Override
			public boolean ShouldMove(Pawn p, Position newPos, BoardGame elem) {
				//check if the newPos don't PutinCheckMat
				return true;
			}
		}
		/**
		 * Execute move for a Crazy Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveCrazy extends Functor {
			@Override
			public boolean CanMove(Pawn p, Position newPos, BoardGame elem) {
				if (newPos.diffMultiple(p.GetPosition()) == -1 ||
					elem.getObstacleRange(p, newPos) != eColor.None)
					return false; // Can't Move there
				if (elem.getObstacleCase(newPos) == p.GetColor())
					return false; // can't go on your pawn
				return true;
			}
			@Override
			public boolean ShouldMove(Pawn p, Position newPos, BoardGame elem) {
				//check if the newPos don't PutinCheckMat
				return true;
			}
		}
		/**
		 * Execute move for a Horse Pawwn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveCavalery extends Functor {
			@Override
			public boolean CanMove(Pawn p, Position newPos, BoardGame elem) {
				Position oldp = p.GetPosition();
				int d1 = oldp.diffRow(newPos);
				if ((d1 == 1 && oldp.diffColumn(newPos) == 2) ||
					(d1 == 2 && oldp.diffColumn(newPos) == 1))
					if (elem.getObstacleCase(newPos) != p.GetColor())
						return true; // Can't Move there
				return false; // can't go on your pawn
			}
			@Override
			public boolean ShouldMove(Pawn p, Position newPos, BoardGame elem) {
				//check if the newPos don't PutinCheckMat
				return true;
			}
		}
		/**
		 * Execute move for a Queen Pawn Class.
		 * @author Lumy-
		 *
		 */
		static private class DoMoveQueen extends Functor {
			@Override
			public boolean CanMove(Pawn p, Position newPos, BoardGame elem) {
				if (newPos.diffMultiple(p.GetPosition()) == -1 ||
						elem.getObstacleRange(p, newPos) != eColor.None)
					return false; // A King Can't Move More Than One
				return true;
			}
			@Override
			public boolean ShouldMove(Pawn p, Position newPos, BoardGame elem) {
				//check if the newPos don't PutinCheckMat
				return true;
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


	
	private static boolean isEatSomethingTower(Pawn p, Position newPos,
			BoardGame elem) {
		// TODO Auto-generated method stub
		return false;
	}

	

	
	

	static public boolean DoMovePawns(Pawn p, Position newPos, BoardGame elem)
	{
		for (Map.Entry<ePawns, MapFunctor.Functor> entry : Rules.MapFunctor.MapFunction.entrySet()) {
		    if (p.GetClass().equals((ePawns)entry.getKey())) {
		    	if ( ((MapFunctor.Functor)entry.getValue()).CanMove(p, newPos, elem) )
			    	if ( ((MapFunctor.Functor)entry.getValue()).ShouldMove(p, newPos, elem) ) {
			    		((MapFunctor.Functor)entry.getValue()).execute(p, newPos, elem);
			    		return true;
			    	}
		    	return false;
		    }
		}
		return false;
	}

	static public boolean CheckRule(String s, List<Pawn> p)
	{
		/*if (Character.isUpperCase(s.charAt(0)) && s.charAt(0) != 'P')
			return CheckHead(s.substring(1)); //King Queen...
		else
			return CheckPion( (s.charAt(0) == 'P') ? s.substring(1) : s);//Pion*/
		return false;
	}
};
