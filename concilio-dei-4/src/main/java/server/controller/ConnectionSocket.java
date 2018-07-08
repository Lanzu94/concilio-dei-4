package server.controller;


import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.logging.Level;

import client.model.LocalStateGame;
import server.StartServer;
import server.model.SocketProtocol;

/**
 * This class sends message to Socket_Client
 */
public class ConnectionSocket implements Connection{

	public static final String problem_Socket_Connection="Problem with socket connection";
	
	private ObjectOutputStream toClient;
	public ConnectionSocket(ObjectOutputStream o) {
		toClient=o;
	}
	
	@Override
	public void updatePlayerOnline(int n) throws IOException {
		try {
			String msgout=SocketProtocol.UNP.toString()+n;
			toClient.writeObject(msgout);
			toClient.flush();
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, problem_Socket_Connection, e);
		}
	}
	
	@Override
	public void updatePlayerInLobby(int n) throws IOException {
		try {
			String msgout=SocketProtocol.UPL.toString()+n; //update player in lobby
			toClient.flush();
			toClient.writeObject(msgout);//return the number of players in the lobby
			toClient.flush();
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, problem_Socket_Connection, e);
		}
	}

	public void startGame(){
		try{
			String msgout=SocketProtocol.GIS.toString(); //game is started
			toClient.flush();
			toClient.writeObject(msgout);
			toClient.flush();
		} catch (IOException e) {
			StartServer.logger.log(Level.INFO, problem_Socket_Connection, e);
		}
	}
	
	public void statusMap(String[][] map, String[] nameRegion, String whereIsKing) throws IOException{
		String msgout =SocketProtocol.PMG.toString();
		toClient.flush();
		toClient.writeObject(msgout);
		toClient.flush();
		toClient.writeObject(map);
		toClient.flush();
		toClient.writeObject(nameRegion);
		toClient.flush();
		toClient.writeObject(whereIsKing);
		toClient.flush();
		toClient.flush();
	}
	@Override
	public void statusVisiblePermitCards(String[][] permitCardForRegion)throws IOException {
		String msgout =SocketProtocol.PPC.toString();
		toClient.flush();
		toClient.writeObject(msgout);
		toClient.flush();
		toClient.writeObject(permitCardForRegion);
		toClient.flush();
	}
	@Override
	public void statusKingBoard(String[][] councilBalconyName, Color[][] councilBalconyColor, int[] kingReward,
		String[][] bonusReward, String[] councillorNameOutOfBalcony, Color[] councillorColorOutOfBalcony) throws IOException{
		String msgout =SocketProtocol.SKC.toString();
		toClient.flush();
		toClient.writeObject(msgout);
		toClient.flush();
		toClient.writeObject(councilBalconyName);
		toClient.flush();
		toClient.writeObject(councilBalconyColor);
		toClient.flush();
		toClient.writeObject(kingReward);
		toClient.flush();
		toClient.writeObject(bonusReward);
		toClient.flush();
		toClient.writeObject(councillorNameOutOfBalcony);
		toClient.flush();
		toClient.writeObject(councillorColorOutOfBalcony);
		toClient.flush();
	}
	@Override
	public void statusNobilityRoad(int[][] bonusNobilityRoad)throws IOException {
		String msgout=SocketProtocol.SNR.toString();
		toClient.flush();
		toClient.writeObject(msgout);
		toClient.flush();
		toClient.writeObject(bonusNobilityRoad);
		toClient.flush();
	}
	@Override
	public void statusOpponents(String[][] statusOpponent, Color[] colorOpponent) throws IOException{
		String msgout=SocketProtocol.PSO.toString();
		toClient.flush();
		toClient.writeObject(msgout);
		toClient.flush();
		toClient.writeObject(statusOpponent);
		toClient.flush();
		toClient.writeObject(colorOpponent);
		toClient.flush();
	}
	@Override
	public void statusPlayer(String[] status, Color c, String[] politicalCards, Color[] politicalCardsColors,
			String[][] permitCardsTowns) throws IOException{
		String msgout=SocketProtocol.PSP.toString();
		toClient.flush();
		toClient.writeObject(msgout);
		toClient.flush();
		toClient.writeObject(status);
		toClient.flush();
		toClient.writeObject(c);
		toClient.flush();
		toClient.writeObject(politicalCards);
		toClient.flush();
		toClient.writeObject(politicalCardsColors);
		toClient.flush();
		toClient.writeObject(permitCardsTowns);
		toClient.flush();		
	}
	public void statusTurn(String namePlayerTurn, int timeTurn)throws IOException {
		String msgout= SocketProtocol.TUR.toString()+namePlayerTurn;
		toClient.flush();
		toClient.writeObject(msgout);
		toClient.flush();
		toClient.writeObject(timeTurn);
		toClient.flush();
	}
	
	public void selectPermitCard() throws IOException {
		toClient.writeObject(SocketProtocol.SPC.toString());
		toClient.flush();
	}

	public void select1BonusPermit() throws IOException {
		toClient.writeObject(SocketProtocol.SBP.toString());
		toClient.flush();
	}
	public void select1BonusToken() throws IOException {
		toClient.writeObject(SocketProtocol.SBT.toString());
		toClient.flush();
	}
	public void select2BonusToken() throws IOException {
		toClient.writeObject(SocketProtocol.STT.toString());
		toClient.flush();	
	}
	public void updateActionPlayer(int mainAction, int secondaryAction) throws IOException {
		toClient.writeObject(SocketProtocol.UAP.toString());
		toClient.writeObject(mainAction);
		toClient.writeObject(secondaryAction);
		toClient.flush();
		
	}
	public void errorInsertedDate() throws IOException {
		toClient.writeObject(SocketProtocol.EID.toString());
		toClient.flush();	
	}
	public void resurceNotEnoght() throws IOException {
		toClient.writeObject(SocketProtocol.RNE.toString());
		toClient.flush();			
	}
	
	public void startMarket(int timeDoOffert) throws IOException {
		toClient.writeObject(SocketProtocol.SMK.toString());
		toClient.writeObject(timeDoOffert);
		toClient.flush();
	}
	public void doAnotherOffert() throws IOException {
		toClient.writeObject(SocketProtocol.ADO.toString());
		toClient.flush();
	}
	public void errorInsertedOffert() throws IOException {
		toClient.writeObject(SocketProtocol.EIO.toString());
		toClient.flush();
	}
	public void resourcesNotEnoughtForOffert() throws IOException {
		toClient.writeObject(SocketProtocol.RNO.toString());
		toClient.flush();
	}
	public void receiveOtherOfferts(String[][] offerts, int timeBuy) throws IOException {
		toClient.writeObject(SocketProtocol.ROO.toString());
		toClient.writeObject(offerts);
		toClient.writeObject(timeBuy);
		toClient.flush();
	}
	public void opponentTurnToBuy(String playerTurn) throws IOException {
		toClient.writeObject(SocketProtocol.OTB.toString());
		toClient.writeObject(playerTurn);
		toClient.flush();
	}
	public void resourcesNotEnoughtToBuyOffert() throws IOException {
		toClient.writeObject(SocketProtocol.RNB.toString());
		toClient.flush();
	}
	public void errorInsertedBought() throws IOException {
		toClient.writeObject(SocketProtocol.EIB.toString());
		toClient.flush();
	}
	public boolean heartClient() throws IOException {
		return false;
	}
	public void finishGame(String[][] rank) throws IOException {
		toClient.writeObject(SocketProtocol.FGM.toString());
		toClient.writeObject(rank);
		toClient.flush();
	}
	public void giveCodeGame(long code) throws IOException {
		toClient.writeObject(SocketProtocol.GCG.toString());
		toClient.writeObject(code);
		toClient.flush();
	}
}
