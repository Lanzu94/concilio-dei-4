package client.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUINobilityRoadElement {

	private JPanel panel=null;
	private JLabel labelReward=null,playersLabel=null;;
	private int positionX=0,positionY=0,numberOfElements;
	private String players="",rewards="";
	
	
	public GUINobilityRoadElement(JPanel window){
		panel=window;
		
		playersLabel=new JLabel(players);
		playersLabel.setFont(new Font(null, Font.BOLD, 14));
		playersLabel.setForeground(Color.BLACK);
		
		labelReward=new JLabel(rewards);
		labelReward.setFont(new Font(null, Font.ITALIC, 14));
		labelReward.setForeground(Color.BLACK);
		
		numberOfElements=0;
	}
	public void setPosition(int x){
		positionX=x;
		labelReward.setBounds(positionX,positionY,150,40);
		playersLabel.setBounds(positionX,positionY-28,150,40);
	}
	public void setPosition(int x,int y){
		positionX=x;
		positionY=y;
		labelReward.setBounds(positionX,positionY,150,40);
		playersLabel.setBounds(positionX,positionY-28,150,40);
	}
	public void setReward(String a){
		rewards=a;
		labelReward.setText("<html>"+rewards+"</html>");
	}
	public void addPlayer(String a){
		if(players=="") players=a;
		else players=players+" "+a;
		playersLabel.setText(players);
	}
	public void addReward(String a){
		if(numberOfElements==0)setReward(a);
		else{
			rewards=rewards+"<br/>"+a;
			labelReward.setText("<html>"+rewards+"</html>");
		}
		numberOfElements++;
	}
	public void clear(){
		rewards="";
		players="";
		numberOfElements=0;
	}
	public void draw(){
		panel.add(labelReward);
		panel.add(playersLabel);
	}
}
