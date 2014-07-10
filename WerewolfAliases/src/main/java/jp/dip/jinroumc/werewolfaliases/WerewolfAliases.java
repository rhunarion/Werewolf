package jp.dip.jinroumc.werewolfaliases;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import jp.dip.jinroumc.werewolf.util.PermissionChecker;
import jp.dip.jinroumc.werewolf.WwTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class WerewolfAliases extends JavaPlugin implements Listener {
	private PacketAdapter clientAdapter = new PacketAdapter(this,
			ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE){
				@Override
				public void onPacketReceiving(PacketEvent event) {
					String cmd = event.getPacket().getStrings().read(0).toLowerCase();
					if (cmd.startsWith("/") && !cmd.contains(" ")){
						List<String> cmdList = PermissionChecker.getCommandList(event.getPlayer());
						for(int i=0; i<cmdList.size(); i++)
							cmdList.set(i, "/" + cmdList.get(i));
						
						String[] ws = new String[1];
						ws[0] = cmd;
						List<String> tabCompList = WwTabCompleter.getPartialMatchesToLowerCase(ws, cmdList);
						if(tabCompList.size()==0){
							event.setCancelled(true);
							return;
						}
						String[] tabCompArray = tabCompList.toArray(new String[0]);
						PacketContainer packet = new PacketContainer(PacketType.Play.Server.TAB_COMPLETE);
						packet.getStringArrays().write(0, tabCompArray);
						
						try {
							ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), packet);
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			};
		
	private PacketAdapter serverAdapter =  new PacketAdapter(this,
			ListenerPriority.NORMAL, PacketType.Play.Server.TAB_COMPLETE){
				@Override
				public void onPacketSending(PacketEvent event) {
					String[] tabCompArray = event.getPacket().getStringArrays().read(0);
					if(tabCompArray.length>0 && tabCompArray[0].startsWith("/")){
						List<String> cmdList = PermissionChecker.getCommandList(event.getPlayer());
						for(int i=0; i<cmdList.size(); i++)
							cmdList.set(i, cmdList.get(i).toLowerCase());
						for(String tabComp : tabCompArray)
							if(!cmdList.contains(tabComp.substring(1)))
								event.setCancelled(true);
					}
				}
			};
	
	@Override
	public void onEnable(){
		ProtocolLibrary.getProtocolManager().addPacketListener(clientAdapter);
		ProtocolLibrary.getProtocolManager().addPacketListener(serverAdapter);
	}
	
	@Override
	public void onDisable(){
		ProtocolLibrary.getProtocolManager().removePacketListener(clientAdapter);
		ProtocolLibrary.getProtocolManager().removePacketListener(serverAdapter);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		String message = "ww "+cmd.getName();
		for(int i=0; i<args.length; i++){
			message += " ";
			message += args[i];
		}
		Bukkit.getServer().dispatchCommand(sender, message);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args){

		Command wc = new Command("ww"){
			public boolean execute(CommandSender sender, String commandLabel, String[] args){
				return false;
			}
		};
		String[] ws = new String[args.length+1];
		ws[0] = cmd.getName();
		for(int i=0; i<args.length; i++)
			ws[i+1] = args[i];
		
		return WwTabCompleter.getCompletionList(sender, wc, null, ws);
	}
}
