package jp.ddo.jinroumc.werewolf.village;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.worlddata.DefaultVillageData;
import jp.ddo.jinroumc.werewolf.worlddata.LobbyData;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
				villageList.add(new Village(vn, vt, plugin));
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
		case "murabito": return VillageRole.murabito;
		case "uranaishi": return VillageRole.uranaishi;
		case "reibaishi": return VillageRole.reibaishi;
		case "kariudo": return VillageRole.kariudo;
		case "jinrou": return VillageRole.jinrou;
		case "kyoujin": return VillageRole.kyoujin;
		case "youko": return VillageRole.youko;
		}
		return null;
	}

	public static String getVillageRoleInJapanese(VillageRole role){
		switch(role){
		case murabito: return C.aqua+"村人";
		case uranaishi: return C.aqua+"占い師";
		case reibaishi: return C.aqua+"霊媒師";
		case kariudo: return C.aqua+"狩人";
		case jinrou: return C.d_red+"人狼";
		case kyoujin: return C.d_red+"狂人";
		case youko: return C.yellow+"妖狐";
		}
		return null;
	}

	public static String getTrueRole(VillagePlayer vp){
		if(vp.role!=VillageRole.jinrou)
			return C.aqua+"村人";
		else
			return C.d_red+"人狼";
	}
	
	public static boolean isInLobby(Player pl){
		if(pl.getWorld().getName().equals(LobbyData.getLobbyWorldName()))
			return true;
		return false;
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
		for(VillageRole vr : VillageRole.values()){
			if(vr==VillageRole.none) continue;
			roleList.add(vr.toString());
		}
		return roleList;
	}

	public static void teleportToVillage(Player pl, Village vil){
		Location loc = DefaultVillageData.getSpawnLocation(vil);
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
		if(vil.status==VillageStatus.empty && !(vp.getName().matches("p\\d+")))
			BarAPI.setMessage(pl, C.yellow+vil.title+C.white
								+" ("+vil.villageName+") <空き状態>");
		if(vil.status==VillageStatus.ongoing)
			vp.addGhostTeam();
		if(vil.status==VillageStatus.finishing)
			vp.changeStatusOnGameFinish();
		
		pl.getInventory().addItem(new ItemStack(Material.LOG, 64));
		pl.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 64));
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
			for(VillagePlayer pc : vil.getPlayerListExceptNPC()){
				pl.showPlayer(pc.getPlayer());
				pc.getPlayer().showPlayer(pl);
			}
			if(vil.getPlayerListExceptNPC().size()==0 && vil.status!=VillageStatus.empty)
				vil.rebuildVillage();
		}
		BarAPI.removeBar(pl);
		pl.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		pl.removePotionEffect(PotionEffectType.INVISIBILITY);
		pl.setFlying(false);
		pl.setAllowFlight(false);
		pl.getInventory().clear();
		pl.getInventory().addItem(LobbyData.getManual());
	}
	
	public static void addPlayer(Player pl, Village vil){
		vil.playerList.add(new VillagePlayer(pl));
		if(vil.status!=VillageStatus.ongoing)
			vil.sendToVillage(C.yellow+pl.getName()+C.gold+" さんが "
							+C.yellow+vil.title+C.gold+" ("+vil.villageName+") を訪れました。");
	}
	
	public static void removePlayer(Player pl, Village vil){
		Iterator<VillagePlayer> itr = vil.playerList.iterator(); 
		while(itr.hasNext()){
			if(itr.next().player==pl){
				itr.remove();
				if(vil.status!=VillageStatus.ongoing)
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
			BarAPI.removeBar(pl);
			vp.teleportToHome();
			if(!vp.alive && vil.status==VillageStatus.ongoing)
				vp.addInvisibleEffect();
		}else{
			pl.teleport(LobbyData.getLobby());
		}
	}	
}

