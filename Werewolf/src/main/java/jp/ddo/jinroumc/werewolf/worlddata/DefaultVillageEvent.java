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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DefaultVillageEvent extends DefaultVillageData implements Listener {

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
			
			if(clickSign(pl, bl, 19, 64, -12, "myrole"))
				return;
			if(clickSign(pl, bl, 19, 64, -20, "myrole"))
				return;
			if(clickSign(pl, bl, 13, 64, -19, "myrole"))
				return;
			if(clickSign(pl, bl, 5, 64, -19, "myrole"))
				return;
			if(clickSign(pl, bl, -4, 64, -19, "myrole"))
				return;
			if(clickSign(pl, bl, -12, 64, -19, "myrole"))
				return;
			if(clickSign(pl, bl, -18, 64, -18, "myrole"))
				return;
			if(clickSign(pl, bl, -18, 64, -10, "myrole"))
				return;
			if(clickSign(pl, bl, 19, 64, 12, "myrole"))
				return;
			if(clickSign(pl, bl, 19, 64, 20, "myrole"))
				return;
			if(clickSign(pl, bl, 13, 64, 19, "myrole"))
				return;
			if(clickSign(pl, bl, 5, 64, 19, "myrole"))
				return;
			if(clickSign(pl, bl, -4, 64, 19, "myrole"))
				return;
			if(clickSign(pl, bl, -12, 64, 19, "myrole"))
				return;
			if(clickSign(pl, bl, -18, 64, 18, "myrole"))
				return;
			if(clickSign(pl, bl, -18, 64, 10, "myrole"))
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
			
			if(vp.alive && vil.status==VillageStatus.ongoing){
				if(vil.getAlivePlayerByColor(C.white)!=null && vp.color!=C.white
						&& clickSign(pl, bl, 3, 64, 0, "vote "+vil.getAlivePlayerByColor(C.white).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.gray)!=null && vp.color!=C.gray
						&& clickSign(pl, bl, 3, 64, -2, "vote "+vil.getAlivePlayerByColor(C.gray).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.d_gray)!=null && vp.color!=C.d_gray
						&& clickSign(pl, bl, 4, 65, -4, "vote "+vil.getAlivePlayerByColor(C.d_gray).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.black)!=null && vp.color!=C.black
						&& clickSign(pl, bl, 2, 64, -3, "vote "+vil.getAlivePlayerByColor(C.black).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.d_red)!=null && vp.color!=C.d_red
						&& clickSign(pl, bl, 0, 64, -3, "vote "+vil.getAlivePlayerByColor(C.d_red).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.red)!=null && vp.color!=C.red
						&& clickSign(pl, bl, -2, 64, -3, "vote "+vil.getAlivePlayerByColor(C.red).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.gold)!=null && vp.color!=C.gold
						&& clickSign(pl, bl, -4, 65, -4, "vote "+vil.getAlivePlayerByColor(C.gold).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.yellow)!=null && vp.color!=C.yellow
						&& clickSign(pl, bl, -3, 64, -2, "vote "+vil.getAlivePlayerByColor(C.yellow).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.green)!=null && vp.color!=C.green
						&& clickSign(pl, bl, -3, 64, 0, "vote "+vil.getAlivePlayerByColor(C.green).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.d_green)!=null && vp.color!=C.d_green
						&& clickSign(pl, bl, -3, 64, 2, "vote "+vil.getAlivePlayerByColor(C.d_green).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.d_aqua)!=null && vp.color!=C.d_aqua
						&& clickSign(pl, bl, -4, 65, 4, "vote "+vil.getAlivePlayerByColor(C.d_aqua).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.d_blue)!=null && vp.color!=C.d_blue
						&& clickSign(pl, bl, -2, 64, 3, "vote "+vil.getAlivePlayerByColor(C.d_blue).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.aqua)!=null && vp.color!=C.aqua
						&& clickSign(pl, bl, 0, 64, 3, "vote "+vil.getAlivePlayerByColor(C.aqua).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.blue)!=null && vp.color!=C.blue
						&& clickSign(pl, bl, 2, 64, 3, "vote "+vil.getAlivePlayerByColor(C.blue).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.d_purple)!=null && vp.color!=C.d_purple
						&& clickSign(pl, bl, 4, 65, 4, "vote "+vil.getAlivePlayerByColor(C.d_purple).getName()))
					return;
				if(vil.getAlivePlayerByColor(C.l_purple)!=null && vp.color!=C.l_purple
						&& clickSign(pl, bl, 3, 64, 2, "vote "+vil.getAlivePlayerByColor(C.l_purple).getName()))
					return;
			}
			
			if(bl.getType()==Material.CHEST
					&& !(bl.getX()==0 && bl.getY()==43 && bl.getZ()==6)
					&& (vil.status==VillageStatus.empty
					|| vil.status==VillageStatus.preparing
					|| vil.status==VillageStatus.recruiting)){
				event.setCancelled(true);
				return;
			}
			if(vil.status==VillageStatus.ongoing){
				/*if(bl.getX()==20 && bl.getY()==64 && bl.getZ()==13 && vp.color!=C.white){
					pl.sendMessage(C.red+"他のプレイヤーのチェストを開けることはできません。");
					event.setCancelled(true);
				}*/
				//(20, , -13);
				//(20, , -21);
				//(14, , -20);
				//(6, , -20);
				//(-5, , -20);
				//(-13, , -20);
				//(-19, , -19);
				//(-19, , -11);
				//(, , );
				//(, , );
				//(, , );
				//(, , );
				//(, , );
				//(, , );
				//(, , );
				//(, , );
					
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
				if(vp.color==C.white || vp.color==C.gray || vp.color==C.d_purple || vp.color==C.l_purple){
					if(to.getX()<18.5 || 23.5<to.getX()){
						pl.sendMessage(C.red+"Error: 夜に外へ出ることはできません。");
						event.setCancelled(true);
						return;
					}
				}
				if(vp.color==C.d_gray || vp.color==C.black || vp.color==C.d_red || vp.color==C.red){
					if(to.getZ()<-22.5 || -17.5<to.getZ()){
						pl.sendMessage(C.red+"Error: 夜に外へ出ることはできません。");
						event.setCancelled(true);
						return;
					}
				}
				if(vp.color==C.gold|| vp.color==C.yellow || vp.color==C.green || vp.color==C.d_green){
					if(to.getX()<-21.5 || -16.5<to.getX()){
						pl.sendMessage(C.red+"Error: 夜に外へ出ることはできません。");
						event.setCancelled(true);
						return;
					}
				}
				if(vp.color==C.d_aqua || vp.color==C.d_blue || vp.color==C.aqua || vp.color==C.blue){
					if(to.getZ()<18.5 || 23.5<to.getZ()){
						pl.sendMessage(C.red+"Error: 夜に外へ出ることはできません。");
						event.setCancelled(true);
						return;
					}
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
			
			if(-6<=x && x<=6 && 63<=y && -6<=z && z<=6){
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
			
			if(18<=x && x<=23 && 62<=y && y<=66 && -14<=z && z<=-9){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(18<=x && x<=23 && 62<=y && y<=66 && -22<=z && z<=-17){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(10<=x && x<=15 && 62<=y && y<=66 && -23<=z && z<=-18){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(2<=x && x<=7 && 62<=y && y<=66 && -23<=z && z<=-18){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(-6<=x && x<=-1 && 62<=y && y<=66 && -23<=z && z<=-18){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(-14<=x && x<=-9 && 62<=y && y<=66 && -23<=z && z<=-18){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(-22<=x && x<=-17 && 62<=y && y<=66 && -20<=z && z<=-15){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(-22<=x && x<=-17 && 62<=y && y<=66 && -12<=z && z<=-7){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(-22<=x && x<=-17 && 62<=y && y<=66 && 7<=z && z<=12){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(-22<=x && x<=-17 && 62<=y && y<=66 && 15<=z && z<=20){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(-14<=x && x<=-9 && 62<=y && y<=66 && 18<=z && z<=23){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(-6<=x && x<=-1 && 62<=y && y<=66 && 18<=z && z<=23){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(2<=x && x<=7 && 62<=y && y<=66 && 18<=z && z<=23){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(10<=x && x<=15 && 62<=y && y<=66 && 18<=z && z<=23){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(18<=x && x<=23 && 62<=y && y<=66 && 17<=z && z<=22){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			if(18<=x && x<=23 && 62<=y && y<=66 && 9<=z && z<=14){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); return; }
			
			/*
			 * 
			 * if(namePlateLocation)
			 * 		event.setCancelled(true);
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
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
			
			if(-6<=x && x<=6 && 63<=y && -6<=z && z<=6){
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
			
			if(18<=x && x<=23 && 62<=y && y<=66 && -14<=z && z<=-9){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(18<=x && x<=23 && 62<=y && y<=66 && -22<=z && z<=-17){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(10<=x && x<=15 && 62<=y && y<=66 && -23<=z && z<=-18){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(2<=x && x<=7 && 62<=y && y<=66 && -23<=z && z<=-18){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(-6<=x && x<=-1 && 62<=y && y<=66 && -23<=z && z<=-18){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(-14<=x && x<=-9 && 62<=y && y<=66 && -23<=z && z<=-18){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(-22<=x && x<=-17 && 62<=y && y<=66 && -20<=z && z<=-15){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(-22<=x && x<=-17 && 62<=y && y<=66 && -12<=z && z<=-7){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(-22<=x && x<=-17 && 62<=y && y<=66 && 7<=z && z<=12){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(-22<=x && x<=-17 && 62<=y && y<=66 && 15<=z && z<=20){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(-14<=x && x<=-9 && 62<=y && y<=66 && 18<=z && z<=23){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(-6<=x && x<=-1 && 62<=y && y<=66 && 18<=z && z<=23){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(2<=x && x<=7 && 62<=y && y<=66 && 18<=z && z<=23){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(10<=x && x<=15 && 62<=y && y<=66 && 18<=z && z<=23){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(18<=x && x<=23 && 62<=y && y<=66 && 17<=z && z<=22){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
			if(18<=x && x<=23 && 62<=y && y<=66 && 9<=z && z<=14){
				pl.sendMessage(C.red+"Error: このエリアは編集できません。");
				event.setCancelled(true); updateInventory(pl, vil); return; }
		}		
	}

	public static void updateInventory(final Player pl, Village vil){
		Bukkit.getScheduler().runTaskLater(vil.plugin, new BukkitRunnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				pl.updateInventory();
			}
		}, 1);
	}
}
