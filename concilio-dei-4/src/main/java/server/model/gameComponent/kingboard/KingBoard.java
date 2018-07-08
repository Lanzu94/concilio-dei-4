package server.model.gameComponent.kingboard;

import java.io.Serializable;
import java.util.ArrayList;

import server.model.gameComponent.cards.BonusReward;
import server.model.gameComponent.cards.KingReward;

public class KingBoard implements Serializable{
	
	//KingRewards
	private ArrayList<KingReward> kingRewards = new ArrayList<KingReward>();
	public void addKingReward(KingReward a){
		kingRewards.add(a);
	}
	public KingReward pickUpKingReward(){
		KingReward kr=kingRewards.get(0);
		kingRewards.remove(0);
		kingRewards.trimToSize();
		return kr;
	}
	public int getKingReward(int i){
		return kingRewards.get(i).getKingRewardValue();
	}
	public int kingRewardSize(){
		return kingRewards.size();
	}

	
	//BonusTiles
	private ArrayList<BonusReward> bonusRewards = new ArrayList<BonusReward>();
	public void addBonusReward(BonusReward a){
		bonusRewards.add(a);
	}
	public BonusReward pickUpBonusReward(int i){
		BonusReward br=bonusRewards.get(i);
		return br;
	}
	public BonusReward getBonusReward(int i){
		return bonusRewards.get(i);
	}
 	public int bonusRewardSize(){
		return bonusRewards.size();
	}
	
	//CouncilBalconies
	private ArrayList <CouncilBalcony> councilBalconies=new ArrayList<CouncilBalcony>();
	
	public void addCouncilBalcony(CouncilBalcony a) {
		councilBalconies.add(a);
	}
	public int howManyBalconies(){
		return councilBalconies.size();
	}
	public CouncilBalcony getCouncilBalcony(int i){
		return councilBalconies.get(i);
	}

	//Councillors out of council balconies
	private ArrayList<Councillor> councillors = new ArrayList<Councillor>();
	public void addCouncillorOutOfBalconies(Councillor a){
		councillors.add(a);
	}
	public int howManyCouncillorsOutOfBalconies(){
		return councillors.size();
	}
	public Councillor getCouncillorOutOfBalconies(int i){
		return councillors.get(i);
	}
	public void removeCouncillor(int i){
		councillors.remove(i);
	}
}
