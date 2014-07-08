package jp.ddo.jinroumc.werewolf.village;

import java.util.Collections;
import java.util.List;

import jp.ddo.jinroumc.werewolf.enumconstant.VillageRole;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.ddo.jinroumc.werewolf.enumconstant.VillageTime;
import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.util.PluginChecker;
import jp.ddo.jinroumc.werewolf.worlddata.DefaultVillage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.MobDisguise;

public class VillagePlayer extends VillagePlayerCore {
	
	VillagePlayer(Player pl){
		super(pl);
	}
	
	VillagePlayer(OfflinePlayer pl, Village vil){
		super(pl, vil);
	}
	
	public void teleportToConfigRoom(){
		getPlayer().teleport(((DefaultVillage) village).getConfigRoom());
	}
	
	public void teleportToScaffold(){
		if(connection)
			getPlayer().teleport(((DefaultVillage) village).getScaffold());
		else
			villagerEntity.teleport(((DefaultVillage) village).getScaffold());
	}
	
	public void teleportToHome(){
		if(!alive || village.status!=VillageStatus.ongoing)
			VillageUtil.teleportToVillage(getPlayer(), village);
		else if(alive && connection)
			getPlayer().teleport(((DefaultVillage) village).getHome(this));
		else
			villagerEntity.teleport(((DefaultVillage) village).getHome(this));
	}

	public void giveUpGame(){
		connection = false;

		if(joining && village.status==VillageStatus.ongoing){
			if(alive){
				village.sendToVillage(color+getName()
						+C.gold+" さんがゲームを放棄して去っていきました。");
				village.scoreboard.resetScores(Bukkit.getOfflinePlayer(getColorName()));

				player = Bukkit.getOfflinePlayer("."+getName());
				spawnVillager();
				if(village.status==VillageStatus.ongoing && village.time==VillageTime.night)
					setFenceAroundBed();
				
				village.objective.getScore(Bukkit.getOfflinePlayer(getColorName())).setScore(1);
				village.objective.getScore(Bukkit.getOfflinePlayer(getColorName())).setScore(0);
	
				village.sendToVillage(C.gold+"生まれ変わりらしき "+color+getName()
						+C.gold+" さんがふらりとやってきました。");
			}else{
				player = Bukkit.getOfflinePlayer("."+getName());
			}
		}

		if(gameMaster && village.status!=VillageStatus.finishing){
			List<VillagePlayer> vpList = village.getAlivePlayerListExceptNpc();
			
			if(vpList.size()==0){
				village.sendToVillage(C.gold+"参加者がいなくなったため強制終了します。");
				village.gameFinishing();
			}else{
				Collections.shuffle(vpList);
				removeGm();
				vpList.get(0).setGm();
			}
		}
		
		if(village.status==VillageStatus.preparing || village.status==VillageStatus.recruiting){
			village.scoreboard.resetScores(player);
			VillageUtil.removePlayer(player, village);
		}else if(village.status==VillageStatus.finishing){
			VillageUtil.removePlayer(player, village);
		}
	}
	
	public void setGm(){
		gameMaster = true;
		if(alive && village.status==VillageStatus.ongoing)
			village.sendToVillage(color+getName()+C.gold+" さんがゲームマスターになりました。");
		else
			village.sendToVillage(C.yellow+getName()+C.gold+" さんがゲームマスターになりました。");
	}
	
	public void removeGm(){
		gameMaster = false;
		if(alive && village.status==VillageStatus.ongoing)
			village.sendToVillage(color+getName()+C.gold+" さんがゲームマスターではなくなりました。");
		else
			village.sendToVillage(C.yellow+getName()+C.gold+" さんがゲームマスターではなくなりました。");
	}

	public void showRule(){
		sendMessage(C.gold+"////////// "+C.yellow
				+"Rule"+C.gold+" //////////");
		String rule = "";
		rule += C.gold+"=="+C.yellow+"村の説明:"+C.gold+village.description;
		rule += "=="+C.yellow+"配役:"
				+C.gold+"村人"+(village.maxNum-village.getTotalRoleNumInRuleExceptMurabito())+"人、"
				+"占い師"+village.uranaiNum+"人、霊媒師"+village.reibaiNum+"人、"
				+"狩人"+village.kariudoNum+"人、人狼"+village.jinrouNum+"人、"
				+"狂人"+village.kyoujinNum+"人、妖狐"+village.youkoNum+"人、"
				+"合計"+village.maxNum+"人";
		rule += "=="+C.yellow+"詳細:"+C.gold+"昼時間"
				+village.dayTime+"秒、夜時間"+village.nightTime+"秒、パスワード";
		rule += village.setPassword ? "あり" : "なし";
		rule += "、役職希望";
		rule += village.requestRole ? "あり" : "なし";
		rule += "、未投票者のランダム投票";
		rule += village.randomVote ? "あり" : "なし";
		rule += "、再投票回数"+village.revoteNum+"回、/"+PluginChecker.getWw()+"whispコマンド";
		rule += village.permitWhisp ? "あり" : "なし";
		rule += "、";
		rule += village.reishiAllPlayers ? "全死亡プレイヤーの霊視" : "処刑者のみの霊視";
		rule += "、/"+PluginChecker.getWw()+"biteコマンド";
		rule += village.permitBite ? "あり" : "なし";
		sendMessage(rule);
	}

	public void joinGame(){
		joining = true;
		village.objective.getScore(player).setScore(1);
		village.objective.getScore(player).setScore(0);
		village.sendToVillage(C.yellow+getName()+C.gold+" さんがゲームに参加しました。");
	}
	
	public void unjoinGame(){
		joining=false;
		role=VillageRole.none;
		roleRequested=VillageRole.none;
		village.scoreboard.resetScores(player);
		village.sendToVillage(C.yellow+getName()+C.gold+" さんがゲームへの参加を辞めました。");
	}
	
	public void setRole(VillageRole role){
		this.role = role;
	}
	
	public void unsetRole(){
		role = VillageRole.none;
	}
	
	public void setRequestedRole(VillageRole role){
		roleRequested = role;
	}
	
	public void kick(){
		Player pl = getPlayer();
		village.kickedPlayerList.add(pl);
		VillageUtil.onPlayerLeave(pl);
		VillageUtil.teleportToLobby(pl);
		pl.sendMessage(C.gold+"ゲームマスターに村から追い出されました。");
	}
	
	public void kill(){
		alive = false;
		numBeingVoted = 0;
		votedPlayer = null;
		guardPlayer = null;
		tryUranai = false;
		Location loc = null;
		
		if(connection){
			addGhostTeam();
			undisguise();
			loc = getPlayer().getLocation();
		}else{
			removeFenceAroundBed();
			loc = villagerEntity.getLocation();
		}

		ItemStack skull = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skull.setDurability((short) 3);			
		skullMeta.setDisplayName(getName()+" の頭");
		skullMeta.setOwner(getName());
		skull.setItemMeta(skullMeta);
		
		ItemStack bone = new ItemStack(Material.BONE, 5);
		ItemMeta boneMeta = (ItemMeta) bone.getItemMeta();
		boneMeta.setDisplayName(getName()+" の骨");
		bone.setItemMeta(boneMeta);
		
		ItemStack flesh = new ItemStack(Material.RAW_BEEF, 5);
		ItemMeta fleshMeta = (ItemMeta) flesh.getItemMeta();
		fleshMeta.setDisplayName(getName()+" の肉");
		flesh.setItemMeta(fleshMeta);
		
		ItemStack upperLether = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		ItemMeta upperLetherMeta = (ItemMeta) upperLether.getItemMeta();
		upperLetherMeta.setDisplayName(getName()+" の上半身の皮");
		upperLether.setItemMeta(upperLetherMeta);
		
		ItemStack lowerLether = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemMeta lowerLetherMeta = (ItemMeta) lowerLether.getItemMeta();
		lowerLetherMeta.setDisplayName(getName()+" の下半身の皮");
		lowerLether.setItemMeta(lowerLetherMeta);

		World world = Bukkit.getWorld(village.villageName);
		world.dropItem(loc, skull);
		world.dropItem(loc, bone);
		world.dropItem(loc, flesh);
		world.dropItem(loc, upperLether);
		world.dropItem(loc, lowerLether);
	}
	
	public void giveDeathDamage(){
		if(connection)
			getPlayer().damage(20.0);
		else
			villagerEntity.damage(20.0);
	}

	public void strikeThroughPlayerName(){
		village.scoreboard.resetScores(Bukkit.getOfflinePlayer(getColorName()));
		village.objective.getScore(Bukkit.getOfflinePlayer(getStrikethroughName())).setScore(1);
		village.objective.getScore(Bukkit.getOfflinePlayer(getStrikethroughName())).setScore(0);
	}
	
	public void addGhostTeam(){
		if(getName().contains("."))
			return;
		
		Player pl = getPlayer();
		village.ghostTeam.addPlayer(pl);
		addInvisibleEffect();
		pl.setAllowFlight(true);
		pl.setFlying(true);
		
		for(VillagePlayer alive : village.getAlivePlayerListExceptNpc())
			alive.getPlayer().hidePlayer(pl);
		for(VillagePlayer ghost : village.getPlayerListExceptAliveWhileOngoing())
			ghost.getPlayer().showPlayer(pl);
		for(VillagePlayer pc : village.getPlayerListExceptNpc())
			pl.showPlayer(pc.getPlayer());
	}
	
	public void addInvisibleEffect(){
		if(getName().contains("."))
			return;
		
		Player pl = getPlayer();
		pl.removePotionEffect(PotionEffectType.INVISIBILITY);
		pl.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
	}

	public void sendMessageToSpecifiedPlayer(VillagePlayer receiver, String message){
		String[] splitList = message.split("\\\\", -1);
		for(String split : splitList){
			sendMessage(C.blue+"["+getName()+" with whisp] "+split);
			receiver.sendMessage(C.blue+"["+getName()+" with whisp] "+split);
		}
	}
	
	public void sendMessageWithShout(String message){
		String[] splitList = message.split("\\\\", -1);
		for(String split : splitList){
			for(VillagePlayer vp : village.getPlayerListExceptNpc())
				vp.sendMessage(C.d_aqua+"["+getName()+" with shout] "+split);
		}
	}

	public void sendMessageWithCO(String message){
		String[] splitList = message.split("\\\\", -1);
		for(String split : splitList){
			for(VillagePlayer vp : village.getPlayerListExceptNpc())
				vp.sendMessage(C.l_purple+"["+getName()+" with co] "+split);
		}
		
		if(alive
				&& village.status==VillageStatus.ongoing && village.time==VillageTime.day){
			int score = village.objective.getScore(Bukkit.getOfflinePlayer(getColorName())).getScore();
			score ++;
			village.objective.getScore(Bukkit.getOfflinePlayer(getColorName())).setScore(score);
		}
	}

	public void showMyRole(){
		if(role==VillageRole.murabito){
			sendMessage(C.gold+"あなたは "+VillageUtil.getVillageRoleInJapanese(VillageRole.murabito)
								+C.gold+" です。");
			sendMessage(C.gold+"村に紛れ込んだ人狼をすべて見つけ出し、村人チームを勝利に導きましょう。");
		}
		else if(role==VillageRole.uranaishi){
			sendMessage(C.gold+"あなたは "+VillageUtil.getVillageRoleInJapanese(VillageRole.uranaishi)
								+C.gold+" です。");
			sendMessage(C.yellow+"/"+PluginChecker.getWw()+"uranai <player>"
							+C.gold+" とコマンドすることで夜に指定したプレイヤーを一人だけ占うことができます。");
		}
		else if(role==VillageRole.reibaishi){
			sendMessage(C.gold+"あなたは "+VillageUtil.getVillageRoleInJapanese(VillageRole.reibaishi)
								+C.gold+" です。");
			sendMessage(C.gold+"命を落としたプレイヤーの正体を知ることができます。");
		}
		else if(role==VillageRole.kariudo){
			sendMessage(C.gold+"あなたは "+VillageUtil.getVillageRoleInJapanese(VillageRole.kariudo)
								+C.gold+" です。");
			sendMessage(C.yellow+"/"+PluginChecker.getWw()+"guard <player>"
							+C.gold+" とコマンドすることで昼に指定したプレイヤーを一人だけ人狼から護衛することができます。");
		}
		else if(role==VillageRole.jinrou){
			sendMessage(C.gold+"あなたは "+VillageUtil.getVillageRoleInJapanese(VillageRole.jinrou)
								+C.gold+" です。");
			if(village.getSettedRoleNum(VillageRole.jinrou)>=2){
				String message = "";
				message += C.gold+"この村の人狼は "+C.d_red+village.getJinrouList().get(0).getName();
				for(int i=1; i<village.getJinrouList().size(); i++)
					message += C.gold+", "+C.d_red+village.getJinrouList().get(i).getName();
				sendMessage(message+C.gold+" です。");
			}
			if(village.permitBite)
				sendMessage(C.yellow+"/"+PluginChecker.getWw()+"bite <player>"
							+C.gold+" とコマンドすることで夜に村人を一人だけ噛み殺すことができます。");
			else
				sendMessage(C.gold+"夜に村人を一人噛み殺すことができます。");
		}
		else if(role==VillageRole.kyoujin){
			sendMessage(C.gold+"あなたは "+VillageUtil.getVillageRoleInJapanese(VillageRole.kyoujin)
								+C.gold+" です。");
			sendMessage(C.gold+"村人を混乱させ、人狼チームを勝利に導きましょう");
		}
		else if(role==VillageRole.youko){
			sendMessage(C.gold+"あなたは "+VillageUtil.getVillageRoleInJapanese(VillageRole.youko)
								+C.gold+" です。");
			if(village.getSettedRoleNum(VillageRole.youko)>=2){
				String message = "";
				message += C.gold+"この村の妖狐は "+C.yellow+village.getYoukoList().get(0).getName();
				for(int i=1; i<village.getYoukoList().size(); i++)
					message += C.gold+", "+C.yellow+village.getYoukoList().get(i).getName();
				sendMessage(message+C.gold+" です。");
			}
			sendMessage(C.gold+"村人と人狼の戦いが終わるまで、正体を隠しながら生き残りましょう。");
		}
	}
	
	public void votePlayer(VillagePlayer target){
		votedPlayer = target;
	}
	
	public void uranaiPlayer(VillagePlayer target){
		sendMessage(C.green+"占いを行った結果 "+target.color+target.getName()+C.green+" さんの正体は "
				+VillageUtil.getTrueRole(target)+C.green+" だとわかりました。");
		tryUranai = true;
		if(target.role==VillageRole.youko){
			village.cursedPlayerList.add(target);
		}
	}

	public void guardPlayer(VillagePlayer target){
		guardPlayer = target;
	}
	
	public void bitePlayer(final VillagePlayer target){
		village.tryBiting = true;
		for(VillagePlayer kariudo : village.getKariudoList()){
			if(kariudo.guardPlayer==target){
				village.sendToVillage(C.green+"どこからか矢を放つ音と、狼の悲鳴が聞こえてきました。");
				for(VillagePlayer jinrou : village.getAliveJinrouListExceptNpc())
					jinrou.sendMessage(color+getName()+C.gold+" さんが "+target.color+target.getName()
							+C.gold+" さんを噛み殺そうとしましたが、 "
							+C.aqua+"狩人"+C.gold+" に護衛されていたようです。");
				
				Location loc = null;
				for(VillagePlayer bitable : village.getPlayerListExceptAliveJinrouAndNpc()){
					if(bitable==target)
						continue;
					loc = new Location(Bukkit.getWorld(village.villageName), 0, 64, 0);
					bitable.playSoundAtModifiedPoint(loc, Sound.SHOOT_ARROW, 1f);
				}
				Bukkit.getScheduler().runTaskLater(village.plugin, new BukkitRunnable(){
					@Override
					public void run(){
						for(VillagePlayer bitable : village.getPlayerListExceptAliveJinrouAndNpc()){
							if(bitable==target)
								continue;
							Location loc = new Location(Bukkit.getWorld(village.villageName), 0, 64, 0);
							bitable.playSoundAtModifiedPoint(loc, Sound.WOLF_HURT, 0f);
						}
					}
				}, 10);
				
				if(target.connection)
					loc = target.getPlayer().getLocation();
				else
					loc = target.villagerEntity.getLocation();
				for(VillagePlayer jinrou : village.getAliveJinrouListExceptNpc())
					jinrou.playSoundAtModifiedPoint(loc, Sound.SHOOT_ARROW, 1f);
				if(target.connection)
					target.playSoundAtModifiedPoint(loc, Sound.SHOOT_ARROW, 1f);
				
				final VillagePlayer bitingPlayer = this;
				final Location bitingLoc;
				if(connection)
					bitingLoc = getPlayer().getLocation();
				else
					bitingLoc = loc;
				Bukkit.getScheduler().runTaskLater(village.plugin, new BukkitRunnable(){
					@Override
					public void run(){
						for(VillagePlayer jinrou : village.getAliveJinrouListExceptNpc())
							jinrou.playSoundAtModifiedPoint(bitingLoc, Sound.WOLF_HURT, 0f);
						if(target.connection)
							target.playSoundAtModifiedPoint(bitingLoc, Sound.WOLF_HURT, 0f);
						
						bitingPlayer.giveDeathDamage();
					}
				}, 10);

				return;
			}
		}
		if(target.role==VillageRole.youko){
			village.sendToVillage(C.green+"どこからか狐の悲鳴が聞こえてきました。");
			target.disguiseBlaze();
			for(VillagePlayer jinrou : village.getAliveJinrouListExceptNpc())
				jinrou.sendMessage(color+getName()+C.gold+" さんが "+target.color+target.getName()
						+C.gold+" さんを噛み殺そうとしましたが、 "
						+C.yellow+"妖狐"+C.gold+" が人間に化けていたようです。");

			Location loc = null;
			for(VillagePlayer bitable : village.getPlayerListExceptAliveJinrouAndNpc()){
				if(bitable==target)
					continue;
				loc = new Location(Bukkit.getWorld(village.villageName), 0, 64, 0);
				bitable.playSoundAtModifiedPoint(loc, Sound.GHAST_SCREAM2, 1.5f);
			}
			if(target.connection)
				loc = target.getPlayer().getLocation();
			else
				loc = target.villagerEntity.getLocation();
			for(VillagePlayer jinrou : village.getAliveJinrouListExceptNpc())
				jinrou.playSoundAtModifiedPoint(loc, Sound.GHAST_SCREAM2, 1.5f);
			if(target.connection)
				target.playSoundAtModifiedPoint(loc, Sound.GHAST_SCREAM2, 1.5f);
			
			return;
		}
		
		village.bittenPlayer = target;
		village.sendToVillage(C.green+"どこからか人間の断末魔の叫びが聞こえてきました。");
		target.kill();
		target.giveDeathDamage();
		if(target.connection)
			target.sendMessage(C.gold+"あなたは人狼に噛み殺されました。");
		for(VillagePlayer jinrou : village.getAliveJinrouListExceptNpc())
			jinrou.sendMessage(color+getName()+C.gold+" さんが "+target.color+target.getName()
						+C.gold+" さんを噛み殺しました。");

		Location loc = null;
		for(VillagePlayer bitable : village.getPlayerListExceptAliveJinrouAndNpc()){
			loc = new Location(Bukkit.getWorld(village.villageName), 0, 64, 0);
			bitable.playSoundAtModifiedPoint(loc,Sound.ENDERMAN_DEATH, 0.5f);
		}
		if(target.connection)
			loc = target.getPlayer().getLocation();
		else
			loc = target.villagerEntity.getLocation();
		for(VillagePlayer jinrou : village.getAliveJinrouListExceptNpc())
			jinrou.playSoundAtModifiedPoint(loc, Sound.ENDERMAN_DEATH, 0.5f);
		if(target.connection)
			target.playSoundAtModifiedPoint(loc, Sound.ENDERMAN_DEATH, 0.5f);
	}
	
	public void playSoundAtModifiedPoint(Location soundLoc, Sound sound, float pitch){
		Location playerLoc = getPlayer().getLocation();
		double diffX = soundLoc.getX()-playerLoc.getX();
		double diffZ = soundLoc.getZ()-playerLoc.getZ();
		double diff = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffZ, 2));

		if(diff>12.0){
			soundLoc.setX(playerLoc.getX()+10*diffX/diff);
			soundLoc.setZ(playerLoc.getZ()+10*diffZ/diff);
		}
		getPlayer().playSound(soundLoc, sound, 0.5f, pitch);
	}
	
	public void changeStatusOnGameFinish(){
		Player pl = getPlayer();
		for(VillagePlayer pc : village.getPlayerListExceptNpc())
			pl.showPlayer(pc.getPlayer());
		pl.removePotionEffect(PotionEffectType.INVISIBILITY);
		pl.setAllowFlight(true);
		pl.setFlying(true);
		undisguise();
	}

	public void spawnVillager(){
		if(role==VillageRole.jinrou
				&& village.status==VillageStatus.ongoing && village.time==VillageTime.night){
			villagerEntity = (LivingEntity) Bukkit.getWorld(village.villageName)
					.spawnEntity(((DefaultVillage) village).getHome(this), EntityType.ZOMBIE);
		}else{
			villagerEntity = (LivingEntity) Bukkit.getWorld(village.villageName)
					.spawnEntity(((DefaultVillage) village).getHome(this), EntityType.VILLAGER);
			villagerEntity.setCustomName(getName());
			villagerEntity.setCustomNameVisible(true);
			((Villager) villagerEntity).setProfession(proffession);
		}
	}
	
	public void setFenceAroundBed(){
		Location home = ((DefaultVillage) village).getHome(this);
		World world = home.getWorld();
		int x = home.getX()>=0 ? (int) home.getX() : (int) home.getX()-1;
		int y = (int) home.getY()+2;
		int z = home.getZ()>=0 ? (int) home.getZ() : (int) home.getZ()-1;
		
		if(world.getBlockAt(x+1, y, z).getType()==Material.AIR)
			world.getBlockAt(x+1, y, z).setType(Material.FENCE);
		if(world.getBlockAt(x+1, y, z+1).getType()==Material.AIR)
			world.getBlockAt(x+1, y, z+1).setType(Material.FENCE);
		if(world.getBlockAt(x, y, z+1).getType()==Material.AIR)
			world.getBlockAt(x, y, z+1).setType(Material.FENCE);
		if(world.getBlockAt(x-1, y, z+1).getType()==Material.AIR)
			world.getBlockAt(x-1, y, z+1).setType(Material.FENCE);
		if(world.getBlockAt(x-1, y, z).getType()==Material.AIR)
			world.getBlockAt(x-1, y, z).setType(Material.FENCE);
		if(world.getBlockAt(x-1, y, z-1).getType()==Material.AIR)
			world.getBlockAt(x-1, y, z-1).setType(Material.FENCE);
		if(world.getBlockAt(x, y, z-1).getType()==Material.AIR)
			world.getBlockAt(x, y, z-1).setType(Material.FENCE);
		if(world.getBlockAt(x+1, y, z-1).getType()==Material.AIR)
			world.getBlockAt(x+1, y, z-1).setType(Material.FENCE);
	}
	
	public void removeFenceAroundBed(){
		Location home = ((DefaultVillage) village).getHome(this);
		World world = home.getWorld();
		int x = home.getX()>=0 ? (int) home.getX() : (int) home.getX()-1;
		int y = (int) home.getY()+2;
		int z = home.getZ()>=0 ? (int) home.getZ() : (int) home.getZ()-1;
		
		if(world.getBlockAt(x+1, y, z).getType()==Material.FENCE)
			world.getBlockAt(x+1, y, z).setType(Material.AIR);
		if(world.getBlockAt(x+1, y, z+1).getType()==Material.FENCE)
			world.getBlockAt(x+1, y, z+1).setType(Material.AIR);
		if(world.getBlockAt(x, y, z+1).getType()==Material.FENCE)
			world.getBlockAt(x, y, z+1).setType(Material.AIR);
		if(world.getBlockAt(x-1, y, z+1).getType()==Material.FENCE)
			world.getBlockAt(x-1, y, z+1).setType(Material.AIR);
		if(world.getBlockAt(x-1, y, z).getType()==Material.FENCE)
			world.getBlockAt(x-1, y, z).setType(Material.AIR);
		if(world.getBlockAt(x-1, y, z-1).getType()==Material.FENCE)
			world.getBlockAt(x-1, y, z-1).setType(Material.AIR);
		if(world.getBlockAt(x, y, z-1).getType()==Material.FENCE)
			world.getBlockAt(x, y, z-1).setType(Material.AIR);
		if(world.getBlockAt(x+1, y, z-1).getType()==Material.FENCE)
			world.getBlockAt(x+1, y, z-1).setType(Material.AIR);
	}
	
	public void disguiseZombie(){
		if(getName().contains(".") && !getName().contains(".."))
			return;

		if(connection){
			DisguiseAPI api = Bukkit.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
			api.disguiseToAll(getPlayer(), new MobDisguise(DisguiseType.ZOMBIE, true));
		}else{
			villagerEntity.remove();
			spawnVillager();
		}
	}
	
	public void disguiseBlaze(){
		if(getName().contains(".") && !getName().contains(".."))
			return;

		if(connection){
			DisguiseAPI api = Bukkit.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
			api.disguiseToAll(getPlayer(), new MobDisguise(DisguiseType.BLAZE, true));
		}else{
			Location loc = villagerEntity.getLocation();
			villagerEntity.remove();
			villagerEntity = (LivingEntity) Bukkit.getWorld(village.villageName)
						.spawnEntity(loc, EntityType.BLAZE);
		}
	}
	
	public void undisguise(){
		if(getName().contains(".") && !getName().contains(".."))
			return;

		DisguiseAPI api = Bukkit.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
		if(connection){
			if(api.isDisguised(getPlayer()))
				api.undisguiseToAll(getPlayer());
		}else{
			if(villagerEntity.getType()==EntityType.VILLAGER)
				return;
			
			villagerEntity.remove();
			spawnVillager();
		}
	}
}
