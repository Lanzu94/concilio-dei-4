package server.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import client.controller.RMIClientInterface;
import server.model.Player;
import server.model.SocketProtocol;
import server.model.StatusServer;
import server.StartServer;
import server.controller.RMIServerInterface;
import server.model.EnumStatePlayer;
import server.model.Game;
import server.model.GameConstants;


/**
 * This class manages player's connection with RMI mode and then replies to the different things that the client can do
 * This class can send messages to RMI and Socket Client without difference using "connection" of player 
 * (Connection is implemented in different way according to the connection type that the player has chosen)
 */
public class RMIControllerServer extends UnicastRemoteObject implements RMIServerInterface{
	/*
	 * This class is the Server for RMI mode. 
	 * Receive the message from the client, modify the model in the server and send
	 * the new things to the client that prints the result
	 */
	
	
	private StatusServer statusServer;

	/**
	 * @param status status of the server, player and game present
	 */
	public RMIControllerServer(StatusServer status) throws IOException {
		//nothing
		statusServer=status;
		timerTurn=new ArrayList <>();
		timerDoOffert=new ArrayList <>();;
		timerBuy=new ArrayList <>();
		timerGameStart=new ArrayList <> ();
		timer=new Timer();
	}
	/*
	 * When I connect again I need to check in which phase of the game I am
	 */
	@Override
	public int player_connect(String name, String password, RMIClientInterface rmi_clientin) throws IOException {
		ConnectionRMI connectionRMI=new ConnectionRMI(rmi_clientin);
		//I check if the name exist, if it doesn't exist I add the player to the online players
		for(int i=0;i<statusServer.sizePlayers();i++) 
			if(name.equals(statusServer.getPlayer(i).getName()))
			{
				checkIfTheClientIsOnline(statusServer.getPlayer(i));
				if(statusServer.getPlayer(i).getPassword().equals(password))
				{
					if(statusServer.getPlayer(i).getStatePlayer().equals(EnumStatePlayer.disconnected))
					{
						statusServer.getPlayer(i).setStatusPlayer(EnumStatePlayer.online);
						statusServer.getPlayer(i).setConnection(connectionRMI);
						for(int j=0;j<statusServer.sizePlayers();j++)
						{
							checkIfTheClientIsOnline(statusServer.getPlayer(j));
						}
						int onlinePlayers=statusServer.onlinePlayers();
						return onlinePlayers;
					}
					if(statusServer.getPlayer(i).getStatePlayer().equals(EnumStatePlayer.lobbyDisconnected))
					{
						statusServer.getPlayer(i).setStatusPlayer(EnumStatePlayer.lobby);
						statusServer.getPlayer(i).setConnection(connectionRMI);
						return -2;
					}
					if(statusServer.getPlayer(i).getStatePlayer().equals(EnumStatePlayer.inGameDisconnected))
					{
						statusServer.getPlayer(i).setStatusPlayer(EnumStatePlayer.inGame);
						statusServer.getPlayer(i).setConnection(connectionRMI);
						return -2;
					}
					return -1;
				}
				else
					return -1;
			}
		Random rand = new Random();
		Player player=new Player(name, password, new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()));
		statusServer.addPlayer(player);
		//Now I create the connection to permit to the server to send the messages to client
		player.setConnection(connectionRMI);
		for(int i=0;i<statusServer.sizePlayers();i++)
		{
			checkIfTheClientIsOnline(statusServer.getPlayer(i));
		}
		int onlinePlayers=statusServer.onlinePlayers();
		return onlinePlayers;
	}
	
	@Override
	public void newConnectionToClient(String name) throws IOException
	{
		Player player=findPlayer(name);
		//Check how many player are online
		for(int i=0;i<statusServer.sizePlayers();i++)
		{
			checkIfTheClientIsOnline(statusServer.getPlayer(i));
		}
		int onlinePlayers=statusServer.onlinePlayers();
		//Now the server updates all clients that there is a new player online
		for(int i=0;i<statusServer.sizePlayers()-1;i++)
		{//try catch is inside so if a player is disconnected the error does not stop the for and other players receive the message
			try {
				if(statusServer.getPlayer(i).getStatePlayer().equals(EnumStatePlayer.online))
					statusServer.getPlayer(i).getConnection().updatePlayerOnline(onlinePlayers);
			}  catch (IOException e) {
				StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
				checkIfTheClientIsOnline(statusServer.getPlayer(i));
			}
		} 
		if(player.getStatePlayer().equals(EnumStatePlayer.lobby))
		{
			Game game=findGameByPlayer(player);
			player.getConnection().updatePlayerInLobby(game.numberOfPlayers());
		}
		if(player.getStatePlayer().equals(EnumStatePlayer.inGame))
		{
			enterAgainInTheGame(player);
		}
	}
 
	
	@Override
	public long newGame(int min, int max, String name, int map) throws IOException
	{
		Game g=new Game(min,max);
		g.setMap(map);
		for(int i=0;i<statusServer.sizePlayers();i++) 
			if(name.equals(statusServer.getPlayer(i).getName())) 
			{
				statusServer.getPlayer(i).setStatusPlayer(EnumStatePlayer.lobby);
				g.addPlayer(statusServer.getPlayer(i));
				break;
			}
		statusServer.addGame(g);
		return g.getCode();
	}
	
	@Override
	public long[][] listOfGames() {
		long[][] dates=new long[statusServer.sizeGames()][5];
		for(int i=0;i<statusServer.sizeGames();i++)
		{
			if(statusServer.getGame(i).isStarted())
				dates[i][4]=0;
			else 
				dates[i][4]=1;
			dates[i][0]=statusServer.getGame(i).numberOfPlayers();
			dates[i][1]=statusServer.getGame(i).maxNumberOfPlayers();
			dates[i][2]=statusServer.getGame(i).getCode();
			dates[i][3]=statusServer.getGame(i).getMap();
		}
		return dates;
	}
	
	@Override
	public String[][][] listOfMaps() {
		ListOfMaps l=new ListOfMaps();
		return l.listOfMaps();
	}
	
	@Override
	public int addPlayerToGame(long codeGame, String name)
	{
		Game game=findGame(codeGame);
		if(game.isStarted()==false)
		{
			for(int i=0;i<statusServer.sizePlayers();i++) 
				if(name.equals(statusServer.getPlayer(i).getName())) 
				{
					Player p=statusServer.getPlayer(i);
					p.setStatusPlayer(EnumStatePlayer.lobby);
					p.setCodeGame(game.getCode());
					game.addPlayer(statusServer.getPlayer(i));
				}
			if(game.numberOfPlayers()<game.maxNumberOfPlayers())
			{//If the lobby isn't full now I say to other players in the lobby that a new player is entered in the lobby
				
				if(game.numberOfPlayers()>=game.minNumberOfPlayers())
				{//I create a timer
					timerGameStart.add(new TimerGameStart(game));
					int t=GameConstants.thousand*(GameConstants.timeGameStart);
					timer.schedule(timerGameStart.get(timerGameStart.size()-1), t);
				}

				for(int i=0;i<game.numberOfPlayers()-1;i++)
				{
					Player p=game.getPlayer(i);
					try{
						p.getConnection().updatePlayerInLobby(game.numberOfPlayers());
					} catch (IOException e) {//the player isn't online
						StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
					}
				}
				return game.numberOfPlayers();
			}
			else
			{//lobby is full, the game can start
				//I must remove the start game timer
				for(int i=0;i<timerGameStart.size();i++)
					if(timerGameStart.get(i).getGame().equals(game))
					{
						timerGameStart.get(i).cancel();
						timerGameStart.remove(i);
						break;
					}
				
				startGame(game);
				return 0;
			}
		}
		return -1;
	}

	 //I use methods below to interactive during the game
	 public void startGame(Game game)
	 {
		 game.setStarted();
		 game.setIsInTurn(1);
		 GameManager gm=new GameManager(game);
		 gm.createGame();
		 int turn=game.getWhoseTurn();
		 for(int i=0;i<game.numberOfPlayers();i++)
		 {
			 Player player=game.getPlayer(i);
			 player.setStatusPlayer(EnumStatePlayer.inGame);
			 try{
				 player.getConnection().startGame();
	    		 printMap(player, game);
			     printVisiblePermitCard(player, game);
				 printKingBoard(player, game);
	    		 printNobility(player, game);
	    		 manageTurn(player, game.getPlayer(turn).getName(), game);
			 }catch(IOException e)
			 {//player disconnected
				 StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
			 }
		 }
	 }
	 
	 //method to print the game status
	public void printMap(Player p, Game game){
		Connection client=p.getConnection();
		PrepareDataForClient prepare=new PrepareDataForClient(game);
		//Prepare Map and Name Region
		String[][]map=prepare.map();
		String[]nameRegion=prepare.nameRegion();
		//Prepare whereIsKing
		String whereIsKing=game.whereIsKing().getName()+"";
		try {
			client.statusMap(map, nameRegion, whereIsKing);
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	public void printVisiblePermitCard(Player p, Game game){
		Connection client=p.getConnection();
		PrepareDataForClient prepare=new PrepareDataForClient(game);
		//Prepare visible permit cards
		String [][]permitCardForRegion=prepare.visiblePermitCard();
		try {
			client.statusVisiblePermitCards(permitCardForRegion);
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	public void printKingBoard(Player p, Game game){
		Connection client=p.getConnection();
		PrepareDataForClient prepare=new PrepareDataForClient(game);
		String councilBalconyName[][]=prepare.councilBalconyNameColor();
		Color councilBalconyColor[][]=prepare.councilBalconyColor();
		int[]kingReward=prepare.kingReward();
		String[][]bonusReward=prepare.bonusReward();
		String[]councillorNameOutOfBalcony=prepare.councillorNameColorOutOfBalcony();
		Color[]councillorColorOutOfBalcony=prepare.councillorColorOutOfBalcony();
		try {
			client.statusKingBoard(councilBalconyName, councilBalconyColor, kingReward, bonusReward, councillorNameOutOfBalcony, councillorColorOutOfBalcony);
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	public void printNobility(Player p, Game game){
		Connection client=p.getConnection();
		PrepareDataForClient prepare=new PrepareDataForClient(game);
		int[][]bonusNobilityRoad=prepare.nobilityRoad();
		try {
			client.statusNobilityRoad(bonusNobilityRoad);
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	public void printOpponents(Player p, Game game){
		Connection client=p.getConnection();
		PrepareDataForClient prepare=new PrepareDataForClient(game);
		String [][]statusOpponent=prepare.statusOpponents(p.getName());
		Color[]colorOpponent=prepare.colorOpponents(p.getName());
		try {
			client.statusOpponents(statusOpponent, colorOpponent);
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	public void printPlayer(Player p, Game game){
		Connection client=p.getConnection();
		PrepareDataForClient prepare=new PrepareDataForClient(game);
		String[]status=prepare.playerStatus(p); 
		Color c=p.getColor(); 
		String[]politicalCards=prepare.playerPoliticalCardsColorName(p);
		Color[]politicalCardsColors=prepare.playerPoliticalCardsColor(p);
		String[][] permitCardsTowns=prepare.playerPermitCards(p);
		try {
			client.statusPlayer(status, c, politicalCards, politicalCardsColors, permitCardsTowns);
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	
	//method to manage turn
	public void manageTurn(Player p, String namePlayerTurn, Game game) throws IOException{
		Connection client=p.getConnection();
		if(p.getName().equals(namePlayerTurn))
		{
			p.setMainAction(GameConstants.actionAvaiableAtStartTurn);
			p.setSecondaryAction(GameConstants.actionAvaiableAtStartTurn);
			//set timer before send message
			timerTurn.add(new TimerTurn(game));
			int t=GameConstants.thousand*(GameConstants.timeTurn+GameConstants.delayForCheck);
			timer.schedule(timerTurn.get(timerTurn.size()-1), t);
		}
		printOpponents(p, game);
		printPlayer(p, game);
		client.statusTurn(namePlayerTurn, GameConstants.timeTurn);
	}
	
	//ACTION FOR THE PLAYER
	private void updateStatusPlayer(Game game) {
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			printPlayer(game.getPlayer(i), game);
			printOpponents(game.getPlayer(i), game);
		}
	}
	//ACTION 1
	@Override
	public void changeCouncillor(long codeGame, int councillorChosen, int balcony) {
		try{
			Game game=findGame(codeGame);
			GameManager gm=new GameManager(game);
			Player player=game.getPlayer(game.getWhoseTurn());
			int result=gm.electCouncillor(councillorChosen, balcony);
			if(result==GameConstants.ok)
			{
				for(int i=0;i<game.numberOfPlayers();i++)
				{
					printKingBoard(game.getPlayer(i),game);	
				}
				
				updateActionPlayer(gm, game, player);
			}
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//ACTION 2
	@Override
	public void buyPermitCard(long codeGame, int []politicCardsUse, int whichBalcony, int permitCardVisible){
		try{
			Game game=findGame(codeGame);
			Player player=game.getPlayer(game.getWhoseTurn());
			GameManager gm=new GameManager(game);
			int result=gm.buyPermitCard(politicCardsUse, whichBalcony, permitCardVisible);
			if(result==GameConstants.noResource)
				player.getConnection().resurceNotEnoght();
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
			if(result==GameConstants.ok)
			{//no problem
				for(int i=0;i<game.numberOfPlayers();i++)
				{
					printVisiblePermitCard(game.getPlayer(i),game);	
				}
				
				updateActionPlayer(gm, game, player);
			}
		}catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//ACTION 3
	@Override
	public void buildEmporiumByPermit(long codeGame, int indexPermitCard, String townNameChosen) {
		try{
			Game game=findGame(codeGame);
			GameManager gm=new GameManager(game);
			Player player=game.getPlayer(game.getWhoseTurn());
			int result=gm.buildEmporiumByPermit(indexPermitCard, townNameChosen);
			if(result==GameConstants.noResource)
				player.getConnection().resurceNotEnoght();
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
			if(result==GameConstants.ok)
			{//no problem
				for(int i=0;i<game.numberOfPlayers();i++)
				{
					printMap(game.getPlayer(i),game);	
				}
				
				updateActionPlayer(gm, game, player);
			}
		}catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//ACTION 4
	@Override
	public void buildEmporiumByKing(long codeGame, int[] politicCardsUsed, String nameTown, String route) {
		try{
			Game game=findGame(codeGame);
			GameManager gm=new GameManager(game);
			Player player=game.getPlayer(game.getWhoseTurn());
			int result=gm.buildEmporiumByKing(politicCardsUsed, nameTown, route);
			if(result==GameConstants.noResource)
				player.getConnection().resurceNotEnoght();
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
			if(result==GameConstants.ok)
			{//no problem	
				for(int i=0;i<game.numberOfPlayers();i++)
				{
					printMap(game.getPlayer(i),game);	
				}
				
				updateActionPlayer(gm, game, player);
			}
		}catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//ACTION 5
	@Override
	public void engangeHelper(long codeGame) {
		try{
			Game game=findGame(codeGame);
			Player player=game.getPlayer(game.getWhoseTurn());
			GameManager gm=new GameManager(game);
			int result=gm.engangeAnHelper();
			if(result==GameConstants.ok)
			{
				
				updateActionPlayer(gm, game, player);
			}
			else
				player.getConnection().resurceNotEnoght();
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//ACTION 6
	@Override
	public void changePermitCard(long codeGame, int region) {
		try{
			Game game=findGame(codeGame);
			Player player=game.getPlayer(game.getWhoseTurn());
			GameManager gm=new GameManager(game);
			int result=gm.changePermitCard(region);
			if(result==GameConstants.ok)
			{
				for(int i=0;i<game.numberOfPlayers();i++)
				{
					printVisiblePermitCard(game.getPlayer(i),game);	
				}
				
				updateActionPlayer(gm, game, player);
			}
			if(result==GameConstants.noResource)
				player.getConnection().resurceNotEnoght();
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//ACTION 7
	@Override
	public void changeCouncillorByHelper(long codeGame, int councillorChosen, int balcony){
		try{
			Game game=findGame(codeGame);
			GameManager gm=new GameManager(game);
			Player player=game.getPlayer(game.getWhoseTurn());
			int result=gm.electCouncillorByAnHelper(councillorChosen, balcony);
			if(result==GameConstants.ok)
			{
				for(int i=0;i<game.numberOfPlayers();i++)
				{
					printKingBoard(game.getPlayer(i),game);	
				}	
				
				updateActionPlayer(gm, game, player);
			}
			if(result==GameConstants.noResource)
				player.getConnection().resurceNotEnoght();
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//ACTION 8
	@Override
	public void otherMainAction(long codeGame){
		try{
			Game game=findGame(codeGame);
			Player player=game.getPlayer(game.getWhoseTurn());
			GameManager gm=new GameManager(game);
			int result=gm.doNewMainAction();
			if(result==GameConstants.ok)
			{
				
				updateActionPlayer(gm, game, player);
			}
			else
				player.getConnection().resurceNotEnoght();
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//UPDATE ACTION PLAYER 
	private void updateActionPlayer(GameManager gm, Game game, Player player)
	{
		try{
			printPlayer(player, game);
			if(gm.manageReward())
			{
				updateStatusPlayer(game);
				player.getConnection().updateActionPlayer(player.getMainAction(), player.getSecondaryAction());
			}
		}catch(IOException e)
		{//player is disconnected
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//ACTION TO GET PARTICULAR BONUS (these bonus are in the nobility road)
	@Override
	public void selectOneBonusToken(long codeGame, String token){
		try{
			Game game=findGame(codeGame);
			Player player=game.getPlayer(game.getWhoseTurn());
			GameManager gm=new GameManager(game);
			int result=gm.selectOneBonusToken(token);
			if(result==GameConstants.ok && gm.manageReward())
			{
				
				updateActionPlayer(gm, game, player);
			}
			if(result==GameConstants.noResource)
				player.getConnection().resurceNotEnoght();
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
		}catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	@Override
	public void selectTwoBonusToken(long codeGame, String token1, String token2){
		try{
			Game game=findGame(codeGame);
			Player player=game.getPlayer(game.getWhoseTurn());
			GameManager gm=new GameManager(game);
			int result=gm.doNewMainAction();
			if(result==GameConstants.ok && gm.manageReward())
			{
				updateActionPlayer(gm, game, player);
			}
			if(result==GameConstants.noResource)
				player.getConnection().resurceNotEnoght();
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
		}catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	@Override
	public void selectBonusPermitCard(long codeGame, int bonusPermit){
		try{
			Game game=findGame(codeGame);
			Player player=game.getPlayer(game.getWhoseTurn());
			GameManager gm=new GameManager(game);
			int result=gm.selectBonusPermitCard(bonusPermit);
			if(result==GameConstants.ok && gm.manageReward())
			{
				
				updateActionPlayer(gm, game, player);
			}
			if(result==GameConstants.noResource)
				player.getConnection().resurceNotEnoght();
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
		}catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	@Override
	public void selectPermitCard(long codeGame, int permitCard)  {
		try{
			Game game=findGame(codeGame);
			Player player=game.getPlayer(game.getWhoseTurn());
			GameManager gm=new GameManager(game);
			int result=gm.doNewMainAction();
			if(result==GameConstants.ok && gm.manageReward())
			{
				
				updateActionPlayer(gm, game, player);
			}
			if(result==GameConstants.noResource)
				player.getConnection().resurceNotEnoght();
			if(result==GameConstants.error)
				player.getConnection().errorInsertedDate();
		}catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}	
	}
	
	@Override
	public void passTurn(long codeGame){
		Game game=findGame(codeGame);		
		//destroy old timer
		for(int i=0;i<timerTurn.size();i++)
			if(timerTurn.get(i).getGame().equals(game))
			{
				timerTurn.get(i).cancel();
				timerTurn.remove(i);
				break;
			}			
		int turn=game.getWhoseTurn();
		do{
			turn++;
			if(turn==game.numberOfPlayers())
				turn=0;
			game.setWhoseTurn(turn);
			game.addCount();
			checkIfTheClientIsOnline(game.getPlayer(turn));
		}while(game.getPlayer(turn).getStatePlayer().equals(EnumStatePlayer.inGameDisconnected) && game.getCount()<game.numberOfPlayers());
				
		if(game.getCount()>=game.numberOfPlayers())
		{//everyone has played his turn and the market starts
			startMarket(game);
		}
		else
		{
			for(int i=0;i<game.numberOfPlayers();i++)
			{
				try{		
				manageTurn(game.getPlayer(i), game.getPlayer(game.getWhoseTurn()).getName(), game);
				}catch (IOException e) {
					StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
				}
			}
		}
	}
	public void startTurnGameAgain(Game game){
		if(theGameIsEnd(game))
			finishGame(game);
		if(isThereAnyone(game))
		{
			game.setCount();
			game.setIsInTurn(1);
			while(game.getPlayer(game.getWhoseTurn()).getStatePlayer().equals(EnumStatePlayer.disconnected))
			{
				int turn=game.getWhoseTurn();
				turn++;
				if(turn>=game.numberOfPlayers())
					turn=0;
				game.addCount();
			}
			for(int i=0;i<game.numberOfPlayers();i++)
			{
				game.getPlayer(i).addPoliticalCard(game.pickUpPoliticalCard());
				try{
				manageTurn(game.getPlayer(i), game.getPlayer(game.getWhoseTurn()).getName(), game);
				}catch (IOException e) {
					StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
				}
			}
		}
		else
			removeGame(game);
	}

	//method to manage market
	public void startMarket(Game game){
		game.setIsInTurn(2);
		game.setPlayerDoneOffert();
		game.setMarket();
		
		//start timer make an offer
		timerDoOffert.add(new TimerDoOffert(game));
		int t=GameConstants.thousand*(GameConstants.timeDoOffert+GameConstants.delayForCheck);
		timer.schedule(timerDoOffert.get(timerDoOffert.size()-1), t);
		
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			try{
				game.getPlayer(i).getConnection().startMarket(GameConstants.timeDoOffert);
			}catch (IOException e) {
				StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
			}
		}
	}
	
	@Override
	public void addOffert(long codeGame, String[] offert) {
		try{
			Game game=findGame(codeGame);
			String namePlayer=offert[0];
			Player player=findPlayerInGame(namePlayer,game);
			Connection client=player.getConnection();
			if(game.checkValidInsertOffer(offert) && game.checkPlayerHasEnoughResourcesForOffert(offert, player) && game.offertNotExist(offert))
			{
				game.addOffert(offert);
				client.doAnotherOffert();
			}
			else
			{
				if(!game.checkPlayerHasEnoughResourcesForOffert(offert, player))
					client.resourcesNotEnoughtForOffert();
				else
					client.errorInsertedOffert();
			}
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}

	@Override
	public void finishDoOffer(long codeGame) {
		Game game=findGame(codeGame);
		game.addPlayerDoneOffert();
		if(game.getPlayerDoneOffert()==game.numberOfPlayersOnline())
		{//all players stop to make an offer and now they can start to buy
			//first I delete TimerTask timerDoOffert
			for(int i=0;i<timerDoOffert.size();i++)
				if(timerDoOffert.get(i).getGame().equals(game))
				{
					timerDoOffert.get(i).cancel();
					timerDoOffert.remove(i);
					break;
				}
			startBuyMarket(game);
		}
	}
	public void startBuyMarket(Game game){// the players start to buy randomly
		
		if(game.sizeMarket()>0)
		{
			game.setIsInTurn(3);
			//start timer buy
			timerBuy.add(new TimerBuy(game));
			int t=GameConstants.thousand*(GameConstants.timeBuy+GameConstants.delayForCheck);
			timer.schedule(timerBuy.get(timerBuy.size()-1), t);
			
			Random x=new Random();
			game.setPlayerDoneBuying();
			game.setPlayerTurnBuying(x.nextInt(game.numberOfPlayers()));
			Player player=game.getPlayer(game.getPlayerTurnBuying());
			PrepareDataForClient prepare=new PrepareDataForClient(game);
			String [][]offerts=game.readOfferts();
			offerts=prepare.offerts(offerts);
			
			for(int i=0;i<game.numberOfPlayers();i++)
			{				
			    Connection client=game.getPlayer(i).getConnection();
			    try{
		    		if(game.getPlayer(i).getName().equals(player.getName()))
		    			client.receiveOtherOfferts(offerts, GameConstants.timeBuy);
		    		else
		    			client.opponentTurnToBuy(player.getName());
			    }catch (IOException e) {
			    	StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
				}
			}
		}
		else
		{//nobody has made an offer
			startTurnGameAgain(game);
		}
	}

	@Override
	public void buyOffert(long codeGame, int offert) {
		try{
			Game game=findGame(codeGame);
			GameManager gm=new GameManager(game);
			Player player=game.getPlayer(game.getPlayerTurnBuying());
			String[]offertWant=game.getOffert(offert);
			int result=gm.buyOffert(offertWant);
			if(result==GameConstants.error)
				player.getConnection().errorInsertedBought();
			if(result==GameConstants.noResource)
				player.getConnection().resourcesNotEnoughtToBuyOffert();
			if(result==GameConstants.ok)
			{
				printPlayer(player, game);
				game.removeOffert(offert);
				if(game.sizeMarket()>0)
				{
					PrepareDataForClient prepare=new PrepareDataForClient(game);
					String [][]offerts=game.readOfferts();
					offerts=prepare.offerts(offerts);
					player.getConnection().receiveOtherOfferts(offerts, GameConstants.timeBuy);
				}
				else
					startTurnGameAgain(game);
			}
		}catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}

	@Override
	public void passMarketTurn(long codeGame) {
		Game game=findGame(codeGame);
		for(int i=0;i<timerBuy.size();i++)
			if(timerBuy.get(i).getGame().equals(game))
			{
				timerBuy.get(i).cancel();
				timerBuy.remove(i);
				break;
			}
		if(game.sizeMarket()>0)
		{	
			int marketTurn=game.getPlayerTurnBuying();
			do{
				marketTurn++;
				if(marketTurn==game.numberOfPlayers())
					marketTurn=0;
				game.setPlayerTurnBuying(marketTurn);
				game.addPlayerDoneBuying();
				checkIfTheClientIsOnline(game.getPlayer(marketTurn));
			}while(game.getPlayer(marketTurn).getStatePlayer().equals(EnumStatePlayer.inGameDisconnected) && game.getPlayerDoneBuying()<game.numberOfPlayers());
			
			if(game.getPlayerDoneBuying()>=game.numberOfPlayersOnline())
			{//everyone buys something in the market and now the normal turn  restarts
				startTurnGameAgain(game);
			}
			else
			{
				//start timer buy
				timerBuy.add(new TimerBuy(game));
				int t=GameConstants.thousand*(GameConstants.timeBuy+GameConstants.delayForCheck);
				timer.schedule(timerBuy.get(timerBuy.size()-1), t);
				
				
				Player playerHasTurn=game.getPlayer(game.getPlayerTurnBuying());
				PrepareDataForClient prepare=new PrepareDataForClient(game);
				String [][]offerts=game.readOfferts();
				offerts=prepare.offerts(offerts);
				for(int i=0;i<game.numberOfPlayers();i++)
				{				
					try{
			    		if(game.getPlayer(i).getName().equals(playerHasTurn.getName()))
			    			game.getPlayer(i).getConnection().receiveOtherOfferts(offerts, GameConstants.timeBuy);
			    		else
			    			game.getPlayer(i).getConnection().opponentTurnToBuy(playerHasTurn.getName());
					}catch (IOException e) {
						StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
					}
				}
				
			}
		}
		else
		{//there aren't any offers
			startTurnGameAgain(game);
		}
	}
	
	//AUXILIAR METHOD
	public Game findGame(long codeGame){
		for(int i=0;i<statusServer.sizeGames();i++)
			if(codeGame==statusServer.getGame(i).getCode())
				return statusServer.getGame(i);
		return null;

	}
	private Player findPlayerInGame(String name, Game game){
		for(int i=0;i<game.numberOfPlayers();i++)
			if(game.getPlayer(i).getName().equals(name))
				return game.getPlayer(i);
		return null;
	}
	private Player findPlayer(String name){
		for(int i=0;i<statusServer.sizePlayers();i++)
			if(statusServer.getPlayer(i).getName().equals(name))
				return statusServer.getPlayer(i);
		return null;
	}
	public Game findGameByPlayer(Player player){
		for(int i=0;i<statusServer.sizeGames();i++)
		{
			if(player==findPlayerInGame(player.getName(), statusServer.getGame(i)))
				return statusServer.getGame(i);
		}
		return null;
	}

	public void enterAgainInTheGame(Player player){
	Game game=findGameByPlayer(player);
		try{
			player.getConnection().giveCodeGame(game.getCode());
			printMap(player, game);
		    printVisiblePermitCard(player, game);
			printKingBoard(player, game);
			printNobility(player, game);
			printOpponents(player, game);
			printPlayer(player, game);	
			if(game.getIsInTurn()==1)
			{		
				player.getConnection().statusTurn(game.getPlayer(game.getWhoseTurn()).getName(), GameConstants.timeTurn);
			}
			if(game.getIsInTurn()==2)
			{
				player.getConnection().startMarket(GameConstants.timeDoOffert);
			}
			if(game.getIsInTurn()==3)
			{
				String [][]offerts=game.readOfferts();
				Connection client=player.getConnection();
	    		if(player.getName().equals(player.getName()))
	    			client.receiveOtherOfferts(offerts, GameConstants.timeBuy);
	    		else
	    			client.opponentTurnToBuy(player.getName());
			}
				
		}catch(IOException e)
		{
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	
	//MANAGE END GAME
	public boolean isThereAnyone(Game game){
		for(int i=0;i<game.numberOfPlayers();i++)
			if(game.getPlayer(i).getStatePlayer().equals(EnumStatePlayer.inGame))
				return true;
		return false;//everybody disconnected
	}
	public void removeGame(Game game){
		for(int i=0;i<statusServer.sizeGames();i++)
			if(statusServer.getGame(i).equals(game))
			{
				statusServer.removeGame(i);
				break;
			}
	}
	//end game in ordinary way
	private boolean theGameIsEnd(Game game) {
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			if(game.getPlayer(i).getEmporium()==0)
				return true;
		}
		return false;
	}
	private void finishGame(Game game) {
		GameManager gm=new GameManager(game);
		String[][]rank=gm.finishGame();
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			try {
				game.getPlayer(i).setStatusPlayer(EnumStatePlayer.online);
				game.getPlayer(i).getConnection().finishGame(rank);
			} catch (IOException e) {
				StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
			}
		}
		removeGame(game);
	}
	
	//TIMER 
	transient private Timer timer;
	private ArrayList <TimerGameStart>timerGameStart;
	private class TimerGameStart extends TimerTask{
		private Game game;
		public TimerGameStart(Game game) {
			this.game=game;
		}
		public Game getGame(){
			return game;
		}
		@Override
		public void run() {
			startGame(game);
		}
	
	}
	//I create a timer that counts the time the player has to do his action
	private ArrayList <TimerTurn>timerTurn;
	private class TimerTurn extends TimerTask {
		private Game game;
		public TimerTurn(Game game) {
			this.game=game;
		}
		public Game getGame(){
			return game;
		}
		@Override
		public void run() {
			passTurn(game.getCode());
		}
	}

	//private boolean checkOffert;
	private ArrayList <TimerDoOffert>timerDoOffert;
	private ArrayList <TimerBuy> timerBuy;
	private class TimerDoOffert extends TimerTask {
		private Game game;
		public TimerDoOffert(Game game) {
			this.game=game;
		}
		public Game getGame(){
			return game;
		}
		@Override
		public void run() {
			startBuyMarket(game);
		}
	}
	private class TimerBuy extends TimerTask {
			private Game game;
			public TimerBuy(Game game) {
				this.game=game;
			}
			public Game getGame(){
				return game;
			}
			@Override
			public void run() {
				startTurnGameAgain(game);
			}
		}
	
	//MANAGE CONNECT PROBLEM

	public void disconnectPlayer(Player player){
		if(player.getStatePlayer().equals(EnumStatePlayer.online))
			player.setStatusPlayer(EnumStatePlayer.disconnected);
		if(player.getStatePlayer().equals(EnumStatePlayer.lobby))
			player.setStatusPlayer(EnumStatePlayer.lobbyDisconnected);
		if(player.getStatePlayer().equals(EnumStatePlayer.inGame))
			player.setStatusPlayer(EnumStatePlayer.inGameDisconnected);
		
	}
 	public boolean checkIfTheClientIsOnline(Player player){
		try {
			return player.getConnection().heartClient();
		} catch (IOException e) {
			disconnectPlayer(player);
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
			return false;
		}
	}
}
