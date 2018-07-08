package client.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class GUIOpponentStatus {

	private JLayeredPane panel=null;
	private String name="Player Name";
	private int money=3,helpers=5,nobility=7,score=9,positionX=0,positionY=0,QBPC=0,QPC=0,QE=0;
	private JLabel backgroundLabel=null,turnBackgroundLabel=null,labelName=null,labelMoneyName=null,labelMoney=null,labelHelpersName=null,labelHelpers=null,labelNobilityName=null,labelNobility=null,labelScoreName=null,labelScore=null,labelQBPCName=null,labelQBPC=null,labelQPCName=null,labelQPC=null,labelQEName=null,labelQE=null;
	
	final int distances=20,offset=10;
	
	public GUIOpponentStatus(JLayeredPane window){
		panel=window;
		
		labelName=new JLabel(name);
		labelName.setFont(new Font(null, Font.BOLD, 20));
		labelName.setForeground(Color.RED);
		
		//Money
		labelMoneyName=new JLabel("Money:");
		labelMoneyName.setFont(new Font(null, Font.BOLD, 14));
		labelMoneyName.setForeground(Color.BLACK);
		
		labelMoney=new JLabel(String.valueOf(money));
		labelMoney.setFont(new Font(null, Font.ITALIC, 14));
		labelMoney.setForeground(Color.BLACK);
		
		//Helpers
		labelHelpersName=new JLabel("Helpers:");
		labelHelpersName.setFont(new Font(null, Font.BOLD, 14));
		labelHelpersName.setForeground(Color.BLACK);
				
		labelHelpers=new JLabel(String.valueOf(money));
		labelHelpers.setFont(new Font(null, Font.ITALIC, 14));
		labelHelpers.setForeground(Color.BLACK);
		
		//Nobility
		labelNobilityName=new JLabel("Nobility:");
		labelNobilityName.setFont(new Font(null, Font.BOLD, 14));
		labelNobilityName.setForeground(Color.BLACK);
		
		labelNobility=new JLabel(String.valueOf(money));
		labelNobility.setFont(new Font(null, Font.ITALIC, 14));
		labelNobility.setForeground(Color.BLACK);
		
		//Score Points
		labelScoreName=new JLabel("Score:");
		labelScoreName.setFont(new Font(null, Font.BOLD, 14));
		labelScoreName.setForeground(Color.BLACK);
				
		labelScore=new JLabel(String.valueOf(money));
		labelScore.setFont(new Font(null, Font.ITALIC, 14));
		labelScore.setForeground(Color.BLACK);
		
		//QBPC
		labelQBPCName=new JLabel("Permit Cards:");
		labelQBPCName.setFont(new Font(null, Font.BOLD, 14));
		labelQBPCName.setForeground(Color.BLACK);
				
		labelQBPC=new JLabel(String.valueOf(QBPC));
		labelQBPC.setFont(new Font(null, Font.ITALIC, 14));
		labelQBPC.setForeground(Color.BLACK);
		
		//QPC
		labelQPCName=new JLabel("Politic Cards:");
		labelQPCName.setFont(new Font(null, Font.BOLD, 14));
		labelQPCName.setForeground(Color.BLACK);
				
		labelQPC=new JLabel(String.valueOf(QPC));
		labelQPC.setFont(new Font(null, Font.ITALIC, 14));
		labelQPC.setForeground(Color.BLACK);
		
		//QE
		labelQEName=new JLabel("Emporiums:");
		labelQEName.setFont(new Font(null, Font.BOLD, 14));
		labelQEName.setForeground(Color.BLACK);
				
		labelQE=new JLabel(String.valueOf(QE));
		labelQE.setFont(new Font(null, Font.ITALIC, 14));
		labelQE.setForeground(Color.BLACK);
		
		//Background
		ImageIcon image1 = new ImageIcon("img/opponentsBackground.png");
		backgroundLabel = new JLabel("", image1, JLabel.LEFT);
		backgroundLabel.setSize(image1.getIconWidth(),image1.getIconHeight());
				
		//Background
		ImageIcon image2 = new ImageIcon("img/opponentsBackgroundTurn.png");
		turnBackgroundLabel = new JLabel("", image2, JLabel.LEFT);
		turnBackgroundLabel.setSize(image2.getIconWidth(),image2.getIconHeight());
		turnBackgroundLabel.setVisible(false);
		
		
		setPosition(positionX,positionY);
	}

	public void setPosition(int x,int y){
		positionX=x;
		positionY=y-5;
		
		backgroundLabel.setLocation(positionX, positionY+5);
		turnBackgroundLabel.setLocation(positionX, positionY+5);
		labelName.setBounds(positionX+offset,positionY,150,40);
		
		labelMoneyName.setBounds(positionX+offset,positionY+distances,150,40);
		labelMoney.setBounds(positionX+offset+100,positionY+distances,150,40);
		
		labelHelpersName.setBounds(positionX+offset,positionY+distances*2,150,40);
		labelHelpers.setBounds(positionX+offset+100,positionY+distances*2,150,40);
		
		labelNobilityName.setBounds(positionX+offset,positionY+distances*3,150,40);
		labelNobility.setBounds(positionX+offset+100,positionY+distances*3,150,40);
		
		labelScoreName.setBounds(positionX+offset,positionY+distances*4,150,40);
		labelScore.setBounds(positionX+offset+100,positionY+distances*4,150,40);
		
		labelQBPCName.setBounds(positionX+offset,positionY+distances*5,150,40);
		labelQBPC.setBounds(positionX+offset+100,positionY+distances*5,150,40);
		
		labelQPCName.setBounds(positionX+offset,positionY+distances*6,150,40);
		labelQPC.setBounds(positionX+offset+100,positionY+distances*6,150,40);
		
		labelQEName.setBounds(positionX+offset,positionY+distances*7,150,40);
		labelQE.setBounds(positionX+offset+100,positionY+distances*7,150,40);
		
	}

	
	public String getName(){
		return name;
	}
	public void setName(String a){
		name=a;
		labelName.setText(name);
	}
	public void setMoney(int a){
		money=a;
		labelMoney.setText(String.valueOf(money));
	}
	public void setHelpers(int a){
		helpers=a;
		labelHelpers.setText(String.valueOf(helpers));
	}
	public void setNobility(int a){
		nobility=a;
		labelNobility.setText(String.valueOf(nobility+1));
	}
	public void setScore(int a){
		score=a;
		labelScore.setText(String.valueOf(score));
	}
	public void setQBPC(int a){
		QBPC=a;
		labelQBPC.setText(String.valueOf(QBPC));
	}
	public void setQPC(int a){
		QPC=a;
		labelQPC.setText(String.valueOf(QPC));
	}
	public void setQE(int a){
		QE=a;
		labelQE.setText(String.valueOf(QE));
	}
	public void setTurn(boolean isMyTurn){
		turnBackgroundLabel.setVisible(isMyTurn);
	}
	
	public void draw(){
		panel.add(labelName,20,6);
		panel.add(labelMoneyName,20,6);
		panel.add(labelMoney,20,6);
		panel.add(labelHelpersName,20,6);
		panel.add(labelHelpers,20,6);
		panel.add(labelNobilityName,20,6);
		panel.add(labelNobility,20,6);
		panel.add(labelScoreName,20,6);
		panel.add(labelScore,20,6);
		panel.add(labelQBPCName,20,6);
		panel.add(labelQBPC,20,6);
		panel.add(labelQPCName,20,6);
		panel.add(labelQPC,20,6);
		panel.add(labelQEName,20,6);
		panel.add(labelQE,20,6);
		panel.add(backgroundLabel,10,6);
		panel.add(turnBackgroundLabel,11,6);
	}
}
