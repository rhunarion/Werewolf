package jp.dip.jinroumc.werewolf.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jp.dip.jinroumc.werewolf.util.C;
import jp.dip.jinroumc.werewolf.util.PermissionChecker;
import jp.dip.jinroumc.werewolf.util.PluginChecker;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandHelp {
	private static HashMap<String, String> commandUsage = new HashMap<String, String>();
	private static HashMap<String, String> commandDescription = new HashMap<String, String>();
	private static HashMap<String, String> ruleUsage = new HashMap<String, String>();
	private static HashMap<String, String> ruleDescription = new HashMap<String, String>();
	
	static{
		commandUsage.put("help", "help [command] [rule]");
		commandUsage.put("enterVil", "entervil <village_name> [password]");
		commandUsage.put("leaveVil", "leavevil");
		commandUsage.put("lobby", "lobby");
		commandUsage.put("showVil", "showvil");
		commandUsage.put("showPlayers", "showplayers");
		commandUsage.put("makeVil", "makevil [village_type]");
		commandUsage.put("startRec", "startrec");
		commandUsage.put("stopRec", "stoprec");
		commandUsage.put("resetTimer", "resettimer");
		commandUsage.put("giveGM", "givegm <player>");
		commandUsage.put("configRoom", "configroom");
		commandUsage.put("chRule", "chrule <rule> <value>");
		commandUsage.put("showRule", "showrule");
		commandUsage.put("shoutRule", "shoutrule");
		commandUsage.put("join", "join");
		commandUsage.put("unjoin", "unjoin");
		commandUsage.put("setRole", "setrole <player> <role>");
		commandUsage.put("unsetRole", "unsetrole <player>");
		commandUsage.put("requestRole", "requestrole <role>");
		commandUsage.put("startGame", "startgame");
		commandUsage.put("finishGame", "finishgame");
		commandUsage.put("skip", "skip");
		commandUsage.put("kick", "kick <player>");
		commandUsage.put("unkick", "unkick <player>");
		commandUsage.put("kill", "kill <player>");
		commandUsage.put("home", "home");
		commandUsage.put("whisp", "whisp <player> <message>");
		commandUsage.put("shout", "shout <message>");
		commandUsage.put("co", "co <message>");
		commandUsage.put("myRole", "myrole");
		commandUsage.put("vote", "vote <player>");
		commandUsage.put("uranai", "uranai <player>");
		commandUsage.put("guard", "guard <player>");
		commandUsage.put("bite", "bite <player>");
		
		commandDescription.put("help", "ヘルプを表示");
		commandDescription.put("enterVil", "入村する");
		commandDescription.put("leaveVil", "ロビーに帰る");
		commandDescription.put("lobby", "ロビーに帰る");
		commandDescription.put("showVil", "村ワールドのリストを取得する");
		commandDescription.put("showPlayers", "プレイヤーリストを取得する");
		commandDescription.put("makeVil", "新しく人狼ゲームの準備を開始する");
		commandDescription.put("startRec", "参加者の募集を開始する (GM)");
		commandDescription.put("stopRec", "参加者の募集を一時停止する (GM)");
		commandDescription.put("resetTimer", "タイマーをリセットする (GM)");
		commandDescription.put("giveGM", "GM権限を渡す (GM)");
		commandDescription.put("configRoom", "コンフィグルームへ移動する (GM)");
		commandDescription.put("chRule", "ルールを変更する (GM)");
		commandDescription.put("showRule", "現在のルールを取得する");
		commandDescription.put("shoutRule", "現在のルールを発言する (GM)");
		commandDescription.put("join", "ゲームに参加する");
		commandDescription.put("unjoin", "ゲーム参加をやめる");
		commandDescription.put("setRole", "配役を設定する (GM)");
		commandDescription.put("unsetRole", "配役の設定を解除する (GM)");
		commandDescription.put("requestRole", "配役を希望する");
		commandDescription.put("startGame", "ゲームをスタートする (GM)");
		commandDescription.put("finishGame", "ゲームを強制終了する (GM)");
		commandDescription.put("skip", "時間をスキップして次の時間帯へ進める (GM)");
		commandDescription.put("kick", "プレイヤーを村から追い出す (GM)");
		commandDescription.put("unkick", "キックを解除する (GM)");
		commandDescription.put("kill", "人狼ゲームの中でプレイヤーを殺す (GM)");
		commandDescription.put("home", "自分の家へ移動する");
		commandDescription.put("whisp", "特定のプレイヤーへのみ発言する");
		commandDescription.put("shout", "強制的に村全体へ発言する (GM)");
		commandDescription.put("co", "色付きメッセージを発言する");
		commandDescription.put("myRole", "自分の配役情報を取得する");
		commandDescription.put("vote", "投票する");
		commandDescription.put("uranai", "占う (占い師)");
		commandDescription.put("guard", "護衛する (狩人)");
		commandDescription.put("bite", "噛む (人狼)");
		
		ruleUsage.put("maxNum", "chrule maxnum <number>");
		ruleUsage.put("uranaiNum", "chrule uranainum <number>");
		ruleUsage.put("reibaiNum", "chrule reiainum <number>");
		ruleUsage.put("kariudoNum", "chrule kariudonum <number>");
		ruleUsage.put("jinrouNum", "chrule jinrounum <number>");
		ruleUsage.put("kyoujinNum", "chrule kyoujinnum <number>");
		ruleUsage.put("youkoNum", "chrule youkonum <number>");
		ruleUsage.put("title", "chrule title <title>");
		ruleUsage.put("description", "chrule description <description>");
		ruleUsage.put("setPassword", "chrule setpassword <password>");
		ruleUsage.put("dayTime", "chrule daytime <seconds>");
		ruleUsage.put("nightTime", "chrule nighttime <seconds>");
		ruleUsage.put("requestRole", "chrule requestrole <true|false>");
		ruleUsage.put("randomVote", "chrule randomvoting <true|false>");
		ruleUsage.put("revoteNum", "chrule revotenum <number>");
		ruleUsage.put("permitWhisp", "chrule permitwhisp <true|false>");
		ruleUsage.put("reishiAllPlayers", "chrule reishiallplayers <true|false>");
		ruleUsage.put("permitBite", "chrule permitbite <true|false>");
		
		ruleDescription.put("maxNum", "最大参加人数");
		ruleDescription.put("uranaiNum", "占い師の人数");
		ruleDescription.put("reibaiNum", "霊媒師の人数");
		ruleDescription.put("kariudoNum", "狩人の人数");
		ruleDescription.put("jinrouNum", "人狼の人数");
		ruleDescription.put("kyoujinNum", "狂人の人数");
		ruleDescription.put("youkoNum", "妖狐の人数");
		ruleDescription.put("title", "人狼ゲームのタイトル");
		ruleDescription.put("description", "この村の人狼ゲームの説明");
		ruleDescription.put("setPassword", "入村パスワードを設定する。falseで解除");
		ruleDescription.put("dayTime", "昼の時間の長さ (秒数)");
		ruleDescription.put("nightTime", "夜の時間の長さ (秒数)");
		ruleDescription.put("requestRole", "配役希望のあり/なし");
		ruleDescription.put("randomVote", "未投票のとき自動でランダム投票する/しない");
		ruleDescription.put("revoteNum", "引き分け投票のとき再投票をする最大回数");
		ruleDescription.put("permitWhisp", "/"+PluginChecker.getWw()+"whispコマンド (特定プレイヤーへのみ発言) を許可する/しない");
		ruleDescription.put("reishiAllPlayers", "霊媒師は命を落とした全てのプレイヤーの配役を知ることができる/できない");
		ruleDescription.put("permitBite", "/"+PluginChecker.getWw()+"biteコマンド (人狼が夜に村人を噛む) を許可する/しない");
	}
	
	public static void help(CommandSender sender){
		sender.sendMessage(C.gold+"////////// "+C.yellow
							+"Command List"+C.gold+" //////////");
		
		if(sender instanceof ConsoleCommandSender){
			List<String> cmdList = getCommandList();
			Collections.sort(cmdList, String.CASE_INSENSITIVE_ORDER);

			for(String cmd : cmdList)
				sender.sendMessage(C.yellow+"/"+PluginChecker.getWw()+commandUsage.get(cmd)
						+C.gold+"    :"+commandDescription.get(cmd));
		}
		else if(sender instanceof Player){
			Player pl = (Player) sender;
			List<String> cmdList = PermissionChecker.getCommandList(pl);
			Collections.sort(cmdList, String.CASE_INSENSITIVE_ORDER);
			
			for(String cmd : cmdList)
				sender.sendMessage(C.yellow+"/"+PluginChecker.getWw()+commandUsage.get(cmd)
						+C.gold+"    :"+commandDescription.get(cmd));
		}
	}

	public static void help(CommandSender sender, String arg1){
		if(arg1.equalsIgnoreCase("chRule")){
			List<String> ruleList = getRuleList();
			Collections.sort(ruleList, String.CASE_INSENSITIVE_ORDER);
			
			sender.sendMessage(C.gold+"////////// "+C.yellow
							+"Changeable Rule List"+C.gold+" //////////");
			sender.sendMessage(C.yellow+"/"+PluginChecker.getWw()+commandUsage.get("chRule")
							+C.gold+"    :"+commandDescription.get("chRule"));
			for(String rule : ruleList)
				sender.sendMessage(C.yellow+"/"+PluginChecker.getWw()+ruleUsage.get(rule)
							+C.gold+"    :"+ruleDescription.get(rule));
			return;
		}
		
		List<String> cmdList = getCommandList();
		if(arg1.equalsIgnoreCase("all")){
			Collections.sort(cmdList, String.CASE_INSENSITIVE_ORDER);
			sender.sendMessage(C.gold+"////////// "+C.yellow
								+"Command List"+C.gold+" //////////");
			for(String cmd : cmdList)
				sender.sendMessage(C.yellow+"/"+PluginChecker.getWw()+commandUsage.get(cmd)
								+C.gold+"    :"+commandDescription.get(cmd));
			return;
		}

		for(String cmd : cmdList){
			if(cmd.equalsIgnoreCase(arg1)){
				sender.sendMessage(C.yellow+"/"+PluginChecker.getWw()+commandUsage.get(cmd)
							+C.gold+"    :"+commandDescription.get(cmd));
				return;
			}
		}

		sender.sendMessage(C.red+"Error: "+C.yellow+"/"+PluginChecker.getWw()+arg1
							+C.red+" というコマンドは存在しません。");
	}

	public static void help(CommandSender sender, String arg1, String arg2){
		if(!arg1.equalsIgnoreCase("chRule")){
			sender.sendMessage(C.red+"Error: "+C.yellow
							+"/"+PluginChecker.getWw()+arg1+" "+arg2+C.red
							+" のヘルプが存在しないか、コマンドが存在しません。");
			return;
		}
		
		List<String> ruleList = getRuleList();
		for(String rule : ruleList){
			if(rule.equalsIgnoreCase(arg2)){
				sender.sendMessage(C.yellow+"/"+PluginChecker.getWw()+ruleUsage.get(rule)
							+C.gold+"    :"+ruleDescription.get(rule));
				return;
			}
		}
		
		sender.sendMessage(C.red+"Error: "+C.yellow
							+"/"+PluginChecker.getWw()+"chrule "+arg2+C.red+" というコマンドは存在しません。");
	}
	
	public static void usageError(CommandSender sender, String arg1){
		List<String> cmdList = getCommandList();
		for(String cmd : cmdList)
			if(cmd.equalsIgnoreCase(arg1))
				sender.sendMessage(C.red+"Error: Usage: "
						+C.yellow+"/"+PluginChecker.getWw()+commandUsage.get(cmd));
	}

	public static List<String> getCommandList(){
		return new ArrayList<String>(commandUsage.keySet());
	}
	
	public static List<String> getRuleList(){
		return new ArrayList<String>(ruleUsage.keySet());
	}
}
