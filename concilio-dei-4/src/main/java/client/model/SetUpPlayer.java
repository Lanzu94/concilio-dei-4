package client.model;

import java.io.Serializable;
import java.util.Observable;

/**
 * Save here some information about the client
 */
public class SetUpPlayer extends Observable implements Serializable{
	/*
	 * In this class I save same think about local player (the connection mode for example)
	 */
	private String connection;//save the connection mode, RMI or Socket
	private String name;
	private long codeGame;
	
	public SetUpPlayer()
	{	
	}
	public void setConnectionMode(String c)
	{
		connection=c;
	}
	public String getConnetionMode()
	{
		return connection;
	}
	public void setName(String c)
	{
		name=c;
	}
	public String getName()
	{
		return name;
	}
	public void setCodeGame(long c)
	{
		codeGame=c;
	}
	public long getCodeGame()
	{
		return codeGame;
	}
	//manage the graphics
	private String graphic;//save the selected graphic
	public void setUpGraphic(String graphicChosen){
		graphic=graphicChosen;
	}
	public String getGraphicChosen(){
		return graphic;
	}
}
