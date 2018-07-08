package client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GUIMarketElements {

	private GUI gui;
	private ArrayList<JButton> elements=new ArrayList<JButton>();
	private int positionX=0,positionY=0,typeC,priceC;
	private String C,whoC;
	private JLayeredPane elementsPanel=null;
	
	public GUIMarketElements(JPanel panel2,GUI gui){
		this.gui=gui;
		elementsPanel=new JLayeredPane();
		elementsPanel.setBounds(0,0,1500,500);
		elementsPanel.setOpaque(false);
		panel2.add(elementsPanel);
	}
	public void addElement(int type,String who,String amountOrNameOrColor,int price,String action){
		typeC=type;
		C=amountOrNameOrColor;
		priceC=price;
		whoC=who;
		

		JButton button=new JButton();
		button.setContentAreaFilled(false);
		elements.add(button);
		elementsPanel.add(button);
		
		String text=null;
		if(typeC==0){
			text="<html>From "+whoC+":<br/>Permit card: <b>"+C+"</b><br/><i>Price: "+priceC+" coins</i></html>";
			button.setText(text);
		}
		if(typeC==1){
			text="<html>From "+whoC+":<br/>Politic card: <br/><i>Price: "+priceC+" coins</i></html>";
			String color;
			if(C.equals("RED")) color="red";
			else if(C.equals("GREEN")) color="green";
			else if(C.equals("BLACK")) color="black";
			else if(C.equals("ORANGE")) color="orange";
			else if(C.equals("WHITE")) color="white";
			else if(C.equals("BLUE")) color="blue";
			else color="jolly";
			
			ImageIcon image=new ImageIcon("img/colors/"+color+".jpg");
			button.setText(text);
			button.setIcon(image);
		    button.setVerticalTextPosition(SwingConstants.CENTER);
		    button.setHorizontalTextPosition(SwingConstants.LEFT);
		}
		if(type==2){
			text="<html>From: "+whoC+"<br/><b>"+C+" x Helpers</b><br/><i>Price: "+priceC+" coins</i></html>";
			button.setText(text);
		}
		
		button.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			gui.optionInsert(action);
		  }});
		
		
		setPosition(positionX,positionY);
	}
	public void setPosition(int x,int y){
		positionX=x;
		positionY=y;
		for(int i=0;i<elements.size();i++)elements.get(i).setBounds(positionX+160*i,positionY,150,50);
	}
	public void clear(){
		
		elements.clear();
		elements.trimToSize();
		
		elementsPanel.removeAll();
		
		setPosition(positionX,positionY);
	}
	public void setVisible(Boolean a){
		for(int i=0;i<elements.size();i++)elements.get(i).setVisible(a);
	}
	
}
