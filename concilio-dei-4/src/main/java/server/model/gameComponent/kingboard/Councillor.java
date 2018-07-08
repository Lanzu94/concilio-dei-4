package server.model.gameComponent.kingboard;

import java.awt.Color;
import java.io.Serializable;

public class Councillor implements Serializable{

	private Color color;
	private String colorName;
	public void setColor(Color a){
		this.color=a;
	}
	public Color getColor(){
		return this.color;
	}
	public void setColorName(String s){
		colorName=s;
	}
	public String getColorName(){
		return colorName;
	}
	
}
