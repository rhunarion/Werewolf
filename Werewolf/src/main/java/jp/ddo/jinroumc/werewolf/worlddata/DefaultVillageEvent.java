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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DefaultVillageEvent implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Player pl = event.getPlayer();
			Block bl = event.getClickedBlock();
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			
			if(bl==null)
				return;
			if(clickSign(pl, bl, 23, 64, 4, "join"))
				return;
			if(clickSign(pl, bl, 23, 64, 5, "unjoin"))
				return;
			if(clickSign(pl, bl, 23, 64, 6, "showrule"))
				return;
			if(clickSign(pl, bl, 23, 64, 7, "showplayers"))
				return;
			if(clickSign(pl, bl, 23, 65, 6, "showvil"))
				return;
			if(clickSign(pl, bl, 23, 65, 7, "leavevil"))
				return;
			if(clickSign(pl, bl, 23, 65, 4, "help"))
				return;
			
			if(clickSign(pl, bl, 23, 64, -4, "makevil"))
				return;
			if(clickSign(pl, bl, 23, 64, -5, "startrec"))
				return;
			if(clickSign(pl, bl, 23, 65, -5, "resettimer"))
				return;
			if(clickSign(pl, bl, 23, 64, -6, "startgame"))
				return;
			if(clickSign(pl, bl, 23, 65, -6, "skip"))
				return;
			if(clickSign(pl, bl, 23, 65, -4, "configroom"))
				return;
			
			if(clickSign(pl, bl, 0, 44, -6, "home"))
				return;
			if(clickSign(pl, bl, 3, 43, 5, "showrule"))
				return;
			if(clickSign(pl, bl, 3, 44, -3, "chrule maxnum "+(vil.maxNum+1)))
				return;
			if(clickSign(pl, bl, 3, 43, -3, "chrule maxnum "+(vil.maxNum-1)))
				return;
			if(clickSign(pl, bl, 3, 44, -2, "chrule uranainum "+(vil.uranaiNum+1)))
				return;
			if(clickSign(pl, bl, 3, 43, -2, "chrule uranainum "+(vil.uranaiNum-1)))
				return;
			if(clickSign(pl, bl, 3, 44, -1, "chrule reibainum "+(vil.reibaiNum+1)))
				return;
			if(clickSign(pl, bl, 3, 43, -1, "chrule reibainum "+(vil.reibaiNum-1)))
				return;
			if(clickSign(pl, bl, 3, 44, 0, "chrule kariudonum "+(vil.kariudoNum+1)))
				return;
			if(clickSign(pl, bl, 3, 43, 0, "chrule kariudonum "+(vil.kariudoNum-1)))
				return;
			if(clickSign(pl, bl, 3, 44, 1, "chrule jinrounum "+(vil.jinrouNum+1)))
				return;
			if(clickSign(pl, bl, 3, 43, 1, "chrule jinrounum "+(vil.jinrouNum-1)))
				return;
			if(clickSign(pl, bl, 3, 44, 2, "chrule kyoujinnum "+(vil.kyoujinNum+1)))
				return;
			if(clickSign(pl, bl, 3, 43, 2, "chrule kyoujinnum "+(vil.kyoujinNum-1)))
				return;
			if(clickSign(pl, bl, 3, 44, 3, "chrule youkonum "+(vil.youkoNum+1)))
				return;
			if(clickSign(pl, bl, 3, 43, 3, "chrule youkonum "+(vil.youkoNum-1)))
				return;

			if(clickSign(pl, bl, -3, 44, 3, "chrule daytime "+(vil.dayTime+60)))
				return;
			if(clickSign(pl, bl, -3, 43, 3, "chrule daytime "+(vil.dayTime-60)))
				return;
			if(clickSign(pl, bl, -3, 44, 2, "chrule nighttime "+(vil.nightTime+60)))
				return;
			if(clickSign(pl, bl, -3, 43, 2, "chrule nighttime "+(vil.nightTime-60)))
				return;
			if(clickSign(pl, bl, -3, 44, 1, "chrule requestrole true"))
				return;
			if(clickSign(pl, bl, -3, 43, 1, "chrule requestrole false"))
				return;
			if(clickSign(pl, bl, -3, 44, 0, "chrule randomvote true"))
				return;
			if(clickSign(pl, bl, -3, 43, 0, "chrule randomvote false"))
				return;
			if(clickSign(pl, bl, -3, 44, -1, "chrule revotenum "+(vil.revoteNum+1)))
				return;
			if(clickSign(pl, bl, -3, 43, -1, "chrule revotenum "+(vil.revoteNum-1)))
				return;
			if(clickSign(pl, bl, -3, 44, -2, "chrule permitwhisp true"))
				return;
			if(clickSign(pl, bl, -3, 43, -2, "chrule permitwhisp false"))
				return;
			if(clickSign(pl, bl, -3, 44, -3, "chrule reishiallplayers true"))
				return;
			if(clickSign(pl, bl, -3, 43, -3, "chrule reishiallplayers false"))
				return;
			if(clickSign(pl, bl, -3, 44, -4, "chrule permitbite true"))
				return;
			if(clickSign(pl, bl, -3, 43, -4, "chrule permitbite false"))
				return;
			
			for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values())
				if(clickSign(pl, bl, house.myroleSignX, house.myroleSignY, house.myroleSignZ, "myrole"))
					return;
			
			
			if(vp.alive && vil.status==VillageStatus.ongoing)
				for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values())
					if(vil.getAlivePlayerByColor(house.color)!=null && vp.color!=house.color
							&& clickSign(pl, bl, house.voteSignX, house.voteSignY, house.voteSignZ,
									"vote "+vil.getAlivePlayerByColor(house.color).getName()))
						return;
						
			if(bl.getType()==Material.CHEST
					&& !(bl.getX()==0 && bl.getY()==43 && bl.getZ()==6)
					&& (vil.status==VillageStatus.empty
					|| vil.status==VillageStatus.preparing
					|| vil.status==VillageStatus.recruiting)){
				event.setCancelled(true);
				return;
			}
			if(vil.status==VillageStatus.ongoing){
				for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values()){
					if(bl.getX()==house.chestX && bl.getY()==house.chestY && bl.getZ()==house.chestZ
							&& vp.color!=house.color){
						pl.sendMessage(C.red+"他のプレイヤーのチェストを開けることはできません。");
						event.setCancelled(true);
					}					
				}
			}
		}
	}

	@EventHandler
	public void onPlaceSign(SignChangeEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Player pl = event.getPlayer();
			Block bl = event.getBlock();
			if(bl.getX()==1 && bl.getY()==43 && bl.getZ()==5
					&& bl.getType()==Material.WALL_SIGN){
				String[] titleList = event.getLines();
				String title = "";
				for(int i=0; i<event.getLines().length; i++)
					title += titleList[i];
				pl.performCommand("chrule title "+title);
				return;
			}
			if(bl.getX()==-1 && bl.getY()==43 && bl.getZ()==5
					&& bl.getType()==Material.WALL_SIGN){
				String[] descList = event.getLines();
				String desc = "";
				for(int i=0; i<descList.length; i++)
					desc += descList[i];
				pl.performCommand("chrule description "+desc);
			}
			if(bl.getX()==-3 && bl.getY()==43 && bl.getZ()==4
					&& bl.getType()==Material.WALL_SIGN){
				String[] pwList = event.getLines();
				String pw = "";
				for(int i=0; i<pwList.length; i++)
					pw += pwList[i];
				pl.performCommand("chrule setpassword "+pw);
			}
			
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Player pl = event.getPlayer();
			Location from = event.getFrom();
			Location to = event.getTo();

			if(to.getX()<-38.0 || 39.0<to.getX()
					|| 80.0<to.getY()
					|| to.getZ()<-38.0 || 39.0<to.getZ()){
				pl.sendMessage(C.red+"Error: ここから先へは移動することはできません。");
				event.setCancelled(true);
				return;
			}
			
			if((from.getY()<=42.0 || 42.4<=from.getY())
					&& 0.3<to.getX() && to.getX()<0.7
					&& 42.0<to.getY() && to.getY()<42.4
					&& -5.7<to.getZ() && to.getZ()<-5.3){
				pl.performCommand("home");
				return;
			}

			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			
			if((-22.0<to.getX() && to.getX()<-16.0
					&& 63.0<to.getY()
					&& -4.0<to.getZ() && to.getZ()<5.0)
					&& vp!=vil.executedPlayer){
				pl.sendMessage(C.red+"Error: ここから先へは移動することはできません。");
				event.setCancelled(true);
				return;
			}			
			
			if(vp.alive && vp.role!=VillageRole.jinrou
					&& vil.status==VillageStatus.ongoing && vil.time==VillageTime.night){
				HashMap<ChatColor, DefaultVillageHouseCore> houseMap = DefaultVillageHouse.getHouseMap();
				DefaultVillageHouseCore house = houseMap.get(vp.color);
				if((house.bedPitch==90 && (to.getX()<house.frontDoorX+0.5 || house.rearDoorX+0.5<to.getX()))
						|| (house.bedPitch==0 && (to.getZ()<house.rearDoorZ+0.5 || house.frontDoorZ+0.5<to.getZ()))
						|| (house.bedPitch==-90 && (to.getX()<house.rearDoorX+0.5 || house.frontDoorX+0.5<to.getX()))
						|| (house.bedPitch==180 && (to.getZ()<house.frontDoorZ+0.5 || house.rearDoorZ+0.5<to.getZ()))){
						pl.sendMessage(C.red+"Error: 夜に外へ出ることはできません。");
						event.setCancelled(true);
						return;
				}
			}
		}
	}
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Player pl = event.getPlayer();
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			int x = event.getBlock().getX();
			int y = event.getBlock().getY();
			int z = event.getBlock().getZ();
			
			if(vil.status==VillageStatus.empty
					|| (vil.status==VillageStatus.preparing && !vp.joining)
					|| (vil.status==VillageStatus.recruiting && !vp.joining)
					|| (vil.status==VillageStatus.ongoing && !vp.alive)){
				event.setCancelled(true); return; }
			
			if(x<=-39 || 39<=x || y<=47 || 80<=y || z<=-39 || 39<=z){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			
			if(-6<=x && x<=6 && 63<=y && -6<=z && z<=6
					&& event.getBlock().getType()!=Material.SKULL){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(-22<=x && x<=-17 && 63<=y && -4<=z && z<=4){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(23<=x && x<=24 && 64<=y && y<=65 && -7<=z && z<=-4){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(23<=x && x<=24 && 64<=y && y<=65 && 4<=z && z<=7){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			
			for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values()){
				if(house.westPlane<=x && x<=house.eastPlane
						&& house.bottomPlane<=y && y<=house.topPlane
						&& house.northPlane<=z && z<=house.southPlane
						&& event.getBlock().getType()!=Material.SKULL){
					pl.sendMessage(C.red+"Error: このエリアは編集できません。");
					event.setCancelled(true);
					return; 
				}
			}
			
			for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values()){
				if(house.namePlateX==x && house.namePlateY==y && house.namePlateZ==z){
					pl.sendMessage(C.red+"Error: このエリアは編集できません。");
					event.setCancelled(true);
					return; 
				}
			}
		}		
	}
	
	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event){
		if(VillageUtil.isInVillage(event.getPlayer())){
			Player pl = event.getPlayer();
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			int x = event.getBlock().getX();
			int y = event.getBlock().getY();
			int z = event.getBlock().getZ();
			
			if(vil.status==VillageStatus.empty
					|| (vil.status==VillageStatus.preparing && !vp.joining)
					|| (vil.status==VillageStatus.recruiting && !vp.joining)
					|| (vil.status==VillageStatus.ongoing && !vp.alive)){
				event.setCancelled(true); updateInventory(pl, vil); return; }
			
			if(x<=-39 || 39<=x || y<=47 || 80<=y || z<=-39 || 39<=z){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			
			if(-6<=x && x<=6 && 63<=y && -6<=z && z<=6
					&& event.getBlock().getType()!=Material.SKULL){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(-22<=x && x<=-17 && 63<=y && -4<=z && z<=4){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(23<=x && x<=24 && 64<=y && y<=65 && -7<=z && z<=-4){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(23<=x && x<=24 && 64<=y && y<=65 && 4<=z && z<=7){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(x==25 && y==64 && z==-6){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(25<=x && x<=23 && 61<=y && y<=62 && -7<=z && z<=-5){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			
			for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values()){
				if(house.westPlane<=x && x<=house.eastPlane
						&& house.bottomPlane<=y && y<=house.topPlane
						&& house.northPlane<=z && z<=house.southPlane
						&& event.getBlock().getType()!=Material.SKULL){
					pl.sendMessage(C.red+"Error: このエリアは編集できません。");
					event.setCancelled(true);
					updateInventory(pl, vil);
					return; 
				}
			}
		}		
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event){
		int x = event.getBlock().getX();
		int y = event.getBlock().getY();
		int z = event.getBlock().getZ();

		if(x<=-39 || 39<=x || y<=47 || 80<=y || z<=-39 || 39<=z){
			event.setCancelled(true); return; }
		
		if(-6<=x && x<=6 && 63<=y && -6<=z && z<=6){
			event.setCancelled(true); return; }
		if(-22<=x && x<=-17 && 63<=y && -4<=z && z<=4){
			event.setCancelled(true); return; }
		if(23<=x && x<=24 && 64<=y && y<=65 && -7<=z && z<=-4){
			event.setCancelled(true); return; }
		if(23<=x && x<=24 && 64<=y && y<=65 && 4<=z && z<=7){
			event.setCancelled(true); return; }
		
		for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values()){
			if(house.westPlane<=x && x<=house.eastPlane
					&& house.bottomPlane<=y && y<=house.topPlane
					&& house.northPlane<=z && z<=house.southPlane){
				event.setCancelled(true);
				return; 
			}
		}
		
		for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values()){
			if(house.namePlateX==x && house.namePlateY==y && house.namePlateZ==z){
				event.setCancelled(true);
				return; 
			}
		}
	}
	
	private boolean clickSign(Player pl, Block block, int x, int y, int z, String command){
		if(block.getX()==x && block.getY()==y && block.getZ()==z){
			pl.performCommand(command);
			return true;
		}
		return false;
	}
	
	private void updateInventory(final Player pl, Village vil){
		Bukkit.getScheduler().runTaskLater(vil.plugin, new BukkitRunnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				pl.updateInventory();
			}
		}, 1);
	}
}
