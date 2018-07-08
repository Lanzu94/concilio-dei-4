package client.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

public class GUIColorsArray {

	private GUI gui=null;
	private boolean enabled=false;
	private JLayeredPane colorsPanel=null;
	private int positionX=0,positionY=0,iconSize;
	private ArrayList<JButton> colors=new ArrayList<JButton>();
	private ArrayList<String> colorNames=new ArrayList<String>();
	private int mod=0,maxPerRow=5;
	
	public GUIColorsArray(JLayeredPane panel2,GUI g){
		gui=g;
		colorsPanel=new JLayeredPane();
		colorsPanel.setBounds(0,0,1000,1000);
		colorsPanel.setOpaque(false);
		panel2.add(colorsPanel,7,1);		
	}
	public void setMaxColorsPerRow(int a){
		maxPerRow=a;
		
		draw();
	}
	public void addColor(String playerName) {
		colorNames.add(0,playerName);
		
		ImageIcon image=new ImageIcon("img/colors/white.jpg");
		iconSize=image.getIconWidth()-1;
		
		JButton label=new JButton(String.valueOf(playerName.toUpperCase().charAt(0)));
		label.setToolTipText(playerName);
		label.setSize(image.getIconWidth(),image.getIconHeight());
		label.setFocusable(false);
		
		label.setBorder(new LineBorder(Color.LIGHT_GRAY,1));
		label.setContentAreaFilled(false);
		
		label.setEnabled(true);
		
		colors.add(0,label);
		colorsPanel.add(label);
		
		draw();
	}
	public void addColor(String color,String action){
		addColor(color,action,0);
	}
	public void addColor(String color,String action,int mode){
		colorNames.add(0,color);
		
		mod=mode;
		String whatColor=null;
		
		if(color.equals("RED")) whatColor="red";
		else if(color.equals("GREEN")) whatColor="green";
		else if(color.equals("BLACK")) whatColor="black";
		else if(color.equals("ORANGE")) whatColor="orange";
		else if(color.equals("WHITE")) whatColor="white";
		else if(color.equals("BLUE")) whatColor="blue";
		else whatColor="jolly";
		
		ImageIcon image=new ImageIcon("img/colors/"+whatColor+".jpg");
		iconSize=image.getIconWidth();
		JButton label = new JButton(image);
		label.setDisabledIcon(new ImageIcon("img/colors/"+whatColor+"Disabled.jpg"));
		label.setSize(image.getIconWidth(),image.getIconHeight());
		label.setEnabled(enabled);
		
		label.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  if(mod==0&&enabled)gui.optionInsert(action);
			  if(mod==1&&enabled){
				  gui.addToActionStringPC(action);
				  label.setEnabled(false);
			  }
			  if(mod==2&&enabled){
				  gui.addToActionStringMarket(action);
				  label.setEnabled(false);
				  gui.marketOffer();
			  }
		  }});
		
		colors.add(0,label);
		colorsPanel.add(label);
		
		draw();
	}
	public void setMode(int mode){
		mod=mode;
	}
	public void setPosition(int x,int y){
		positionX=x;
		positionY=y;
		
		for(int i=0;i<colors.size();i++) colors.get(i).setLocation(2+positionX+iconSize*(i%maxPerRow), positionY+iconSize*(i/maxPerRow));
	}
	public String pushInColor(String color){
		addColor(color,"");
		
		String temp=colorNames.get(colorNames.size()-1);
		colorNames.remove(colorNames.size()-1);
		colors.remove(colors.size()-1);
		
		draw();
		
		return temp;
	}
	public void clear(){
		
		colors.clear();
		colors.trimToSize();
		colorNames.clear();
		colorNames.trimToSize();
		
		colorsPanel.removeAll();
		
		
		draw();
	}
	public void draw(){
		setPosition(positionX,positionY);
	}
	public void setEnabled(Boolean a){
		enabled=a;
		for(int i=0;i<colors.size();i++)colors.get(i).setEnabled(enabled);
	}
	public int getNumberOfElements(){
		return colors.size();
	}

}
