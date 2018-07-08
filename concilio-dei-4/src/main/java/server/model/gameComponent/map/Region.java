package server.model.gameComponent.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import server.model.GameConstants;
import server.model.gameComponent.cards.PermitCard;

public class Region implements Serializable{
	/*
	 * This class contains every method for the Region
	 */
	
	private TypeOfRegion[] types;
	private ArrayList<Towns> towns=new ArrayList <Towns>();
	private int type;//save the tipe of Region
	public Region(int t){
		types=TypeOfRegion.values();
		type=t;
	}
	public String getName(){//this method return the type of the Region = name
		return types[type].toString();
	}
	
	//method for towns
	public void addTown(Towns town){
		towns.add(town);
	}
	public Towns getTown(int i){
		return towns.get(i);
	}
	
	//method for BuildPermits
	private ArrayList <PermitCard> permitCards = new ArrayList <PermitCard>();
	private ArrayList <PermitCard> permitCardsVisible = new ArrayList <PermitCard>();
	
	public void addPermitCard(PermitCard a){
		permitCards.add(a);
	}
	private void addPermitCardVisible(){
		//When the player pick up permit card I add a new visible permit card
		if(sizeNotVisiblePermitCard()>0)
		{
			permitCardsVisible.add(permitCards.get(0));
			permitCards.remove(0);
		}
	}
	private void mixPermitCardsDeck(){
		ArrayList <PermitCard> tmp=new ArrayList <PermitCard> ();
		int size=permitCards.size();
		for(int i=0;i<size;i++)
		{
			Random x=new Random();
			int n=x.nextInt(permitCards.size());
			tmp.add(permitCards.get(n));
			permitCards.remove(n);
		}
		permitCards.addAll(tmp);
	}
 	public void setUpPermitCard(){
 		mixPermitCardsDeck();
		for(int i=0;i<GameConstants.cardsVisible;i++)
		{
			addPermitCardVisible();
		}
	}
	public PermitCard pickUpPermitCardVisible(int i){
		if(permitCardsVisible.size()==0)
			return null;
		PermitCard p=permitCardsVisible.get(i);
		permitCardsVisible.remove(i);
		permitCardsVisible.trimToSize();
		addPermitCardVisible();
		return p;
	}
	public int sizeNotVisiblePermitCard(){
		return permitCards.size();
	}
	public int sizeVisiblePermitCards(){
		return permitCardsVisible.size();
	}
	public boolean thereIsOtherPermitCard(){
		if(permitCardsVisible.size()>0)
			return true;
		return false;
	}
	public void morePermitCards(int m){
		//If the player surpass 4 I clone some permitCard to have enough card for everyone
		//I add 4 permit card for every player over 4 
		ArrayList <PermitCard> clonePermitCard = new ArrayList <PermitCard>();
		clonePermitCard.addAll(permitCards);
		for(int i=0;i<m*GameConstants.standardNumberOfPlayer;i++)
		{	
			Random x=new Random();
			int index=x.nextInt(clonePermitCard.size());
			permitCards.add(clonePermitCard.get(index));
			clonePermitCard.remove(index);
			if(clonePermitCard.size()==0)
				clonePermitCard.addAll(permitCards);
		}
	}
	public PermitCard[] getPermitCardsVisible(){
		PermitCard[] vettPermit=new PermitCard[GameConstants.cardsVisible];
		for(int i=0;i<vettPermit.length;i++)
			vettPermit[i]=permitCardsVisible.get(i);
		return vettPermit;
	}
	public void changePermitCardsVisible(){
		int size=permitCardsVisible.size();
		for(int i=0;i<size;i++)
		{
			permitCards.add(permitCardsVisible.get(0));
			permitCardsVisible.remove(0);
		}
		for(int i=0;i<size;i++)
			addPermitCardVisible();
	}
	
}
