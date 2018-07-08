package server.controller;

import java.awt.Color;
import java.io.Serializable;

import server.model.Game;
import server.model.GameConstants;
import server.model.Player;
import server.model.gameComponent.Reward;
import server.model.gameComponent.cards.PermitCard;
import server.model.gameComponent.kingboard.KingBoard;
import server.model.gameComponent.map.Towns;

/**
 * This class reads the state of the game and saves it in different elementary structures as array or variable.
 * In this way CLI or GUI don't need to know the complex model in the server
 */
public class PrepareDataForClient implements Serializable{
	/*
	 * I use this class to prepare the date that then I send to the client.
	 * This class is used to RMI_Server and Socket_Server
	 */
	private Game game;
	/**
	 * @param g the game that I want read to sent then the state to the client
	 */
	public PrepareDataForClient(Game g){
		game=g;
	}
	//Prepare Map
	/**
	 * @return the map of the game: row -> [connected towns, money, helper, politicalCard, nobility, scorePoint, who has emporium in the town, name of color town]
	 */
	public String[][]map(){
		int townsForRegion=GameConstants.townsForRegion;
		String[][]map=new String[game.sizeRegion()*townsForRegion][GameConstants.proprietiesForTown];
		for(int i=0;i<game.sizeRegion();i++)
		{
			for(int j=0;j<townsForRegion;j++)
			{
				Towns town=game.getRegion(i).getTown(j);
				//add connections
				map[i*townsForRegion+j][0]="";
				for(int k=0;k<town.sizeConnection();k++)
					map[i*townsForRegion+j][0]=map[i*townsForRegion+j][0]+town.getConnection(k).getName();
				//setup Reward
				Reward rewardTown=town.getRewardCity(); 
				map[i*townsForRegion+j][1]=rewardTown.getMoney()+"";
				map[i*townsForRegion+j][2]=rewardTown.getHelper()+"";
				map[i*townsForRegion+j][3]=rewardTown.getPoliticalCard()+"";
				map[i*townsForRegion+j][4]=rewardTown.getNobility()+"";
				map[i*townsForRegion+j][5]=rewardTown.getScorePoint()+"";
				map[i*townsForRegion+j][7]=town.getColorName();
				//setup Emporium that was built in the town
				map[i*townsForRegion+j][6]="";
				for(int k=0;k<town.howManyEmporium();k++)
					map[i*townsForRegion+j][6]=map[i*townsForRegion+j][6]+town.ownerEmporium(k).getName()+";";
				if(map[i*townsForRegion+j][6].length()>0)
					map[i*townsForRegion+j][6]=map[i*townsForRegion+j][6].substring(0, map[i*townsForRegion+j][6].length()-1);//I delete the last ";"
			}
		}
		return map;
	}
	//Prepare name region
	/**
	 * @return list of the region's name in the game
	 */
	public String[]nameRegion(){
		String[]nameRegion=new String[game.sizeRegion()];
		for(int i=0;i<game.sizeRegion();i++)
		{
			nameRegion[i]=game.getRegion(i).getName();
		}
		return nameRegion;
	}
	//Prepare visible permit card
	
	/**
	 * @return the visible permit card in the game: row -> [town where the card permits to build, money, helper, politicalcard, nobility, scorepoint, mainaction]
	 */
	public String[][]visiblePermitCard(){
		int  permitCard=game.getRegion(0).sizeVisiblePermitCards();
		String[][]visiblePermitCard=new String[permitCard*game.sizeRegion()][GameConstants.proprietiesForPermitCard];
		for(int i=0;i<game.sizeRegion();i++)
		{
			PermitCard []p=game.getRegion(i).getPermitCardsVisible();
			for(int j=0;j<p.length;j++)
			{
				int index=i*p.length+j;
				visiblePermitCard[index][0]="";
				for(int k=0;k<p[j].howManyTown();k++)
					visiblePermitCard[index][0]=visiblePermitCard[index][0]+p[j].getTown(k);
				Reward rewardPermitCard=p[j].getReward();
				visiblePermitCard[index][1]=rewardPermitCard.getMoney()+"";
				visiblePermitCard[index][2]=rewardPermitCard.getHelper()+"";
				visiblePermitCard[index][3]=rewardPermitCard.getPoliticalCard()+"";
				visiblePermitCard[index][4]=rewardPermitCard.getNobility()+"";
				visiblePermitCard[index][5]=rewardPermitCard.getScorePoint()+"";
				visiblePermitCard[index][6]=rewardPermitCard.getAnotherMainAction()+"";
			}
		}
		return visiblePermitCard;
	}
	//Prepare councilBalcony's color name 
	/**
	 * @return the name of the color of the councillor in Council Balcony (row->balcony, column->name of color of councillor)
	 */
	public String[][] councilBalconyNameColor(){
		KingBoard kingBoard=game.getKingBoard();
		String[][]councilBalconyName=new String[kingBoard.howManyBalconies()][GameConstants.maxNumberOfCouncilBalconies];
		for(int i=0;i<councilBalconyName.length;i++)
		{
			for(int j=0;j<councilBalconyName[i].length;j++)
			{
				councilBalconyName[i][j]=kingBoard.getCouncilBalcony(i).getCouncillor(j).getColorName();
			}
		}
		return councilBalconyName;
	}
	//Prepare councilBalcony's Color
	/**
	 * @return the color of the councillor in Council Balcony (row->balcony, column->color of councillor)
	 */
	public Color[][] councilBalconyColor(){
		KingBoard kingBoard=game.getKingBoard();
		Color[][]councilBalconyColor=new Color[kingBoard.howManyBalconies()][GameConstants.maxNumberOfCouncilBalconies];
		for(int i=0;i<councilBalconyColor.length;i++)
		{
			for(int j=0;j<councilBalconyColor[i].length;j++)
			{
				councilBalconyColor[i][j]=kingBoard.getCouncilBalcony(i).getCouncillor(j).getColor();
			}
		}
		return councilBalconyColor;
	}
	//Prepare King Reward
	/**
	 * @return the king rewards available
	 */
	public int[] kingReward(){
		KingBoard kingBoard=game.getKingBoard();
		int[]kingReward=new int[kingBoard.kingRewardSize()];
		for(int i=0;i<kingReward.length;i++)
		{
			kingReward[i]=kingBoard.getKingReward(i);
		}
		return kingReward;
	}
	//Prepare Bonus Reward
	/**
	 * @return bonus rewards available and in which town you must build to take them
	 */
	public String[][]bonusReward(){
		KingBoard kingBoard=game.getKingBoard();
		String[][]bonusReward=new String[kingBoard.bonusRewardSize()][GameConstants.proprietiesForBonusReward];
		for(int i=0;i<bonusReward.length;i++)
		{
			bonusReward[i][0]="";
			for(int j=0;j<kingBoard.getBonusReward(i).howManyTown();j++)
			{
				bonusReward[i][0]=bonusReward[i][0]+kingBoard.getBonusReward(i).getTown(j);
			}
			bonusReward[i][1]=kingBoard.getBonusReward(i).getScorePoints()+"";
		}
		return bonusReward;
	}
	//Prepare council name color out of balcony
	/**
	 * @return the name of the color of the councillor that are out of balcony
	 */
	public String[]councillorNameColorOutOfBalcony(){
		KingBoard kingBoard=game.getKingBoard();
		String[]councillorNameOutOfBalcony=new String[kingBoard.howManyCouncillorsOutOfBalconies()];
		for(int i=0;i<councillorNameOutOfBalcony.length;i++)
		{
			councillorNameOutOfBalcony[i]=kingBoard.getCouncillorOutOfBalconies(i).getColorName();
		}
		return councillorNameOutOfBalcony;
	}
	//Prepare  out of balcony councils' color
	/**
	 * @return the color of the councillor that are out of balcony
	 */
	public Color[]councillorColorOutOfBalcony(){
		KingBoard kingBoard=game.getKingBoard();
		Color[]councillorColorNameOutOfBalcony=new Color[kingBoard.howManyCouncillorsOutOfBalconies()];
		for(int i=0;i<councillorColorNameOutOfBalcony.length;i++)
		{
			councillorColorNameOutOfBalcony[i]=kingBoard.getCouncillorOutOfBalconies(i).getColor();
		}
		return councillorColorNameOutOfBalcony;
	}
	//Prepare nobility road	
	/**
	 * @return the nobility road in the game: row -> [money, helper, politicalcard, nobility, scorepoint, mainaction, permitcardbonus, permitcard, bonustown]
	 */
	public int[][] nobilityRoad(){
		int[][]nobilityRoad=new int[game.sizeNobilityBonus()][GameConstants.proprietiesForNobilityRoad];
		for(int i=0;i<nobilityRoad.length;i++)
		{
			Reward reward=game.getNobilityBonus(i);
			nobilityRoad[i][0]=reward.getMoney();
			nobilityRoad[i][1]=reward.getHelper();
			nobilityRoad[i][2]=reward.getPoliticalCard();
			nobilityRoad[i][3]=reward.getNobility();
			nobilityRoad[i][4]=reward.getScorePoint();
			nobilityRoad[i][5]=reward.getAnotherMainAction();
			nobilityRoad[i][6]=reward.getPermitCards();
			nobilityRoad[i][7]=reward.getbonusPermitCards();
			nobilityRoad[i][8]=reward.getBonusTown();
		}
		return nobilityRoad;
	}
	//Prepare status opponents
	
	/**
	 * @param namePlayer name of the player I want to sent the status of opponents
	 * @return status of opponents
	 * row-> [money, helper, scorePoint, nobility, how many permit cards the player has, how many politic cards the player has, how many not-used emporiums the player has]
	 */
	public String[][]statusOpponents(String namePlayer){
		String[][]statusOpponents=new String[game.numberOfPlayers()-1][GameConstants.proprietiesForStatusOpponent];
		int index=0;
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			Player p=game.getPlayer(i);
			if(!p.getName().equals(namePlayer))
			{//I just send the opponent status, so I check that the name who I send the dates is different
				statusOpponents[index][0]=p.getName();
				statusOpponents[index][1]=p.getMoney()+"";
				statusOpponents[index][2]=p.getHelper()+"";
				statusOpponents[index][3]=p.getScorePoints()+"";
				statusOpponents[index][4]=p.getNobility()+"";
				statusOpponents[index][5]=p.permitDeckSize()+"";
				statusOpponents[index][6]=p.politicalDeckSize()+"";
				statusOpponents[index][7]=p.getEmporium()+"";
				index++;
			}
		}
		return statusOpponents;
	}
	//Prepare color opponent
	/**
	 * @param namePlayer name of the player I want to sent the opponents' color:
	 * @return color of opponents 
	 */
	public Color[]colorOpponents(String namePlayer){
		Color[]colorOpponents=new Color[game.numberOfPlayers()-1];
		int index=0;
		for(int i=0;i<colorOpponents.length;i++)
		{
			Player p=game.getPlayer(i);
			if(!p.getName().equals(namePlayer))
			{//I just send the opponent status, so I check that the name who I send the dates is different
				colorOpponents[index]=p.getColor();
				index++;
			}
		}
		return colorOpponents;
	}
	//Prepare player status
	/**
	 * @param p player I want to read the resources
	 * @return player status
	 */
	public String[] playerStatus(Player p){
		String []playerStatus=new String[GameConstants.proprietiesForPlayer];
		playerStatus[0]=p.getMoney()+"";
		playerStatus[1]=p.getHelper()+"";
		playerStatus[2]=p.getNobility()+"";
		playerStatus[3]=p.getScorePoints()+"";
		return playerStatus;
	}
	//prepare player's political cards
	/**
	 * @param p player I want to read the resources
	 * @return the name of the color of political cards that the player has
	 */
	public String[] playerPoliticalCardsColorName(Player p){
		String[]playerPoliticalCards=new String[p.politicalDeckSize()];
		for(int i=0;i<playerPoliticalCards.length;i++)
		{
			playerPoliticalCards[i]=p.getPoliticalCard(i).getColorName();
		}
		
		return playerPoliticalCards;
	}
	//prepare player's political cards' color
	/**
	 * @param p player I want to read the resources
	 * @return the color of political cards that the player has
	 */
	public Color[] playerPoliticalCardsColor(Player p){
		Color[]playerPoliticalCardColors=new Color[p.politicalDeckSize()];
		for(int i=0;i<playerPoliticalCardColors.length;i++)
		{
			playerPoliticalCardColors[i]=p.getPoliticalCard(i).getColor();
		}
		
		return playerPoliticalCardColors;
	}
	//prepare player's permit cards town
	/**
	 * @param p player  I want to read the resources
	 * @return the permit cards that the player has
	 */
	public String[][]playerPermitCards(Player p){
		String[][]playerPermitCards=new String[p.permitDeckSize()][GameConstants.proprietiesForPlayerPermitCards];
		for(int i=0;i<playerPermitCards.length;i++)
		{
			PermitCard permitCard=p.getPermitCard(i);
			playerPermitCards[i][0]=permitCard.getIsUsed()+"";
			playerPermitCards[i][1]="";
			for(int j=0;j<permitCard.howManyTown();j++)
			{
				playerPermitCards[i][1]=playerPermitCards[i][1]+permitCard.getTown(j);
			}
			Reward reward=permitCard.getReward();
			playerPermitCards[i][2]=reward.getMoney()+"";
			playerPermitCards[i][3]=reward.getHelper()+"";
			playerPermitCards[i][4]=reward.getPoliticalCard()+"";
			playerPermitCards[i][5]=reward.getNobility()+"";
			playerPermitCards[i][6]=reward.getScorePoint()+"";
			playerPermitCards[i][7]=reward.getAnotherMainAction()+"";
		}
		return playerPermitCards;
	}
	//prepare offer to the client
	/*Insert you offer in this way firstNumber-secondNumber-thirdNumber (example: 1-2-5)
	 zeroCell: Name of player
	 firstNumber: 0-permit card, 1-political card, 2-helper
	 secondNumber: which permit card/political card or how much helper you want to sell
	 thirdNumber: how much money do you want for your offer
	example: 1-1-5 -> you offer your political card number 1 for 5 money
	*/
	/**
	 * @param off offers available with the number and name of the player that did the offer
	 * @return the offers available in the market set up with name of the resource that was offered
	 */
	public String[][] offerts(String[][]off){
		for(int i=0;i<off.length;i++)
		{
			Player player=game.findPlayer(off[i][0]);
			if(off[i][1].equals("1"))
			{
				off[i][2]=player.getPoliticalCard(Integer.parseInt(off[i][2])).getColorName();
			}
			if(off[i][1].equals("0"))
			{
				int indexPermit=Integer.parseInt(off[i][2]);
				off[i][2]=player.getPermitCard(indexPermit).getAllTowns();
				if(player.getPermitCard(indexPermit).getIsUsed())
					off[i][2]=off[i][2]+";true";
				else
					off[i][2]=off[i][2]+";false";
			}
		}
		return off;
	}
}
