package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.controller.RMIControllerServer;
import server.controller.SocketServer;
import server.model.StatusServer;

/**
 * With this class I start the server
 */
public class StartServer {
	final public static  Logger logger=Logger.getLogger("logger");
	public static void main (String args[]){
		try{
			System.out.println("Server is ready");
			StatusServer statusServer=new StatusServer();
			LocateRegistry.createRegistry(1099);
			RMIControllerServer a=new RMIControllerServer(statusServer);
			Naming.rebind("rmi://localhost/SERVER", a);
			SocketServer s=new SocketServer(statusServer, a);
		}catch(Exception e){logger.log(Level.SEVERE, "Problem with the server", e);}
	}
} 
