package server.model;

import java.awt.Color;

import server.model.gameComponent.map.TypeOfRegion;

/**
 * @author Angelo
 *
 */
/**
 * Save here some constant useful for the server and for the game
 */
public class GameConstants {
	final static public int numberOfRegion=3;
	final static public int townsForRegion=5;
	final static public int proprietiesForPermitCard=7;
	final static public int proprietiesForBonusReward=2;
	final static public int proprietiesForNobilityRoad=9;
	final static public int proprietiesForStatusOpponent=8;
	final static public int proprietiesForPlayer=4;
	final static public int proprietiesForPlayerPermitCards=8;
	final static public int proprietiesForTown=8;
	final static public int actionAvaiableAtStartTurn=1;
	final static public int maxNumberOfCouncilBalconies=4;
	final static public int maxNumberOfCouncillorForColor=4;
	final static public int startEmporium=10;//how many emporiums the players have at the beginning
	final static public int maxNumberOfCitiesPerRegion=5;
	final static public int cardsVisible=2;//number of visible permit cards  for region
	final static public int maxNumberOfCouncillors=4;//max number of councillor for balcony
	//game manager
	final static public int moneyCountStandard=10;//Minimum count of money that a player has at the beginning 
	final static public int helperCountStandard=1;//Minimum count of helpers that a player has at the beginning
	final static public int politicalCardCountStandard=6;//Minimum count of political cards that a player has at the beginning
	final static public int pointForFirst=5;
	final static public int pointForSecond=2;
	final static public int pointForLastEmporium=3;
	final static public int pointForPermitCard=3;
	final static public int howManyColors=6;
	
	final static public int error=-1;
	final static public int noResource=-2;
	final static public int ok=0;
	//TIMER
	final static public int delayForCheck=30;
	final static public int thousand=1000;
	final static public int timeGameStart=30;
	final static public int timeTurn=180;//how much time must last the turn of player in seconds
	final static public int timeDoOffert=180;//how much time must last the turn to make an offer in the market
	final static public int timeBuy=180;//how much time must last the turn to buy in the market
	
	final static public String Player_Offline="Player is offline";
	final static public int PORT = 9001;
	//MARKET
	final static public int validLenghtOffert=4;
	final static public int differentObjectCanSell=3;//permit card, political card and helper
	final static public int standardNumberOfPlayer=4;//if I have more players I duplicate some resource to save the playability
}
