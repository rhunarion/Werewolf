package jp.dip.jinroumc.werewolflobby;

import jp.dip.jinroumc.werewolf.util.C;
import jp.dip.jinroumc.werewolf.village.Village;
import jp.dip.jinroumc.werewolf.village.VillageUtil;
import jp.dip.jinroumc.werewolf.village.VillagePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDebug implements CommandExecutor {
	
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String cmdLabel, String[] args){
		
		if(args.length==0){
			sender.sendMessage(C.red+"Error: 無効なデバッグコマンドです。");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("shout")){
			if(args.length==2){
				String[] splitList = args[1].split("\\\\", -1);
				for(String split : splitList)
					for(Player pl : Bukkit.getOnlinePlayers())
						pl.sendMessage(C.red+"[Admin] "+split);
				return true;
			}
		}
		
		if(args[0].equalsIgnoreCase("tp")){
			if(args.length==2)
				((Player) sender).teleport(new Location(Bukkit.getWorld(args[1]), 0, 64, 0));
			return true;
		}
		
		if(args[0].equalsIgnoreCase("showRole")){
			if(args.length==2){
				if(VillageUtil.isVillageName(args[1])){
					Village vil = VillageUtil.getVillage(args[1]);
					sender.sendMessage(C.yellow+args[1]+C.gold+" の参加者の配役を表示します。");
					for(VillagePlayer vp : vil.getJoiningPlayerList())
						sender.sendMessage(C.gold+vp.getName()+" : "+vp.role);
					return true;
				}
			}
		}
		
		if(args[0].equalsIgnoreCase("showPlayers")){
			sender.sendMessage(C.gold+"全てのプレイヤーの現在いるワールドを表示します。");
			for(Player pl : Bukkit.getOnlinePlayers())
				sender.sendMessage(C.gold+pl.getName()+" : "+pl.getLocation().getWorld().getName());
			return true;
		}
		
		if(args[0].equalsIgnoreCase("rebuild")){
			if(args.length==1){
				sender.sendMessage(C.gold+"全ての村を再生成します。");
				for(String vilName : VillageUtil.getVillageNameList())
					VillageUtil.getVillage(vilName).rebuildVillage();
				return true;
			}else if(args.length==2){
				if(VillageUtil.isVillageName(args[1])){
					if(VillageUtil.isVillageName(args[1])){
						sender.sendMessage(C.yellow+args[1]+C.gold+" を再生成します。");
						Village vil = VillageUtil.getVillage(args[1]);
						vil.rebuildVillage();
						return true;
					}
				}
			}else{
				return true;
			}
		}
		
		if(args[0].equalsIgnoreCase("restart")){
			
			class AddShutdownHookSample {
				public void attachShutDownHook(){
					Runtime.getRuntime().addShutdownHook(new Thread() {
						@Override
						public void run() {
							System.out.println("Inside Add Shutdown Hook");
						}
					});
					System.out.println("Shut Down Hook Attached.");
				}
			}

			AddShutdownHookSample sample = new AddShutdownHookSample();
			sample.attachShutDownHook();
			System.out.println("Last instruction of Program....");
			Bukkit.shutdown();
		}
		
		if(args[0].equalsIgnoreCase("g")){
			/*Location loc = new Location(Bukkit.getWorld("default_village"),23, 65, -4);
			
			Sign sign = (Sign) loc.getBlock().getState();
			sign.setLine(0, "コンフィグルームへ");
			sign.setLine(1, "移動する");
			sign.setLine(2, "/configroom");

			sign.update();*/
			return true;
		}
		
		if(args[0].equalsIgnoreCase("join")){
			Server sv = Bukkit.getServer();
			sv.dispatchCommand(sender, "entervil vil0");
			sv.dispatchCommand(sender, "makevil");
			sv.dispatchCommand(sender, "startrec");
			sv.dispatchCommand(sender, "chrule daytime 300");
			sv.dispatchCommand(sender, "chrule nighttime 300");
			sv.dispatchCommand(sender, "chrule randomvote true");
			sv.dispatchCommand(sender, "chrule requestrole true");
			sv.dispatchCommand(sender, "chrule permitbite true");
			sv.dispatchCommand(sender, "chrule permitwhisp true");
			sv.dispatchCommand(sender, "chrule reishiallplayers true");
			sv.dispatchCommand(sender, "chrule jinrounum 2");
			sv.dispatchCommand(sender, "chrule kyoujinnum 0");
			sv.dispatchCommand(sender, "chrule youkonum 1");
			sv.dispatchCommand(sender, "chrule uranainum 1");
			sv.dispatchCommand(sender, "chrule kariudonum 1");
			sv.dispatchCommand(sender, "dc .p1 ww entervil vil0");
			sv.dispatchCommand(sender, "dc .p1 ww join");
			sv.dispatchCommand(sender, "dc .p2 ww entervil vil0");
			sv.dispatchCommand(sender, "dc .p2 ww join");
			sv.dispatchCommand(sender, "dc .p3 ww entervil vil0");
			sv.dispatchCommand(sender, "dc .p3 ww join");
			sv.dispatchCommand(sender, "dc .p4 ww entervil vil0");
			sv.dispatchCommand(sender, "dc .p4 ww join");
			sv.dispatchCommand(sender, "dc .p5 ww entervil vil0");
			sv.dispatchCommand(sender, "dc .p5 ww join");
			sv.dispatchCommand(sender, "dc .p6 ww entervil vil0");
			sv.dispatchCommand(sender, "dc .p6 ww join");
			sv.dispatchCommand(sender, "dc .p7 ww entervil vil0");
			sv.dispatchCommand(sender, "dc .p7 ww join");
			sv.dispatchCommand(sender, "dc .p1 ww requestrole jinrou");
			sv.dispatchCommand(sender, "dc .p2 ww requestrole jinrou");
			sv.dispatchCommand(sender, "dc .p3 ww requestrole youko");
			sv.dispatchCommand(sender, "dc .p4 ww requestrole kariudo");
			sv.dispatchCommand(sender, "startgame");
			return true;
		}
		
		sender.sendMessage(C.red+"Error: 無効なデバッグコマンドです。");
		return true;
	}
}
