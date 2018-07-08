package client.controller;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import client.model.ClientConstants;
import client.model.EnumPage;
import client.model.Page;
import client.model.SetUpPlayer;
import client.view.CLI;
import client.view.GUI;

/**
 * this class checks the options that the player inserts in the offline menu in GUI mode
 */
public class GUIController implements Observer, Serializable{
	/*
	 * This is the controller the player uses when he is in the main menu.
	 * When he chooses "play online" the program creates CLI_RMI_Controller if the connection_mode=RMI
	 * or creates CLI_SocketController if the connection_mode=Socket
	 */

	//constant EnumPage

	private Page modelPage;
	private SetUpPlayer player;
	transient private GUI gui;
	
	public GUIController(Page p, SetUpPlayer play)
	{
		modelPage=p;
		player=play;
	}
	
	private void managerOption(String n)
	{
		boolean validOption=false;
		
		//option that I can select in the main menu
		if(modelPage.getPage().equals(EnumPage.main_menu) && n.equals("1"))
		{//go to play online.. then insert name
			if(ClientConstants.RMI.equals(player.getConnetionMode()))
			{
				ClientController cliRmiContr=new ClientController(modelPage,player);
				gui.addObserver(cliRmiContr);	
				gui.deleteObserver(this);
				modelPage.setPage(EnumPage.play_online,"");	
				validOption=true;
			}
			if(ClientConstants.Socket.equals(player.getConnetionMode()))
			{//connection_mode=Socket
				ClientController cliRmiContr=new ClientController(modelPage,player);
				SocketClient cliSocketContr=new SocketClient(modelPage,player,cliRmiContr);
				gui.addObserver(cliSocketContr);	
				gui.deleteObserver(this);
				modelPage.setPage(EnumPage.play_online,"");	
				validOption=true;
			}
		}
		//option that I can select in connection mode
		if((modelPage.getPage().equals(EnumPage.connection_mode) || modelPage.getPage().equals(EnumPage.selected_rmi) || modelPage.getPage().equals(EnumPage.selected_socket)) && "1".equals(n))
		{//selected rmi
			player.setConnectionMode(ClientConstants.RMI);
			modelPage.setPage(EnumPage.selected_rmi,"");
			validOption=true;
		}
		if((modelPage.getPage().equals(EnumPage.connection_mode) || modelPage.getPage().equals(EnumPage.selected_rmi) || modelPage.getPage().equals(EnumPage.selected_socket)) && "2".equals(n))
		{//selected socket
			player.setConnectionMode(ClientConstants.Socket);
			modelPage.setPage(EnumPage.selected_socket,"");
			validOption=true;
			
		}
		if(modelPage.getPage().equals(EnumPage.main_menu) && "2".equals(n))
		{//go to connection mode menu
			modelPage.setPage(EnumPage.connection_mode,"");
			validOption=true;
		}
		if((modelPage.getPage().equals(EnumPage.connection_mode) || modelPage.getPage().equals(EnumPage.selected_rmi) || modelPage.getPage().equals(EnumPage.selected_socket)) && "3".equals(n))
		{//go back to the menu
			modelPage.setPage(EnumPage.main_menu,"");
			validOption=true;
		}
		if(!validOption)
		{//if the player uses a not valid key I print again the page where he was
			modelPage.setPage(modelPage.getPage(),"");
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		String option=arg.toString();
		gui=(GUI) o;
		managerOption(option);
	}

	

}
