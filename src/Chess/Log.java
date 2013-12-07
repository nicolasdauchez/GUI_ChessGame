/**
 * 
 */
package Chess;
import main.Pair;
import java.util.ArrayList;
/**
 * @author Lumy-
 * Log Import and Export with PGN Norme
 * Also log each Mouvement. You can move in the game (backmove foremove)
 */
public class Log {

	public ArrayList<Pair<Position, Position>> log;

	public Log() {
		log = new ArrayList<Pair<Position, Position>>();
	}
	
	public void newGame() {
		log.clear();
	}

	public void Initialize()
	{		
	}

/*	private String getLastLog() {
		if (log.size() != 0)
			return log.get(log.size() - 1);
		return null;
	}*/


	public void add(Position oldp, Position newp) {
		log.add(new Pair<Position, Position>(oldp, newp));
	}

	public Boolean Export(ChessDataGame header) {
		Export e = new Export(header, log);
		return false;
	}
	public void Import(String path) {
		Import.ImportPathName(path);
		return ;
	}
}
