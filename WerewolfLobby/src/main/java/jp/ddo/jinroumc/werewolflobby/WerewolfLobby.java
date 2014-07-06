package jp.ddo.jinroumc.werewolflobby;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.material.Lever;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WerewolfLobby extends JavaPlugin implements Listener {
	public static int rewriteSignID = -1;
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		rewriteSign();
	}
	
	@Override
	public void onDisable(){
		stopRewriteSign();
		Bukkit.getScheduler().cancelAllTasks();
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player pl = event.getPlayer();
		pl.sendMessage(C.gold+"////////// "+C.yellow+"Minecraft 人狼サーバー"
				+C.gold+" へようこそ！ //////////" );
		pl.sendMessage(C.gold+"看板をクリックすれば村ワールドへ移動できます。"
				+"ゲームに参加するには村ワールドへ移動後、"
				+"スポーン地点両脇にあるコマンド掲示板をクリックします。");
		
		pl.getInventory().clear();
		pl.getInventory().addItem(getManual());
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getPlayer().getWorld().getName().equals("world")){
			Player pl = event.getPlayer();
			Block bl = event.getClickedBlock();
		
			if(bl==null)
				return;
			if(clickSign(pl, bl, 2, 65, 3, "entervil vil0"))
				return;
			if(clickSign(pl, bl, 1, 65, 3, "entervil vil1"))
				return;
			if(clickSign(pl, bl, 0, 65, 3, "entervil vil2"))
				return;
			if(clickSign(pl, bl, -1, 65, 3, "entervil vil3"))
				return;
			if(clickSign(pl, bl, -2, 65, 3, "entervil vil4"))
				return;
			if(clickSign(pl, bl, -4, 65, 3, "makevil"))
				return;
		}
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event){
		if(event.getPlayer().getWorld().getName().equals("world")
			&& event.getPlayer().getGameMode()!=GameMode.CREATIVE){
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event){
		if(event.getPlayer().getWorld().getName().equals("world")
			&& event.getPlayer().getGameMode()!=GameMode.CREATIVE){
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event){
		if(event.getEntity().getWorld().getName().equals("world")){
			Entity attacker = event.getDamager();
			if(attacker instanceof Player)
				event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		if(event.getEntity().getWorld().getName().equals("world")){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void setMotd(ServerListPingEvent event){
		event.setMotd(C.gold+"Minecraft 人狼サーバー【jinrou-mc.ddo.jp】");
	}

	private boolean clickSign(Player pl, Block block, int x, int y, int z, String command){
		if(block.getX()==x && block.getY()==y && block.getZ()==z){
			pl.performCommand(command);
			return true;
		}
		return false;
	}

	private static ItemStack getManual(){
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setTitle("Minecraft 人狼サーバーへようこそ");
		meta.addPage("< 遊び方 1/2 >\n" +
				"ロビー(デフォルトスポーン地点)にある看板をクリックすると、それぞれの村ワールドへ移動できます。\n" +
				"村ワールドには、スポーン地点の両脇にコマンドが実行できる掲示板があります。");
		meta.addPage("< 遊び方 2/2 >\n" +
				"人狼ゲームが「空き状態」の場合は「人狼ゲームの準備を開始する」、\n" +
				"人狼ゲームが「募集中」場合は「ゲームに参加する」と書かれた" +
				"看板をクリックしましょう。");
		meta.addPage("< 困ったときは 1/2 >\n" +
				"まずは公式Wikiを確認してください。\n" +
				"http://jinroumc.wiki.fc2.com\n" +
				"(または「jinrou-mc」で検索)\n\n" +
				"最新情報や詳しい遊び方を確認できます。");
		meta.addPage("< 困ったときは 2/2 >\n" +
				"/helpとコマンドすると、今使えるコマンドのヘルプを見ることができます。\n" +
				"動けなくなった場合は、/homeまたは/lobbyコマンドを使ってスポーン地点へ移動してみてください。\n" +
				"それでも解決しない場合は再ログインしてみてください。");
		book.setItemMeta(meta);
		return book;
	}

	private void rewriteSign(){
		rewriteSignID = Bukkit.getScheduler().runTaskTimer(this, new BukkitRunnable(){
			@Override
			public void run(){
				World lobby = Bukkit.getWorld("world");
				Sign[] signArr = new Sign[5];
				signArr[0] = (Sign) lobby.getBlockAt(2, 65, 3).getState();
				signArr[1] = (Sign) lobby.getBlockAt(1, 65, 3).getState();
				signArr[2] = (Sign) lobby.getBlockAt(0, 65, 3).getState();
				signArr[3] = (Sign) lobby.getBlockAt(-1, 65, 3).getState();
				signArr[4] = (Sign) lobby.getBlockAt(-2, 65, 3).getState();
				Village[] vilArr = new Village[5];
				vilArr[0] = VillageUtil.getVillage("vil0");
				vilArr[1] = VillageUtil.getVillage("vil1");
				vilArr[2] = VillageUtil.getVillage("vil2");
				vilArr[3] = VillageUtil.getVillage("vil3");
				vilArr[4] = VillageUtil.getVillage("vil4");
				
				for(int i=0; i<5; i++)
					setVillageStatusLine(signArr[i], vilArr[i]);
				
				BlockState state = lobby.getBlockAt(-4, 65, 5).getState();
				Lever lever = (Lever) state.getData(); 
				for(Village vil : VillageUtil.getVillageList()){
					if(vil.status==VillageStatus.empty){
						lever.setPowered(true);
						state.update();
						return;
					}
				}
				lever.setPowered(false);
				state.update();
			}
		}, 20, 20).getTaskId();
	}
	
	private static void setVillageStatusLine(Sign sign, Village vil){
		String title = vil.title;
		if(title.length()>=9)
			title = title.substring(0, 8)+"...";
		
		String desc = vil.description;
		if(desc.length()>=9)
			desc = desc.substring(0, 8)+"...";
		
		String status = "";
		switch(vil.status){
		case empty: status = "空き状態"; break;
		case preparing: status = "準備中"; break;
		case recruiting: status = "募集中"; break;
		case ongoing:
			status = vil.day+"日目";
			switch(vil.time){
			case day: status += "昼"; break;
			case execution: status += "夕方"; break;
			case night: status += "夜"; break;
			}
			break;
		case finishing: status = "終了中"; break;
		}
		
		int num = 0;
		switch(vil.status){
		case preparing: num = vil.getJoiningPlayerNum(); break;
		case recruiting: num = vil.getJoiningPlayerNum(); break;
		case ongoing: num = vil.getAlivePlayerNum(); break;
		}

		sign.setLine(0, vil.villageName);
		sign.setLine(1, title);
		sign.setLine(2, desc);
		if(vil.status==VillageStatus.empty || vil.status==VillageStatus.finishing)
			sign.setLine(3, status);
		else
			sign.setLine(3, status+" "+num+"/"+vil.maxNum);
		sign.update();
		
		Location loc = sign.getLocation();
		loc.setZ(loc.getZ()+2);
		BlockState state = loc.getBlock().getState();
		Lever lever = (Lever) state.getData();
		if(vil.status==VillageStatus.recruiting && vil.getJoiningPlayerNum()<vil.maxNum)
			lever.setPowered(true);
		else
			lever.setPowered(false);
		state.update();
	}
	
	private static void stopRewriteSign(){
		if(rewriteSignID!=-1){
			Bukkit.getScheduler().cancelTask(rewriteSignID);
			rewriteSignID = -1;
		}
	}
}
