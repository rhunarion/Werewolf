package jp.ddo.jinroumc.werewolf.worlddata;

import java.util.HashMap;

import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillagePlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DefaultVillage extends Village {
	
	public DefaultVillage(String villageName, String villageType, JavaPlugin plugin){
		super(villageName, villageType, plugin);
	}
	
	public Location getSpawnLocation(){
		World world = Bukkit.getWorld(villageName);
		return new Location(world, 24.5, 63.0, 0.5, 90, 0); 
	}
	
	public Location getConfigRoom(){
		World world = Bukkit.getWorld(villageName);
		return new Location(world, 0.5, 43.0, 0.5); 
	}
	
	public Location getScaffold(){
		World world = Bukkit.getWorld(villageName);
		return new Location(world, -19.5, 70.5, 0.5, -90, 0); 
	}
	
	public boolean isInsideScaffold(Location loc){
		if(-21.0<=loc.getX() && loc.getX()<=-17.0
				&& 64.0<=loc.getY()
				&& -3.0<=loc.getZ() && loc.getZ()<=4.0)
			return true;
		return false;
	}
	
	public Location getHome(VillagePlayer vp){
		World world = Bukkit.getWorld(villageName);
		
		HashMap<ChatColor, DefaultVillageHouseCore> houseMap = DefaultVillageHouse.getHouseMap();
		DefaultVillageHouseCore house = houseMap.get(vp.color);
		return new Location(world, house.bedX+0.5, house.bedY+0.563, house.bedZ+0.5, house.bedPitch, 0);
	}
	
	public void writeSign(){
		for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values()){
			if(getAlivePlayerByColor(house.color)==null)
				continue;
			
			VillagePlayer vp = getAlivePlayerByColor(house.color);
			World world = Bukkit.getWorld(villageName);
			
			Sign voteSign = (Sign) world.getBlockAt(house.voteSignX, house.voteSignY, house.voteSignZ).getState();
			voteSign.setLine(0, vp.color+vp.getName());
			voteSign.setLine(1, "さんに投票する");
			voteSign.update();
			
			Sign namePlate = (Sign) world.getBlockAt(house.namePlateX, house.namePlateY, house.namePlateZ).getState();
			namePlate.setLine(1, vp.color+vp.getName());
			namePlate.setLine(2, "さんの家");
			namePlate.update();
			System.out.println("aあいうえお4");
		}
	}
	
	public void eraseSign(){
		World world = Bukkit.getWorld(villageName);
		
		for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values()){
			Sign voteSign = (Sign) world.getBlockAt(house.voteSignX, house.voteSignY, house.voteSignZ).getState();
			for(int i=0; i<4; i++)
				voteSign.setLine(i, "");
			voteSign.update();

			Sign namePlate = (Sign) world.getBlockAt(house.namePlateX, house.namePlateY, house.namePlateZ).getState();
			for(int i=0; i<4; i++)
				namePlate.setLine(i, "");
			namePlate.update();
		}
	}
	
	public void changeHouseEffect(){
		World world = Bukkit.getWorld(villageName);
		
		for(DefaultVillageHouseCore house : DefaultVillageHouse.getHouseMap().values()){

			BlockState frontDoor = world.getBlockAt(house.frontDoorX, house.frontDoorY, house.frontDoorZ).getState();
			((Openable) frontDoor.getData()).setOpen(false);
			frontDoor.update();
			
			BlockState rearDoor = world.getBlockAt(house.rearDoorX, house.rearDoorY, house.rearDoorZ).getState();
			((Openable) rearDoor.getData()).setOpen(false);
			rearDoor.update();
			
			BlockState leverState = world.getBlockAt(house.leverX, house.leverY, house.leverZ).getState();
			Lever lever = (Lever) leverState.getData();
			if(getAlivePlayerByColor(house.color)!=null)
				lever.setPowered(true);
			else
				lever.setPowered(false);
			leverState.update();
		}
	}
	
	public void preExecution(){
		for(int x=-21; x<-17; x++){
			for(int z=-3; z<4; z++){
				if(x==-21 && z==0) continue;
				Bukkit.getWorld(villageName).getBlockAt(x, 64, z).setType(Material.LAVA);
			}
		}
	}
	
	public void execution(){
		Bukkit.getWorld(villageName).getBlockAt(-20, 70, 0).setType(Material.AIR);
	}
	
	public void postExecution(){
		Bukkit.getWorld(villageName).getBlockAt(-20, 70, 0).setType(Material.STEP);
		for(int x=-21; x<-17; x++){
			for(int z=-3; z<4; z++){
				if(x==-21 && z==0) continue;
				Bukkit.getWorld(villageName).getBlockAt(x, 64, z).setType(Material.AIR);
			}
		}
	}
	
	public void finishFirework(){
		final Village vil = this;
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
	public void copyOriginal(){
		final Village vil = this;
		final World modifiedVillage = Bukkit.getWorld(villageName);
		final World defaultVillage = Bukkit.getWorld("default_village");
		vil.stopAsyncRebuild();

		vil.asyncRebuildId = Bukkit.getScheduler().runTaskTimer(vil.plugin, new BukkitRunnable(){
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
							if(def.getType()==Material.WALL_SIGN || def.getType()==Material.SIGN_POST){
								Sign modSign = (Sign) mod.getState();
								Sign defSign = (Sign) def.getState();
								for(int j=0; j<4; j++)
									modSign.setLine(j, defSign.getLine(j));
								modSign.update();
							}
							if(def.getType()==Material.CHEST){
								Chest modChest = (Chest) mod.getState();
								Chest defChest = (Chest) def.getState();
								modChest.getInventory().setContents(defChest.getInventory().getContents());
							}
							if(def.getType()==Material.FURNACE)
								((Furnace) mod.getState()).getInventory().clear(); 
							if(def.getType()==Material.JUKEBOX)
								((Jukebox) mod.getState()).eject();
							mod.getState().update();
						}
						x++;
						if(x>38){
							x = -38;
							y--;
							if(y<48){
								def = defaultVillage.getBlockAt(0, 43, 6);
								mod = modifiedVillage.getBlockAt(0, 43, 6);
								Chest modChest = (Chest) mod.getState();
								Chest defChest = (Chest) def.getState();
								modChest.getInventory().setContents(defChest.getInventory().getContents());
								
								def = defaultVillage.getBlockAt(1, 43, 5);
								mod = modifiedVillage.getBlockAt(1, 43, 5);
								mod.setType(def.getType());

								def = defaultVillage.getBlockAt(-1, 43, 5);
								mod = modifiedVillage.getBlockAt(-1, 43, 5);
								mod.setType(def.getType());

								def = defaultVillage.getBlockAt(-3, 43, 4);
								mod = modifiedVillage.getBlockAt(-3, 43, 4);
								mod.setType(def.getType());

								y = 69;
								secondTime = true;
							}
							break;
						}
					}
				}else{
					for(int i=0; i<20; i++){
						for(int z=-38; z<=38; z++){
							def = defaultVillage.getBlockAt(x, y, z);
							if(def.getType().isOccluding() && def.getType()!=Material.GRAVEL)
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
							if(y<55){
								for(Entity entity : modifiedVillage.getEntities())
									if(!(entity instanceof Player)
											&& !entity.getType().equals(EntityType.VILLAGER))
										entity.remove();
								for(int j=0; j<4; j++)
									Bukkit.getWorld(vil.villageName).spawnEntity(new Location
											(Bukkit.getWorld(vil.villageName), -32.5, 56.0, 12.5), EntityType.WOLF);
								vil.stopAsyncRebuild();
							}
							break;
						}
					}
				}
			}
		}, 0, 1).getTaskId();
	}
}
