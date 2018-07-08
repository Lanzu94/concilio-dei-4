package client.model;

/**
 * This class save some costant used in the client
 */
public class ClientConstants {
	//Error Message
	final static public String SERVER_IS_DOWN="Server is Down";
	final static public String PROBLEM_SLEEP="Problem with sleep";
	//how often update the time available
	final static public int timeRepeat=1*1000;
	//Action available at the start of turn
	final static public int mainActionCanDo=1;
	final static public int secondaryActionCanDo=1;
	//Graphycs Mode
	final static public String GUI="GUI";
	final static public String CLI="CLI";
	//Connection Mode
	final static public String RMI="RMI";
	final static public String Socket="Socket";
	//Server date
	final static public String servername="localhost";
	
	final static public int serverport=9001;
}
