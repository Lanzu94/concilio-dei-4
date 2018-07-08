package client.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class GUIBonus{

	private JPanel panel=null;
	private Boolean enabled=false;
	private JButton bonusButton=null;
	private String cities=null,reward=null,actionz=null;
	private int positionX=0,positionY=0;
	private GUI gui=null;
	private int mod=0;
	private int sizeX=100,sizeY=34;
	
	
	
	public GUIBonus(JPanel window,GUI g,String action,int mode){
		panel=window;
		gui=g;
		mod=mode;
		
		bonusButton=new JButton();
		bonusButton.setBorder(null);
		bonusButton.setContentAreaFilled(false);
		
		actionz=action;
		
		bonusButton.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
				if(action!=null&&enabled){
					if(mod==0){
						gui.setChosenPermit(cities);
						gui.optionInsert(actionz);
					}
					if(mod==1){
						gui.addToActionStringMarket(action);
						gui.marketOffer();
					}
				}
				
		  }});
		
		setPosition(positionX,positionY);
		print();
		
		setEnabled(false);
	}
	public void setAction(String a){
		actionz=a;
	}
	public void setMode(int a){
		mod=a;
	}
	public void setPosition(int x){
		positionX=x;
		bonusButton.setBounds(positionX,positionY,sizeX,sizeY);
	}
	public void setPosition(int x,int y){
		positionX=x;
		positionY=y;
		bonusButton.setBounds(positionX,positionY,sizeX,sizeY);
	}
	
	public void setCities(String n,int mode){
		if(mode==0)cities=n;
		else if(mode==1){
			for(int i=0;i<n.length();i++)
				if(i==0)cities=String.valueOf(n.charAt(0));
				else cities=cities+"-"+n.charAt(i);
		}
		print();
	}
	public void setRewards(String a){
		reward="<span size='12' color='black'><i>"+a+"</i></span>";
		print();
	}
	public void addRewards(String a){
		reward=reward+"<span size='12' color='black'><i>"+a+"</i></span>";
		print();
	}
	
	public void clear(){
		cities="";
		reward="";
	}
	private void print(){
		bonusButton.setText("<html><span size='14' color='black'><b>"+cities+"</b></span><br/>"+reward+"</html>");
	}
	public void draw(){
		panel.add(bonusButton);
	}
	
	public void setEnabled(Boolean a){
		enabled=a;
		bonusButton.setBorder(null);
		bonusButton.setEnabled(true);
	}
	public void setTrueEnabled(Boolean a){
		setEnabled(a);
		if(a){
			bonusButton.setBorder(new LineBorder(Color.GREEN,3,true));
		}
		bonusButton.setEnabled(a);
	}
	public void setTiny(boolean a){
		if(a){
			sizeX=35;
			sizeY=30;
		}
		else{
			sizeX=100;
			sizeY=34;
		}
		setPosition(positionX);
	}
}