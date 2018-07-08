package server.controller;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import server.model.Game;
import server.model.GameConstants;
import server.model.Player;
import server.model.gameComponent.cards.BonusReward;
import server.model.gameComponent.cards.KingReward;
import server.model.gameComponent.cards.PermitCard;
import server.model.gameComponent.cards.PoliticalCard;
import server.model.gameComponent.kingboard.CouncilBalcony;
import server.model.gameComponent.kingboard.Councillor;
import server.model.gameComponent.kingboard.KingBoard;
import server.model.gameComponent.map.Region;
import server.model.gameComponent.Reward;
import server.model.gameComponent.RewardType;
import server.model.gameComponent.map.Towns;
import server.model.gameComponent.map.TownsColor;

/**
 * This class represents the game logic,  creates the game and modifies it when the player does an action
 */
public class GameManager {
	/*
	 * This class has the methods that modify the game.
	 * I pass the Game that I want to modify when I create this builder and then I call the methods. 
	 */
	
	private Game game; 
	
	
	public GameManager(Game g)
	{
		game=g;
	}
	
	/**
	 * Create a new game
	 */
	public void createGame()
	{
		
		String configurationGame=readConfigurationFile(game.getMap());
		String split[]=configurationGame.split("&");
		setUpTowns(split[0],split[1]);
		setUpBonusTowns(split[2]);
		setUpPoliticalCards();
		setUpPermitCards(split[3]);
		setUpKingBoard(split[4],split[5]);
		setUpNobilityRoad(split[6]);
		setUpPlayers();
	}
	
	
	/**
	 * @param map inserts the configuration file chosen
	 * @return String with the content of configuration file
	 */
	private String readConfigurationFile(int map)
	{
		// The name of the file I want to open.
        String fileName = "src/map"+map+".txt";
        String configurationGame="";
        String line = "";// This will refer one line at a time
        try (BufferedReader bufferedReader =  new BufferedReader(new FileReader(fileName))){
            
            while((line = bufferedReader.readLine()) != null) {
                configurationGame=configurationGame+line+"\n";
            }   
            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) {
            System.err.println("ERROR="+e);                
        }
        return configurationGame;
	}
	
	/**
	 * @param colors name colors of towns
	 * @param roads	connections of towns
	 */
	private void setUpTowns(String colors,String roads)
	{
		
		String color[]=colors.split("\n");
		//Now I create all towns
		Towns vettTowns[]=new Towns[GameConstants.numberOfRegion*GameConstants.townsForRegion];
		for(int i=0;i<vettTowns.length;i++)
		{
			char c=(char) ('A'+i);
			Color colorTowns=null;
			switch(color[i])
			{
			case "purple":
				colorTowns=new Color(153, 102, 204);
				break;
			case "orange":
				colorTowns=Color.ORANGE;
				break;
			case "blue":
				colorTowns=Color.BLUE;
				break;
			case "yellow":
				colorTowns=Color.YELLOW;
				break;
			case "gray":
				colorTowns=Color.GRAY;
				break;
			default: colorTowns=null;
			}
			Towns t=new Towns(c,color[i],colorTowns);
			if(t.getColorName().equals(TownsColor.purple.toString()))
				game.moveKing(t);//The King is in the Capital at the beginning
			vettTowns[i]=t;
		}
		//Now I add the connections for every city
		String roadOwnOneCity[]=roads.split("\n");
		for(int i=0;i<vettTowns.length;i++)
		{
			for(int j=0;j<roadOwnOneCity[i].length();j++)
			{
				char t=roadOwnOneCity[i].charAt(j);
				vettTowns[i].addConnection(vettTowns[t-'A']);
			}
			
		}
		//Now I add the city in the Region and then I add the Region in the Game
		for(int i=0;i<GameConstants.numberOfRegion;i++)
		{
			Region region=new Region(i);
			for(int j=GameConstants.townsForRegion*i;j<GameConstants.townsForRegion*GameConstants.numberOfRegion;j++)
			{
				region.addTown(vettTowns[j]);
			}
			game.addRegion(region);
		}
	}
	/**
	 * @param b list of bonus
	 */
	private void setUpBonusTowns(String b)
	{
		//I read the bonus from the files and put them in an ArrayList
		ArrayList<Reward>reward=new ArrayList<Reward>();
		String lines[]=b.split("\n");
		for(int i=0;i<lines.length;i++)
		{
			String bonus[]=lines[i].split(";");
			Reward r=new Reward();
			for(int j=0;j<bonus.length;j++)
			{
				String singleBonus[]=bonus[j].split(",");			
				if(singleBonus[0].equals(RewardType.money.toString()))
				{
					r.setMoney(Integer.parseInt(singleBonus[1]));
				}
				if(singleBonus[0].equals(RewardType.helper.toString()))
				{
					r.setHelper(Integer.parseInt(singleBonus[1]));
				}
				if(singleBonus[0].equals(RewardType.nobility.toString()))
				{
					r.setNobility(Integer.parseInt(singleBonus[1]));
				}
				if(singleBonus[0].equals(RewardType.scorepoint.toString()))
				{
					r.setScorePoint(Integer.parseInt(singleBonus[1]));
				}
				if(singleBonus[0].equals(RewardType.politicalcard.toString()))
				{
					r.setPoliticalCard(Integer.parseInt(singleBonus[1]));
				}
				if(singleBonus[0].equals(RewardType.mainaction.toString()))
				{
					r.setAnotherMainAction(Integer.parseInt(singleBonus[1]));
				}
			}
			reward.add(r);
		}
		//Now I get randomly the bonus from the ArrayList and put them in every city
		for(int i=0;i<GameConstants.numberOfRegion;i++)
		{
			for(int j=0;j<GameConstants.townsForRegion;j++)
			{
				Random x=new Random();
				int s=reward.size();
				int index=x.nextInt(reward.size());
				if(!game.getRegion(i).getTown(j).getColorName().equals("purple"))//I don't add the reward to the capital
				{
					game.getRegion(i).getTown(j).setRewardCity(reward.get(index));//add the random reward
					reward.remove(index);//remove the reward that was assigned
				}
			}
		}
	}
	
	final static private Color colors[]={Color.BLACK,Color.BLUE,Color.RED,Color.ORANGE,Color.WHITE,Color.GREEN};
	final static private String colorsName[]={"BLACK","BLUE","RED","ORANGE","WHITE","GREEN"};
	private void setUpPoliticalCards() {
		int players=game.numberOfPlayers();
		int howManyForColor=13;
		int howManyJolly=12;
		if(players>4)
		{//I increment total cards to 21 for every player over the 4
			howManyForColor=howManyForColor+3*(players-4);
			howManyJolly=howManyJolly+3*(players-4);
		}
		for(int i=0;i<GameConstants.howManyColors;i++)
		{
			for(int j=0;j<howManyForColor;j++)
			{
				PoliticalCard p=new PoliticalCard(colors[i]);
				p.setColorName(colorsName[i]);
				game.addPoliticalCard(p);
			}
		}
		for(int j=0;j<howManyJolly;j++)
		{
			PoliticalCard p=new PoliticalCard();
			game.addPoliticalCard(p);
		}
	}
	
	/**
	 * @param s list of permit cards
	 */
	private void setUpPermitCards(String s)
	{
		String permitCardForRegion[]=s.split("%");
		for(int i=0;i<permitCardForRegion.length;i++)
		{//divide the permits in different groups for the region own
			String singlePermit[]=permitCardForRegion[i].split("\n");
			for(int j=0;j<singlePermit.length;j++)
			{//divide the group of permits
				String townAndRewards[]=singlePermit[j].split("-");
				PermitCard permitCard=new PermitCard();
				for(int k=0;k<townAndRewards[0].length();k++)
				{//Add the town in the permit card
					permitCard.addTown(townAndRewards[0].charAt(k)+"");
				}
				Reward r=new Reward();
				String rewards[]=townAndRewards[1].split(";");
				for(int k=0;k<rewards.length;k++)
				{//Create the Reward to be added to the permit cards
					String singleReward[]=rewards[k].split(",");
					if(singleReward[0].equals(RewardType.money.toString()))
					{
						r.setMoney(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.helper.toString()))
					{
						r.setHelper(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.nobility.toString()))
					{
						r.setNobility(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.scorepoint.toString()))
					{
						r.setScorePoint(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.politicalcard.toString()))
					{
						r.setPoliticalCard(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.mainaction.toString()))
					{
						r.setAnotherMainAction(Integer.parseInt(singleReward[1]));
					}
				}
				//Now I add the reward to the permit card
				permitCard.setReward(r);
				//Now I add the permit card to the Region	
				game.getRegion(i).addPermitCard(permitCard);
			}
			game.getRegion(i).setUpPermitCard();
		}
		//Now for every Region I add 3 permit cards for each player over 4 
		if(game.numberOfPlayers()>4)
		{
			for(int i=0;i<GameConstants.numberOfRegion;i++)
			{
				game.getRegion(i).morePermitCards(game.numberOfPlayers()-4);
			}
		}
	}
	
	/**
	 * @param kingReward list of king rewards
	 * @param BonusReward list of bonus rewards
	 */
	private void setUpKingBoard(String kingReward,String BonusReward)
	{
		KingBoard kingboard=new KingBoard();
		//Now I add the King Reward
		String skr[]=kingReward.split("\n");
		for(int i=0;i<skr.length;i++)
		{
			KingReward kr=new KingReward(Integer.parseInt(skr[i])); 
			kingboard.addKingReward(kr);
		}
		//Now I add The Bonus Reward
		String sbr[]=BonusReward.split("\n");
		for(int i=0;i<sbr.length;i++)
		{
			String townsAndScorePoint[]=sbr[i].split("-");
			BonusReward br=new BonusReward(Integer.parseInt(townsAndScorePoint[1]));
			for(int j=0;j<townsAndScorePoint[0].length();j++)
			{//I add the towns where a player must build to take this bonus Reward
				br.addTown(townsAndScorePoint[0].charAt(j)+"");
			}
			kingboard.addBonusReward(br);
		}
		//Now I setUp the council and councilBalcony
		/*
		 * First, I put every councillor in the kingBoard, then I get them randomly and put in different councilBalcony
		 */
		for(int i=0;i<GameConstants.howManyColors;i++)
		{
			for(int j=0;j<GameConstants.maxNumberOfCouncillorForColor;j++)
			{
				Councillor c=new Councillor();
				c.setColor(colors[i]);
				c.setColorName(colorsName[i]);
				kingboard.addCouncillorOutOfBalconies(c);
			}
		}
		//Now I get council randomly and put them in different balcony
		int howManyBalconies=GameConstants.maxNumberOfCouncilBalconies;
		for(int i=0;i<howManyBalconies;i++)
		{
			CouncilBalcony cb;
			if(i<howManyBalconies-1)
				cb=new CouncilBalcony(game.getRegion(i).getName());
			else
				cb=new CouncilBalcony("KING");//the last balcony is the King Balcony
			Councillor c[]=new Councillor[GameConstants.maxNumberOfCouncillors];
			for(int j=0;j<c.length;j++)
			{
				Random x=new Random();
				int index=x.nextInt(kingboard.howManyCouncillorsOutOfBalconies());
				c[j]=kingboard.getCouncillorOutOfBalconies(index);
				kingboard.removeCouncillor(index);
			}
			cb.setUpCouncilBalcony(c);
			kingboard.addCouncilBalcony(cb);
		}
		game.setKingBoard(kingboard);
	}
	
	/**
	 * @param s list of bonus in the nobility road
	 */
	private void setUpNobilityRoad(String s){
		String []nobilityReward=s.split("\n");
		for(int i=0;i<nobilityReward.length;i++)
		{
			if(nobilityReward[i].equals("0"))
			{
				Reward r=new Reward();
				game.addNobilityBonus(r);
			}
			else
			{
				String [] bonus=nobilityReward[i].split(";");
				Reward r=new Reward();
				for(int j=0;j<bonus.length;j++)
				{
					String [] singleReward=bonus[j].split(",");
					if(singleReward[0].equals(RewardType.money.toString()))
					{
						r.setMoney(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.helper.toString()))
					{
						r.setHelper(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.nobility.toString()))
					{
						r.setNobility(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.scorepoint.toString()))
					{
						r.setScorePoint(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.politicalcard.toString()))
					{
						r.setPoliticalCard(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.mainaction.toString()))
					{
						r.setAnotherMainAction(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.permitcard.toString()))
					{
						r.setPermitCards(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.bonuspermitcard.toString()))
					{
						r.setbonusPermitCards(Integer.parseInt(singleReward[1]));
					}
					if(singleReward[0].equals(RewardType.bonustown.toString()))
					{
						r.setBonusTown(Integer.parseInt(singleReward[1]));
					}
				}
				game.addNobilityBonus(r);
			}
		}
	}
	
	//setup Players
	private void setUpPlayers()
	{
		Random x=new Random();
		int turn=x.nextInt(game.numberOfPlayers());
		game.setWhoseTurn(turn);
		int sizePlayers=game.numberOfPlayers();
		int count=0;
		while(count<sizePlayers)
		{
			Player p=game.getPlayer(turn);
			p.setUpPlayer();
			if(count<=5)
			{
				p.addMoney(GameConstants.moneyCountStandard+count);
				p.addHelper(GameConstants.helperCountStandard+count);
			}
			else
			{
				p.addMoney(15);
				p.addHelper(6);
			}
			for(int j=0;j<GameConstants.politicalCardCountStandard;j++)
			{
				p.addPoliticalCard(game.pickUpPoliticalCard());
			}
			count++;
			turn++;
			if(turn==sizePlayers)
				turn=0;
		}
		game.setCount();
	}
	
	//Below there are the methods to play during the game
	//MAIN ACTION
	//ACTION 1
	/**
	 * @param councillorIndex councillor that I want to add in the balcony
	 * @param councilBalconyIndex balcony where I want to add the new councillor
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int electCouncillor(int councillorIndex, int councilBalconyIndex){
		//	0=1st Region's CouncilBalcony	1=2nd	2=3rd councilBalconyIndex: 4(or last)=KingBoard's CouncilBalcony	
		KingBoard kb=game.getKingBoard();
		if(councillorIndex>=kb.howManyCouncillorsOutOfBalconies())
			return GameConstants.error;
		if(councilBalconyIndex>=kb.howManyBalconies())
			return GameConstants.error;
		Councillor c=kb.getCouncillorOutOfBalconies(councillorIndex);
		Councillor c2=kb.getCouncilBalcony(councilBalconyIndex).pushInCouncillor(c);
		kb.removeCouncillor(councillorIndex);
		kb.addCouncillorOutOfBalconies(c2);
		
		game.getPlayer(game.getWhoseTurn()).addMoney(4);
		game.getPlayer(game.getWhoseTurn()).setMainAction(game.getPlayer(game.getWhoseTurn()).getMainAction()-1);
		return GameConstants.ok;
	}		
	//ACTION 2
	/**
	 * @param politicCardsUse vector with the index of politic cards that you want to use
	 * @param whichBalcony the balcony that you want to corrupt
	 * @param permitCardIndex the permit card chosen 
	 * @return the value indicates what the method returns(OK, error or noResource)
	 */
	public int buyPermitCard(int []politicCardsUse, int whichBalcony,int permitCardIndex){
		Player player=game.getPlayer(game.getWhoseTurn());
		ArrayList<PoliticalCard> cardsThePlayerWantsToSpend=new ArrayList<PoliticalCard>();
		for(int i=0;i<politicCardsUse.length;i++)
			if(politicCardsUse[i]<player.politicalDeckSize())
				cardsThePlayerWantsToSpend.add(player.getPoliticalCard(politicCardsUse[i]));
			else
				return GameConstants.error;
		if(whichBalcony>GameConstants.numberOfRegion)
			return GameConstants.error;
		Region region=game.getRegion(whichBalcony);
		CouncilBalcony balcony=game.getKingBoard().getCouncilBalcony(whichBalcony);
		if(!doesThePlayerMeetTheConditionsToBuyAPermitCard(cardsThePlayerWantsToSpend,balcony)) 
			return GameConstants.error;//player has inserted wrong card
		
		int amountOfMoneyToSpend;
		switch(cardsThePlayerWantsToSpend.size()){
		case 1: amountOfMoneyToSpend=10; break;
		case 2: amountOfMoneyToSpend=7; break;
		case 3: amountOfMoneyToSpend=4; break;
		case 4: amountOfMoneyToSpend=0; break;
		default: return GameConstants.error;//player has inserted wrong card
		}
		//Each jolly increases the price of 1
		for(int i=0;i<cardsThePlayerWantsToSpend.size();i++)
			if(cardsThePlayerWantsToSpend.get(i).isJolly())
				amountOfMoneyToSpend++;
		if(player.getMoney()>=amountOfMoneyToSpend)
			player.addMoney(-1*amountOfMoneyToSpend);
		else
			return GameConstants.noResource;//player doesn't have enough money
		
		PermitCard permitCard=region.pickUpPermitCardVisible(permitCardIndex);
		player.addPermitCard(permitCard);
		//now I delete the cards that the player has used
		player.removePoliticalCard(cardsThePlayerWantsToSpend);
		player.addReward(permitCard.getReward());
		player.setMainAction(game.getPlayer(game.getWhoseTurn()).getMainAction()-1);
		return GameConstants.ok;
	}
	private boolean doesThePlayerMeetTheConditionsToBuyAPermitCard(ArrayList<PoliticalCard> cardsThePlayerWantsToSpend,CouncilBalcony councilBalcony){
		//Every card the player wants to spend must be of the same color of at least a councillor in the council balcony
		boolean found=true;
		boolean[] occourrances=new boolean[GameConstants.maxNumberOfCouncillors];
		for(int i=0;i<cardsThePlayerWantsToSpend.size();i++) 
		{
				found=false;
				for(int j=0;j<GameConstants.maxNumberOfCouncillors;j++)
				{
					if(!occourrances[j]&&!found)
						if(cardsThePlayerWantsToSpend.get(i).getColorName().equals(councilBalcony.getCouncillor(j).getColorName()) || cardsThePlayerWantsToSpend.get(i).isJolly())
						{
							occourrances[j]=true;
							found=true;
						}
				}
				if(!found)
					return false;
		}
		return true;
	}
	//ACTION 3
	/**
	 * @param indexPermitCard permit card that you want to use to build emporium
	 * @param townName town where you want to build the emporium
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int buildEmporiumByPermit(int indexPermitCard,String townName){
		int townIndex=indexOfTown(townName);
		if(townIndex==-1)
			return GameConstants.error;
		int regionIndex=townIndex/GameConstants.townsForRegion;
		townIndex=townIndex%GameConstants.townsForRegion;
		Player player=game.getPlayer(game.getWhoseTurn());
		Towns town=game.getRegion(regionIndex).getTown(townIndex);
		for(int i=0;i<town.howManyEmporium();i++)
			if(town.ownerEmporium(i)==player) 
				return GameConstants.error;
		if(town.howManyEmporium()>player.getHelper()) 
			return GameConstants.noResource;
		if(indexPermitCard>=player.permitDeckSize())
			return GameConstants.error;
		if(player.getPermitCard(indexPermitCard).getIsUsed())
			return GameConstants.error;
		PermitCard permitCard=player.getPermitCard(indexPermitCard);//check that the permit card permits to build in the chosen town
		boolean checkTownName=true;
		for(int i=0;i<permitCard.howManyTown();i++)
			if(permitCard.getTown(i).equals(townName))
				checkTownName=false;
		if(checkTownName)
			return GameConstants.error;
		//end every control, if I arrive here everything is OK and I can start to change the status game
		player.decrementEmporium();
		if(player.getEmporium()==0)
			player.addScorePoints(GameConstants.pointForLastEmporium);
		player.addHelper(-1*town.howManyEmporium());
		town.newEmporium(player);
		permitCard.cardIsUsed();
		giveAllRewards(player,town);
		checkBonusAndKingReward();
		player.setMainAction(game.getPlayer(game.getWhoseTurn()).getMainAction()-1);
		return GameConstants.ok;
	}
	private void giveAllRewards(Player p,Towns currentTown){		
		int numberOfTowns=GameConstants.numberOfRegion*GameConstants.townsForRegion;
		int townIndex=indexOfTown(currentTown.getName()+"");
		boolean [][] connections1=new boolean[numberOfTowns][numberOfTowns];
		for(int i=0;i<numberOfTowns;i++)
			for(int j=0;j<numberOfTowns;j++)
				connections1[i][j]=isTheConnectionValid(p,game.getRegion(i/GameConstants.townsForRegion).getTown(i%GameConstants.townsForRegion),game.getRegion(j/GameConstants.townsForRegion).getTown(j%GameConstants.townsForRegion));
			
		boolean [] connections2=new boolean[numberOfTowns];
		connections2=validConnections(connections1, townIndex, numberOfTowns);
		
		
		for(int i=0;i<numberOfTowns;i++) 
			if(connections2[i])
				p.addReward(game.getRegion(i/GameConstants.townsForRegion).getTown(i/GameConstants.townsForRegion).getRewardCity());
		p.addReward(currentTown.getRewardCity());
	}
	private boolean[] validConnections(boolean[][] connections1,int townIndex, int numberOfTowns) {
		boolean[]connectionsNewTown=new boolean[numberOfTowns];
		boolean[]check=new boolean[numberOfTowns];//save the town I check
		//setup vett
		for(int i=0;i<connectionsNewTown.length;i++)
		{
			connectionsNewTown[i]=connections1[townIndex][i];
			if(connectionsNewTown[i] && i!=townIndex)
				check[i]=false;
			else
				check[i]=true;
		}
		int indexVett=0;
		while(indexVett<connectionsNewTown.length)
		{
			if(connectionsNewTown[indexVett] && check[indexVett]==false)
			{//the city in the indexVett position is connected
				for(int i=0;i<numberOfTowns;i++)
				{
					if(connections1[indexVett][i]==true && i!=townIndex && connectionsNewTown[i]==false)
					{
						connectionsNewTown[i]=true;
						check[i]=false;
					}
				}
				check[indexVett]=true;
				indexVett=0;
			}
			else
				indexVett++;
		}
		return connectionsNewTown;
	}
	private boolean isTheConnectionValid(Player p,Towns t1,Towns t2){
		if(hasEmporium(p,t1) && hasEmporium(p,t2) && townsAreConnected(t1,t2))
			return true;
		return false;
	}
	private boolean townsAreConnected(Towns t1, Towns t2) {
		for(int i=0;i<t1.sizeConnection();i++)
			if(t1.getConnection(i).equals(t2))
				return true;
		return false;
	}
	private boolean hasEmporium(Player p,Towns t1){
		for(int i=0;i<t1.howManyEmporium();i++)
			if(t1.ownerEmporium(i)==p)
				return true;
		return false;
	}
	private int indexOfTown(String townName){
		int indexOfTheCityToFind=0;
		Region currentRegion=null;
		Towns currentTown=null;
		for(int regionIndex=0;regionIndex<game.sizeRegion();regionIndex++)
		{
			currentRegion=game.getRegion(regionIndex);
			for(int townIndex=0;townIndex<GameConstants.townsForRegion;townIndex++)
			{
				currentTown=currentRegion.getTown(townIndex);
				if((currentTown.getName()+"").equals(townName))
					return indexOfTheCityToFind;
				indexOfTheCityToFind++;
			}
				
		}
		return -1;
	}
	//ACTION 4
	/**
	 * @param politicCardsUse politic cards you want to use to corrupt king balcony
	 * @param townName town where you want to build
	 * @param route	the towns that you want to cross
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int buildEmporiumByKing(int []politicCardsUse, String townName, String route){
		Player player=game.getPlayer(game.getWhoseTurn());
		ArrayList<PoliticalCard> politicalCardsUsed=new ArrayList<PoliticalCard>();
		for(int i=0;i<politicCardsUse.length;i++)
			if(politicCardsUse[i]<player.politicalDeckSize())
				 politicalCardsUsed.add(player.getPoliticalCard(politicCardsUse[i]));
			else
				return GameConstants.error;
		int townIndex=indexOfTown(townName);
		if(townIndex==-1)
			return GameConstants.error;
		int regionIndex=townIndex/GameConstants.townsForRegion;
		Region region=game.getRegion(regionIndex);
		KingBoard kingBoard=game.getKingBoard();
		if(townIndex==-1)
			return GameConstants.error;
		townIndex=townIndex%GameConstants.townsForRegion;
		Towns townWhereToBuild=region.getTown(townIndex);
		for(int i=0;i<townWhereToBuild.howManyEmporium();i++)
			if(townWhereToBuild.ownerEmporium(i)==player) 
				return GameConstants.error;
		if(!doesThePlayerMeetTheConditionsToBuyAPermitCard(politicalCardsUsed,kingBoard.getCouncilBalcony(kingBoard.howManyBalconies()-1))) 
			return GameConstants.error;
		if(townWhereToBuild.howManyEmporium()>player.getHelper()) 
			return GameConstants.noResource;
		int amountOfMoneyToSpend;
		switch(politicalCardsUsed.size()){
		case 1: amountOfMoneyToSpend=10; break;
		case 2: amountOfMoneyToSpend=7; break;
		case 3: amountOfMoneyToSpend=4; break;
		case 4: amountOfMoneyToSpend=0; break;
		default: return GameConstants.error;
		}
		for(int i=0;i<politicalCardsUsed.size();i++)
			if(politicalCardsUsed.get(i).isJolly())
			amountOfMoneyToSpend++;
		if(player.getHelper()<townWhereToBuild.howManyEmporium())
			return GameConstants.noResource;
		amountOfMoneyToSpend=amountOfMoneyToSpend+2*(route.length()-1);
		if(player.getMoney()<(amountOfMoneyToSpend))
			return GameConstants.noResource;
		if(!isValidRoute(route, townName))
			return GameConstants.error;
		//end every control, if I arrive here everything is OK and I can start to change the status game
		player.addHelper(-1*townWhereToBuild.howManyEmporium());
		player.decrementEmporium();
		if(player.getEmporium()==0)
			player.addScorePoints(GameConstants.pointForLastEmporium);
		//Price of the action
		player.addMoney(-1*amountOfMoneyToSpend);
		//Price of moving the king
		Towns townKing=game.whereIsKing();
		player.removePoliticalCard(politicalCardsUsed);
		game.moveKing(townWhereToBuild);
		townWhereToBuild.newEmporium(player);
		giveAllRewards(player,townWhereToBuild);
		checkBonusAndKingReward();
		player.setMainAction(game.getPlayer(game.getWhoseTurn()).getMainAction()-1);
		return GameConstants.ok;
	}
	private boolean isValidRoute(String route, String destination){
		if(!route.substring(0,1).equals(game.whereIsKing().getName()+""))//check that the player start where is king
			return false;
		for(int i=0;i<route.length()-1;i++)
		{
			Towns t1=findTown(route.charAt(i)+"");
			Towns t2=findTown(route.charAt(i+1)+"");
			if(!townsAreConnected(t1, t2))
				return false;
			if((t2.getName()+"").equals(destination) && i<route.length()-2)
				return false;
		}
		return true;
	}
	private Towns findTown(String name){
		int townIndex=indexOfTown(name);
		int regionIndex=townIndex/GameConstants.townsForRegion;
		townIndex=townIndex%GameConstants.townsForRegion;
		if(townIndex!=-1)
			return game.getRegion(regionIndex).getTown(townIndex);
		return null;
	}
	
	//Check bonus and king reward when the player build an Emporium
	private void checkBonusAndKingReward(){
		KingBoard kingBoard=game.getKingBoard();
		for(int i=0;i<kingBoard.bonusRewardSize();i++)
		{
			Player player=game.getPlayer(game.getWhoseTurn());
			String s=wherePlayerBuild(player);
			if(kingBoard.getBonusReward(i).playerCanTakeBonus(s))
			{
				player.addScorePoints(kingBoard.pickUpBonusReward(i).getScorePoints());
				if(kingBoard.kingRewardSize()>0)
					player.addScorePoints(kingBoard.pickUpKingReward().getKingRewardValue());
			}
		}
	}
	private String wherePlayerBuild(Player player){
		String towns="";
		for(int i=0;i<GameConstants.townsForRegion*GameConstants.numberOfRegion;i++)
		{
			Towns t=game.getRegion(i/GameConstants.townsForRegion).getTown(i%GameConstants.townsForRegion);
			if(t.thePlayerHasEmporium(player.getName()))
				towns=towns+t.getName();
		}
		return towns;
	}
	
	//SECONDARY ACTION
	//ACTION 5
	/**
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int engangeAnHelper(){
		Player p=game.getPlayer(game.getWhoseTurn());
		if(p.getMoney()<3)
			return GameConstants.noResource;
		p.addMoney(-3);
		p.addHelper(1);
		p.setSecondaryAction(p.getSecondaryAction()-1);
		return GameConstants.ok;		
	}
	//ACTION 6
	/**
	 * @param regionIndex index of the region where you want to change visible permit cards
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int changePermitCard(int regionIndex){
		Player p=game.getPlayer(game.getWhoseTurn());
		if(p.getHelper()<=0)
			return GameConstants.noResource;
		if(regionIndex>game.sizeRegion())
			return GameConstants.error;
		p.addHelper(-1);
		game.getRegion(regionIndex).changePermitCardsVisible();
		p.setSecondaryAction(p.getSecondaryAction()-1);
		return GameConstants.ok;
	}
	//ACTION 7
	/**
	 * @param councillor the councillor that you want to add in the balcony
	 * @param balcony the balcony where you want to add the councillor
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int electCouncillorByAnHelper(int councillor,int balcony){
		Player p=game.getPlayer(game.getWhoseTurn());
		if(p.getHelper()<1)
			return GameConstants.noResource;
		int result=electCouncillor(councillor, balcony);
		game.getPlayer(game.getWhoseTurn()).setMainAction(game.getPlayer(game.getWhoseTurn()).getMainAction()+1);
		p.setSecondaryAction(p.getSecondaryAction()-1);
		return result;
	}	
	//ACTION 8
	/**
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int doNewMainAction(){
		Player p=game.getPlayer(game.getWhoseTurn());
		if(p.getHelper()<3)
			return GameConstants.noResource;
		p.addHelper(-3);
		p.setMainAction(p.getMainAction()+1);
		p.setSecondaryAction(p.getSecondaryAction()-1);
		return GameConstants.ok;
	}
	
	//REWARD
	/**
	 * @return true if the player doesn't have other rewards to manage, false if some reward requires a choice of the player
	 */
	public boolean manageReward(){
		Player player=game.getPlayer(game.getWhoseTurn());
		while(player.sizeReward()>0)
		{
			Reward reward=player.getReward();
			player.removeReward();
			//money
			player.addMoney(reward.getMoney());
			//helper
			player.addHelper(reward.getHelper());
			//scorePoint
			player.addScorePoints(reward.getScorePoint());
			//politicalCard
			for(int j=0;j<reward.getPoliticalCard();j++)
			{
				PoliticalCard politicalCard=game.pickUpPoliticalCard();
				player.addPoliticalCard(politicalCard);
			}
			//other main action
			player.setMainAction(player.getMainAction()+reward.getAnotherMainAction());
			//nobility
			incrementNobilityAndTakeNewReward(player,reward.getNobility());
			try
			{
				//permitCard
				if(reward.getPermitCards()>0)
				{
					player.getConnection().selectPermitCard();
					return false;
				}
				//bonus permit card
				if(reward.getbonusPermitCards()>0)
				{	
					player.getConnection().select1BonusPermit();
					return false;
				}
				//bonus town 1
				if(reward.getBonusTown()==1)
				{
					player.getConnection().select1BonusToken();
					return false;
				}
				//bonus town 2
				if(reward.getBonusTown()==2)
				{
					player.getConnection().select2BonusToken();
					return false;
				}
			}catch(Exception e)
			{
				System.err.println("ERROR="+e);
			}
		}
		return true;
	}

	private void incrementNobilityAndTakeNewReward(Player player, int increment) {
		int nobilityStep=player.getNobility();
		for(int i=0;i<increment;i++)
		{
			Reward reward=game.getNobilityBonus(nobilityStep);
			player.addReward(reward);
			nobilityStep++;
		}
		player.addNobility(increment);
	}

	/**
	 * @param token the name of town of which you want to take the bonus
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int selectOneBonusToken(String token)  {
		Towns town=findTown(token);
		if(town==null)
			return GameConstants.error;
		Player player=game.getPlayer(game.getWhoseTurn());
		if(town.thePlayerHasEmporium(player.getName()))
			return GameConstants.error;
		player.addReward(town.getRewardCity());
		return GameConstants.ok;
	}

	/**
	 * @param token1 the name of town of which you want to take the bonus
	 * @param token2 the name of town of which you want to take the bonus
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int selectTwoBonusToken(String token1, String token2) {
		Towns town1=findTown(token1);
		Towns town2=findTown(token2);
		if(town1==null || town2==null)
			return GameConstants.error;
		Player player=game.getPlayer(game.getWhoseTurn());
		if(town1.thePlayerHasEmporium(player.getName()) && town2.thePlayerHasEmporium(player.getName()))
			return GameConstants.error;
		player.addReward(town1.getRewardCity());
		player.addReward(town2.getRewardCity());
		return GameConstants.ok;
	}

	/**
	 * @param bonusPermit permit card chosen to take the bonus again
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int selectBonusPermitCard(int bonusPermit) {
		Player player=game.getPlayer(game.getWhoseTurn());
		if(bonusPermit>=player.permitDeckSize())
			return GameConstants.error;
		player.addReward(player.getPermitCard(bonusPermit).getReward());
		return GameConstants.ok;
	}

	/**
	 * @param indexPermitCard permit card chosen (0->first permit card in the first region, n -> last permit card in the last region)
	 * @return the value indicates what the method returns (OK, error or noResource)
	 */
	public int selectPermitCard(int indexPermitCard)  {
		int permitCardsForRegion=GameConstants.cardsVisible;
		if(permitCardsForRegion>=game.sizeRegion())
			return GameConstants.error;
		Region region=game.getRegion(indexPermitCard/permitCardsForRegion);
		PermitCard permitCard=region.pickUpPermitCardVisible(indexPermitCard%permitCardsForRegion);
		Player player=game.getPlayer(game.getWhoseTurn());
		player.addPermitCard(permitCard);
		return GameConstants.ok;
	}

	//MARKET
	/*
	 * We have an item in arraylist for every player's offer and every item is a string vector build in this way:
	 * {namePlayer, what he offers:(permitCardNumber, politicalCardNumber, quantityOfHelper), which or how much, moneyHeWants}
	 * {Angelo,0,0,5} -> Angelo offers you permit card number 0 for 5 money
	 */
	/**
	 * @param offert the offer that you want to buy
	 * @return he value indicates what the method ends (OK, error or noResource)
	 */
	public int buyOffert(String[]offert){
		Player player=game.getPlayer(game.getPlayerTurnBuying());
		if(player.getMoney()<Integer.parseInt(offert[3]))
			return GameConstants.noResource;
		if(player.getName().equals(offert[0]))
			return GameConstants.error;
		Player whoOffer=game.findPlayer(offert[0]);
		int money=Integer.parseInt(offert[3]);
		int indexCard=Integer.parseInt(offert[2]);
		if(offert[1].equals("0"))
		{//permit card
			player.addPermitCard(whoOffer.getPermitCard(indexCard));
			whoOffer.removePermitCard(indexCard);
		}
		if(offert[1].equals("1"))
		{//political card
			player.addPoliticalCard(whoOffer.getPoliticalCard(indexCard));
			whoOffer.removePoliticalCard(indexCard);
		}
		if(offert[1].equals("2"))
		{//helper
			int helper=Integer.parseInt(offert[2]);
			player.addHelper(helper);
			whoOffer.addHelper(-1*helper);
		}
		whoOffer.addMoney(money);
		player.addMoney(-1*money);
		return GameConstants.ok;
	}
	
	//FINISH GAME
	/**
	 * This method counts the last Score Point and then calls other methods for building the rank
	 * @return the rank of the game
	 */
	public String[][] finishGame(){
		//I see who has more nobility points
		int first=0, countFirst=0, indexFirst=0;
		int second=0, countSecond=0, indexSecond=0;
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			Player p=game.getPlayer(i);
			if(p.getNobility()>second)
			{
				second=p.getNobility();
				if(second>first)
				{
					second=first;
					first=p.getNobility();
				}
			}
		}
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			Player p=game.getPlayer(i);
			if(p.getNobility()==first)
			{
				countFirst++;
				indexFirst=i;
			}
			if(p.getNobility()==second)
			{
				indexSecond=i;
				countSecond++;
			}
		}
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			Player p=game.getPlayer(i);
			if(p.getNobility()==first)
			{
				p.addScorePoints(GameConstants.pointForFirst);
			}
		}
		if(countSecond>1 && countFirst==1)
		{
			for(int i=0;i<game.numberOfPlayers();i++)
			{
				Player p=game.getPlayer(i);
				if(p.getNobility()==second)
				{
					p.addScorePoints(GameConstants.pointForSecond);
				}
			}
		}
		//I see who has more permit card
		first=0;
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			Player p=game.getPlayer(i);
			if(p.permitDeckSize()>0)
			{
				first=p.permitDeckSize();
			}
		}
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			Player p=game.getPlayer(i);
			if(p.permitDeckSize()==first)
			{
				p.addScorePoints(GameConstants.pointForPermitCard);
			}
		}
		//I see if there are more people with the same points in first position
		int max=0;
		ArrayList<Player>players=new ArrayList<Player>();
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			Player p=game.getPlayer(i);
			if(p.getScorePoints()==max)
				players.add(p);
			if(p.getScorePoints()>max)
			{
				players.clear();
				players.add(p);
				max=p.getScorePoints();
			}
		}
		if(players.size()>1)
		{
			int maxHelperAndCards=0, quantity=0, count=0;
			for(int i=0;i<players.size();i++)
			{
				if(players.get(i).politicalDeckSize()+players.get(i).getHelper()==quantity)
					count++;
				if(players.get(i).politicalDeckSize()+players.get(i).getHelper()>quantity)
				{
					quantity=players.get(i).politicalDeckSize()+players.get(i).getHelper();
					maxHelperAndCards=i;
					count=1;
				}
			}
			if(count==1)
				players.get(maxHelperAndCards).addScorePoints(1);
		}
		return buildRank();
	}
	private String[][] buildRank(){
		String[][]rank=new String[game.numberOfPlayers()][2];
		for(int i=0;i<game.numberOfPlayers();i++)
		{
			rank[i][0]=game.getPlayer(i).getName();
			rank[i][1]=game.getPlayer(i).getScorePoints()+"";
		}
		return rank;
	}
	
}
