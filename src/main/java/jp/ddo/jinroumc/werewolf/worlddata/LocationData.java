package jp.ddo.jinroumc.werewolf.worlddata;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LocationData {
	static boolean clickSign(Player pl, Block block, int x, int y, int z, String command){
		if(block.getX()==x && block.getY()==y && block.getZ()==z){
			pl.performCommand(command);
			return true;
		}
		return false;
	}
}
