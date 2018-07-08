package client.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.model.AbbreviationRewardType;
import client.model.ClientConstants;
import client.model.EnumPage;
import client.model.EnumStatusGame;
import client.model.LocalStateGame;
import client.model.Page;
import server.StartServer;

public class GUI extends Observable implements Observer{
	
	private JFrame window;
	private final int WINDOW_WIDTH=1280,WINDOW_HEIGHT=720;
	private String[][][]mapz=null;
	private Boolean notChosenYet=true,showError=false;
	private String actionString=null,chosenPermit=null,option=null;
	private int actionStringCount=0;
	
	//Init
	public GUI()
	{		
		window=new JFrame();
		window.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		window.setVisible(true);
		
		initGUIMenu();
	}

	private void initGUIMenu(){
		GUICreateWelcomeToTheCouncil();
		GUICreateChangeConnectionMode();
		GUICreateSelectAName();
		GUICreateMenuOnline();
		GUICreateSelectNumberOfPlayers();
		GUICreateLobby();
		GUIListOfGames();
		GUICreateGameIsStarting();
	}
	private void destroyGUI(){
		hideEverything();
		window.getContentPane().removeAll();
	}
	private void initGUIGame(){
		destroyGUI();
		GUICreateLayout();
		GUICreateNotYourTurn();
		GUICreateActions();
		GUICreateMarket();
		GUICreateEndOfTheGame();
		
		showLayout();
	}
	private void hideEverything(){
		Container contentPane=window.getContentPane();
		for(int i=0;i<contentPane.getComponentCount();i++)contentPane.getComponent(i).setVisible(false);
	}
	private void hideEverythingInGame(){
		Container contentPane=window.getContentPane();
		for(int i=1;i<contentPane.getComponentCount();i++)contentPane.getComponent(i).setVisible(false);
	}
	
//GUIMenu
	//Welcome to the council
	private JPanel welcomePanel=new JPanel(null);
	private void GUICreateWelcomeToTheCouncil(){
		//0
		welcomePanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		window.getContentPane().add(welcomePanel);
		
		JLabel label0=new JLabel("Welcome to");
		label0.setBounds(WINDOW_WIDTH/2-10,WINDOW_HEIGHT/2-200,400,100);
		welcomePanel.add(label0);
		
		JLabel label1=new JLabel("The Council of Four");
		label1.setForeground(Color.red);
		label1.setFont(new Font("Serif", Font.BOLD, 50));
		label1.setBounds(WINDOW_WIDTH/2-190,WINDOW_HEIGHT/2-150,1000,100);
		welcomePanel.add(label1);
		
		JButton button1=new JButton("Play Online");
		button1.setBounds(WINDOW_WIDTH/2-350,WINDOW_HEIGHT/2,250,50);
		welcomePanel.add(button1);
		button1.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("1");
		  }});
		
		JButton button2=new JButton("Change connection mode");
		button2.setBounds(WINDOW_WIDTH/2+100,WINDOW_HEIGHT/2,250,50);
		welcomePanel.add(button2);
		button2.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("2");
		  }});
		
		
	}
	private void showWelcomeToTheCouncil(){
		hideEverything();
		window.getContentPane().getComponent(0).setVisible(true);
	}
	
	//Change connection mode
	private JPanel connectionPanel=new JPanel(null);
	private void GUICreateChangeConnectionMode(){
		//1
		connectionPanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		window.getContentPane().add(connectionPanel);
		
		JLabel label1=new JLabel("Change connection mode");
		label1.setBounds(WINDOW_WIDTH/2-90,WINDOW_HEIGHT/2-150,400,100);
		connectionPanel.add(label1);
		
		JButton button1=new JButton("RMI");
		button1.setBounds(WINDOW_WIDTH/2-350,WINDOW_HEIGHT/2,250,50);
		connectionPanel.add(button1);
		button1.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("1");
			  label1.setText("You selected RMI");
		  }});
		
		JButton button2=new JButton("Socket");
		button2.setBounds(WINDOW_WIDTH/2+100,WINDOW_HEIGHT/2,250,50);
		connectionPanel.add(button2);
		button2.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("2");
			  label1.setText("You selected Socket");
		  }});
		
		JButton button3=new JButton("Back");
		button3.setBounds(WINDOW_WIDTH/2-50,WINDOW_HEIGHT/2+125,100,50);
		connectionPanel.add(button3);
		button3.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("3");
			  label1.setText("Change connection mode");
		  }});
		
	}
	private void showChangeConnectionMode(){
		hideEverything();
		window.getContentPane().getComponent(1).setVisible(true);
	}

	//Select a name
	private JPanel namePanel=new JPanel(null);
	private void GUICreateSelectAName(){
		//2
		namePanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		window.getContentPane().add(namePanel);
		
		JLabel label4=new JLabel("Name not avaible, try again.");
		label4.setForeground(Color.red);
		label4.setBounds(WINDOW_WIDTH/2-75,WINDOW_HEIGHT/2+150,400,100);
		label4.setVisible(false);
		namePanel.add(label4);
		
		JLabel label1=new JLabel("Your Nickname");
		label1.setBounds(WINDOW_WIDTH/2-50,WINDOW_HEIGHT/2-190,400,100);
		namePanel.add(label1);
		
		final JTextField text1=new JTextField("Alessio");
		text1.setBounds(WINDOW_WIDTH/2-120,WINDOW_HEIGHT/2-125,250,50);
		namePanel.add(text1);
		
		JLabel label2=new JLabel("Your Password");
		label2.setBounds(WINDOW_WIDTH/2-50,WINDOW_HEIGHT/2-80,400,100);
		namePanel.add(label2);
		
		JLabel label3=new JLabel("(only to avoid other players joining as you)");
		label3.setBounds(WINDOW_WIDTH/2-110,WINDOW_HEIGHT/2-67,400,100);
		namePanel.add(label3);
		
		final JPasswordField text2=new JPasswordField("Alessio");
		text2.setBounds(WINDOW_WIDTH/2-120,WINDOW_HEIGHT/2,250,50);
		namePanel.add(text2);
		
		JButton button3=new JButton("Submit");
		button3.setBounds(WINDOW_WIDTH/2-50,WINDOW_HEIGHT/2+125,100,50);
		namePanel.add(button3);
		button3.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert(text1.getText()+" "+text2.getText());
		  }});
		
		
		
	}
	private void showSelectAName(){
		hideEverything();
		window.getContentPane().getComponent(2).setVisible(true);
	}
	private void showSelectANameError(){
		showSelectAName();
		namePanel.getComponent(0).setVisible(true);
	}

	//Menu ONLINE
	private JPanel onlineMenuPanel=new JPanel(null);
	private JButton joinAGameButton=null;
	private void GUICreateMenuOnline(){
		//3
		onlineMenuPanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		window.getContentPane().add(onlineMenuPanel);

		JLabel label1=new JLabel("");
		label1.setBounds(WINDOW_WIDTH-200,WINDOW_HEIGHT-100,400,100);
		onlineMenuPanel.add(label1);
		
		JButton button1=new JButton("New Game");
		button1.setBounds(WINDOW_WIDTH/2-350,WINDOW_HEIGHT/2-50,250,50);
		onlineMenuPanel.add(button1);
		button1.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("3");
			  try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				StartServer.logger.log(Level.SEVERE, "Problem in the GUI", e);
				Thread.currentThread().interrupt();
			}
			  optionInsert("1");
			  optionInsert("1");
		  }});
		
		joinAGameButton=new JButton("Join a Game");
		joinAGameButton.setBounds(WINDOW_WIDTH/2+100,WINDOW_HEIGHT/2-50,250,50);
		onlineMenuPanel.add(joinAGameButton);
		joinAGameButton.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("2");
		  }});
		
	}
	private void showMenuOnline(){
		hideEverything();
		window.getContentPane().getComponent(3).setVisible(true);
	}
	private void updatePlayersCount(int numberOfPlayers){
		if(numberOfPlayers==1){
			joinAGameButton.setEnabled(false);
			((JLabel) onlineMenuPanel.getComponent(0)).setText("You are the only player online");
		}
		else {
			joinAGameButton.setEnabled(true);
			((JLabel) onlineMenuPanel.getComponent(0)).setText("There are "+(numberOfPlayers)+" players online");
		}
	}

	//Number of players
	private JPanel numberOfPlayersPanel=new JPanel(null);
	private ButtonGroup buttonsMaps=new ButtonGroup();
	private JPanel radioButtonsMaps=new JPanel(new GridLayout(0, 1));
	private void GUICreateSelectNumberOfPlayers(){
		//4
		numberOfPlayersPanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		window.getContentPane().add(numberOfPlayersPanel);
		
		JLabel label4=new JLabel("Error, try again.");
		label4.setVisible(false);
		label4.setForeground(Color.red);
		label4.setBounds(WINDOW_WIDTH/2-75,WINDOW_HEIGHT/2+150,400,100);
		numberOfPlayersPanel.add(label4);
		
		JLabel label1=new JLabel("How many players do you want in your game?");
		label1.setBounds(WINDOW_WIDTH/2-330,WINDOW_HEIGHT/2-150,400,100);
		numberOfPlayersPanel.add(label1);
		
		JLabel label2=new JLabel("Minimum:");
		label2.setBounds(WINDOW_WIDTH/2-290,WINDOW_HEIGHT/2-100,400,100);
		numberOfPlayersPanel.add(label2);
		
		final JTextField text1=new JTextField("2");
		text1.setBounds(WINDOW_WIDTH/2-225,WINDOW_HEIGHT/2-75,70,50);
		numberOfPlayersPanel.add(text1);
		
		JLabel label3=new JLabel("Maximum:");
		label3.setBounds(WINDOW_WIDTH/2-290,WINDOW_HEIGHT/2-25+50-75,400,100);
		numberOfPlayersPanel.add(label3);
		
		final JTextField text2=new JTextField("2");
		text2.setBounds(WINDOW_WIDTH/2-225,WINDOW_HEIGHT/2+50-75,70,50);
		numberOfPlayersPanel.add(text2);
		
		JLabel label5=new JLabel("Choose a map:");
		label5.setBounds(WINDOW_WIDTH/2+200,WINDOW_HEIGHT/2-150,400,100);
		numberOfPlayersPanel.add(label5);
		
		JButton button3=new JButton("Submit");
		button3.setBounds(WINDOW_WIDTH/2-50,WINDOW_HEIGHT/2+125,100,50);
		numberOfPlayersPanel.add(button3);
		button3.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  for(int i=0;i<radioButtonsMaps.getComponentCount();i++) 
				  if(((JRadioButton)radioButtonsMaps.getComponent(i)).isSelected())
					  optionInsert(text1.getText()+"-"+text2.getText()+"-"+i);
		  }});

		
		JButton button4=new JButton("Maps details");
		button4.setBounds(WINDOW_WIDTH/2+350,WINDOW_HEIGHT/2-130,140,40);
		numberOfPlayersPanel.add(button4);
		button4.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {	
			    for (int i = 0; i < mapz.length; i++) {
						JFrame windows=new JFrame();
						windows.setSize(906,454);
						windows.setResizable(false);
						windows.setLocation(100+i*100, 100+i*100);
						windows.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						windows.setLayout(null);
						windows.setVisible(true);
						windows.setTitle("MAP "+i);
						
						ArrayList<GUICity> cities=new ArrayList<GUICity>();
						
						ImageIcon backgroundImage = new ImageIcon("img/mapBackground.png");
						JLabel imageLabel = new JLabel("", backgroundImage, JLabel.LEFT);
						imageLabel.setSize(backgroundImage.getIconWidth(),backgroundImage.getIconHeight());
						imageLabel.setLocation(0, 0);
						
						JLayeredPane citiesPane=new JLayeredPane();
						
						citiesPane.setOpaque(false);
						citiesPane.setBounds(0, 0,backgroundImage.getIconWidth(),backgroundImage.getIconHeight());
						for(int ii=0;ii<15;ii++){
							cities.add(new GUICity(citiesPane,null,Character.toString((char) ('A'+ii))));
							cities.get(ii).draw();
						}
						
						cities.get(0).setPosition(LEFT_BORDER+0*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
						cities.get(2).setPosition(LEFT_BORDER+1*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
						cities.get(1).setPosition(LEFT_BORDER+0*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
						cities.get(3).setPosition(LEFT_BORDER+1*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
						cities.get(4).setPosition(LEFT_BORDER+1*COLUMN_DISTANCE, TOP_BORDER+2*ROW_DISTANCE);
						
						cities.get(5).setPosition(LEFT_BORDER+2*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
						cities.get(8).setPosition(LEFT_BORDER+3*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
						cities.get(6).setPosition(LEFT_BORDER+2*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
						cities.get(9).setPosition(LEFT_BORDER+3*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
						cities.get(7).setPosition(LEFT_BORDER+2*COLUMN_DISTANCE, TOP_BORDER+2*ROW_DISTANCE);
						
						cities.get(10).setPosition(LEFT_BORDER+4*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
						cities.get(13).setPosition(LEFT_BORDER+5*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
						cities.get(11).setPosition(LEFT_BORDER+4*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
						cities.get(14).setPosition(LEFT_BORDER+5*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
						cities.get(12).setPosition(LEFT_BORDER+4*COLUMN_DISTANCE, TOP_BORDER+2*ROW_DISTANCE);
						
							for(int iii=0;iii<mapz[i].length;iii++)
							{
								cities.get(iii).setName(String.valueOf((char)('A'+iii)));
								
								if("blue".equals(mapz[i][iii][1])) cities.get(iii).setColor(Color.BLUE);
								else if("orange".equals(mapz[i][iii][1])) cities.get(iii).setColor(Color.ORANGE);
								else if("yellow".equals(mapz[i][iii][1])) cities.get(iii).setColor(Color.YELLOW);
								else if("purple".equals(mapz[i][iii][1])) cities.get(iii).setColor(Color.PINK);
								else if("gray".equals(mapz[i][iii][1])) cities.get(iii).setColor(Color.GRAY);
								
								cities.get(iii).setRewards("");
							}
						
						//Roads
						String [][]map=mapz[i];

						
						for(int index=0;index<15;index++){

							String roads=map[index][0];
							boolean[] doIntersection=new boolean[15];
							int[] cutRoads=new int[roads.length()];
							for(int k=0;k<roads.length();k++){
								cutRoads[k]=Character.getNumericValue(roads.charAt(k))-Character.getNumericValue('A');
							}
							
						if(index<5){
							for(int k=0;k<cutRoads.length;k++){
								if((cutRoads[k]-index)==3){
									if(index==0||index==1){
										cities.get(index).addRoadDownRight();
										doIntersection[index]=true;
									}
									else if(index==2||index==3||index==4)cities.get(index).addRoadRight();
								}
								if((cutRoads[k]-index)==4&&(index==2||index==3)){
									cities.get(index).addRoadDownRight();
									doIntersection[index]=true;
								}
								if((cutRoads[k]-index)==-1&&index==2){
									if(doIntersection[0]) cities.get(0).addRoadIntersection();
									else cities.get(index).addRoadDownLeft();
								}
								if((cutRoads[k]-index)==1&&(index==0||index==2||index==3))cities.get(index).addRoadDown();
								if((cutRoads[k]-index)==2&&(index==0||index==1))cities.get(index).addRoadRight();
								
							}
						}
						else if(index<10){
							for(int k=0;k<cutRoads.length;k++){
								if((cutRoads[k]-index)==3){
									if(index==5||index==6)cities.get(index).addRoadRight();
									else if(index==8||index==9){
										cities.get(index).addRoadDownRight();
										doIntersection[index]=true;
									}
								}
								if((cutRoads[k]-index)==4&&(index==5)){
									cities.get(index).addRoadDownRight();
									doIntersection[index]=true;
								}
								if((cutRoads[k]-index)==-2&&(index==5||index==6||index==8||index==9)){
									if(index==5&&doIntersection[2])cities.get(index).addRoadIntersection();
									else if(index==6&&doIntersection[3])cities.get(index).addRoadIntersection();
									else if(index==8&&doIntersection[5])cities.get(index).addRoadIntersection();
									else if(index==9&&doIntersection[6])cities.get(index).addRoadIntersection();
									else cities.get(index).addRoadDownLeft();
								}
								if((cutRoads[k]-index)==1&&(index==5||index==6||index==8))cities.get(index).addRoadDown();
								if((cutRoads[k]-index)==2&&(index==8||index==9))cities.get(index).addRoadRight();
								
								if((cutRoads[k]-index)==5&&(index==7))cities.get(index).addRoadRightLong();
							}
						}else{
							for(int k=0;k<cutRoads.length;k++){
								if((cutRoads[k]-index)==4&&(index==10)){
									cities.get(index).addRoadDownRight();
									doIntersection[index]=true;
								}
								if((cutRoads[k]-index)==-2&&(index==13||index==14)){
									if(index==13&&doIntersection[10])cities.get(index).addRoadIntersection();
									else if(index==14&&doIntersection[11])cities.get(index).addRoadIntersection();
									else cities.get(index).addRoadDownLeft();
								}
								if(((cutRoads[k]-index)==-1)&&(index==10)){
									if(index==10&&doIntersection[8])cities.get(index).addRoadIntersection();
									else cities.get(index).addRoadDownLeft();
								}
								if((cutRoads[k]-index)==1&&(index==10||index==11||index==13))cities.get(index).addRoadDown();
								if((cutRoads[k]-index)==3&&(index==10||index==11))cities.get(index).addRoadRight();
							
							}
						}}
						
						
						
						windows.getContentPane().add(citiesPane);
						windows.getContentPane().add(imageLabel);
						
			    }
				
		  }});
		
		
		radioButtonsMaps.setBounds(WINDOW_WIDTH/2+200,WINDOW_HEIGHT/2-80,500,150);
		numberOfPlayersPanel.add(radioButtonsMaps);
	}
	private void showSelectNumberOfPlayers(){
		hideEverything();
		window.getContentPane().getComponent(4).setVisible(true);
	}
	/*private void selectNumberOfPlayersError(){
		showSelectNumberOfPlayers();
		numberOfPlayersPanel.getComponent(0).setVisible(true);
	}*/
	private Boolean firstTimeMaps=true;
	private void addMapButton(String name){
		final JRadioButton radioButton=new JRadioButton(name);
		if(radioButtonsMaps.getComponentCount()==0)radioButton.setSelected(true);
		else radioButton.setSelected(false);
		radioButtonsMaps.add(radioButton);
		buttonsMaps.add(radioButton);
		showSelectNumberOfPlayers();
	}
	
	//Lobby
	private JPanel lobbyPanel=new JPanel(null);
	private void GUICreateLobby(){
		//5
		lobbyPanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		window.getContentPane().add(lobbyPanel);
		
		JLabel label1=new JLabel("Wainting for more players...");
		label1.setBounds(WINDOW_WIDTH/2-110,WINDOW_HEIGHT/2+50,400,100);
		lobbyPanel.add(label1);

		JLabel label2=new JLabel("There are 0 players waiting.");
		label2.setBounds(WINDOW_WIDTH/2-110,WINDOW_HEIGHT/2-150,400,100);
		lobbyPanel.add(label2);
		
	}
	private void showLobby(){
		hideEverything();
		window.getContentPane().getComponent(5).setVisible(true);
	}
	private void updateLobby(int numberOfPlayers){
		((JLabel) lobbyPanel.getComponent(1)).setText("There are "+numberOfPlayers+" players waiting.");
	}
	
	//List of games
	private JPanel gamesPanel=new JPanel(null);
	private ButtonGroup buttonsGames=new ButtonGroup();
	private JPanel radioButtonsGames=new JPanel(new GridLayout(0, 1));
	private void GUIListOfGames(){
		//6
		gamesPanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		window.getContentPane().add(gamesPanel);
		
	
		
		JLabel label1=new JLabel("List of waiting games:");
		label1.setBounds(0+100,0+100,400,100);
		gamesPanel.add(label1);
		
		radioButtonsGames.setBounds(50, 200, WINDOW_WIDTH-400, 300);
		gamesPanel.add(radioButtonsGames);
		
		JButton button3=new JButton("Submit");
		button3.setBounds(WINDOW_WIDTH-115,WINDOW_HEIGHT-90,100,50);
		gamesPanel.add(button3);
		button3.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			for(int i=0;i<radioButtonsGames.getComponentCount();i++) if(((JRadioButton)radioButtonsGames.getComponent(i)).isSelected()) optionInsert(i+"");
		  }});
	}
	private void showListOfGames(){
		hideEverything();
		window.getContentPane().getComponent(6).setVisible(true);
	}
	private void addGameButton(String name){
		final JRadioButton radioButton1=new JRadioButton(name);
		if(radioButtonsGames.getComponentCount()==0)radioButton1.setSelected(true);
		else radioButton1.setSelected(false);
		radioButtonsGames.add(radioButton1);
		buttonsGames.add(radioButton1);
	}
	
	//Game is starting
	private JPanel startingPanel=new JPanel(null);
	private void GUICreateGameIsStarting(){
		//7
		startingPanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		window.getContentPane().add(startingPanel);
		
		JLabel label1=new JLabel("The game is starting...");
		label1.setBounds(WINDOW_WIDTH/2-110,WINDOW_HEIGHT/2+50,400,100);
		startingPanel.add(label1);

	}
	private void showGameIsStarting(){
		hideEverything();
		window.getContentPane().getComponent(7).setVisible(true);
	}
	
//GUIGame
	//Layout
	private ArrayList<GUIOpponentStatus> opponents=new ArrayList<GUIOpponentStatus>();
	private ArrayList<GUICity> cities=new ArrayList<GUICity>();
	private ArrayList<GUIStatusElement> status=new ArrayList<GUIStatusElement>();
	private ArrayList<GUIBonus> permitCards=new ArrayList<GUIBonus>(),playerPermitCards=new ArrayList<GUIBonus>();
	private ArrayList<GUINobilityRoadElement> nobilityRoadBonuses=new ArrayList<GUINobilityRoadElement>();
	private ArrayList<GUIBonus> bonuses=new ArrayList<GUIBonus>();
	private ArrayList<GUIColorsArray> colorsArrays=new ArrayList<GUIColorsArray>();
	private JScrollPane scrollOpponent;
	private JLayeredPane opponentsPanel=new JLayeredPane(),citiesPanel=new JLayeredPane(),layoutPanel=new JLayeredPane(),balconiesPanel=new JLayeredPane(),councillorsOutOfBalconiesPanel,playerStatusPanel=new JLayeredPane();
	private JPanel permitCardsPanel=new JPanel(null),playerPermitCardsPanel=new JPanel(null),kingRewardpanel=new JPanel(null),nobilityRoadPanel=new JPanel(null),bonusPanel=new JPanel(null);
	private GUIColorsArray politicCards=null,councillorsOutOfBalconies=null;
	private JButton submitButton=null,politicCardsButton=null;
	private JLabel regionLabel1=null,regionLabel2=null,regionLabel3=null,kingReward=null,message=null,timer=null,loading=null;
	private final int LEFT_BORDER=25,TOP_BORDER=30,COLUMN_DISTANCE=150, ROW_DISTANCE=130;
	private void GUICreateLayout(){
		ImageIcon backgroundImage = new ImageIcon("img/mapBackground.png");
		
		//0
		layoutPanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		layoutPanel.setOpaque(false);
		window.getContentPane().add(layoutPanel);
		
		window.getContentPane().setBackground(Color.WHITE);
		
		

		ImageIcon image = new ImageIcon("img/loading.png");
		loading=new JLabel(image);
		loading.setSize(image.getIconWidth(),image.getIconHeight());
		loading.setLocation(0, 0);
		layoutPanel.add(loading,50,50);
		
		
		
		
		//Regions names
		regionLabel1=new JLabel("Region 1");
		regionLabel1.setBounds(12,-35,400,100);
		layoutPanel.add(regionLabel1,5);
		regionLabel2=new JLabel("Region 2");
		regionLabel2.setBounds(310,-35,400,100);
		layoutPanel.add(regionLabel2,5);
		regionLabel3=new JLabel("Region 3");
		regionLabel3.setBounds(608,-35,400,100);
		layoutPanel.add(regionLabel3,5);
		
		//Balconies
		balconiesPanel.setBounds(0,0,2000,1000);
		balconiesPanel.setOpaque(false);
		
		//Labels
		JLabel balconyLabel1=new JLabel("Region's Balcony");
		balconyLabel1.setBounds(11,355,400,20);
		balconiesPanel.add(balconyLabel1);
		
		JLabel balconyLabel2=new JLabel("Region's Balcony");
		balconyLabel2.setBounds(490,355,400,20);
		balconiesPanel.add(balconyLabel2);
		
		JLabel balconyLabel3=new JLabel("Region's Balcony");
		balconyLabel3.setBounds(790,355,400,20);
		balconiesPanel.add(balconyLabel3);
		
		JLabel balconyLabel4=new JLabel("King's Balcony");
		balconyLabel4.setBounds(7,459,400,20);
		balconiesPanel.add(balconyLabel4);
		
		//Colors
		colorsArrays.add(new GUIColorsArray(balconiesPanel,this));
		colorsArrays.get(0).setPosition(11, 375);
		
		colorsArrays.add(new GUIColorsArray(balconiesPanel,this));
		colorsArrays.get(1).setPosition(490, 375);
		
		colorsArrays.add(new GUIColorsArray(balconiesPanel,this));
		colorsArrays.get(2).setPosition(790, 375);
		
		colorsArrays.add(new GUIColorsArray(balconiesPanel,this));
		colorsArrays.get(3).setMaxColorsPerRow(10);
		colorsArrays.get(3).setPosition(6, 478);
		
		layoutPanel.add(balconiesPanel);
		
		//Cities
		citiesPanel.setOpaque(false);
		citiesPanel.setBounds(0, 0,backgroundImage.getIconWidth(),backgroundImage.getIconHeight());
		for(int i=0;i<15;i++){
			cities.add(new GUICity(citiesPanel,this,Character.toString((char) ('A'+i))));
			cities.get(i).draw();
		}
		
		cities.get(0).setPosition(LEFT_BORDER+0*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
		cities.get(2).setPosition(LEFT_BORDER+1*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
		cities.get(1).setPosition(LEFT_BORDER+0*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
		cities.get(3).setPosition(LEFT_BORDER+1*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
		cities.get(4).setPosition(LEFT_BORDER+1*COLUMN_DISTANCE, TOP_BORDER+2*ROW_DISTANCE);
		
		cities.get(5).setPosition(LEFT_BORDER+2*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
		cities.get(8).setPosition(LEFT_BORDER+3*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
		cities.get(6).setPosition(LEFT_BORDER+2*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
		cities.get(9).setPosition(LEFT_BORDER+3*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
		cities.get(7).setPosition(LEFT_BORDER+2*COLUMN_DISTANCE, TOP_BORDER+2*ROW_DISTANCE);
		
		cities.get(10).setPosition(LEFT_BORDER+4*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
		cities.get(13).setPosition(LEFT_BORDER+5*COLUMN_DISTANCE, TOP_BORDER+0*ROW_DISTANCE);
		cities.get(11).setPosition(LEFT_BORDER+4*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
		cities.get(14).setPosition(LEFT_BORDER+5*COLUMN_DISTANCE, TOP_BORDER+1*ROW_DISTANCE);
		cities.get(12).setPosition(LEFT_BORDER+4*COLUMN_DISTANCE, TOP_BORDER+2*ROW_DISTANCE);
		
		layoutPanel.add(citiesPanel);
		
		//Permit Cards
		permitCardsPanel.setBounds(15,425,1200,100);
		permitCardsPanel.setOpaque(false);
		
		for(int i=0;i<6;i++){
			permitCards.add(new GUIBonus(permitCardsPanel,this,String.valueOf(i%2),0));
			permitCards.get(i).draw();
		}
		permitCards.get(0).setPosition(40);
		permitCards.get(1).setPosition(150);
		permitCards.get(2).setPosition(340);
		permitCards.get(3).setPosition(450);
		permitCards.get(4).setPosition(640);
		permitCards.get(5).setPosition(750);
		
		layoutPanel.add(permitCardsPanel);
		
		//King Reward
		kingRewardpanel.setBounds(7,485,1200,100);
		kingRewardpanel.setOpaque(false);
		
		JLabel kingRewardLabel1=new JLabel("King Reward");
		kingRewardLabel1.setBounds(14,0,400,50);
		kingRewardpanel.add(kingRewardLabel1);
		
		kingReward=new JLabel("0");
		kingReward.setFont(new Font(null, Font.BOLD, 20));
		kingReward.setForeground(Color.BLACK);
		kingReward.setBounds(40,25,400,50);
		kingRewardpanel.add(kingReward);
		
		layoutPanel.add(kingRewardpanel);
		
		//Bonus Rewards
		bonusPanel.setBounds(170,460,1200,100);
		bonusPanel.setOpaque(false);
		
		JLabel bonusLabel=new JLabel("<html>Bonus<br/>Rewards:</html>");
		bonusLabel.setBounds(45,-5,400,50);
		bonusPanel.add(bonusLabel);
		
		for(int i=0;i<7;i++){
			bonuses.add(new GUIBonus(bonusPanel,this,null,0));
			bonuses.get(i).draw();
		}
		bonuses.get(0).setPosition(100);
		bonuses.get(1).setPosition(190);
		bonuses.get(2).setPosition(280);
		bonuses.get(3).setPosition(370);
		bonuses.get(4).setPosition(460);
		bonuses.get(5).setPosition(550);
		bonuses.get(6).setPosition(640);
		
		layoutPanel.add(bonusPanel);
		
		//Nobility Road
		nobilityRoadPanel.setBounds(0,500,1200,100);
		nobilityRoadPanel.setOpaque(false);
		

		
		layoutPanel.add(nobilityRoadPanel);
		//Status
		playerStatusPanel.setBounds(10,550,1200,100);
		playerStatusPanel.setOpaque(false);
		
		JLabel playerStatusLabel=new JLabel("Your Status: ");
		playerStatusLabel.setBounds(0,20,100,20);
		playerStatusPanel.add(playerStatusLabel);
		
		status.add(new GUIStatusElement(playerStatusPanel));
		status.get(0).setPosition(100);
		status.get(0).setName("Money");
		status.get(0).setValuePosition(50);
		status.get(0).draw();
		
		status.add(new GUIStatusElement(playerStatusPanel));
		status.get(1).setPosition(190);
		status.get(1).setName("Helpers");
		status.get(1).setValuePosition(60);
		status.get(1).draw();
		
		status.add(new GUIStatusElement(playerStatusPanel));
		status.get(2).setPosition(100,20);
		status.get(2).setName("Nobility");
		status.get(2).setValuePosition(60);
		status.get(2).draw();
		
		status.add(new GUIStatusElement(playerStatusPanel));
		status.get(3).setPosition(190,20);
		status.get(3).setName("Score Points");
		status.get(3).setValuePosition(95);
		status.get(3).draw();
		
		JLabel politicCardsLabel=new JLabel("Politic Cards:");
		politicCardsLabel.setFont(new Font(null, Font.BOLD, 14));
		politicCardsLabel.setForeground(Color.BLACK);
		politicCardsLabel.setBounds(300,10,100,20);
		playerStatusPanel.add(politicCardsLabel);
		
		politicCards=new GUIColorsArray(playerStatusPanel,this);
		politicCards.setPosition(400, 11);
		
		politicCardsButton=new JButton("Done");
		politicCardsButton.setBounds(310,29,70,20);
		politicCardsButton.setVisible(false);
		playerStatusPanel.add(politicCardsButton);
		politicCardsButton.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			optionInsert(actionString);
			actionStringCount=0;
			politicCardsButton.setVisible(false);
		  }});
		
		JLabel permitCardsLabel=new JLabel("Permit Cards:");
		permitCardsLabel.setFont(new Font(null, Font.BOLD, 14));
		permitCardsLabel.setForeground(Color.BLACK);
		permitCardsLabel.setBounds(510,10,100,20);
		playerStatusPanel.add(permitCardsLabel);
		
		//Permit Cards
		playerPermitCardsPanel.setBounds(580,5,1200,100);
		playerPermitCardsPanel.setOpaque(false);
		playerStatusPanel.add(playerPermitCardsPanel);

		layoutPanel.add(playerStatusPanel);
		
		//Submit button
		submitButton=new JButton("Submit");
		submitButton.setBounds(910,450,100,50);
		submitButton.setVisible(false);
		layoutPanel.add(submitButton);
		submitButton.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			optionInsert(actionString);
			actionStringCount=0;
			submitButton.setVisible(false);
			for(int i=0;i<15;i++) {
				cities.get(i).setEnabled(false);
				cities.get(i).setMode(0);
			}
		  }});
		
		//Councillors out of balconies		
		councillorsOutOfBalconiesPanel=new JLayeredPane();
		councillorsOutOfBalconiesPanel.setBounds(910,0,1200,100);
		councillorsOutOfBalconiesPanel.setOpaque(false);
		
		JLabel councillorsOutOfBalconiesLabel=new JLabel("<html>Councillors avaible:</html>");
		councillorsOutOfBalconiesLabel.setBounds(0,0,100,40);
		councillorsOutOfBalconiesPanel.add(councillorsOutOfBalconiesLabel);
		
		councillorsOutOfBalconies=new GUIColorsArray(councillorsOutOfBalconiesPanel,this);
		councillorsOutOfBalconies.setPosition(0,40);
		councillorsOutOfBalconies.setMaxColorsPerRow(3);
		
		layoutPanel.add(councillorsOutOfBalconiesPanel);
		
		//Opponents
		opponentsPanel.setOpaque(false);
		
		JLabel opponentsLabel=new JLabel("OPPONENTS");
		opponentsLabel.setFont(new Font(null, Font.BOLD, 24));
		opponentsLabel.setForeground(Color.BLACK);
		opponentsLabel.setBounds(1110,0,500,20);
		layoutPanel.add(opponentsLabel,0,10);
		
		
		scrollOpponent=new JScrollPane(opponentsPanel);
		scrollOpponent.setLocation(1110,30);
		scrollOpponent.setSize(158,570);
		scrollOpponent.getViewport().setBackground(Color.WHITE);
		scrollOpponent.setBorder(null);
		scrollOpponent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollOpponent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		
		layoutPanel.add(scrollOpponent,0,10);
		
		
		//HELP MESSAGE
		message=new JLabel();
		message.setFont(new Font(null, Font.BOLD, 24));
		message.setForeground(Color.BLUE);
		message.setBounds(915,315,165,156);
		
		layoutPanel.add(message);
		
		//TIMER
		timer=new JLabel();
		timer.setFont(new Font(null, Font.BOLD, 20));
		timer.setForeground(Color.BLACK);
		timer.setBounds(910, 540, 165, 100);
		
		layoutPanel.add(timer);
		
		//Background image
		JLabel imageLabel = new JLabel("", backgroundImage, JLabel.LEFT);
		imageLabel.setSize(backgroundImage.getIconWidth(),backgroundImage.getIconHeight());
		imageLabel.setLocation(0, 0);
		layoutPanel.add(imageLabel);
		
	}
	private void showLayout(){
		hideEverything();
		
		window.getContentPane().getComponent(0).setVisible(true);
	}
	private void endOfLoading(){
		loading.setVisible(false);
	}
	
	//Not Your Turn Messages
	private JLabel notYourTurnTitle=null,notYourTurnMex=null;
	private JPanel notYourTurnPanel=new JPanel(null);
	private void GUICreateNotYourTurn(){

		window.getContentPane().add(notYourTurnPanel);
		
		//1
		notYourTurnPanel.setBounds(10,WINDOW_HEIGHT-152,WINDOW_WIDTH,500);
		notYourTurnPanel.setOpaque(false);
		
		notYourTurnTitle=new JLabel("It's Alessio's turn");
		notYourTurnTitle.setBounds(0,0,400,100);
		notYourTurnTitle.setFont(new Font(null, Font.BOLD, 14));
		notYourTurnTitle.setForeground(Color.RED);
		notYourTurnPanel.add(notYourTurnTitle);
		
		notYourTurnMex=new JLabel("Wait for your turn...");
		notYourTurnMex.setBounds(0,80,1000,20);
		notYourTurnMex.setFont(new Font(null, Font.ITALIC, 20));
		notYourTurnMex.setForeground(Color.BLACK);
		notYourTurnPanel.add(notYourTurnMex);
		
		
	}
	private void showNotMyTurn(){
		hideEverythingInGame();
		window.getContentPane().getComponent(1).setVisible(true);
		
		notYourTurnTitle.setVisible(true);
		notYourTurnMex.setVisible(true);
	}
	
	//Actions
	private JPanel actionsPanel=new JPanel(null);
	private JLabel primaryActionsAvaible=null,secondaryActionsAvaible=null;
	private JButton primaryAction1,primaryAction2,primaryAction3,primaryAction4,secondaryAction1,secondaryAction2,secondaryAction3,secondaryAction4,moreAction1;
	private void GUICreateActions(){
		final int spaceBetweenButtons=10;
		final int buttonSize=120;
		
		//2
		actionsPanel.setBounds(10,WINDOW_HEIGHT-152,WINDOW_WIDTH,500);
		actionsPanel.setOpaque(false);
		window.getContentPane().add(actionsPanel);
		
		//Primary actions
		JLabel label1=new JLabel("Primary Actions");
		label1.setBounds(0,0,400,100);
		label1.setFont(new Font(null, Font.BOLD, 14));
		label1.setForeground(Color.RED);
		actionsPanel.add(label1);
		
		primaryActionsAvaible=new JLabel();
		primaryActionsAvaible.setForeground(Color.BLACK);
		primaryActionsAvaible.setText("Avaible: 0");
		primaryActionsAvaible.setBounds(120,0,400,100);
		actionsPanel.add(primaryActionsAvaible);
		
		primaryAction1=new JButton("<html>Change Councillor</html>");
		primaryAction1.setBounds(0*buttonSize,63,buttonSize,50);
		primaryAction1.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("1");
		  }});
		
		primaryAction2=new JButton("<html>Buy Permit Build</html>");
		primaryAction2.setBounds(1*buttonSize+1*spaceBetweenButtons,63,buttonSize,50);
		primaryAction2.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("2");
		  }});
		
		primaryAction3=new JButton("<html>Build an Emporium by Permit</html>");
		primaryAction3.setBounds(2*buttonSize+2*spaceBetweenButtons,63,buttonSize,50);
		primaryAction3.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("3");
		  }});
		
		primaryAction4=new JButton("<html>Build an Emporium by King</html>");
		primaryAction4.setBounds(3*buttonSize+3*spaceBetweenButtons,63,buttonSize,50);
		primaryAction4.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("4");
		  }});

		//Secondary actions
		JLabel label2=new JLabel("Secondary Actions");
		label2.setBounds(20+4*buttonSize+4*spaceBetweenButtons,0,400,100);
		label2.setFont(new Font(null, Font.BOLD, 14));
		label2.setForeground(Color.RED);
		actionsPanel.add(label2);
		
		secondaryActionsAvaible=new JLabel();
		secondaryActionsAvaible.setForeground(Color.BLACK);
		secondaryActionsAvaible.setText("Avaible: 0");
		secondaryActionsAvaible.setBounds(20+4*buttonSize+4*spaceBetweenButtons+150,0,400,100);
		actionsPanel.add(secondaryActionsAvaible);
		
		secondaryAction1=new JButton("<html>Engange an Helper</html>");
		secondaryAction1.setBounds(20+4*buttonSize+4*spaceBetweenButtons,63,buttonSize,50);
		secondaryAction1.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("5");
		  }});
		
		secondaryAction2=new JButton("<html>Change Permit Build Card</html>");
		secondaryAction2.setBounds(20+5*buttonSize+5*spaceBetweenButtons,63,buttonSize,50);
		secondaryAction2.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("6");
		  }});
		
		secondaryAction3=new JButton("<html>Use an Helper to Change a Councillor</html>");
		secondaryAction3.setBounds(20+6*buttonSize+6*spaceBetweenButtons,63,buttonSize,50);
		secondaryAction3.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("7");
		  }});
		
		secondaryAction4=new JButton("<html>Do another Main Action</html>");
		secondaryAction4.setBounds(20+7*buttonSize+7*spaceBetweenButtons,63,buttonSize,50);
		secondaryAction4.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  optionInsert("8");
		  }});
	
		//More actions
		moreAction1=new JButton("<html>Pass your turn</html>");
		moreAction1.setBounds(40+8*buttonSize+8*spaceBetweenButtons,63,buttonSize,50);
		moreAction1.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			optionInsert("11");
		  }});

		actionsPanel.add(primaryAction1);
		actionsPanel.add(primaryAction2);
		actionsPanel.add(primaryAction3);
		actionsPanel.add(primaryAction4);
		actionsPanel.add(secondaryAction1);
		actionsPanel.add(secondaryAction2);
		actionsPanel.add(secondaryAction3);
		actionsPanel.add(secondaryAction4);
		actionsPanel.add(moreAction1);
		
		showNotMyTurn();
	
	}
	private void primaryActionsSetEnabled(Boolean a){
		primaryAction1.setEnabled(a);
		primaryAction2.setEnabled(a);
		primaryAction3.setEnabled(a);
		primaryAction4.setEnabled(a);
	}
	private void secondaryActionsSetEnabled(Boolean a){
		secondaryAction1.setEnabled(a);
		secondaryAction2.setEnabled(a);
		secondaryAction3.setEnabled(a);
		secondaryAction4.setEnabled(a);
	}
	private void moreActionsSetEnabled(Boolean a){
		moreAction1.setEnabled(a);
	}
	private void showActions(){
		hideEverythingInGame();
		
		window.getContentPane().getComponent(2).setVisible(true);
	}

	//Market
	private JPanel marketPanel=new JPanel(null);
	private JButton marketButton1,marketButton2,marketButton3,endMarketButton,marketMakeOfferButton,marketHowManyHelpersButton;
	private JLabel marketLabel,howMuchLabel,howManyHelpersLabel,marketElementsLabel;
	private JTextField howMuchTextField,howManyHelpersTextField;
	private GUIMarketElements marketElements=null;
	private void GUICreateMarket(){
		final int spaceBetweenButtons=10;
		final int buttonSize=200;
		
		//3
		marketPanel.setBounds(10,WINDOW_HEIGHT-152,WINDOW_WIDTH,500);
		marketPanel.setOpaque(false);
		window.getContentPane().add(marketPanel);
		
		//Market
		marketLabel=new JLabel("What do you want to offer?");
		marketLabel.setBounds(0,0,400,100);
		marketLabel.setFont(new Font(null, Font.BOLD, 14));
		marketLabel.setForeground(Color.BLACK);
		marketPanel.add(marketLabel);
		
		marketButton1=new JButton("<html>A permit card.</html>");
		marketButton1.setBounds(0*buttonSize,63,buttonSize,50);
		marketButton1.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			actionStringCount=0;
			  addToActionStringMarket("0");
			  marketAction1();
		  }});
		
		marketButton2=new JButton("<html>A political card.</html>");
		marketButton2.setBounds(1*buttonSize+1*spaceBetweenButtons,63,buttonSize,50);
		marketButton2.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			actionStringCount=0;
			  addToActionStringMarket("1");
			  marketAction2();
		  }});
		
		marketButton3=new JButton("<html>Some helpers.</html>");
		marketButton3.setBounds(2*buttonSize+2*spaceBetweenButtons,63,buttonSize,50);
		marketButton3.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			actionStringCount=0;
			  addToActionStringMarket("2");
			  marketAction3();
		  }});
		
		marketPanel.add(marketButton1);
		marketPanel.add(marketButton2);
		marketPanel.add(marketButton3);
		
		//MarketOffer
		howMuchLabel=new JLabel("How much do you sell it for?");
		howMuchLabel.setBounds(0,0,400,100);
		howMuchLabel.setFont(new Font(null, Font.BOLD, 14));
		howMuchLabel.setForeground(Color.BLACK);
		marketPanel.add(howMuchLabel);
		
		howMuchTextField=new JTextField();
		howMuchTextField.setBounds(0, 70, 60, 40);
		marketPanel.add(howMuchTextField);
		
		marketMakeOfferButton=new JButton("<html>Make the offer.</html>");
		marketMakeOfferButton.setBounds(70,63,120,50);
		marketMakeOfferButton.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  addToActionStringMarket(howMuchTextField.getText());
			  optionInsert(actionString);
			  actionStringCount=0;

			  showMarket();
			  
		  }});
		marketPanel.add(marketMakeOfferButton);
		
		//Market sell helpers
		howManyHelpersLabel=new JLabel("How many helpers do you want to sell?");
		howManyHelpersLabel.setBounds(0,0,400,100);
		howManyHelpersLabel.setFont(new Font(null, Font.BOLD, 14));
		howManyHelpersLabel.setForeground(Color.BLACK);
		marketPanel.add(howManyHelpersLabel);
		
		howManyHelpersTextField=new JTextField();
		howManyHelpersTextField.setBounds(0, 70, 60, 40);
		marketPanel.add(howManyHelpersTextField);
		
		marketHowManyHelpersButton=new JButton("<html>Submit.</html>");
		marketHowManyHelpersButton.setBounds(70,63,120,50);
		marketHowManyHelpersButton.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			  addToActionStringMarket(howManyHelpersTextField.getText());

			  marketOffer();
			  
		  }});
		marketPanel.add(marketHowManyHelpersButton);
		
		//EndMarketButton
		endMarketButton=new JButton("<html>End the market.</html>");
		endMarketButton.setBounds(1000,63,200,50);
		endMarketButton.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
				optionInsert("F");
		  }});
		marketPanel.add(endMarketButton);
		
		//Market Elements
		marketElementsLabel=new JLabel("These are the offers:");
		marketElementsLabel.setBounds(0,0,400,100);
		marketElementsLabel.setFont(new Font(null, Font.BOLD, 14));
		marketElementsLabel.setForeground(Color.BLACK);
		marketPanel.add(marketElementsLabel);
		
		marketElements=new GUIMarketElements(marketPanel,this);
		marketElements.setPosition(0, 60);
		
	}
	private void showMarket(){
		hideEverythingInGame();
		
		window.getContentPane().getComponent(3).setVisible(true);
		
		marketLabel.setVisible(true);
		marketButton1.setVisible(true);
		marketButton2.setVisible(true);
		marketButton3.setVisible(true);
		howMuchLabel.setVisible(false);
		howMuchTextField.setVisible(false);
		marketMakeOfferButton.setVisible(false);
		endMarketButton.setVisible(true);
		howManyHelpersLabel.setVisible(false);
		howManyHelpersTextField.setVisible(false);
		marketHowManyHelpersButton.setVisible(false);
		marketElementsLabel.setVisible(false);
		marketElements.setVisible(false);
		
		if(playerPermitCards.size()==0)marketButton1.setEnabled(false);
		if(politicCards.getNumberOfElements()==0)marketButton2.setEnabled(false);
		if(status.get(1).getValue()==0)marketButton3.setEnabled(false);
	}
	private void showMarketElements(){
		hideEverythingInGame();
		
		window.getContentPane().getComponent(3).setVisible(true);
		
		marketLabel.setVisible(false);
		marketButton1.setVisible(false);
		marketButton2.setVisible(false);
		marketButton3.setVisible(false);
		howMuchLabel.setVisible(false);
		howMuchTextField.setVisible(false);
		marketMakeOfferButton.setVisible(false);
		endMarketButton.setVisible(true);
		howManyHelpersLabel.setVisible(false);
		howManyHelpersTextField.setVisible(false);
		marketHowManyHelpersButton.setVisible(false);
		marketElements.setVisible(true);
		marketElementsLabel.setVisible(true);
	}
	private void marketAction1(){
		message.setText("<html>Choose the permit card to sell.</html>");
		
		marketLabel.setVisible(false);
		marketButton1.setVisible(false);
		marketButton2.setVisible(false);
		marketButton3.setVisible(false);
		endMarketButton.setVisible(false);
		
		endOfLoading();
		for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
		for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
		for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
		politicCards.setEnabled(false);
		councillorsOutOfBalconies.setEnabled(false);
		primaryActionsSetEnabled(false);
		secondaryActionsSetEnabled(false);
		moreActionsSetEnabled(false);
		
		for(int i=0;i<playerPermitCards.size();i++) playerPermitCards.get(i).setTrueEnabled(true);
		
		for(int i=0;i<playerPermitCards.size();i++)playerPermitCards.get(i).setMode(1);
		
		
	}
	private void marketAction2(){
		message.setText("<html>Choose the politic card to sell.</html>");
		
		marketLabel.setVisible(false);
		marketButton1.setVisible(false);
		marketButton2.setVisible(false);
		marketButton3.setVisible(false);
		endMarketButton.setVisible(false);
		
		endOfLoading();
		for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
		for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
		for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
		politicCards.setEnabled(true);
		councillorsOutOfBalconies.setEnabled(false);
		primaryActionsSetEnabled(false);
		secondaryActionsSetEnabled(false);
		moreActionsSetEnabled(false);
		
		politicCards.setMode(2);
	}
	private void marketAction3(){

		marketLabel.setVisible(false);
		marketButton1.setVisible(false);
		marketButton2.setVisible(false);
		marketButton3.setVisible(false);
		endMarketButton.setVisible(false);
		howManyHelpersLabel.setVisible(true);
		howManyHelpersTextField.setVisible(true);
		marketHowManyHelpersButton.setVisible(true);
	}
	public void marketOffer(){
		message.setText("<html>How much do you want to sell for.</html>");
		
		for(int i=0;i<playerPermitCards.size();i++){
			playerPermitCards.get(i).setEnabled(false);
			playerPermitCards.get(i).setMode(0);
		}
		

		howManyHelpersLabel.setVisible(false);
		howManyHelpersTextField.setVisible(false);
		marketHowManyHelpersButton.setVisible(false);
		
		howMuchLabel.setVisible(true);
		howMuchTextField.setVisible(true);
		marketMakeOfferButton.setVisible(true);
		
	}

//End of the Game
	private JLayeredPane endGamePanel=new JLayeredPane();
	private GUIRanking endGameRanking=null;
	private void GUICreateEndOfTheGame(){
		//4
		endGamePanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		endGamePanel.setOpaque(false);
		window.getContentPane().add(endGamePanel);
		
		window.getContentPane().setBackground(Color.WHITE);
		
		ImageIcon image = new ImageIcon("img/gameOver.png");
		JLabel loading=new JLabel(image);
		loading.setSize(image.getIconWidth(),image.getIconHeight());
		loading.setLocation(0, 0);
		endGamePanel.add(loading,50,50);
		
		JLabel ranking=new JLabel("<html><h1 Color='RED'>RANKING</h1></html>",SwingConstants.CENTER);
		ranking.setBounds(400,180,450,420);
		endGameRanking=new GUIRanking(ranking);
		endGamePanel.add(ranking,51,50);
		
		JButton backToTheBeginning=new JButton("Leave the game");
		backToTheBeginning.setBounds(1050,570,200,100);
		endGamePanel.add(backToTheBeginning,51,50);
		backToTheBeginning.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent e)
		  {
			System.exit(0);
		  }});
		
		
		
	}
	private void showEndOfTheGame(){
		hideEverything();
		
		window.getContentPane().getComponent(4).setVisible(true);
	}
	
	
	//CORE
	public void update(Observable o, Object arg) {
		Page p=(Page)o;
		if(arg.toString().length()>0)
			printOnVideo(p,arg);
		else
			printOnVideo(p,"");
	}
	private boolean firstTime=true;
	private int currentTime,lastTime;
	private void printOnVideo(Page page, Object d)
	{
		EnumPage p = page.getPage();
		if(d.equals(EnumStatusGame.timerTurn))
		{
			lastTime=currentTime;
			currentTime=page.getTimer();
        	String print="Time left: "+page.getTimer();
			timer.setText(print);
			
	        timer.setVisible(true);

			
				Timer timerz = new Timer();
		        timerz.schedule(new TimerTask() {

		            @Override
		            public void run() {
		    			timer.setVisible(false);
		    			timerz.cancel();
		            }
		        }, 985);
				
		        
				return;
		}
		if (p.equals(EnumPage.main_menu))showWelcomeToTheCouncil();
		if (p.equals(EnumPage.connection_mode))showChangeConnectionMode();
		if (p.equals(EnumPage.play_online))showSelectAName();
		if (p.equals(EnumPage.name_not_valid))showSelectANameError();
		if (p.equals(EnumPage.how_many_players))showSelectNumberOfPlayers();
		if (p.equals(EnumPage.menu_online)){
			showMenuOnline();
			String date = d.toString();
			if(date==null||date.equals(""))date="0";
			updatePlayersCount(Integer.parseInt(date));
		}
		if (p.equals(EnumPage.update_menu_online)) {
			showMenuOnline();
			String date = d.toString();
			if(date==null||date.equals(""))date="0";
			updatePlayersCount(Integer.parseInt(date));
		}
		if (p.equals(EnumPage.lobby)) {
			showLobby();
			String date = d.toString();
			updateLobby(Integer.parseInt(date));
		}
		if (p.equals(EnumPage.join_game)) {
			
			long list[][] = (long[][]) d;
				showListOfGames();
				for (int i = 0; i < list.length; i++) 
				addGameButton(i+") \nPlayers waiting in this game: "+list[i][0]+"   Max number of players in this game: "+list[i][1] + "   Game code = " + list[i][2]+"  Map Selected = "+list[i][3]);
				
			}
		if(p.equals(EnumPage.list_of_maps))
		{	
			mapz=(String[][][])d;
			for(int m=0;m<mapz.length;m++) addMapButton(String.valueOf(m));
		}
		if(p.equals(EnumPage.game_status))
		{	
			LocalStateGame local=page.getLocalStateGame();
			EnumStatusGame statusGame=local.getStatusGame();

			

			if(statusGame.equals(EnumStatusGame.gameStart))showGameIsStarting();
			if(statusGame.equals(EnumStatusGame.map))printMap(local);
			if(statusGame.equals(EnumStatusGame.permitCards))printPermitCard(local);
			if(statusGame.equals(EnumStatusGame.kingBoard))printKingBoard(local);
			if(statusGame.equals(EnumStatusGame.nobilityRoad))printNobilityRoad(local);
			if(statusGame.equals(EnumStatusGame.opponentStatus))printOpponentStatus(local);
			if(statusGame.equals(EnumStatusGame.playerStatus))printPlayerStatus(local);
			//action 1 Change Councillor
			if(statusGame.equals(EnumStatusGame.listCouncillorOutOfBalconies))
			{
				printListCouncillorOutOfBalconies(local);
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(true);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				message.setText("<html>Select an avaible councillor.</html>");
			}
			if(statusGame.equals(EnumStatusGame.balconyChose))
			{		
				message.setText("<html>Select the balcony.</html>");
				
				printBalconies(local,1);
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(true);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
			}
			//action 2 Buy Permit Card
			if(statusGame.equals(EnumStatusGame.balconyChoseForCorrupt))
			{
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				for(int i=0;i<3;i++)colorsArrays.get(i).setEnabled(true);
				
				message.setText("<html>Choose the balcony.</html>");
				
				printBalconies(local,1);
				
			}
			if(statusGame.equals(EnumStatusGame.politicalCardChosen))
			{
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(true);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				message.setText("<html>Choose the politic cards to use.</html>");
			}
			if(statusGame.equals(EnumStatusGame.whichPermitCardGet))
			{
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				int balconyChose=local.getBalconyChosenForCorruption();
				for(int i=0;i<6;i++)
					if(i==balconyChose*2||i==balconyChose*2+1)permitCards.get(i).setTrueEnabled(true);
					else permitCards.get(i).setTrueEnabled(false);
				
				
				message.setText("<html>Choose the permit card.<html>");
			}
			//action 3 Build an Emporium By Permit
			if(statusGame.equals(EnumStatusGame.indexPermitCard))
			{
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				for(int i=0;i<playerPermitCards.size();i++) playerPermitCards.get(i).setTrueEnabled(true);
				
				message.setText("<html>Choose the permit card to use.</html>");
				
				notChosenYet=true;
				
			}
			if(statusGame.equals(EnumStatusGame.townNameChosed))
			{
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				for(int i=0;i<playerPermitCards.size();i++) playerPermitCards.get(i).setEnabled(false);
				
				
				for(int i=0;i<15;i++)
					for(int j=0;j<chosenPermit.length();j++)
						if(cities.get(i).getName().charAt(0)==chosenPermit.charAt(j))cities.get(i).setEnabled(true);
				
				message.setText("<html>Choose the city where you want to build.</html>");
			}
			//action 4 Build an Emporium By King
			if(statusGame.equals(EnumStatusGame.politicCardsUseToKing))
			{
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(true);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				message.setText("<html>Choose the politic cards you want to use.</html>");
				
				
			}
			if(statusGame.equals(EnumStatusGame.townChosenToKing))
			{
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				for(int i=0;i<playerPermitCards.size();i++) playerPermitCards.get(i).setEnabled(false);
				
				for(int i=0;i<15;i++) cities.get(i).setEnabled(true);
				
				message.setText("<html>Choose the city where you want to build.</html>");
				
			}
			if(statusGame.equals(EnumStatusGame.routeToFollow))
			{
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				for(int i=0;i<playerPermitCards.size();i++) playerPermitCards.get(i).setEnabled(false);;
				
				for(int i=0;i<15;i++) {
					cities.get(i).setEnabled(true);
					cities.get(i).setMode(1,option);
				}
				
				message.setText("<html><font size='5'>Choose the cities the king will have to go through.</font></html>");
			}
			//action 6 Change Permit Build Card
			if(statusGame.equals(EnumStatusGame.regionChose))
			{
				message.setText("<html>Select the balcony.</html>");
				
				printBalconies(local,1);
				
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				colorsArrays.get(0).setEnabled(true);
				colorsArrays.get(1).setEnabled(true);
				colorsArrays.get(2).setEnabled(true);			
				
			}
			//action 7 Use an Helper To Change Councillor
			if(statusGame.equals(EnumStatusGame.listCouncillorFreeByHelper))
			{	
				printListCouncillorOutOfBalconies(local);
				
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(true);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
					
				
				message.setText("<html>Select an avaible councillor.</html>");
			}
			if(statusGame.equals(EnumStatusGame.balconyChoseByHelper))
			{
				message.setText("<html>Select the balcony.</html>");
				
				printBalconies(local,1);
				
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				for(int i=0;i<playerPermitCards.size();i++) playerPermitCards.get(i).setEnabled(false);
				


				colorsArrays.get(0).setEnabled(true);
				colorsArrays.get(1).setEnabled(true);
				colorsArrays.get(2).setEnabled(true);
				colorsArrays.get(4).setEnabled(true);
				
			}
			//action for chose the bonus in nobility road
			if(statusGame.equals(EnumStatusGame.select1BonusToken))
			{
				message.setText("<html><span color='green'>Bonus city reward.</span><br/><span>Choose a city.</span></html>");
				
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				for(int i=0;i<playerPermitCards.size();i++) playerPermitCards.get(i).setEnabled(false);;
				
				for(int i=0;i<15;i++) {
					cities.get(i).setEnabled(true);
					cities.get(i).setMode(1,option);
				}
				
			}
			if(statusGame.equals(EnumStatusGame.select2BonusToken))
			{
				message.setText("<html><span color='green'>Bonus city reward.</span><br/><span>Choose two cities.</span></html>");
				
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				for(int i=0;i<playerPermitCards.size();i++) playerPermitCards.get(i).setEnabled(false);;
				
				for(int i=0;i<15;i++) {
					cities.get(i).setEnabled(true);
					cities.get(i).setMode(2,option);
				}
				
				
			}
			if(statusGame.equals(EnumStatusGame.select1PermitCard))
			{		
				message.setText("<html><span color='green'>Bonus city reward.</span><br/><span>Choose permit card.</span></html>");
				
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				int balconyChose=local.getBalconyChosenForCorruption();
				for(int i=0;i<6;i++)
					if(i==balconyChose*2||i==balconyChose*2+1)permitCards.get(i).setTrueEnabled(true);
					else permitCards.get(i).setTrueEnabled(false);
			}
			if(statusGame.equals(EnumStatusGame.select1BonusPermitCard))
			{
				message.setText("<html><span color='green'>Bonus city reward.</span><br/><span>Choose a permit card.</span></html>");
				
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				for(int i=0;i<playerPermitCards.size();i++) playerPermitCards.get(i).setTrueEnabled(true);
				
				notChosenYet=true;
				
				
				
				
			}
			if(statusGame.equals(EnumStatusGame.resourceNotEnought))
			{
				message.setText("<html><span color='red'>Not enought resources to do that.</span><html>");
				showError=true;
			}
			if(statusGame.equals(EnumStatusGame.errorInsertedDate))
			{
				message.setText("<html><span color='red'>You can't do that!<br/>Try again.</span><html>");
				showError=true;
			}
			if(statusGame.equals(EnumStatusGame.opponentTurn))
			{
				
				printListCouncillorOutOfBalconies(local);
				
				endOfLoading();
				for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
				for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
				for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
				politicCards.setEnabled(false);
				councillorsOutOfBalconies.setEnabled(false);
				primaryActionsSetEnabled(false);
				secondaryActionsSetEnabled(false);
				moreActionsSetEnabled(false);
				
				String nameOpponent=d.toString();
				showNotMyTurn();
				message.setText("<html>It's "+nameOpponent+"'s turn.</html>");
				notYourTurnTitle.setText("It's "+nameOpponent+"'s turn");
				notYourTurnMex.setText("Wait for your turn...");
				for(int i=0;i<opponents.size();i++){
					if(nameOpponent.equals(opponents.get(i).getName()))opponents.get(i).setTurn(true);
					else opponents.get(i).setTurn(false);
				}
			}
			//status game during the market period
			if(statusGame.equals(EnumStatusGame.marketStart))
			{
				endOfLoading();
				message.setText("<html><span color='green'>The Market</span></html>");
				showMarket();
			}
			if(statusGame.equals(EnumStatusGame.turnDoneOffert))
			{
				endOfLoading();
				if(!showError)
					message.setText("<html><span color='blue'>Offer added.</span><html>");
				Timer timer = new Timer();
		        timer.schedule(new TimerTask() {

		            @Override
		            public void run() {
		            	message.setText("<html><span color='green'>The Market</span></html>");
		            	timer.cancel();
		            }
		        }, 2500);
				
			}
			if(statusGame.equals(EnumStatusGame.waitTurnDoneOffertFinish))
			{
				endOfLoading();
				showNotMyTurn();
				notYourTurnTitle.setVisible(false);
				notYourTurnMex.setText("Wait for everyone to finish to offer...");
			}
			if(statusGame.equals(EnumStatusGame.resourcesNotEnoughtToOffert))
			{
				endOfLoading();
				message.setText("<html><span color='red'>You don't have enought resources to make that offer.</span><html>");
				showError=true;
				Timer timer = new Timer();
		        timer.schedule(new TimerTask() {

		            @Override
		            public void run() {
		            	message.setText("<html><span color='green'>The Market</span></html>");
		            	showError=false;
		            	timer.cancel();
		            }
		        }, 2500);
				
			}
			if(statusGame.equals(EnumStatusGame.yourTurnToBuy))
			{
				endOfLoading();
				
				int time=0;
				if(showError)time=2500;
				Timer timer = new Timer();
		        timer.schedule(new TimerTask() {

		            @Override
		            public void run() {
		            	message.setText("<html>What do you want to buy?</html>");
		            	showError=false;
		            	timer.cancel();
		            }
		        }, time);
				
				
				
				showMarketElements();
				
				marketElements.clear();
				
				String[][]offert=local.getOfferts();
				for(int i=0;i<offert.length;i++)
				{
					if(!offert[i][0].equals(local.getPlayerName())){
					if(offert[i][1].equals("0"))
					{//permit card
						String []tmp=offert[i][2].split(";");
						marketElements.addElement(0,offert[i][0], tmp[0],Integer.valueOf(offert[i][3]),String.valueOf(i));
					}
					if(offert[i][1].equals("1"))
					{//political card
						marketElements.addElement(1,offert[i][0], offert[i][2],Integer.valueOf(offert[i][3]),String.valueOf(i));
					}
					if(offert[i][1].equals("2"))
					{//helper
						marketElements.addElement(2,offert[i][0], offert[i][2],Integer.valueOf(offert[i][3]),String.valueOf(i));
					}}
				}
				
			}
			if(statusGame.equals(EnumStatusGame.opponentTurnToBuy))
			{
				endOfLoading();
				String nameOpponent=d.toString();
				
				showNotMyTurn();
				message.setText("<html>It's "+nameOpponent+"'s turn to buy.</html>");
				notYourTurnTitle.setText("It's "+nameOpponent+"'s turn.");
				notYourTurnMex.setText("Wait for your turn...");
			}
			if(statusGame.equals(EnumStatusGame.resourcesNotEnoughtToBuy))
			{
				endOfLoading();
				message.setText("<html><span color='red'>You don't have enought money to buy that.</span><html>");
				Timer timer = new Timer();
		        timer.schedule(new TimerTask() {

		            @Override
		            public void run() {
		            	message.setText("<html><span color='green'>The Market</span></html>");
		            	timer.cancel();
		            }
		        }, 2500);
			}
		}
		if(p.equals(EnumPage.your_turn))
		{
			endOfLoading();
			for(int i=0;i<6;i++)permitCards.get(i).setEnabled(false);
			for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).setEnabled(false);
			for(int i=0;i<15;i++)cities.get(i).setEnabled(false);
			politicCards.setEnabled(false);
			councillorsOutOfBalconies.setEnabled(false);
			primaryActionsSetEnabled(false);
			secondaryActionsSetEnabled(false);
			moreActionsSetEnabled(true);

			
			int time=0;
			if(showError)time=2500;
			Timer timer = new Timer();
	        timer.schedule(new TimerTask() {

	            @Override
	            public void run() {
	            	message.setText("<html>It's your turn.<br/>Choose an action.</html>");
	            	showError=false;
	            	timer.cancel();
	            }
	        }, time);
			
	        
			LocalStateGame local=page.getLocalStateGame();
			if(local.getMainActionRemain()==0)primaryActionsSetEnabled(false);
			else primaryActionsSetEnabled(true);
			if(local.getSecondaryAction()==0)secondaryActionsSetEnabled(false);
			else secondaryActionsSetEnabled(true);
			primaryActionsAvaible.setText("Avaible: "+local.getMainActionRemain());
			secondaryActionsAvaible.setText("Avaible: "+local.getSecondaryAction());
			
			showActions();
			if(playerPermitCards.size()==0) primaryAction3.setEnabled(false);
			
			if(numberOfShownElements<1) primaryAction3.setEnabled(false);
		}
		
		if(p.equals(EnumPage.finish_game))
		{
			showEndOfTheGame();
			
			String[][]rank=(String[][])d;
			for(int i=0;i<rank.length;i++)
			{
				endGameRanking.addElement(rank[i][0]+" = "+rank[i][1]);
			}
			
		}
		
	}


	//Utility
	public void optionInsert(String input)
	{
		option=input;
		setChanged();
		notifyObservers(option);
	}

	private void printMap(LocalStateGame local){
		if(firstTime){initGUIGame();firstTime=false;}
		
		String [][]map=local.getMap();
		String[] regionNames=local.getRegionName();
		final int townsPerRegion=map.length/regionNames.length;
		
		regionLabel1.setText(regionNames[0]);
		regionLabel2.setText(regionNames[1]);
		regionLabel3.setText(regionNames[2]);
		
		int index;
		boolean firstBonus;
		GUICity currentCity;
		boolean[] doIntersection=new boolean[15];
		for(int k=0;k<doIntersection.length;k++)doIntersection[k]=false;
		for(int i=0;i<regionNames.length;i++)
		{
			for(int j=0;j<townsPerRegion;j++)
			{
				
				
				index=j+townsPerRegion*i;
				currentCity=cities.get(index);
				
				
				char name=(char) ('A'+index);
				currentCity.setName(String.valueOf(name));
				currentCity.setKingIsHere(false);
				if(local.getWhereIsKing().equals(String.valueOf(name))) currentCity.setKingIsHere(true);
				
				
				if("blue".equals(map[index][7])) currentCity.setColor(Color.BLUE);
				else if("orange".equals(map[index][7])) currentCity.setColor(Color.ORANGE);
				else if("yellow".equals(map[index][7])) currentCity.setColor(Color.YELLOW);
				else if("purple".equals(map[index][7])) currentCity.setColor(Color.PINK);
				else if("gray".equals(map[index][7])) currentCity.setColor(Color.GRAY);

				
				firstBonus=true;
				
				if(!map[index][1].equals("0")){
					if(firstBonus){
						currentCity.setRewards(AbbreviationRewardType.M+" x"+map[index][1]);
						firstBonus=false;
					}
					else currentCity.addRewards(" - "+AbbreviationRewardType.M+" x"+map[index][1]);
				}
				if(!map[index][2].equals("0")){
					if(firstBonus){
						currentCity.setRewards(AbbreviationRewardType.H+" x"+map[index][2]);
						firstBonus=false;
					}
					else currentCity.addRewards(" - "+AbbreviationRewardType.H+" x"+map[index][2]);
				}
				if(!map[index][3].equals("0")){
					if(firstBonus){
						currentCity.setRewards(AbbreviationRewardType.PC+" x"+map[index][3]);
						firstBonus=false;
					}
					else currentCity.addRewards(" - "+AbbreviationRewardType.PC+" x"+map[index][3]);
				}
				if(!map[index][4].equals("0")){
					if(firstBonus){
						currentCity.setRewards(AbbreviationRewardType.N+" x"+map[index][4]);
						firstBonus=false;
					}
					else currentCity.addRewards(" - "+AbbreviationRewardType.N+" x"+map[index][4]);
				}
				if(!map[index][5].equals("0")){
					if(firstBonus){
						currentCity.setRewards(AbbreviationRewardType.SP+" x"+map[index][5]);
						firstBonus=false;
					}
					else currentCity.addRewards(" - "+AbbreviationRewardType.SP+" x"+map[index][5]);
				}
				
				String roads=map[index][0];
				int[] cutRoads=new int[roads.length()];
				for(int k=0;k<roads.length();k++){
					cutRoads[k]=Character.getNumericValue(roads.charAt(k))-Character.getNumericValue('A');
				}
				
				//Roads
				if(index<5){
					for(int k=0;k<cutRoads.length;k++){
						if((cutRoads[k]-index)==3){
							if(index==0||index==1){
								cities.get(index).addRoadDownRight();
								doIntersection[index]=true;
							}
							else if(index==2||index==3||index==4)cities.get(index).addRoadRight();
						}
						if((cutRoads[k]-index)==4&&(index==2||index==3)){
							cities.get(index).addRoadDownRight();
							doIntersection[index]=true;
						}
						if((cutRoads[k]-index)==-1&&index==2){
							if(doIntersection[0]) cities.get(0).addRoadIntersection();
							else cities.get(index).addRoadDownLeft();
						}
						if((cutRoads[k]-index)==1&&(index==0||index==2||index==3))cities.get(index).addRoadDown();
						if((cutRoads[k]-index)==2&&(index==0||index==1))cities.get(index).addRoadRight();
						
					}
				}
				else if(index<10){
					for(int k=0;k<cutRoads.length;k++){
						if((cutRoads[k]-index)==3){
							if(index==5||index==6)cities.get(index).addRoadRight();
							else if(index==8||index==9){
								cities.get(index).addRoadDownRight();
								doIntersection[index]=true;
							}
						}
						if((cutRoads[k]-index)==4&&(index==5)){
							cities.get(index).addRoadDownRight();
							doIntersection[index]=true;
						}
						if((cutRoads[k]-index)==-2&&(index==5||index==6||index==8||index==9)){
							if(index==5&&doIntersection[2])cities.get(index).addRoadIntersection();
							else if(index==6&&doIntersection[3])cities.get(index).addRoadIntersection();
							else if(index==8&&doIntersection[5])cities.get(index).addRoadIntersection();
							else if(index==9&&doIntersection[6])cities.get(index).addRoadIntersection();
							else cities.get(index).addRoadDownLeft();
						}
						if((cutRoads[k]-index)==1&&(index==5||index==6||index==8))cities.get(index).addRoadDown();
						if((cutRoads[k]-index)==2&&(index==8||index==9))cities.get(index).addRoadRight();
						
						if((cutRoads[k]-index)==5&&(index==7))cities.get(index).addRoadRightLong();
					}
				}else{
					for(int k=0;k<cutRoads.length;k++){
						if((cutRoads[k]-index)==4&&(index==10)){
							cities.get(index).addRoadDownRight();
							doIntersection[index]=true;
						}
						if((cutRoads[k]-index)==-2&&(index==13||index==14)){
							if(index==13&&doIntersection[10])cities.get(index).addRoadIntersection();
							else if(index==14&&doIntersection[11])cities.get(index).addRoadIntersection();
							else cities.get(index).addRoadDownLeft();
						}
						if(((cutRoads[k]-index)==-1)&&(index==10)){
							if(index==10&&doIntersection[8])cities.get(index).addRoadIntersection();
							else cities.get(index).addRoadDownLeft();
						}
						if((cutRoads[k]-index)==1&&(index==10||index==11||index==13))cities.get(index).addRoadDown();
						if((cutRoads[k]-index)==3&&(index==10||index==11))cities.get(index).addRoadRight();
					
					}
				}
			}
		}
		printEmporiums(local);
	}
	private void printEmporiums(LocalStateGame local){
		//Emporiums
		String [][]map=local.getMap();
		for(int index=0;index<15;index++)
		if(map[index][6].length()!=0){
			cities.get(index).clearEmporiums();
			cities.get(index).addEmporium(map[index][6]);
		}
	}
	private void printPermitCard(LocalStateGame local)
	{
		String[]nameRegion=local.getRegionName();
		String[][]permitCard=local.getPermitCardsTown();
		
		int permitCardForRegion=permitCard.length/nameRegion.length;
		
		GUIBonus currentPermitCard=null;
		
		for(int i=0;i<nameRegion.length;i++){
			for(int j=0;j<permitCardForRegion;j++){
				int index=j+i*permitCardForRegion;
				currentPermitCard=permitCards.get(index);
				currentPermitCard.clear();
				currentPermitCard.setCities(permitCard[index][0],1);
				if(!permitCard[index][1].equals("0")){
					currentPermitCard.addRewards(AbbreviationRewardType.M+permitCard[index][1]+" ");
				}
				if(!permitCard[index][2].equals("0")){
					currentPermitCard.addRewards(AbbreviationRewardType.H+permitCard[index][2]+" ");
				}
				if(!permitCard[index][3].equals("0")){
					currentPermitCard.addRewards(AbbreviationRewardType.PC+permitCard[index][3]+" ");
				}
				if(!permitCard[index][4].equals("0")){
					currentPermitCard.addRewards(AbbreviationRewardType.N+permitCard[index][4]+" ");
				}
				if(!permitCard[index][5].equals("0")){
					currentPermitCard.addRewards(AbbreviationRewardType.SP+permitCard[index][5]+" ");
				}
				if(!permitCard[index][6].equals("0")){
					currentPermitCard.addRewards(AbbreviationRewardType.MA+permitCard[index][6]+" ");
				}
			}
		}
	}
	private void printKingBoard(LocalStateGame local) {
		String[]nameRegion=local.getRegionName();
		int[]kingRewards=local.getKingReward();
		
		//Balconies
		printBalconies(local,0);
		
		//King reward
		kingReward.setText(""+kingRewards[0]);
		
		//Bonus rewards
		String[][]bonusReward=local.getBonusReward();
		int regionNumber=0;
		for(int i=0;i<bonusReward.length;i++){
			if(i>bonusReward.length-4){
				bonuses.get(i).setCities(nameRegion[regionNumber],0);
				regionNumber++;
			}
			else bonuses.get(i).setCities(bonusReward[i][0],1);
			bonuses.get(i).setRewards(bonusReward[i][1]);
		}
	}
	private void printNobilityRoad(LocalStateGame local) {
		
		int[][]nobilityRoad=local.getBonusNobilityRoad();
		String[] statusPlayer=local.getStatusPlayer();
		String[][]statusOpponent=local.getStatusOpponent();
		
		nobilityRoadBonuses.clear();
		nobilityRoadPanel.removeAll();
		
		for(int i=0;i<20;i++){
			nobilityRoadBonuses.add(new GUINobilityRoadElement(nobilityRoadPanel));
			nobilityRoadBonuses.get(i).draw();
			nobilityRoadBonuses.get(i).setPosition(118+i*39,18);
		}
		
		for(int i=0;i<nobilityRoad.length/10;i++)
		{
			for(int j=i*10;j<10+i*10;j++)
			{
				nobilityRoadBonuses.get(j).clear();
				
				if(statusPlayer!=null)
					if(j==Integer.valueOf(statusPlayer[2])-1)
						nobilityRoadBonuses.get(j).addPlayer("("+String.valueOf(local.getPlayerName().toUpperCase().charAt(0))+")");
				if(statusOpponent!=null)
					for(int x=0;x<statusOpponent.length;x++) 
						if(j==Integer.valueOf(statusOpponent[x][4])-1)
							nobilityRoadBonuses.get(j).addPlayer(String.valueOf(statusOpponent[x][0].toUpperCase().charAt(0)));
				
				if(nobilityRoad[j][0]!=0){
					nobilityRoadBonuses.get(j).addReward("Mx"+nobilityRoad[j][0]);
				}
				if(nobilityRoad[j][1]!=0){
					nobilityRoadBonuses.get(j).addReward("Hx"+nobilityRoad[j][1]);
				}
				if(nobilityRoad[j][2]!=0){
					nobilityRoadBonuses.get(j).addReward("PCx"+nobilityRoad[j][2]);
				}
				if(nobilityRoad[j][3]!=0){
					nobilityRoadBonuses.get(j).addReward("Nx"+nobilityRoad[j][3]);
				}
				if(nobilityRoad[j][4]!=0){
					nobilityRoadBonuses.get(j).addReward("SPx"+nobilityRoad[j][4]);
				}
				if(nobilityRoad[j][5]!=0){
					nobilityRoadBonuses.get(j).addReward("MAx"+nobilityRoad[j][5]);
				}
				if(nobilityRoad[j][6]!=0){
					nobilityRoadBonuses.get(j).addReward("PBx"+nobilityRoad[j][6]);
				}
				if(nobilityRoad[j][7]!=0){
					nobilityRoadBonuses.get(j).addReward("BPBx"+nobilityRoad[j][7]);
				}
				if(nobilityRoad[j][8]!=0){
					nobilityRoadBonuses.get(j).addReward("BTx"+nobilityRoad[j][8]);
				}
			}
		}
	}
	private void printOpponentStatus(LocalStateGame local) {
		String[][]statusOpponent=local.getStatusOpponent();
		opponents.clear();
		opponentsPanel.removeAll();
		for(int i=0;i<statusOpponent.length;i++)
		{
			opponents.add(new GUIOpponentStatus(opponentsPanel));
			opponents.get(i).setPosition(0, i*170);
			opponents.get(i).setName(statusOpponent[i][0]);
			opponents.get(i).setMoney(Integer.valueOf(statusOpponent[i][1]));
			opponents.get(i).setHelpers(Integer.valueOf(statusOpponent[i][2]));
			opponents.get(i).setScore(Integer.valueOf(statusOpponent[i][3]));
			opponents.get(i).setNobility(Integer.valueOf(statusOpponent[i][4]));
			opponents.get(i).setQBPC(Integer.valueOf(statusOpponent[i][5]));
			opponents.get(i).setQPC(Integer.valueOf(statusOpponent[i][6]));
			opponents.get(i).setQE(Integer.valueOf(statusOpponent[i][7]));
			
			opponents.get(i).draw();
		}
		
		opponentsPanel.setPreferredSize(new Dimension(300,opponents.size()*170));
		scrollOpponent.setViewportView(opponentsPanel);
		
		printNobilityRoad(local);
		
	} 
	private int numberOfShownElements=0;
	private void printPlayerStatus(LocalStateGame local) {
		
		String[] statusPlayer=local.getStatusPlayer();
		String[] politicalCards=local.getPoliticalCards();
		String[][]permitCards=local.getPermitCardsTownsPlayer();

		status.get(0).setValue(Integer.valueOf(statusPlayer[0]));
		status.get(1).setValue(Integer.valueOf(statusPlayer[1]));
		status.get(2).setValue(Integer.valueOf(statusPlayer[2]));
		status.get(3).setValue(Integer.valueOf(statusPlayer[3]));
		
		//Politic cards
		politicCards.clear();
		for(int i=0;i<politicalCards.length;i++)
		{
			politicCards.addColor(politicalCards[i],""+i,1);
		}
		
		printBalconies(local,0);
		printListCouncillorOutOfBalconies(local);
		
		//Permit cards
		for(int r=0;r<playerPermitCards.size();r++)playerPermitCards.remove(r);
		playerPermitCardsPanel.removeAll();
		
		numberOfShownElements=0;
		for(int i=0;i<permitCards.length;i++)
		{
				playerPermitCards.add(new GUIBonus(playerPermitCardsPanel,this,String.valueOf(i),0));
				try{
					playerPermitCards.get(i).setMode(0);
					playerPermitCards.get(i).setAction(String.valueOf(i));
					playerPermitCards.get(i).setPosition(30+numberOfShownElements*40,5);
					playerPermitCards.get(i).setCities(permitCards[i][1],1);
					playerPermitCards.get(i).setRewards("");
					playerPermitCards.get(i).setEnabled(false);
					playerPermitCards.get(i).setTiny(true);
					if(permitCards[i][0].equals(false+""))playerPermitCards.get(i).draw();
					
				}catch(IndexOutOfBoundsException e){
					StartServer.logger.log(Level.SEVERE, "Problem in the GUI", e);
				}
				numberOfShownElements++;
				
				if(!permitCards[i][0].equals(false+"")) numberOfShownElements--;
		}

		if(numberOfShownElements<1) primaryAction3.setEnabled(false);
	}
	private void printListCouncillorOutOfBalconies(LocalStateGame local){
		String []councillors=local.getCouncillorNameOutOfBalcony();
		
		councillorsOutOfBalconies.clear();
		
		councillorsOutOfBalconies=new GUIColorsArray(councillorsOutOfBalconiesPanel,this);
		councillorsOutOfBalconies.setPosition(0,40);
		councillorsOutOfBalconies.setMaxColorsPerRow(3);
		for(int i=0;i<councillors.length;i++)
		{
			councillorsOutOfBalconies.addColor(councillors[i],""+i);
		}
	}
	private void printBalconies(LocalStateGame local,int mode){
		String[][]balconies=local.getCouncilBalconyName();
		String action=null;
		
		for(int i=0;i<colorsArrays.size();i++)colorsArrays.get(i).clear();
		
		//Colors
		colorsArrays.add(new GUIColorsArray(balconiesPanel,this));
		colorsArrays.get(0).setPosition(11, 375);
		
		colorsArrays.add(new GUIColorsArray(balconiesPanel,this));
		colorsArrays.get(1).setPosition(490, 375);
		
		colorsArrays.add(new GUIColorsArray(balconiesPanel,this));
		colorsArrays.get(2).setPosition(790, 375);
		
		colorsArrays.add(new GUIColorsArray(balconiesPanel,this));
		colorsArrays.get(3).setMaxColorsPerRow(10);
		colorsArrays.get(3).setPosition(6, 478);
		
		//Balconies
		for(int i=0;i<4;i++)
		{
			for(int j=balconies[i].length-1;j>=0;j--)
			{
			if(mode==0)action=String.valueOf(j);
			else if(mode==1)action=String.valueOf(i);
			
			colorsArrays.get(i).addColor(balconies[i][j],action);
			}
		}
		
		printListCouncillorOutOfBalconies(local);
	}

	public void setChosenPermit(String a){
		if(notChosenYet){
			chosenPermit=a;
			notChosenYet=false;
		}
	}
	public void addToActionStringPC(String a){
		if(actionStringCount==0){
			actionString=a;
			politicCardsButton.setVisible(true);
		}
		else actionString=actionString+"-"+a;
		actionStringCount++;
		
		if(actionStringCount==1){
			politicCardsButton.setVisible(true);
		}
		else if(actionStringCount==4) {
			optionInsert(actionString);
			actionStringCount=0;
			politicCardsButton.setVisible(false);
		}
	}
	public void addToActionStringCity(String a){
		if(actionStringCount==0){
			actionString=a;
			submitButton.setVisible(true);
		}
		else actionString=actionString+a;
		actionStringCount++;
		
		if(actionStringCount==1){
			submitButton.setVisible(true);
		}
	}
	public void addToActionStringBonusCity(String a){
		if(actionStringCount==0)actionString=a;
		else actionString=actionString+"-"+a;
		actionStringCount++;
		
		if(actionStringCount==2){
			optionInsert(actionString);
			actionStringCount=0;
			actionString=null;
		}
	}
	public void addToActionStringMarket(String a){
		if(actionStringCount==0) actionString=a;
		else actionString=actionString+"-"+a;
		actionStringCount++;
		if(actionStringCount==4){
			actionStringCount=0;
			optionInsert(actionString);
		}
	}
}