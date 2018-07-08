package server.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;

import client.controller.RMIClientInterface;

/**
 * This class communicates with RMIClient using RMIClientInterface
 */
public class ConnectionRMI implements Connection, Serializable{

	transient private RMIClientInterface client;
	public ConnectionRMI(RMIClientInterface client) {
		this.client=client;
	}
	@Override
	public void updatePlayerOnline(int msgout) throws IOException {
			client.updatePlayerOnline(msgout);
			
	}
	@Override
	public void updatePlayerInLobby(int msgout)throws IOException {
		client.updatePlayerInLobby(msgout);
	}
	@Override
	public void startGame() throws IOException{
		client.startGame();
	}
	@Override
	public void statusMap(String[][] map, String[] nameRegion, String whereIsKing) throws IOException{
		client.statusMap(map, nameRegion, whereIsKing);
	}
	@Override
	public void statusVisiblePermitCards(String[][] permitCardForRegion) throws IOException {
		client.statusVisiblePermitCards(permitCardForRegion);
	}
	@Override
	public void statusKingBoard(String[][] councilBalconyName, Color[][] councilBalconyColor, int[] kingReward,
			String[][] bonusReward, String[] councillorNameOutOfBalcony, Color[] councillorColorOutOfBalcony)
			throws IOException {
		client.statusKingBoard(councilBalconyName, councilBalconyColor, kingReward, bonusReward, councillorNameOutOfBalcony, councillorColorOutOfBalcony);
	}
	@Override
	public void statusNobilityRoad(int[][] bonusNobilityRoad) throws IOException {
		client.statusNobilityRoad(bonusNobilityRoad);
	}
	@Override
	public void statusOpponents(String[][] statusOpponent, Color[] colorOpponent) throws IOException {
		client.statusOpponents(statusOpponent, colorOpponent);
	}
	@Override
	public void statusPlayer(String[] status, Color c, String[] politicalCards, Color[] politicalCardsColors,
			String[][] permitCardsTowns) throws IOException {
		client.statusPlayer(status, c, politicalCards, politicalCardsColors, permitCardsTowns);
	}
	@Override
	public void selectPermitCard() throws IOException{
		client.selectPermitCard();
	}
	@Override
	public void select1BonusPermit()throws IOException {
		client.select1BonusPermit();
	}
	@Override
	public void select1BonusToken() throws IOException{
		client.select1BonusToken();
	}
	@Override
	public void select2BonusToken() throws IOException{
		client.select2BonusToken();
	}
	@Override
	public void statusTurn(String namePlayerTurn, int timeTurn) throws IOException {
		client.statusTurn(namePlayerTurn,timeTurn);
	}
	@Override
	public void updateActionPlayer(int mainAction, int secondaryAction) throws IOException {
		client.updateActionPlayer(mainAction, secondaryAction);
	}
	@Override
	public void errorInsertedDate() throws IOException {
		client.errorInsertedDate();
	}
	@Override
	public void resurceNotEnoght() throws IOException {
		client.resurceNotEnoght();
	}
	//MARKET
	@Override
	public void startMarket(int timeDoOffert) throws IOException {
		client.startMarket(timeDoOffert);
	}
	@Override
	public void doAnotherOffert() throws IOException {
		client.doAnotherOffert();
	}
	@Override
	public void errorInsertedOffert() throws IOException {
		client.errorInsertedOffert();
	}
	@Override
	public void resourcesNotEnoughtForOffert() throws IOException {
		client.resourcesNotEnoughtForOffert();
	}
	@Override
	public void receiveOtherOfferts(String[][] offerts, int timeBuy) throws IOException {
		client.receiveOtherOfferts(offerts, timeBuy);
	}
	@Override
	public void opponentTurnToBuy(String playerTurn) throws IOException {
		client.opponentTurnToBuy(playerTurn);
	}

	@Override
	public void resourcesNotEnoughtToBuyOffert() throws IOException {
		client.resourcesNotEnoughtToBuyOffert();
	}
	@Override
	public void errorInsertedBought() throws IOException {
		client.errorInsertedBought();
	}
	@Override
	public boolean heartClient() throws IOException {
		return client.heartClient();
	}
	@Override
	public void finishGame(String[][]rank) throws IOException {
		client.finishGame(rank);
	}
	@Override
	public void giveCodeGame(long code) throws IOException {
		client.giveCodeGame(code);
	}
}
