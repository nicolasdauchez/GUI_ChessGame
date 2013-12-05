/**
 * 
 */
package Chess;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * This represent the information about one game of Chess
 * you can say it's like the Header of a game
 * @author Lumy-
 *
 */
public class ChessDataGame {
	private String 	nameWhite; //Never Change
	private String 	nameBlack; // Never Change
	public 	String 	Place;
	public	String 	Event;
	private String 	Date; // Date of the Day or Date by import.
	public String	Round;
	public String 	Result;
	private eColor	Turn;
	
	public ChessDataGame(List<String> a)
	{
	}
	public ChessDataGame(String nw, String nb)
	{
		this(nw, nb, "1");
	}
	public ChessDataGame(String nw, String nb, String round)
	{
		this(nw, nb, round, "*");
	}
	public ChessDataGame(String nw, String nb, String round, String res)
	{
		this(nw, nb, round, res, new Date());
	}

	public ChessDataGame(String nw, String nb, String round, String res, Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String day = sdf.format(date);
		nameWhite = nw;
		nameBlack = nb;
		Date = day;
		Round = round;
		Result = res;
		Turn = eColor.White;
	}

	public ChessDataGame(String nw, String nb, String round, String res, Date date, String place, String event)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String day = sdf.format(date);
		nameWhite = nw;
		nameBlack = nb;
		Place = place;
		Event = event;
		Date = day;
		Round = round;
		Result = res;
		Turn = eColor.White;
	}

	public void setResult(String res)
	{
		Result = res;
	}

	public String	getHeader()
	{
		String s = "[Event \"" + Event + "\"]\n[Site \"" + Place + "\"]\n[Date \"" + Date.toString() + "\"]\n";
		s += "[Round \"" + Round + "\"]\n[White \"" + nameWhite + "\"\n[Black \"" + nameBlack + "\"]\n[Result \"" + Result + "\"]\n";
		System.out.print(s);
		return s;
	}
	public eColor	getPlayerTurn()
	{
		return Turn;
	}
}
