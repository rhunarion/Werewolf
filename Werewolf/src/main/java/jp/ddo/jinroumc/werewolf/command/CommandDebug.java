package jp.ddo.jinroumc.werewolf.command;

import java.util.List;

import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.util.LookupNameManager;
import jp.ddo.jinroumc.werewolf.util.PluginChecker;
import jp.ddo.jinroumc.werewolf.village.Village;
import jp.ddo.jinroumc.werewolf.village.VillageUtil;
import jp.ddo.jinroumc.werewolf.village.VillagePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class CommandDebug {
	public static boolean prohibitMakeVil = false;
	
	public static void debug(CommandSender sender, String[] args){
		
		if(args.length==1){
			sender.sendMessage(C.red+"Error: 無効なデバッグコマンドです。");
			return;
		}
		
		if(args[1].equalsIgnoreCase("sendToServer")){
			if(args.length==3)
				for(Player pl : Bukkit.getOnlinePlayers())
					pl.sendMessage(C.gold+"[Admin] "+args[2]);
			return;
		}
		
		if(args[1].equalsIgnoreCase("teleport")){
			if(args.length==3)
				((Player) sender).teleport(new Location(Bukkit.getWorld(args[2]), 0, 64, 0));
			return;
		}
		
		if(args[1].equalsIgnoreCase("showRole")){
			if(args.length==3){
				if(VillageUtil.isVillageName(args[2])){
					Village vil = VillageUtil.getVillage(args[2]);
					sender.sendMessage(C.yellow+args[2]+C.gold+" の参加者の配役を表示します。");
					for(VillagePlayer vp : vil.getJoiningPlayerList())
						sender.sendMessage(C.gold+vp.getName()+" : "+vp.role);
					return;
				}
			}
		}
		
		if(args[1].equalsIgnoreCase("prohibitMakeVil")){
			CommandDebug.prohibitMakeVil = true;
			sender.sendMessage(C.yellow+"/"+PluginChecker.getWw()+"makevil"+C.gold+" コマンドの使用を禁止します。");
			return;
		}
		
		if(args[1].equalsIgnoreCase("allowMakeVil")){
			CommandDebug.prohibitMakeVil = false;
			sender.sendMessage(C.yellow+"/"+PluginChecker.getWw()+"makevil"+C.gold+" コマンドの使用を許可します。");
			return;
		}
		
		if(args[1].equalsIgnoreCase("rebuild")){
			if(args.length==3){
				if(args[2].equalsIgnoreCase("all")){
					sender.sendMessage(C.gold+"全ての村をリビルドします。");
					List<Village> vilList = VillageUtil.getVillageList();
					for(int i=0; i<vilList.size(); i++)
						vilList.get(i).rebuildVillage();
					return;
				}
				if(VillageUtil.isVillageName(args[2])){
					if(VillageUtil.isVillageName(args[2])){
						sender.sendMessage(C.yellow+args[2]+C.gold+" をリビルドします。");
						Village vil = VillageUtil.getVillage(args[2]);
						vil.rebuildVillage();
						return;
					}
				}
			}
		}
		
		if(args[1].equalsIgnoreCase("g")){
			Player pl = (Player) sender;
			Village vil = VillageUtil.getVillage(pl);
			
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(vil.plugin,
				ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT){
				@Override
				public void onPacketSending(PacketEvent event) {
					System.out.println("あいうえお: named sound");
				}
			});
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(vil.plugin,
					ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_EVENT){
					@Override
					public void onPacketSending(PacketEvent event) {
						System.out.println("あいうえお: world event");
						
					}
				});
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(vil.plugin,
					ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_PARTICLES){
					@Override
					public void onPacketSending(PacketEvent event) {
						System.out.println("あいうえお: world part");
						
					}
				});
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(vil.plugin,
					ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_EFFECT){
					@Override
					public void onPacketSending(PacketEvent event) {
						System.out.println("あいうえお: entity effe");
						
					}
				});
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(vil.plugin,
					ListenerPriority.NORMAL, PacketType.Play.Server.CUSTOM_PAYLOAD){
					@Override
					public void onPacketSending(PacketEvent event) {
						System.out.println("あいうえお: custom pay");
						
					}
				});
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(vil.plugin,
					ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_METADATA){
					@Override
					public void onPacketSending(PacketEvent event) {
						System.out.println("あいうえお: entity meta");
						
					}
				});
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(vil.plugin,
					ListenerPriority.NORMAL, PacketType.Play.Server.SET_SLOT){
					@Override
					public void onPacketSending(PacketEvent event) {
						System.out.println("あいうえお: set slot");
						
					}
				});
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(vil.plugin,
					ListenerPriority.NORMAL, PacketType.Play.Server.UPDATE_ATTRIBUTES){
					@Override
					public void onPacketSending(PacketEvent event) {
						System.out.println("あいうえお: update att");
						
					}
				});
			


			
			/*LookupNameManager manager = new LookupNameManager(vil.plugin);
			manager.start(ListenerPriority.NORMAL);
			manager.setClientName(pl, pl, "aiueo");*/
			
			
			/*Location loc = new Location(Bukkit.getWorld("default_village"),23, 65, -4);
			
			Sign sign = (Sign) loc.getBlock().getState();
			sign.setLine(0, "コンフィグルームへ");
			sign.setLine(1, "移動する");
			sign.setLine(2, "/configroom");

			sign.update();*/
			return;
		}
		
		if(args[1].equalsIgnoreCase("join")){
			Server sv = Bukkit.getServer();
			sv.dispatchCommand(sender, "entervil vil0");
			sv.dispatchCommand(sender, "makevil");
			sv.dispatchCommand(sender, "startrec");
			sv.dispatchCommand(sender, "chrule daytime 300");
			sv.dispatchCommand(sender, "chrule nighttime 300");
			sv.dispatchCommand(sender, "chrule randomvote true");
			sv.dispatchCommand(sender, "chrule requestrole true");
			sv.dispatchCommand(sender, "chrule reishiallplayers true");
			sv.dispatchCommand(sender, "chrule jinrounum 2");
			sv.dispatchCommand(sender, "chrule kyoujinnum 0");
			sv.dispatchCommand(sender, "chrule youkonum 1");
			sv.dispatchCommand(sender, "chrule uranainum 1");
			sv.dispatchCommand(sender, "chrule kariudonum 1");
			sv.dispatchCommand(sender, "dc p1 ww entervil vil0");
			sv.dispatchCommand(sender, "dc p1 ww join");
			sv.dispatchCommand(sender, "dc p2 ww entervil vil0");
			sv.dispatchCommand(sender, "dc p2 ww join");
			sv.dispatchCommand(sender, "dc p3 ww entervil vil0");
			sv.dispatchCommand(sender, "dc p3 ww join");
			sv.dispatchCommand(sender, "dc p4 ww entervil vil0");
			sv.dispatchCommand(sender, "dc p4 ww join");
			sv.dispatchCommand(sender, "dc p5 ww entervil vil0");
			sv.dispatchCommand(sender, "dc p5 ww join");
			sv.dispatchCommand(sender, "dc p6 ww entervil vil0");
			sv.dispatchCommand(sender, "dc p6 ww join");
			sv.dispatchCommand(sender, "dc p7 ww entervil vil0");
			sv.dispatchCommand(sender, "dc p7 ww join");
			sv.dispatchCommand(sender, "dc p4 ww requestrole jinrou");
			sv.dispatchCommand(sender, "startgame");
			return;
		}
		
		sender.sendMessage(C.red+"Error: 無効なデバッグコマンドです。");
	}
}
