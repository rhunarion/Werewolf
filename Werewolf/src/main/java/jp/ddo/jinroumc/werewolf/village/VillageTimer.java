package jp.ddo.jinroumc.werewolf.village;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageTime;
import jp.ddo.jinroumc.werewolf.util.C;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class VillageTimer extends VillageCore {
	
	VillageTimer(String villageName, String villageType, JavaPlugin plugin){
		super(villageName, villageType, plugin);
	}
	
	public void setTimer(final String message, final int initSeconds){
		final VillageTimer vil = this;
		stopTimer();
		
		timerID = Bukkit.getScheduler().runTaskTimer(plugin, new BukkitRunnable(){

			int seconds = initSeconds;
			int rnd = new Random().nextInt(9)+2;
			
			@Override
			public void run(){
				
				int min = seconds/60;
				int sec = seconds - min*60;
				String time = min+":";
				time = sec<=9 ? time+"0"+sec : time+sec;
				float percent = (float) (100.0/initSeconds*seconds);
				
				if(seconds>=0){
					for(VillagePlayer vp : vil.getPlayerListExceptNPC())
						if(vp.player.isOnline() && !(vp.getName().matches("p\\d+")))
							if(seconds>30)
								BarAPI.setMessage(vp.getPlayer(), C.yellow+vil.title+C.white
													+" ("+vil.villageName+") "+message+time, percent);
							else
								BarAPI.setMessage(vp.getPlayer(), C.yellow+vil.title+C.white
													+" ("+vil.villageName+") "+message+C.red+time, percent);
					if(seconds==30 && vil.status==VillageStatus.ongoing && vil.time==VillageTime.day)
						for(VillagePlayer vp : vil.getAlivePlayerListExceptNPC())
							if(vp.votedPlayer==null)
								vp.sendMessage(C.gold+"あなたは未投票です。"
										+"集会場の看板をクリックして、処刑までに投票を完了してください。");
					if(seconds==initSeconds/rnd && !vil.tryBiting
							&& vil.status==VillageStatus.ongoing
							&& vil.day!=0 && vil.time==VillageTime.night
							&& vil.getJinrouListExceptNPC().size()==0){
						List<VillagePlayer> bitableList = vil.getBitablePlayerList();
						Collections.shuffle(bitableList);
						List<VillagePlayer> jinrouList = vil.getJinrouList();
						Collections.shuffle(jinrouList);
						
						jinrouList.get(0).bitePlayer(bitableList.get(0));
					}
					seconds--;
				}else{
					for(VillagePlayer vp : vil.getPlayerListExceptNPC())
						BarAPI.removeBar(vp.getPlayer());
					vil.stopTimer();
					vil.afterTimer();
				}
			}
		}, 20, 20).getTaskId();
	}
	
	public void stopTimer(){
		if(timerID!=-1){
			Bukkit.getScheduler().cancelTask(timerID);
			timerID = -1;
			for(VillagePlayer vp : getPlayerListExceptNPC())
				BarAPI.removeBar(vp.getPlayer());
		}
	}
	
	public void stopDoTaskLater(){
		if(doTaskLaterID!=-1){
			Bukkit.getScheduler().cancelTask(doTaskLaterID);
			doTaskLaterID = -1;
		}
	}
	
	public void stopAsyncRebuild(){
		if(asyncRebuildID!=-1){
			Bukkit.getScheduler().cancelTask(asyncRebuildID);
			asyncRebuildID = -1;
		}
	}

	public void resetTimer(){
		stopTimer();
		switch(status){
		case preparing: setTimer("<準備中>  :募集開始まで ", 60); break;
		case recruiting: setTimer("<募集中>  ：終了まで ", 900); break;
		case finishing: setTimer("<終了中>  ：解散まで ", 300); break;
		}
	}
	
	public void afterTimer(){
		Village vil = (Village) this;
		
		if(status==VillageStatus.preparing)
			vil.gameRecruiting();
		else if(status==VillageStatus.recruiting)
			vil.gameFinishing();
		else if(status==VillageStatus.ongoing && time==VillageTime.day)
			vil.totalizeVote();
		else if(status==VillageStatus.ongoing && time==VillageTime.revote)
			vil.totalizeVote();
		else if(status==VillageStatus.ongoing && time==VillageTime.night)
			vil.checkResult();
		else if(status==VillageStatus.finishing)
			vil.rebuildVillage();
	}
	
}
