package jp.ddo.jinroumc.werewolf.util;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageTime;
import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillagePlayer;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendMessage {
	public static void splitUnspecifiedMessage(Player sender, String message){
		String[] splitList = message.split("\\\\", -1);
		for(String split : splitList)
			unspecifiedMessage(sender, split);
	}
	
	public static void unspecifiedMessage(Player sender, String message){
		Village vil = VillageUtil.getVillage(sender);
		VillagePlayer vp = vil.getPlayer(sender);
		
		if(vil.status!=VillageStatus.ongoing){
			sendToVillageByPlayer(vp, message, vil);
		}else{
			if(!vp.alive){
				sendToGhost(sender, message, vil);
			}else{
				if(vil.time!=VillageTime.night){
					sendToVillageByPlayer(vp, message, vil);
				}else{
					if(vp.role==VillageRole.jinrou)
						sendToWolf(sender, message, vil);
					else if(vp.role==VillageRole.youko)
						sendToFox(sender, message, vil);
					else
						sendToMyself(sender, message);
				}
			}
		}
	}
	
	public static void sendToServer(String message){
		for(Player pl : Bukkit.getOnlinePlayers())
			pl.sendMessage(message);
	}
	
	public static void sendToLobbyByPlayer(Player sender, String message){
		for(Player pl : Bukkit.getOnlinePlayers())
			if(!VillageUtil.isInVillage(pl))
				pl.sendMessage("<"+sender.getName()+"> "+message);
	}
	
	public static void sendToVillageByPlayer(VillagePlayer vp, String message, Village vil){
		if(vp.gameMaster
				&& (vil.status==VillageStatus.preparing
				|| vil.status==VillageStatus.recruiting
				|| vil.status==VillageStatus.finishing))
			for(VillagePlayer pc : vil.getPlayerListExceptNPC())
				pc.sendMessage(C.d_aqua+"["+vp.getName()+"] "
								+C.reset+""+message);
		else
			for(VillagePlayer pc : vil.getPlayerListExceptNPC())
				pc.sendMessage("["+vp.getName()+"] "+C.reset+message);
		
		if(vp.alive
				&& vil.status==VillageStatus.ongoing && vil.time==VillageTime.day){
			int score = vil.objective.getScore(Bukkit.getOfflinePlayer(vp.getColorName())).getScore();
			score ++;
			vil.objective.getScore(Bukkit.getOfflinePlayer(vp.getColorName())).setScore(score);
		}
	}
	
	public static void sendToGhost(Player sender, String message, Village vil){
		for(VillagePlayer vp : vil.getPlayerListExceptAliveWhileOngoing())
			vp.sendMessage(C.gray+"["+sender.getName()+" to ghost] "+message);
	}

	public static void sendToWolf(Player sender, String message, Village vil){
		for(VillagePlayer vp : vil.getPlayerListExceptNPC()){
			if(vp.role==VillageRole.jinrou)
				vp.sendMessage(C.d_red+"["+sender.getName()+" to wolf] "+message);
			else{
				if(message.length()<=5){
					vp.sendMessage(C.d_red+"[人狼のだれか] アオーーン");
				}else if(message.length()>15){
					vp.sendMessage(C.d_red+"[人狼のだれか] アオーーーーーーーーーーーーン");
				}else{
					String howl = "アオーー";
					for(int i=0; i<message.length()-5; i++)
						howl += "ー";
					howl += "ン";
					vp.sendMessage(C.d_red+"[人狼のだれか] "+howl);
				}
			}
		}
	}

	public static void sendToFox(Player sender, String message, Village vil){
		for(VillagePlayer vp : vil.getPlayerListExceptNPC()){
			if(vp.role==VillageRole.youko)
				vp.sendMessage(C.yellow+"["+sender.getName()+" to fox] "+message);
			else{
				if(message.length()<=5){
					vp.sendMessage(C.yellow+"[妖狐のだれか] コーーーン");
				}else if(message.length()>15){
					vp.sendMessage(C.yellow+"[妖狐のだれか] コーーーーーーーーーーーーーン");
				}else{
					String howl = "コーーー";
					for(int i=0; i<message.length()-5; i++)
						howl += "ー";
					howl += "ン";
					vp.sendMessage(C.yellow+"[妖狐のだれか] "+howl);
				}
			}
		}
	}

	public static void sendToMyself(Player sender, String message){
		sender.sendMessage(C.gray+"["+sender.getName()+" to myself] "+message);
	}
}
