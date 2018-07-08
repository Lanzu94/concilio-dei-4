

import  org.junit.Assert.*;

import static org.junit.Assert.*;

import java.awt.Color;
import java.rmi.RemoteException;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import client.model.LocalStateGame;
import server.controller.GameManager;
import server.controller.PrepareDataForClient;
import server.controller.RMIControllerServer;
import server.model.gameComponent.Reward;
import server.model.gameComponent.cards.KingReward;
import server.model.gameComponent.cards.PermitCard;
import server.model.gameComponent.cards.PoliticalCard;
import server.model.gameComponent.kingboard.CouncilBalcony;
import server.model.gameComponent.kingboard.Councillor;
import server.model.gameComponent.kingboard.KingBoard;
import server.model.gameComponent.map.Region;
import server.model.gameComponent.map.Towns;
import server.model.gameComponent.map.TypeOfRegion;
import server.model.gameComponent.cards.*;
import server.model.*;


public class TestActionGame {
	private  Player player1,player2,player3;
	private  Game game;
	private  GameManager gameManager;
	private  RMIControllerServer server;
	private  StatusServer statusServer; 
	private  PrepareDataForClient prepareGame;
	
	public void setTestActionGame(){
		player1=new Player();
		player2=new Player();
		game=new Game(1,2);
		game.addPlayer(player1);
		game.addPlayer(player2);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		
		PoliticalCard p=new PoliticalCard(Color.RED);
		while(player1.politicalDeckSize()>0)
			player1.removePoliticalCard(0);
		p.setColorName("RED");
		
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		CouncilBalcony kb=game.getKingBoard().getCouncilBalcony(0);
		Councillor c=new Councillor();
		c.setColorName("RED");
		kb.setUpCouncilBalcony(new Councillor[]{c,c,c,c});
	}
	final static private int error=-1;
	final static private int noResource=-2;
	final static private int ok=0;
	@Test
	public void testAction1(){
		int a;
		int countTest=1;
		//1 correct action
		setTestActionGame();
		a=gameManager.electCouncillor(0,0);
		assertEquals(countTest+"",ok,a);
		//2
		countTest++;
		setTestActionGame();
		KingBoard b=game.getKingBoard();
		Councillor c0=new Councillor();
		Councillor c1 =new Councillor();
		Councillor c2 =new Councillor();
		c0.setColorName("RED"); 
		c1.setColorName("GREY");
		c2.setColorName("BLUE");
		Councillor d= new Councillor();
		d.setColorName("GREEN");
		CouncilBalcony cb1= game.getKingBoard().getCouncilBalcony(0);
		CouncilBalcony cb2= game.getKingBoard().getCouncilBalcony(1);
		cb1.setUpCouncilBalcony(new Councillor[] {c0,c1,c2,c0});
		cb2.setUpCouncilBalcony(new Councillor[] {d,c1,c0,d});
		b.addCouncilBalcony(cb1);
		b.addCouncilBalcony(cb2);
		a=gameManager.electCouncillor(1,2);
		assertEquals(countTest+"",ok,a);
		//3 councillorIndex> number councillors out of balcony
		a=gameManager.electCouncillor(10,5);
		assertEquals(countTest+"",error,a);
		//4 councillorINdex> number of councillors in the balcony
		countTest++;
		setTestActionGame();
		a=gameManager.electCouncillor(10,20);
		assertEquals(countTest+"",error,a);
	}
	
	@Test
	public void testAction2 (){
				
		int b;
		int countTest=1;
		setTestActionGame();
		//1 correct action
		b=gameManager.buyPermitCard(new int[] {0, 1, 2, 3} ,0,0);
		assertEquals(countTest+"", ok, b);
		//2
		countTest++;
		setTestActionGame();
		b=gameManager.buyPermitCard(new int[] {0, 1, 2} ,0,0);
		assertEquals(countTest+"", ok, b);
		//3
		countTest++;
		setTestActionGame();
		b=gameManager.buyPermitCard(new int[] {0} ,0,0);
		assertEquals(countTest+"", ok, b);
		//4 player has inserted wrong cards
		countTest++;
		setTestActionGame();
		b=gameManager.buyPermitCard(new int[] {} ,0,0);
		assertEquals(countTest+"", error, b);
		//5 the player hasn't enough money
		countTest++;
		setTestActionGame();
		player1.addMoney(-100);
		b=gameManager.buyPermitCard(new int[] {0} ,0,0);
		assertEquals(countTest+"", noResource, b);
		//5 politic card is > than politic deck
		countTest++;
		setTestActionGame();
		b=gameManager.buyPermitCard(new int[] {0, 1, 2, 4} ,0,0);
		assertEquals(countTest+"", error, b);
		//6
		countTest++;
		setTestActionGame();
		b=gameManager.buyPermitCard(new int[] {0, 1, 2, 3, 5} ,0,0);
		assertEquals(countTest+"", error, b);
		//7 player has inserted wrong card
		countTest++;
		setTestActionGame();
		PoliticalCard p=new PoliticalCard(Color.RED);
		while(player1.politicalDeckSize()>0)
			player1.removePoliticalCard(0);
		p.setColorName("RED");
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		p=new PoliticalCard(Color.BLACK);
		p.setColorName("BLACK");
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		b=gameManager.buyPermitCard(new int[] {0, 1, 2, 3} ,0,0);
		assertEquals(countTest+"", error, b);
		//8
		countTest++;
		setTestActionGame();
		p=new PoliticalCard(Color.RED);
		while(player1.politicalDeckSize()>0)
			player1.removePoliticalCard(0);
		p.setColorName("RED");
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		p=new PoliticalCard();
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		b=gameManager.buyPermitCard(new int[] {0, 1, 2, 3} ,0,0);
		assertEquals(countTest+"", ok, b);
		//9 correct values
		countTest++;
		p=new PoliticalCard();
		while(player1.politicalDeckSize()>0)
			player1.removePoliticalCard(0);
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		setTestActionGame();
		b=gameManager.buyPermitCard(new int[] {0, 3, 1} ,0,0);
		assertEquals(countTest+"", ok, b);
		//10 out of region
		countTest++;
		setTestActionGame();
		b=gameManager.buyPermitCard(new int[] {0, 1, 2, 3} ,4,0);
		assertEquals(countTest+"", error, b);
	}
	@Test
	public void testAction3(){ 
		int count=1;
		//1 the player does a correct action
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		game.addPlayer(player1);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		PermitCard p= new PermitCard();
		PermitCard c=new PermitCard();
		p.addTown("A");
		c.addTown("C");
		player1.addPermitCard(p);
		player1.addPermitCard(p);
		int a=gameManager.buildEmporiumByPermit(0, "A");
		assertEquals(count+"", ok, a);
		//2 the town inserted doesn't exist in the map 
		count++; 
		a=gameManager.buildEmporiumByPermit(0, "Z");
		assertEquals(count+"", error, a);
		//3 the player has built an emporium before
		count++;
		Towns town=game.getRegion(0).getTown(0);
		town.newEmporium(player1);
		a=gameManager.buildEmporiumByPermit(0, "A");
		assertEquals(count+"", error, a);
		//4 the player hasn't enough helpers
		count++;
		player1.addPermitCard(c);
		player1.addHelper(-10);
		a=gameManager.buildEmporiumByPermit(0, "C");
		assertEquals(count+"", noResource, a);
		//5 the player hasn't any card
		count++;
		player1.addHelper(50);
		while (player1.permitDeckSizeAvailable()>0)
			player1.removePermitCard(0);
		a=gameManager.buildEmporiumByPermit(0, "C");
		assertEquals(count+"", error, a);
		//6 the player hasn't the permit card
		count++;
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		game.addPlayer(player1);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		p= new PermitCard();
		player1.addPermitCard(p);
		player1.addPermitCard(p);
		a=gameManager.buildEmporiumByPermit(0, "C");
		assertEquals(count+"", error, a);
		
	}
	@Test 
	public void testAction4(){ //codice ripetuto:(townWhereToBuild.howManyEmporium()>player.getHelper()) 

		int count=1;
		//1 correct values
		player1=new Player("Angelo","00", Color.BLACK);
		player2=new Player("Luca", "00", Color.red);
		game=new Game(1,2);
		game.addPlayer(player1);
		game.addPlayer(player2);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		
		PoliticalCard p=new PoliticalCard(Color.RED);
		while(player1.politicalDeckSize()>0)
			player1.removePoliticalCard(0);
		p.setColorName("RED");
		
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		CouncilBalcony kb=game.getKingBoard().getCouncilBalcony(3);
		Councillor c=new Councillor();
		c.setColorName("RED");
		kb.setUpCouncilBalcony(new Councillor[]{c,c,c,c});
		int b;
		int countTest=1;
		
		b=gameManager.buildEmporiumByKing(new int[] {0, 1, 2, 3} ,"C","JIFC");
		assertEquals(countTest+"", ok, b);
		//2 politic card > politic card deck
		count++;
		while(player1.politicalDeckSize()>0)
			player1.removePoliticalCard(0);
		b=gameManager.buildEmporiumByKing(new int[] {0, 1, 2, 3} ,"C","JIFC");
		assertEquals(countTest+"", error, b);
		//3 town doesn't exist
		count++;
		setTestActionGame();
		b=gameManager.buildEmporiumByKing(new int[] {0, 1, 2, 3} ,"Z","JIFC");
		assertEquals(countTest+"", error, b); 
		//4 there is already an emporium 
		count++;
		setTestActionGame();
		Towns town=game.getRegion(0).getTown(0);
		town.newEmporium(player1);
		b=gameManager.buildEmporiumByKing(new int[] {0, 1, 2, 3} ,"A","JIFC");
		assertEquals(countTest+"", error, b);
		//5 the player hasn't the condition to buy a permit card
		count++;
		setTestActionGame();
		while(player1.politicalDeckSize()>0)
			player1.removePoliticalCard(0);
		PoliticalCard c1= new PoliticalCard(Color.GREEN);
		c1.setColorName("GREEN");
		player1.addPoliticalCard(c1);
		player1.addPoliticalCard(c1);
		player1.addPoliticalCard(c1);
		player1.addPoliticalCard(c1);
		kb=game.getKingBoard().getCouncilBalcony(3);
		c=new Councillor();
		c.setColorName("RED");
		kb.setUpCouncilBalcony(new Councillor[]{c,c,c,c});
		b=gameManager.buildEmporiumByKing(new int[] {0, 1, 2, 3} ,"C","JIFC");
		assertEquals(countTest+"", error, b);
		//6 there aren't enough helper
		count++;
		setTestActionGame();
		p=new PoliticalCard(Color.RED);
		while(player1.politicalDeckSize()>0)
			player1.removePoliticalCard(0);
		p.setColorName("RED");
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		player1.addPoliticalCard(p);
		kb=game.getKingBoard().getCouncilBalcony(3);
		c=new Councillor();
		c.setColorName("RED");
		kb.setUpCouncilBalcony(new Councillor[]{c,c,c,c});
		player1.addHelper(-100);
		b=gameManager.buildEmporiumByKing(new int[] {0, 1, 2, 3} ,"C","JIFC");
		assertEquals(countTest+"", noResource, b);
		//7 insert wrong cards
		count++;
		player1.addHelper(101);
		b=gameManager.buildEmporiumByKing(new int[] {} ,"C","JIFC");
		assertEquals(countTest+"", error, b);
		//8 not enough money
		count++;
		player1.addMoney(-100);
		b=gameManager.buildEmporiumByKing(new int[] {0, 1, 2, 3} ,"C","JIFC");
		assertEquals(countTest+"", noResource, b);
		//route not valid
		player1.addMoney(150);
		b=gameManager.buildEmporiumByKing(new int[] {0, 1, 2, 3} ,"C","ABCD");
		assertEquals(countTest+"", error, b);
	}
	
	@Test
	public void testAction5(){
		//to engange an Helper
		setTestActionGame();
		int b=gameManager.engangeAnHelper();
		assertEquals("OK", ok, b);
		assertTrue("Helper", (player1.getHelper()==2 || player1.getHelper()==3));
		//not enough money
		setTestActionGame();
		player1.addMoney(-10);
		b=gameManager.engangeAnHelper();
		assertEquals("Not Resource", noResource, b);
	}
	@Test
	public void testAction6(){
		//change permit card visible
		setTestActionGame();
		int b=gameManager.changePermitCard(0);
		assertEquals("OK", ok, b);
		//not enough helper
		player1.addHelper(-10);
		b=gameManager.changePermitCard(0);
		assertEquals("No Resource", noResource, b);
		//region not valid
		player1.addHelper(20);
		b=gameManager.changePermitCard(5);
		assertEquals("Error", error,b);
	}
	@Test
	public void testAction7(){
		//elect councillor by helper
		setTestActionGame();
		//1
		int countTest=1;
		player1.addHelper(2);
		int b=gameManager.electCouncillorByAnHelper(0,0);
		assertEquals("OK", ok, b);
		//2 
		countTest++ ;
		b=gameManager.electCouncillorByAnHelper(5,5);
		assertEquals(countTest+"",error,b);
		//3
		countTest++;
		player1.addHelper(-50);
		b=gameManager.electCouncillorByAnHelper(0,0);
		assertEquals(countTest+"", noResource, b);
	}
	@Test
	public void testAction8(){
		setTestActionGame();
		player1.addHelper(5);
		int b=gameManager.doNewMainAction();
		assertEquals("OK", ok, b);
		setTestActionGame();
		player1.addHelper(-5);
		b=gameManager.doNewMainAction();
		assertEquals("No Resource", noResource, b);
	}
	
	// maps and components
	@Test
	public void map(){
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		game.addPlayer(player1);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		//connection
		String [][] a= prepareGame.map();
		String[][] map=new String [15][8];
		map[0][0]="BC";
		assertEquals("ok",map[0][0],a[0][0]);
		//rewards
		map[0][1]="1";
		Reward r= new Reward();
		r.setMoney(1);
		Towns t = game.getRegion(0).getTown(0);
		t.setRewardCity(r);
		prepareGame= new PrepareDataForClient(game);
		a= prepareGame.map();
		assertEquals("ok",map[0][1],a[0][1]);
		//emporium
		t.newEmporium(player1);
		map[0][2]="lanzu";
		prepareGame= new PrepareDataForClient(game);
		a= prepareGame.map();
		assertEquals("ok",map[0][2],a[0][6]);
	}
	
	@Test
	public void nameRegion(){
		setTestActionGame();
		Region r= new Region(1);
		game.addRegion(r);
		prepareGame= new PrepareDataForClient(game);
		String []a= prepareGame.nameRegion();
		assertEquals("OK",TypeOfRegion.HILL.toString(), a[3]);
	}

	public void colorBalconyColor(){
		game=new Game(1,2);
		gameManager=new GameManager(game);
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		KingBoard b= new KingBoard();
		Region r=new Region(0);
		CouncilBalcony c= new CouncilBalcony(game.getRegion(0).getName());
		Councillor coun= new Councillor();
		coun.setColor(Color.GRAY);
		coun.setColorName("GRAY");
		Councillor[] cl= new Councillor[4];
		int i=0;
		while (i<4)
		{
			cl[i]=coun;
			i++;
		}
		c.setUpCouncilBalcony(cl);
		b.addCouncilBalcony(c);
		b.addCouncilBalcony(c);
		game.getKingBoard();
		//color
		Color[][]a= prepareGame.councilBalconyColor();
		assertEquals("OK", Color.GRAY, a[0][0]);
		//name color
		String [][] a2= prepareGame.councilBalconyNameColor();
		assertEquals("OK", "GRAY", a2[0][0]);
		
	}
	
	@Test
	public void visiblePermitCard(){
		game=new Game(1,2);
		gameManager=new GameManager(game);
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		Region r= new Region(0);
		PermitCard p= new PermitCard();
		Reward reward = new Reward();
		reward.setHelper(10);
		p.addTown("A");
		p.setReward(reward);
		r.addPermitCard(p);
		r.addPermitCard(p);
		r.addPermitCard(p);
		r.addPermitCard(p);
		r.setUpPermitCard();
		game.addRegion(r);
		PermitCard [] permits=game.getRegion(0).getPermitCardsVisible();
		String [][]a= prepareGame.visiblePermitCard();
		//right town
		assertEquals("OK", "A",a[0][0]);
		//right reward
		assertEquals("OK", "10", a[0][2]);	
	}

	@Test
	public void kingReward(){
		game=new Game(1,2);
		gameManager=new GameManager(game);
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		KingBoard b= new KingBoard();
		KingReward r= new KingReward(1);
		b.addKingReward(r);
		game.setKingBoard(b);
		int [] a= prepareGame.kingReward();
		assertEquals("OK", 1, a[0]);
	}
	
	@Test
	public void bonusReward (){
		game=new Game(1,2);
		gameManager=new GameManager(game);
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		KingBoard b= new KingBoard();
		BonusReward r= new BonusReward(10);
		r.addTown("A");
		b.addBonusReward(r);
		game.setKingBoard(b);
		String[][] a= prepareGame.bonusReward();
		//name town
		assertEquals("OK", "A",a[0][0]);
		//reward
		assertEquals("ok", "10", a[0][1]);
	}
	
	@Test
	public void colorConsillorOutOfBalcony(){
		game=new Game(1,2);
		gameManager=new GameManager(game);
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		KingBoard b= new KingBoard();
		Councillor coun= new Councillor();
		coun.setColor(Color.GRAY);
		coun.setColorName("GRAY");
		b.addCouncillorOutOfBalconies(coun);
		game.setKingBoard(b);
		//name color
		String[]a= prepareGame.councillorNameColorOutOfBalcony();
		assertEquals("ok", "GRAY", a[0]);
		//color
		Color[]c= prepareGame.councillorColorOutOfBalcony();
		assertEquals("ok", Color.GRAY, c[0]);
	}

	@Test
	public void nobiliyRoad(){
		game=new Game(1,2);
		gameManager=new GameManager(game);
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		Reward r= new Reward();
		r.setMoney(100);
		r.setNobility(2);
		game.addNobilityBonus(r);
		int[][]a= prepareGame.nobilityRoad();
		assertEquals("ok", 100, a[0][0]);
		assertEquals("ok", 2, a[0][3]);
	}
	
	public void pushInCouncillor(){
		setTestActionGame();
		KingBoard kb=game.getKingBoard();
		CouncilBalcony cb= new CouncilBalcony("plain");
		Councillor c= new Councillor();
		c.setColor(Color.RED);
		Councillor c2= new Councillor();
		c2.setColor(Color.ORANGE);
		Councillor[] councillors= new Councillor[4];
		councillors[0]=c;
		councillors[1]=c2;
		councillors[2]=c;
		councillors[3]=c2;
		cb.setUpCouncilBalcony(councillors);
		kb.addCouncilBalcony(cb);
		Councillor a=cb.pushInCouncillor(c);
		assertEquals("ok", c2.getColor(), a.getColor());
		
	}
	
	
	//player status
	@Test
	public void numberOfPlayersOnline(){
		player1=new Player();
		player2=new Player();
		game=new Game(1,2);
		game.addPlayer(player1);
		game.addPlayer(player2);
		player1.setStatusPlayer(EnumStatePlayer.inGame);
		player2.setStatusPlayer(EnumStatePlayer.inGame);
		int a= game.numberOfPlayersOnline();
		assertEquals("OK", 2, a);
		player3= new Player();
		game.addPlayer(player3);
		player3.setStatusPlayer(EnumStatePlayer.disconnected);
		a= game.numberOfPlayersOnline();
		assertEquals("OK", 2, a);
	}
	
	@Test
	public void statusOpponent(){
		game=new Game(1,2);
		gameManager=new GameManager(game);
		player1=new Player("molly","00", Color.ORANGE);
		player2=new Player("polly", "00", Color.BLACK);
		game.addPlayer(player1);
		game.addPlayer(player2);
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		player2.addMoney(23);
		PoliticalCard p= new PoliticalCard();
		player2.addPoliticalCard(p);
		player2.addPoliticalCard(p);
		player2.addPoliticalCard(p);
		String[][] a= prepareGame.statusOpponents("molly");
		//checkname
		assertEquals("OK", "polly", a[0][0]);
		//check resource
		assertEquals("OK", "23", a[0][1]);
		assertEquals("OK", "3", a[0][6]);
		//color opponent
		Color[] c= prepareGame.colorOpponents("polly");
		assertEquals("ok", Color.ORANGE, c[0]);
	}
	
	@Test
	public void playerStatus(){
		game=new Game(1,2);
		gameManager=new GameManager(game);
		player1=new Player("molly","00", Color.ORANGE);
		player2=new Player("polly", "00", Color.BLACK);
		game.addPlayer(player1);
		game.addPlayer(player2);
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		player1.addHelper(12);
		PoliticalCard c= new PoliticalCard(Color.WHITE);
		c.setColorName("WHITE");
		PermitCard p= new PermitCard();
		player1.addPermitCard(p);
		player1.addPoliticalCard(c);
		player1.addPoliticalCard(c);
		//right resources
		String[]a= prepareGame.playerStatus(player1);
		assertEquals("OK","12",a[1]);
		//color political card (name,color)
		String[]b= prepareGame.playerPoliticalCardsColorName(player1);
		assertEquals("OK","WHITE",b[0]);
		Color[]color= prepareGame.playerPoliticalCardsColor(player1);
		assertEquals("OK",Color.WHITE,color[0]);
		
	}

	@Test
	public void playerPermitCards(){
		game=new Game(1,2);
		gameManager=new GameManager(game);
		player1=new Player("molly","00", Color.ORANGE);
		game.addPlayer(player1);
		game.setWhoseTurn(0);
		prepareGame= new PrepareDataForClient(game);
		PermitCard p= new PermitCard();
		Reward r= new Reward();
		r.setNobility(24);
		p.addTown("A");
		p.setReward(r);
		player1.addPermitCard(p);
		String a[][]= prepareGame.playerPermitCards(player1);
		//name town
		assertEquals("OK", "A", a[0][1]);
		//resource
		assertEquals("OK", "24", a[0][5]);
		
	}
	
	//REWARD

	@Test
	public void selectOneBonusToken(){ 
		setTestActionGame();
		int count=1;
		int a;
		a=gameManager.selectOneBonusToken("A");
		assertEquals(count+"",ok,a);
		//2 town not found
		count++;
		a=gameManager.selectOneBonusToken("Z");
		assertEquals(count+"",error,a); 
		//3 the player has already an emporium 
		count++;
		setTestActionGame();
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		game.addPlayer(player1);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		game.getRegion(0).getTown(2).newEmporium(player1);//C
		a=gameManager.selectOneBonusToken("C");
		assertEquals(count+"",error,a); 
	}
	
	@Test
	public void selectTwoBonusToken(){ 
		setTestActionGame();
		int count=1;
		int a;
		a=gameManager.selectTwoBonusToken("A", "B");
		assertEquals(count+"",ok,a);
		//2 the town doesn't exist or the token in the town
		a=gameManager.selectTwoBonusToken("B", "Z");
		assertEquals(count+"",error,a);  
		//3 the player has already an emporium 
		count++;
		setTestActionGame();
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		game.addPlayer(player1);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		game.getRegion(0).getTown(2).newEmporium(player1);//C
		a=gameManager.selectOneBonusToken("C");
		assertEquals(count+"",error,a); 
		
	}
	@Test
	
	public void selectBonusPermitCard(){
		int a;
		setTestActionGame();
		PermitCard p= new PermitCard();
		player1.addPermitCard(p);
		player1.addPermitCard(p);
		a=gameManager.selectBonusPermitCard(0);
		assertEquals("OK",ok,a);
		//bonusPermit>=permitDeckSize
		while(player1.permitDeckSize()>0)
			player1.removePermitCard(0);
		a=gameManager.selectBonusPermitCard(0);
		assertEquals("ERROR",error,a); 
		//values not valid
		a=gameManager.selectBonusPermitCard(12443);
		assertEquals("ERROR",error,a); 
	}
	@Test
	public void selectPermitCard(){
		int a;
		setTestActionGame();
		a=gameManager.selectPermitCard(0);
		assertEquals("OK",ok,a);
		//2 permitCardsForRegion>=game.sizeRegion() 
		game=new Game(1,2);
		gameManager=new GameManager(game);
		Region r= new Region(0);
		game.addRegion(r); //only one region, while permitCards are 2
		a=gameManager.selectPermitCard(0);
		assertEquals("ERROR",error,a);

	}
	
	
	
	//MARKET
	
	@Test
	public void buyOffert(){
		int a; int count=1;
		//correct values
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		player2= new Player ("angie", "fucile", Color.RED);
		game.addPlayer(player1);
		game.addPlayer(player2);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		player1.addMoney(10);
		a=gameManager.buyOffert(new String[] {"angie","2","4","5"});
		assertEquals(count+"",ok,a);
		//2 the player does an offer to himself
		count++;
		a=gameManager.buyOffert(new String[] {"lanzu","2","4","5"});
		assertEquals(count+"",error,a);
		//3 the player hasn't enough money
		count++;
		player1.addMoney(-50);
		a=gameManager.buyOffert(new String[] {"angie","2","4","5"});
		assertEquals(count+"",noResource,a);
		//4 choose permit card
		count++;
		player1.addMoney(100);
		PermitCard p= new PermitCard();
		player2.addPermitCard(p);
		player2.addPermitCard(p);
		player2.addPermitCard(p);
		a=gameManager.buyOffert(new String[] {"angie","0","2","5"});
		assertEquals(count+"",ok,a);
		//5 choose political card
		count++;
		PoliticalCard c= new PoliticalCard();
		player2.addPoliticalCard(c);
		player2.addPoliticalCard(c);
		player2.addPoliticalCard(c);
		a=gameManager.buyOffert(new String[] {"angie","1","2","5"});
		assertEquals(count+"",ok,a);
		//6 choose helper
		count++;
		player2.addHelper(10);
		a=gameManager.buyOffert(new String[] {"angie","2","2","5"});
		assertEquals(count+"",ok,a);
	}
	
	@Test
	public void checkValidInsertOffer(){
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		player2= new Player ("angie", "fucile", Color.RED);
		game.addPlayer(player1);
		game.addPlayer(player2);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		String[] offert= new String []{"angie","0","0","5"};
		boolean a = game.checkValidInsertOffer(offert);
		assertEquals("ok",true,a);
		//string too long
		offert= new String []{"0","0","0","0","0"};
		a = game.checkValidInsertOffer(offert);
		assertEquals("ERROR",false,a);
		//insert wrong type of offer
		offert= new String []{"angie","15","0","5"};
		a = game.checkValidInsertOffer(offert);
		assertEquals("ERROR",false,a);	
		//insert wrong number of offer
		offert= new String []{"angie","2","-32","5"};
		a = game.checkValidInsertOffer(offert);
		assertEquals("ERROR",false,a);	
		//insert wrong number of money
		offert= new String []{"angie","0","0","-23"};
		a = game.checkValidInsertOffer(offert);
		assertEquals("ERROR",false,a);	
	}
	 @Test 
	public void checkPlayerHasEnoughResourcesForOffert(){
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		player2= new Player ("angie", "fucile", Color.RED);
		game.addPlayer(player1);
		game.addPlayer(player2);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		player1.addMoney(10);
		String[] offert= new String []{"angie","1","0","5"};
		boolean a = game.checkPlayerHasEnoughResourcesForOffert(offert, player1);
		assertEquals("ok",true,a);
		//not enough permit card
		offert= new String[]{"angie", "0","10","5"};
		 a = game.checkPlayerHasEnoughResourcesForOffert(offert, player1);
		assertEquals("ERROR",false,a);
		//not enough political card
		offert= new String[]{"angie", "1","10","5"};
		a = game.checkPlayerHasEnoughResourcesForOffert(offert, player1);
		assertEquals("ERROR",false,a);
		//not enough helper
		offert= new String[]{"angie", "2","10","5"};
		a = game.checkPlayerHasEnoughResourcesForOffert(offert, player1);
		assertEquals("ERROR",false,a); 
	 }
	 
	//finish game

	 @Test 
	 public void offerts(){//NOT CORRESPOND THE VALUE
		 	game=new Game(1,2);
			player1= new Player("lanzu","password", Color.PINK);
			player2= new Player ("angie", "fucile", Color.RED);
			game.addPlayer(player1);
			game.addPlayer(player2);
			gameManager=new GameManager(game);
			game.setWhoseTurn(0);
			prepareGame=new PrepareDataForClient(game);
			//political card
			PoliticalCard p= new PoliticalCard();
			p.setColorName("GREY");
			player2.addPoliticalCard(p);
			player2.addPoliticalCard(p);
			player2.addPoliticalCard(p);
			String[][] offerts=new String[1][3];
			offerts[0][0]= "angie";
			offerts[0][1]="1";
			offerts[0][2]="1";
			String a[][]= prepareGame.offerts(offerts);
			assertEquals("OK", "GREY", a[0][2]);
			//permit card
			offerts=new String[1][3];
			offerts[0][0]= "angie";
			offerts[0][1]="0";
			offerts[0][2]="1";
			PermitCard c= new PermitCard();
			c.addTown("A");
			player2.addPermitCard(c);
			player2.addPermitCard(c);
			player2.addPermitCard(c);
			a= prepareGame.offerts(offerts);
			assertEquals("OK", "A;false", a[0][2]);
			//used card
			player2.removePermitCard(0);
			player2.removePermitCard(0);
			player2.removePermitCard(0);
			offerts=new String[1][3];
			offerts[0][0]= "angie";
			offerts[0][1]="0";
			offerts[0][2]="1";
			c= new PermitCard();
			c.addTown("A");
			c.cardIsUsed();
			player2.addPermitCard(c);
			player2.addPermitCard(c);
			player2.addPermitCard(c);
			a= prepareGame.offerts(offerts);
			assertEquals("OK", "A;true", a[0][2]);
	 }
	 
	 
	 //FINISH GAME
	 
	 @Test
	public void finish(){
		//player 1 wins
		String [][]a;
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		player2= new Player ("angie", "fucile", Color.RED);
		game.addPlayer(player1);
		game.addPlayer(player2);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		PermitCard p= new PermitCard();
		player1.addNobility(100);
		player2.addNobility(10);
		player1.addPermitCard(p);
		player1.addPermitCard(p);
		player1.addPermitCard(p);
		player2.addPermitCard(p);
		PoliticalCard c= new PoliticalCard();
		player1.addPoliticalCard(c);
		player2.addPoliticalCard(c);
		player2.addPoliticalCard(c);
		a=gameManager.finishGame();
		String[][] rank = new String[2][2];
		rank[0][0]= "lanzu";
		rank[0][1]= game.getPlayer(0).getScorePoints()+"";
		rank[1][0]= "angie";
		rank[1][1]= game.getPlayer(1).getScorePoints()+"";
		assertEquals("OK",rank,a);
	
		// players has the same resource/cards
		game=new Game(1,2);
		player1= new Player("lanzu","password", Color.PINK);
		player2= new Player ("angie", "fucile", Color.RED);
		game.addPlayer(player1);
		game.addPlayer(player2);
		gameManager=new GameManager(game);
		gameManager.createGame();
		game.setWhoseTurn(0);
		p= new PermitCard();
		player1.addNobility(10);
		player2.addNobility(10);
		player1.addPermitCard(p);
		player2.addPermitCard(p);
		player1.addPoliticalCard(c);
		player2.addPoliticalCard(c);
		player1.addHelper(5);
		player1.addHelper(5);
		a=gameManager.finishGame();
		rank = new String[2][2];
		rank[0][0]= "lanzu";
		rank[0][1]= game.getPlayer(0).getScorePoints()+"";
		rank[1][0]= "angie";
		rank[1][1]= game.getPlayer(1).getScorePoints()+"";
		assertEquals("OK",rank,a);
		
	}
	
	
}