import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class sListener extends PluginListener
{
	public SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public HashMap<String, Long> afkList = new HashMap<String, Long>();
	public HashMap<Player, Long> lastAction = new HashMap<Player, Long>();
	public ArrayList<String> whitelist = new ArrayList<String>();
	public ArrayList<String> channels = new ArrayList<String>();
	public Map<Player, String> connected = new HashMap<Player, String>();
	
	public boolean onChat(Player player, String message)
	{
		lastAction.put(player, new Date().getTime());
		
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
		
		if(!connected.containsKey(player))
		{
			connected.put(player, "#"+sChat.channels.getString("default-channel"));
		}
		  
		Date now = new Date();
		Format formatter = new SimpleDateFormat(sChat.sconf.getString("time-format"));
		String timeFormat = Colors.LightGray+formatter.format(now)+Colors.White;
		String chatFormat = sChat.sconf.getString("chat-format");
		String logFormat = sChat.sconf.getString("log-format");
		String worldColor = sChat.sconf.getString(player.getWorld().getType().toString().toLowerCase()+"-world-color");
		String locChan = sChat.channels.getString("local-channel");
		String godChan = sChat.channels.getString("god-channel");
		
		if (logFormat.contains("%msg")) logFormat = logFormat.replaceAll("%msg", message);
		if (logFormat.contains("%group")) logFormat = logFormat.replaceAll("%group", player.getGroups()[0]);
		if (logFormat.contains("%name")) logFormat = logFormat.replaceAll("%name", player.getName());
		if (logFormat.contains("\u00A7")) logFormat = logFormat.replaceAll("\u00A7", "&");
		
		sChat.log.info(logFormat);
		
		if (sChat.sconf.getBoolean("use-censor")) message = sChat.core.censor(message);
		if ((player.canUseCommand("/colorchat") || player.canUseCommand("/cchat"))&& message.contains("&")) message = message.replaceAll("&", "\u00A7");
		if (chatFormat.contains("&")) chatFormat = chatFormat.replaceAll("&", "\u00A7");
		if (chatFormat.contains("%time")) chatFormat = chatFormat.replaceAll("%time", timeFormat);
		if (chatFormat.contains("%msg")) chatFormat = chatFormat.replaceAll("%msg", Colors.White+message);
		if (chatFormat.contains("%group")) chatFormat = chatFormat.replaceAll("%group", sChat.core.getGroupName(player));
		if (chatFormat.contains("%name")) chatFormat = chatFormat.replaceAll("%name", Colors.White+sChat.core.getNickname(player)+Colors.White);
		if (chatFormat.contains("%wc")) chatFormat = chatFormat.replaceAll("%wc", Colors.Marker+worldColor);
		if (chatFormat.contains("%chan") && sChat.channels.getBoolean("use-channels")) chatFormat = chatFormat.replaceAll("%chan", sChat.core.getChanName(connected.get(player)));
		
		if ((message.isEmpty()) || (message.length() < 1))
		{
			return false;
		}
		
		if (sChat.sconf.getBoolean("use-channels"))
		{
			if(connected.get(player).equalsIgnoreCase("#"+locChan))
			{
				sChat.core.doLocal(player, chatFormat);
				sChat.core.adminChat(player, chatFormat);
			}
			else if(connected.get(player).equalsIgnoreCase("#"+godChan))
			{
				etc.getServer().messageAll(chatFormat);
			}
			else
			{
				sChat.core.chat(player, chatFormat);
				sChat.core.adminChat(player, chatFormat);
			}
		}
		else
		{
			etc.getServer().messageAll(chatFormat);
		}
		return true;
	}
	
	public boolean onCommand(Player player, String[] split)
	{
		lastAction.put(player, new Date().getTime());
		
		if (split[0].equalsIgnoreCase("/load"))
		{
			sChat.sconf.getString(player.getWorld().getType().toString().toLowerCase()+"-world-color", "123");
			player.sendMessage("Done!");
			return true;
		}
		
		if ((split[0].equalsIgnoreCase("/nickname") || split[0].equalsIgnoreCase("/nick")) && player.canUseCommand("/nickname"))
		{
			
			if (split.length < 2)
			{
				player.notify("Usage: /nick [nickname]");
		        return true;
			}
			
			Player ouser = etc.getServer().matchPlayer(split[1]);
			if (ouser != null && split.length > 2)
			{				String newNick = etc.combineSplit(2, split, " ");
				
				if (newNick.length() > 12)
				{
					player.notify("Nickname is too long! Cannot be longer than 12 characters");
					return true;
				}
				
				sChat.nicknames.setString(ouser.getName(), newNick+",f");
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
				
				sChat.nicknames.setString(player.getName(), newNick+",f");
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
		
		if ((split[0].equalsIgnoreCase("/color")) && (player.canUseCommand("/color")))
		{
			if (sChat.sconf.getBoolean("use-colors"))
			{
				if (split.length < 2)
				{
					player.notify("Correct usage is: /color [color]");
					return true;
				}
				String color = sChat.core.setColor(split[1]);
				{
					sChat.nicknames.setString(player.getName(), sChat.core.getNickname(player)+","+color);
					player.sendMessage("\u00A77Successfully updated your color to \u00A7" + sChat.core.getColor(color) + color + "\u00A77.");
				}
			}
			else
			{
				player.notify("Chat colors are not enabled!");
			}
			return true;
		}
		
		if (split[0].equalsIgnoreCase("/chan") && player.canUseCommand("/chan") && sChat.channels.getBoolean("use-channels"))
		{
			if (split.length < 2)
			{
				player.notify("Correct usage is: /chan [join/part/list] [#channel]");
				return true;
			}
			
			if(split[1].equalsIgnoreCase("join"))
			{
				if (split.length < 3)
				{
					player.notify("Correct usage is: /chan join [#channel]");
					return true;
				}
				
				if (!channels.contains(split[2]) && !split[2].startsWith("#"))
				{
					player.notify("Channel does not exist");
					return true;
				}
				
				if (connected.get(player).equalsIgnoreCase(split[2]))
				{
					player.notify("You are already in that channel!");
					return true;
				}
				
				if ((split[2].equalsIgnoreCase("#"+sChat.channels.getString("admin-channel")) || split[2].equalsIgnoreCase("#"+sChat.channels.getString("admin-channel"))) && !player.isAdmin())
				{
					player.notify("You are are not permitted to be in that channel!");
					return true;
				}
				else
				{
					sChat.core.part(player, connected.get(player));
					connected.put(player, split[2]);
					if(split[1].equalsIgnoreCase("#"+sChat.channels.getString("local-channel")))
					{
						int talkdistance = sChat.sconf.getInt("local-distance");
						ArrayList<Player> recipients = new ArrayList<Player>();
						for (Player p : etc.getServer().getPlayerList())
						{
							if (sChat.core.getDistance(player, p) <= talkdistance && player.getLocation().dimension == p.getLocation().dimension)
							{
								recipients.add(p);
							}
						}
						for (Player p : recipients)
						{
							if(p == player)
							{
								player.sendMessage("[\u00A77Channels\u00A7f] You joined channel \u00A7e"+split[2]);
							}
							else
							{
								p.sendMessage("[\u00A77Channels\u00A7f] Player \u00A7e"+sChat.core.getNickname(player)+" \u00A7fhas joined channel \u00A7e"+split[1]);
							}
						}
					}
					else
					{
						sChat.core.join(player, split[2]);
					}
				}
				return true;
			}
			else if(split[1].equalsIgnoreCase("part"))
			{
				if (split.length < 3)
				{
					player.notify("Correct usage is: /chan part [#channel]");
					return true;
				}
				
				if (!channels.contains(split[2]) && !split[2].startsWith("#"))
				{
					player.notify("Channel does not exist");
					return true;
				}
				
				if (!connected.get(player).equalsIgnoreCase(split[2]))
				{
					player.notify("You aren't in that channel!");
					return true;
				}
				
				connected.put(player, "#"+sChat.sconf.getString("default-channel"));
				if(split[2].equalsIgnoreCase("#"+sChat.channels.getString("local-channel")))
				{
					int talkdistance = sChat.sconf.getInt("local-distance");
					ArrayList<Player> recipients = new ArrayList<Player>();
					for (Player p : etc.getServer().getPlayerList())
					{
						if (sChat.core.getDistance(player, p) <= talkdistance && player.getLocation().dimension == p.getLocation().dimension)
						{
							recipients.add(p);
						}
					}
					for (Player p : recipients)
					{
						if(p == player)
						{
							player.sendMessage("[\u00A77Channels\u00A7f] You parted channel \u00A7e"+split[2]);
						}
						else
						{
							p.sendMessage("[\u00A77Channels\u00A7f] Player \u00A7e"+sChat.core.getNickname(player)+" \u00A7fhas parted channel \u00A7e"+split[2]);
						}
					}
				}
				else
				{
					sChat.core.part(player, split[2]);
				}
				return true;
			}
			else if(split[1].equalsIgnoreCase("list"))
			{
				if(split.length == 3)
				{
					Player p = etc.getServer().matchPlayer(split[2]);
					if (p != null)
					{
						player.sendMessage("[\u00A77Channels\u00A7f] "+p.getName()+" is in \u00A7e"+connected.get(p));
					}
		        }
				else if(split.length == 2)
				{
		           	player.sendMessage("[\u00A77Channels\u00A7f] Channel list: \u00A7e"+channels.toString());
		        }
				return true;
			}
		}		
		return false;
	}
	
	public boolean onTimeChange(World world, long newValue)
	{
		for (Player player : etc.getServer().getPlayerList())
		{
			if (lastAction.containsKey(player))
			{
				long time = lastAction.get(player);
				if(time == time + (sChat.sconf.getInt("afk-delay") * 1000))
				{
					afkList.put(player.getName(), System.nanoTime());
					etc.getServer().messageAll("\u00A7f[\u00A77Status\u00A7f] \u00A7e" + player.getName() + "\u00A77 is now \u00A74AFK");
				}
			}
		}
		return false;
	}
	
	public void onDisconnect(Player player)
	{
		if (sChat.sconf.getBoolean("use-channels") && connected.containsKey(player))
		{
   			connected.remove(player);
  		}
		
		if (lastAction.containsKey(player))
  		{
  			lastAction.remove(player);
  		}
		
		if (afkList.containsKey(player.getName()))
		{
			afkList.remove(player.getName());
		}
	}
	
  	public void onLogin(Player player) 
  	{
  		lastAction.put(player, new Date().getTime());
  		
  		if (sChat.sconf.getBoolean("use-channels")) 
  		{
  			if(!connected.containsKey(player) && player.isAdmin())
  			{
  				connected.put(player, "#"+sChat.sconf.getString("god-channel"));
  			}
  			else if(!connected.containsKey(player))
  			{
  				connected.put(player, "#"+sChat.sconf.getString("default-channel"));
  			}
  		}
  	}
  
  	public void onPlayerMove(Player player, Location from, Location to)
  	{
  		lastAction.put(player, new Date().getTime());
  		
  		if (afkList.containsKey(player.getName()))
  		{
  			afkList.remove(player.getName());
  			etc.getServer().messageAll("[\u00A77Status\u00A7f] \u00A7e" + player.getName() + "\u00A77 has returned");
  		}
  	}
}