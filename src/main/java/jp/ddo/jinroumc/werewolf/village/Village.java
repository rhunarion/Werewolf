package jp.ddo.jinroumc.werewolf.village;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageResult;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageTime;
import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.util.PluginChecker;
import jp.ddo.jinroumc.werewolf.util.SendMessage;
import jp.ddo.jinroumc.werewolf.worlddata.DefaultVillageData;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Village extends VillageTimer {
	
	Village(String villageName, String villageType, JavaPlugin plugin){
		super(villageName, villageType, plugin);
	}
	
	private void assignRole(){
		List<VillagePlayer> vpList = getJoiningPlayerListExceptNPC();
		Collections.shuffle(vpList);
		VillagePlayer fv = getPlayer("Mr.Firvic");

		if(fv.role==VillageRole.none){
			List<VillageRole> remainder = new ArrayList<VillageRole>();
			for(int i=0; i<getRoleNumInRule(VillageRole.murabito)
					- getSettedRoleNum(VillageRole.murabito); i++)
				remainder.add(VillageRole.murabito);
			for(int i=0; i<getRoleNumInRule(VillageRole.uranaishi)
					- getSettedRoleNum(VillageRole.uranaishi); i++)
				remainder.add(VillageRole.uranaishi);
			for(int i=0; i<getRoleNumInRule(VillageRole.reibaishi)
					- getSettedRoleNum(VillageRole.reibaishi); i++)
				remainder.add(VillageRole.reibaishi);
			for(int i=0; i<getRoleNumInRule(VillageRole.kariudo)
					- getSettedRoleNum(VillageRole.kariudo); i++)
				remainder.add(VillageRole.kariudo);
			
			Collections.shuffle(remainder);
			fv.role = remainder.get(0);
		}
		for(VillageRole vr : VillageRole.values()){
			if(vr==VillageRole.none) continue;
			if(vr==VillageRole.murabito) continue;
			int rlNum = getRoleNumInRule(vr) - getSettedRoleNum(vr);
			if(requestRole && rlNum>0){
				for(VillagePlayer vp : vpList){
					if(vp.roleRequested==vr && vp.role==VillageRole.none){
						vp.role = vr;
						rlNum--;
						if(rlNum==0) break;
					}
				}
			}
		}
		for(VillageRole vr : VillageRole.values()){
			if(vr==VillageRole.none) continue;
			if(vr==VillageRole.murabito) continue;
			int rlNum = getRoleNumInRule(vr) - getSettedRoleNum(vr);
			if(rlNum>0){
				for(VillagePlayer vp : vpList){
					if(vp.role==VillageRole.none){
						vp.role = vr;
						rlNum--;
						if(rlNum==0) break;
					}
				}
			}
		}
		for(VillagePlayer vp : vpList)
			if(vp.role==VillageRole.none)
				vp.role = VillageRole.murabito;
	}

	private void assignColor(){
		List<VillagePlayer> vpList = getJoiningPlayerListExceptNPC();
		
		List<ChatColor> clList = new ArrayList<ChatColor>();
		clList.add(ChatColor.AQUA);
		clList.add(ChatColor.BLACK);
		clList.add(ChatColor.BLUE);
		clList.add(ChatColor.DARK_AQUA);
		clList.add(ChatColor.DARK_BLUE);
		clList.add(ChatColor.DARK_GRAY);
		clList.add(ChatColor.DARK_GREEN);
		clList.add(ChatColor.DARK_PURPLE);
		clList.add(ChatColor.DARK_RED);
		clList.add(ChatColor.GOLD);
		clList.add(ChatColor.GRAY);
		clList.add(ChatColor.GREEN);
		clList.add(ChatColor.LIGHT_PURPLE);
		clList.add(ChatColor.RED);
		clList.add(ChatColor.YELLOW);
		Collections.shuffle(clList);

		for(int i=0; i<vpList.size(); i++)
			vpList.get(i).color = clList.get(i);
	}
	
	private void setScoreboard(){
		for(VillagePlayer vp : getJoiningPlayerList()){
			scoreboard.resetScores(vp.player);
		}
		for(VillagePlayer vp : getJoiningPlayerList()){
			objective.getScore(Bukkit.getOfflinePlayer(vp.getColorName())).setScore(1);
			objective.getScore(Bukkit.getOfflinePlayer(vp.getColorName())).setScore(0);
		}
	}
	
	private void rewriteScoreboard(){
		for(VillagePlayer vp : getDeadPlayerList()){
			if(scoreboard.getPlayers().contains(Bukkit.getOfflinePlayer(vp.getColorName()))){
				vp.strikeThroughPlayerName();
			}
		}
	}

	public void gamePreparing(){
		if(status==VillageStatus.empty)
			title = "みんなの人狼村";
		status = VillageStatus.preparing;
		
		setTimer("<準備中>  :募集開始まで ", 60);
		sendToVillage(C.yellow+title
				+C.gold+" ("+villageName+") は現在準備中です。");
	}
	
	public void gameRecruiting(){
		status = VillageStatus.recruiting;
		
		setTimer("<募集中>  ：終了まで ", 900);
		SendMessage.sendToServer(C.yellow+title+C.gold
				+" ("+villageName+") は参加者の募集を開始しました。");
	}
	
	public void gameStarting(){
		status = VillageStatus.ongoing;
		maxNum = getJoiningPlayerNum();
		assignRole();
		assignColor();
		setScoreboard();
		stopAsyncRebuild();
		DefaultVillageData.writeSign(this);
		for(VillagePlayer vp : getJoiningPlayerList())
			vp.alive = true;
		
		sendToVillage(C.green+"/////////////////////////////////");
		sendToVillage(C.green+"/////      "+C.aqua
												+"ゲーム開始"+C.green+"      /////");
		sendToVillage(C.green+"/////////////////////////////////");
		sendToVillage(C.green+"平和だった村の様子がおかしい…。今夜は何か起きそうな気がする…。");
		for(VillagePlayer vp : getAlivePlayerListExceptNPC())
			vp.showMyRole();
		for(VillagePlayer vp : getGhostPlayerList())
			vp.addGhostTeam();

		nightTime();
	}
	
	public void nightTime(){
		setTimer("<"+day+"日目夜>  ："+(day+1)+"日目朝まで ", nightTime);
		Bukkit.getWorld(villageName).setTime(18000);
		
		DefaultVillageData.changeHouseEffect(this);
		for(VillagePlayer vp : getGhostPlayerList())
			vp.addInvisibleEffect();
		for(VillagePlayer vp : getAlivePlayerList())
			vp.teleportToHome();
		for(VillagePlayer vp : getAliveNPCList())
			vp.setFenceAroundBed();
		
		if(day!=0){
			sendToVillage(C.green+"////////// " 
							+C.aqua+""+day+"日目夜"+C.green+" になりました。 //////////");
			for(VillagePlayer vp : getAlivePlayerListExceptNPC()){
				if(vp.role==VillageRole.uranaishi){
					vp.sendMessage(C.yellow+"/"+PluginChecker.getWw()+"uranai <player>"
							+C.gold+" とコマンドすることで夜に指定したプレイヤーを占うことができます。");
				}
				else if(vp.role==VillageRole.reibaishi){
					vp.sendMessage(C.green+"死者と会話し "
							+executedPlayer.color+executedPlayer.getName()+C.green+" さんの正体は "
							+VillageUtil.getTrueRole(executedPlayer)+C.green+" だとわかりました。");
				}
				else if(vp.role==VillageRole.kariudo){
					if(vp.guardPlayer==null)
						vp.sendMessage(C.gold+"今夜は誰も護衛しません。");
					else
						vp.sendMessage(vp.guardPlayer.color+vp.guardPlayer.getName()
								+C.gold+" さんを人狼から護衛します。");
				}
				else if(vp.role==VillageRole.jinrou){
					if(permitBite)
						vp.sendMessage(C.yellow+"/"+PluginChecker.getWw()+"bite <player>"
									+C.gold+" とコマンドすることで夜に村人を一人噛み殺すことができます。");
					else
						vp.sendMessage(C.gold+"夜に村人を一人噛み殺すことができます。");
				}
			}
		}
	}

	public void dayTime(){
		setTimer("<"+day+"日目昼>  ：処刑まで ", dayTime);
		Bukkit.getWorld(villageName).setTime(6000);

		DefaultVillageData.changeHouseEffect(this);
		for(VillagePlayer vp : getGhostPlayerList())
			vp.addInvisibleEffect();
		for(VillagePlayer vp : getAlivePlayerList())
			vp.teleportToHome();
		for(VillagePlayer vp : getAliveNPCList())
			vp.removeFenceAroundBed();
		
		sendToVillage(C.green+"////////// " 
								+C.aqua+""+day+"日目朝"+C.green+" になりました。 //////////");
		if(bittenPlayer==null && cursedPlayerList.size()==0){
			sendToVillage(C.green+"特に変わったことは起こっていないようです。");
		}else{
			if(bittenPlayer!=null)
				sendToVillage(bittenPlayer.color+bittenPlayer.getName()
						+C.green+" さんが無残な姿で発見されました。");
			if(cursedPlayerList.size()>0)
				for(VillagePlayer cursed : cursedPlayerList)
					sendToVillage(cursed.color+cursed.getName()
							+C.green+" さんが誰かに呪い殺されました。");
		}
		
		for(VillagePlayer vp : getAlivePlayerListExceptNPC()){
			if(vp.role==VillageRole.reibaishi){
				if(reishiAllPlayers){
					if(bittenPlayer!=null)
						vp.sendMessage(C.green+"死者と会話し "
								+bittenPlayer.color+bittenPlayer.getName()+C.green+" さんの正体は "
								+VillageUtil.getTrueRole(bittenPlayer)+C.green+" だとわかりました。");
					if(cursedPlayerList.size()>0)
						for(VillagePlayer cursed : cursedPlayerList)
							vp.sendMessage(C.green+"死者と会話し "
									+cursed.color+cursed.getName()+C.green+" さんの正体は"
									+VillageUtil.getTrueRole(cursed)+C.green+" だとわかりました。");
				}
			}
			else if(vp.role==VillageRole.kariudo){
				vp.sendMessage(C.yellow+"/"+PluginChecker.getWw()+"guard <player>"
						+C.gold+" とコマンドすることで指定したプレイヤーを一人だけ人狼から護衛することができます。");
			}
		}
		sendToVillage(C.gold+"集会場の看板をクリックすることで、今日処刑するプレイヤーの投票を行うことができます。");
	}
	
	public void totalizeVote(){
		sendToVillage(C.aqua+"処刑時刻"+C.green+" になりました。");
		Bukkit.getWorld(villageName).setTime(12500);
		
		Random rnd = new Random();
		List<VillagePlayer> aliveList = getAlivePlayerList();
		for(VillagePlayer vp : aliveList){
			if(vp.votedPlayer!=null){
				vp.votedPlayer.numBeingVoted++;
				sendToVillage(vp.color+vp.getName()+C.gold+" さんは "
						+vp.votedPlayer.color+vp.votedPlayer.getName()+C.gold+" さんに投票しました。");
			}else{
				if(randomVote){
					while(true){
						VillagePlayer voted = aliveList.get(rnd.nextInt(aliveList.size()));
						if(voted!=vp){
							voted.numBeingVoted++;
							sendToVillage(vp.color+vp.getName()+C.gold+" さんは "
									+voted.color+voted.getName()+C.gold+" さんに投票しました。(Random)");
							break;
						}
					}
				}else{
					sendToVillage(vp.color+vp.getName()+C.gold
							+" さんは誰にも投票しませんでした。");
				}
			}
		}

		sendToVillage(C.green+"////////// "
						+C.aqua+"投票結果"+C.green+" //////////");
		List<VillagePlayer> votedPlayerList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : aliveList){
			if(vp.numBeingVoted>0){
				votedPlayerList.add(vp);
				sendToVillage(vp.color+vp.getName()+C.gold+" さんは "
						+C.yellow+vp.numBeingVoted+"票"+C.gold+" 投票されました。");
			}
		}

		VillagePlayer maxVotedPlayer = null;
		if(votedPlayerList.size()>0){
			maxVotedPlayer = votedPlayerList.get(0);
			for(int i=1; i<votedPlayerList.size(); i++)
				if(maxVotedPlayer.numBeingVoted < votedPlayerList.get(i).numBeingVoted)
					maxVotedPlayer = votedPlayerList.get(i);
		}else{
			revote();
			return;
		}

		for(VillagePlayer vp : votedPlayerList){
			if(maxVotedPlayer == vp)
				continue;
			if(maxVotedPlayer.numBeingVoted == vp.numBeingVoted){
				if(revoteNum==currentVoteNum){
					gameFinishing();
					return;
				}
				revote();
				return;
			}
		}

		time = VillageTime.execution;
		sendToVillage(C.green+"全員の投票の結果 "+maxVotedPlayer.color
				+maxVotedPlayer.getName()+C.green+" さんを処刑することに決まりました。");
		executedPlayer = maxVotedPlayer;
		maxVotedPlayer.teleportToScaffold();
		DefaultVillageData.preExecution(this);
		
		final Village vil = this;
		final VillagePlayer playerToBeExecuted = maxVotedPlayer;
		doTaskLaterID = Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable(){
			@Override
			public void run(){
				vil.execution(playerToBeExecuted);
				vil.doTaskLaterID = -1;
			}
		}, 100).getTaskId();
	}
	
	public void execution(VillagePlayer playerToBeExecuted){
		for(VillagePlayer vp : getGhostPlayerList())
			vp.addInvisibleEffect();
		
		DefaultVillageData.execution(this);
	}
	
	public void revote(){
		setTimer("<"+day+"日目再投票>  ：処刑まで ", 30);

		time = VillageTime.revote;
		currentVoteNum++;
		for(VillagePlayer vp : getGhostPlayerList())
			vp.addInvisibleEffect();
		for(VillagePlayer vp : getAlivePlayerList())
			vp.numBeingVoted = 0;


		if(revoteNum==currentVoteNum)
			sendToVillage(C.green+"最後の再投票になりました。" +
					"投票先を変える方は集会場の看板をクリックしてください。");
		else
			sendToVillage(C.green+"再投票になりました。" +
					"投票先を変える方は集会場の看板をクリックしてください。");
	}
	
	public void checkResult(){
		if(day==0 && !tryBiting){
			VillagePlayer fv = getPlayer("Mr.Firvic");
			fv.kill();
			fv.giveDeathDamage();
			bittenPlayer = fv;
		}
		if(cursedPlayerList.size()>0){
			for(VillagePlayer cursed : cursedPlayerList){
				cursed.kill();
				cursed.giveDeathDamage();
				if(cursed.connection)
					cursed.sendMessage(C.gold+"あなたは占い師に呪い殺されました。");
			}
		}
		rewriteScoreboard();
		
		int human = 0;
		int jinrou = 0;
		int youko = 0;
		for(VillagePlayer vp : getAlivePlayerList()){
			if(vp.role!=VillageRole.jinrou && vp.role!=VillageRole.youko) human++;
			if(vp.role==VillageRole.jinrou) jinrou++;
			if(vp.role==VillageRole.youko) youko++;
		}
		if(jinrou==0){
			if(youko>0) result = VillageResult.youko;
			else result = VillageResult.murabito;
			gameFinishing();
			return;
		}else if(jinrou>=human){
			if(youko>0) result = VillageResult.youko;
			else result = VillageResult.jinrou;
			gameFinishing();
			return;
		}

		if(time==VillageTime.execution){
			time = VillageTime.night;
			bittenPlayer = null;
			cursedPlayerList = new ArrayList<VillagePlayer>();
			currentVoteNum = 0;
			for(VillagePlayer vp : getAlivePlayerList()){
				vp.numBeingVoted = 0;
				vp.votedPlayer = null;
			}
			nightTime();
		}else if(time==VillageTime.night){
			time = VillageTime.day;
			day++;
			executedPlayer = null;
			for(VillagePlayer vp : getKariudoList())
				vp.guardPlayer = null;
			for(VillagePlayer vp : getUranaishiList())
				vp.tryUranai = false;
			tryBiting = false;
			dayTime();
		}
	}
	
	public void gameFinishing(){
		Bukkit.getWorld(villageName).setTime(0);
		stopDoTaskLater();
		setTimer("<終了中>  ：解散まで ", 300);

		if(status==VillageStatus.ongoing){
			String winner = "";
			switch(result){
			case draw: winner = "      引き分け！！       "; break;
			case murabito: winner = C.aqua+"  村人チームの勝利！！  "; break;
			case jinrou: winner = C.d_red+"  人狼チームの勝利！！  "; break;
			case youko: winner = C.yellow+"  妖狐チームの勝利！！  "; break;
			}
			sendToVillage(C.green+"/////////////////////////////////");
			sendToVillage(C.green+"/////"+winner+C.green+"/////");
			sendToVillage(C.green+"/////////////////////////////////");
			for(VillagePlayer vp : getJoiningPlayerList()){
				sendToVillage(vp.color+vp.getName()+C.gold+" さんは "
						+VillageUtil.getVillageRoleInJapanese(vp.role)+C.gold+" でした。");
			}
		}
		DefaultVillageData.finishFirework(this);
		status = VillageStatus.finishing;
		for(VillagePlayer vp : getPlayerListExceptNPC())
			vp.changeStatusOnGameFinish();
		for(VillagePlayer vp : getJoiningPlayerList())
			vp.joining = false;
	}
	
	public void rebuildVillage(){
		sendToVillage(C.gold+"お疲れ様でした。この村は再生成されます。");
		stopTimer();
		stopDoTaskLater();
		for(VillagePlayer vp : getPlayerListExceptNPC()){
			Player pl = vp.getPlayer();
			VillageUtil.teleportToLobby(pl);
			VillageUtil.onPlayerLeave(pl);
		}
		
		VillageUtil.getVillageList().add(new Village(villageName, villageType, plugin));
		Iterator<Village> itr = VillageUtil.getVillageList().iterator(); 
		while(itr.hasNext()){
			if(itr.next()==this){
				itr.remove();
				break;
			}
		}
		
		for(Entity entity : Bukkit.getWorld(villageName).getEntities())
			if(!(entity instanceof Player))
				entity.remove();
		Village newVil = VillageUtil.getVillage(villageName);
		newVil.getPlayer("Mr.Firvic").spawnVillager();
		DefaultVillageData.eraseSign(newVil);
		DefaultVillageData.copyOriginal(newVil);
	}
}
