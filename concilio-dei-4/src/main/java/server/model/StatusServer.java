package server.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class has the status of the server: player and running game
 */
public class StatusServer implements Serializable{
	//Players
	private ArrayList<Player> players = new ArrayList<Player>();//list of online player
	public void addPlayer(Player player){
		players.add(player);
	}
	public Player getPlayer(int i){
		return players.get(i);
	}
	public void removePlayer(int i){
		players.remove(i);
	}
	public int sizePlayers(){
		return players.size();
	}
	public int onlinePlayers(){
		int cont=0;
		for(int i=0;i<sizePlayers();i++)
		{
			EnumStatePlayer s=players.get(i).getStatePlayer();
			if(s.equals(EnumStatePlayer.online)||s.equals(EnumStatePlayer.inGame)||s.equals(EnumStatePlayer.lobby))
				cont++;
		}
		return cont;
	}
	
	//Games
	private ArrayList<Game> games=new ArrayList<Game>();//list of games
	public void addGame(Game game){
		games.add(game);
	}
	public Game getGame(int i){
		return games.get(i);
	}
	public void removeGame(int i){
		games.remove(i);
	}
	public int sizeGames(){
		return games.size();
	}
}
