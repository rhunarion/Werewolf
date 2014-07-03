package jp.ddo.jinroumc.werewolf;

import java.util.List;

import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;
import jp.ddo.jinroumc.werewolf.worlddata.DefaultVillageEvent;
import jp.ddo.jinroumc.werewolf.worlddata.GameEvent;
import jp.ddo.jinroumc.werewolf.worlddata.LobbyData;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class Werewolf extends JavaPlugin {
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		getServer().getPluginManager().registerEvents(new GameEvent(), this);
		getServer().getPluginManager().registerEvents(new LobbyData(), this);
		getServer().getPluginManager().registerEvents(new DefaultVillageEvent(), this);
		getCommand("ww").setExecutor(new WwCommandExecutor());
		getCommand("ww").setTabCompleter(new WwTabCompleter());

		LobbyData.initLobbyLocation(getConfig());
		for(Player pl : Bukkit.getOnlinePlayers()){
			VillageUtil.teleportToLobby(pl);
			VillageUtil.onPlayerLeave(pl);
		}

		VillageUtil.initVillageData(getConfig(), this);
		WorldCreator.name("default_village").createWorld();
		for(Village vil : VillageUtil.getVillageList())
			WorldCreator.name(vil.villageName).createWorld();
		List<Village> vilList = VillageUtil.getVillageList();
		for(int i=0; i<vilList.size(); i++)
			vilList.get(i).rebuildVillage();

		LobbyData.rewriteSign(this);
	}
	
	@Override
	public void onDisable(){
		Bukkit.getScheduler().cancelAllTasks();
	}
}

