package server.model.gameComponent.cards;

import java.io.Serializable;
import java.util.ArrayList;

import server.model.gameComponent.Reward;

public class PermitCard implements Serializable{
	/*
	 * this class rappresent the permit cards and include every city when the permit can build
	 */
	private ArrayList <String> towns=new ArrayList <String>();
	private Reward reward;
	private boolean isUsed;
	public PermitCard(){
		isUsed=false;
	}
	public void cardIsUsed(){
		isUsed=true;
	}
	public boolean getIsUsed(){
		return isUsed;
	}
	
	//Town
	public void addTown(String t){
		towns.add(t);
	}
	public String getTown(int index){
		return towns.get(index);
	}
	public int howManyTown(){
		return towns.size();
	}
	public String getAllTowns(){
		String s="";
		for(int i=0;i<towns.size();i++)
			s=s+towns.get(i);
		return s;
	}
	
	//Reward
	public void setReward(Reward r){
		reward=r;
	}
	public Reward getReward(){
		return reward;
	}
}
