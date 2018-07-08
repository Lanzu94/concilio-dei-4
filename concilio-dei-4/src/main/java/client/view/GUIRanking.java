package client.view;

import javax.swing.JLabel;

public class GUIRanking {

	private JLabel label=null;
	private String text=null;
	private int numberOfScores=0;
	
	public GUIRanking(JLabel rankingLabel){
		label=rankingLabel;
	}
	
	public void addElement(String a){
		if(text!=null)text=text+"<br/>"+"<h2>"+a+"</h2>";
		else text="<h2>"+a+"</h2>";
	numberOfScores++;
	label.setText("<html><h1 Color='RED'>RANKING</h1>"+text+"</html>");
	}
	
	public void addElement(String a,boolean isOrdered){
		if(isOrdered){
			if(text!=null)
				if(numberOfScores<4)text=text+"<br/>"+"<h"+(numberOfScores+2)+">"+a+"</h"+(numberOfScores+2)+">";
				else text=text+"<br/>"+"<h5>"+a+"</h5>";
			else text="<h2>"+a+"</h2>";
		numberOfScores++;
		label.setText("<html><h1 Color='RED'>RANKING</h1>"+text+"</html>");
		}
		else addElement(a);
	}
	
}
