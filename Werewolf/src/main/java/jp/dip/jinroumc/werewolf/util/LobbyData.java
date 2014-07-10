package jp.dip.jinroumc.werewolf.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class LobbyData{
	private static String lobbyWorldName;
	private static Location lobbyLocation; 
	
	public static void initLobbyLocation(FileConfiguration config){
		lobbyWorldName = config.getString("lobbyLocation.worldName");
		
		if(config.getBoolean("lobbyLocation.useDefaultSpawnPoint")){
			lobbyLocation	= Bukkit.getWorld(lobbyWorldName).getSpawnLocation();
		}else{
			double x = config.getDouble("lobbyLocation.x");
			double y = config.getDouble("lobbyLocation.y");
			double z = config.getDouble("lobbyLocation.z");
			lobbyLocation	= new Location(Bukkit.getWorld(lobbyWorldName), x, y, z, 0, 0);
		}
	}
	
	public static String getLobbyWorldName(){
		return lobbyWorldName; 
	}
	
	public static Location getLobby(){
		return lobbyLocation;
	}
}
