package jp.dip.jinroumc.werewolf.village;

import de.robingrether.idisguise.api.DisguiseAPI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.dip.jinroumc.werewolf.enumconstant.VillageRole;
import jp.dip.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.dip.jinroumc.werewolf.util.C;
import jp.dip.jinroumc.werewolf.util.LobbyData;
import jp.dip.jinroumc.werewolf.util.VillageManual;
import jp.dip.jinroumc.werewolf.worlddata.DefaultVillage;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class VillageUtil {
	private static List<Village> villageList = new ArrayList<Village>();
	
	public static void initVillageData(FileConfiguration config, JavaPlugin plugin){
		List<String> villageType = config.getStringList("villageType");
		for(String vt : villageType){
			List<String> villageName = config.getStringList("villageName."+vt);
			for(String vn : villageName){
				villageList.add(new DefaultVillage(vn, vt, plugin));
			}
		}
	}
	
	public static List<Village> getVillageList(){
		return villageList;
	}
	
	public static Village getVillage(String vilName){
		for(Village vil : villageList)
			if(vil.villageName.equalsIgnoreCase(vilName))
				return vil;
		return null;
	}

	public static Village getVillage(Player pl){
		String vilName = pl.getWorld().getName();
		for(Village vil : villageList)
			if(vil.villageName.equals(vilName))
				return vil;
		return null;
	}	
	
	public static VillageRole getVillageRoleByString(String role){
		role = role.toLowerCase();
		switch(role){
		case "none": return VillageRole.NONE;
		case "murabito": return VillageRole.MURABITO;
		case "uranaishi": return VillageRole.URANAISHI;
		case "reibaishi": return VillageRole.REIBAISHI;
		case "kariudo": return VillageRole.KARIUDO;
		case "jinrou": return VillageRole.JINROU;
		case "kyoujin": return VillageRole.KYOUJIN;
		case "youko": return VillageRole.YOUKO;
		}
		return null;
	}

	public static String getVillageRoleInJapanese(VillageRole role){
		switch(role){
		case MURABITO: return C.aqua+"村人";
		case URANAISHI: return C.aqua+"占い師";
		case REIBAISHI: return C.aqua+"霊媒師";
		case KARIUDO: return C.aqua+"狩人";
		case JINROU: return C.d_red+"人狼";
		case KYOUJIN: return C.d_red+"狂人";
		case YOUKO: return C.yellow+"妖狐";
		}
		return null;
	}

	public static String getTrueRole(VillagePlayer vp){
		if(vp.role!=VillageRole.JINROU)
			return C.aqua+"村人";
		else
			return C.d_red+"人狼";
	}
	
	public static boolean isInVillage(Player pl){
		String vilName = pl.getWorld().getName();
		for(Village vil : villageList)
			if(vil.villageName.equals(vilName))
				return true;
		return false;
	}	

	public static boolean isVillageName(String vilName){
		for(Village vil : villageList)
			if(vil.villageName.equalsIgnoreCase(vilName))
				return true;
		return false;
	}
	
	public static boolean isVillageType(String vilType){
		for(Village vil : villageList)
			if(vil.villageType.equalsIgnoreCase(vilType))
				return true;
		return false;
	}
	
	public static boolean isVillageRoleExceptNone(String role){
		if(role.equalsIgnoreCase("none"))
			return false;
		
		VillageRole vilRole = getVillageRoleByString(role);
		for(VillageRole vr : VillageRole.values())
			if(vr==vilRole)
				return true;
		return false;
	}
	
	public static boolean isVillageRole(String role){
		VillageRole vilRole = getVillageRoleByString(role);
		for(VillageRole vr : VillageRole.values())
			if(vr==vilRole)
				return true;
		return false;
	}
	
	public static List<String> getVillageNameList(){
		List<String> vilNameList = new ArrayList<String>();
		for(Village vil : villageList)
			vilNameList.add(vil.villageName);
		return vilNameList;
	}
	
	public static List<String> getVillageTypeList(){
		List<String> vilTypeList = new ArrayList<String>();
		for(Village vil : villageList)
			if(!vilTypeList.contains(vil.villageType))
				vilTypeList.add(vil.villageType);
		return vilTypeList;
	}
	
	public static List<String> getRoleList(){
		List<String> roleList = new ArrayList<String>();
		for(VillageRole vr : VillageRole.values())
			roleList.add(vr.toString());
		return roleList;
	}

	public static List<String> getRoleListExceptNone(){
		List<String> roleList = new ArrayList<String>();
		for(VillageRole vr : VillageRole.values()){
			if(vr==VillageRole.NONE) continue;
			roleList.add(vr.toString());
		}
		return roleList;
	}

	public static void teleportToVillage(Player pl, Village vil){
		Location loc = ((DefaultVillage) vil).getSpawnLocation();
		pl.teleport(loc);
		pl.setBedSpawnLocation(loc, true);
	}
	
	public static void teleportToLobby(Player pl){
		Location loc = LobbyData.getLobby();
		pl.teleport(loc);
		pl.setBedSpawnLocation(loc, true);
	}

	@SuppressWarnings("deprecation")
	public static void onPlayerEnter(Player pl, Village vil){
		addPlayer(pl, vil);
		pl.setScoreboard(VillageUtil.getVillage(pl).scoreboard);
		
		VillagePlayer vp = vil.getPlayer(pl);
		if(vil.status==VillageStatus.EMPTY && !(vp.getName().contains(".")))
			BarAPI.setMessage(pl, C.yellow+vil.title+C.white
								+" ("+vil.villageName+") <空き状態>");
		if(vil.status==VillageStatus.ONGOING && !vp.alive)
			vp.addGhostTeam();
		if(vil.status==VillageStatus.FINISHING)
			vp.changeStatusOnGameFinish();
		
		pl.getInventory().addItem(new ItemStack(Material.LOG, 64));
		pl.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 64));
		pl.getInventory().addItem(new ItemStack(Material.BREAD, 64));
		pl.updateInventory();
	}

	public static void onPlayerLeave(Player pl){
		if(VillageUtil.isInVillage(pl)){
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			if(vp.joining || vp.gameMaster){
				vp.giveUpGame();
			}else{
				removePlayer(pl, vil);
			}
			for(VillagePlayer pc : vil.getPlayerListExceptNpc()){
				pl.showPlayer(pc.getPlayer());
				pc.getPlayer().showPlayer(pl);
			}
			if(vil.getPlayerListExceptNpc().size()==0 && vil.status!=VillageStatus.EMPTY)
				vil.rebuildVillage();
		}
		BarAPI.removeBar(pl);
		pl.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		pl.removePotionEffect(PotionEffectType.INVISIBILITY);
		if(pl.getGameMode()!=GameMode.CREATIVE){
			pl.setFlying(false);
			pl.setAllowFlight(false);
		}
		pl.getInventory().clear();
		pl.getInventory().setArmorContents(null);
		pl.getInventory().addItem(VillageManual.getManual());
		if(!pl.getName().contains("."))
			pl.getInventory().setHeldItemSlot(0);
		
		DisguiseAPI api = Bukkit.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
		if(api.isDisguised(pl))
			api.undisguiseToAll(pl);
	}
	
	public static void addPlayer(Player pl, Village vil){
		for(VillagePlayer vp : vil.playerList){
			if(vp.getName().equals("."+pl.getName()) && vp.alive){
				vil.scoreboard.resetScores(Bukkit.getOfflinePlayer(vp.getColorName()));
				vp.player = (OfflinePlayer) pl;
				vp.connection = true;
				vp.teleportToHome();
				vp.villagerEntity.remove();
				vp.villagerEntity = null;
				vp.removeFenceAroundBed();
				vil.objective.getScore(Bukkit.getOfflinePlayer(vp.getColorName())).setScore(1);
				vil.objective.getScore(Bukkit.getOfflinePlayer(vp.getColorName())).setScore(0);
				vil.sendToVillage(C.yellow+pl.getName()+C.gold+" さんが "
						+C.yellow+vil.title+C.gold+" ("+vil.villageName+") に帰ってきました。");
				return;
			}
		}
		
		vil.playerList.add(new VillagePlayer(pl));
		if(vil.status!=VillageStatus.ONGOING)
			vil.sendToVillage(C.yellow+pl.getName()+C.gold+" さんが "
							+C.yellow+vil.title+C.gold+" ("+vil.villageName+") を訪れました。");
	}
	
	public static void removePlayer(OfflinePlayer pl, Village vil){
		Iterator<VillagePlayer> itr = vil.playerList.iterator(); 
		while(itr.hasNext()){
			if(itr.next().player==pl){
				itr.remove();
				if(vil.status!=VillageStatus.ONGOING)
					vil.sendToVillage(C.yellow+pl.getName()+C.gold
									+" さんが "+C.yellow+vil.title+C.gold
									+" ("+vil.villageName+") から去っていきました。");
				break;
			}
		}
	}

	public static void onPlayerRespawn(Player pl){
		if(VillageUtil.isInVillage(pl)){
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			vp.teleportToHome();
			if(!vp.alive && vil.status==VillageStatus.ONGOING)
				vp.addInvisibleEffect();
		}
	}	
}

