package jp.ddo.jinroumc.werewolf;

import jp.ddo.jinroumc.werewolf.command.CommandChRule;
import jp.ddo.jinroumc.werewolf.command.CommandHelp;
import jp.ddo.jinroumc.werewolf.command.CommandMethod;
import jp.ddo.jinroumc.werewolf.util.C;
import jp.ddo.jinroumc.werewolf.util.PluginChecker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WwCommandExecutor implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		//console log
		String str = args.length>0 ? " "+args[0] : "";
		for(int i=1; i<args.length; i++)
			str += " "+args[i];
		String world = "";
		if(sender instanceof Player)
			world = " (in "+((Player) sender).getWorld().getName()+")";
		System.out.println("[Werewolf] "+sender.getName()+" issued command: /"+cmd.getName()+str+world);

		
		//for console command
		if(args.length==0){
			CommandHelp.help(sender);
			return true;
		}
		if(args[0].equalsIgnoreCase("help")){
			if(args.length>=4) CommandHelp.usageError(sender, "help");
			else if(args.length==1) CommandHelp.help(sender);
			else if(args.length==2) CommandHelp.help(sender, args[1]);
			else if(args.length==3) CommandHelp.help(sender, args[1], args[2]);
			return true;
		}		
		
		//check sender
		if(!(sender instanceof Player)){
			sender.sendMessage("Error: このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player pl = (Player) sender;

		//help command
		if(args[0].equalsIgnoreCase("help")){
			if(args.length>=4) CommandHelp.usageError(pl, "help");
			else if(args.length==1) CommandHelp.help(pl);
			else if(args.length==2) CommandHelp.help(pl, args[1]);
			else if(args.length==3) CommandHelp.help(pl, args[1], args[2]);
			return true;
		}
		
		//enterVil command
		if(args[0].equalsIgnoreCase("enterVil")){
			if(args.length!=2 && args.length!=3) CommandHelp.usageError(pl, "enterVil");
			else if(args.length==2) CommandMethod.enterVil(pl, args[1]);
			else if(args.length==3) CommandMethod.enterVil(pl, args[1], args[2]);
			return true;
		}

		//leaveVil command
		if(args[0].equalsIgnoreCase("leaveVil")){
			if(args.length!=1) CommandHelp.usageError(pl, "leaveVil");
			else CommandMethod.leaveVil(pl);
			return true;
		}

		//lobby command
		if(args[0].equalsIgnoreCase("lobby")){
			if(args.length!=1) CommandHelp.usageError(pl, "lobby");
			else CommandMethod.lobby(pl);
			return true;
		}

		//showVil command
		if(args[0].equalsIgnoreCase("showVil")){
			if(args.length!=1) CommandHelp.usageError(pl, "showVil");
			else CommandMethod.showVil(pl);
			return true;
		}

		//showPlayers command
		if(args[0].equalsIgnoreCase("showPlayers")){
			if(args.length!=1) CommandHelp.usageError(pl, "showPlayers");
			else CommandMethod.showPlayers(pl);
			return true;
		}
		
		//makeVil command
		if(args[0].equalsIgnoreCase("makeVil")){
			if(args.length!=1 && args.length!=2) CommandHelp.usageError(pl, "makeVil");
			else if(args.length==1) CommandMethod.makeVil(pl);
			else if(args.length==2) CommandMethod.makeVil(pl, args[1]);
			return true;
		}
		
		//startRec command
		if(args[0].equalsIgnoreCase("startRec")){
			if(args.length!=1) CommandHelp.usageError(pl, "startRec");
			else CommandMethod.startRec(pl);
			return true;
		}
		
		//stopRec command
		if(args[0].equalsIgnoreCase("stopRec")){
			if(args.length!=1) CommandHelp.usageError(pl, "stopRec");
			else CommandMethod.stopRec(pl);
			return true;
		}
		
		//resetTimer command
		if(args[0].equalsIgnoreCase("resetTimer")){
			if(args.length!=1) CommandHelp.usageError(pl, "resetTimer");
			else CommandMethod.resetTimer(pl);
			return true;
		}

		//giveGM command
		if(args[0].equalsIgnoreCase("giveGM")){
			if(args.length!=2) CommandHelp.usageError(pl, "giveGM");
			else CommandMethod.giveGm(pl, args[1]);
			return true;
		}
		
		//configRoom command
		if(args[0].equalsIgnoreCase("configRoom")){
			if(args.length!=1) CommandHelp.usageError(pl, "configRoom");
			else CommandMethod.configRoom(pl);
			return true;
		}
		
		//chRule command
		if(args[0].equalsIgnoreCase("chRule")){
			if(args.length!=3) CommandHelp.usageError(pl, "chRule");
			else CommandChRule.chRule(pl, args[1], args[2]);
			return true;
		}
		
		//showRule command
		if(args[0].equalsIgnoreCase("showRule")){
			if(args.length!=1) CommandHelp.usageError(pl, "showRule");
			else CommandMethod.showRule(pl);
			return true;
		}
		
		//shoutRule command
		if(args[0].equalsIgnoreCase("shoutRule")){
			if(args.length!=1) CommandHelp.usageError(pl, "shoutRule");
			else CommandMethod.shoutRule(pl);
			return true;
		}
		
		//join command
		if(args[0].equalsIgnoreCase("join")){
			if(args.length!=1) CommandHelp.usageError(pl, "join");
			else CommandMethod.join(pl);
			return true;
		}
		
		//unjoin command
		if(args[0].equalsIgnoreCase("unjoin")){
			if(args.length!=1) CommandHelp.usageError(pl, "unjoin");
			else CommandMethod.unjoin(pl);
			return true;
		}
		
		//setRole command
		if(args[0].equalsIgnoreCase("setRole")){
			if(args.length!=3) CommandHelp.usageError(pl, "setRole");
			else CommandMethod.setRole(pl, args[1], args[2]);
			return true;
		}
		
		//unsetRole command
		if(args[0].equalsIgnoreCase("unsetRole")){
			if(args.length!=2) CommandHelp.usageError(pl, "unsetRole");
			else CommandMethod.unsetRole(pl, args[1]);
			return true;
		}
		
		//requestRole command
		if(args[0].equalsIgnoreCase("requestRole")){
			if(args.length!=2) CommandHelp.usageError(pl, "requestRole");
			else CommandMethod.requestRole(pl, args[1]);
			return true;
		}
		
		//startGame command
		if(args[0].equalsIgnoreCase("startGame")){
			if(args.length!=1) CommandHelp.usageError(pl, "startGame");
			else CommandMethod.startGame(pl);
			return true;
		}
		
		//finishGame command
		if(args[0].equalsIgnoreCase("finishGame")){
			if(args.length!=1) CommandHelp.usageError(pl, "finishGame");
			else CommandMethod.finishGame(pl);
			return true;
		}
		
		//skip command
		if(args[0].equalsIgnoreCase("skip")){
			if(args.length!=1) CommandHelp.usageError(pl, "skip");
			else CommandMethod.skip(pl);
			return true;
		}
		
		//kick command
		if(args[0].equalsIgnoreCase("kick")){
			if(args.length!=2) CommandHelp.usageError(pl, "kick");
			else CommandMethod.kick(pl, args[1]);
			return true;
		}
		
		//unkick command
		if(args[0].equalsIgnoreCase("unkick")){
			if(args.length!=2) CommandHelp.usageError(pl, "unkick");
			else CommandMethod.unkick(pl, args[1]);
			return true;
		}
		
		//kill command
		if(args[0].equalsIgnoreCase("kill")){
			if(args.length!=2) CommandHelp.usageError(pl, "kill");
			else CommandMethod.kill(pl, args[1]);
			return true;
		}
		
		//home command
		if(args[0].equalsIgnoreCase("home")){
			if(args.length!=1) CommandHelp.usageError(pl, "home");
			else CommandMethod.home(pl);
			return true;
		}
		
		//whisp command
		if(args[0].equalsIgnoreCase("whisp")){
			if(args.length!=3) CommandHelp.usageError(pl, "whisp");
			else CommandMethod.whisp(pl, args[1], args[2]);
			return true;
		}
		
		//shout command
		if(args[0].equalsIgnoreCase("shout")){
			if(args.length!=2) CommandHelp.usageError(pl, "shout");
			else CommandMethod.shout(pl, args[1]);
			return true;
		}
		
		//co command
		if(args[0].equalsIgnoreCase("co")){
			if(args.length!=2) CommandHelp.usageError(pl, "co");
			else CommandMethod.co(pl, args[1]);
			return true;
		}
		
		//myRole command
		if(args[0].equalsIgnoreCase("myRole")){
			if(args.length!=1) CommandHelp.usageError(pl, "myRole");
			else CommandMethod.myRole(pl);
			return true;
		}
		
		//vote command
		if(args[0].equalsIgnoreCase("vote")){
			if(args.length!=2) CommandHelp.usageError(pl, "vote");
			else CommandMethod.vote(pl, args[1]);
			return true;
		}
		
		//uranai command
		if(args[0].equalsIgnoreCase("uranai")){
			if(args.length!=2) CommandHelp.usageError(pl, "uranai");
			else CommandMethod.uranai(pl, args[1]);
			return true;
		}
		
		//guard command
		if(args[0].equalsIgnoreCase("guard")){
			if(args.length!=2) CommandHelp.usageError(pl, "guard");
			else CommandMethod.guard(pl, args[1]);
			return true;
		}
		
		//bite command
		if(args[0].equalsIgnoreCase("bite")){
			if(args.length!=2) CommandHelp.usageError(pl, "bite");
			else CommandMethod.bite(pl, args[1]);
			return true;
		}
		
		pl.sendMessage(C.red+"Error: 無効なコマンドです。ヘルプを見るには "+C.yellow+"/"
								+PluginChecker.getWw()+"help"+C.red+" と入力してください。");
		return true;
	}
}
