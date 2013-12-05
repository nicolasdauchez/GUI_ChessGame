/**
 * 
 */
package Chess;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author Lumy-
 *
 */
public class ChessData {
	private String 	nameWhite;
	private String 	nameBlack;
	private String 	Event;
	private String 	Date;
	private String	Round;
	private String 	Result;
	
	public ChessData(List<String> a)
	{
	}
	public ChessData(String nw, String nb, String event)
	{
		this(nw, nb, event, "1");
	}
	public ChessData(String nw, String nb, String event, String round)
	{
		this(nw, nb, event, round, "*");
	}
	public ChessData(String nw, String nb, String event, String round, String res)
	{
		this(nw, nb, event, round, res, new Date());
	}
	public ChessData(String nw, String nb, String e, String round, String res, Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String day = sdf.format(date);
		nameWhite = nw;
		nameBlack = nb;
		Event = e;
		Date = day;
		Round = round;
		Result = res;
	}
}
