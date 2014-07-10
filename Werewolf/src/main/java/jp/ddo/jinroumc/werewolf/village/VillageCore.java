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
	public int timerId;
	public int doTaskLaterId;
	public int asyncRebuildId;
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
		this.status = VillageStatus.EMPTY;
		this.result = VillageResult.DRAW;
		this.plugin = plugin;
		this.timerId = -1;
		this.doTaskLaterId = -1;
		this.asyncRebuildId = -1;
		this.playerList = new ArrayList<VillagePlayer>();
		this.kickedPlayerList = new ArrayList<Player>();

		this.day = 0;
		this.time = VillageTime.NIGHT;
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
		for(VillagePlayer vp : getPlayerListExceptNpc())
			vp.sendMessage(message);
	}
	
	public void unsetAllSettedRole(VillageRole role){
		for(VillagePlayer vp : getJoiningPlayerList())
			if(vp.role==role)
				vp.role = VillageRole.NONE;
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
		for(VillagePlayer vp : getPlayerListExceptNpc()){
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
		if(status==VillageStatus.ONGOING)
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
	
	public List<VillagePlayer> getJoiningPlayerListExceptNpc(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.joining && vp.connection)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getPlayerListExceptNpc(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.connection)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getPlayerListExceptAliveJinrouAndNpc(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.connection && (vp.role!=VillageRole.JINROU || !vp.alive))
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getNpcList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		if(status==VillageStatus.EMPTY
				|| status==VillageStatus.PREPARING
				|| status==VillageStatus.RECRUITING){
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

	public List<VillagePlayer> getAlivePlayerListExceptNpc(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.connection)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getAliveNpcList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && !vp.connection)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getJoiningPlayerListExceptAlive(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(!vp.alive && vp.joining)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getPlayerListExceptAliveWhileOngoing(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		if(status==VillageStatus.ONGOING)
			for(VillagePlayer vp : playerList)
				if(!vp.alive && vp.connection)
					plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getAlivePlayerListExceptJinrou(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.role!=VillageRole.JINROU)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getAlivePlayerListExceptJinrouAndNpc(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.role!=VillageRole.JINROU && vp.connection)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getUranaishiList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.URANAISHI)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getKariudoList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.KARIUDO)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getJinrouList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.JINROU)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getJinrouListExceptNpc(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.JINROU && vp.connection)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getAliveJinrouList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.JINROU && vp.alive)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getAliveJinrouListExceptNpc(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.JINROU && vp.connection && vp.alive)
				plList.add(vp);
		return plList;
	}
	
	public List<VillagePlayer> getYoukoList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.YOUKO)
				plList.add(vp);
		return plList;
	}

	public List<VillagePlayer> getAliveYoukoList(){
		List<VillagePlayer> plList = new ArrayList<VillagePlayer>();
		for(VillagePlayer vp : playerList)
			if(vp.role==VillageRole.YOUKO && vp.alive)
				plList.add(vp);
		return plList;
	}

	public List<String> getPlayerNameListExceptNpc(){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.connection)
				plList.add(vp.getName());
		return plList;
	}
	
	public List<String> getPlayerNameListExceptGmAndNpc(){
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
	
	public List<String> getAlivePlayerNameListExceptMyselfAndNpc(Player pl){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.player!=pl && vp.connection)
				plList.add(vp.getName());
		return plList;
	}

	public List<String> getAlivePlayerNameListExceptJinrou(){
		List<String> plList = new ArrayList<String>();
		for(VillagePlayer vp : playerList)
			if(vp.alive && vp.role!=VillageRole.JINROU)
				plList.add(vp.getName());
		return plList;
	}
	
	public boolean isPlayerExceptGmAndNpc(String plName){
		for(String pn : getPlayerNameListExceptGmAndNpc()){
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

	public boolean isAlivePlayerExceptMyselfAndNpc(Player pl, String plName){
		for(String pn : getAlivePlayerNameListExceptMyselfAndNpc(pl)){
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

	public boolean isAlivePlayerExceptJinrou(String plName){
		for(String pn : getAlivePlayerNameListExceptJinrou()){
			if(pn.equals(plName))
				return true;
		}
		return false;
	}

	public int getTotalRoleNumInRuleExceptMurabito(){
		return uranaiNum
				+ reibaiNum
				+ kariudoNum
				+ jinrouNum
				+ kyoujinNum
				+ youkoNum;
	}
	
	public int getRoleNumInRule(VillageRole role){
		switch(role){
		case MURABITO: return maxNum-getTotalRoleNumInRuleExceptMurabito();
		case URANAISHI: return uranaiNum;
		case REIBAISHI: return reibaiNum;
		case KARIUDO: return kariudoNum;
		case JINROU: return jinrouNum;
		case KYOUJIN: return kyoujinNum;
		case YOUKO: return youkoNum;
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

