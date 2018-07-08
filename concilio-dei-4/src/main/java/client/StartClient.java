package client;

import client.controller.StartController;
import client.view.SelectGUICLI;

/**
 * With this class I start the client
 */
public class StartClient {
	public static void main( String[] args )
    {
       SelectGUICLI s=new SelectGUICLI();//view
       StartController m=new StartController(s);//controller
       s.addObserver(m);
       
    } 
}
