package client.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class GUIStatusElement {

	private JLayeredPane panel=null;
	private JLabel labelName=null,labelValue=null;
	private int value=0,positionX=0,positionY=0,rewardPosition=0;
	private String name="Reward Name: ";
	
	public GUIStatusElement(JLayeredPane window){
		panel=window;

		labelName=new JLabel(name);
		labelName.setFont(new Font(null, Font.BOLD, 14));
		labelName.setForeground(Color.BLACK);

		labelValue=new JLabel(String.valueOf(value));
		labelValue.setFont(new Font(null, Font.ITALIC, 14));
		labelValue.setForeground(Color.RED);
	}
	public void setPosition(int x){
		positionX=x;
		labelName.setBounds(positionX,positionY,150,40);
		labelValue.setBounds(positionX+rewardPosition,positionY,100,40);
	}
	public void setPosition(int x,int y){
		positionX=x;
		positionY=y;
		labelName.setBounds(positionX,positionY,150,40);
		labelValue.setBounds(positionX+rewardPosition,positionY,100,40);
	}
	public void setValuePosition(int x){
		rewardPosition=x;
		setPosition(positionX);
	}
	public void setValue(int a){
		value=a;
		labelValue.setText(String.valueOf(value));
		setPosition(positionX);
	}
	public int getValue(){
		return value;
	}
	public void setName(String a){
		name=a;
		labelName.setText(name);
		setPosition(positionX);
	}
	public void draw(){
		panel.add(labelName);
		panel.add(labelValue);
	}
}
