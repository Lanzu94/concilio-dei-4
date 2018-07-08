package server.model.gameComponent.cards;

import java.io.Serializable;

public class KingReward implements Serializable{
	/*
	 * This class rappresent King Reward (there are 5 King Reward in the game standard)
	 */
	int value;
	public KingReward(int i){
		value=i;
	}
	public int getKingRewardValue(){
		return value;
	}
}
