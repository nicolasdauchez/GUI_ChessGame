/**
 * 
 */
package Chess;

import java.util.List;
/**
 * @author Lumy-
 *
 */
public class Rules {

	static private void DoEatPieces(Pion p, Pion eaten, BoardGame lp)
	{
		lp.remove(eaten);
		p.SetPosition(eaten.GetPosition());
	}
	
	static private boolean DoMoveHead(Pion p, Position newPos, BoardGame elem)
	{
		boolean f = false;
		switch (p.GetClass())
		{
		case King:
			break;
		case Queen:
			break;
		case Cavalery:
			break;
		case Tower:
			if (canMoveTower(p, newPos, elem))
				f = isEatSomethingTower(p, newPos, elem);
			else
				p.SetPosition(newPos);
			break;
		case Crazy:			
			break;
		default:
			break;
		}
		if (f)
			DoEatPieces(p, elem.get(elem.indexOf(newPos)), elem);//can use indexOf with pos because overload Equal
		return true;
	}
	
	private static boolean isEatSomethingTower(Pion p, Position newPos,
			BoardGame elem) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean canMoveTower(Pion p, Position newPos, BoardGame elem) {
		Position oldp = p.GetPosition();
		if (oldp.sameRow(newPos) || oldp.sameColumn(newPos))
			if (elem.isNoObstacle(p, newPos) || elem.isObstacle(p, newPos)) // check if free way or if can move at pos and eat (free way too)
				return true;
		return false; // else there is pion of same color or unreacheable position
	}

	

	static public boolean DoMovePion(Pion p, Position newPos, BoardGame elem)
	{
		if (elem.contains(newPos)) // we can do this because we overload Equal to compare with Position
			if (newPos.column == p.GetPosition().column ||
			(newPos.column - 1 != p.GetPosition().column && newPos.column + 1 != p.GetPosition().column))
				return false;
			else
			{
				DoEatPieces(p, elem.get(elem.indexOf(newPos)), elem);//can use indexOf with pos because overload Equal
				return true;
			}
		else if (newPos.column == p.GetPosition().column)
		{
			p.SetPosition(newPos);
			return true;
		}
			return false;
	}
	
	public boolean CheckRule(String s, List<Pion> p)
	{
		/*if (Character.isUpperCase(s.charAt(0)) && s.charAt(0) != 'P')
			return CheckHead(s.substring(1)); //King Queen...
		else
			return CheckPion( (s.charAt(0) == 'P') ? s.substring(1) : s);//Pion*/
		return false;
	}
}
