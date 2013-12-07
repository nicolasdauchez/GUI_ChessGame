/**
 * 
 */
package Chess;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import main.Pair;

/**
 * @author Lumy-
 *
 */
public class Import {
	static class ParseHeader
	{
		
	}

	public static Pair<ChessDataGame, Log> ImportPathName(String path)
	{
		System.out.println(path);
		File fd = new File(path); 
		BufferedReader reader = null;
		//ParseHeader p = new Import.ParseHeader();
		Pair<ChessDataGame, Log> ret = null;
        try {
	    reader = new BufferedReader(new FileReader(fd));
	    if (reader != null) {
	        ret = ImportGame(reader);
			reader.close();
			}
        }
        catch (IOException e) {
			e.printStackTrace();
				}
		return ret;
	}

	private static Pair<ChessDataGame, Log> ImportGame(BufferedReader reader)
	{
		int ret = 0;
		int line = 0;
		int size = 80;
		char[] cbuf = new char[80];
	    String text = null;
	    List<String> list = new ArrayList<String>();
	    Log l = new Log();
	    try {
	    	while ((ret = reader.read(cbuf, 0, size)) != -1)
	    	{
	    		text = new String(cbuf);
//	    		System.out.println(text);
	    		System.out.println(text.length());
	    		if (text.charAt(0) == '[') ImportHeader(text, list);
				else if (text.charAt(0) == '\n') line += 1;
				else ImportLog(text, l);
		    	//if (line == 2) return new Pair<ChessData, Log>(new ChessData(list), l);
			//Game Loaded
	    	//Parse Item
	    	}
	    }
	    catch (FileNotFoundException e) {
		}
		catch (Exception e) {
			//File Doesn't respect The PGN Norme
			System.out.println("End Of File Reached");
		}
	    return null;
	}

	static private void ImportHeader(String text, List<String> list)
	{
//		System.out.println(text);
	}
	private static void		ImportLog(String text, Log l)
	{
//		System.out.println(text);
	}
}
