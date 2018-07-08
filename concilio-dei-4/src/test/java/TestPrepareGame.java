

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

import server.controller.GameManager;
import server.controller.PrepareDataForClient;
import server.model.Game;
import server.model.Player;
import server.model.gameComponent.cards.PermitCard;

public class TestPrepareGame {
	private  Player player1,player2;
	private  Game game;
	private  GameManager gameManager;
	private PrepareDataForClient prepare;
	
	public void testPrepareGame(){
		player1=new Player("Angelo","00", Color.BLACK);
		player2=new Player("Richi","00", Color.RED);
		game=new Game(1,2);
		game.addPlayer(player1);
		game.addPlayer(player2);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		prepare=new PrepareDataForClient(game);
	}
	
	@Test
	public void prepareOfferts(){
		testPrepareGame();
		String[]offert=new String[]{"Angelo","1","1","5"};
		game.setMarket();
		game.addOffert(offert);
		offert=new String[]{"Richi","2","1","7"};
		game.addOffert(offert);
		PermitCard p=game.getRegion(0).pickUpPermitCardVisible(0);
		player1.addPermitCard(p);
		offert=new String[]{"Angelo","0","0","10"};
		game.addOffert(offert);
		String [][]result=prepare.offerts(game.readOfferts());
		String testPermit=p.getAllTowns()+";"+"false";
		String[][]resultIWant=new String[][]{{"Angelo","1", player1.getPoliticalCard(1).getColorName(),"5"},
			{"Richi","2","1","7"},
			{"Angelo","0",testPermit,"10"}};
		assertEquals("prepare offert",resultIWant,result);
	}
}
