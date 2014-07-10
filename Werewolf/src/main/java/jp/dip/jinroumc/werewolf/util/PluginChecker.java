package jp.dip.jinroumc.werewolf.util;

import org.bukkit.Bukkit;

public class PluginChecker {
	private static String strIfAliasesDisabled;
	
	static{
		if(!Bukkit.getPluginManager().isPluginEnabled("WerewolfAliases"))
			strIfAliasesDisabled = "ww ";
		else
			strIfAliasesDisabled = "";
	}
	
	public static String getWw(){
		return strIfAliasesDisabled;
	}
}
