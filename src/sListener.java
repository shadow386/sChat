import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class sListener extends PluginListener
{
	public SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public static HashMap<String, Long> afkList = new HashMap<String, Long>();
	public static ArrayList<String> whitelist = new ArrayList<String>();
	public static ArrayList<String> channels = new ArrayList<String>();
	public static Map<Player, String> connected = new HashMap<Player, String>();
	public static ArrayList<Player> admin = new ArrayList<Player>();
	public static ArrayList<String> adminList = new ArrayList<String>();
	
	public boolean onChat(Player player, String message)
	{
		if (afkList.containsKey(player.getName()))
		{
			afkList.remove(player.getName());
			etc.getServer().messageAll("[\u00A77Status\u00A7f] \u00A7e" + player.getName() + "\u00A77 has returned");
		}
		
		if (player.isMuted())
		{
			player.notify("You are currently muted.");
			return true;
		}
		  
		Date now = new Date();
		Format formatter = new SimpleDateFormat(sChat.sconf.getString("time-format"));
		String timeFormat = formatter.format(now);
		String chatFormat = sChat.sconf.getString("chat-format");
		String logFormat = sChat.sconf.getString("log-format");
		
		if (chatFormat.contains("&"))
		{
			chatFormat = chatFormat.replaceAll("&", Colors.Marker);
		}
		if (chatFormat.contains("%time")) chatFormat = chatFormat.replaceAll("%time", timeFormat);
		if (chatFormat.contains("%msg")) chatFormat = chatFormat.replaceAll("%msg", message);
		if (chatFormat.contains("%group")) chatFormat = chatFormat.replaceAll("%group", sChat.core.getGroupName(player));
		if (chatFormat.contains("%name")) chatFormat = chatFormat.replaceAll("%name", sChat.core.getNickname(player));
		if (logFormat.contains("%msg")) logFormat = logFormat.replaceAll("%msg", message);
		if (logFormat.contains("%group")) logFormat = logFormat.replaceAll("%group", sChat.core.getGroupName(player));
		if (logFormat.contains("%name")) logFormat = logFormat.replaceAll("%name", sChat.core.getNickname(player));
		if (logFormat.contains("%chan") && sChat.channels.getBoolean("use-channels")) logFormat = logFormat.replaceAll("%name", sChat.core.getNickname(player));
		if (logFormat.contains(Colors.Marker)) logFormat = logFormat.replaceAll(Colors.Marker, "&");
		if (message.contains("&") && player.canUseCommand("/colorchat"))
		{
			chatFormat = chatFormat.replaceAll("&", Colors.Marker);
		}
		
		if (sChat.sconf.getBoolean("use-censor")) {
			chatFormat = sChat.core.censor(chatFormat);
		}
		
		sChat.log.info(logFormat);
		etc.getServer().messageAll(chatFormat);
		return true;
	}
	
	public boolean onCommand(Player player, String[] split)
	{
		if (split[0].equalsIgnoreCase("/nickname") || split[0].equalsIgnoreCase("/nick"))
		{
			if (!player.canUseCommand("/nickname"))
			{
				player.notify("You cannot use this command");
				return true;
			}
			
			if (split.length < 2) {
				player.notify("Usage: /nick [nickname]");
		        return true;
			}
			
			Player ouser = etc.getServer().matchPlayer(split[1]);
			if (ouser != null)
			{
				String newNick = etc.combineSplit(2, split, " ");
				
				if (newNick.length() > 12)
				{
					player.notify("Nickname is too long! Cannot be longer than 12 characters");
					return true;
				}
				
				sChat.nicknames.setString(ouser.getName(), newNick);
				player.sendMessage("\u00A77You have changed \u00A72"+ouser.getName()+"'s\u00A77 name to \u00A72" + newNick);
				ouser.sendMessage("\u00A77Your name has been changed to \u00A72" + newNick);
				return true;
			}
			else
			{
				String newNick = etc.combineSplit(1, split, " ");
				
				if (newNick.length() > 12)
				{
					player.notify("Nickname is too long! Cannot be longer than 12 characters");
					return true;
				}
				
				sChat.nicknames.setString(player.getName(), newNick);
				player.sendMessage("\u00A77Your name has been changed to \u00A72" + newNick);
				return true;
			}
		}
		
		if ((split[0].equalsIgnoreCase("/gnick") || split[0].equalsIgnoreCase("/groupname")) && (player.canUseCommand("/gnick")))
		{
			if (split.length < 3) {
				player.notify("Usage: /gnick [group] [group prefix]");
		        return true;
			}

			if (split.length >= 3) {
				String nick = etc.combineSplit(2, split, " ");
		        sChat.groupnames.setString(split[1], nick);
		        player.sendMessage("\u00A77You changed group \u00A72" + split[1] + "'s\u00A77 name to \u00A72" + nick);
			}
			return true;
		}
		
		if (split[0].equalsIgnoreCase("/unnick") && player.canUseCommand("/unnick"))
		{
			sChat.nicknames.removeKey(player.getName());
	        player.sendMessage("\u00A77Your name has been removed");
	        return true;
		}
		
		if ((split[0].equalsIgnoreCase("/status")) && (player.canUseCommand("/status")))
		{
			if (split.length < 2)
			{
				player.notify("Usage: /status [afk/back/list]");
		        return true;
			}
			if (split[1].equalsIgnoreCase("afk"))
			{
				if (afkList.containsKey(player.getName()))
				{
					player.sendMessage("\u00A7f[\u00A77Status\u00A7f] \u00A77You are already AFK!");
				}
				else
				{
					afkList.put(player.getName(), System.nanoTime());
					etc.getServer().messageAll("\u00A7f[\u00A77Status\u00A7f] \u00A7e" + player.getName() + "\u00A77 is now \u00A74AFK");
				}
				return true;
			}
			if (split[1].equalsIgnoreCase("list"))
			{
				if (afkList.size() < 1)
				{
					player.sendMessage("[\u00A77Status\u00A7f] \u00A7cAFK List is empty!");
				}
				else
				{
					player.sendMessage("[\u00A77Status\u00A7f] \u00A7eAFK Players \u00A7f" + afkList.toString());
				}
		        return true;
			}
			if (split[1].equalsIgnoreCase("back")) {
				if (afkList.containsKey(player.getName())) {
					afkList.remove(player.getName());
					etc.getServer().messageAll("\u00A7f[\u00A77Status\u00A7f] \u00A7e" + player.getName() + "\u00A77 has returned");
				} else {
					player.sendMessage("\u00A7f[\u00A77Status\u00A7f] \u00A77You are not AFK!");
		        }
		        return true;
			}
		}
		return false;
	}
	
	public boolean onTimeChange(World world, long newValue)
	{
		return false;
	}
}