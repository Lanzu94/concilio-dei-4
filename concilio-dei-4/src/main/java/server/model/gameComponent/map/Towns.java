package server.model.gameComponent.map;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import server.model.Player;
import server.model.gameComponent.Reward;

public class Towns implements Serializable{

	private String colorName;
	private char name;
	private Color color;
	
	public Towns(char n, String c, Color color){
		name=n;
		colorName=c;
		this.color=color;
		rewardCity=new Reward();
	}
	public char getName(){
		return name;
	}
	public String getColorName(){
		return colorName;
	}
	public Color getColor(){
		return color;
	}
	
	//Connection
	private ArrayList<Towns>connections=new ArrayList<Towns>();
	public void addConnection(Towns t){
		connections.add(t);
	}
	public int sizeConnection(){
		return connections.size();
	}
	public Towns getConnection(int i){
		return connections.get(i);
	}
	
	//Reward
	private Reward rewardCity;
	private boolean rewardVisible;
	public void setRewardCity(Reward r){
		rewardCity=r;
		rewardVisible=false;//At the beginning of the game every Reward is invisible
	}
	public Reward getRewardCity(){
		return rewardCity;
	}
	
	public boolean getRewardVisible(){
		return rewardVisible;
	}
	public void setVisibleReward(){
		rewardVisible=true;
	}
	
	//Emporium
	private ArrayList<Player> ownersEmporium= new ArrayList<Player>();//I save the player that have an Emporium in this town
	public void newEmporium(Player p){
		ownersEmporium.add(p);
	}
	public int howManyEmporium(){
		return ownersEmporium.size();
	}
	public Player ownerEmporium(int i){
		return ownersEmporium.get(i);
	}
	public boolean thePlayerHasEmporium(String name){
		for(int i=0;i<ownersEmporium.size();i++)
			if(name.equals(ownersEmporium.get(i).getName()))
				return true;
		return false;
	}
}
