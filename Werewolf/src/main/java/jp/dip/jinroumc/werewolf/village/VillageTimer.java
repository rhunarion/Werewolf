package jp.dip.jinroumc.werewolf.village;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.dip.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.dip.jinroumc.werewolf.enumconstant.VillageTime;
import jp.dip.jinroumc.werewolf.util.C;
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
		
		timerId = Bukkit.getScheduler().runTaskTimer(plugin, new BukkitRunnable(){

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
					for(VillagePlayer vp : vil.getPlayerListExceptNpc())
						if(vp.player.isOnline() && !(vp.getName().contains(".")))
							if(seconds>30)
								BarAPI.setMessage(vp.getPlayer(), C.yellow+vil.title+C.white
													+" ("+vil.villageName+") "+message+time, percent);
							else
								BarAPI.setMessage(vp.getPlayer(), C.yellow+vil.title+C.white
													+" ("+vil.villageName+") "+message+C.red+time, percent);
					if(seconds==30 && vil.status==VillageStatus.ONGOING && vil.time==VillageTime.NOON)
						for(VillagePlayer vp : vil.getAlivePlayerListExceptNpc())
							if(vp.votedPlayer==null)
								vp.sendMessage(C.gold+"あなたは未投票です。"
										+"集会場の看板をクリックして、処刑までに投票を完了してください。");
					if(seconds==initSeconds/rnd && !vil.tryBiting
							&& vil.status==VillageStatus.ONGOING
							&& vil.day!=0 && vil.time==VillageTime.NIGHT
							&& vil.getAliveJinrouListExceptNpc().size()==0){
						List<VillagePlayer> bitableList = vil.getAlivePlayerListExceptJinrou();
						Collections.shuffle(bitableList);
						List<VillagePlayer> jinrouList = vil.getAliveJinrouList();
						Collections.shuffle(jinrouList);
						
						jinrouList.get(0).bitePlayer(bitableList.get(0));
					}
					seconds--;
				}else{
					for(VillagePlayer vp : vil.getPlayerListExceptNpc())
						BarAPI.removeBar(vp.getPlayer());
					vil.stopTimer();
					vil.afterTimer();
				}
			}
		}, 0, 20).getTaskId();
	}
	
	public void stopTimer(){
		if(timerId!=-1){
			Bukkit.getScheduler().cancelTask(timerId);
			timerId = -1;
			for(VillagePlayer vp : getPlayerListExceptNpc())
				BarAPI.removeBar(vp.getPlayer());
		}
	}
	
	public void stopDoTaskLater(){
		if(doTaskLaterId!=-1){
			Bukkit.getScheduler().cancelTask(doTaskLaterId);
			doTaskLaterId = -1;
		}
	}
	
	public void stopAsyncRebuild(){
		if(asyncRebuildId!=-1){
			Bukkit.getScheduler().cancelTask(asyncRebuildId);
			asyncRebuildId = -1;
		}
	}

	public void resetTimer(){
		stopTimer();
		switch(status){
		case PREPARING: setTimer("<準備中>  :募集開始まで ", 60); break;
		case RECRUITING: setTimer("<募集中>  ：終了まで ", 900); break;
		case FINISHING: setTimer("<終了中>  ：解散まで ", 300); break;
		}
	}
	
	public void afterTimer(){
		Village vil = (Village) this;
		
		if(status==VillageStatus.PREPARING)
			vil.gameRecruiting();
		else if(status==VillageStatus.RECRUITING)
			vil.gameFinishing();
		else if(status==VillageStatus.ONGOING && time==VillageTime.NOON)
			vil.totalizeVote();
		else if(status==VillageStatus.ONGOING && time==VillageTime.REVOTE)
			vil.totalizeVote();
		else if(status==VillageStatus.ONGOING && time==VillageTime.NIGHT)
			vil.checkResult();
		else if(status==VillageStatus.FINISHING)
			vil.rebuildVillage();
	}
	
}
