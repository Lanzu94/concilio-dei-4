package client.model;

import java.awt.Color;
import java.io.Serializable;


/**
 * Save here some information that the server sent to the client (like status map for example)
 */
public class LocalStateGame implements Serializable{
	/*
	 * In this class I save the state of the game in the client so I know the date that I need to print
	 */
	private SetUpPlayer player;
	public LocalStateGame(SetUpPlayer p){
		player=p;
	}
	public String getPlayerName(){
		return player.getName();
	}
	//timer
	int timerTurn;
	public void setTimerTurn(int i){
		timerTurn=i;
	}
	public int getTimerTurn(){
		return timerTurn;
	}
	int timerMarket;
	public void setTimerMarket(int i){
		timerMarket=i;
	}
	public int getTimerMarket(){
		return timerMarket;
	}
	
	//map
	private String[][]map;
	private String[]regionName;
	private String whereIsKing;
	private Color[]colorTowns;
	public void setMap(String[][] m){
		map=m;
	}
	public String[][] getMap(){
		return map;
	}
	public void setRegionName(String[]s){
		regionName=s;
	}
	public String[] getRegionName(){
		return regionName;
	}
	public void setWhereIsKing(String k){
		whereIsKing=k;
	}
	public String getWhereIsKing(){
		return whereIsKing;
	}
	public void setColorTowns(Color[]color){
		colorTowns=color;
	}
	public Color[] getColorTowns(){
		return colorTowns;
	}
	
	//permitCardVisible
	private String[][] permitCardsTowns;
	public void setPermitCardVisible(String[][]s){
		permitCardsTowns=s;
	}
	public String[][] getPermitCardsTown(){
		return permitCardsTowns;
	}
	
	//kingBoard
	private String councilBalconyName[][];
	private Color councilBalconyColor[][];
	private int[]kingReward;
	private String[][]bonusReward;
	private String councillorNameOutOfBalcony[];
	private Color councillorColorOutofBalcony[];
	public void setCouncilBalconyName(String[][]s){
		councilBalconyName=s;
	}
	public String[][] getCouncilBalconyName() {
		return councilBalconyName;
	}
	
	public void setCouncilBalconyColor(Color[][]c){
		councilBalconyColor=c;
	}
	public Color[][] getCouncilBalconyColor(){
		return councilBalconyColor;
	}
	public void setKingReward(int []v){
		kingReward=v;
	}
	public int[] getKingReward(){
		return kingReward;
	}
	public void setBonusReward(String[][]s){
		bonusReward=s;
	}
	public String[][] getBonusReward(){
		return bonusReward;
	}
	public void setCouncillorNameOutOfBalcony(String[]s){
		councillorNameOutOfBalcony=s;
	}
	public String[] getCouncillorNameOutOfBalcony() {
		return councillorNameOutOfBalcony;
	}
	
	public void setCouncillorColorOutOfBalcony(Color[]c){
		councillorColorOutofBalcony=c;
	}
	public Color[] getCouncillorColorOutOfBalcony(){
		return councillorColorOutofBalcony;
	}
	
	//nobilityRoad
	private int[][]bonusNobilityRoad;
	public void setBonusNobilityRoad(int[][]n){
		bonusNobilityRoad=n;
	}
	public int[][] getBonusNobilityRoad(){
		return bonusNobilityRoad;
	}
	
	//opponents
	private String[][]statusOpponents;
	private Color[]colorOpponent;
	public void setStatusOpponent(String[][]o){
		statusOpponents=o;
	}
	public String[][] getStatusOpponent(){
		return statusOpponents;
	}
	public void setColorOpponent(Color[]c){
		colorOpponent=c;
	}
	public Color[] getColorOpponent(){
		return colorOpponent;
	}
	
	//player
	private String[]statusPlayer;
	private Color color;
	private String[]politicalCards;
	private Color[]politicalCardsColors;
	private String[][] permitCardsTownsPlayer;
	public void setStatusPlayer(String[]s){
		statusPlayer=s;
	}
	public String[] getStatusPlayer(){
		return statusPlayer;
	}
	public void setColorPlayer(Color c){
		color=c;
	}
	public Color getColorPlayer(){
		return color;
	}
	public void setPoliticalCards(String[]s){
		politicalCards=s;
	}
	public String[] getPoliticalCards(){
		return politicalCards;
	}
	public void setColorPoliticalCards(Color[]c){
		politicalCardsColors=c;
	}
	public Color[] getColorPoliticalCards(){ 
		return politicalCardsColors;
	}
	public void setPermitCardsTownsPlayer(String[][]s){
		permitCardsTownsPlayer=s;
	}
	public String[][] getPermitCardsTownsPlayer(){
		return permitCardsTownsPlayer;
	}
	
	//manager turn
	private int mainActionRemain;//the main action that the player can do
	private int secondaryActionRemain;//the secondary action the player can do
	private EnumStatusGame statusGame;//I use this variable to know what stamp in game_status page
	public void setStatusGame(EnumStatusGame en){
		statusGame=en;
	}
	public EnumStatusGame getStatusGame(){
		return statusGame;
	}
	public void setMainActionRemain(int i){
		mainActionRemain=i;
	}
	public int getMainActionRemain(){
		return mainActionRemain;
	}
	public void setSecondaryAction(int i){
		secondaryActionRemain=i;
	}
	public int getSecondaryAction(){
		return secondaryActionRemain;
	}
	
	//when the player do multiple chose I save them here and then send them to the server
	//ACTION 1
	private int councillorOutOfBalconyChosen;
	public void setCouncillorOutOfBalconyChosen(int i){
		councillorOutOfBalconyChosen=i;
	}
	public int getCouncillorOutOfBalconyChosen(){
		return councillorOutOfBalconyChosen;
	}
	
	//ACTION 2
	private int balconyChosenForCorruption;
	public void setBalconyChosenForCorruption(int i){
		balconyChosenForCorruption=i;
	}
	public int getBalconyChosenForCorruption(){
		return balconyChosenForCorruption;
	}

	private int[] politicalCardsUsedForCorruption;
	public void setPoliticalCardsUsedForCorruption(int[]v){
		politicalCardsUsedForCorruption=v;
	}
	public int[] getPoliticalCardsUsedForCorruption(){
		return politicalCardsUsedForCorruption;
	}
	
	//ACTION 3
	private int indexPermitCard;
	public void setIndexPermitCard(int i){
		indexPermitCard=i;
	}
	public int getIndexPermitCard(){
		return indexPermitCard;
	}
	
	//ACTION 4
	private int[]politicalCardUseToKing;
	public void setPoliticalCardUseToKing(int[] v){
		politicalCardUseToKing=v;
	}
	public int[] getPoliticalCardUseToKing(){
		return politicalCardUseToKing;
	}
	private String townWhereToBuild;
	public void setTownWhereToBuild(String town){
		townWhereToBuild=town;
	}
	public String getTownWhereToBuild(){
		return townWhereToBuild;
	}
	
	//MARKET
	String[][] offerts;
	public void setOfferts(String[][]oppOff){
		offerts=oppOff;
	}
	public String[][] getOfferts(){
		return offerts;
	}

	//FINISH
	String [][]rank;
	public void setRank(String [][]rank){
		this.rank=rank;
	}
	public String[][] getRank(){
		return rank;
	}
}
