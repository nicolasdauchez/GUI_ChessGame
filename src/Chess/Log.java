/**
 * 
 */
package Chess;
import java.util.ArrayList;
/**
 * @author Lumy-
 * Log Import and Export with PGN Norme
 */
public class Log {

	public ArrayList<String> log;

	public Log() {
		log = new ArrayList<String>();
	}
	
	public void Initialize()
	{
		
	}

	private String getLastLog() {
		if (log.size() != 0)
			return log.get(log.size() - 1);
		return null;
	}
	private void add(String e) {
		log.add(e);
	}
	public void addEvent(String e) {
		String c = "[Event \"" + e + "\"]";
		add(c);
	}
	public void addSite(String e) {
		String c = "[Site \"" + e + "\"]";
		add(c);
	}
	public void addDate(String e) {
		String c = "[Date \"" + e + "\"]";
		add(c);
	}
	public void addRound(String e) {
		String c = "[Round \"" + e + "\"]";
		add(c);
	}
	public void addWhite(String e) {
		String c = "[White \"" + e + "\"]";
		add(c);
	}
	public void addBlack(String e) {
		String c = "[Black \"" + e + "\"]";
		add(c);
	}
	public void addResult(String e) {
		String c = "[Result \"" + e + "\"]";
		add(c);
	}
	//[ECO "C40/01"]
	public Boolean Export(ChessData header) {
		Export e = new Export(header, log);
		return false;
	}
	public void Import(String path) {
		Import.ImportPathName(path);
		return ;
	}
}
