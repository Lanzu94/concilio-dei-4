package server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import server.model.gameComponent.Reward;
import server.model.gameComponent.cards.PoliticalCard;
import server.model.gameComponent.kingboard.KingBoard;
import server.model.gameComponent.map.Region;
import server.model.gameComponent.map.Towns;
import server.model.Player;

/**
 * This class represents the Game
 */
public class Game implements Serializable{
	private ArrayList<Player> players = new ArrayList<Player>();
	private long cod_game;
	private int min_number_player, max_number_player;
	private boolean isStarted;
	private int selectedMap;
	
	public Game(int min, int max)
	{
		min_number_player=min;
		max_number_player=max;
		selectedMap=1;
		Random x=new Random();
		do{
		cod_game=x.nextLong();
		}while(cod_game==-1);
	}
	public void setMap(int map){
		selectedMap=map;
	}
	public int getMap(){
		return selectedMap;
	}
	
	public long getCode()
	{
		return cod_game;
	}	
	
	//Return true if the game is already started
	public boolean isStarted(){
		return isStarted;
	}
	public void setStarted(){
		isStarted=true;
	}
	
	//Adds a new player to the game if the game is not started yet
	public void addPlayer(Player p){
		if(!isStarted())
			players.add(p);
	}
	
	//Return the number #i Player in the game
	public Player getPlayer(int i){
		return players.get(i);
	}
	
	//Return the number of players in the game
	public int numberOfPlayers(){
		return players.size();		
	}
	public int numberOfPlayersOnline() {
		int cont=0;
		for(int i=0;i<numberOfPlayers();i++)
			if(getPlayer(i).getStatePlayer().equals(EnumStatePlayer.inGame))
				cont++;
		return cont;
	}
	
	//Return the max and min number of players in the game
	public int maxNumberOfPlayers(){
		return max_number_player;		
	}
	public int minNumberOfPlayers(){
		return min_number_player;
	}

	public Player findPlayer(String name){
		for(int i=0;i<players.size();i++)
			if(players.get(i).getName().equals(name))
				return players.get(i);
		return null;
	}
	/*
	 * The methods and variable below manage the game when it starts
	 */
	//component of the game
	private ArrayList<Region>region=new ArrayList<Region>();
	private ArrayList<PoliticalCard>deckPoliticalCard=new ArrayList <PoliticalCard>();
	private ArrayList<PoliticalCard>deckPoliticalCardGarbage=new ArrayList <PoliticalCard>();//the political card that I used
	private Towns whereIsKing;
	private KingBoard kingBoard;
	private ArrayList <Reward> nobilityRewards=new ArrayList <Reward>();
	private int whoseTurn;
	private int gameTurnStatic;
	private int count;//with this variable I check that everyone has played his turn
	
	//manage the turn
	public void setWhoseTurn(int i){
		whoseTurn=i;
	}
	public int getWhoseTurn(){
		return whoseTurn;
	}
	public void setTurnStatic(int i){
		gameTurnStatic=i;
	}
	public int getTurnStatic(){
		return gameTurnStatic;
	}
	public void setCount(){
		count=0;
	}
	public int getCount(){
		return count;
	}
	public void addCount(){
		count++;
	}
	
	//Save Map
	public void addRegion(Region r){
		region.add(r);
	}
	public Region getRegion(int i){
		return region.get(i);
	}
	public int sizeRegion(){
		return region.size();
	}

	//King
	public void moveKing(Towns t){
		whereIsKing=t;
	}
	public Towns whereIsKing(){
		return whereIsKing;
	}
	
	//Save Politics Deck
	public void addPoliticalCard(PoliticalCard p){
		deckPoliticalCard.add(p);
	}
	public PoliticalCard pickUpPoliticalCard(){
		Random x=new Random();
		int i=x.nextInt(deckPoliticalCard.size());
		PoliticalCard p=deckPoliticalCard.get(i);
		deckPoliticalCardGarbage.add(p);
		deckPoliticalCard.remove(i);
		if(deckPoliticalCard.size()==0)
			mixDeckPoliticalCard();
		return p;
	}
	private void mixDeckPoliticalCard() {
		deckPoliticalCard.addAll(deckPoliticalCardGarbage);
		deckPoliticalCardGarbage.clear();
	}

	public int sizePoliticDeck(){
		return deckPoliticalCard.size();
	}
	
	//Save KingBoard
	public void setKingBoard(KingBoard b){
		kingBoard=b;
	}
	public KingBoard getKingBoard(){
		return kingBoard;
	}
	
	//Save Nobility Road
	public void addNobilityBonus(Reward r){
		nobilityRewards.add(r);
	}
	public Reward getNobilityBonus(int i){
		return nobilityRewards.get(i);
	}
	public int sizeNobilityBonus(){
		return nobilityRewards.size();
	}
	
	//variables and methods for the Market
	private int playerDoneOffert;//count the players that have finished to do offer, when is equal to numberOfPlayer the people can buy other things
	private int playerDoneBuying;//count the players that have finished to buy
	private int playerTurnBuying;//save who has the turn
	public void setPlayerDoneOffert(){
		playerDoneOffert=0;
	}
	public int getPlayerDoneOffert(){
		return playerDoneOffert;
	}
	public void addPlayerDoneOffert(){
		playerDoneOffert++;
	}
	public void setPlayerDoneBuying(){
		playerDoneBuying=0;
	}
	public int getPlayerDoneBuying(){
		return playerDoneBuying;
	}
	public void addPlayerDoneBuying(){
		playerDoneBuying++;
	}
	public void setPlayerTurnBuying(int i){
		playerTurnBuying=i;
	}
	public int getPlayerTurnBuying(){
		return playerTurnBuying;
	}
		
	/*
	 * We have an item in arraylist for every player's offer and every item is a vector string build in this way:
	 * {namePlayer, what he offers:(permitCardNumber, politicalCardNumber, quantityOfHelper), which or how much, moneyHeWants}
	 * {Angelo,0,0,5} -> Angelo offers you permit card number 0 for 5 money
	 */
	private ArrayList <String[]>market;
	public void setMarket(){
		market=new ArrayList<String[]>();
	}
	public void addOffert(String[]offert){
		market.add(offert);
	}
	public String[] getOffert(int i){
		return market.get(i);
	}
	public String[][] readOfferts(){
	String[][]offerts=new String[sizeMarket()][GameConstants.validLenghtOffert];
	for(int i=0;i<offerts.length;i++)
	{
		for(int j=0;j<offerts[i].length;j++)
		{
			offerts[i][j]=getOffert(i)[j];
		}
	}
	return offerts;
	}
	public int sizeMarket(){
		return market.size();
	}
	public void removeOffert(int i){
		market.remove(i);
	}
	public boolean checkValidInsertOffer(String[]offert){
		//check if the player inserted valid numbers
		if(offert.length>GameConstants.validLenghtOffert)
			return false;
		try{
			if(Integer.parseInt(offert[1])>GameConstants.differentObjectCanSell-1)
				return false;
			if(Integer.parseInt(offert[2])<0)
				return false;
			if(Integer.parseInt(offert[3])<0)
				return false;
		}catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}
	public boolean checkPlayerHasEnoughResourcesForOffert(String[]offert,Player player){
		//check if the player has the resources that he is offering
		switch(Integer.parseInt(offert[1]))
		{
		case 0://permit card
			if(Integer.parseInt(offert[2])>=player.permitDeckSize())
				return false;
			break;
		case 1://political card
			if(Integer.parseInt(offert[2])>=player.politicalDeckSize())
				return false;
			break;
		case 2://helper
			if(Integer.parseInt(offert[2])>player.getHelper())
				return false;
			break;
		}
		return true;
	}
	public boolean offertNotExist(String[] offert) {
		for(int i=0;i<market.size();i++)
		{
			String[]m=market.get(i);
			if(offert[0].equals(m[0]) && offert[1].equals(m[1]) && offert[2].equals(m[2]))
				return false;
		}
		return true;
	}
	
	//manage game session
	private int isInTurn;//1=turn 2=offer 3=buy
	public void setIsInTurn(int b){
		isInTurn=b;
	}
	public int getIsInTurn(){
		return isInTurn;
	}

}
