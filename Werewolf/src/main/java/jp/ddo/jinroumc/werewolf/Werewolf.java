package jp.ddo.jinroumc.werewolf;

import jp.ddo.jinroumc.werewolf.util.LobbyData;
import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;
import jp.ddo.jinroumc.werewolf.worlddata.DefaultVillageEvent;
import jp.ddo.jinroumc.werewolf.worlddata.GameEvent;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import de.robingrether.idisguise.sound.SoundSystem;

public class Werewolf extends JavaPlugin {
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		getServer().getPluginManager().registerEvents(new GameEvent(), this);
		getServer().getPluginManager().registerEvents(new DefaultVillageEvent(), this);
		GameEvent.removeNightSound(this);
		getCommand("ww").setExecutor(new WwCommandExecutor());
		getCommand("ww").setTabCompleter(new WwTabCompleter());

		this.saveDefaultConfig();
		VillageUtil.initVillageData(getConfig(), this);
		WorldCreator.name("default_village").createWorld();
		World defaultVillage = Bukkit.getWorld("default_village");
		defaultVillage.setTime(6000);
		defaultVillage.setDifficulty(Difficulty.PEACEFUL);
		defaultVillage.setGameRuleValue("keepInventory", "true");
		defaultVillage.setGameRuleValue("mobGriefing", "false");
		defaultVillage.setGameRuleValue("doDaylightCycle", "false");
		defaultVillage.setGameRuleValue("doMobSpawning", "false");
		
		for(Village vil : VillageUtil.getVillageList()){
			WorldCreator.name(vil.villageName).createWorld();
			Bukkit.getWorld(vil.villageName).setTime(6000);
			Bukkit.getWorld(vil.villageName).setDifficulty(Difficulty.EASY);
			Bukkit.getWorld(vil.villageName).setGameRuleValue("keepInventory", "true");
			Bukkit.getWorld(vil.villageName).setGameRuleValue("mobGriefing", "false");
			Bukkit.getWorld(vil.villageName).setGameRuleValue("doDaylightCycle", "false");
		}

		LobbyData.initLobbyLocation(getConfig());
		for(Player pl : Bukkit.getOnlinePlayers()){
			if(VillageUtil.isInVillage(pl)){
				VillageUtil.teleportToLobby(pl);
				VillageUtil.onPlayerLeave(pl);
			}
		}
		
		SoundSystem.setEnabled(false);
	}
	
	@Override
	public void onDisable(){
		for(Village vil : VillageUtil.getVillageList()){
			vil.stopTimer();
			vil.stopDoTaskLater();
			vil.stopAsyncRebuild();
		}
	}
}

