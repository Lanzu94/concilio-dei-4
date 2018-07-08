package client.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;

import client.model.EnumPage;
import server.StartServer;
import server.model.SocketProtocol;

/**
 * In this class I pass the data to Socket_Server
 */
public class ClientConnectionSocket implements ClientConnection, Serializable{
	transient private ObjectOutputStream toServer;
	transient private ObjectInputStream fromServer;
	
	public ClientConnectionSocket(ObjectOutputStream toServer, ObjectInputStream fromServer) {
		this.toServer=toServer;
		this.fromServer=fromServer;
	}
	//the following methods aren't used in socket mode
	@Override
	public int player_connect(String nome, String password, RMIClientInterface rmi_clientin) throws IOException {
		return 0;
	}
	@Override
	public void newConnectionToClient(String name) throws IOException {
		
	}
	//end methods not used

	@Override
	public long newGame(int min, int max, String name, int map) throws IOException {
		String msgout=SocketProtocol.CNG.toString()+min+"-"+max+"-"+map+"-"+name;
		toServer.writeObject(msgout);
		toServer.flush();
		return 0;
	}

	@Override
	public long[][] listOfGames() throws IOException {
		try {
			String msgout=SocketProtocol.LOL.toString();
			toServer.writeObject(msgout);
			toServer.flush();
		} catch (Exception e) {
			System.out.println("ERROR="+e);
		}
		return null;
	}

	@Override
	public int addPlayerToGame(long codeGame, String name) throws IOException {
		toServer.writeObject(SocketProtocol.JAL.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(name);
		toServer.flush(); 	
		return -1;
	}

	

	@Override
	public void changeCouncillor(long codeGame, int councillorChosen, int balcony) throws IOException {
		toServer.writeObject(SocketProtocol.CHC.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(councillorChosen);
		toServer.writeObject(balcony);
		toServer.flush(); 	
	}

	@Override
	public void buyPermitCard(long codeGame, int[] politicCardsUse, int whichBalcony, int permitCardVisible)
			throws IOException {
		toServer.writeObject(SocketProtocol.BPC.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(politicCardsUse);
		toServer.writeObject(whichBalcony);
		toServer.writeObject(permitCardVisible);
		toServer.flush(); 	
	}

	@Override
	public void buildEmporiumByPermit(long codeGame, int indexPermitCard, String townNameChosen)
			throws IOException {
		toServer.writeObject(SocketProtocol.BEP.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(indexPermitCard);
		toServer.writeObject(townNameChosen);
		toServer.flush();
		
	}

	@Override
	public void buildEmporiumByKing(long codeGame, int[] politicalCardUsed, String nameTown, String route)
			throws IOException {
		toServer.writeObject(SocketProtocol.BEK.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(politicalCardUsed);
		toServer.writeObject(nameTown);
		toServer.writeObject(route);
		toServer.flush();
	}

	@Override
	public void engangeHelper(long codeGame) throws IOException {
		toServer.writeObject(SocketProtocol.EAH.toString()+codeGame);
	}

	@Override
	public void changePermitCard(long codeGame, int region) throws IOException {
		toServer.writeObject(SocketProtocol.CPC.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(region);
		toServer.flush();
	}

	@Override
	public void changeCouncillorByHelper(long codeGame, int councillorChosen, int balcony) throws IOException {
		toServer.writeObject(SocketProtocol.CCH.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(councillorChosen);
		toServer.writeObject(balcony);
		toServer.flush();
	}

	@Override
	public void otherMainAction(long codeGame) throws IOException {
		toServer.writeObject(SocketProtocol.OMA.toString()+codeGame);
		toServer.flush();
	}

	@Override
	public void selectOneBonusToken(long codeGame, String token) throws IOException {
		toServer.writeObject(SocketProtocol.SBT.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(token);
		toServer.flush();
	}

	@Override
	public void selectTwoBonusToken(long codeGame, String token1, String token2) throws IOException {
		toServer.writeObject(SocketProtocol.STT.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(token1);
		toServer.writeObject(token2);
		toServer.flush();
	}

	@Override
	public void selectBonusPermitCard(long codeGame, int bonusPermit) throws IOException {
		toServer.writeObject(SocketProtocol.SBP.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(bonusPermit);
		toServer.flush();
	}

	@Override
	public void selectPermitCard(long codeGame, int permitCard) throws IOException {
		toServer.writeObject(SocketProtocol.SPC.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(permitCard);
		toServer.flush();
	}

	@Override
	public void passTurn(long codeGame) throws IOException {
		toServer.writeObject(SocketProtocol.PTT.toString());
		toServer.writeObject(codeGame);
		toServer.flush();
	}

	@Override
	public void addOffert(long codeGame, String[] offert) throws IOException {
		toServer.writeObject(SocketProtocol.ADO.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(offert);
		toServer.flush();
	}

	@Override
	public void finishDoOffer(long codeGame) throws IOException {
		toServer.writeObject(SocketProtocol.FOF.toString());
		toServer.writeObject(codeGame);
		toServer.flush();
	}

	@Override
	public void buyOffert(long codeGame, int offert) throws IOException {
		toServer.writeObject(SocketProtocol.BOF.toString());
		toServer.writeObject(codeGame);
		toServer.writeObject(offert);
		toServer.flush();
	}

	@Override
	public void passMarketTurn(long codeGame) throws IOException {
		toServer.writeObject(SocketProtocol.PMT.toString()+codeGame);
		toServer.flush();
	}

	@Override
	public String[][][] listOfMaps() throws IOException {
		toServer.writeObject(SocketProtocol.LOM.toString());
		toServer.flush();
		return null;
	}

	
}
