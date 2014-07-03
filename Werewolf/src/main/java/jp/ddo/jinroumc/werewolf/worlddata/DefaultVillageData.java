package jp.ddo.jinroumc.werewolf.worlddata;

import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillagePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.Lever;
import org.bukkit.material.Openable;
import org.bukkit.scheduler.BukkitRunnable;

public class DefaultVillageData extends LocationData{
	public static Location getSpawnLocation(Village vil){
		World vilName = Bukkit.getWorld(vil.villageName);
		return new Location(vilName, 24.5, 63.0, 0.5, 90, 0); 
	}
	
	public static Location getConfigRoom(Village vil){
		World vilName = Bukkit.getWorld(vil.villageName);
		return new Location(vilName, 0.5, 43.0, 0.5); 
	}
	
	public static Location getScaffold(Village vil){
		World vilName = Bukkit.getWorld(vil.villageName);
		return new Location(vilName, -19.5, 70.5, 0.5, -90, 0); 
	}
	
	public static boolean isInsideScaffold(Location loc){
		if(-21.0<=loc.getX() && loc.getX()<=-17.0
				&& 64.0<=loc.getY()
				&& -3.0<=loc.getZ() && loc.getZ()<=4.0)
			return true;
		return false;
	}
	
	public static Location getHome(VillagePlayer vp, Village vil){
		World vilName = Bukkit.getWorld(vil.villageName);
		switch(vp.color){
		case WHITE: return new Location(vilName, 22.5, 63.563, -9.5, 90, 0);
		case GRAY: return new Location(vilName, 22.5, 63.563, -17.5, 90, 0);
		case DARK_GRAY: return new Location(vilName, 11.5, 63.563, -21.5, 0, 0); 
		case BLACK: return new Location(vilName, 3.5, 63.563, -21.5, 0, 0); 
		case DARK_RED: return new Location(vilName, -1.5, 63.563, -21.5, 0, 0); 
		case RED: return new Location(vilName, -9.5, 63.563, -21.5, 0, 0); 
		case GOLD: return new Location(vilName, -20.5, 63.563, -15.5, -90, 0); 
		case YELLOW: return new Location(vilName, -20.5, 63.563, -7.5, -90, 0); 
		case GREEN: return new Location(vilName, -20.5, 63.563, 8.5, -90, 0); 
		case DARK_GREEN: return new Location(vilName, -20.5, 63.563, 16.5, -90, 0); 
		case DARK_AQUA: return new Location(vilName, -9.5, 63.563, 22.5, 180, 0); 
		case DARK_BLUE: return new Location(vilName, -1.5, 63.563, 22.5, 180, 0); 
		case AQUA: return new Location(vilName, 3.5, 63.563, 22.5, 180, 0); 
		case BLUE: return new Location(vilName, 11.5, 63.563, 22.5, 180, 0); 
		case DARK_PURPLE: return new Location(vilName, 22.5, 63.563, 18.5, 90, 0);
		case LIGHT_PURPLE: return new Location(vilName, 22.5, 63.563, 10.5, 90, 0);
		}
		return null;
	}
	
	public static void writeSign(Village vil){
		for(VillagePlayer vp : vil.getJoiningPlayerList()){
			switch(vp.color){
			case WHITE: writeVoteSign(vp, 3, 64, 0); writeHomeSign(vp, 17, 64, -9); break;
			case GRAY: writeVoteSign(vp, 3, 64, -2); writeHomeSign(vp, 17, 64, -17); break;
			case DARK_GRAY: writeVoteSign(vp, 4, 65, -4); writeHomeSign(vp, 10, 64, -17); break;
			case BLACK: writeVoteSign(vp, 2, 64, -3); writeHomeSign(vp, 2, 64, -17); break;
			case DARK_RED: writeVoteSign(vp, 0, 64, -3); writeHomeSign(vp, -1, 64, -17); break;
			case RED: writeVoteSign(vp, -2, 64, -3); writeHomeSign(vp, -9, 64, -17); break;
			case GOLD: writeVoteSign(vp, -4, 65, -4); writeHomeSign(vp, -16, 64, -15); break;
			case YELLOW: writeVoteSign(vp, -3, 64, -2); writeHomeSign(vp, -16, 64, -7); break;
			case GREEN: writeVoteSign(vp, -3, 64, 0); writeHomeSign(vp, -16, 64, 7); break;
			case DARK_GREEN: writeVoteSign(vp, -3, 64, 2); writeHomeSign(vp, -16, 64, 15); break;
			case DARK_AQUA: writeVoteSign(vp, -4, 65, 4); writeHomeSign(vp, -9, 64, 17); break;
			case DARK_BLUE: writeVoteSign(vp, -2, 64, 3); writeHomeSign(vp, -1, 64, 17); break;
			case AQUA: writeVoteSign(vp, 0, 64, 3); writeHomeSign(vp, 2, 64, 17); break;
			case BLUE: writeVoteSign(vp, 2, 64, 3); writeHomeSign(vp, 10, 64, 17); break;
			case DARK_PURPLE: writeVoteSign(vp, 4, 65, 4); writeHomeSign(vp, 17, 64, 17); break;
			case LIGHT_PURPLE: writeVoteSign(vp, 3, 64, 2); writeHomeSign(vp, 17, 64, 9); break;
			}
		}
	}
	
	public static void writeVoteSign(VillagePlayer vp, int x, int y, int z){
		Sign sign = (Sign) Bukkit.getWorld(vp.village.villageName).getBlockAt(x, y, z).getState();
		sign.setLine(0, vp.color+vp.getName());
		sign.setLine(1, "さんに投票する");
		sign.update();
	}
	
	public static void writeHomeSign(VillagePlayer vp, int x, int y, int z){
		Sign sign = (Sign) Bukkit.getWorld(vp.village.villageName).getBlockAt(x, y, z).getState();
		sign.setLine(1, vp.color+vp.getName());
		sign.setLine(2, "さんの家");
		sign.update();
	}
	
	public static void eraseSign(Village vil){
		World world = Bukkit.getWorld(vil.villageName);
		eraseProvidedSign(world, 3, 64, 0);
		eraseProvidedSign(world, 3, 64, -2);
		eraseProvidedSign(world, 4, 65, -4);
		eraseProvidedSign(world, 2, 64, -3);
		eraseProvidedSign(world, 0, 64, -3);
		eraseProvidedSign(world, -2, 64, -3);
		eraseProvidedSign(world, -4, 65, -4);
		eraseProvidedSign(world, -3, 64, -2);
		eraseProvidedSign(world, -3, 64, 0);
		eraseProvidedSign(world, -3, 64, 2);
		eraseProvidedSign(world, -4, 65, 4);
		eraseProvidedSign(world, -2, 64, 3);
		eraseProvidedSign(world, 0, 64, 3);
		eraseProvidedSign(world, 2, 64, 3);
		eraseProvidedSign(world, 4, 65, 4);
		eraseProvidedSign(world, 3, 64, 2);
		eraseProvidedSign(world, 17, 64, -9);
		eraseProvidedSign(world, 17, 64, -17);
		eraseProvidedSign(world, 10, 64, -17);
		eraseProvidedSign(world, 2, 64, -17);
		eraseProvidedSign(world, -1, 64, -17);
		eraseProvidedSign(world, -9, 64, -17);
		eraseProvidedSign(world, -16, 64, -15);
		eraseProvidedSign(world, -16, 64, -7);
		eraseProvidedSign(world, -16, 64, 7);
		eraseProvidedSign(world, -16, 64, 15);
		eraseProvidedSign(world, -9, 64, 17);
		eraseProvidedSign(world, -1, 64, 17);
		eraseProvidedSign(world, 2, 64, 17);
		eraseProvidedSign(world, 10, 64, 17);
		eraseProvidedSign(world, 17, 64, 17);
		eraseProvidedSign(world, 17, 64, 9);
	}
	
	public static void eraseProvidedSign(World world, int x, int y, int z){
		Sign sign = (Sign) world.getBlockAt(x, y, z).getState();
		for(int i=0; i<4; i++)
			sign.setLine(i, "");
		sign.update();
	}
	
	public static void changeHouseEffect(Village vil){
		World world = Bukkit.getWorld(vil.villageName);
		
		changeDoorState(world, 18, 63, -10, false);
		changeDoorState(world, 23, 63, -13, false);
		changeDoorState(world, 18, 63, -18, false);
		changeDoorState(world, 23, 63, -21, false);
		changeDoorState(world, 11, 63, -18, false);
		changeDoorState(world, 14, 63, -23, false);
		changeDoorState(world, 3, 63, -18, false);
		changeDoorState(world, 6, 63, -23, false);
		changeDoorState(world, -2, 63, -18, false);
		changeDoorState(world, -5, 63, -23, false);
		changeDoorState(world, -10, 63, -18, false);
		changeDoorState(world, -13, 63, -23, false);
		changeDoorState(world, -17, 63, -16, false);
		changeDoorState(world, -22, 63, -19, false);
		changeDoorState(world, -17, 63, -8, false);
		changeDoorState(world, -22, 63, -11, false);
		changeDoorState(world, -17, 63, 8, false);
		changeDoorState(world, -22, 63, 11, false);
		changeDoorState(world, -17, 63, 16, false);
		changeDoorState(world, -22, 63, 19, false);
		changeDoorState(world, -10, 63, 18, false);
		changeDoorState(world, -13, 63, 23, false);
		changeDoorState(world, -2, 63, 18, false);
		changeDoorState(world, -5, 63, 23, false);
		changeDoorState(world, 3, 63, 18, false);
		changeDoorState(world, 6, 63, 23, false);
		changeDoorState(world, 11, 63, 18, false);
		changeDoorState(world, 14, 63, 23, false);
		changeDoorState(world, 18, 63, 18, false);
		changeDoorState(world, 23, 63, 21, false);
		changeDoorState(world, 18, 63, 10, false);
		changeDoorState(world, 23, 63, 13, false);

		boolean white = vil.getAlivePlayerByColor(C.white)!=null ? true : false;
		boolean gray = vil.getAlivePlayerByColor(C.gray)!=null ? true : false;
		boolean d_gray = vil.getAlivePlayerByColor(C.d_gray)!=null ? true : false;
		boolean black = vil.getAlivePlayerByColor(C.black)!=null ? true : false;
		boolean d_red = vil.getAlivePlayerByColor(C.d_red)!=null ? true : false;
		boolean red = vil.getAlivePlayerByColor(C.red)!=null ? true : false;
		boolean gold = vil.getAlivePlayerByColor(C.gold)!=null ? true : false;
		boolean yellow = vil.getAlivePlayerByColor(C.yellow)!=null ? true : false;
		boolean green = vil.getAlivePlayerByColor(C.green)!=null ? true : false;
		boolean d_green = vil.getAlivePlayerByColor(C.d_green)!=null ? true : false;
		boolean d_aqua = vil.getAlivePlayerByColor(C.d_aqua)!=null ? true : false;
		boolean d_blue = vil.getAlivePlayerByColor(C.d_blue)!=null ? true : false;
		boolean aqua = vil.getAlivePlayerByColor(C.aqua)!=null ? true : false;
		boolean blue = vil.getAlivePlayerByColor(C.blue)!=null ? true : false;
		boolean d_purple = vil.getAlivePlayerByColor(C.d_purple)!=null ? true : false;
		boolean l_purple = vil.getAlivePlayerByColor(C.l_purple)!=null ? true : false;
		
		changeLeverPower(world, 19, 65, -10, white);
		changeLeverPower(world, 19, 65, -18, gray);
		changeLeverPower(world, 11, 65, -19, d_gray);
		changeLeverPower(world, 3, 65, -19, black);
		changeLeverPower(world, -2, 65, -19, d_red);
		changeLeverPower(world, -10, 65, -19, red);
		changeLeverPower(world, -18, 65, -16, gold);
		changeLeverPower(world, -18, 65, -8, yellow);
		changeLeverPower(world, -18, 65, 8, green);
		changeLeverPower(world, -18, 65, 16, d_green);
		changeLeverPower(world, 11, 65, 19, d_aqua);
		changeLeverPower(world, 3, 65, 19, d_blue);
		changeLeverPower(world, -2, 65, 19, aqua);
		changeLeverPower(world, -10, 65, 19, blue);
		changeLeverPower(world, 19, 65, 18, d_purple);
		changeLeverPower(world, 19, 65, 10, l_purple);
	}
	
	public static void changeDoorState(World world, int x, int y, int z, boolean isOpen){
		BlockState state = world.getBlockAt(x, y, z).getState();
		Openable door = (Openable) state.getData();
		if(isOpen)
			door.setOpen(true);
		else
			door.setOpen(false);
		state.update();
	}
	
	public static void changeLeverPower(World world, int x, int y, int z, boolean power){
		BlockState state = world.getBlockAt(x, y, z).getState();
		Lever lever = (Lever) state.getData();
		if(power)
			lever.setPowered(true);
		else
			lever.setPowered(false);
		state.setData(lever);
		state.update();
	}
	
	public static void preExecution(Village vil){
		for(int x=-21; x<-17; x++){
			for(int z=-3; z<4; z++){
				if(x==-21 && z==0) continue;
				Bukkit.getWorld(vil.villageName).getBlockAt(x, 64, z).setType(Material.LAVA);
			}
		}
	}
	
	public static void execution(Village vil){
		Bukkit.getWorld(vil.villageName).getBlockAt(-20, 70, 0).setType(Material.AIR);
	}
	
	public static void postExecution(Village vil){
		Bukkit.getWorld(vil.villageName).getBlockAt(-20, 70, 0).setType(Material.STEP);
		for(int x=-21; x<-17; x++){
			for(int z=-3; z<4; z++){
				if(x==-21 && z==0) continue;
				Bukkit.getWorld(vil.villageName).getBlockAt(x, 64, z).setType(Material.AIR);
			}
		}
	}
	
	public static void finishFirework(final Village vil){
		
		final Builder effect = FireworkEffect.builder();
		effect.with(FireworkEffect.Type.BALL_LARGE);
		effect.trail(true);

		switch(vil.result){
		case draw:
			effect.withColor(Color.LIME);
			effect.withFade(Color.GREEN);
			break;
		case murabito:
			effect.withColor(Color.AQUA);
			effect.withFade(Color.TEAL);
			break;
		case jinrou:
			effect.withColor(Color.RED);
			effect.withFade(Color.MAROON);
			break;
		case youko:
			effect.withColor(Color.YELLOW);
			effect.withFade(Color.OLIVE);
			break;
		}
		
		final World world = Bukkit.getWorld(vil.villageName);
		final Location[] loc = {new Location(world, 8, 68, 8),
								new Location(world, -8, 68, 8),
								new Location(world, -8, 68, -8),
								new Location(world, 8, 68, -8)};
		
		Firework firework = world.spawn(loc[0], Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffect(effect.build());
		meta.setPower(0);
		firework.setFireworkMeta(meta);
		
		Bukkit.getScheduler().runTaskLater(vil.plugin, new BukkitRunnable(){
			public void run(){
				Firework firework = world.spawn(loc[1], Firework.class);
				FireworkMeta meta = firework.getFireworkMeta();
				meta.addEffect(effect.build());
				meta.setPower(0);
				firework.setFireworkMeta(meta);
			}
		}, 5);
		
		Bukkit.getScheduler().runTaskLater(vil.plugin, new BukkitRunnable(){
			public void run(){
				Firework firework = world.spawn(loc[2], Firework.class);
				FireworkMeta meta = firework.getFireworkMeta();
				meta.addEffect(effect.build());
				meta.setPower(0);
				firework.setFireworkMeta(meta);
			}
		}, 10);
		
		Bukkit.getScheduler().runTaskLater(vil.plugin, new BukkitRunnable(){
			public void run(){
				effect.flicker(true);
				Firework firework = world.spawn(loc[3], Firework.class);
				FireworkMeta meta = firework.getFireworkMeta();
				meta.addEffect(effect.build());
				meta.setPower(0);
				firework.setFireworkMeta(meta);
			}
		}, 15);
	}

	@SuppressWarnings("deprecation")
	public static void copyOriginal(final Village vil){
		final World modifiedVillage = Bukkit.getWorld(vil.villageName);
		final World defaultVillage = Bukkit.getWorld("default_village");
		vil.stopAsyncRebuild();

		vil.asyncRebuildID = Bukkit.getScheduler().runTaskTimer(vil.plugin, new BukkitRunnable(){
			int y = 79;
			int x = -38;
			boolean secondTime = false;
			
			@Override
			public void run(){
				Block mod = null;
				Block def = null;
				
				if(!secondTime){
					for(int i=0; i<20; i++){
						for(int z=-38; z<=38; z++){
							def = defaultVillage.getBlockAt(x, y, z);
							mod = modifiedVillage.getBlockAt(x, y, z);
							mod.setType(def.getType());
							mod.setData(def.getData());
							if(def.getType().equals(Material.CHEST)){
								Chest modChest = (Chest) mod.getState();
								Chest defChest = (Chest) def.getState();
								modChest.getInventory().setContents(defChest.getInventory().getContents());
							}
							if(def.getType().equals(Material.FURNACE))
								((Furnace) mod.getState()).getInventory().clear(); 
							if(def.getType().equals(Material.JUKEBOX))
								((Jukebox) mod.getState()).eject();
							mod.getState().update();
						}
						x++;
						if(x>38){
							x = -38;
							y--;
							if(y<41){
								y = 79;
								secondTime = true;
							}
							break;
						}
					}
				}else{
					for(int i=0; i<20; i++){
						for(int z=-38; z<=38; z++){
							def = defaultVillage.getBlockAt(x, y, z);
							if(def.getType().isOccluding() && !def.getType().equals(Material.GRAVEL))
								continue;
							mod = modifiedVillage.getBlockAt(x, y, z);
							mod.setType(def.getType());
							mod.setData(def.getData());
							mod.getState().update();
						}
						x++;
						if(x>38){
							x = -38;
							y--;
							if(y<41){
								for(Entity entity : modifiedVillage.getEntities())
									if(!(entity instanceof Player)
											&& !entity.getType().equals(EntityType.VILLAGER))
										entity.remove();
								for(int j=0; j<4; j++)
									Bukkit.getWorld(vil.villageName).spawnEntity(new Location
											(Bukkit.getWorld(vil.villageName), -32.5, 55.0, 12.5), EntityType.WOLF);
								vil.stopAsyncRebuild();
							}
							break;
						}
					}
				}
			}
		}, 1, 1).getTaskId();
	}
}
