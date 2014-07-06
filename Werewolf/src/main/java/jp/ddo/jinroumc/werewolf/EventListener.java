package jp.ddo.jinroumc.werewolf;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import jp.ddo.jinroumc.werewolf.util.SendMessage;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
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
	public void onPlayerJoin(PlayerJoinEvent event){
		if(VillageUtil.isInVillage(event.getPlayer()))
			VillageUtil.teleportToLobby(event.getPlayer());
		else
			SendMessage.sendToLobby(event.getJoinMessage());
		event.setJoinMessage(null);
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event){
		if(VillageUtil.isInVillage(event.getPlayer()))
			VillageUtil.teleportToLobby(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event){
		if(VillageUtil.isInVillage(event.getPlayer()))
			VillageUtil.onPlayerLeave(event.getPlayer());
		else
			SendMessage.sendToLobby(event.getQuitMessage());
		event.setQuitMessage(null);
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		SendMessage.sendToLobby(event.getLeaveMessage());
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
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
		}else{
			SendMessage.sendToLobby(event.getDeathMessage());
		}
		event.setDeathMessage(null);
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
