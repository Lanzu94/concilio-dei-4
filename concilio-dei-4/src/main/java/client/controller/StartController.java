package client.controller;

import java.util.Observable;
import java.util.Observer;

import client.model.*;
import client.view.*;

/**
 * This class reads the option that the player chooses at the beginning of the game (Connection Mode and Graphics Mode)
 *
 */
public class StartController implements Observer{
	/*
	 * I create this controller as first in the main
	 */
	private EnumPage pages;
	
	
	private SelectGUICLI chosen;
	private SetUpPlayer player;
	
	public StartController(SelectGUICLI s)
	{
		chosen=s;
		player=new SetUpPlayer();
	}
	private void play(String [] s)
	{
		Thread t=null;
		if(s[1].equals(ClientConstants.RMI)) player.setConnectionMode(ClientConstants.RMI);
		else player.setConnectionMode(ClientConstants.Socket);
		
		if(s[0].equals(ClientConstants.GUI))
		{
			player.setUpGraphic(ClientConstants.GUI);
			t = new Thread(new StartGameGUI(player));
		}	
		else
		{
			player.setUpGraphic(ClientConstants.CLI);
			t = new Thread(new StartGameCLI(player));
		}
        t.start();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		String[]s=chosen.inizia();
		play(s);
	}
	private static class StartGameCLI implements Runnable {
		private SetUpPlayer player;
		public StartGameCLI(SetUpPlayer p){
			player=p;
		}
		@Override
		public void run() {
			Page p=new Page();//model
			CLI view=new CLI();//view
			CLIController cli_contr=new CLIController(p,player);//controller
			view.addObserver(cli_contr);//cli_contr observes cli
			p.addObserver(view);//cli observes p
			p.setPage(EnumPage.main_menu,"");
			Thread.interrupted();		
		}
	}
	
	private static class StartGameGUI implements Runnable {
		private SetUpPlayer player;
		public StartGameGUI(SetUpPlayer p){
			player=p;
		}
		@Override
		public void run() {
			Page p=new Page();
			GUI view=new GUI();
			GUIController gui_contr=new GUIController(p,player);
			view.addObserver(gui_contr);
			p.addObserver(view);
			p.setPage(EnumPage.main_menu,"");
			Thread.interrupted();
		}
	}
}
