package jp.ddo.jinroumc.werewolf;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.util.SendMessage;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;
import jp.ddo.jinroumc.werewolf.worlddata.LobbyData;

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
		event.setJoinMessage(null);
		
		Player pl = event.getPlayer();
		pl.sendMessage(C.gold+"////////// "+C.yellow+"Minecraft 人狼サーバー"
				+C.gold+" へようこそ！ //////////" );
		pl.sendMessage(C.gold+"看板をクリックすれば村ワールドへ移動できます。"
				+"ゲームに参加するには村ワールドへ移動後、"
				+"スポーン地点両脇にあるコマンド掲示板をクリックします。");
		
		pl.getInventory().clear();
		pl.getInventory().addItem(LobbyData.getManual());
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event){
		if(VillageUtil.isInVillage(event.getPlayer()))
			VillageUtil.teleportToLobby(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event){
		VillageUtil.onPlayerLeave(event.getPlayer());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		event.setCancelled(true);
		SendMessage.splitUnspecifiedMessage(event.getPlayer(), event.getMessage());
	}

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event){
		event.setDeathMessage(null);

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
	
	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event){
		Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable(){
			public void run(){
				Player pl = event.getPlayer();
				VillageUtil.onPlayerRespawn(pl);
			}
		}, 2);
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		List<Block> blList = event.blockList();
		Iterator<Block> itr = blList.iterator();
		while(itr.hasNext())
			if(itr.next().getType()!=Material.TNT)
				itr.remove();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMoveInCreativeMode(PlayerMoveEvent event){
		if(event.getPlayer().getGameMode()==GameMode.CREATIVE)
			event.setCancelled(false);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBreakBlockInCreativeMode(BlockBreakEvent event){
		if(event.getPlayer().getGameMode()==GameMode.CREATIVE)
			event.setCancelled(false);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlaceBlockInCreativeMode(BlockPlaceEvent event){
		if(event.getPlayer().getGameMode()==GameMode.CREATIVE)
			event.setCancelled(false);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractInCreativeMode(PlayerInteractEvent event){
		if(event.getPlayer().getGameMode()==GameMode.CREATIVE)
			event.setCancelled(false);
	}
}
