package client.controller;

import java.awt.Color;
import java.io.IOException;
import java.rmi.Remote;

/**
 * Server can use this interface to communicate to RMIClient
 *
 */
public interface RMIClientInterface extends Remote{
	/*
	 * This Interface includes every client's method that the server can invoke
	 * using RMI connection 
	 */
	/**
	 * @param n number of player online -1
	 */
	void updatePlayerOnline(int n) throws IOException;//server invoke this method when new players connect online
	
	//server invokes this method to say to the player to wait for a new player entering
	//in their lobby
	/**
	 * @param n number of player in the lobby -1
	 */
	void updatePlayerInLobby(int n)throws IOException;

	//this method prints the general status in the game
	/** 
	 * advice that the game is starting
	 */
	void startGame()throws IOException;
	
	/**
	 * @param map [connected towns, money, helper, politicalCard, nobility, scorePoint, who has emporium in the town, name of color town]
	 * @param nameRegion list of name region
	 * @param whereIsKing town's name where is the king
	 
	 */
	void statusMap(String[][] map, String[]nameRegion,String whereIsKing) throws IOException;
	/**
	 * @param permitCardTowns [town/s where the card permits to build, money, helper, politicalcard, nobility, scorepoint, mainaction]
	 *
	 */
	void statusVisiblePermitCards(String[][] permitCardTowns) throws IOException;
	/**
	 * @param councilBalconyName in every row there are the names of the color that have the councillors in any balcony (row->balcony, column->councillor)
	 * @param councilBalconyColor like councilBalconyName but in this array the color is in the Color type, not string
	 * @param kingReward list of king reward available
	 * @param bonusReward list of bonus available and in which town you must build to take them
	 * @param councillorNameOutOfBalcony list of the councillor out of balcony, the name of their color
	 * @param councillorColorOutOfBalcony like councillorNameOutOfBalcony but this array is Color type
	 
	 */
	void statusKingBoard(String councilBalconyName[][],Color councilBalconyColor[][],int[]kingReward,String[][]bonusReward,String[]councillorNameOutOfBalcony,Color[]councillorColorOutOfBalcony) throws IOException;
	/**
	 * @param bonusNobilityRoad every row is a step of nobility road and in the columns there are the bonus for every step
	 * [money, helper, politicalcard, nobility, scorepoint, mainaction, permitcardbonus, permitcard, bonustown]
	 */
	void statusNobilityRoad(int[][]bonusNobilityRoad)throws IOException;
	/**
	 * @param statusOpponent every row is an opponent and in the columns there are the different resources that have the opponent
	 * [money, helper, scorePoint, nobility, how many permit cards has, how many politic cards has, how many emporium not used has]
	 * @param color Color of the opponent
	 
	 */
	void statusOpponents(String[][]statusOpponent, Color[]color)throws IOException;
	/**
	 * @param status of some resource of the player [money, helper, scorePoint, nobility]
	 * @param c Color of the player
	 * @param politicalCards list of the political card that the player has (the name of the card color)
	 * @param politicalCardsColors like politicalCards but the array is type Color
	 * @param permitCardsTowns
	 */
	void statusPlayer(String[]status, Color c, String[]politicalCards,Color[]politicalCardsColors, String[][] permitCardsTowns)throws IOException;
	/**
	 * @param turn name of the player that plays the turn
	 * @param timeTurn how much time has the player in his turn
	 
	 */
	void statusTurn(String turn, int timeTurn)throws IOException;

	/**
	 * @param mainAction how many main actions the player can do
	 * @param secondaryAction how many secondary actions the player can do
	 */
	void updateActionPlayer(int mainAction, int secondaryAction)throws IOException;
	
	//this method prints the status of particular situations
	/**
	 * Ask to the player which bonus token he wants to take again (just 1)
	 */
	void select1BonusToken() throws IOException;
	/**
	 * Ask to the player which bonus token he wants to take again (just 2)
	 */
	void select2BonusToken() throws IOException;
	/**
	 * Ask to the player which bonus of permit card that he wants to take again
	 */
	void select1BonusPermit()throws IOException;
	/**
	 * Ask to the player which permit card he wants to pick up
	 */
	public void selectPermitCard()throws IOException;
		
	/**
	 * Warn the client that he doesn't have enough resources to do the actions
	 */
	public void resurceNotEnoght()throws IOException;
	/**
	 * Warn the client that he did an error during the turn
	 */
	public void errorInsertedDate()throws IOException;
	
	//method for the market
	/**
	 * @param timeDoOffert how much time the player has to make an offer
	 *
	 */
	public void startMarket(int timeDoOffert)throws IOException;
	/**
	 * 
	 */
	public void doAnotherOffert()throws IOException;
	/**
	 * Warn the client that he doesn't have enough resources to make an offer
	 */
	public void resourcesNotEnoughtForOffert()throws IOException;
	/**
	 * Warn the client that he did an error during offer's turn
	 */
	public void errorInsertedOffert()throws IOException;
	/**
	 * @param offerts every row is an offer [name of opponent, firstNumber, secondNumber, thirdNumber]
	 * 			firstNumber: 0-permit card, 1-political card, 2-helper
				secondNumber: which permit card/political card or how much helper you want to sell
				thirdNumber: how much money do you want for your offer
	 * @param timeBuy how much time the player has to buy something
	 *
	 */
	public void receiveOtherOfferts(String[][] offerts, int timeBuy)throws IOException;
	/**
	 * @param opponentName name of the opponent that now is buying
	 *
	 */
	public void opponentTurnToBuy(String opponentName)throws IOException;
	/**
	 * Warn the client that he doesn't have enough resources to buy the offer
	 */
	public void resourcesNotEnoughtToBuyOffert()throws IOException;
	/**
	 * Warn the client that he did an error during buying an offer
	 */
	public void errorInsertedBought()throws IOException;

	//finish game
	/**
	 * @param rank server gives to the players with their final Score Point
	 
	 */
	public void finishGame(String[][]rank)throws IOException;
	
	//reconnect
	/**
	 * @param code server gives the codeGame to the client
	 */
	public void giveCodeGame(long code)throws IOException;
	
	//methods for the connection
	/**
	 * @return client says to server he is connected
	 */
	public boolean heartClient()throws IOException;
}