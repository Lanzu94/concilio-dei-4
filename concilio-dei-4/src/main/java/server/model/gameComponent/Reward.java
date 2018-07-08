package server.model.gameComponent;

import java.io.Serializable;

public class Reward implements Serializable{
	/*
	 * This class rappresent the Reward that a player can puck up.
	 * A reward is composed by different thing: money, helper, politicalCard, nobility, scorePoint, anotherMainAction
	 */
	private int money;
	private int helper;
	private int politicalCard;
	private int nobility;
	private int scorePoint;
	private int anotherMainAction;
	private int permitCards;
	private int bonusPermitCards;
	private int bonusTown;
	public Reward(){
		money=0;
		helper=0;
		politicalCard=0;
		nobility=0;
		scorePoint=0;
		anotherMainAction=0;
		permitCards=0;
		bonusPermitCards=0;
		bonusTown=0;
	}
	
	public void setMoney(int i){
		money=i;
	}
	public int getMoney(){
		return money;
	}
	
	public void setHelper(int i){
		helper=i;
	}
	public int getHelper(){
		return helper;
	}
	
	public void setPoliticalCard(int i){
		politicalCard=i;
	}
	public int getPoliticalCard(){
		return politicalCard;
	}
	
	public void setNobility(int i){
		nobility=i;
	}
	public int getNobility(){
		return nobility;
	}
	
	public void setScorePoint(int i){
		scorePoint=i;
	}
	public int getScorePoint(){
		return scorePoint;
	}
	
	public void setAnotherMainAction(int i){
		anotherMainAction=i;
	}
	public int getAnotherMainAction(){
		return anotherMainAction;
	}
	
	public void setPermitCards(int i){
		permitCards=i;
	}
	public int getPermitCards(){
		return permitCards;
	}
	
	public void setbonusPermitCards(int i){
		bonusPermitCards=i;
	}
	public int getbonusPermitCards(){
		return bonusPermitCards;
	}
	
	public void setBonusTown(int i){
		bonusTown=i;
	}
	public int getBonusTown(){
		return bonusTown;
	}
}
