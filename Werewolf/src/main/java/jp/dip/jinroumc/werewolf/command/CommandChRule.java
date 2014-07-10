package jp.dip.jinroumc.werewolf.command;

import jp.dip.jinroumc.werewolf.enumconstant.VillageRole;
import jp.dip.jinroumc.werewolf.enumconstant.VillageStatus;
import jp.dip.jinroumc.werewolf.util.C;
import jp.dip.jinroumc.werewolf.util.PluginChecker;
import jp.dip.jinroumc.werewolf.village.Village;
import jp.dip.jinroumc.werewolf.village.VillagePlayer;
import jp.dip.jinroumc.werewolf.village.VillageUtil;

import org.bukkit.entity.Player;

public class CommandChRule {
	public static void chRule(Player pl, String arg, String value){
		if(!VillageUtil.isInVillage(pl)){
			pl.sendMessage(C.red+"Error: 村ワールドの中でしか使えないコマンドです。");
			return;
		}else{
			Village vil = VillageUtil.getVillage(pl);
			VillagePlayer vp = vil.getPlayer(pl);
			if(!vp.gameMaster){
				pl.sendMessage(C.red+"Error: ゲームマスターしか使えないコマンドです。");
				return;
			}
			if(vil.status==VillageStatus.FINISHING){
				pl.sendMessage(C.red+"Error: ゲーム終了中にルールを変更することはできません。");
				return;
			}
			
			if(arg.equalsIgnoreCase("maxNum")){
				if(vil.status==VillageStatus.ONGOING){
					pl.sendMessage(C.red+"Error: ゲーム進行中に最大人数の変更はできません。");
					return;
				}
				if(!value.matches("\\d+") && !value.matches("-\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。");
					return;
				}
				int val = Integer.valueOf(value);
				if(val<=3 || val>=17){
					pl.sendMessage(C.red+"Error: 最大人数は4人以上、16人以下にしてください。");
					return;
				}
				if(val<vil.getTotalRoleNumInRuleExceptMurabito()){
					pl.sendMessage(C.red+"Error: 先に村人以外の配役の数を減らしてください。");
					return;
				}
				if(val-vil.jinrouNum-vil.youkoNum<vil.jinrouNum+2){
					pl.sendMessage(C.red+"Error: 人狼の人数が多すぎます。先に人狼を減らしてください。");
					return;
				}

				vil.maxNum = val;
				vil.sendToVillage(C.gold+"最大人数が "+C.yellow
										+vil.maxNum+"人"+C.gold+" に設定されました。");
				if(vil.getJoiningPlayerNum()>vil.maxNum){
					vil.sendToVillage(C.gold+"参加者が定員を越えているためリセットします。");
					for(VillagePlayer join : vil.getJoiningPlayerListExceptNpc()){
						join.unjoinGame();
					}
				}
				return;
			}
			if(arg.equalsIgnoreCase("uranaiNum")){
				if(vil.status==VillageStatus.ONGOING){
					pl.sendMessage(C.red+"Error: ゲーム進行中に配役人数の変更はできません。");
					return;
				}
				if(!value.matches("\\d+") && !value.matches("-\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。");
					return;
				}
				int val = Integer.valueOf(value);
				if(val<0){
					pl.sendMessage(C.red+"Error: 占い師の人数は0人以上にしてください。");
					return;
				}
				if(vil.getTotalRoleNumInRuleExceptMurabito()-vil.uranaiNum+val > vil.maxNum){
					pl.sendMessage(C.red+"Error: 占い師の人数が多すぎます。先に他の配役を減らしてください。");
					return;
				}

				vil.uranaiNum = val;
				vil.sendToVillage(C.gold+"占い師の人数が "+C.yellow
												+vil.uranaiNum+"人"+C.gold+" に設定されました。");
				if(vil.getSettedRoleNum(VillageRole.URANAISHI)>val){
					vil.unsetAllSettedRole(VillageRole.URANAISHI);
					pl.sendMessage(C.gold+"占い師に配役されていたプレイヤーの配役を解除します。");
				}
				return;
			}
			if(arg.equalsIgnoreCase("reibaiNum")){
				if(vil.status==VillageStatus.ONGOING){
					pl.sendMessage(C.red+"Error: ゲーム進行中に配役人数の変更はできません。");
					return;
				}
				if(!value.matches("\\d+") && !value.matches("-\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。");
					return;
				}
				int val = Integer.valueOf(value);
				if(val<0){
					pl.sendMessage(C.red+"Error: 霊媒師の人数は0人以上にしてください。");
					return;
				}
				if(vil.getTotalRoleNumInRuleExceptMurabito()-vil.reibaiNum+val > vil.maxNum){
					pl.sendMessage(C.red+"Error: 霊媒師の人数が多すぎます。先に他の配役を減らしてください。");
					return;
				}

				vil.reibaiNum = val;
				vil.sendToVillage(C.gold+"霊媒師の人数が "+C.yellow
												+vil.reibaiNum+"人"+C.gold+" に設定されました。");
				if(vil.getSettedRoleNum(VillageRole.REIBAISHI)>val){
					vil.unsetAllSettedRole(VillageRole.REIBAISHI);
					pl.sendMessage(C.gold+"霊媒師に配役されていたプレイヤーの配役を解除します。");
				}
				return;
			}
			if(arg.equalsIgnoreCase("kariudoNum")){
				if(vil.status==VillageStatus.ONGOING){
					pl.sendMessage(C.red+"Error: ゲーム進行中に配役人数の変更はできません。");
					return;
				}
				if(!value.matches("\\d+") && !value.matches("-\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。");
					return;
				}
				int val = Integer.valueOf(value);
				if(val<0){
					pl.sendMessage(C.red+"Error: 狩人の人数は0人以上にしてください。");
					return;
				}
				if(vil.getTotalRoleNumInRuleExceptMurabito()-vil.kariudoNum+val > vil.maxNum){
					pl.sendMessage(C.red+"Error: 狩人の人数が多すぎます。先に他の配役を減らしてください。");
					return; 
				}

				vil.kariudoNum = val;
				vil.sendToVillage(C.gold+"狩人の人数が "+C.yellow
												+vil.kariudoNum+"人"+C.gold+" に設定されました。");
				if(vil.getSettedRoleNum(VillageRole.KARIUDO)>val){
					vil.unsetAllSettedRole(VillageRole.KARIUDO);
					pl.sendMessage(C.gold+"狩人に配役されていたプレイヤーの配役を解除します。");
				}
				return;
			}
			if(arg.equalsIgnoreCase("jinrouNum")){
				if(vil.status==VillageStatus.ONGOING){
					pl.sendMessage(C.red+"Error: ゲーム進行中に配役人数の変更はできません。");
					return;
				}
				if(!value.matches("\\d+") && !value.matches("-\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。");
					return;
				}
				int val = Integer.valueOf(value);
				if(val<=0){
					pl.sendMessage(C.red+"Error: 人狼の人数は1人以上にしてください。");
					return;
				}
				if(vil.getTotalRoleNumInRuleExceptMurabito()-vil.jinrouNum+val > vil.maxNum){
					pl.sendMessage(C.red+"Error: 人狼の人数が多すぎます。先に他の配役を減らしてください。");
					return;
				}
				if(vil.getHumanNumInRule()+vil.jinrouNum-val<val+2){
					pl.sendMessage(C.red+"Error: 人狼の人数が多すぎます。人間と同じくらいいます。");
					return;
				}

				vil.jinrouNum = val;
				vil.sendToVillage(C.gold+"人狼の人数が "+C.yellow
												+vil.jinrouNum+"人"+C.gold+" に設定されました。");
				if(vil.getSettedRoleNum(VillageRole.JINROU)>val){
					vil.unsetAllSettedRole(VillageRole.JINROU);
					pl.sendMessage(C.gold+"人狼に配役されていたプレイヤーの配役を解除します。");
				}
				return;
			}
			if(arg.equalsIgnoreCase("kyoujinNum")){
				if(vil.status==VillageStatus.ONGOING){
					pl.sendMessage(C.red+"Error: ゲーム進行中に配役人数の変更はできません。");
					return;
				}
				if(!value.matches("\\d+") && !value.matches("-\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。");
					return;
				}
				int val = Integer.valueOf(value);
				if(val<0){
					pl.sendMessage(C.red+"Error: 狂人の人数は0人以上にしてください。");
					return;
				}
				if(vil.getTotalRoleNumInRuleExceptMurabito()-vil.kyoujinNum+val > vil.maxNum){
					pl.sendMessage(C.red+"Error: 狂人の人数が多すぎます。先に他の配役を減らしてください。");
					return;
				}

				vil.kyoujinNum = val;
				vil.sendToVillage(C.gold+"狂人の人数が "+C.yellow
												+vil.kyoujinNum+"人"+C.gold+" に設定されました。");
				if(vil.getSettedRoleNum(VillageRole.KYOUJIN)>val){
					vil.unsetAllSettedRole(VillageRole.KYOUJIN);
					pl.sendMessage(C.gold+"狂人に配役されていたプレイヤーの配役を解除します。");
				}
				return;
			}
			if(arg.equalsIgnoreCase("youkoNum")){
				if(vil.status==VillageStatus.ONGOING){
					pl.sendMessage(C.red+"Error: ゲーム進行中に配役人数の変更はできません。");
					return;
				}
				if(!value.matches("\\d+") && !value.matches("-\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。");
					return;
				}
				int val = Integer.valueOf(value);
				if(val<0){
					pl.sendMessage(C.red+"Error: 妖狐の人数は0人以上にしてください。");
					return;
				}
				if(vil.getTotalRoleNumInRuleExceptMurabito()-vil.youkoNum+val > vil.maxNum){
					pl.sendMessage(C.red+"Error: 妖狐の人数が多すぎます。先に他の配役を減らしてください。");
					return;
				}

				vil.youkoNum = val;
				vil.sendToVillage(C.gold+"妖狐の人数が "+C.yellow
												+vil.youkoNum+"人"+C.gold+" に設定されました。");
				if(vil.getSettedRoleNum(VillageRole.YOUKO)>val){
					vil.unsetAllSettedRole(VillageRole.YOUKO);
					pl.sendMessage(C.gold+"妖狐に配役されていたプレイヤーの配役を解除します。");
				}
				return;
			}
			if(arg.equalsIgnoreCase("title")){
				if(value.length()>=17){
					pl.sendMessage(C.red+"Error: 人狼ゲームのタイトルは16文字以下にしてください。");
					return;
				}

				vil.title = value;
				vil.sendToVillage(C.gold+"人狼ゲームのタイトルが "+C.yellow
										+vil.title+C.gold+" に設定されました。");
				return;
			}
			if(arg.equalsIgnoreCase("description")){
				vil.description = value;
				vil.sendToVillage(C.gold+"この村の人狼ゲームの説明が "+C.yellow
												+vil.description+C.gold+" に設定されました。");
				return;
			}
			if(arg.equalsIgnoreCase("setPassword")){
				if(value.equalsIgnoreCase("false")){
					vil.setPassword = false;
					vil.sendToVillage(C.gold+"入村パスワードを "+C.yellow
												+"設定しない"+C.gold+" に設定されました。");
				}else{
					vil.setPassword = true;
					vil.password = value;
					vil.sendToVillage(C.gold+"入村パスワードが "+C.yellow
												+vil.password+C.gold+" に設定されました。");
				}
				return;
			}
			if(arg.equalsIgnoreCase("dayTime")){
				if(!value.matches("\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。"); 
					return;
				}
				int val = Integer.valueOf(value);
				if(val<30 || val>600){
					pl.sendMessage(C.red+"Error: 昼の時間は30秒以上、600秒以下にしてください。");
					return;
				}

				vil.dayTime = val;
				vil.sendToVillage(C.gold+"昼の時間が "+C.yellow
												+vil.dayTime+"秒"+C.gold+" に設定されました。");
				return;
			}
			if(arg.equalsIgnoreCase("nightTime")){
				if(!value.matches("\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。");
					return;
				}
				int val = Integer.valueOf(value);
				if(val<30 || val>600){
					pl.sendMessage(C.red+"Error: 夜の時間は30秒以上、600秒以下にしてください。");
					return;
				}

				vil.nightTime = val;
				vil.sendToVillage(C.gold+"夜の時間が "+C.yellow
												+vil.nightTime+"秒"+C.gold+" に設定されました。");
				return;
			}
			if(arg.equalsIgnoreCase("requestRole")){
				if(vil.status==VillageStatus.ONGOING){
					pl.sendMessage(C.red+"Error: ゲーム進行中に配役リクエストの設定は変更できません。");
					return;
				}
				if(!value.equals("true") && !value.equals("false")){
					pl.sendMessage(C.red+"Error: 値には "+C.yellow+"true"+C.red
									+" または "+C.yellow+"false"+C.red+" を指定してください。");
					return;
				}

				vil.requestRole = Boolean.valueOf(value);
				if(vil.requestRole)
					vil.sendToVillage(C.gold+"配役の "+C.yellow
												+"リクエストあり"+C.gold+" に設定されました。");
				else
					vil.sendToVillage(C.gold+"配役の "+C.yellow
												+"リクエストなし"+C.gold+" に設定されました。");
				return;
			}
			if(arg.equalsIgnoreCase("randomVote")){
				if(!value.equals("true") && !value.equals("false")){
					pl.sendMessage(C.red+"Error: 値には "+C.yellow+"true"+C.red
									+" または "+C.yellow+"false"+C.red+" を指定してください。");
					return;
				}

				vil.randomVote = Boolean.valueOf(value);
				if(vil.randomVote)
					vil.sendToVillage(C.gold+"未投票のとき "+C.yellow
												+"ランダム投票する"+C.gold+" に設定されました。");
				else
					vil.sendToVillage(C.gold+"未投票のとき "+C.yellow
												+"ランダム投票しない"+C.gold+" に設定されました。");
				return;
			}
			if(arg.equalsIgnoreCase("revoteNum")){
				if(!value.matches("\\d+") && !value.matches("-\\d+")){
					pl.sendMessage(C.red+"Error: 値には数値を指定してください。");
					return;
				}
				int val = Integer.valueOf(value);
				if(val<0 || val>10){
					pl.sendMessage(C.red+"Error: 再投票の回数は0回以上、10回以下にしてください。");
					return;
				}

				vil.revoteNum = val;
				vil.sendToVillage(C.gold+"再投票の最大回数が "+C.yellow
												+vil.revoteNum+"回"+C.gold+" に設定されました。");
				return;
			}
			if(arg.equalsIgnoreCase("permitWhisp")){
				if(!value.equals("true") && !value.equals("false")){
					pl.sendMessage(C.red+"Error: 値には "+C.yellow+"true"+C.red
									+" または "+C.yellow+"false"+C.red+" を指定してください。");
					return;
				}

				vil.permitWhisp = Boolean.valueOf(value);
				if(vil.permitWhisp)
					vil.sendToVillage(C.yellow+"/"+PluginChecker.getWw()+"whisp"+C.gold+" コマンドの使用を "
												+C.yellow+"許可する"+C.gold+" に設定されました。");
				else
					vil.sendToVillage(C.gold+"/"+PluginChecker.getWw()+"whisp コマンドの使用を "
												+C.yellow+"許可しない"+C.gold+" に設定されました。");
				return;
			}
			if(arg.equalsIgnoreCase("reishiAllPlayers")){
				if(!value.equals("true") && !value.equals("false")){
					pl.sendMessage(C.red+"Error: 値には "+C.yellow+"true"+C.red
									+" または "+C.yellow+"false"+C.red+" を指定してください。");
					return;
				}

				vil.reishiAllPlayers = Boolean.valueOf(value);
				if(vil.reishiAllPlayers)
					vil.sendToVillage(C.gold+"霊媒師は "+C.yellow
										+"命を落とした全てのプレイヤーの配役"+C.gold+" を知ることができるに設定されました。");
				else
					vil.sendToVillage(C.gold+"霊媒師は "+C.yellow
										+"処刑されたプレイヤーの配役のみ"+C.gold+" を知ることできるに設定されました。");
				return;
			}
			if(arg.equalsIgnoreCase("permitBite")){
				if(!value.equals("true") && !value.equals("false")){
					pl.sendMessage(C.red+"Error: 値には "+C.yellow+"true"+C.red
									+" または "+C.yellow+"false"+C.red+" を指定してください。");
					return;
				}

				vil.permitBite = Boolean.valueOf(value);
				if(vil.permitBite)
					vil.sendToVillage(C.yellow+"/"+PluginChecker.getWw()+"bite"+C.gold+" コマンドの使用を "
												+C.yellow+"許可する"+C.gold+" に設定されました。");
				else
					vil.sendToVillage(C.gold+"/"+PluginChecker.getWw()+"bite コマンドの使用を "
												+C.yellow+"許可しない"+C.gold+" に設定されました。");
				return;
			}
		}
		
		pl.sendMessage(C.red+"Error: 無効なコマンドです。ヘルプを見るには "+C.yellow+"/"
								+PluginChecker.getWw()+"help chrule"+C.yellow+" とコマンドしてください。");
	}
}
