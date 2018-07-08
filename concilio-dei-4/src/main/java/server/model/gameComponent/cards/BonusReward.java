package server.model.gameComponent.cards;

import java.io.Serializable;
import java.util.ArrayList;

public class BonusReward implements Serializable {
	/*
	 * This class rappresent the Bonus Card (There are 7 Bonus Card in the game standard)
	 */
	private ArrayList<String> towns=new ArrayList <String>();//the towns where a player must build to take this bonus Reward
	private int scorePoint;
	public BonusReward(int i){
		scorePoint=i;
	}
	public int getScorePoints(){
		return scorePoint;
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
	//Check game
	public boolean playerCanTakeBonus(String t){
		if(t.length()!=towns.size())
			return false;
		for(int i=0;i<t.length();i++)
			if(!towns.get(i).equals(t.charAt(i)+""))
				return false;
		return true;
	}
}
