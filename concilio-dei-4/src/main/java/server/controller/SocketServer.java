package server.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket; 
import java.util.Random;
import java.util.logging.Level;

import server.StartServer;
import server.model.EnumStatePlayer;
import server.model.Game;
import server.model.GameConstants;
import server.model.Player;
import server.model.SocketProtocol;
import server.model.StatusServer;

/**
 * This class manages players connect with Socket mode and then receives the message from Socket Client and calls methods in RMI_Server passing the data of messages
 */
public class SocketServer{
	
	
	private StatusServer statusServer;
	private Socket  socket;
	private ServerSocket connection;
	private RMIControllerServer server;
	
	
	public SocketServer(StatusServer status , RMIControllerServer a){  
		statusServer=status;
		server=a;
		try {
			connection=new ServerSocket(GameConstants.PORT);
			waitClients();
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
		}
	}
	//this method waits client connection requests
   public void waitClients()
   {  
	   while (true)
       {   
		  try
          {  
              socket = connection.accept();
              Thread t = new Thread(new ManagerSingleClient(socket,statusServer));
		      t.start();
          }catch (Exception e){
        	  StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
         }
      }
   }
	
	/*
	 * This thread manages the clients that connect to the server
	 * The server accepts new connections, starts a new thread for the client and returns
	 * to listen for new connection requests
	 */
	private class ManagerSingleClient implements Runnable {
		private Socket socket;
		private StatusServer statusServer;
		
		private ObjectInputStream  fromClient= null;
		private ObjectOutputStream toClient=null;
		private Player player;
		
		public ManagerSingleClient(Socket c,StatusServer status){
			socket=c;
			statusServer=status;
			try{
			open();
			} catch (IOException e) {
				StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
			} 
		}
		public void setPlayer(Player player){
			this.player=player;
		}
		//open the Input/Output Stream
	 	private void open() throws IOException 
	 	{  
	 		toClient=new ObjectOutputStream(socket.getOutputStream());
	 		toClient.flush();
	 		fromClient = new ObjectInputStream(socket.getInputStream());
	 	}
	 	//close connection
		public void close() throws IOException
	    {  
		  if (socket != null)    
			  socket.close();
	      if (fromClient != null)  
	    	  fromClient.close();
	    } 
		public void run(){
		try {
				while(true)
				{ //Listening to the client
			
					String msgin = (String) fromClient.readObject();
					//the initials of the message correspond to a particular action written in the socket protocol
				
					//a player tries to connect
					if (msgin.substring(0,3).equals(SocketProtocol.PCG.toString())){
						newConnectionPlayer();
					}
					//a player creates a new game
					if (msgin.substring(0,3).equals(SocketProtocol.CNG.toString())){
						newGame(msgin.substring(3));
					}
					//to see a list of lobbies
					if (msgin.substring(0,3).equals(SocketProtocol.LOL.toString())){
						listOfGames();
					}
					//to join a lobby
					if (msgin.substring(0,3).equals(SocketProtocol.JAL.toString())){
						addPlayerToGame();
					}
					//list of maps
					if (msgin.substring(0,3).equals(SocketProtocol.LOM.toString())){
						listOfMaps();
					}
					//action
					//change councillor
					if (msgin.substring(0,3).equals(SocketProtocol.CHC.toString())){
						changeCouncillor();
					}
					if(msgin.substring(0,3).equals(SocketProtocol.EAH.toString())){				
						engageHelper(Long.parseLong(msgin.substring(3)));
					}
					if (msgin.substring(0,3).equals(SocketProtocol.OMA.toString())){				
						otherMainAction(Long.parseLong(msgin.substring(3)));
					}
					if (msgin.substring(0,3).equals(SocketProtocol.BPC.toString())){			
						buyPermitCard();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.BEP.toString())){				
						buildEmporiumByPermit();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.BEK.toString())){				
						buildEmporiumByKing();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.CPC.toString())){				
						changePermitCard();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.CCH.toString())){
						changeCouncillorByHelper();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.SBT.toString())){
						selectOneBonusToken();	
					}
					if (msgin.substring(0,3).equals(SocketProtocol.STT.toString())){
						selectTwoBonusToken();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.SBP.toString())){
						selectBonusPermitCard();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.SPC.toString())){
						selectPermitCard();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.PTT.toString())){
						passTurn();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.STA.toString())){
						startTurnGameAgain();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.SMK.toString())){
						startMarket();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.ADO.toString())){
						addOffert();
					}
					if(msgin.substring(0,3).equals(SocketProtocol.FOF.toString())){
						finishDoOffer();
					}
					if(msgin.substring(0,3).equals(SocketProtocol.SBM.toString())){
						startBuyMarket();
					}
					if(msgin.substring(0,3).equals(SocketProtocol.BOF.toString())){
						buyOffert();
					}
					if(msgin.substring(0,3).equals(SocketProtocol.PMT.toString())){
						passMarketTurn(Long.parseLong(msgin.substring(3)));
						
					}
					if(msgin.substring(0,3).equals(SocketProtocol.EAG.toString())){
						enterAgainInTheGame();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.EAG.toString())){
						removeGame();
					}
				}
			} catch (IOException e) {
				StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
			} catch (ClassNotFoundException e) {
				StartServer.logger.log(Level.WARNING, "Problem with server", e);
			}finally{
				server.disconnectPlayer(player);
		}
	}	
		//set new player 
		public void newConnectionPlayer() throws ClassNotFoundException, IOException{
			//Check how many player are online
			for(int i=0;i<statusServer.sizePlayers();i++)
			{
				server.checkIfTheClientIsOnline(statusServer.getPlayer(i));
			}
			int onlinePlayers=statusServer.onlinePlayers();
			String msgout="";
			boolean checkExist=false;
			String name = (String) fromClient.readObject();
			String password=(String) fromClient.readObject();
			Player player=null;
			for(int i=0;i<statusServer.sizePlayers();i++) 
				if(name.equals(statusServer.getPlayer(i).getName())) 
				{
					boolean isOnline=false;
					if(statusServer.getPlayer(i).getPassword().equals(password))
					{
						server.checkIfTheClientIsOnline(statusServer.getPlayer(i));
						if(statusServer.getPlayer(i).getStatePlayer().equals(EnumStatePlayer.disconnected))
						{
							statusServer.getPlayer(i).setStatusPlayer(EnumStatePlayer.online);
							msgout=SocketProtocol.PCG.toString()+(onlinePlayers)+"";
							isOnline=true;
						}
						if(statusServer.getPlayer(i).getStatePlayer().equals(EnumStatePlayer.lobbyDisconnected))
						{
							statusServer.getPlayer(i).setStatusPlayer(EnumStatePlayer.lobby);
							msgout=SocketProtocol.PCG.toString()+"-2";
							isOnline=true;
						}
						if(statusServer.getPlayer(i).getStatePlayer().equals(EnumStatePlayer.inGameDisconnected))
						{
							statusServer.getPlayer(i).setStatusPlayer(EnumStatePlayer.inGame);
							msgout=SocketProtocol.PCG.toString()+"-2";
							isOnline=true;
						}
					}
					if(!isOnline)
					{//player is online yet
						msgout=SocketProtocol.PCG.toString()+"-1";
					}
					else
					{ 
						ConnectionSocket cs=new ConnectionSocket(toClient);
						statusServer.getPlayer(i).setConnection(cs);
					}
					player=statusServer.getPlayer(i);
					setPlayer(player);
					checkExist=true;
				}
			if(!checkExist)
			{
				Random rand = new Random();
				Player p=new Player(name,password, new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()));
				setPlayer(p);
				ConnectionSocket cs=new ConnectionSocket(toClient);
				p.setConnection(cs);
				statusServer.addPlayer(p);
				//Check how many player are online
				for(int i=0;i<statusServer.sizePlayers();i++)
				{
					server.checkIfTheClientIsOnline(statusServer.getPlayer(i));
				}
				onlinePlayers=statusServer.onlinePlayers();
				msgout=SocketProtocol.PCG.toString()+(onlinePlayers)+"";
			}
			//update other players
			for(int i=0;i<statusServer.sizePlayers();i++)
			{
				server.checkIfTheClientIsOnline(statusServer.getPlayer(i));
			}
			onlinePlayers=statusServer.onlinePlayers();
			for(int i=0;i<statusServer.sizePlayers()-1;i++)
			{//try catch is inside so if a player is disconnected the error does not stop the for and other players receive the message
				try {
					if(statusServer.getPlayer(i).getStatePlayer().equals(EnumStatePlayer.online))
						statusServer.getPlayer(i).getConnection().updatePlayerOnline(onlinePlayers);
				}  catch (IOException e) {
					StartServer.logger.log(Level.INFO, GameConstants.Player_Offline, e);
					server.disconnectPlayer(statusServer.getPlayer(i));
				}
			} 
			
			toClient.writeObject(msgout);
			toClient.flush();
			//if the player is already subscribed I setup him in the right position
			if(checkExist && !player.getStatePlayer().equals(EnumStatePlayer.online))
			{
				if(player.getStatePlayer().equals(EnumStatePlayer.lobby))
				{
					Game game=server.findGameByPlayer(player);
					player.getConnection().updatePlayerInLobby(game.numberOfPlayers());
				}
				if(player.getStatePlayer().equals(EnumStatePlayer.inGame))
				{
					server.enterAgainInTheGame(player);
				}
				setPlayer(player);
			}
		}
		
		//create new game
		public void newGame (String numberPlayersAndMap) throws IOException { //string in which client put "minimum number of players" - "maximum number of player"				
			String[] split=numberPlayersAndMap.split("-"); //convert string into integer	
			int min, max, map;
			min=Integer.parseInt(split[0]);
			max=Integer.parseInt(split[1]);
			map=Integer.parseInt(split[2]);
			String name=split[3];
			String msgout=SocketProtocol.CNG+String.valueOf(server.newGame(min, max, name, map));		
			toClient.writeObject(msgout);
			toClient.flush();
		}
		//list of lobbies
		public void listOfGames () throws IOException{
			long[][] dates=server.listOfGames();
			toClient.writeObject(SocketProtocol.LOL.toString());
			toClient.flush();
			toClient.writeObject(dates);
			toClient.flush();		
		}
		
		//add a player to a game
		public void addPlayerToGame () throws ClassNotFoundException, IOException{	
			long codeGame = (long) fromClient.readObject();
			String name= (String) fromClient.readObject();
			int n=server.addPlayerToGame(codeGame, name);
			if(n>0){
				toClient.writeObject(SocketProtocol.UPL.toString()+n);
				toClient.flush();
			}
		}
	 
		private void listOfMaps() throws IOException {
			String[][][]maps=server.listOfMaps();
			toClient.writeObject(SocketProtocol.LOM.toString());
			toClient.writeObject(maps);
			toClient.flush();
		}
		
	  	//ACTION 1
	  	public void changeCouncillor() throws ClassNotFoundException, IOException {
		  long codeGame=(long) fromClient.readObject();
		  int councillorChosen=(int) fromClient.readObject();
		  int balcony= (int)fromClient.readObject();
		  server.changeCouncillor(codeGame, councillorChosen, balcony);
	  }
	  	//ACTION 2
	  	public void buyPermitCard() throws ClassNotFoundException, IOException{
		  long codeGame =(long) fromClient.readObject();
		  int []politicCardsUse=(int[]) fromClient.readObject();
		  int whichBalcony=(int) fromClient.readObject();
		  int permitCardVisible=(int) fromClient.readObject();
		  server.buyPermitCard(codeGame, politicCardsUse, whichBalcony, permitCardVisible);
	  }
	  	//ACTION 3
	  	public void buildEmporiumByPermit() throws ClassNotFoundException, IOException {
		  	long codeGame= (long) fromClient.readObject();
			int indexPermitCard= (int) fromClient.readObject();
			String townNameChosen= (String) fromClient.readObject();
		  server.buildEmporiumByPermit(codeGame, indexPermitCard, townNameChosen);
	  }
	  	//ACTION 4
		public void buildEmporiumByKing() throws ClassNotFoundException, IOException {
			long codeGame=(long) fromClient.readObject();
			int[] politicCardsUsed=	(int[]) fromClient.readObject();
			String nameTown=(String) fromClient.readObject();
			String route=(String) fromClient.readObject();
			server.buildEmporiumByKing(codeGame, politicCardsUsed, nameTown, route);
		}
		//ACTION 5
		public void engageHelper(long s) throws IOException {
			server.engangeHelper(s);
		}
		//ACTION 6
		public void changePermitCard() throws ClassNotFoundException, IOException {
			long codeGame= (long) fromClient.readObject();
			int region= (int) fromClient.readObject();
			server.changePermitCard(codeGame, region);
		}
		//ACTION 7
		public void changeCouncillorByHelper() throws ClassNotFoundException, IOException {
			long codeGame= (long) fromClient.readObject();
			int  councillorChosen= (int) fromClient.readObject();
			int balcony=(int) fromClient.readObject();
			server.changeCouncillorByHelper(codeGame, councillorChosen, balcony);
		}
		//ACTION 8
		public void otherMainAction(long codeGame) throws IOException {
			server.otherMainAction(codeGame);
		}
		//ACTION TO GET PARTICULAR BONUS (these bonus are in the nobility road)
		public void selectOneBonusToken() throws ClassNotFoundException, IOException {
			long codeGame=(long) fromClient.readObject();
			String token= (String) fromClient.readObject();
			server.selectOneBonusToken(codeGame, token);
		}
		public void selectTwoBonusToken() throws ClassNotFoundException, IOException {
			long codeGame=(long) fromClient.readObject();
			String token1= (String) fromClient.readObject();
			String token2= (String) fromClient.readObject();
			server.selectTwoBonusToken(codeGame, token1, token2);
		}
		public void selectBonusPermitCard() throws ClassNotFoundException, IOException {
			long codeGame=(long) fromClient.readObject();
			int bonusPermit= (int) fromClient.readObject();
			server.selectBonusPermitCard(codeGame, bonusPermit);
			}
		public void selectPermitCard() throws ClassNotFoundException, IOException {
			long codeGame=(long) fromClient.readObject();
			int permitCard=(int) fromClient.readObject();
			server.selectPermitCard(codeGame, permitCard);
		}
		public void passTurn() throws ClassNotFoundException, IOException{
			long codeGame=(long)fromClient.readObject();
			server.passTurn(codeGame);
		}
		private void startTurnGameAgain() throws ClassNotFoundException, IOException{
			Game game= (Game) fromClient.readObject();
			server.startTurnGameAgain(game);
		}
		//MARKET
		private void startMarket() throws ClassNotFoundException, IOException{
			Game game= (Game) fromClient.readObject();
			server.startMarket(game);
		}
		public void addOffert() throws ClassNotFoundException, IOException {
			long codeGame= (long) fromClient.readObject();
			String[] offert= (String[]) fromClient.readObject();
			server.addOffert(codeGame, offert);
		}
		public void finishDoOffer() throws IOException, ClassNotFoundException {
			long codeGame=(long)fromClient.readObject();
			server.finishDoOffer(codeGame);
		}
		private void startBuyMarket() throws ClassNotFoundException, IOException {//in random order the players start to buy
			Game game= (Game) fromClient.readObject();
			server.startBuyMarket(game);
		}
		public void buyOffert() throws ClassNotFoundException, IOException {
			long codeGame=(long) fromClient.readObject();
			int offer= (int) fromClient.readObject();
			server.buyOffert(codeGame, offer);
		}
		public void passMarketTurn(long codeGame) throws ClassNotFoundException, IOException {
			server.passMarketTurn(codeGame);
		}
		
		//restore session
		public void enterAgainInTheGame() throws ClassNotFoundException, IOException{
			Player player= (Player) fromClient.readObject();
			server.enterAgainInTheGame(player);
		}
		//end game
		public void removeGame() throws ClassNotFoundException, IOException{
			Game game= (Game) fromClient.readObject();
			server.removeGame(game);
		}		
	}
}
