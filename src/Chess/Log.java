/**
 * 
 */
package Chess;
import main.Pair;

import com.codethesis.pgnparse.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
		PGNSource test;

		try {
			test = new PGNSource(new File("wcc.PGN"));
			List<PGNGame> p = test.listGames();
			
		} catch (IOException | NullPointerException | PGNParseException | MalformedMoveException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//
		try {
			test = new PGNSource(new File("wccc1974.PGN"));
			//System.out.println(test.toString());
			try {
				List<PGNGame> p = test.listGames();
				System.out.println(p.size());
//				System.out.println(p.toString());
				for (PGNGame g : p)
				{
					int i = 0;
					for (int max = g.getMovesCount(); i < max; i++) {
						PGNMove m = g.getMove(i);
					}
					break;
				}
			} catch (NullPointerException | PGNParseException | IOException
					| MalformedMoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
