package client.view;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.Timer;

import client.model.AbbreviationRewardType;
import client.model.EnumPage;
import client.model.EnumStatusGame;
import client.model.LocalStateGame;
import client.model.Page;

/**
 * This class creates and print the string in the Command Line
 */
public class CLI extends Observable implements Observer, Serializable{
	
	private String option;
	transient private Thread thread;
	public CLI()
	{
		option="0";
		inputUsed=0;
	}
	@Override
	public void update(Observable o, Object arg) {
		Page p=(Page)o;
		if(arg.toString().length()>0)//I have some date
			printOnVideo(p,arg);
		else//I don't have date
			printOnVideo(p,"");
	}
	
	private void printOnVideo(Page page, Object d)
	{
		boolean optionInsert=false;
		String stampa="";
		EnumPage p=page.getPage();
		if(p.equals(EnumPage.main_menu))
		{
			stampa="Welcome to The Council\n"
					+ "What do you want to do?\n"
					+ "1) Play Online\n"
					+ "2) Connection mode";
			optionInsert=true;
		}
		if(p.equals(EnumPage.connection_mode) || p.equals(EnumPage.selected_rmi) || p.equals(EnumPage.selected_socket))
		{
			stampa="Choose the connection mode or exit:\n"
					+ "1) RMI\n"
					+ "2) Socket\n"
					+ "3) Back to menu\n";
			optionInsert=true;
		}
		if(p.equals(EnumPage.selected_rmi))
		{
			stampa=stampa+"You selected RMI";
			optionInsert=true;
		}
		if(p.equals(EnumPage.selected_socket))
		{
			stampa=stampa+"You selected Socket";
			optionInsert=true;
		}
		if(p.equals(EnumPage.play_online))
		{
			stampa="Insert name and password to play online (in this way: name password)\n"
					+ "If you were in a game and the game isn't finished, you will enter in the same game";
			optionInsert=true;
		}
		if(p.equals(EnumPage.name_not_valid))
		{
			stampa="This name is not avaiable or your password is wrong!\n"
					+ "Insert another name to play online and press Enter";
			optionInsert=true;
		}
		if(p.equals(EnumPage.menu_online))
		{
			String date=d.toString();
			if(!"".equals(date))
				stampa="Name Confirmed, there are "+date+" players online\n";
			stampa=stampa+"Choose the option that you want and press Enter:\n"
					+ "1) New Game\n"
					+ "2) Join to some game\n"
					+ "3) List of default maps";
				
			optionInsert=true;
		}
		if(p.equals(EnumPage.update_menu_online))
		{
			String date=d.toString();
			
			stampa="New player connected, there are "+date+" players online\n"
					+ "Now choose the option that you want and press Enter:\n"
					+ "1) New Game\n"
					+ "2) Join to some game\n"
					+ "3) List of default maps";
			optionInsert=true;
		}
		if(p.equals(EnumPage.how_many_players))
		{
			stampa="How many player do you want in the match and which map?\n"
					+ "Insert in this way: MinNumber-MaxNumber-MapNumber and press Enter";
			optionInsert=true;
		}
		if(p.equals(EnumPage.lobby))
		{
			String date=d.toString();
			stampa="In the lobby there are "+date+" players\n"
					+ "Wait for other players...";
		}
		if(p.equals(EnumPage.join_game))
		{
			long [][]list=(long[][]) d;
			stampa="There is the list of the games written in this way: player ready/max player code game\n"
					+ "Insert the number of game or M to back to menu and press Enter:\n";
			for(int i=0;i<list.length;i++)
			{
				if(list[0][4]==1)
					stampa=stampa+i+") "+list[i][0]+"/"+list[i][1]+" CODE="+list[i][2]+" MAP="+list[i][3]+"\n";
			}
			optionInsert=true;
		}
		if(p.equals(EnumPage.list_of_maps))
		{
			String[][][]maps=(String[][][])d;
			stampa=printListOfMaps(maps);
			optionInsert=true;
		}
		if(p.equals(EnumPage.game_status))
		{
			LocalStateGame local=page.getLocalStateGame();
			EnumStatusGame statusGame=local.getStatusGame();
			if(statusGame.equals(EnumStatusGame.gameStart)){
				stampa="The game is starting...\n";
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.map))
			{//print the map
				stampa=printMap(local);
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.permitCards))
			{//print the visible permit cards and the permit cards that remain in total
				stampa=printPermitCard(local);
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.kingBoard))
			{//print kingboard: council balcony, king bonus and bonus reward
				stampa=printKingBoard(local);
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.nobilityRoad))
			{//print the reward for every step in nobility road
				stampa=printNobilityRoad(local);
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.opponentStatus))
			{//print the status of opponent: money, scorePoint ecc..
				stampa=printOpponentStatus(local);
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.playerStatus))
			{//print the status of player: money, politicalCard ecc..
				stampa=printPlayerStatus(local);
				optionInsert=false;
			}
			//action 1 Change Councillor
			if(statusGame.equals(EnumStatusGame.listCouncillorOutOfBalconies))
			{//print the list of council that are out of balconies
				stampa=printListCouncillorOutOfBalconies(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.balconyChose))
			{
				stampa=printAllBalconies(local);
				optionInsert=true;
			}
			//action 2 Buy Permit Card
			if(statusGame.equals(EnumStatusGame.balconyChoseForCorrupt))
			{
				stampa=balconyChoseForCorrupt(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.politicalCardChosen))
			{
				stampa=politicalCardChosen(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.whichPermitCardGet))
			{
				stampa=whichPermitCardGet(local);
				optionInsert=true;
			}
			//action 3 Build an Emporium By Permit
			if(statusGame.equals(EnumStatusGame.indexPermitCard))
			{
				stampa=indexPermitCard(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.townNameChosed))
			{
				stampa=townNameChosed(local);
				optionInsert=true;
			}
			//action 4 Build an Emporium By King
			if(statusGame.equals(EnumStatusGame.politicCardsUseToKing))
			{
				stampa=politicalCardsUseToKing(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.townChosenToKing))
			{
				stampa=townChosenToKing(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.routeToFollow))
			{
				stampa=routeToFollow(local);
				optionInsert=true;
			}
			//action 6 Change Permit Build Card
			if(statusGame.equals(EnumStatusGame.regionChose))
			{
				stampa=regionChose(local);
				optionInsert=true;
			}
			//action 7 Use an Helper To Change Councillor
			if(statusGame.equals(EnumStatusGame.listCouncillorFreeByHelper))
			{
				stampa=listCouncillorFreeByHelper(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.balconyChoseByHelper))
			{
				stampa=balconyChoseByHelper(local);
				optionInsert=true;
			}
			//action for chose the bonus in nobility road
			if(statusGame.equals(EnumStatusGame.select1BonusToken))
			{
				stampa=select1BonusToken(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.select2BonusToken))
			{
				stampa=select2BonusToken(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.select1PermitCard))
			{
				stampa=select1PermitCard(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.select1BonusPermitCard))
			{
				stampa=select1BonusPermitCard(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.resourceNotEnought))
			{//I print one message and ask to player to insert again the option
				stampa="You don't have enough resources";
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.errorInsertedDate))
			{//I print one message and ask to player to insert again the option
				stampa="You insert not valid data";
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.opponentTurn))
			{
				String nameOpponent=d.toString();
				stampa="it's "+nameOpponent+" turn";
				optionInsert=false;
			}
			//status game during the market period
			if(statusGame.equals(EnumStatusGame.marketStart))
			{
				stampa="The market is starting...\n";
				stampa=stampa+turnDoneOffert();
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.turnDoneOffert))
			{
				stampa="Offer Added!\n";
				stampa=stampa+turnDoneOffert();
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.waitTurnDoneOffertFinish))
			{
				stampa="Wait that other players finish their turn...";
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.resourcesNotEnoughtToOffert))
			{
				stampa="You don't have the resources that you tried to offer";
				optionInsert=false;
			}
			if(d.equals(EnumStatusGame.timerMarket))
			{
				stampa="Time left="+page.getTimer();
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.yourTurnToBuy))
			{
				stampa=yourTurnToBuy(local);
				optionInsert=true;
			}
			if(statusGame.equals(EnumStatusGame.opponentTurnToBuy))
			{
				String nameOpponent=d.toString();
				stampa=nameOpponent+" is buying";
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.resourcesNotEnoughtToBuy))
			{
				stampa="You don't have enough money to buy this thing";
				optionInsert=false;
			}
			if(statusGame.equals(EnumStatusGame.legend))
			{
				stampa=legend();
				optionInsert=false;
			}
		}
		if(p.equals(EnumPage.your_turn))
		{
			LocalStateGame local=page.getLocalStateGame();
			stampa="It's your turn, select an action and press enter:\n";
			stampa=stampa+printAction(local);
			stampa=stampa+"9) Print Game Status\n";
			stampa=stampa+"10) Print your status\n"
					+ "11) Pass turn\n"
					+ "12) Legend of Letters\n";
			stampa=stampa+"Attention!! If you insert M anywhere you return in the main menu where "
					+ "you can choose the action to do";
			optionInsert=true;
		}
		if(d.equals(EnumStatusGame.timerTurn))
		{//this method is not used
			stampa="Time left="+page.getTimer();
			optionInsert=false;
		}
		if(p.equals(EnumPage.finish_game))
		{
			String[][]rank=(String[][])d;
			stampa=finishGame(rank);			
			optionInsert=true;
		}
		System.out.println(stampa);
		if(optionInsert && inputUsed==0)
		{
			thread=new Thread(new ThreadInsert());
			thread.start();
		}
	}

	private String legend() {
		String s="";
		s="M=Money\n"
				+ "PC=Political Cards\n"
				+ "H=Helper\n"
				+ "N=Nobility Road Points\n"
				+ "SP=Score Points\n"
				+ "MA=Main Action\n"
				+ "PB=Permit Build Cards\n"
				+ "BPB=Bonus Permit Build cards\n"
				+ "BT=Bonus Town\n"
				+ "QBPC=Quantity of Build Permit Cards\n"
				+ "QPC=Quantity of Political Cards\n"
				+ "QE=Quantity of Emporium not used\n";
		return s;
	}
	private String printListOfMaps(String[][][] maps) {
		String s="";
		for(int m=0;m<maps.length;m++)
		{
			s=s+"map number "+(m)+"\n";
			for(int i=0;i<maps[m].length;i++)
			{
				s=s+(char)('A'+i);
				s=s+":"+maps[m][i][0]+":"+maps[m][i][1];
				s=s+"\n";
			}
			s=s+"\n";
		}
		return s;
	}

	/*I use the next variable to know if there is a request insert input that player doensn't use*/
	private class  ThreadInsert implements Runnable{
		@Override
		public void run() {
			optionInsert();
		}
		
	}
	private int inputUsed;
	public void optionInsert()
	{
		option="0";
		Scanner scanner=new Scanner(System.in);
		inputUsed=1;
		option=scanner.nextLine();
		inputUsed=0;
		setChanged();
	    notifyObservers(option);
	}

	private String printAction(LocalStateGame p){
		String s="";
		s=s+"Main action avaiable:"+p.getMainActionRemain()+"\t";
		s=s+"Secondary action avaiable:"+p.getSecondaryAction()+"\n";
		if(p.getMainActionRemain()>0)
			s=s+"Primary Action:\n"
				+ "1) Change Councillor\n"
				+ "2) Buy Permit Build\n"
				+ "3) Build an Emporium by Permit\n"
				+ "4) Build an Emporium by King\n";
		if(p.getSecondaryAction()>0){
			s=s+"Secondary Action:\n"
				+ "5) Engange an Helper\n"
				+ "6) Change Permit Build Card\n"
				+ "7) Use an Helper to Change a Councillor\n"
				+ "8) Do an other Main Action\n";
		}
		return s;
	}
	private String printMap(LocalStateGame l){
		//Format -> A(M0,H0,PC0,N0,SP0):BC:(Angelo;Alessio)
		String [][]map=l.getMap();
		String[]nameRegion=l.getRegionName();
		String whereIsKing=l.getWhereIsKing();
		int townsForRegion=map.length/nameRegion.length;
		String s="MAP\n";
		for(int i=0;i<nameRegion.length;i++)
		{
			s=s+nameRegion[i]+"\n";
			for(int j=0;j<townsForRegion;j++)
			{
				int index=j+townsForRegion*i;
				s=s+(char)('A'+index);
				s=s+":"+map[index][7];
				s=s+"(";
				if(!"0".equals(map[index][1])){
					s=s+AbbreviationRewardType.M+map[index][1]+",";
				}
				if(!"0".equals(map[index][2])){
					s=s+AbbreviationRewardType.H+map[index][2]+",";
				}
				if(!"0".equals(map[index][3])){
					s=s+AbbreviationRewardType.PC+map[index][3]+",";
				}
				if(!"0".equals(map[index][4])){
					s=s+AbbreviationRewardType.N+map[index][4]+",";
				}
				if(!"0".equals(map[index][5])){
					s=s+AbbreviationRewardType.SP+map[index][5]+",";
				}
				if(s.charAt(s.length()-1)==',')
					s=s.substring(0,s.length()-1);
				s=s+"):"+map[index][0]+":";
				s=s+"("+map[index][6]+")";
				s=s+"\n";
			}
		}
		s=s+"Town whese is the King="+whereIsKing+"\n";
		return s;
	}
	private String printPermitCard(LocalStateGame l)
	{
		//Format -> NameRegion
		//AB (M0,H0,PC0,N0,SP0.MA0)
		String s="";
		s="PERMIT CARDS"+"\n";
		String[]nameRegion=l.getRegionName();
		String[][]permitCard=l.getPermitCardsTown();
		
		int permitCardForRegion=permitCard.length/nameRegion.length;
		
		for(int i=0;i<nameRegion.length;i++)
		{
			s=s+nameRegion[i]+"\n";
			for(int j=0;j<permitCardForRegion;j++)
			{
				int index=j+i*permitCardForRegion;
				s=s+permitCard[index][0]+"(";
				if(!"0".equals(permitCard[index][1])){
					s=s+AbbreviationRewardType.M+permitCard[index][1]+",";
				}
				if(!"0".equals(permitCard[index][2])){
					s=s+AbbreviationRewardType.H+permitCard[index][2]+",";
				}
				if(!"0".equals(permitCard[index][3])){
					s=s+AbbreviationRewardType.PC+permitCard[index][3]+",";
				}
				if(!"0".equals(permitCard[index][4])){
					s=s+AbbreviationRewardType.N+permitCard[index][4]+",";
				}
				if(!"0".equals(permitCard[index][5])){
					s=s+AbbreviationRewardType.SP+permitCard[index][5]+",";
				}
				if(!"0".equals(permitCard[index][6])){
					s=s+AbbreviationRewardType.MA+permitCard[index][6]+",";
				}
				if(s.charAt(s.length()-1)==',')
					s=s.substring(0,s.length()-1);
				s=s+")\n";
			}
		}
		return s;
	}
	private String printKingBoard(LocalStateGame local) {
		String s="KING BOARD\n";
		s=s+"Balconies:\n";
		String[]nameRegion=local.getRegionName();
		String[][]balconies=local.getCouncilBalconyName();
		for(int i=0;i<nameRegion.length;i++)
		{
			s=s+i+")"+nameRegion[i]+":";
			for(int j=0;j<balconies[i].length;j++)
			{
				s=s+balconies[i][j]+"-";
			}
			s=s+">\n";
		}
		//now I print the council of the king
		s=s+nameRegion.length+") KING:";
		for(int j=0;j<balconies[nameRegion.length].length;j++)
		{
			s=s+balconies[nameRegion.length][j]+"-";
		}
		s=s+">\n";
		//now print the king reward
		s=s+"King reward avaiable=";
		int[]kingReward=local.getKingReward();
		for(int i=0;i<kingReward.length-1;i++)
		{
			s=s+kingReward[i]+"-";
		}
		s=s+kingReward[kingReward.length-1]+"\n";
		//now I print the bonus Reward
		s=s+"Bonus reward avaiable= ";
		String[][]bonusReward=local.getBonusReward();
		for(int i=0;i<bonusReward.length-1;i++)
		{
			s=s+bonusReward[i][0]+"("+bonusReward[i][1]+") - ";
		}
		s=s+bonusReward[bonusReward.length-1][0]+"("+bonusReward[bonusReward.length-1][1]+")";
		s=s+"\n";
		return s;
	}
	private String printNobilityRoad(LocalStateGame local) {
		// I print 10 step for row (2 row in total)
		String s="NOBILITY ROAD\n";
		int[][]nobilityRoad=local.getBonusNobilityRoad();
		
		for(int i=0;i<nobilityRoad.length/10;i++)
		{
			for(int j=i*10;j<10+i*10;j++)
			{
				s=s+(j+1)+"-(";
				if(nobilityRoad[j][0]!=0){
					s=s+AbbreviationRewardType.M+nobilityRoad[j][0]+",";
				}
				if(nobilityRoad[j][1]!=0){
					s=s+AbbreviationRewardType.H+nobilityRoad[j][1]+",";
				}
				if(nobilityRoad[j][2]!=0){
					s=s+AbbreviationRewardType.PC+nobilityRoad[j][2]+",";
				}
				if(nobilityRoad[j][3]!=0){
					s=s+AbbreviationRewardType.N+nobilityRoad[j][3]+",";
				}
				if(nobilityRoad[j][4]!=0){
					s=s+AbbreviationRewardType.SP+nobilityRoad[j][4]+",";
				}
				if(nobilityRoad[j][5]!=0){
					s=s+AbbreviationRewardType.MA+nobilityRoad[j][5]+",";
				}
				if(nobilityRoad[j][6]!=0){
					s=s+AbbreviationRewardType.PB+nobilityRoad[j][6]+",";
				}
				if(nobilityRoad[j][7]!=0){
					s=s+AbbreviationRewardType.BPB+nobilityRoad[j][7]+",";
				}
				if(nobilityRoad[j][8]!=0){
					s=s+AbbreviationRewardType.BT+nobilityRoad[j][8]+",";
				}
				if(s.charAt(s.length()-1)==',')
					s=s.substring(0,s.length()-1);
				s=s+")  ";
			}
			s=s+"\n";
		}
		return s;
	}
	private String printOpponentStatus(LocalStateGame local) {
		String[][]statusOpponent=local.getStatusOpponent();
		String s="";
		for(int i=0;i<statusOpponent.length;i++)
		{
			s=s+(statusOpponent[i][0])+"(";
				
			s=s+AbbreviationRewardType.M+statusOpponent[i][1]+",";
			s=s+AbbreviationRewardType.H+statusOpponent[i][2]+",";
			s=s+AbbreviationRewardType.SP+statusOpponent[i][3]+",";
			s=s+AbbreviationRewardType.N+statusOpponent[i][4]+",";
			s=s+AbbreviationRewardType.QBPC+statusOpponent[i][5]+",";
			s=s+AbbreviationRewardType.QPC+statusOpponent[i][6]+",";
			s=s+AbbreviationRewardType.QE+statusOpponent[i][7];
				
			s=s+")";
			s=s+"\n";
		}
		return s;
	} 
	private String printPlayerStatus(LocalStateGame local) {
		//print player status
		String s="YOUR STATUS\n";
		String[] statusPlayer=local.getStatusPlayer();
		
		s=s+"Money="+statusPlayer[0]+" - ";
		s=s+"Helper="+statusPlayer[1]+"\n";
		s=s+"Nobility="+statusPlayer[2]+" - ";
		s=s+"ScorePoint="+statusPlayer[3]+"\n";
		//print politic cards
		String[]politicCards=local.getPoliticalCards();
		s=s+"Politic Cards=";
		for(int i=0;i<politicCards.length;i++)
		{
			s=s+"("+i+")"+politicCards[i]+"-";
		}
		if(s.charAt(s.length()-1)=='-')
			s=s.substring(0,s.length()-1);
		s=s+"\n";
		//print permit card
		String[][]permitCards=local.getPermitCardsTownsPlayer();
		s=s+"Permit Cards Avaiable=";
		for(int i=0;i<permitCards.length;i++)
		{
			if(permitCards[i][0].equals(Boolean.toString(false)))
				s=s+"("+i+")"+permitCards[i][1]+"-";
		}
		if(s.charAt(s.length()-1)=='-')
			s=s.substring(0,s.length()-1);
		return s+"\n";
	}
	
	//ACTION 1:Change (or elect) Councillor
	private String printListCouncillorOutOfBalconies(LocalStateGame local){
		/* I print the list of councillor out of balconies and player must chose
		 * which use to elect
		 */
		String []councillors=local.getCouncillorNameOutOfBalcony();
		String s="Select the councillor you want to elect:\n";
		for(int i=0;i<councillors.length-1;i++)
		{
			s=s+"("+i+")"+councillors[i]+" - ";
		}
		s=s+"("+(councillors.length-1)+")"+councillors[councillors.length-1];
		return s;
	}
	private String printAllBalconies(LocalStateGame local) {
		/* Print the only the balconies and player must chose which balcony want 
		 * to change
		 */
		String[][]balconies=local.getCouncilBalconyName();
		String[]nameRegion=local.getRegionName();
		String s="Select the balcony that you want to change\n";
		int cont=0;
		for(int i=0;i<nameRegion.length;i++)
		{
			s=s+"("+i+")"+nameRegion[i]+":";
			for(int j=0;j<balconies[i].length;j++)
			{
				s=s+balconies[i][j]+"-";
			}
			s=s+">\n";
			cont++;
		}
		//print the balcony king
		s=s+"("+cont+")KING:";
		for(int j=0;j<balconies[nameRegion.length].length;j++)
		{
			s=s+balconies[nameRegion.length][j]+"-";
		}
		s=s+">";
		return s;
	}
	//ACTION 2:Buy Permit Card 
	private String balconyChoseForCorrupt(LocalStateGame local) {
		/*Print just the region balconies, not the king balcony and player chose
		 * which balcony want corrupt to but permit card
		 */
		String[][]balconies=local.getCouncilBalconyName();
		String[]nameRegion=local.getRegionName();
		String s="Select the balcony that you want to corrupt with political cards\n";
		for(int i=0;i<nameRegion.length;i++)
		{
			s=s+"("+i+")"+nameRegion[i]+":";
			for(int j=0;j<balconies[i].length;j++)
			{
				s=s+balconies[i][j]+"-";
			}
			s=s+">\n";
		}
		return s;
	}
	private String politicalCardChosen(LocalStateGame local) {
		/* Print the political cards of the player and the player chose which
		 * cards he want to use
		 */
		String s="Which political card do you want to use to buy permit?\n"
				+ "Insert the cards in this way: 1-5-4, the money that you use depends"
				+ "on the cards that you select: 4 cards -> 0 money, 3 cards -> 4 money"
				+ "2 cards -> 7 money, 1 card -> 10 money\n";
		String[]politicCards=local.getPoliticalCards();
		s=s+"Your politic cards=";
		for(int i=0;i<politicCards.length;i++)
		{
			s=s+"("+i+")"+politicCards[i]+"-";
		}
		if(s.charAt(s.length()-1)=='-')
			s=s.substring(0,s.length()-1);
		return s;
	}
	private String whichPermitCardGet(LocalStateGame local) {
		/* Print all the permit cards belong to the region that player choose
		 * before
		 */
		int balconyChose=local.getBalconyChosenForCorruption();
		String[]nameRegion=local.getRegionName();
		String[][]permitCard=local.getPermitCardsTown();
		int permitCardForRegion=permitCard.length/nameRegion.length;
		String s="Which permit card do you want? Region="+nameRegion[balconyChose]+"\n";
		int index=0;
		for(int i=balconyChose*permitCardForRegion;i<balconyChose*permitCardForRegion+permitCardForRegion;i++)
		{
			s=s+index+") ";
			s=s+permitCard[i][0]+"(";
			if(!"0".equals(permitCard[i][1])){
				s=s+AbbreviationRewardType.M+permitCard[i][1]+",";
			}
			if(!"0".equals(permitCard[i][2])){
				s=s+AbbreviationRewardType.H+permitCard[i][2]+",";
			}
			if(!"0".equals(permitCard[i][3])){
				s=s+AbbreviationRewardType.PC+permitCard[i][3]+",";
			}
			if(!"0".equals(permitCard[i][4])){
				s=s+AbbreviationRewardType.N+permitCard[i][4]+",";
			}
			if(!"0".equals(permitCard[i][5])){
				s=s+AbbreviationRewardType.SP+permitCard[i][5]+",";
			}
			if(!"0".equals(permitCard[i][6])){
				s=s+AbbreviationRewardType.MA+permitCard[i][6]+",";
			}
			if(s.charAt(s.length()-1)==',')
				s=s.substring(0,s.length()-1);
			s=s+")\n";
			index++;
		}
		return s;
	}
	//ACTION 3:Build an Emporium By Permit
	private String indexPermitCard(LocalStateGame local) {
		String s="Which permit cards do you want to use? Insert the number\n";
		String[][]permitCards=local.getPermitCardsTownsPlayer();
		s=s+"Your permit cards avaiable=";
		for(int i=0;i<permitCards.length;i++)
		{
			if(permitCards[i][0].equals(Boolean.toString(false)))
				s=s+"("+i+")"+permitCards[i][1]+"-";
		}
		if(s.charAt(s.length()-1)=='-')
			s=s.substring(0,s.length()-1);
		return s;
	}
	private String townNameChosed(LocalStateGame local) {
		String s=printMap(local)+"\n";
		s="Insert the name of town where you want to build with the permit:A, B, C ecc..";
		return s;
	}
	//ACTION 4:Build an Emporium By King
	private String politicalCardsUseToKing(LocalStateGame local) {
		String s="Which political card do you want to use to corrupt King Council?\n"
				+ "Insert the cards in this way: 1-5-4, the money that you use depend"
				+ "of the cards that you select: 4 cards -> 0 money, 3 cards -> 4 money"
				+ "2 cards -> 7 money, 1 card -> 10 money\n";
		String[]politicCards=local.getPoliticalCards();
		s=s+"Your politic cards=";
		for(int i=0;i<politicCards.length;i++)
		{
			s=s+"("+i+")"+politicCards[i]+"-";
		}
		if(s.charAt(s.length()-1)=='-')
			s=s.substring(0,s.length()-1);
		return s;
	}
	private String townChosenToKing(LocalStateGame local) {
		String s=printMap(local)+"\n";
		s=s+"Insert the name of town where you want to build with the King:A, B, C ecc..";
		return s;
	}
	private String routeToFollow(LocalStateGame local) {
		String s=printMap(local);
		s=s+"Insert the road to follow in order to arrive in the destination Town (the road must include the"
				+ "town where the king starts and the town where the king must arrive)\n"
				+ "Example: King is in J and want to arrive in B, a possible road is -> JEHB";
		return s;
	}
	//ACTION 6:Change Permit Build Card
	private String regionChose(LocalStateGame local) {
		String s="In which region do you want change the visible permit cards?"
				+ " Insert the number:\n";
		String[]nameRegion=local.getRegionName();
		for(int i=0;i<nameRegion.length;i++)
		{
			s=s+i+") "+nameRegion[i]+"\n";
		}
		return s;
	}
	//ACTION 7 Use an Helper To Change Councillor
	private String listCouncillorFreeByHelper(LocalStateGame local) {
		String []councillors=local.getCouncillorNameOutOfBalcony();
		String s="Select the councillor you want to elect with the helper:\n";
		for(int i=0;i<councillors.length-1;i++)
		{
			s=s+"("+i+")"+councillors[i]+" - ";
		}
		s=s+"("+(councillors.length-1)+")"+councillors[councillors.length-1];
		return s;
	}
	private String balconyChoseByHelper(LocalStateGame local) {
		String[][]balconies=local.getCouncilBalconyName();
		String[]nameRegion=local.getRegionName();
		String s="Select the balcony that you want to change with the helper\n";
		for(int i=0;i<nameRegion.length;i++)
		{
			s=s+"("+i+")"+nameRegion[i]+":";
			for(int j=0;j<balconies[i].length;j++)
			{
				s=s+balconies[i][j]+"-";
			}
			s=s+">\n";
		}
		//print the balcony king
		s=s+"KING:";
		for(int j=0;j<balconies[nameRegion.length].length;j++)
		{
			s=s+balconies[nameRegion.length][j]+"-";
		}
		s=s+">";
		return s;
	}
	//Action for chose the bonus in nobility road
	private String select1BonusToken(LocalStateGame local) {
		String playerName=local.getPlayerName();
		String [][]map=local.getMap();
		String s="Which bonus do you want to take again? Insert the name of the city:A, B, C ecc..\n";
		for(int index=0;index<map.length;index++)
		{
			boolean emporiumPresent=false;
			String []owner=map[index][6].split(";");
			for(int j=0;j<owner.length;j++)
			{
				if(playerName.equals(owner[j]))
					emporiumPresent=true;
			}
			if(emporiumPresent)
			{//player has an Emporium in i city
				s=s+(char)('A'+index);
				s=s+"(";
				if(map[index][6].length()>0)//if there is at least 1 emporium
				{//the bonus token is visible
					if(!"0".equals(map[index][1])){
						s=s+AbbreviationRewardType.M+map[index][1]+",";
					}
					if(!"0".equals(map[index][2])){
						s=s+AbbreviationRewardType.H+map[index][2]+",";
					}
					if(!"0".equals(map[index][3])){
						s=s+AbbreviationRewardType.PC+map[index][3]+",";
					}
					if(!"0".equals(map[index][4])){
						s=s+AbbreviationRewardType.N+map[index][4]+",";
					}
					if(!"0".equals(map[index][5])){
						s=s+AbbreviationRewardType.SP+map[index][5]+",";
					}
					if(s.charAt(s.length()-1)==',')
						s=s.substring(0,s.length()-1);
				}
				s=s+")\n";
			}
		}
		return s;
	}
	private String select2BonusToken(LocalStateGame local) {
		String playerName=local.getPlayerName();
		String [][]map=local.getMap();
		String s="Which bonus do you want to take again? Insert the name of the 2 cities:A-B, D-F, C-G ecc..\n";
		for(int index=0;index<map.length;index++)
		{
			boolean emporiumPresent=false;
			String []owner=map[index][6].split(";");
			for(int j=0;j<owner.length;j++)
			{
				if(playerName.equals(owner[j]))
					emporiumPresent=true;
			}
			if(emporiumPresent)
			{//player has an Emporium in i city
				s=s+(char)('A'+index);
				s=s+"(";
				if(map[index][6].length()>0)//if there is at least 1 emporium
				{//the bonus token is visible
					if(!"0".equals(map[index][1])){
						s=s+AbbreviationRewardType.M+map[index][1]+",";
					}
					if(!"0".equals(map[index][1])){
						s=s+AbbreviationRewardType.H+map[index][2]+",";
					}
					if(!"0".equals(map[index][1])){
						s=s+AbbreviationRewardType.PC+map[index][3]+",";
					}
					if(!"0".equals(map[index][1])){
						s=s+AbbreviationRewardType.N+map[index][4]+",";
					}
					if(!"0".equals(map[index][1])){
						s=s+AbbreviationRewardType.SP+map[index][5]+",";
					}
					if(s.charAt(s.length()-1)==',')
						s=s.substring(0,s.length()-1);
				}
				s=s+")\n";
			}
		}
		return s;
	}
	private String select1PermitCard(LocalStateGame local) {
		String s="";
		s="Select the permit card that you want: insert the number\n";
		String[]nameRegion=local.getRegionName();
		String[][]permitCard=local.getPermitCardsTown();
		int permitCardForRegion=permitCard.length/nameRegion.length;
		
		for(int i=0;i<nameRegion.length;i++)
		{
			s=s+nameRegion[i]+"\n";
			for(int j=0;j<permitCardForRegion;j++)
			{
				int index=j+i*permitCardForRegion;
				s=s+"("+index+")"+permitCard[index][0]+"(";
				if(!"0".equals(permitCard[index][1])){
					s=s+AbbreviationRewardType.M+permitCard[index][1]+",";
				}
				if(!"0".equals(permitCard[index][2])){
					s=s+AbbreviationRewardType.H+permitCard[index][2]+",";
				}
				if(!"0".equals(permitCard[index][3])){
					s=s+AbbreviationRewardType.PC+permitCard[index][3]+",";
				}
				if(!"0".equals(permitCard[index][4])){
					s=s+AbbreviationRewardType.N+permitCard[index][4]+",";
				}
				if(!"0".equals(permitCard[index][5])){
					s=s+AbbreviationRewardType.SP+permitCard[index][5]+",";
				}
				if(!"0".equals(permitCard[index][6])){
					s=s+AbbreviationRewardType.MA+permitCard[index][6]+",";
				}
				if(s.charAt(s.length()-1)==',')
					s=s.substring(0,s.length()-1);
				s=s+")\n";
			}
		}
		return s;
	}
	private String select1BonusPermitCard(LocalStateGame local) {
		String s="Select the permit card of which you want the bonus again. Insert the number:\n";
		//print permit card
		String[][]permitCards=local.getPermitCardsTownsPlayer();
		s=s+"Your permit card=";
		for(int index=0;index<permitCards.length;index++)
		{
			s=s+"("+index+") "+permitCards[index][0]+" (";
			if(!"0".equals(permitCards[index][1])){
				s=s+AbbreviationRewardType.M+permitCards[index][1]+",";
			}
			if(!"0".equals(permitCards[index][2])){
				s=s+AbbreviationRewardType.H+permitCards[index][2]+",";
			}
			if(!"0".equals(permitCards[index][3])){
				s=s+AbbreviationRewardType.PC+permitCards[index][3]+",";
			}
			if(!"0".equals(permitCards[index][4])){
				s=s+AbbreviationRewardType.N+permitCards[index][4]+",";
			}
			if(!"0".equals(permitCards[index][5])){
				s=s+AbbreviationRewardType.SP+permitCards[index][5]+",";
			}
			if(!"0".equals(permitCards[index][6])){
				s=s+AbbreviationRewardType.MA+permitCards[index][6]+",";
			}
			if(s.charAt(s.length()-1)==',')
				s=s.substring(0,s.length()-1);
			s=s+")\n";
		}
		return s;
	}
	
	//MARKET
	private String turnDoneOffert() {
		String s="Insert you offer in this way firstNumber-secondNumber-thirdNumber (example: 1-2-5)\n"
				+ "- firstNumber: 0-permit card, 1-political card, 2-helper\n"
				+ "- secondNumber: which permit card/political card or how much helper you want to sell\n"
				+ "- thirdNumber: how much money do you want for your offert\n"
				+ "example: 1-1-5 -> you offer your political card number 1 for 5 money\n"
				+ "Or insert F to finish to do offert";
		return s;
	}
	private String yourTurnToBuy(LocalStateGame local) {
		String s="Select which offer you want to buy or insert F to finish offer (you can't buy you offert):\n";
		String[][]offert=local.getOfferts();
		for(int i=0;i<offert.length;i++)
		{
			s=s+i+") "+offert[i][0]+" offers ";
			if(offert[i][1].equals("0"))
			{//permit card
				String []tmp=offert[i][2].split(";");
				s=s+"one permit card to build in these cities:"+tmp[0];
				switch(tmp[1])
				{
				case "true":s=s+" (you can build with this card)";
				break;
				case "false":s=s+" (the card is used)";
				break;
				default:
					break;
				}
			}
			if(offert[i][1].equals("1"))
			{//political card
				s=s+"one political card with this color:"+offert[i][2];
			}
			if(offert[i][1].equals("2"))
			{//helper
				s=s+"these helpers:"+offert[i][2];
			}
			s=s+" for "+offert[i][3]+" money\n";
		}
		return s;
	}
	
	//END GAME
	private String finishGame(String[][] rank) {
		String s="GAME OVER\n";
		for(int i=0;i<rank.length;i++)
		{
			s=s+rank[i][0]+"="+rank[i][1]+"\n";
		}
		s=s+"Press enter to go back to the menu\n";
		return s;
	}
}