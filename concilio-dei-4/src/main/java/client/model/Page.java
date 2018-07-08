package client.model;

import java.io.Serializable;
import java.util.Observable;

/**
 * This class save in which page the game is currently and some other information for the view
 * Every time this class is update View update itself
 */
public class Page extends Observable implements Serializable{
	/*
	 * In this page I save in which page the player is 
	 */
	private EnumPage page;
	private long[][]listOfGames;
	
	public Page()
	{
	}
	
	public EnumPage getPage()
	{
		return page;
	}
	
	public void setPage(EnumPage s, Object d)
	{
		page=s;
		EnumPage pageToGiveView=page;
		if(page.equals(EnumPage.join_game))
			listOfGames=(long[][])d;
		update(d);
	}
	public long[][]getListOfGames(){
		//I save the list of games in the client because this list can change while player chose the game in the list
		return listOfGames;
	}
	
	//status local game
	private LocalStateGame localStateGame;
	public void setLocalStateGame(LocalStateGame l){
		localStateGame=l;
	}
	public LocalStateGame getLocalStateGame(){
		return localStateGame;
	}
	
	//timer
	private int timer;
	public void updateTimer(int t){
		timer=t;
		update(EnumStatusGame.timerTurn);
	}
	
	private void update(Object ob){
		setChanged();
		//I sent to the view the page and the date that it must print
	    notifyObservers(ob);
	}
	
	public int getTimer(){
		return timer;
	}
}
