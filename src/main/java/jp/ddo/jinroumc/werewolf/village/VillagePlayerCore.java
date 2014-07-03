package jp.ddo.jinroumc.werewolf.village;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.util.C;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class VillagePlayerCore{
	public OfflinePlayer player;
	public LivingEntity villagerEntity;
	public Village village;
	public VillageRole role;
	public VillageRole roleRequested;
	public ChatColor color;
	public boolean gameMaster;
	public boolean joining;
	public boolean alive;
	public boolean connection;
	public int numBeingVoted;
	public VillagePlayer votedPlayer;
	public VillagePlayer guardPlayer;
	public boolean tryUranai;

	VillagePlayerCore(Player pl){
		this.player = (OfflinePlayer) pl;
		this.villagerEntity = null;
		this.village = VillageUtil.getVillage(pl);
		this.role = VillageRole.none;
		this.roleRequested = VillageRole.none;
		this.color = C.white;
		this.gameMaster = false;
		this.joining = false;
		this.alive = false;
		this.connection = true;
		this.numBeingVoted = 0;
		this.votedPlayer = null;
		this.guardPlayer = null;
		this.tryUranai = false;
	}
	
	VillagePlayerCore(OfflinePlayer pl, Village vil){
		this.player = pl;
		this.village = vil;
		this.role = VillageRole.none;
		this.roleRequested = VillageRole.none;
		this.color = C.white;
		this.gameMaster = false;
		this.joining = false;
		this.alive = false;
		this.connection = false;
		this.numBeingVoted = 0;
		this.votedPlayer = null;
		this.guardPlayer = null;
	}
	
	public String getName(){
		return player.getName();
	}
	
	public void sendMessage(String message){
		((Player) player).sendMessage(message);
	}
	
	public Player getPlayer(){
		return (Player) player;
	}
	
	public String getColorName(){
		String colorName = color + getName();
		if(colorName.length()<=16)
			return colorName;
		else
			return colorName.substring(0, 16);
	}
	
	public String getStrikethroughName(){
		String stName = C.strikethrough + getName();
		if(stName.length()<=16)
			return stName;
		else
			return stName.substring(0, 16);
	}
}