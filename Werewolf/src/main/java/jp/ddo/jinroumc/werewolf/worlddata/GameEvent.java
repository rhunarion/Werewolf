package jp.ddo.jinroumc.werewolf.worlddata;

import java.util.HashMap;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageTime;
import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillagePlayer;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class GameEvent implements Listener {
	private static PacketAdapter doorAdapter;
	private static PacketAdapter voiceAdapter;
	private static PacketAdapter entitySpawnAdapter;
	private static PacketAdapter entityMoveAdapter;
	private static PacketAdapter entityTeleportAdapter;
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event){
		
		if(VillageUtil.isVillageName(event.getDamager().getWorld().getName())){
			Entity attacker = event.getDamager();
			if(!(attacker instanceof Player)){
				event.setCancelled(true);
				return;
			}
			Player pl = (Player) attacker;
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer attackerVp = vil.getPlayer(pl); 
			if(!attackerVp.alive && vil.status==VillageStatus.ongoing){
				event.setCancelled(true);
				return;
			}
	
			Entity defender = event.getEntity();
			VillagePlayer defenderVp = null;
			if(defender instanceof Player){
				defenderVp = vil.getPlayer((Player) defender);
			}else{
				for(VillagePlayer vp : vil.getNpcList())
					if(vp.villagerEntity==defender)
						defenderVp = vp;
			}
			if(defenderVp==null)
				return;
			
			if(attackerVp.alive && defenderVp.alive  
					&& attackerVp.role==VillageRole.jinrou
					&& defenderVp.role!=VillageRole.jinrou
					&& vil.status==VillageStatus.ongoing && vil.time==VillageTime.night){
				
				if(vil.tryBiting){
					attackerVp.sendMessage(C.red+"Error: 今夜はすでに一人噛んでいます。");
				}
				else if(vil.day==0 && !defenderVp.getName().equals("Mr.Firvic")){
					attackerVp.sendMessage(C.red+"Error: 0日目の夜は Mr.FirstVictm しか噛み殺すことができません。");
				}else{
					attackerVp.bitePlayer(defenderVp);
				}
			}
	
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(EntityDeathEvent event){
		
		if(VillageUtil.isVillageName(event.getEntity().getWorld().getName())){
			Entity entity = event.getEntity();
			Location loc = entity.getLocation();

			final Village vil = VillageUtil.getVillage(loc.getWorld().getName());
			if(vil.status!=VillageStatus.ongoing
					|| vil.time!=VillageTime.execution
					|| !((DefaultVillage) vil).isInsideScaffold(loc)){
				for(VillagePlayer npc : vil.getNpcList()){
					if(npc.villagerEntity==entity
							&& (vil.status!=VillageStatus.ongoing || npc.alive)){
						npc.spawnVillager();
						return;
					}
				}
				return;
			}
			
			VillagePlayer vp = null;
			if(entity instanceof Player){
				vp = vil.getPlayer((Player) entity);
			}else if(entity.getType().equals(EntityType.VILLAGER)){
				for(VillagePlayer npc : vil.getAliveNpcList())
					if(npc.villagerEntity==entity)
						vp = npc;
			}else{
				return;
			}
			
			if(vp!=vil.executedPlayer)
				return;
			vp.kill();
			vil.sendToVillage(vp.color+vp.getName()
					   +C.green+" さんが処刑されました。間もなく夜が訪れます。");
	
			vil.doTaskLaterId = Bukkit.getScheduler().runTaskLater(vil.plugin, new BukkitRunnable(){
				@Override
				public void run(){
					((DefaultVillage) vil).postExecution();
					vil.checkResult();
					vil.doTaskLaterId = -1;
				}
			}, 100).getTaskId();
		}
	}

	@EventHandler
	public void onTargetPlayer(EntityTargetEvent event){
		if(VillageUtil.isVillageName(event.getEntity().getWorld().getName())){
			Entity entity = event.getEntity();
			Village vil = VillageUtil.getVillage(entity.getWorld().getName());
			for(VillagePlayer npc : vil.getAliveNpcList())
				if(npc.villagerEntity==entity)
					event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onGhostPickupItem(PlayerPickupItemEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Player pl = event.getPlayer();
			
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			if(!vp.alive && vil.status==VillageStatus.ongoing)
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onGhostDropItem(final PlayerDropItemEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Player pl = event.getPlayer();
			
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			if(!vp.alive && vil.status==VillageStatus.ongoing)
				event.setCancelled(true);
	
			Bukkit.getScheduler().runTaskLater(vil.plugin, new BukkitRunnable(){
				@SuppressWarnings("deprecation")
				@Override
				public void run(){
					event.getPlayer().updateInventory();
				}
			}, 1);
		}
	}
	
	public static void removeNightEffect(JavaPlugin plugin){
		doorAdapter = new PacketAdapter(plugin,
				ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_EVENT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						if(VillageUtil.isInVillage(event.getPlayer())){
							Village vil = VillageUtil.getVillage(event.getPlayer());
							int soundId = event.getPacket().getIntegers().read(0);
							if(vil.status==VillageStatus.ongoing && vil.time==VillageTime.night
								&& soundId==1003)
									event.setCancelled(true);
						}
					}
				};
		voiceAdapter = new PacketAdapter(plugin,
				ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						if(VillageUtil.isInVillage(event.getPlayer())){
							Village vil = VillageUtil.getVillage(event.getPlayer());
							String sound = event.getPacket().getStrings().read(0);
							
							if(vil.status==VillageStatus.ongoing && vil.time==VillageTime.night
									&& (sound.startsWith("mob.villager.")
									|| sound.startsWith("mob.zombie.")
									|| sound.startsWith("mob.blaze.")
									|| sound.equals("fire.fire")))
								event.setCancelled(true);
						}
					}
				};
		entitySpawnAdapter = new PacketAdapter(plugin,
				ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
					@Override
					public void onPacketSending(PacketEvent event) {
						if(VillageUtil.isInVillage(event.getPlayer())){
							Player pl = event.getPlayer();
							Village vil = VillageUtil.getVillage(event.getPlayer());

							if(vil.status==VillageStatus.ongoing && vil.time==VillageTime.night){
								VillagePlayer vp = vil.getPlayer(pl);
								
								if(vp.alive && vp.role!=VillageRole.jinrou){
									HashMap<ChatColor, DefaultVillageHouseCore> houseMap = DefaultVillageHouse.getHouseMap();
									double x = (double) event.getPacket().getIntegers().read(2) / 32;
									double y = (double) event.getPacket().getIntegers().read(3) / 32;
									double z = (double) event.getPacket().getIntegers().read(4) / 32;
									if(x<houseMap.get(vp.color).westPlane+0.5
											|| houseMap.get(vp.color).eastPlane+0.5<x
											|| y<houseMap.get(vp.color).bottomPlane+0.5
											|| houseMap.get(vp.color).topPlane+0.5<y
											|| z<houseMap.get(vp.color).northPlane+0.5
											|| houseMap.get(vp.color).southPlane+0.5<z)
										event.setCancelled(true);
								}
							}
						}
					}
				};
		entityMoveAdapter = new PacketAdapter(plugin,
				ListenerPriority.NORMAL, PacketType.Play.Server.REL_ENTITY_MOVE) {
					@Override
					public void onPacketSending(PacketEvent event) {
						if(VillageUtil.isInVillage(event.getPlayer())){
							Village vil = VillageUtil.getVillage(event.getPlayer());
							for(Entity entity : Bukkit.getWorld(vil.villageName).getEntities())
								if(entity.getEntityId()==event.getPacket().getIntegers().read(0)
									&& entity.getType()==EntityType.VILLAGER)
									;//event.setCancelled(true);
						}
					}
				};
		entityTeleportAdapter = new PacketAdapter(plugin,
				ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_TELEPORT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						if(VillageUtil.isInVillage(event.getPlayer())){
							Village vil = VillageUtil.getVillage(event.getPlayer());
							for(Entity entity : Bukkit.getWorld(vil.villageName).getEntities())
								if(entity.getEntityId()==event.getPacket().getIntegers().read(0)
									&& entity.getType()==EntityType.VILLAGER)
									;//event.setCancelled(true);
						}
					}
				};
	
		ProtocolLibrary.getProtocolManager().addPacketListener(doorAdapter);
		ProtocolLibrary.getProtocolManager().addPacketListener(voiceAdapter);
		ProtocolLibrary.getProtocolManager().addPacketListener(entitySpawnAdapter);
		ProtocolLibrary.getProtocolManager().addPacketListener(entityMoveAdapter);
		ProtocolLibrary.getProtocolManager().addPacketListener(entityTeleportAdapter);
	}
	
	public static void removePacketAdapter(){
		ProtocolLibrary.getProtocolManager().removePacketListener(doorAdapter);
		ProtocolLibrary.getProtocolManager().removePacketListener(voiceAdapter);
		ProtocolLibrary.getProtocolManager().removePacketListener(entitySpawnAdapter);
		ProtocolLibrary.getProtocolManager().removePacketListener(entityMoveAdapter);
		ProtocolLibrary.getProtocolManager().removePacketListener(entityTeleportAdapter);
	}
}
