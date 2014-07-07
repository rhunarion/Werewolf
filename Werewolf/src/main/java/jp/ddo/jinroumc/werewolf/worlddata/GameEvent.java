package jp.ddo.jinroumc.werewolf.worlddata;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageTime;
import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillagePlayer;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;

import org.bukkit.Bukkit;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class GameEvent implements Listener {

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
				for(VillagePlayer vp : vil.getNPCList())
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
					|| !((DefaultVillageData) vil).isInsideScaffold(loc)){
				for(VillagePlayer npc : vil.getNPCList()){
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
				for(VillagePlayer npc : vil.getAliveNPCList())
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
	
			vil.doTaskLaterID = Bukkit.getScheduler().runTaskLater(vil.plugin, new BukkitRunnable(){
				@Override
				public void run(){
					((DefaultVillageData) vil).postExecution();
					vil.checkResult();
					vil.doTaskLaterID = -1;
				}
			}, 100).getTaskId();
		}
	}

	@EventHandler
	public void onTargetPlayer(EntityTargetEvent event){
		if(VillageUtil.isVillageName(event.getEntity().getWorld().getName())){
			Entity entity = event.getEntity();
			Village vil = VillageUtil.getVillage(entity.getWorld().getName());
			for(VillagePlayer npc : vil.getAliveNPCList())
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
	
	@EventHandler
	public void onGhostInteract(PlayerInteractEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Player pl = event.getPlayer();
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			if(!vp.alive && vil.status==VillageStatus.ongoing)
				event.setCancelled(true);
		}
	}

	public static void removeNightSound(JavaPlugin plugin){
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
			ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_EVENT) {
				@Override
				public void onPacketSending(PacketEvent event) {
					System.out.println(event.getPacket().getStrings().size());
					System.out.println(event.getPacket().getStrings().readSafely(0));
					System.out.println(event.getPacket().getStrings().readSafely(1));
					System.out.println(event.getPacket().getStrings().readSafely(2));
					System.out.println(event.getPacket().getStrings().readSafely(3));
					/*String soundName = event.getPacket().getStrings().read(0);
					if (soundName.equals("random.door_open")
							|| soundName.equals("random.door_close")) {
						event.setCancelled(true);
					}*/
				}
			}
		);
	}
}
