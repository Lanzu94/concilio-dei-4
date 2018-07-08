package client.controller;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import client.model.ClientConstants;
import client.model.EnumPage;
import client.model.EnumStatusGame;
import client.model.LocalStateGame;
import client.model.Page;
import client.model.SetUpPlayer;
import server.StartServer;
import server.controller.ConnectionSocket;
import server.controller.PrepareDataForClient;
import server.controller.RMIServerInterface;
import server.model.Game;
import server.model.GameConstants;
import server.model.Player;
import server.model.SocketProtocol;

/**
 * This class checks the connection of the player that has chosen "socket mode".
 * After the connection this class receives the data from the server and then calls the methods in the ClientController
 */
public class SocketClient implements Observer{
	/*
	 * this class manages the Socket connection mode in CLI
	 */

	private Page modelPage;
	private SetUpPlayer player;
	private Socket connection;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private ClientController cli;
	
	public SocketClient(Page p,SetUpPlayer sp, ClientController c) {
		modelPage=p;
		player=sp;
		cli=c;
		try{
			connection = new Socket(ClientConstants.servername, ClientConstants.serverport); //localhost
			fromServer= new ObjectInputStream(connection.getInputStream());
			toServer = new ObjectOutputStream(connection.getOutputStream());
			ClientConnectionSocket connSocket=new ClientConnectionSocket(toServer,fromServer);
			cli.setConnection(connSocket);
			
		} catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
	}
	
	private void managerOption(String n)
	{
		boolean valid_option=false;
		
			if(modelPage.getPage().equals(EnumPage.play_online) || modelPage.getPage().equals(EnumPage.name_not_valid))
			{// check online the name that the player has inserted
				try{
					check_name(n);
					valid_option=true;
				}catch(Exception e)
				{
					StartServer.logger.log(Level.WARNING, "Inserted wrong date", e);
					valid_option=false;
				}
			}
			else
			{
				cli.managerOption(n);
				valid_option=true;
			}
		if(!valid_option)
		{//if the player uses a not valid key I print again the page where he is
			modelPage.setPage(modelPage.getPage(),"");
		}
		
	}
	
	public void update(Observable o, Object arg) {
		String option=arg.toString();
		managerOption(option);		
		
	}
	
	
	void check_name(String dati){
		int n=-1;	
		String[] d=dati.split(" ");
		String name=d[0];
		String password=d[1];
		String msgout=SocketProtocol.PCG.toString(); 
		try {
			toServer.writeObject(msgout);
			toServer.writeObject(name);
			toServer.writeObject(password);
			toServer.flush();
		} catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
		String msgin="";
		try {
			msgin = fromServer.readObject().toString();
		} catch (ClassNotFoundException e) {
			StartServer.logger.log(Level.SEVERE, "Problem Stream", e);
		} catch (IOException e) {
			StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
		}
		n=Integer.parseInt(msgin.substring(3));		
		if(n==-1)
		{
			modelPage.setPage(EnumPage.name_not_valid,"");
		}
		if(n>=0)
		{//The name is valid
			//the next commends need to communicate with the server
			player.setName(name);
			Thread t=new Thread(new ListeningServer(fromServer,modelPage));
			t.start();
			modelPage.setPage(EnumPage.menu_online,n+"");
		}	
		if(n<-1)
		{
			player.setName(name);
			cli.startGame();
			Thread t=new Thread(new ListeningServer(fromServer,modelPage));
			t.start();
		}
	}
	
	//Thread that listens to the server
	private class ListeningServer implements Runnable {
		private ObjectInputStream fromServer;
		private Page modelPage;
		public ListeningServer(ObjectInputStream server, Page p){
			fromServer=server;
			modelPage=p;
		}
		@Override
		public void run() {
			while(true){
				try { //when the clients have to wait and listen the string, this thread is executed
					//the string that receives are acronyms that correspond to a specific action
					String msgin=(String) fromServer.readObject();
					if(msgin.substring(0,3).equals(SocketProtocol.UNP.toString()))
					{
						modelPage.setPage(EnumPage.update_menu_online,msgin.substring(3));
					}
					if (msgin.substring(0,3).equals(SocketProtocol.CNG.toString()))
					{
						newGameConfirm(msgin.substring(3));
					}
					if(msgin.substring(0,3).equals(SocketProtocol.LOL.toString()))
					{
						printListOfLobby();
					}
					if (msgin.substring(0, 3).equals(SocketProtocol.UPL.toString()))
					{
						addPlayerToLobby(msgin.substring(3));
					}
					if (msgin.substring(0, 3).equals(SocketProtocol.LOM.toString()))
					{
						String[][][]maps=(String[][][])fromServer.readObject();
						modelPage.setPage(EnumPage.list_of_maps,maps);
					}
					if (msgin.substring(0, 3).equals(SocketProtocol.GIS.toString()))
					{
						startGame();
					}
					if (msgin.substring(0, 3).equals(SocketProtocol.GCG.toString()))
					{
						long codeGame=(long)fromServer.readObject();
						cli.giveGameCode(codeGame);
					}
					
					if (msgin.substring(0,3).equals(SocketProtocol.PMG.toString()))
					{
						printMap();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.PPC.toString()))
					{
						printPermitCards();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.SKC.toString())){
						printKingBoard();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.SNR.toString())){
						printNobilityRoad();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.PSO.toString())){
						printOpponentStatus();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.PSP.toString())){
						printStatusPlayer ();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.TUR.toString())){
						printStatusTurn (msgin.substring(3));
					}
					if (msgin.substring(0,3).equals(SocketProtocol.UAP.toString())){
						actionAgain();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.RNE.toString())){
						//resource not enough
						cli.resourceNotEnoght();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.EID.toString())){
						//error inserted data
						cli.ErrorIsertedDate();
					}
					//action to manage the bonus in the nobility road
					if (msgin.substring(0,3).equals(SocketProtocol.SBT.toString())){
						select1BonusToken();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.STT.toString())){
						select2BonusToken();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.SBP.toString())){
						select1BonusPermit();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.SPC.toString())){
						selectPermitCard();
					}
					//market
					if (msgin.substring(0,3).equals(SocketProtocol.SMK.toString())){
						//start market
						int timeDoOffert=(int)fromServer.readObject();
						cli.startMarket(timeDoOffert);
					}
					if (msgin.substring(0,3).equals(SocketProtocol.ADO.toString())){
						//add offer
						cli.doAnotherOffert();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.ROO.toString())){
						//receive other offer
						String[][]offerts=(String[][])fromServer.readObject();
						int timeToBuy=(int)fromServer.readObject();
						cli.receiveOtherOfferts(offerts, timeToBuy);
					}
					if (msgin.substring(0,3).equals(SocketProtocol.OTB.toString())){
						//opponent turn to buy
						String opponentName=(String)fromServer.readObject();
						cli.opponentTurnToBuy(opponentName);
					}
					if (msgin.substring(0,3).equals(SocketProtocol.RNO.toString())){
						//resource not enough for offer
						cli.resourcesNotEnoughtForOffert();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.EIO.toString())){
						//error insert offer
						cli.errorInsertedOffert();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.RNB.toString())){
						//resource not enough to buy offer
						cli.resourcesNotEnoughtToBuyOffert();
					}
					if (msgin.substring(0,3).equals(SocketProtocol.EIB.toString())){
						//error insert bought
						cli.errorInsertedBought();
					}
					//finish game
					if (msgin.substring(0,3).equals(SocketProtocol.FGM.toString())){
						String[][]rank=(String[][])fromServer.readObject();
						cli.finishGame(rank);
					}
					//other action
					if (msgin.substring(0,3).equals(SocketProtocol.HRT.toString())){
						toServer.writeObject(SocketProtocol.HRT.toString());
					}
					
				} catch (IOException e) {
					StartServer.logger.log(Level.SEVERE, ClientConstants.SERVER_IS_DOWN, e);
					return;
				}catch (ClassNotFoundException e) {
					StartServer.logger.log(Level.SEVERE, "Problem Stream", e);
					return;
				} 
			}
		}
		
	}

	private void newGameConfirm(String msgin){ 
		long codeGame= Long.parseLong(msgin); //get code of the game and convert it in long
		player.setCodeGame(codeGame);
	}
	
	public void printListOfLobby()
	{
		try {
			long[][] msgin=(long[][])fromServer.readObject();
			modelPage.setPage(EnumPage.join_game, msgin);
		} catch (Exception e) {
			System.out.println("ERROR="+e);
		}
	}
	
	private void addPlayerToLobby (String s){
		int cont=Integer.parseInt(s); //receive the number of the players
		cli.updatePlayerInLobby(cont);
	}
	
	public void startGame(){//I say to client that the game is starting
		cli.startGame();
	}
	
	private void printMap () throws ClassNotFoundException, IOException{
		String [][] map=(String[][]) fromServer.readObject();
		String [] nameRegion= (String[]) fromServer.readObject();
		String king= (String) fromServer.readObject();
		cli.printMap(map, nameRegion, king);
	}
	
	public void printPermitCards() throws ClassNotFoundException, IOException{	
		String[][] permitCardTowns;
		permitCardTowns = (String[][]) fromServer.readObject();
		cli.printPermitCards(permitCardTowns);
	}
	public void printKingBoard() throws ClassNotFoundException, IOException{
		String[][]councilBalconyName=(String[][]) fromServer.readObject();
		Color[][] councilBalconyColor= (Color[][]) fromServer.readObject();
		int[]kingReward= (int[]) fromServer.readObject();
		String[][]bonusReward=(String[][]) fromServer.readObject();
		String[] councillorNameOutOfBalcony= (String []) fromServer.readObject();
		Color[] councillorColorOutOfBalcony= (Color []) fromServer.readObject();
		cli.printKingBoard(councilBalconyName, councilBalconyColor, kingReward, bonusReward, councillorNameOutOfBalcony, councillorColorOutOfBalcony);
	}	
	public void printNobilityRoad() throws ClassNotFoundException, IOException{
		int[][]bonusNobilityRoad= (int[][]) fromServer.readObject();
		cli.printNobilityRoad(bonusNobilityRoad);
	}
	public void printOpponentStatus() throws ClassNotFoundException, IOException{
		String[][]statusOpponent= (String[][]) fromServer.readObject();
		Color[] colorOpponent= (Color[]) fromServer.readObject();
		cli.printOpponentStatus(statusOpponent, colorOpponent);
	}
	public void printStatusPlayer() throws ClassNotFoundException, IOException{
		String[] status= (String[]) fromServer.readObject();
		Color c= (Color) fromServer.readObject();
		String [] politicalCards=(String[]) fromServer.readObject();
		Color[] politicalCardsColors= ( Color[]) fromServer.readObject();
		String[][] permitCardsTowns= (String[][]) fromServer.readObject();
		cli.printStatusPlayer(status, c, politicalCards, politicalCardsColors, permitCardsTowns);
	}
	
	public void printStatusTurn(String turn) throws ClassNotFoundException, IOException{
		int timeTurn;
		timeTurn = (int) fromServer.readObject();
		cli.printStatusTurn(turn, timeTurn);
	}
	private void actionAgain() throws ClassNotFoundException, IOException {
		int mainAction=(int) fromServer.readObject();
		int secondaryAction=(int) fromServer.readObject();
		cli.actionAgain(mainAction, secondaryAction);
	}
	
	//ACTION FOR CHOOSING THE BONUS IN NOBILITY ROAD (this method are invoked to the server)
	public void select1BonusToken(){
		cli.select1BonusToken();
	}
	public void select2BonusToken(){
		cli.select2BonusToken();
	}
	public void select1BonusPermit(){
		cli.select1BonusPermit();
	}
	public void selectPermitCard(){
		cli.selectPermitCard();
	}

		
		
}

