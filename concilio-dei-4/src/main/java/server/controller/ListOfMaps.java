package server.controller;

import java.io.BufferedReader;
import java.io.FileReader;

import server.model.GameConstants;

/**
 * This class reads every file configuration and can create a list of maps
 */
public class ListOfMaps {
	final static private int howManyMaps=4;
	/**
	 * @return array 3D with different map in 2D: every map has in every row -> name of town, connection of town, color of town
	 */
	public String[][][] listOfMaps(){
		int towns=GameConstants.numberOfRegion*GameConstants.townsForRegion;
		String []s=new String[howManyMaps];
		for(int i=0;i<howManyMaps;i++)
		{
			s[i]=readConfigurationFile(i);
		}
		String [][][] maps=new String[howManyMaps][towns][2];
		for(int m=0;m<howManyMaps;m++)
		{
			String[]split=s[m].split("&");
			String[]color=split[0].split("\n");
			String[]connections=split[1].split("\n");
			for(int i=0;i<towns;i++)
			{
				maps[m][i][0]=connections[i];
				maps[m][i][1]=color[i];
			}
		}
		return maps;
	}
	
	private String readConfigurationFile(int i)
	{
		// The name of the file I want to open.
        String fileName = "src/map"+i+".txt";
        // This will refer one line at a time
        String configurationGame="";
        String line = "";
        try (BufferedReader bufferedReader =  new BufferedReader(new FileReader(fileName))){
            // Always wrap FileReader in BufferedReader.
            
            while((line = bufferedReader.readLine()) != null) {
                configurationGame=configurationGame+line+"\n";
            }   
            // Always close files.
            bufferedReader.close();
        }catch(Exception e) {
            System.err.println("ERROR="+e);                
        }
        return configurationGame;
	}
}
