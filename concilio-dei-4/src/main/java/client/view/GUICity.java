package client.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

public class GUICity {

	private GUI gui=null;
	private GUIColorsArray emporiums=null;
	private JLayeredPane panel=null;
	private JButton labelCity=null;
	private JLabel labelName=null,labelReward=null,labelEmporiums=null;
	private int positionX=0,positionY=0,mode=0;;
	private String name=null,rewards=null,action=null;
	private Boolean enabled,kingIsHere=false;
	
	public GUICity(JLayeredPane citiesPanel,GUI gui,String action){
		panel=citiesPanel;
		this.action=action;
		this.gui=gui;
		
		emporiums=new GUIColorsArray(panel,gui);
		emporiums.setPosition(positionX, positionY);
		
		ImageIcon image = new ImageIcon("img/city.jpg");
		labelCity = new JButton(image);
		labelCity.setSize(image.getIconWidth(),image.getIconHeight());
		labelCity.setLocation(positionX, positionY);
		labelCity.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
				if(action!=null) {
					if(mode==0){
						if(enabled)gui.optionInsert(action);
					}
					if(mode==1){
						if(kingIsHere){
							setEnabled(false);
							labelCity.setBorder(new LineBorder(Color.BLUE,3));
							}
						if(enabled){
							gui.addToActionStringCity(action);
							setEnabled(false);
							labelCity.setBorder(new LineBorder(Color.BLUE,3));
							}
						}
					if(mode==2){
						if(enabled){
							gui.addToActionStringBonusCity(action);
							setEnabled(false);
							labelCity.setBorder(new LineBorder(Color.BLUE,3));
						}
					}
				}
		  }});
		
		
		labelName=new JLabel("City Name");
		labelName.setFont(new Font(null, Font.BOLD, 14));
		labelName.setForeground(Color.BLUE);

		labelReward=new JLabel("No Rewards");
		labelReward.setFont(new Font(null, Font.BOLD, 14));
		labelReward.setForeground(Color.BLUE);
		
		labelEmporiums=new JLabel("Emporiums");
		labelEmporiums.setFont(new Font(null, Font.PLAIN, 10));
		labelEmporiums.setForeground(Color.BLACK);
		
		setEnabled(false);
	}
	
	public void setPosition(int x,int y){
		positionX=x;
		positionY=y;
		labelEmporiums.setBounds(positionX+3, positionY+44, 100, 20);
		emporiums.setPosition(positionX, positionY+60);
		labelCity.setLocation(positionX, positionY);
		labelName.setBounds(positionX+7,positionY+2,100,20);
		labelReward.setBounds(positionX+7,positionY+25,100,20);
		
	}
	public void setName(String n){
		name=n;
		labelName.setText(name);
	}
	public void setColor(Color color){
		labelName.setForeground(color);
	}
	public void setKingIsHere(boolean b){
		kingIsHere=b;
		if (kingIsHere) labelName.setText(name+"   ♔");
		else  labelName.setText(name);
	}
	public void setRewards(String a){
		rewards=a;
		labelReward.setText(rewards);
	}
	public void addRewards(String a){
		rewards=rewards+a;
		labelReward.setText(rewards);
	}
	public void addEmporium(String playerName){
		emporiums.addColor(playerName);
	}
	public String getName(){
		return name;
	}
	
	//utility
	public void clearEmporiums(){
		emporiums.clear();
	}
	public void draw(){
		panel.add(labelReward,7,1);
		panel.add(labelName,7,1);
		panel.add(labelEmporiums,7,1);

		panel.add(labelCity,6,1);
	}
	public void setEnabled(Boolean a){
		enabled=a;
		if(enabled){
			labelCity.setBorder(new LineBorder(Color.RED,3));
		}else labelCity.setBorder(null);
	}
	public void setMode(int a){
		mode=a;
	}
	public void setMode(int a,String b){
		setMode(a);
		if(mode==1){
			if(labelName.getText().equals(b))labelCity.setBorder(new LineBorder(Color.GREEN,3));
			if(kingIsHere){
				gui.addToActionStringCity(action);
				labelCity.setBorder(new LineBorder(Color.GREEN,3));
			}
		}
	}
	
	//roads
	public void addRoadRight(){
		ImageIcon image = new ImageIcon("img/roads/right.jpg");
		JLabel labelRoad = new JLabel("", image, JLabel.LEFT);
		labelRoad.setSize(image.getIconWidth(),image.getIconHeight());
		labelRoad.setLocation(positionX+labelCity.getHeight(),positionY+labelCity.getWidth()/2);
		panel.add(labelRoad,4,1);
	}
	public void addRoadRightLong(){
		ImageIcon image = new ImageIcon("img/roads/rightLong.jpg");
		JLabel labelRoad = new JLabel("", image, JLabel.LEFT);
		labelRoad.setSize(image.getIconWidth(),image.getIconHeight());
		labelRoad.setLocation(positionX+labelCity.getHeight(),positionY+labelCity.getWidth()/2);
		panel.add(labelRoad,4,1);
	}
	public void addRoadDown(){
		ImageIcon image = new ImageIcon("img/roads/down.jpg");
		JLabel labelRoad = new JLabel("", image, JLabel.LEFT);
		labelRoad.setSize(image.getIconWidth(),image.getIconHeight());
		labelRoad.setLocation(positionX+labelCity.getWidth()/2,positionY+labelCity.getHeight());
		panel.add(labelRoad,4,1);
	}
	public void addRoadDownRight(){
		ImageIcon image = new ImageIcon("img/roads/downRight.gif");
		JLabel labelRoad = new JLabel("", image, JLabel.LEFT);
		labelRoad.setSize(image.getIconWidth(),image.getIconHeight());
		labelRoad.setLocation(positionX+labelCity.getWidth()-10,positionY+labelCity.getHeight()-10);
		panel.add(labelRoad,4,1);
	}
	public void addRoadDownLeft(){
		ImageIcon image = new ImageIcon("img/roads/downLeft.gif");
		JLabel labelRoad = new JLabel("", image, JLabel.LEFT);
		labelRoad.setSize(image.getIconWidth(),image.getIconHeight());
		labelRoad.setLocation(positionX-image.getIconWidth()+10,positionY+labelCity.getHeight()-10);
		panel.add(labelRoad,4,1);
	}
	public void addRoadIntersection(){
		ImageIcon image = new ImageIcon("img/roads/intersection.gif");
		JLabel labelRoad = new JLabel("", image, JLabel.LEFT);
		labelRoad.setSize(image.getIconWidth(),image.getIconHeight());
		labelRoad.setLocation(positionX+labelCity.getWidth()-30,positionY+labelCity.getHeight()-10);
		panel.add(labelRoad,5,1);
	}
}
