package jp.dip.jinroumc.werewolf;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import jp.dip.jinroumc.werewolf.util.SendMessage;
import jp.dip.jinroumc.werewolf.village.VillageUtil;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;


public class EventListener implements Listener {
	private JavaPlugin plugin;
	
	EventListener(JavaPlugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable(){
				@Override
				public void run(){
					Player pl = Bukkit.getPlayer(event.getPlayer().getName());
					if(pl!=null){
						VillageUtil.teleportToLobby(event.getPlayer());
						System.out.println("[Werewolf] "+event.getPlayer().getName()+" teleport to Lobby in VillageJoinEvent");
					}
				}
			}, 2);
		}
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event){
		if(VillageUtil.isInVillage(event.getPlayer()))
			VillageUtil.onPlayerLeave(event.getPlayer());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		String world = " (in "+event.getPlayer().getWorld().getName()+")";
		System.out.println("[Werewolf] "+event.getPlayer().getName()+" chat: "+event.getMessage()+world);

		if(VillageUtil.isInVillage(event.getPlayer()))
			SendMessage.splitUnspecifiedMessage(event.getPlayer(), event.getMessage());
		else
			SendMessage.sendToLobbyByPlayer(event.getPlayer(), event.getMessage());
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event){
		if(VillageUtil.isInVillage(event.getEntity())){
			Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable(){
				public void run(){
					PacketContainer packet = new PacketContainer(PacketType.Play.Client.CLIENT_COMMAND);
					packet.getClientCommands().write(0, EnumWrappers.ClientCommand.PERFORM_RESPAWN);
					try {
						ProtocolLibrary.getProtocolManager().recieveClientPacket(event.getEntity(), packet);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}, 40);
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable(){
				public void run(){
					Player pl = event.getPlayer();
					VillageUtil.onPlayerRespawn(pl);
				}
			}, 2);
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		if(VillageUtil.isVillageName(event.getEntity().getWorld().getName())){
			List<Block> blList = event.blockList();
			Iterator<Block> itr = blList.iterator();
			while(itr.hasNext())
				if(itr.next().getType()!=Material.TNT)
					itr.remove();
		}
	}
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event){
		if(VillageUtil.isVillageName(event.getEntity().getWorld().getName())){
			if(event.getSpawnReason()==SpawnReason.NATURAL
					&& event.getEntityType()!=EntityType.COW
					&& event.getEntityType()!=EntityType.SHEEP
					&& event.getEntityType()!=EntityType.PIG
					&& event.getEntityType()!=EntityType.CHICKEN
					&& event.getEntityType()!=EntityType.HORSE
					&& event.getEntityType()!=EntityType.BAT
					&& event.getEntityType()!=EntityType.SQUID)
					event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMoveInCreativeMode(PlayerMoveEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())
				&& event.getPlayer().getGameMode()==GameMode.CREATIVE)
			event.setCancelled(false);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBreakBlockInCreativeMode(BlockBreakEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())
				&& event.getPlayer().getGameMode()==GameMode.CREATIVE)
			event.setCancelled(false);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlaceBlockInCreativeMode(BlockPlaceEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())
				&& event.getPlayer().getGameMode()==GameMode.CREATIVE)
			event.setCancelled(false);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractInCreativeMode(PlayerInteractEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())
				&& event.getPlayer().getGameMode()==GameMode.CREATIVE)
			event.setCancelled(false);
	}
}
