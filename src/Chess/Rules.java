/**
 * 
 */
package Chess;

import java.util.ArrayList;
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
				if (Rules.isDraw(tmp, p.GetEnemyColor(), elem)) {
					ret = eGameState.DRAW;
					System.out.println("DRAW DETECTED");
				}
				else if ((n = Rules.CheckKing.isCheckKing(tmp, elem)) != eColor.None)
					ret = (n == eColor.Black ? eGameState.CHECK_KING_B : eGameState.CHECK_KING_W);
				else if ((n = Rules.CheckMate.isCheckMate(tmp, elem)) != eColor.None)
					ret = (n == eColor.Black ? eGameState.CHECK_MATE_B : eGameState.CHECK_MATE_W);
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
				if (elem.getObstacleCase(newPos) == p.GetColor())
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
				if ((newPos.column - 1 == p.GetPosition().column || newPos.column + 1 == p.GetPosition().column) && // Move Diagonal
				    elem.contains(newPos)) { // Contain Something and not Same Color (Enemy)
					if (newPos.diffRow(p.GetPosition()) == 1)// try to make one move
						if (elem.getObstacleCase(newPos) != p.GetColor())
							return eMoveState.SUCCESS;
						else
							return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				}
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
				if (elem.getObstacleCase(newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
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
				if (elem.getObstacleCase(newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
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
				System.out.println("------Can Move In Crazy");
				if (elem.getObstacleCase(newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				if (!newPos.DiagonalMove(p.GetPosition()) || //Check if RealDiagonal
					elem.getObstacleRange(p, newPos) != eColor.None) // Check if Nothing on the way
					return eMoveState.FAIL_UNAUTHORIZED; // Can't Move there
				if (elem.getObstacleCase(newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED; // can't go on your pawn
				System.out.println("------Can Move Out");
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
				if (elem.getObstacleCase(newPos) == p.GetColor())
					return eMoveState.FAIL_SAME_COLOR_CASE_OCCUPIED;
				if (newPos.diffMultiple(p.GetPosition()) == -1 || // Check True Diagonal 
						elem.getObstacleRange(p, newPos) != eColor.None) // Check no Obstacle
					return eMoveState.FAIL_UNAUTHORIZED; // Obstacle or no True Diagonal
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

	static private class CheckMate {
		public static boolean isCheckMate(List<Pawn> tmp, BoardGame elem, eColor e) {
			/*
			 * Check is extremely simple actually. If any piece can currently move to the king's position, 
			 * then the king is in check. Since you already have to implement the ability to allow players 
			 * to move any given piece to any square (that their piece can move to), deciding if 
			 * those pieces can be moved to the king's square should be trivial.
			 * For checkmate, it is a little harder, but first decide whether the king can move his 
			 * piece to a square that puts him out of check (by temporarily 'pretending' the king is at a
			 *  different square, and seeing if he is in check still, and doing that for every square
			 *   around him). If he can't, it still might not be checkmate. So now you have to see if 
			 *   there is any piece that can either be moved to a square that blocks the 'check' or that
			 *    can take the piece that is causing the checkmate.
			 */
			return false;
		}
		public static eColor isCheckMate(List<Pawn> tmp, BoardGame elem) {
			eColor e = CheckKing.isCheckKing(tmp, elem);
			if (e == eColor.Black)
				isCheckMate(tmp, elem, eColor.Black);
			else if (e == eColor.White)
				isCheckMate(tmp, elem, eColor.White);
			return eColor.None;
		}
	}
	static class CheckKing
	{
		private static boolean isCheckKing_LaunchRange(List<Pawn> tmp, BoardGame elem, Position pos, Position nps, eColor e) {
			eColor ret = elem.getObstacleRange(tmp, elem.get(tmp, elem.indexOf(tmp, pos)), nps);
			if (ret != eColor.None && e != ret) // Something not Same Color
			{
				Position a = elem.getPositionFirstObstacleRange(tmp, elem.get(tmp, elem.indexOf(tmp, pos)), nps);
				ePawns p = elem.get(tmp, elem.indexOf(tmp, a)).GetClass();
				switch (p)
				{
				case Queen:
					return true;
				case Tower:
					return true;
				case King:
					if (a.diffMultiple(pos) == 1)
						return true;
				default:
					return false;
				}
			}
			return false;
		}
		private static boolean isCheckKing_LaunchRangeDiagonal(List<Pawn> tmp, BoardGame elem, Position pos, Position nps, eColor e) {
			eColor ret = elem.getObstacleRange(tmp, elem.get(tmp, elem.indexOf(tmp, pos)), nps);
			if (ret != eColor.None && e != ret) // Something not Same Color
			{
				Position a = elem.getPositionFirstObstacleRange(tmp, elem.get(tmp, elem.indexOf(tmp, pos)), nps);
				ePawns p = elem.get(tmp, elem.indexOf(tmp, a)).GetClass();
				switch (p)
				{
				case Crazy:
					return true;
				case Queen:
					return true;
				case King:
					if (a.diffMultiple(pos) == 1)
						return true;
				case Pawn:
					if (a.diffMultiple(pos) == 1)
						return true;
				default:
					return false;
				}
			}
			return false;
		}
		private static boolean isCheckKing_LaunchRow(List<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
			Position nps = new Position();
			nps.row = pos.row;
			nps.column = 'a' - 1;// Set to 'a' -1 to Check Position 'a'
			if (isCheckKing_LaunchRange(tmp, elem, pos, nps, e))
				return true;
			nps.row = pos.row; 
			nps.column = 'i';// Set to 'i' to check Position 'h'
			if (isCheckKing_LaunchRange(tmp, elem, pos, nps, e))
				return true;
			return false;
		}
		private static boolean isCheckKing_LaunchColumn(List<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
			Position nps = new Position();
			nps.row = 0; // Set to 0 to Check Position 1
			nps.column = pos.column;
			if (isCheckKing_LaunchRange(tmp, elem, pos, nps, e))
				return true;
			nps.row = 9; // Set to 9 to check Position 8
			nps.column = pos.column;
			if (isCheckKing_LaunchRange(tmp, elem, pos, nps, e))
				return true;
			return false;
	}
		private static boolean isCheckKing_LaunchDiagonal(List<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
			Position nps = new Position();
			nps.SetPosition(pos);
			while (nps.row > 0 && nps.column > 'a' - 1) {
				nps.column -= 1;
				nps.row -= 1;
			}
			if (isCheckKing_LaunchRangeDiagonal(tmp, elem, pos, nps, e))
				return true;
			nps.SetPosition(pos);
			while (nps.row < 9 && nps.column < 'i') {
				nps.column += 1;
				nps.row += 1;
			}
			if (isCheckKing_LaunchRangeDiagonal(tmp, elem, pos, nps, e))
				return true;
			nps.SetPosition(pos);
			while (nps.row < 9 && nps.column > 'a' - 1) {
				nps.column -= 1;
				nps.row += 1;
			}
			if (isCheckKing_LaunchRangeDiagonal(tmp, elem, pos, nps, e))
				return true;
			nps.SetPosition(pos);
			while (nps.row > 0 && nps.column < 'i') {
				nps.column += 1;
				nps.row -= 1;
			}
			if (isCheckKing_LaunchRangeDiagonal(tmp, elem, pos, nps, e))
				return true;
			return false;
		}
		private static boolean _isCheckKing_LaunchCavalery(List<Pawn> tmp, BoardGame elem, Position oldp, eColor e) {
			if (elem.contains(tmp, oldp)) {
				Pawn p = elem.get(tmp, elem.indexOf(tmp, oldp));
				if (p.GetColor() != e && p.GetClass() == ePawns.Cavalery)
					return true;
			}
			return false;
		}
		private static boolean isCheckKing_LaunchCavalery(List<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
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
			for (Pair<Integer, Integer> a : l) {
				oldp.SetPosition(pos);
				oldp.row += a.GetLeft();
				oldp.column += a.GetRight();
				if (_isCheckKing_LaunchCavalery(tmp, elem, oldp, e))
					return true;
			}
			return false;
		}
		private static boolean isCheckKing(List<Pawn> tmp, BoardGame elem, Position pos, eColor e) {
			if (isCheckKing_LaunchRow(tmp, elem, pos, e))
				return true;
			if (isCheckKing_LaunchColumn(tmp, elem, pos,e))
				return true;
			if (isCheckKing_LaunchDiagonal(tmp, elem, pos,e))
				return true;
			if (isCheckKing_LaunchCavalery(tmp, elem, pos, e))
				return true;
			return false;
		}
		public static boolean isCheckKing(List<Pawn> tmp, BoardGame elem, eColor e) {
			Position k = elem.getPawnsBoardPosition(e, ePawns.King, tmp);
			return isCheckKing(tmp,  elem,  k, e);
		}
		public static eColor isCheckKing(List<Pawn> tmp, BoardGame elem) {
			if (isCheckKing(tmp, elem, eColor.White))
				return eColor.White;
			if (isCheckKing(tmp, elem, eColor.Black))
				return eColor.Black;
			return eColor.None;
		}
	}
	public static boolean isDraw(List<Pawn> tmp, eColor c, BoardGame elem) {	
			if (ImpossibilityCheckMate(tmp, elem))
				return true;
			if (Stalemate(tmp, c, elem))
				return true;
			return false;
		}
		private static boolean Stalemate(List<Pawn> tmp, eColor e, BoardGame elem) {
			//List<Pawn> allColor = elem.getAllColor(e);
			//for (Pawn p : allColor)
			//	if (CheckMat(tmp, p, ))
			//	return false;
			// for each Pawns of Next Player
			// if all == eGameState.CheckMat_my_color
			// return true
			return false;
		}

	private static boolean ImpossibilityCheckMate(List<Pawn> tmp, BoardGame elem) {
		if (tmp.size() <= 4) {
			
			if (tmp.size() == 2 && null != elem.getPawnsBoardPosition(eColor.Black, ePawns.King, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.King, tmp)) // king versus king Draw !
				return true;
			else if (tmp.size() == 3) {
				System.out.println("# Element");
				if (null != elem.getPawnsBoardPosition(eColor.Black, ePawns.King, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.King, tmp) &&
					(null != elem.getPawnsBoardPosition(eColor.Black, ePawns.Crazy, tmp) || null != elem.getPawnsBoardPosition(eColor.White, ePawns.Crazy, tmp))) //king and bishop versus king
					return true;
				if (null != elem.getPawnsBoardPosition(eColor.Black, ePawns.King, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.King, tmp) &&
				    (null != elem.getPawnsBoardPosition(eColor.Black, ePawns.Cavalery, tmp) || null != elem.getPawnsBoardPosition(eColor.White, ePawns.Cavalery, tmp) )) //king and knight versus king
						 	return true;
			}
			else
					if (null != elem.getPawnsBoardPosition(eColor.Black, ePawns.King, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.King, tmp) &&
						(null != elem.getPawnsBoardPosition(eColor.Black, ePawns.Crazy, tmp) && null != elem.getPawnsBoardPosition(eColor.White, ePawns.Crazy, tmp) )) //king and bishop versus king and bishop with the bishops on the same colour.
						if (elem.getPawnsBoardPosition(eColor.Black, ePawns.Crazy, tmp).GetColorCase() == elem.getPawnsBoardPosition(eColor.White, ePawns.Crazy, tmp).GetColorCase())
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
	public static Pair<eMoveState, eGameState> DoMovePawns(List<Pair<Position, Position>> l, Pawn p,
			Position click,	BoardGame elem) {
		eMoveState r = eMoveState.FAIL_CLASS_UNKNOWN;
		eGameState r2 = eGameState.SAME;
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
					}
				}
			return new Pair<eMoveState, eGameState>(eMoveState.FAIL_CHECK, r2);
		}
		return new Pair<eMoveState, eGameState>(r, r2);
	}
}
