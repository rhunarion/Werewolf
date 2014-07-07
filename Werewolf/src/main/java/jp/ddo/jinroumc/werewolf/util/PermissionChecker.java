package jp.ddo.jinroumc.werewolf.util;

import java.util.ArrayList;
import java.util.List;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageTime;
import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;
import jp.ddo.jinroumc.werewolf.village.VillagePlayer;

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
		if(!vp.alive || vil.status!=VillageStatus.ongoing)
			cmdList.add("enterVil");
		if(!vp.alive|| vil.status!=VillageStatus.ongoing)
			cmdList.add("leaveVil");
		if(!vp.alive|| vil.status!=VillageStatus.ongoing)
			cmdList.add("lobby");
		if(true)
			cmdList.add("showPlayers");
		if(vil.status==VillageStatus.empty)
			cmdList.add("makeVil");
		if(vp.gameMaster && vil.status==VillageStatus.preparing)		
			cmdList.add("startRec");
		if(vp.gameMaster && vil.status==VillageStatus.recruiting)		
			cmdList.add("stopRec");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.recruiting || vil.status==VillageStatus.preparing
				|| vil.status==VillageStatus.finishing))		
			cmdList.add("resetTimer");
		if(vp.gameMaster)
			cmdList.add("giveGM");
		if(vp.gameMaster
				&& (!vp.alive || vil.status!=VillageStatus.ongoing || vil.time!=VillageTime.night))
			cmdList.add("configRoom");
		if(vp.gameMaster && vil.status!=VillageStatus.finishing)
			cmdList.add("chRule");
		if(true)
			cmdList.add("showRule");
		if(vp.gameMaster)
			cmdList.add("shoutRule");
		if(!vp.joining && vil.status==VillageStatus.recruiting)
			cmdList.add("join");
		if(vp.joining
				&& (vil.status==VillageStatus.preparing || vil.status==VillageStatus.recruiting))
			cmdList.add("unjoin");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.preparing || vil.status==VillageStatus.recruiting))
			cmdList.add("setRole");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.preparing || vil.status==VillageStatus.recruiting))
			cmdList.add("unsetRole");
		if(vp.joining && vil.requestRole
				&& (vil.status==VillageStatus.preparing || vil.status==VillageStatus.recruiting))
			cmdList.add("requestRole");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.preparing || vil.status==VillageStatus.recruiting))
			cmdList.add("startGame");
		if(vp.gameMaster && vil.status==VillageStatus.ongoing)
			cmdList.add("finishGame");
		if(vp.gameMaster
				&& (vil.status==VillageStatus.ongoing || vil.status==VillageStatus.finishing))
			cmdList.add("skip");
		if(vp.gameMaster)
			cmdList.add("kick");
		if(vp.gameMaster)
			cmdList.add("unkick");
		if(vp.gameMaster && vil.status==VillageStatus.ongoing)
			cmdList.add("kill");
		if(true)
			cmdList.add("home");
		if(vp.alive && vil.permitWhisp
				&& vil.status==VillageStatus.ongoing && vil.time!=VillageTime.night)
			cmdList.add("whisp");
		if(vp.gameMaster && vil.status==VillageStatus.ongoing)
			cmdList.add("shout");
		if(vp.alive && vil.status==VillageStatus.ongoing && vil.time!=VillageTime.night)
			cmdList.add("co");
		if(vp.joining && vil.status==VillageStatus.ongoing)
			cmdList.add("myRole");
		if(vp.alive
				&& vil.status==VillageStatus.ongoing && (vil.time==VillageTime.day || vil.time==VillageTime.revote))
			cmdList.add("vote");
		if(vp.alive && vp.role==VillageRole.uranaishi && !vp.tryUranai
				&& vil.status==VillageStatus.ongoing && vil.time==VillageTime.night)
			cmdList.add("uranai");
		if(vp.alive && vp.role==VillageRole.kariudo && vil.day!=0
				&& vil.status==VillageStatus.ongoing && vil.time!=VillageTime.night)
			cmdList.add("guard");
		if(vp.alive && vp.role==VillageRole.jinrou && !vil.tryBiting && vil.permitBite
				&& vil.status==VillageStatus.ongoing && vil.time==VillageTime.night)
			cmdList.add("bite");
		
		return cmdList;
	}
}
