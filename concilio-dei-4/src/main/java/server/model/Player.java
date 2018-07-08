package server.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import server.controller.Connection;
import server.model.gameComponent.Reward;
import server.model.gameComponent.cards.PermitCard;
import server.model.gameComponent.cards.PoliticalCard;

/**
 * This class represents the player and saves important information about him like name and password
 */
public class Player implements Serializable{
	//Attributes for connection
	transient private Connection connection;

	private String name;
	private Color color;
	private EnumStatePlayer statePlayer;
	private long game_code;
	private String password;
	
	public Player(){
	}
	
	public Player(String name, String password,Color c){
		this.name=name;
		this.password=password;
		color=c;
		statePlayer=EnumStatePlayer.online;
	}
	
	public String getName(){
		return name;
	}
	public String getPassword(){
		return password;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public void setCodeGame(long l){
		game_code=l;
	}
	public long getCodeGame(){
		return game_code;
	}
	//Connection
	public void setConnection(Connection c){
		connection=c;
	}
	public Connection getConnection(){
		return connection;
	}
	
	public void setStatusPlayer(EnumStatePlayer p){
		statePlayer=p;
	}
	public EnumStatePlayer getStatePlayer(){
		return statePlayer;
	}
	
	//Methods I use to know if the player does some action
	private int mainActionRemain;
	private int secondaryActionRemain;
	public void setMainAction(int i){
		mainActionRemain=i;
	}
	public int getMainAction(){
		return mainActionRemain;
	}
	public void setSecondaryAction(int i){
		secondaryActionRemain=i;
	}
	public int getSecondaryAction(){
		return secondaryActionRemain;
	}
	
	//Method that I use during the game
	private int emporium;
	private int scorePoints;
	private int nobility; 
	private int money;
	private int helperCounts;
	private ArrayList <PoliticalCard> deckPoliticalCards=new ArrayList <PoliticalCard>();
	private ArrayList <PermitCard> deckPermitCards=new ArrayList <PermitCard>();
	
	public void setUpPlayer(){
		emporium=GameConstants.startEmporium;
		scorePoints=0;
		nobility=0;
		money=0;
		helperCounts=0;
		rewards=new ArrayList <Reward> ();
	}
	//money
	public void addMoney(int m){
		//m can be positive or negative
		money=money+m;
	}
	public int getMoney(){
		return money;
	}
	
	//scorePoint
	public void addScorePoints(int sp){
		scorePoints=scorePoints+sp;
	}
	public int getScorePoints(){
		return scorePoints;
	}
	//nobiliy
	public void addNobility(int n){
		nobility=nobility+n;
	}
	public int getNobility(){
		return nobility;
	}
	//helper 
	public void addHelper(int n){
		//n can be positive or negative
		helperCounts=helperCounts+n;
	}
	public int getHelper(){
		return helperCounts;
	}
	//emporium
	public void decrementEmporium(){
		emporium--;
	}
	public int getEmporium(){
		return emporium;
	}
	//Political Card
	public void addPoliticalCard(PoliticalCard p){
		deckPoliticalCards.add(p);
	}
	public PoliticalCard getPoliticalCard(int i){
		return deckPoliticalCards.get(i);
	}
	public void removePoliticalCard(int i){
		deckPoliticalCards.remove(i);
	}
	public void removePoliticalCard(ArrayList<PoliticalCard> p){
		int index=0;
		while(p.size()!=0)
		{
			if(p.get(0).getColorName().equals(deckPoliticalCards.get(index).getColorName()))
			{
				p.remove(0);
				deckPoliticalCards.remove(index);
				index=0;
			}
			else
				index++;
		}
	}
	public int politicalDeckSize(){
		return deckPoliticalCards.size();
	}
	//Permission Card
	public void addPermitCard(PermitCard p){
		deckPermitCards.add(p);
	}
	public PermitCard getPermitCard(int i){
		return deckPermitCards.get(i);
	}
	public void removePermitCard(int i){
		deckPermitCards.remove(i);
	}
	public int permitDeckSize(){
		return deckPermitCards.size();
	} 
	public int permitDeckSizeAvailable(){
		int cont=0;
		for(int i=0;i<permitDeckSize();i++)
		{
			if(deckPermitCards.get(i).getIsUsed()==false)
				cont++;
		}
		return cont;
	}
	
	//REWARD
	private ArrayList<Reward> rewards;
	public void addReward(Reward reward){
		rewards.add(reward);
	}
	public Reward getReward(){
		return rewards.get(0);
	}
	public void removeReward(){
		rewards.remove(0);
	}
	public int sizeReward(){
		return rewards.size();
	}
}
