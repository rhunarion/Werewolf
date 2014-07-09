package jp.ddo.jinroumc.werewolf.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageTime;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.util.PluginChecker;
import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillagePlayer;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;
import jp.ddo.jinroumc.werewolf.worlddata.DefaultVillage;

import org.bukkit.entity.Player;

public class CommandMethod {
	public static boolean allowMakeVil = true;
	
	public static void enterVil(Player pl, String vilName){
		if(VillageUtil.isInVillage(pl)){
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			if(vp.alive && vil.status==VillageStatus.ongoing){
				pl.sendMessage(C.red+"Error: ゲーム進行中に他の村に行くことはできません。");
				return;
			}
		}
		if(!VillageUtil.isVillageName(vilName)){
			pl.sendMessage(C.red+"Error: "+vilName+" という名前の村ワールドは存在しません。");
			return;
		}
		Village vil = VillageUtil.getVillage(vilName);
		if(vil.isKickedPlayer(pl)){
			pl.sendMessage(C.red+"Error: この村ワールドから追い出されたため "+vil.title+" ("+vilName+") に入ることができません。");
			return;
		}
		if(vil==VillageUtil.getVillage(pl)){
			pl.sendMessage(C.red+"Error: すでに "+vil.title+" ("+vilName+") の中にいます。");
			return;
		}
		if(vil.setPassword){
			pl.sendMessage(C.red+"Error: "+vil.title+" ("+vilName+") にはパスワードが設定されています。");
			pl.sendMessage(C.red+"Error: "+C.yellow+"/"+PluginChecker.getWw()
								+"entervil "+vilName+" <password>"+C.red+" とコマンドして入村してください。");
			return;
		}
		
		pl.sendMessage(C.yellow+vil.title+C.gold+" ("+vilName+") に移動します。");
		VillageUtil.onPlayerLeave(pl);
		VillageUtil.teleportToVillage(pl, vil);
		VillageUtil.onPlayerEnter(pl, vil);
		if(vil.status!=VillageStatus.empty) showRule(pl);
	}
	
	public static void enterVil(Player pl, String vilName, String password){
		if(VillageUtil.isInVillage(pl)){
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			if(vp.alive && vil.status==VillageStatus.ongoing){
				pl.sendMessage(C.red+"Error: ゲーム進行中に他の村に行くことはできません。");
				return;
			}
		}
		if(!VillageUtil.isVillageName(vilName)){
			pl.sendMessage(C.red+"Error: "+vilName+" という名前の村ワールドは存在しません。");
			return;
		}
		Village vil = VillageUtil.getVillage(vilName);
		if(vil.isKickedPlayer(pl)){
			pl.sendMessage(C.red+"Error: この村ワールドから追い出されたため "+vil.title+" ("+vilName+") に入ることができません。");
			return;
		}
		if(vil==VillageUtil.getVillage(pl)){
			pl.sendMessage(C.red+"Error: すでに "+vil.title+" ("+vilName+") の中にいます。");
			return;
		}
		if(!vil.setPassword){
			pl.sendMessage(C.red+"Error: "+vil.title+" ("+vilName+") にはパスワードが設定されていません。");
			pl.sendMessage(C.red+"Error: "+C.yellow+"/"+PluginChecker.getWw()
								+"entervil "+vilName+C.red+" とコマンドして入村してください。");
			return;
		}
		if(!password.equals(vil.password)){
			pl.sendMessage(C.red+"Error: パスワードが違います。");
			return;
		}
		
		pl.sendMessage(C.yellow+vil.title+C.gold+" ("+vilName+") に移動します。");
		VillageUtil.onPlayerLeave(pl);
		VillageUtil.teleportToVillage(pl, vil);
		VillageUtil.onPlayerEnter(pl, vil);
		if(vil.status!=VillageStatus.empty) showRule(pl);
	}
	
	public static void leaveVil(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(vp.alive && vil.status==VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム進行中に村を出ることはできません。");
			return;
		}

		pl.sendMessage(C.yellow+vil.title+C.gold+" ("+vil.villageName+") から退出します。");
		VillageUtil.onPlayerLeave(pl);
		VillageUtil.teleportToLobby(pl);
	}
	
	public static void lobby(Player pl){
		if(VillageUtil.isInVillage(pl)){
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			if(vp.alive && vil.status==VillageStatus.ongoing){
				pl.sendMessage(C.red+"Error: ゲーム進行中に村を出ることはできません。");
				return;
			}
		}
		
		pl.sendMessage(C.gold+"ロビーに戻ります。");
		VillageUtil.onPlayerLeave(pl);
		VillageUtil.teleportToLobby(pl);
		return;
	}
	
	public static void showVil(Player pl){
		pl.sendMessage(C.gold+"////////// "+C.yellow
							+"Village List"+C.gold+" //////////");
		List<String> vilNameList = new ArrayList<String>();
		for(Village vil : VillageUtil.getVillageList())
			vilNameList.add(vil.villageName);
		Collections.sort(vilNameList, String.CASE_INSENSITIVE_ORDER);
		String status = "";
		for(String vilName : vilNameList){
			Village vil = VillageUtil.getVillage(vilName);
			switch(vil.status){
			case empty: status = C.gold+"空き状態"; break;
			case preparing: status = C.yellow+"準備中"; break;
			case recruiting: status = C.aqua+"参加者募集中"; break;
			case ongoing:
				status = C.yellow+""+vil.day+"日目";
				switch(vil.time){
				case day: status += "昼"; break;
				case execution: status += "夕方"; break;
				case night: status += "夜"; break;
				}
				break;
			case finishing: status = C.yellow+"ゲーム終了中"; break;
			}
			pl.sendMessage(C.gold+" - "+C.yellow+vil.title+C.gold
							+" ("+vil.villageName+") は現在 "+status+C.gold+" です。");
		}
	}
	
	public static void showPlayers(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		
		pl.sendMessage(C.gold+"////////// "+C.yellow
				+"Player List"+C.gold+" //////////");
		Village vil = VillageUtil.getVillage(pl);
		List<String> pnList = vil.getPlayerNameListExceptNpc();
		Collections.sort(pnList, String.CASE_INSENSITIVE_ORDER);
		String message = C.gold+"Mr.Firvic";
		if(vil.getPlayer("Mr.Firvic").role!=VillageRole.none
				&& (vil.status==VillageStatus.preparing
				|| vil.status==VillageStatus.recruiting))
			message += " (配役済み)";
		
		for(String pn : pnList){
			message += ", ";
			message += pn;
			VillagePlayer vp = vil.getPlayer(pn);
			if(vil.status==VillageStatus.preparing || vil.status==VillageStatus.recruiting){
				if(vp.role!=VillageRole.none){
					if(vp.gameMaster) message += " (GM, 配役済み)";
					else message += " (配役済み)";	
				}else{
					if(vp.gameMaster) message += " (GM)";
				}
			}else{
				if(vp.gameMaster) message += " (GM)";
			}
		}
		pl.sendMessage(message);
		return;
	}
	
	public static void makeVil(Player pl, String vilType){
		if(!allowMakeVil){
			pl.sendMessage(C.red+"Error: 現在、人狼ゲームの準備開始が禁止されています。");
			return;
		}
		if(!VillageUtil.isVillageType(vilType) && !(vilType=="")){
			pl.sendMessage(C.red+"Error: "+vilType+" という村ワールドのタイプは存在しません。");
			return;
		}
		if(VillageUtil.isInVillage(pl)){
			Village vil = VillageUtil.getVillage(pl);
			if(vil.villageType.equalsIgnoreCase(vilType) || vilType==""){
				if(vil.status!=VillageStatus.empty){
					pl.sendMessage(C.red+"Error: "+vil.villageName+" ではすでに人狼ゲームが開始されています。");
					return;
				}
				VillagePlayer vp = vil.getPlayer(pl);
				pl.sendMessage(C.yellow+vil.title+C.gold+" ("+vil.villageName+") で人狼ゲームの準備を開始します。");
				vp.setGm();
				vp.joinGame();
				vil.gamePreparing();
				return;
			}
		}
		for(Village vil : VillageUtil.getVillageList()){
			if(vil.status==VillageStatus.empty && (vil.villageType.equalsIgnoreCase(vilType) || vilType=="")){
				VillageUtil.onPlayerLeave(pl);
				VillageUtil.teleportToVillage(pl, vil);
				VillageUtil.onPlayerEnter(pl, vil);
				pl.sendMessage(C.yellow+vil.title+C.gold+" ("+vil.villageName+") で人狼ゲームの準備を開始します。");
				VillagePlayer vp = vil.getPlayer(pl);
				vp.setGm();
				vp.joinGame();
				vil.gamePreparing();
				return;
			}
		}
		pl.sendMessage(C.red+"Error: 空きがないため人狼ゲームの準備を開始できません。");
	}
	
	public static void makeVil(Player pl){
		makeVil(pl, "");
	}
	
	public static void startRec(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.preparing){
			pl.sendMessage(C.red+"Error: 人狼ゲームの状態が準備中のときしか使えないコマンドです。");
			return;
		}
		
		vil.gameRecruiting();
	}
	
	public static void stopRec(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.recruiting){
			pl.sendMessage(C.red+"Error: 人狼ゲームの状態が募集中のときしか使えないコマンドです。");
			return;
		}
		
		vil.gamePreparing();
	}
	
	public static void resetTimer(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.preparing && vil.status!=VillageStatus.recruiting
				&& vil.status!=VillageStatus.finishing){
			pl.sendMessage(C.red+"Error: ゲーム進行中には使えないコマンドです。");
			return;
		}
		
		vil.sendToVillage(C.gold+"ゲームマスターがタイマーをリセットしました。");
		vil.resetTimer();
	}
	
	public static void giveGm(Player pl, String target){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(!vil.isPlayerExceptGmAndNpc(target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}
		
		VillagePlayer tarvp = vil.getPlayer(target);
		vp.removeGm();
		tarvp.setGm();
	}
	
	public static void configRoom(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vp.alive && vil.status==VillageStatus.ongoing && vil.time==VillageTime.night){
			pl.sendMessage(C.red+"Error: 昼にしか使えないコマンドです。");
			return;
		}

		pl.sendMessage(C.gold+"コンフィグルームに移動します。");
		vp.teleportToConfigRoom();
	}
	
	public static void showRule(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		vp.showRule();
	}
	
	public static void shoutRule(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		
		vil.shoutRule();
	}
	
	public static void join(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(vil.status!=VillageStatus.recruiting){
			pl.sendMessage(C.red+"Error: 現在 "+C.yellow+vil.title+C.red
					+" ("+vil.villageName+") は参加者を募集していません。");
			return;
		}
		if(vp.joining){
			pl.sendMessage(C.red+"Error: すでにゲームに参加しています");
			return;
		}
		if(vil.getJoiningPlayerNum()>=vil.maxNum){
			pl.sendMessage(C.red+"Error: 定員に達しているため参加できません。");
			return;
		}

		vp.joinGame();
	}
	
	public static void unjoin(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.joining){
			pl.sendMessage(C.red+"Error: ゲームに参加中ではありません。");
			return;
		}
		if(vil.status!=VillageStatus.preparing && vil.status!=VillageStatus.recruiting){
			pl.sendMessage(C.red+"Error: ゲーム中は参加を辞めることができません。");
			return;
		}

		vp.unjoinGame();
	}
	
	public static void setRole(Player pl, String target, String role){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.preparing && vil.status!=VillageStatus.recruiting){
			pl.sendMessage(C.red+"Error: 人狼ゲームの状態が準備中か募集中のときしか使えないコマンドです。");
			return;
		}
		if(!VillageUtil.isVillageRoleExceptNone(role)){
			pl.sendMessage(C.red+"Error: "+role+" という配役は指定できません。");
			return;
		}
		if(!vil.isJoiningPlayer(target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}
		VillageRole vr = VillageUtil.getVillageRoleByString(role);
		if(target.equalsIgnoreCase("Mr.Firvic")
			&& (vr==VillageRole.jinrou || vr==VillageRole.kyoujin || vr==VillageRole.youko)){
			pl.sendMessage(C.red+"Error: Mr.Firvic に "
							+VillageUtil.getVillageRoleInJapanese(vr)+C.red+" をセットすることはできません。");
			return;				
		}
		if(vil.getSettedRoleNum(vr) >= vil.getRoleNumInRule(vr)){
			pl.sendMessage(C.red+"Error: すでに "+VillageUtil.getVillageRoleInJapanese(vr)+" は定員に達しています。");
			return;
		}

		VillagePlayer tarvp = vil.getPlayer(target);		
		tarvp.setRole(vr);
		vil.sendToVillage(C.yellow+target
								+C.gold+" さんに何かの配役がセットされました。");
	}
	
	public static void unsetRole(Player pl, String target){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.preparing && vil.status!=VillageStatus.recruiting){
			pl.sendMessage(C.red+"Error: 人狼ゲームの状態が準備中か募集中のときしか使えないコマンドです。");
			return;
		}
		if(!vil.isJoiningPlayer(target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}

		VillagePlayer tarvp = vil.getPlayer(target);
		tarvp.unsetRole();
		vil.sendToVillage(C.yellow+target
				+C.gold+" さんにセットされていた配役が解除されました。");
	}
	
	public static void requestRole(Player pl, String role){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		if(!vil.requestRole){
			pl.sendMessage(C.red+"Error: ルールで許可されていないため、配役のリクエストはできません。");
			return;
		}
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.joining){
			pl.sendMessage(C.red+"Error: ゲームに参加しているときしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.preparing && vil.status!=VillageStatus.recruiting){
			pl.sendMessage(C.red+"Error: 人狼ゲームの状態が準備中か募集中のときしか使えないコマンドです。");
			return;
		}
		if(!VillageUtil.isVillageRole(role)){
			pl.sendMessage(C.red+"Error: "+role+" という配役は指定できません。");
			return;
		}
		VillageRole vr = VillageUtil.getVillageRoleByString(role);
		vp.setRequestedRole(vr);
		if(vr==VillageRole.none)
			pl.sendMessage(C.gold+"配役の "+C.yellow+"希望なし"+C.gold+" に設定されました。");
		else
			pl.sendMessage(VillageUtil.getVillageRoleInJapanese(vr)+C.gold+" の配役を希望しました。");
	}
	
	public static void startGame(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.preparing && vil.status!=VillageStatus.recruiting){
			pl.sendMessage(C.red+"Error: 人狼ゲームの状態が準備中か募集中のときしか使えないコマンドです。");
			return;
		}
		if(vil.getTotalRoleNumInRuleExceptMurabito() > vil.getJoiningPlayerNum()
				|| vil.jinrouNum*2+vil.youkoNum+2 > vil.getJoiningPlayerNum()){
			pl.sendMessage(C.red+"Error: 参加人数が足りません。配役の数を減らしてください。");
			return;
		}
		if(	vil.uranaiNum+vil.reibaiNum+vil.kariudoNum == 
				vil.getSettedRoleNum(VillageRole.uranaishi)
				+vil.getSettedRoleNum(VillageRole.reibaishi)
				+vil.getSettedRoleNum(VillageRole.kariudo)
				&& vil.getJoiningPlayerNum()-vil.getTotalRoleNumInRuleExceptMurabito()
				<= vil.getSettedRoleNum(VillageRole.murabito)){
			pl.sendMessage(C.red+"Error: 第一犠牲者が人間以外になってしまいます。人間の配役を解除してください。");
			return;
		}
		
		vil.stopTimer();
		vil.sendToVillage(C.gold+"ゲームマスターがスタートを宣言しました。");
		vil.gameStarting();
	}
	
	public static void finishGame(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム進行中にしか使えないコマンドです。");
			return;
		}

		vil.sendToVillage(C.gold+"ゲームマスターがゲームを強制終了しました。");
		vil.gameFinishing();
	}
	
	public static void skip(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		final Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.ongoing && vil.status!=VillageStatus.finishing){
			pl.sendMessage(C.red+"Error: ゲーム進行中かゲーム終了中にしか使えないコマンドです。");
			return;
		}

		vil.sendToVillage(C.gold+"ゲームマスターがゲームをスキップしました。");
		if(vil.status==VillageStatus.ongoing && vil.time==VillageTime.execution){
			vil.stopDoTaskLater();
			vil.executedPlayer.kill();
			
			((DefaultVillage) vil).postExecution();
			vil.checkResult();
			return;
		}
		vil.stopTimer();
		vil.afterTimer();
	}
	
	public static void kick(Player pl, String target){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(!vil.isPlayerExceptGmAndNpc(target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}

		VillagePlayer tarvp = vil.getPlayer(target);
		if(tarvp.alive && vil.status==VillageStatus.ongoing)
			vil.sendToVillage(C.gold+"ゲームマスターが "+tarvp.color+target
					+C.gold+" さんをキックしました。");
		else
			vil.sendToVillage(C.gold+"ゲームマスターが "+C.yellow+target
					+C.gold+" さんをキックしました。");
		tarvp.kick();
	}
	
	public static void unkick(Player pl, String target){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(!vil.isKickedPlayer(target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}

		vil.unkick(target);
		vil.sendToVillage(C.gold+"ゲームマスターが "+C.yellow+target
					+C.gold+" さんのキックを解除しました。");
	}
	
	public static void kill(Player pl, String target){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム進行中にしか使えないコマンドです。");
			return;
		}
		if(!vil.isAlivePlayer(target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}

		VillagePlayer tarvp = vil.getPlayer(target);
		tarvp.kill();
		tarvp.giveDeathDamage();
		tarvp.strikeThroughPlayerName();
		vil.sendToVillage(C.gold+"ゲームマスターが "+tarvp.color+target
						+C.gold+" さんを強制的に殺しました。");
	}
	
	public static void home(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(vil.status==VillageStatus.ongoing && vil.time==VillageTime.execution
				&& vil.executedPlayer==vp && vp.alive){
			pl.sendMessage(C.red+"Error: 処刑者は使えないコマンドです。");
			return;
		}
		
		
		vp.teleportToHome();
		if(vp.alive && vil.status==VillageStatus.ongoing)
			pl.sendMessage(C.gold+"自分の家に移動します。");
		else
			pl.sendMessage(C.gold+"村の入り口に移動します。");
	}
	
	public static void whisp(Player pl, String target, String message){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.alive || vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム参加中にしか使えないコマンドです。");
			return;
		}
		if(vil.time==VillageTime.night){
			pl.sendMessage(C.red+"Error: 夜には使えないコマンドです。");
			return;
		}
		if(!vil.permitWhisp){
			pl.sendMessage(C.red+"Error: ルールで許可されていないため、そのコマンドは使えません。");
			return;
		}
		if(!vil.isAlivePlayerExceptMyselfAndNpc(pl, target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}

		VillagePlayer tarvp = vil.getPlayer(target);
		vp.sendMessageToSpecifiedPlayer(tarvp, message);
	}
	
	public static void shout(Player pl, String message){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.gameMaster){
			pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
			return;
		}
		if(vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム進行中にしか使えないコマンドです。");
			return;
		}
		
		vp.sendMessageWithShout(message);
	}
	
	public static void co(Player pl, String message){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.alive || vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム参加中にしか使えないコマンドです。");
			return;
		}
		if(vil.time==VillageTime.night){
			pl.sendMessage(C.red+"Error: 夜には使えないコマンドです。");
			return;
		}
		
		vp.sendMessageWithCO(message);
	}
	
	public static void myRole(Player pl){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.joining || vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム参加中にしか使えないコマンドです。");
			return;
		}

		vp.showMyRole();
	}
	
	public static void vote(Player pl, String target){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.alive || vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム参加中にしか使えないコマンドです。");
			return;
		}
		if(vil.time!=VillageTime.day && vil.time!=VillageTime.revote){
			pl.sendMessage(C.red+"Error: 昼にしか使えないコマンドです。");
			return;
		}
		if(!vil.isAlivePlayerExceptMyself(pl, target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}

		VillagePlayer tarvp = vil.getPlayer(target);
		vp.votePlayer(tarvp);
		pl.sendMessage(tarvp.color+target+C.gold+" さんに投票しました。");
	}
	
	public static void uranai(Player pl, String target){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.alive || vp.role!=VillageRole.uranaishi || vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム参加中の占い師にしか使えないコマンドです。");
			return;
		}
		if(vil.time!=VillageTime.night){
			pl.sendMessage(C.red+"Error: 夜にしか使えないコマンドです。");
			return;
		}
		if(vp.tryUranai){
			pl.sendMessage(C.red+"Error: 今夜はすでに一人占っています。");
			return;
		}
		if(!vil.isAlivePlayerExceptMyselfAddedBittenPlayer(pl, target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}
	
		VillagePlayer tarvp = vil.getPlayer(target);
		vp.uranaiPlayer(tarvp);
	}
	
	public static void guard(Player pl, String target){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.alive || vp.role!=VillageRole.kariudo || vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム参加中の狩人にしか使えないコマンドです。");
			return;
		}
		if(vil.time==VillageTime.night){
			pl.sendMessage(C.red+"Error: 夜には使えないコマンドです。");
			return;
		}
		if(!vil.isAlivePlayerExceptMyself(pl, target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}

		VillagePlayer tarvp = vil.getPlayer(target);
		vp.guardPlayer(tarvp);
		pl.sendMessage(tarvp.color+target+C.gold+" さんを人狼から護衛します。");
	}
	
	public static void bite(Player pl, String target){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}
		Village vil = VillageUtil.getVillage(pl);
		VillagePlayer vp = vil.getPlayer(pl);
		if(!vp.alive || vp.role!=VillageRole.jinrou || vil.status!=VillageStatus.ongoing){
			pl.sendMessage(C.red+"Error: ゲーム参加中の人狼にしか使えないコマンドです。");
			return;
		}
		if(vil.time!=VillageTime.night){
			pl.sendMessage(C.red+"Error: 夜にしか使えないコマンドです。");
			return;
		}
		if(!vil.permitBite){
			pl.sendMessage(C.red+"Error: ルールで許可されていないため、そのコマンドは使えません。");
			return;
		}
		if(vil.tryBiting){
			pl.sendMessage(C.red+"Error: 今夜はすでに一人噛んでいます。");
			return;
		}
		if(!vil.isAlivePlayerExceptJinrou(target)){
			pl.sendMessage(C.red+"Error: "+target+" というプレイヤーは指定できません。");
			return;
		}
		if(vil.day==0 && !target.equalsIgnoreCase("Mr.Firvic")){
			pl.sendMessage(C.red+"Error: 0日目の夜は Mr.FirstVictm しか噛み殺すことができません。");
			return;
		}

		VillagePlayer tarvp = vil.getPlayer(target);
		vp.bitePlayer(tarvp);
	}
}
