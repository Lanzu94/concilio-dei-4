package server.model.gameComponent.cards;

import java.awt.Color;
import java.io.Serializable;

public class PoliticalCard implements Serializable{
	/*
	 * this class rappresent the political card's
	 */
	private Color color;
	private boolean isJolly;
	private String colorName;
	public PoliticalCard(){//if I don't give color when I create it is jolly
		colorName="JOLLY";
		color=null;
		isJolly=true;
	}
	public PoliticalCard(Color c){
		color=c;
	}
	public Color getColor(){
		return color;
	}
	public boolean isJolly(){
		return isJolly;
	}
	public void setColorName(String s){
		colorName=s;
	}
	public String getColorName(){
		return colorName;
	}
}
