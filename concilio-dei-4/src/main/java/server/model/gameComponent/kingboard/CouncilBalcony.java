package server.model.gameComponent.kingboard;

import java.io.Serializable;

import server.model.GameConstants;
import server.model.gameComponent.kingboard.Councillor;

public class CouncilBalcony implements Serializable{


	private Councillor[] councillors=new Councillor[GameConstants.maxNumberOfCouncillors];
	private int lastCouncillor;//this is the councillor that go out for first 
	private String nameBalcony;//identify where is the balcony (plain, hill, mountain or king)
	
	public CouncilBalcony(String t){
		nameBalcony=t;
	}
	public String getCouncilRegion(){
		return nameBalcony;
	}
	public Councillor getCouncillor(int i){
		return councillors[i];
	}
	public void setUpCouncilBalcony (Councillor c[]){
		lastCouncillor=0;
		for(int i=0;i<GameConstants.maxNumberOfCouncillors;i++)
			councillors[i]=c[i];
	}
	public Councillor pushInCouncillor(Councillor c){
		Councillor lastCouncillor=councillors[GameConstants.maxNumberOfCouncillors-1];
		for(int i=GameConstants.maxNumberOfCouncillors-1;i>0;i--){
			councillors[i]=councillors[i-1];
		}
		councillors[0]=c;
		return lastCouncillor;
	}
}
