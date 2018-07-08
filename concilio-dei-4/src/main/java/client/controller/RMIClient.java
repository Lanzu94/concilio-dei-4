package client.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import server.StartServer;

/**
 * This class implements RMIClientInterface. It creates thread that calls the methods in the ClientController so in this way
 * the server doesn't wait the methods in the client to end
 */
public class RMIClient extends UnicastRemoteObject implements RMIClientInterface{
	/*
	 * The program uses this class to send message to the server. 
	 * It invokes the server's method and receives the return.
	 */
	transient private ClientController clientController;
	private ArrayList <Thread> threads;
	private int sync;
	
	public RMIClient () throws IOException
	{
		StartThread startThread=new StartThread();
		sync=0;
		threads=new ArrayList<>();
		Thread start=new Thread(startThread);
		start.start();
	}

	public void setController(ClientController c)
	{
		clientController=c;
	}
	
	private class StartThread implements Runnable{
		@Override
		public void run() {
			while(true)
			{
				if(!threads.isEmpty() && sync==0)
				{	
					sync=1;
					threads.get(0).start();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					StartServer.logger.log(Level.WARNING, "Error in RMI Client", e);
					Thread.currentThread().interrupt();
				}	
			}
		}
	}
	
	/*
	 * Below there are the methods that the server can invoke to the client
	 */
	
	@Override
	public void updatePlayerOnline(int n) throws IOException {	
		Thread t = new Thread(new UpdatePlayerOnline(n));
		threads.add(t);
	}
	private class UpdatePlayerOnline implements Runnable {
		 private int size;
		 public UpdatePlayerOnline(int n)
		 {
			 size=n;
		 }
		 @Override
		 public void run() {
			 clientController.updatePlayerOnline(size);
			 threads.remove(0);
			 sync=0;
			 
        }
    }
	@Override
	public void updatePlayerInLobby(int n) throws IOException {
		Thread t = new Thread(new UpdateLobby(n));
        threads.add(t);
	}
	private class UpdateLobby implements Runnable {
		 private int numberPlayer;
		 public UpdateLobby(int n)
		 {
			 numberPlayer=n;
		 }
		 @Override
		 public void run() {
			 clientController.updatePlayerInLobby(numberPlayer);
			 threads.remove(0);
			 sync=0;
			 
        }
    }
	@Override
	public void startGame() throws IOException {
		Thread t = new Thread(new StartGame());
        threads.add(t);	
	}
	private class StartGame implements Runnable {
		@Override
		 public void run() {
			 clientController.startGame();
			 threads.remove(0);
			 sync=0;
			 
			 
       }
   }
	
	@Override
	public void statusMap(String[][] map,String[]nameRegion,String king) throws IOException {		
		Thread t = new Thread(new StatusMap(map,nameRegion,king));
        threads.add(t);
	}
	private class StatusMap implements Runnable{
		private String[][] map;
		private String[]nameRegion;
		private String king;
		public StatusMap(String[][] map,String[]nameRegion,String king){
			this.map=map;
			this.nameRegion=nameRegion;
			this.king=king;
		}
		@Override
		public void run() {
			clientController.printMap(map,nameRegion,king);
			 threads.remove(0);
			 sync=0;
			
		}
		
	}

	@Override
	public void statusVisiblePermitCards(String[][] permitCardTowns) throws IOException {		
		Thread t = new Thread(new StatusVisiblePermitCards(permitCardTowns));
        threads.add(t);	
	}
	private class StatusVisiblePermitCards implements Runnable{
		private String[][] permitCardTowns;
		public StatusVisiblePermitCards(String[][] permitCardTowns){
			this.permitCardTowns=permitCardTowns;
		}
		@Override
		public void run() {
			clientController.printPermitCards(permitCardTowns);
			 threads.remove(0);
			 sync=0;
			
		}
		
	}
	@Override
	public void statusKingBoard(String[][] councilBalconyName, Color[][] councilBalconyColor, int[] kingReward,
			String[][] bonusReward,String[]councillorNameOutOfBalcony,Color[]councillorColorOutOfBalcony) throws IOException {		
		Thread t = new Thread(new StatusKingBoard( councilBalconyName, councilBalconyColor,kingReward,
				bonusReward,councillorNameOutOfBalcony,councillorColorOutOfBalcony));
        threads.add(t);	
	}
	private class StatusKingBoard implements Runnable{
		private String[][] councilBalconyName;
		private Color[][] councilBalconyColor;
		private int[] kingReward;
		private String[][] bonusReward;
		private String[]councillorNameOutOfBalcony;
		private Color[]councillorColorOutOfBalcony;
		public StatusKingBoard(String[][] councilBalconyName, Color[][] councilBalconyColor, int[] kingReward,
				String[][] bonusReward,String[]councillorNameOutOfBalcony,Color[]councillorColorOutOfBalcony){
			this.councilBalconyName=councilBalconyName;
			this.councilBalconyColor=councilBalconyColor;
			this.kingReward=kingReward;
			this.bonusReward=bonusReward;
			this.councillorNameOutOfBalcony=councillorNameOutOfBalcony;
			this.councillorColorOutOfBalcony=councillorColorOutOfBalcony;
		}
		@Override
		public void run() {
			clientController.printKingBoard(councilBalconyName, councilBalconyColor, kingReward, bonusReward,councillorNameOutOfBalcony,councillorColorOutOfBalcony);
			 threads.remove(0);
			 sync=0;
		}
		
	}

	@Override
	public void statusNobilityRoad(int[][] bonusNobilityRoad) {		
		Thread t = new Thread(new StatusNobilityRoad(bonusNobilityRoad));
        threads.add(t);	
	}
	private class StatusNobilityRoad implements Runnable{
		private int [][]bonusNobilityRoad;
		public StatusNobilityRoad(int[][] bonusNobilityRoad){
			this.bonusNobilityRoad=bonusNobilityRoad;
		}
		@Override
		public void run() {
			 clientController.printNobilityRoad(bonusNobilityRoad);
			 threads.remove(0);
			 sync=0;		
		}
		
	}

	@Override
	public void statusOpponents(String[][] statusOpponent,Color[]opponentColor) throws IOException {		
		Thread t = new Thread(new StatusOpponent(statusOpponent,opponentColor));
        threads.add(t);	
	}
	private class StatusOpponent implements Runnable{
		private String[][] statusOpponent;
		private Color[]opponentColor;
		public StatusOpponent(String[][] statusOpponent,Color[]opponentColor){
			this.statusOpponent=statusOpponent;
			this.opponentColor=opponentColor;
		}
		@Override
		public void run() {
			clientController.printOpponentStatus(statusOpponent,opponentColor);
			 threads.remove(0);
			 sync=0;			
		}
		
	}

	@Override
	public void statusPlayer(String[] status, Color c, String[] politicalCards, Color[] politicalCardsColors,
			String[][] permitCardsTowns) throws IOException {		
		Thread t = new Thread(new StatusPlayer(status, c, politicalCards, politicalCardsColors, permitCardsTowns));
        threads.add(t);	
	}
	private class StatusPlayer implements Runnable{
		private String[] status;
		private Color c;
		private String[] politicalCards;
		private Color[] politicalCardsColors;
		private String[][] permitCardsTowns;
		public StatusPlayer(String[] status, Color c, String[] politicalCards, Color[] politicalCardsColors,
				String[][] permitCardsTowns){
			this.status=status;
			this.c=c;
			this.politicalCards=politicalCards;
			this.politicalCardsColors=politicalCardsColors;
			this.permitCardsTowns=permitCardsTowns;
		}
		@Override
		public void run() {
			clientController.printStatusPlayer(status, c, politicalCards, politicalCardsColors, permitCardsTowns);	
			 threads.remove(0);
			 sync=0;
				
			
		}
	}
	@Override
	public void statusTurn(String turn, int timeTurn) throws IOException {		
		Thread t = new Thread(new StatusTurn(turn, timeTurn));
		threads.add(t);
	}
	private class StatusTurn implements Runnable{
		private String turn;
		private int timeTurn;
		public StatusTurn(String turn,int timeTurn){
			this.turn=turn;
			this.timeTurn=timeTurn;
		}
		@Override
		public void run() {
			clientController.printStatusTurn(turn,timeTurn);
			threads.remove(0);
			 sync=0;
		}
		
	}
	@Override
	public void updateActionPlayer(int mainAction, int secondaryAction) throws IOException {		
		Thread t = new Thread(new updateActionPlayer(mainAction, secondaryAction));
        threads.add(t);
	}
	private class updateActionPlayer implements Runnable{
		private int mainAction;
		private int secondaryAction;
		public updateActionPlayer(int mainAction, int secondaryAction){
			this.mainAction=mainAction;
			this.secondaryAction=secondaryAction;
		}
		@Override
		public void run() {
			clientController.actionAgain(mainAction, secondaryAction);
			 threads.remove(0);
			 sync=0;
			
			
		}
		
	}
	@Override
	public void resurceNotEnoght()throws IOException{		
		Thread t = new Thread(new ResourceNotEnought());
        threads.add(t);
	}
	private class ResourceNotEnought implements Runnable{
		@Override
		public void run() {
			clientController.resourceNotEnoght();
			 threads.remove(0);
			 sync=0;		
		}
	}
	@Override
	public void errorInsertedDate()throws IOException{		
		Thread t = new Thread(new ErrorInsertedDate());
        threads.add(t);
	}
	private class ErrorInsertedDate implements Runnable{
		@Override
		public void run() {
			clientController.ErrorIsertedDate();	
			 threads.remove(0);
			 sync=0;
		}
	}

	@Override
	public void select1BonusToken() throws IOException {		
		Thread t = new Thread(new Select1BonusToken());
        threads.add(t);
	}
	private class Select1BonusToken implements Runnable{
		@Override
		public void run() {
			clientController.select1BonusToken();
			 threads.remove(0);
			 sync=0;			
		}
	}

	@Override
	public void select2BonusToken() throws IOException {		
		Thread t = new Thread(new Select2BonusToken());
        threads.add(t);
	}
	private class Select2BonusToken implements Runnable{
		@Override
		public void run() {
			clientController.select2BonusToken();
			 threads.remove(0);
			 sync=0;		
		}
	}

	@Override
	public void select1BonusPermit() throws IOException {		
		Thread t = new Thread(new Select1BonusPermit());
        threads.add(t);
		
	}
	private class Select1BonusPermit implements Runnable{
		@Override
		public void run() {
			clientController.select1BonusPermit();
			 threads.remove(0);
			 sync=0;			
		}
	}

	@Override
	public void selectPermitCard() throws IOException {	
		Thread t = new Thread(new SelectPermitCard());
        threads.add(t);
	}
	private class SelectPermitCard implements Runnable{
		@Override
		public void run() {
			clientController.selectPermitCard();
			 threads.remove(0);
			 sync=0;			
		}
	}
	

	//method for the market
	@Override
	public void startMarket(int timeDoOffert) throws IOException {
		Thread t=new Thread(new StartMarket(timeDoOffert));
		threads.add(t);
	}
	private class StartMarket implements Runnable{
		private int timeDoOffert;
		public StartMarket(int timeDoOffert){
			this.timeDoOffert=timeDoOffert;
		}
		@Override
		public void run() {
			clientController.startMarket(timeDoOffert);
			 threads.remove(0);
			 sync=0;
		}
		
	}
	
	@Override
 	public void doAnotherOffert() throws IOException {
		Thread t=new Thread(new DoAnotherOffert());
		threads.add(t);
	}
	private class DoAnotherOffert implements Runnable{
		@Override
		public void run() {
			clientController.doAnotherOffert();
			 threads.remove(0);
			 sync=0;
		}
		
	}
	
	@Override
	public void resourcesNotEnoughtForOffert() throws IOException {
		Thread t=new Thread(new ResourcesNotEnoughtForOffert());
		threads.add(t);
	}
	private class ResourcesNotEnoughtForOffert implements Runnable{
		@Override
		public void run() {
			clientController.resourcesNotEnoughtForOffert();
			 threads.remove(0);
			 sync=0;
		}
		
	}
	
	@Override
	public void errorInsertedOffert() throws IOException {
		Thread t=new Thread(new ErrorInsertedOffert());
		threads.add(t);
	}
	private class ErrorInsertedOffert implements Runnable{
		@Override
		public void run() {
			clientController.errorInsertedOffert();
			 threads.remove(0);
			 sync=0;
		}
		
	}
	
	@Override
	public void receiveOtherOfferts(String[][] offerts, int timeBuy) throws IOException {
		Thread t=new Thread(new ReceiveOtherOfferts(offerts, timeBuy));
		threads.add(t);
	}
	private class ReceiveOtherOfferts implements Runnable{
		private String[][] offerts;
		private int timeBuy;
		public ReceiveOtherOfferts(String[][] offerts, int timeBuy) {
			this.offerts=offerts;
			this.timeBuy=timeBuy;
		}
		@Override
		public void run() {
			clientController.receiveOtherOfferts(offerts, timeBuy);
			 threads.remove(0);
			 sync=0;
		}
		
	}
	
	@Override
	public void opponentTurnToBuy(String opponentName) throws IOException {
		Thread t=new Thread(new OpponentTurnToBuy(opponentName));
		threads.add(t);
	}
	private class OpponentTurnToBuy implements Runnable{
		private String opponentName;
		public OpponentTurnToBuy(String opponentName) {
			this.opponentName=opponentName;
		}
		@Override
		public void run() {
			clientController.opponentTurnToBuy(opponentName);
			 threads.remove(0);
			 sync=0;
		}
		
	}
	
	@Override
	public void resourcesNotEnoughtToBuyOffert() throws IOException {
		Thread t=new Thread(new ResourcesNotEnoughtToBuyOffert());
		threads.add(t);
	}
	private class ResourcesNotEnoughtToBuyOffert implements Runnable{
		@Override
		public void run() {
			clientController.resourcesNotEnoughtToBuyOffert();
			 threads.remove(0);
			 sync=0;
		}
		
	}

	@Override
	public void errorInsertedBought() throws IOException {
		Thread t=new Thread(new ErrorInsertedBought());
		threads.add(t);
	}
	private class ErrorInsertedBought implements Runnable{
		@Override
		public void run() {
			clientController.errorInsertedBought();
			 threads.remove(0);
			 sync=0;
		}
		
	}

	@Override
	public void finishGame(String[][]rank){
		Thread t=new Thread(new FinishGame(rank));
		threads.add(t);
	}
	private class FinishGame implements Runnable{
		private String [][]rank;
		public FinishGame(String [][]rank) {
			this.rank=rank;
		}
		@Override
		public void run() {
			clientController.finishGame(rank);
			 threads.remove(0);
			 sync=0;
		}
		
	}
	
	@Override
	public boolean heartClient() throws IOException {
		return true;//the server calls this method to know if the client is online or disconnected
	}
	
	@Override
	public void giveCodeGame(long code) throws IOException {
		Thread t=new Thread(new GiveCodeGame(code));
		threads.add(t);
	}
	private class GiveCodeGame implements Runnable{
		private long code;
		public GiveCodeGame (long code) {
			this.code=code;
		}
		@Override
		public void run() {
			clientController.giveGameCode(code);
			 threads.remove(0);
			 sync=0;		
		}
		
	}
}
