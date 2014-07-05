package jp.ddo.jinroumc.werewolf.village;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageResult;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageTime;
import jp.ddo.jinroumc.werewolf.util.C;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class VillageCore {
	public String villageName;
	public String villageType;
	public VillageStatus status;
	public VillageResult result;
	public JavaPlugin plugin;
	public int timerID;
	public int doTaskLaterID;
	public int asyncRebuildID;
	public List<VillagePlayer> playerList;
	public List<Player> kickedPlayerList;

	public int day;
	public VillageTime time;
	public Scoreboard scoreboard;
	public Objective objective;
	public Team ghostTeam;
	public boolean tryBiting;
	public VillagePlayer executedPlayer;
	public VillagePlayer bittenPlayer;
	public List<VillagePlayer> cursedPlayerList;
	public int currentVoteNum;
	
	public int maxNum;
	public int uranaiNum;
	public int reibaiNum;
	public int kariudoNum;
	public int jinrouNum;
	public int kyoujinNum;
	public int youkoNum;
	public String title;
	public String description;
	public boolean setPassword;
	public String password;
	public int dayTime;
	public int nightTime;
	public boolean randomVote;
	public int revoteNum;
	public boolean requestRole;
	public boolean permitWhisp;
	public boolean reishiAllPlayers;
	public boolean permitBite;
	
	VillageCore(String villageName, String villageType, JavaPlugin plugin){
		this.villageName = villageName;
		this.villageType = villageType;
		this.status = VillageStatus.empty;
		this.result = VillageResult.draw;
		this.plugin = plugin;
		this.timerID = -1;
		this.doTaskLaterID = -1;
		this.asyncRebuildID = -1;
		this.playerList = new ArrayList<VillagePlayer>();
		this.kickedPlayerList = new ArrayList<Player>();

		this.day = 0;
		this.time = VillageTime.night;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = this.scoreboard.registerNewObjective("Player Status", "dummy");
		this.objective.setDisplayName("Player Status");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.ghostTeam = this.scoreboard.registerNewTeam("Ghost");
		this.ghostTeam.setCanSeeFriendlyInvisibles(true);
		this.tryBiting = false;
		this.executedPlayer = null;
		this.bittenPlayer = null;
		this.cursedPlayerList = new ArrayList<VillagePlayer>();;
		this.currentVoteNum = 0;
		
		this.maxNum = 16;
		this.uranaiNum = 1;
		this.reibaiNum = 1;
		this.kariudoNum = 1;
		this.kyoujinNum = 1;
		this.jinrouNum = 2;
		this.youkoNum = 1;
		this.title = "みんなの人狼村";
		this.description = "誰でも歓迎！マナーを守って楽しく人狼ゲーム！";
		this.setPassword = false;
		this.password = "";
		this.dayTime = 240;
		this.nightTime = 120;
		this.randomVote = false;
		this.revoteNum = 3;
		this.requestRole = false;
		this.permitWhisp = false;
		this.reishiAllPlayers = false;
		this.permitBite = false;

		this.playerList.add(new VillagePlayer(Bukkit.getOfflinePlayer("Mr.Firvic"), (Village) this));
		VillagePlayer fv = this.getPlayer("Mr.Firvic");
		fv.joining = true;
		this.objective.getScore(fv.player).setScore(1);
		this.objective.getScore(fv.player).setScore(0);
	}

	public void sendToVillage(String message){
		for(VillagePlayer vp : getPlayerListExceptNPC())
			vp.sendMessage(message);
	}
	
	public void unsetAllSettedRole(VillageRole role){
		for(VillagePlayer vp : getJoiningPlayerList())
			if(vp.role==role)
				vp.role = VillageRole.none;
	}
	
	public void unkick(String plName){
		Iterator<Player> itr = kickedPlayerList.iterator();
		while(itr.hasNext()){
			if(itr.next().getName().equals(plName)){
				itr.remove();
				break;
			}
		}
	}
	
	public void shoutRule(){
		for(VillagePlayer vp : getPlayerListExceptNPC()){
			vp.sendMessage(C.gold+"ゲームマスターがルールの確認を促しています。");
			vp.showRule();
		}
	}
	
	public VillagePlayer getPlayer(Player pl){
		for(VillagePlayer vp : playerList)
			if(vp.player==pl)
				return vp;
		return null;
	}
	
	public VillagePlayer getPlayer(String plName){
		for(VillagePlayer vp : playerList)
			if(vp.getName().equals(plName))
				return vp;
		return null;		
	}
	
	public VillagePlayer getAlivePlayerByColor(ChatColor color){
		if(status==VillageStatus.ongoing)
			for(VillagePlayer vp : playerList)
				if(vp.alive && vp.color==color)
					return vp;
		return null;
	}

	public List<VillagePlayer> getJoiningPlayerList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.joining)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getJoiningPlayerListExceptNPC(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.joining && vp.connection)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getPlayerListExceptNPC(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.connection)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getNPCList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		if(status==VillageStatus.empty
				|| status==VillageStatus.preparing
				|| status==VillageStatus.recruiting){
			plList.add(getPlayer("Mr.Firvic"));
		}else{
			plList = getAlivePlayerList();
		}
		return plList;
	}
	
	public List<VillagePlayer> getAlivePlayerList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.alive)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getAlivePlayerListExceptNPC(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.connection)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getAliveNPCList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && !vp.connection)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getDeadPlayerList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(!vp.alive && vp.joining)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getGhostPlayerList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		if(status==VillageStatus.ongoing)
			for(VillagePlayer vp : playerList)
				if(!vp.alive && vp.connection)
					plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getBitablePlayerList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.role!=VillageRole.jinrou)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getUranaishiList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.uranaishi)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getKariudoList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.kariudo)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getJinrouList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.jinrou)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getJinrouListExceptNPC(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.jinrou && vp.connection)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getYoukoList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.youko)
				plList.add(vp);
		return plList;
	}

	public List<String> getPlayerNameListExceptNPC(){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.connection)
				plList.add(vp.getName());
		return plList;
	}
	
	public List<String> getPlayerNameListExceptGMandNPC(){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(!vp.gameMaster && vp.connection)
				plList.add(vp.getName());
		return plList;
	}
	
	public List<String> getJoiningPlayerNameList(){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.joining)
				plList.add(vp.getName());
		return plList;
	}

	public List<String> getKickedPlayerNameList(){
		List<String> plList = new ArrayList<String>();
		for(Player pl : kickedPlayerList)
			plList.add(pl.getName());
		return plList;
	}

	public List<String> getAlivePlayerNameList(){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.alive)
				plList.add(vp.getName());
		return plList;
	}
	
	public List<String> getAlivePlayerNameListExceptMyself(Player pl){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.player!=pl)
				plList.add(vp.getName());
		return plList;
	}
	
	public List<String> getAlivePlayerNameListExceptMyselfAddedBittenPlayer(Player pl){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.player!=pl)
				plList.add(vp.getName());
		if(bittenPlayer!=null)
			plList.add(bittenPlayer.getName());
		return plList;
	}
	
	public List<String> getAlivePlayerNameListExceptMyselfAndNPC(Player pl){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.player!=pl && vp.connection)
				plList.add(vp.getName());
		return plList;
	}

	public List<String> getBitablePlayerNameList(){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.role!=VillageRole.jinrou)
				plList.add(vp.getName());
		return plList;
	}
	
	public boolean isPlayerExceptGMandNPC(String plName){
		for(String pn : getPlayerNameListExceptGMandNPC()){
			if(pn.equals(plName))
				return true;
		}
		return false;
	}
	
	public boolean isJoiningPlayer(String plName){
		for(String pn : getJoiningPlayerNameList()){
			if(pn.equals(plName))
				return true;
		}
		return false;
	}
	
	public boolean isAlivePlayer(String plName){
		for(String pn : getAlivePlayerNameList()){
			if(pn.equals(plName))
				return true;
		}
		return false;
	}

	public boolean isAlivePlayerExceptMyself(Player pl, String plName){
		for(String pn : getAlivePlayerNameListExceptMyself(pl)){
			if(pn.equals(plName))
				return true;
		}
		return false;
	}

	public boolean isAlivePlayerExceptMyselfAddedBittenPlayer(Player pl, String plName){
		for(String pn : getAlivePlayerNameListExceptMyselfAddedBittenPlayer(pl)){
			if(pn.equals(plName))
				return true;
		}
		return false;
	}

	public boolean isAlivePlayerExceptMyselfAndNPC(Player pl, String plName){
		for(String pn : getAlivePlayerNameListExceptMyselfAndNPC(pl)){
			if(pn.equals(plName))
				return true;
		}
		return false;
	}

	public boolean isKickedPlayer(Player pl){
		for(Player p : kickedPlayerList){
			if(p==pl)
				return true;
		}
		return false;
	}
	
	public boolean isKickedPlayer(String plName){
		for(Player pl : kickedPlayerList){
			if(pl.getName().equals(plName))
				return true;
		}
		return false;
	}

	public boolean isBitablePlayer(String plName){
		for(String pn : getBitablePlayerNameList()){
			if(pn.equals(plName))
				return true;
		}
		return false;
	}

	public int getTotalRoleNumInRule(){
		return uranaiNum
				+ reibaiNum
				+ kariudoNum
				+ jinrouNum
				+ kyoujinNum
				+ youkoNum;
	}
	
	public int getRoleNumInRule(VillageRole role){
		switch(role){
		case murabito: return maxNum-getTotalRoleNumInRule();
		case uranaishi: return uranaiNum;
		case reibaishi: return reibaiNum;
		case kariudo: return kariudoNum;
		case jinrou: return jinrouNum;
		case kyoujin: return kyoujinNum;
		case youko: return youkoNum;
		}
		return 0;
	}
	
	public int getSettedRoleNum(VillageRole role){
		int roleNum = 0;
		for(VillagePlayer vp : playerList)
			if(vp.role==role)
				roleNum++;
		return roleNum;
	}
	
	public int getHumanNumInRule(){
		return maxNum
				- jinrouNum
				- youkoNum;
	}
	
	public int getJoiningPlayerNum(){
		int count = 0;
		for(VillagePlayer vp : playerList)
			if(vp.joining)
				count++;
		return count;
	}
	
	public int getAlivePlayerNum(){
		int count = 0;
		for(VillagePlayer vp : playerList)
			if(vp.alive)
				count++;
		return count;
	}
}

