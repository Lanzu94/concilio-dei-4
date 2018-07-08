

import java.awt.Color;

import server.controller.GameManager;
import server.model.Game;
import server.model.Player;

public class TestGameManager {
	public static void main( String[] args )
    {
		testaLettura();
    }
	private static void testaLettura()
	{
		Game g=new Game(2,4);
		Player p1=new Player("Angelo","00", Color.BLACK);
		Player p2=new Player("Angelo","00", Color.WHITE);
		g.addPlayer(p1);
		g.addPlayer(p2);
		GameManager gameManager=new GameManager(g);
		gameManager.createGame();
		
		
	}
}
