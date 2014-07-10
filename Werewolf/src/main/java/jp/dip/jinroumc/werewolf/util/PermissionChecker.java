package jp.dip.jinroumc.werewolf.util;

import java.util.ArrayList;
import java.util.List;

import jp.dip.jinroumc.werewolf.enumconstant.VillageRole;
import jp.dip.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.dip.jinroumc.werewolf.enumconstant.VillageTime;
import jp.dip.jinroumc.werewolf.village.Village;
import jp.dip.jinroumc.werewolf.village.VillagePlayer;
import jp.dip.jinroumc.werewolf.village.VillageUtil;

import org.bukkit.entity.Player;

public class PermissionChecker {
	public static List<String> getCommandList(Player pl){
		List<String> cmdList = new ArrayList<String>();

		cmdList.add("help");
		cmdList.add("showVil");
		
		if(!VillageUtil.isInVillage(pl)){
			cmdList.add("enterVil");
			cmdList.add("lobby");
			cmdList.add("makeVil");
			return cmdList;
		}

		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.alive || vil.status!=VillageStatus.ONGOING)
			cmdList.add("enterVil");
		if(!vp.alive|| vil.status!=VillageStatus.ONGOING)
			cmdList.add("leaveVil");
		if(!vp.alive|| vil.status!=VillageStatus.ONGOING)
			cmdList.add("lobby");
		if(true)
			cmdList.add("showPlayers");
		if(vil.status==VillageStatus.EMPTY)
			cmdList.add("makeVil");
		if(vp.gameMaster && vil.status==VillageStatus.PREPARING)		
			cmdList.add("startRec");
		if(vp.gameMaster && vil.status==VillageStatus.RECRUITING)		
			cmdList.add("stopRec");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.RECRUITING || vil.status==VillageStatus.PREPARING
				|| vil.status==VillageStatus.FINISHING))		
			cmdList.add("resetTimer");
		if(vp.gameMaster)
			cmdList.add("giveGM");
		if(vp.gameMaster
				&& (!vp.alive || vil.status!=VillageStatus.ONGOING || vil.time!=VillageTime.NIGHT))
			cmdList.add("configRoom");
		if(vp.gameMaster && vil.status!=VillageStatus.FINISHING)
			cmdList.add("chRule");
		if(true)
			cmdList.add("showRule");
		if(vp.gameMaster)
			cmdList.add("shoutRule");
		if(!vp.joining && vil.status==VillageStatus.RECRUITING)
			cmdList.add("join");
		if(vp.joining
				&& (vil.status==VillageStatus.PREPARING || vil.status==VillageStatus.RECRUITING))
			cmdList.add("unjoin");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.PREPARING || vil.status==VillageStatus.RECRUITING))
			cmdList.add("setRole");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.PREPARING || vil.status==VillageStatus.RECRUITING))
			cmdList.add("unsetRole");
		if(vp.joining && vil.requestRole
				&& (vil.status==VillageStatus.PREPARING || vil.status==VillageStatus.RECRUITING))
			cmdList.add("requestRole");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.PREPARING || vil.status==VillageStatus.RECRUITING))
			cmdList.add("startGame");
		if(vp.gameMaster && vil.status==VillageStatus.ONGOING)
			cmdList.add("finishGame");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.ONGOING || vil.status==VillageStatus.FINISHING))
			cmdList.add("skip");
		if(vp.gameMaster)
			cmdList.add("kick");
		if(vp.gameMaster)
			cmdList.add("unkick");
		if(vp.gameMaster && vil.status==VillageStatus.ONGOING)
			cmdList.add("kill");
		if(vil.status!=VillageStatus.ONGOING || vil.time!=VillageTime.EXECUTION
				|| vil.executedPlayer!=vp || !vp.alive)
			cmdList.add("home");
		if(vp.alive && vil.permitWhisp
				&& vil.status==VillageStatus.ONGOING && vil.time!=VillageTime.NIGHT)
			cmdList.add("whisp");
		if(vp.gameMaster && vil.status==VillageStatus.ONGOING)
			cmdList.add("shout");
		if(vp.alive && vil.status==VillageStatus.ONGOING && vil.time!=VillageTime.NIGHT)
			cmdList.add("co");
		if(vp.joining && vil.status==VillageStatus.ONGOING)
			cmdList.add("myRole");
		if(vp.alive
				&& vil.status==VillageStatus.ONGOING && (vil.time==VillageTime.NOON || vil.time==VillageTime.REVOTE))
			cmdList.add("vote");
		if(vp.alive && vp.role==VillageRole.URANAISHI && !vp.tryUranai
				&& vil.status==VillageStatus.ONGOING && vil.time==VillageTime.NIGHT)
			cmdList.add("uranai");
		if(vp.alive && vp.role==VillageRole.KARIUDO && vil.day!=0
				&& vil.status==VillageStatus.ONGOING && vil.time!=VillageTime.NIGHT)
			cmdList.add("guard");
		if(vp.alive && vp.role==VillageRole.JINROU && !vil.tryBiting && vil.permitBite
				&& vil.status==VillageStatus.ONGOING && vil.time==VillageTime.NIGHT)
			cmdList.add("bite");
		
		return cmdList;
	}
}
