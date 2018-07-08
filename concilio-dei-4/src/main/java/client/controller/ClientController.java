package client.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import client.model.EnumPage;
import client.model.EnumStatusGame;
import client.model.LocalStateGame;
import client.model.Page;
import client.model.SetUpPlayer;
import server.StartServer;
import server.controller.RMIServerInterface;
import client.model.ClientConstants;

/**
 * When the player is online this class manages each of his action 
 *
 */
public class ClientController implements Observer{

	/*
	 * This class manages the RMI connection mode in the CLI
	 */
	
	private Page modelPage;
	private SetUpPlayer player;
	private RMIClient rmiClient;
	private RMIServerInterface rmiServer;
	private ClientConnection server;
	
	public ClientController(Page p,SetUpPlayer sp) {
		modelPage=p;
		player=sp;
	}
	public void setConnection(ClientConnection c){
		server=c;
	}
	
	/**
	 * @param n is the value that the player inserts
	 */
	public void managerOption(String n){
		boolean valid_option=false;
		try{
			if((modelPage.getPage().equals(EnumPage.play_online) || modelPage.getPage().equals(EnumPage.name_not_valid))&& !valid_option)
			{//I check online the name that the player has inserted
				String[]split=n.split(" ");
				check_name(split[0], split[1]);
				valid_option=true;
			}
			if((modelPage.getPage().equals(EnumPage.menu_online) || modelPage.getPage().equals(EnumPage.update_menu_online)) && n.equals("1") && !valid_option)
			{//new game online and choose how many player do you want
				howManyPlayer();
				valid_option=true;
			}
			if(modelPage.getPage().equals(EnumPage.how_many_players)&& !valid_option)
			{//the player chooses the max and min players he wants in a game
				newGameOnline(n);
				valid_option=true;
			}
			if((modelPage.getPage().equals(EnumPage.menu_online) || modelPage.getPage().equals(EnumPage.update_menu_online)) && n.equals("2") && !valid_option)
			{//I show the list of the games that currently look for a player
				listOfLobby();
				valid_option=true;
			}
			if((modelPage.getPage().equals(EnumPage.menu_online) || modelPage.getPage().equals(EnumPage.update_menu_online)) && n.equals("3") && !valid_option)
			{//I show the list of the maps available for the game
				listOfMaps();
				valid_option=true;
			}
			if(modelPage.getPage().equals(EnumPage.join_game) && !valid_option)
			{
				joinToTheLobby(n);
				valid_option=true;
			}
			if(modelPage.getPage().equals(EnumPage.list_of_maps) && !valid_option)
			{
				modelPage.setPage(EnumPage.menu_online, "");
				valid_option=true;
			}
			if(modelPage.getPage().equals(EnumPage.your_turn) && !valid_option)
			{
				if(localStateGame.getMainActionRemain()>0)
				{
					switch(n){
					case "1":
						changeCouncillor();
						valid_option=true;
						break;
					case "2":
						buyPermitCard();
						valid_option=true;
						break;
					case "3":
						buildEmporiumByPermit();
						valid_option=true;
						break;
					case "4":
						buildEmporiumByKing();
						valid_option=true;
						break;
					}
				}
				if(localStateGame.getSecondaryAction()>0)
				{
					switch(n){
					case "5":
						engangeAnHelper();
						valid_option=true;
						break;
					case "6":
						changePermitCard();
						valid_option=true;
						break;  
					case "7":
						changeCouncillorByHelper();
						valid_option=true;
						break;
					case "8":
						otherMainAction();
						valid_option=true;
						break;
					}
				}
				switch(n){
				case "9":
					printGameStatus();
					valid_option=true;
					break;
				case "10":
					printStatusPlayer(localStateGame.getStatusPlayer(), localStateGame.getColorPlayer(), localStateGame.getPoliticalCards(), localStateGame.getColorPoliticalCards(), localStateGame.getPermitCardsTownsPlayer());
					actionAgain(localStateGame.getMainActionRemain(), localStateGame.getSecondaryAction());
					valid_option=true;
					break;
				case "11":
					passTurn();
					valid_option=true;
					break;
				case "12":
					localStateGame.setStatusGame(EnumStatusGame.legend);
					modelPage.setPage(EnumPage.game_status, "");
					actionAgain(localStateGame.getMainActionRemain(), localStateGame.getSecondaryAction());
					valid_option=true;
					break;
				}
			}
			if(modelPage.getPage().equals(EnumPage.game_status) && !valid_option)
			{
				//ACTION 1
				if(localStateGame.getStatusGame().equals(EnumStatusGame.balconyChose) && !valid_option)
				{//player chooses the balcony where he wants to substitute the councillor chosen before
					server.changeCouncillor(player.getCodeGame(), localStateGame.getCouncillorOutOfBalconyChosen(), Integer.parseInt(n));						
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.listCouncillorOutOfBalconies) && !valid_option)
				{//player chooses the council out of balcony that wants to substitute
					localStateGame.setCouncillorOutOfBalconyChosen(Integer.parseInt(n));
					localStateGame.setStatusGame(EnumStatusGame.balconyChose);
					modelPage.setPage(EnumPage.game_status, "");
					valid_option=true;
				}
				//ACTION 2
				if(localStateGame.getStatusGame().equals(EnumStatusGame.whichPermitCardGet) && !valid_option)
				{
					server.buyPermitCard(player.getCodeGame(), localStateGame.getPoliticalCardsUsedForCorruption(), localStateGame.getBalconyChosenForCorruption(),Integer.parseInt(n));					
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.politicalCardChosen) && !valid_option)
				{//player selects the card that wants to use in this way: 0-2-4-1
					String numberInserted[]=n.split("-");
					int[]politicCardsUse=new int[numberInserted.length];
					for(int i=0;i<numberInserted.length;i++)
						politicCardsUse[i]=Integer.parseInt(numberInserted[i]);
					localStateGame.setPoliticalCardsUsedForCorruption(politicCardsUse);
					localStateGame.setStatusGame(EnumStatusGame.whichPermitCardGet);
					modelPage.setPage(EnumPage.game_status, "");
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.balconyChoseForCorrupt) && !valid_option)
				{
					localStateGame.setBalconyChosenForCorruption(Integer.parseInt(n));
					localStateGame.setStatusGame(EnumStatusGame.politicalCardChosen);
					modelPage.setPage(EnumPage.game_status, "");
					valid_option=true;
				}
				//ACTION 3
				if(localStateGame.getStatusGame().equals(EnumStatusGame.townNameChosed) && !valid_option)
				{
					int indexPermitCard=localStateGame.getIndexPermitCard();
					server.buildEmporiumByPermit(player.getCodeGame(), indexPermitCard, n);						
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.indexPermitCard) && !valid_option)
				{
					localStateGame.setIndexPermitCard(Integer.parseInt(n));
					localStateGame.setStatusGame(EnumStatusGame.townNameChosed);
					modelPage.setPage(EnumPage.game_status, "");
					valid_option=true;
				}
				//ACTION 4
				if(localStateGame.getStatusGame().equals(EnumStatusGame.routeToFollow) && !valid_option)
				{
					int[]politicCardUseToKing=localStateGame.getPoliticalCardUseToKing();
					String townWhereToBuild=localStateGame.getTownWhereToBuild();
					server.buildEmporiumByKing(player.getCodeGame(), politicCardUseToKing, townWhereToBuild, n);						
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.townChosenToKing) && !valid_option)
				{
					localStateGame.setTownWhereToBuild(n);
					localStateGame.setStatusGame(EnumStatusGame.routeToFollow);
					modelPage.setPage(EnumPage.game_status, "");
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.politicCardsUseToKing) && !valid_option)
				{
					String numberInserted[]=n.split("-");
					int[]politicCardsUseToKing=new int[numberInserted.length];
					for(int i=0;i<numberInserted.length;i++)
						politicCardsUseToKing[i]=Integer.parseInt(numberInserted[i]);
					localStateGame.setPoliticalCardUseToKing(politicCardsUseToKing);
					localStateGame.setStatusGame(EnumStatusGame.townChosenToKing);
					modelPage.setPage(EnumPage.game_status, "");
					valid_option=true;
				}
				//ACTION 6
				if(localStateGame.getStatusGame().equals(EnumStatusGame.regionChose) && !valid_option)
				{
					server.changePermitCard(player.getCodeGame(), Integer.parseInt(n));						
					valid_option=true;
				}
				//ACTION 7
				if(localStateGame.getStatusGame().equals(EnumStatusGame.balconyChoseByHelper) && !valid_option)
				{
					server.changeCouncillorByHelper(player.getCodeGame(), localStateGame.getCouncillorOutOfBalconyChosen(), Integer.parseInt(n));					
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.listCouncillorFreeByHelper) && !valid_option)
				{
					localStateGame.setCouncillorOutOfBalconyChosen(Integer.parseInt(n));
					localStateGame.setStatusGame(EnumStatusGame.balconyChoseByHelper);
					modelPage.setPage(EnumPage.game_status, "");
					valid_option=true;
				}
				//ACTION FOR CHOOSING THE BONUS IN NOBILITY ROAD
				if(localStateGame.getStatusGame().equals(EnumStatusGame.select1BonusToken) && !valid_option)
				{
					server.selectOneBonusToken(player.getCodeGame(), n);
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.select2BonusToken) && !valid_option)
				{
					String[] townsChosen=n.split("-");
					server.selectTwoBonusToken(player.getCodeGame(), townsChosen[0], townsChosen[0]);					
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.select1BonusPermitCard) && !valid_option)
				{
					server.selectBonusPermitCard(player.getCodeGame(), Integer.parseInt(n));					
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.select1PermitCard) && !valid_option)
				{
						server.selectPermitCard(player.getCodeGame(), Integer.parseInt(n));				
						valid_option=true;
				}
				//ACTION FOR THE MARKET
				if((localStateGame.getStatusGame().equals(EnumStatusGame.turnDoneOffert) || localStateGame.getStatusGame().equals(EnumStatusGame.marketStart)) && !valid_option)
				{
					turnDoneOffert(n);
					valid_option=true;
				}
				if(localStateGame.getStatusGame().equals(EnumStatusGame.yourTurnToBuy) && !valid_option)
				{
					turnBuy(n);
					
					valid_option=true;
				}
			}
			if(modelPage.getPage().equals(EnumPage.finish_game) && !valid_option)
			{
				modelPage.setPage(EnumPage.menu_online, "");
				valid_option=true;
			}
		}
		catch(IOException e){
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
			valid_option=false;
		}
		catch(Exception e)
		{//the try-catch inside capture the
			StartServer.logger.log(Level.WARNING, "Inserted wrong date", e);
			valid_option=false;
		}
		if(!valid_option)
		{//if the player uses a not valid key I print again the page where he was
			modelPage.setPage(modelPage.getPage(),"");
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		String option=arg.toString();
		managerOption(option);		
	}
	
	private void check_name(String name, String password) {
		int n=-1;
		try {
			rmiClient=new RMIClient();
			rmiClient.setController(this);
			rmiServer=(RMIServerInterface)Naming.lookup("rmi://localhost/SERVER");
			ClientConnectionRMI rmi=new ClientConnectionRMI(rmiServer);
			setConnection(rmi);
			n=server.player_connect(name,password, rmiClient);
		}catch (NotBoundException e){
			StartServer.logger.log(Level.SEVERE, "Server not found", e);
		}catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
		if(n==-1)
		{
			modelPage.setPage(EnumPage.name_not_valid,"");
		}
		if(n>=0)
		{//The name is valid
			//the next commands need to communicate with the server
			try {
				player.setName(name);
				server.newConnectionToClient(name);
				modelPage.setPage(EnumPage.menu_online,n+"");				
			} catch (IOException e) {
				StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
			}
		}
		if(n<-1)
		{
			try {
				player.setName(name);
				setupTimer();
				localStateGame=new LocalStateGame(player);
				modelPage.setLocalStateGame(localStateGame);
				server.newConnectionToClient(name);
			} catch (IOException e) {
				StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
			}
		}
	}
	/**
	 * @param code server calls this method to give the codeGame to the client
	 */
	public void giveGameCode(long code){
		player.setCodeGame(code);
	}
	
	/**
	 * @param n how many player are online, this method is called every time that a new player connects to the server
	 */
	public void updatePlayerOnline(int n){
		if(modelPage.getPage().equals(EnumPage.menu_online) || modelPage.getPage().equals(EnumPage.update_menu_online))
			modelPage.setPage(EnumPage.update_menu_online,n+"");
	}
	private void howManyPlayer() {
		modelPage.setPage(EnumPage.how_many_players, "");
	}
	
	private void newGameOnline(String numberPlayer){
		String max_mins[]=numberPlayer.split("-");
		int max_min[]=new int[2];
		max_min[0]=Integer.parseInt(max_mins[0]);
		max_min[1]=Integer.parseInt(max_mins[1]);
		int map=Integer.parseInt(max_mins[2]);
		try {
			player.setCodeGame(0);
			long codeGame=server.newGame(max_min[0],max_min[1], player.getName(), map);
			if(codeGame!=0)
				player.setCodeGame(codeGame);
			while(player.getCodeGame()==0)
			{	
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					StartServer.logger.log(Level.WARNING, ClientConstants.PROBLEM_SLEEP, e);
					Thread.currentThread().interrupt();
				}
			}
			modelPage.setPage(EnumPage.lobby, "1");
		} catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
	}
	
	private void listOfLobby()
	{//This method shows an open list of games
		try {
			long l[][]=server.listOfGames();
			if(l!=null)
				modelPage.setPage(EnumPage.join_game, l);
		} catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
	}
	
	private void joinToTheLobby(String s)
	{
		if(s.equals("M"))
		{
			modelPage.setPage(EnumPage.menu_online,"");
		}
		else
		{
			int n=Integer.parseInt(s);
			long[][]listOfGames=modelPage.getListOfGames();
			if(n<listOfGames.length)
			{
				long codeGame=listOfGames[n][2];
				player.setCodeGame(codeGame);
				try {
					int cont=server.addPlayerToGame(codeGame, player.getName());
					if(cont!=-1)
						updatePlayerInLobby(cont);
				} catch (IOException e) {
					StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
				}
			}
			else
				listOfLobby();
		}
	}
	/**
	 * @param n how many player are in the lobby, this method is called every time that a new player connects to the lobby
	 */
	public void updatePlayerInLobby(int n){
		modelPage.setPage(EnumPage.lobby, n+"");
	}
	
	private void listOfMaps() {
		String[][][] maps;
		try {
			maps = server.listOfMaps();
			if(maps!=null)
				modelPage.setPage(EnumPage.list_of_maps,maps);
		} catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
	}
	
	/*
	* Action that player can do
	*/
	//ACTION 1
	private void changeCouncillor(){
		localStateGame.setStatusGame(EnumStatusGame.listCouncillorOutOfBalconies);
		modelPage.setPage(EnumPage.game_status, "");
	}
	//ACTION 2
	private void buyPermitCard(){
		localStateGame.setStatusGame(EnumStatusGame.balconyChoseForCorrupt);
		modelPage.setPage(EnumPage.game_status, "");
	}
	//ACTION 3
	private void buildEmporiumByPermit(){
		localStateGame.setStatusGame(EnumStatusGame.indexPermitCard);
		modelPage.setPage(EnumPage.game_status, "");
	}
	//ACTION 4
	private void buildEmporiumByKing(){
		localStateGame.setStatusGame(EnumStatusGame.politicCardsUseToKing);
		modelPage.setPage(EnumPage.game_status, "");
	}
	//ACTION 5
	private void engangeAnHelper(){
		try {
			server.engangeHelper(player.getCodeGame());
		} catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
	}
	//ACTION 6
	private void changePermitCard(){
		localStateGame.setStatusGame(EnumStatusGame.regionChose);
		modelPage.setPage(EnumPage.game_status, "");
	}
	//ACTION 7
	private void changeCouncillorByHelper(){
		localStateGame.setStatusGame(EnumStatusGame.listCouncillorFreeByHelper);
		modelPage.setPage(EnumPage.game_status, "");
	}
	//ACTION 8
	private void otherMainAction(){
		try {
			server.otherMainAction(player.getCodeGame());
		} catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
	}
	//ACTION FOR CHOOSING THE BONUS IN NOBILITY ROAD (this methods are invoked to the server)
	/**
	 * @see client.controller.RMIClientInterface#select1BonusToken()
	 */
	public void select1BonusToken(){
		localStateGame.setStatusGame(EnumStatusGame.select1BonusToken);
		modelPage.setPage(EnumPage.game_status, "");
	}
	/**
	 * @see client.controller.RMIClientInterface#select2BonusToken()
	 */
	public void select2BonusToken(){
		localStateGame.setStatusGame(EnumStatusGame.select2BonusToken);
		modelPage.setPage(EnumPage.game_status, "");
	}
	/**
	 * @see client.controller.RMIClientInterface#select1BonusPermit()
	 */
	public void select1BonusPermit(){
		localStateGame.setStatusGame(EnumStatusGame.select1BonusPermitCard);
		modelPage.setPage(EnumPage.game_status, "");
	}
	/**
	 * @see client.controller.RMIClientInterface#selectPermitCard()
	 */
	public void selectPermitCard(){
		localStateGame.setStatusGame(EnumStatusGame.select1PermitCard);
		modelPage.setPage(EnumPage.game_status, "");
	}
	//ACTION 9
	private void printGameStatus() {
		printMap(localStateGame.getMap(),localStateGame.getRegionName(),localStateGame.getWhereIsKing());
		printPermitCards(localStateGame.getPermitCardsTown());
		printKingBoard(localStateGame.getCouncilBalconyName(), localStateGame.getCouncilBalconyColor(), localStateGame.getKingReward(), localStateGame.getBonusReward(), localStateGame.getCouncillorNameOutOfBalcony(),localStateGame.getCouncillorColorOutOfBalcony());
		printNobilityRoad(localStateGame.getBonusNobilityRoad());
		printOpponentStatus(localStateGame.getStatusOpponent(), localStateGame.getColorOpponent());
		actionAgain(localStateGame.getMainActionRemain(), localStateGame.getSecondaryAction());
	}
	//ACTION 11
	private void passTurn() {
		try {
			timerTurn.cancel();
			checkTurn=true;
			server.passTurn(player.getCodeGame());
		} catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
	}
	
	/*
	* Below there are some methods that the server can invoke to the client
	*/
	
	//Below there are the methods I use when the game starts
	private LocalStateGame localStateGame;
	/**
	 * Server call this method to start the game
	 */
	public void startGame(){//I say to client that the game is starting
		setupTimer();
		localStateGame=new LocalStateGame(player);
		localStateGame.setStatusGame(EnumStatusGame.gameStart);
		modelPage.setLocalStateGame(localStateGame);
		modelPage.setPage(EnumPage.game_status, "");
	}
	/**
	 * @see client.controller.RMIClientInterface#statusMap(String[][] map, String[]nameRegion,String whereIsKing)
	 */
	public void printMap(String[][] map,String[]nameRegion,String king){
		localStateGame.setStatusGame(EnumStatusGame.map);
		localStateGame.setMap(map);
		localStateGame.setRegionName(nameRegion);
		localStateGame.setWhereIsKing(king);
		modelPage.setPage(EnumPage.game_status, "");
	}
	/**
	 * @see client.controller.RMIClientInterface#statusVisiblePermitCards(String[][] permitCardTowns)
	 */
	public void printPermitCards(String[][] permitCardTowns){
		localStateGame.setStatusGame(EnumStatusGame.permitCards);
		localStateGame.setPermitCardVisible(permitCardTowns);
		modelPage.setPage(EnumPage.game_status, "");
	}
	/**
	 * @see client.controller.RMI_ClientInterface#statusKingBoard(String councilBalconyName[][],Color councilBalconyColor[][],int[]kingReward,String[][]bonusReward,String[]councillorNameOutOfBalcony,Color[]councillorColorOutOfBalcony)
	 */
	public void printKingBoard(String[][] councilBalconyName, Color[][] councilBalconyColor, int[] kingReward,String[][] bonusReward,String[]councillorNameOutOfBalcony,Color[]councillorColorOutOfBalcony){
		localStateGame.setStatusGame(EnumStatusGame.kingBoard);
		localStateGame.setCouncilBalconyName(councilBalconyName);
		localStateGame.setCouncilBalconyColor(councilBalconyColor);
		localStateGame.setKingReward(kingReward);
		localStateGame.setBonusReward(bonusReward);
		localStateGame.setCouncillorNameOutOfBalcony(councillorNameOutOfBalcony);
		localStateGame.setCouncillorColorOutOfBalcony(councillorColorOutOfBalcony);
		modelPage.setPage(EnumPage.game_status, "");
	}
	/**
	 * @see client.controller.RMIClientInterface#statusNobilityRoad(int[][]bonusNobilityRoad)
	 */
	public void printNobilityRoad(int[][] bonusNobilityRoad){
		localStateGame.setStatusGame(EnumStatusGame.nobilityRoad);
		localStateGame.setBonusNobilityRoad(bonusNobilityRoad);
		modelPage.setPage(EnumPage.game_status, "");
	}
	/**
	 * @see client.controller.RMIClientInterface#statusOpponents(String[][]statusOpponent, Color[]colorOpponent)
	 */
	public void printOpponentStatus(String[][]statusOpponent, Color[] colorOpponent){
		localStateGame.setStatusGame(EnumStatusGame.opponentStatus);
		localStateGame.setStatusOpponent(statusOpponent);
		localStateGame.setColorOpponent(colorOpponent);
		modelPage.setPage(EnumPage.game_status, "");
		
	}
	/**
	 * @see client.controller.RMIClientInterface#statusPlayer(String[]status, Color c, String[]politicalCards,Color[]politicalCardsColors, String[][] permitCardsTowns)
	 */
	public void printStatusPlayer(String[] status, Color c, String[] politicalCards, Color[] politicalCardsColors,String[][] permitCardsTowns){
		localStateGame.setStatusGame(EnumStatusGame.playerStatus);
		localStateGame.setStatusPlayer(status);
		localStateGame.setColorPlayer(c);
		localStateGame.setPoliticalCards(politicalCards);
		localStateGame.setColorPoliticalCards(politicalCardsColors);
		localStateGame.setPermitCardsTownsPlayer(permitCardsTowns);
		modelPage.setPage(EnumPage.game_status, "");
	}
	

	/**
	 * @see client.controller.RMIClientInterface#statusTurn(String turn, int timeTurn)
	 */
	public void printStatusTurn(String turn, int timeTurn){
		if(timerBuy!=null)
		{
			timerBuy.cancel();
			checkBuy=true;
		}
		
		if(turn.equals(player.getName()))
		{//it's your turn!
			localStateGame.setMainActionRemain(ClientConstants.mainActionCanDo);
			localStateGame.setSecondaryAction(ClientConstants.secondaryActionCanDo);
			if(checkTurn)
			{//I start the timer just 1 time, when the turn of player starts
				checkTurn=false;
				timerTurn = new TimerTurn(timeTurn);
				// running timer task as daemon thread
				timer.schedule(timerTurn, 0, ClientConstants.timeRepeat);
				
			}
			modelPage.setPage(EnumPage.your_turn,"");
		}
		else
		{//it's opponent's turn!
			localStateGame.setStatusGame(EnumStatusGame.opponentTurn);
			modelPage.setPage(EnumPage.game_status,turn);
		}
	}
	/**
	 * @see client.controller.RMIClientInterface#updateActionPlayer(int mainAction, int secondaryAction)
	 */
	public void actionAgain(int mainAction,int secondaryAction)
	{//Server calls this method after an action if the player can do other ones
		//thing
		localStateGame.setMainActionRemain(mainAction);
		localStateGame.setSecondaryAction(secondaryAction);
		modelPage.setPage(EnumPage.your_turn,"");
	}
	/**
	 * @see client.controller.RMIClientInterface#resurceNotEnoght()
	 */
	public void resourceNotEnoght(){
		localStateGame.setStatusGame(EnumStatusGame.resourceNotEnought);
		modelPage.setPage(EnumPage.game_status,"");
		modelPage.setPage(EnumPage.your_turn,"");
	}
	/**
	 * @see client.controller.RMIClientInterface#ErrorIsertedDate()
	 */
	public void ErrorIsertedDate(){
		localStateGame.setStatusGame(EnumStatusGame.errorInsertedDate);
		modelPage.setPage(EnumPage.game_status,"");
		modelPage.setPage(EnumPage.your_turn,"");
	}
	
	//MARKET
	private void turnDoneOffert(String n){
		try{
			if(!n.equals("F"))
			{
				String[]tmp=n.split("-");
				String[]offert=new String[tmp.length+1];
				offert[0]=player.getName();
				for(int i=0;i<tmp.length;i++)
					offert[i+1]=tmp[i];
				server.addOffert(player.getCodeGame(), offert);
			}
			else
			{
				timerDoOffert.cancel();
				server.finishDoOffer(player.getCodeGame());
				localStateGame.setStatusGame(EnumStatusGame.waitTurnDoneOffertFinish);
				modelPage.setPage(EnumPage.game_status,"");
			}
		}catch(IOException e)
		{
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
	}
	
	//method that server can invoke for the market
	/**
	 * @see client.controller.RMIClientInterface#startMarket(int timeDoOffert)
	 */
	public void startMarket(int timeDoOffert){//server calls this method to notify the market stage is starting
		if(timerTurn!=null)
		{
			timerTurn.cancel();
			checkTurn=true;
		}
		localStateGame.setStatusGame(EnumStatusGame.playerStatus);
		modelPage.setPage(EnumPage.game_status,"");
		//set up turn
		timerDoOffert = new TimerDoOffert(timeDoOffert);
		// running timer task as daemon thread
		timer.schedule(timerDoOffert, 0, ClientConstants.timeRepeat);
		localStateGame.setStatusGame(EnumStatusGame.marketStart);
		modelPage.setPage(EnumPage.game_status,"");
		
	}
	/**
	 * @see client.controller.RMIClientInterface#doAnotherOffert
	 */
	public void doAnotherOffert(){//server calls this method to notify that the player can do other offers
		localStateGame.setStatusGame(EnumStatusGame.playerStatus);
		modelPage.setPage(EnumPage.game_status,"");
		localStateGame.setStatusGame(EnumStatusGame.turnDoneOffert);
		modelPage.setPage(EnumPage.game_status,"");
	}
	/**
	 * @see client.controller.RMIClientInterface#resourcesNotEnoughtForOffert()
	 */
	public void resourcesNotEnoughtForOffert(){//server calls this method to say to the player that he doesn't have the resources he wants send
		localStateGame.setStatusGame(EnumStatusGame.resourcesNotEnoughtToOffert);
		modelPage.setPage(EnumPage.game_status,"");
		localStateGame.setStatusGame(EnumStatusGame.playerStatus);
		modelPage.setPage(EnumPage.game_status,"");
		localStateGame.setStatusGame(EnumStatusGame.turnDoneOffert);
		modelPage.setPage(EnumPage.game_status,"");
	}
	/**
	 * @see client.controller.RMIClientInterface#errorInsertedOffert()
	 */
	public void errorInsertedOffert(){//server calls this method to say to the player that he inserted the offer in a not valid way
		localStateGame.setStatusGame(EnumStatusGame.errorInsertedDate);
		modelPage.setPage(EnumPage.game_status,"");
		localStateGame.setStatusGame(EnumStatusGame.playerStatus);
		modelPage.setPage(EnumPage.game_status,"");
		localStateGame.setStatusGame(EnumStatusGame.turnDoneOffert);
		modelPage.setPage(EnumPage.game_status,"");
	}
	/**
	 * @see client.controller.RMIClientInterface#receiveOtherOfferts(String[][] offerts, int timeBuy)
	 */
	public void receiveOtherOfferts(String[][] offerts, int timeBuy){//server send the offers to the client with this methods
		if(checkBuy)
		{
			if(timerDoOffert!=null)
				timerDoOffert.cancel();
			checkBuy=false;
			//set up turn
			timerBuy = new TimerBuy(timeBuy);
			// running timer task as daemon thread
			timer.schedule(timerBuy, 0, ClientConstants.timeRepeat);
		}
		localStateGame.setOfferts(offerts);
		localStateGame.setStatusGame(EnumStatusGame.yourTurnToBuy);
		modelPage.setPage(EnumPage.game_status,"");
	}
	private void turnBuy(String n) 
	{
		try
		{
			if(!n.equals("F"))
				server.buyOffert(player.getCodeGame(), Integer.parseInt(n));
			else
			{
				timerBuy.cancel();
				checkBuy=true;
				server.passMarketTurn(player.getCodeGame());
			}
		}catch(IOException e)
		{
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
	}
	/**
	 * @see client.controller.RMIClientInterface#opponentTurnToBuy(String opponentName)
	 */
	public void opponentTurnToBuy(String opponentName){
		localStateGame.setStatusGame(EnumStatusGame.opponentTurnToBuy);
		modelPage.setPage(EnumPage.game_status,opponentName);
	}
	/**
	 * @see client.controller.RMIClientInterface#resourcesNotEnoughtToBuyOffert()
	 */
	public void resourcesNotEnoughtToBuyOffert(){//server calls this method to say to the player that he doesn't have the resources he wants send
		localStateGame.setStatusGame(EnumStatusGame.resourcesNotEnoughtToBuy);
		modelPage.setPage(EnumPage.game_status,"");
		localStateGame.setStatusGame(EnumStatusGame.playerStatus);
		modelPage.setPage(EnumPage.game_status,"");
		localStateGame.setStatusGame(EnumStatusGame.yourTurnToBuy);
		modelPage.setPage(EnumPage.game_status,"");
	}
	/**
	 * @see client.controller.RMIClientInterface#errorInsertedBought()
	 */
	public void errorInsertedBought(){//server calls this method to say to the player that he inserted offer in a not valid way
		localStateGame.setStatusGame(EnumStatusGame.errorInsertedDate);
		modelPage.setPage(EnumPage.game_status,"");
		localStateGame.setStatusGame(EnumStatusGame.playerStatus);
		modelPage.setPage(EnumPage.game_status,"");
		localStateGame.setStatusGame(EnumStatusGame.yourTurnToBuy);
		modelPage.setPage(EnumPage.game_status,"");
	}
	
	//FINISH GAME
	/**
	 * @see client.controller.RMIClientInterface#finishGame(String[][]rank)
	 */
	public void finishGame(String[][]rank){
		modelPage.setPage(EnumPage.finish_game,rank);
	}

	//TIMER CLASS
	private Timer timer;
	public void setupTimer(){
		checkTurn=true;
		checkBuy=true;
		timer=new Timer();
	}
	//I create a timer that counts the time that the player has to do his action
	private boolean checkTurn;
	private TimerTask timerTurn;
	private class TimerTurn extends TimerTask {
		private int timer;
		public TimerTurn(int t) {
			timer=t;
		}
		@Override
		public void run() {
				if(!player.getGraphicChosen().equals("CLI"))
				{//player plays with GUI
					modelPage.updateTimer(timer);
				}
				timer=timer-1;
				if(timer==0){//end turn
					passTurn();	
				}
		}
	}
	
	//private boolean checkOffert;
	private TimerTask timerDoOffert;
	private class TimerDoOffert extends TimerTask {
		private int timer;
		public TimerDoOffert(int timeDoOffert) {
			this.timer=timeDoOffert;
		}
		@Override
		public void run() {
			if(!player.getGraphicChosen().equals("CLI"))
			{//player plays with GUI
				modelPage.updateTimer(timer);
			}
			timer=timer-1;
			if(timer==0){//end turn		
				turnDoneOffert("F");
			}
		}
	}
	private boolean checkBuy;
	private TimerTask timerBuy;
	private class TimerBuy extends TimerTask{
		private int timer;
		public TimerBuy(int timeBuy) {
			this.timer=timeBuy;
		}
		@Override
		public void run() {
			if(!player.getGraphicChosen().equals("CLI"))
			{//player plays with GUI
				modelPage.updateTimer(timer);
			}
			timer=timer-1;
			if(timer==0){//end turn
				turnBuy("F");
			}
		}
	}
}
