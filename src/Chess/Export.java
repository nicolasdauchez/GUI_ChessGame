/**
 * 
 */
package Chess;
import main.Pair;

import java.util.List;
/**
 * @author Lumy-
 *
 */
public class Export {
	public Export(ChessDataGame header, List<Pair<Position, Position>> log)
	{
		
	}
	private void exportLog(Position oldp, Position newp) {
		String s = "" + oldp.column + "" + oldp.row + "->" + newp.column + "" + newp.row;
	}

}
