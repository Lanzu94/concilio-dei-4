package client.controller;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

import server.controller.RMIServerInterface;

/**
 * In this class I call the methods of RMI_Server
 */
public class ClientConnectionRMI implements ClientConnection, Serializable{

	transient RMIServerInterface interfaceServer;
	public ClientConnectionRMI(RMIServerInterface server) {
		this.interfaceServer=server;
	}
	@Override
	public int player_connect(String nome, String password, RMIClientInterface rmi_clientin) throws IOException {
		return interfaceServer.player_connect(nome, password, rmi_clientin);
	}
	@Override
	public long newGame(int min, int max, String name, int map) throws IOException {
		return interfaceServer.newGame(min, max, name, map);
	}
	@Override
	public void newConnectionToClient(String name) throws IOException {
		interfaceServer.newConnectionToClient(name);
	}
	@Override
	public String[][][] listOfMaps() throws IOException {
		return interfaceServer.listOfMaps();
	}
	@Override
	public long[][] listOfGames() throws IOException {
		return interfaceServer.listOfGames();
	}
	@Override
	public int addPlayerToGame(long codeGame, String name) throws IOException {
		return interfaceServer.addPlayerToGame(codeGame, name);
	}
	
	@Override
	public void changeCouncillor(long codeGame, int councillorChosen, int balcony) throws IOException {
		interfaceServer.changeCouncillor(codeGame, councillorChosen, balcony);
		
	}

	@Override
	public void buyPermitCard(long codeGame, int[] politicCardsUse, int whichBalcony, int permitCardVisible)
			throws IOException {
		interfaceServer.buyPermitCard(codeGame, politicCardsUse, whichBalcony, permitCardVisible);
	}

	@Override
	public void buildEmporiumByPermit(long codeGame, int indexPermitCard, String townNameChosen)
			throws IOException {
		interfaceServer.buildEmporiumByPermit(codeGame, indexPermitCard, townNameChosen);
	}

	@Override
	public void buildEmporiumByKing(long codeGame, int[] politicalCardUsed, String nameTown, String route)
			throws IOException {
		interfaceServer.buildEmporiumByKing(codeGame, politicalCardUsed, nameTown, route);
	}

	@Override
	public void engangeHelper(long codeGame) throws IOException {
		interfaceServer.engangeHelper(codeGame);		
	}

	@Override
	public void changePermitCard(long codeGame, int region) throws IOException {
		interfaceServer.changePermitCard(codeGame, region);
	}

	@Override
	public void changeCouncillorByHelper(long codeGame, int councillorChosen, int balcony) throws IOException {
		interfaceServer.changeCouncillorByHelper(codeGame, councillorChosen, balcony);
	}

	@Override
	public void otherMainAction(long codeGame) throws IOException {
		interfaceServer.otherMainAction(codeGame);	
	}

	@Override
	public void selectOneBonusToken(long codeGame, String token) throws IOException {
		interfaceServer.selectOneBonusToken(codeGame, token);		
	}

	@Override
	public void selectTwoBonusToken(long codeGame, String token1, String token2) throws IOException {
		interfaceServer.selectTwoBonusToken(codeGame, token1, token2);		
	}

	@Override
	public void selectBonusPermitCard(long codeGame, int bonusPermit) throws IOException {
		interfaceServer.selectBonusPermitCard(codeGame, bonusPermit);
	}

	@Override
	public void selectPermitCard(long codeGame, int permitCard) throws IOException {
		interfaceServer.selectPermitCard(codeGame, permitCard);		
	}

	@Override
	public void passTurn(long codeGame) throws IOException {
		interfaceServer.passTurn(codeGame);		
	}

	@Override
	public void addOffert(long codeGame, String[] offert) throws IOException {
		interfaceServer.addOffert(codeGame, offert);		
	}

	@Override
	public void finishDoOffer(long codeGame) throws IOException {
		interfaceServer.finishDoOffer(codeGame);
	}

	@Override
	public void buyOffert(long codeGame, int offert) throws IOException {
		interfaceServer.buyOffert(codeGame, offert);
	}

	@Override
	public void passMarketTurn(long codeGame) throws IOException {
		interfaceServer.passMarketTurn(codeGame);		
	}
}
