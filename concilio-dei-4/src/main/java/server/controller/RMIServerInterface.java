package server.controller;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import client.controller.RMIClientInterface;

/**
 * RMIClient uses this interface to call RMI_Server methods 
 */
public interface RMIServerInterface extends Remote {
	/*
	 * This Interface includes every server's method that the client can invoke
	 * using RMI connection 
	 */
	int player_connect(String nome, String password, RMIClientInterface rmi_clientin) throws IOException;
	long newGame(int min, int max,String name, int map) throws IOException;
	void newConnectionToClient(String name) throws IOException;
	public String[][][] listOfMaps()throws IOException;
	public long[][] listOfGames() throws IOException;
	int addPlayerToGame(long codeGame, String name) throws IOException;
	
	//I use the methods below when the game is started
	//Change councillor
	void changeCouncillor(long codeGame, int councillorChosen, int balcony)throws IOException;
	//Buy Permit Card
	void buyPermitCard(long codeGame, int []politicCardsUse, int whichBalcony, int permitCardVisible)throws IOException;
	//Build an Emporium by permit
	void buildEmporiumByPermit(long codeGame, int indexPermitCard, String townNameChosen)throws IOException;
	//Build an Emporium by King
	void buildEmporiumByKing(long codeGame, int[]politicalCardUsed, String nameTown, String route)throws IOException;
	//Engage an Helper
	void engangeHelper(long codeGame)throws IOException;
	//Change permit build card
	void changePermitCard(long codeGame, int region)throws IOException;
	//use an helper to change a councillor
	void changeCouncillorByHelper(long codeGame, int councillorChosen, int balcony)throws IOException;
	//do another main action
	void otherMainAction(long codeGame)throws IOException;
	//action for choosing the bonus in nobility road
	void selectOneBonusToken(long codeGame, String token)throws IOException;
	void selectTwoBonusToken(long codeGame, String token1, String token2)throws IOException;
	void selectBonusPermitCard(long codeGame, int bonusPermit)throws IOException;;
	void selectPermitCard(long codeGame, int permitCard)throws IOException;

	void passTurn(long codeGame)throws IOException;
	
	//method for the market
	void addOffert(long codeGame,String[]offert)throws IOException;
	void finishDoOffer(long codeGame)throws IOException;
	void buyOffert(long codeGame,int offert)throws IOException;
	void passMarketTurn(long codeGame)throws IOException;

}