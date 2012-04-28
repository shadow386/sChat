import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

public class sCore {
	public static void loadSlaps()
	{
		if (!new File(sChat.slaps).exists())
		{
			FileWriter writer = null;
			try {
				writer = new FileWriter(sChat.slaps);
				writer.write("drives over %player\r\n");
				writer.write("shreds %player with a chainsaw\r\n");
				writer.write("steals %player's cookies. Mwahaha!\r\n");
				writer.write("pushes %player into a crevasse\r\n");
				writer.write("follows %player into a dark avenue. Hey test my crowbar!\r\n");
				writer.write("persuades %player to eat the cyanide sandwich\r\n");
				writer.write("sends some DDoS in %player's direction\r\n");
				writer.write("slaps %player around a bit with a large trout\r\n");
				writer.write("forgets %player in the desert. awww\r\n");
				writer.write("is searching for %player + goatsex on yahoo. WEEE %random hits!\r\n");
				writer.write("touches %player with a twenty-foot rusty halberd\r\n");
				writer.write("burns %player's house down\r\n");
				writer.write("throws a keyboard at %player\r\n");
				writer.write("beautifies %player with some knuckle duster imprints\r\n");
				writer.write("sends hordes of lemmings over %player\r\n");
				writer.write("aims his LOIC at %player and fires\r\n");
				writer.write("slaps %player around with a pink Macintosh\r\n");
				writer.write("whips %player with a mouse cord\r\n");
				writer.write("slaps %player around with nuclear waste\r\n");
				writer.write("slaps %player with Ozzy Osbourne\r\n");
				writer.write("sends the seven plagues of Egypt to %player\r\n");
				writer.write("throws a playstation at %player\r\n");
				writer.write("eats %player\r\n");
				writer.write("slaps %player in the face and yells \"I'm Rick James, bitch!\"\r\n");
				writer.write("slaps %player around with a Riemann rearrangement theorem\r\n");
				writer.write("prepares %player to be shown on rotten.com\r\n");
				writer.write("sneezes in %player's face\r\n");
				writer.write("stabs %player in the face\r\n");
				writer.write("smacks %player with a red Swingline stapler\r\n");
				writer.write("slaps %player around a bit with a small Imperial-Class Stardestroyer\r\n");
				writer.write("pokes %player with a stick\r\n");
				writer.write("slaps %player around with a large trout\r\n");
				writer.write("discovers %player's picture at uglypeople.com\r\n");
				writer.write("slaps %player around with Windows Me\r\n");
				writer.close();
			} catch (Exception e) {
				sChat.log.log(Level.SEVERE, "Exception while creating " + sChat.slaps);
				try {	
					if (writer != null)
						writer.close();
				} catch (IOException e1) {sChat.log.log(Level.SEVERE, "Exception while closing writer for "+ sChat.slaps, e1);}
			}
			finally {
				try	{
					if (writer != null)
						writer.close();
				} catch (IOException e) {sChat.log.log(Level.SEVERE, "Exception while closing writer for " + sChat.slaps, e);}
			}
		}
	}

	public void loadChannels()
	{
		String[] chanList = sChat.channels.getString("channels").split(",");
		for (int i = 0; i < chanList.length; i++)
		{
			sChat.listener.channels.add("#"+chanList[i]);
		}
	}

	public String getNickname(Player p)
	{
		if (sChat.nicknames.keyExists(p.getName()))
		{
			String[] nickname = sChat.nicknames.getString(p.getName()).split(",");
			return Colors.Marker+nickname[1]+nickname[0]+"\u00A7f";
	    }
		return p.getName();
	}
	
	public String getGroupName(Player p)
	{
		if (p.hasNoGroups())
		{ 
			return sChat.groupnames.getString("guest-prefix");
		}
		else if (sChat.groupnames.keyExists(p.getGroups()[0]))
		{
			return p.getColor()+sChat.groupnames.getString(p.getGroups()[0])+"\u00A7f";
		}
		return p.getColor()+p.getGroups()[0];
	}

	public String getChanName(String channel)
	{
		String chan = "";
		if(channel.startsWith("#"))
		{
			chan = channel.replace("#", "");
		}
		if (sChat.channels.keyExists(chan))
		{
			return sChat.channels.getString(chan);
		}
		return channel;
	}
	
	public String censor(String text) {
		String[] censorList = sChat.sconf.getString("censored-words").split(",");
	    for (int i = 0; i < censorList.length; i++)
	    {
	    	text = text.replaceAll("(?i)\\b"+censorList[i]+"\\b", countLetters(censorList[i]));
	    }
	    return text;
	}
	
	public String censorRemove(String text) {
		String n = "";
	    String[] censorList = sChat.sconf.getString("censored-words").split(",");
	    for (int i = 0; i < censorList.length; i++)
	    {
	    	if (!text.equalsIgnoreCase(censorList[i])) {
	    		n = n + censorList[i] + ",";
	    	}
	    }
	    return n;
	}
	  
	public String censorAdd(String text) {
	    String list = sChat.sconf.getString("censored-words");
	    list = list + text + ",";
	    return list;
	}
	
	public String countLetters(String str) {
		int counter = 0;
	    for (int i = 0; i < str.length(); i++) {
	      if (Character.isLetter(str.charAt(i)))
	        counter++;
	    }
	    str = buildAsterisk(counter);
	    return str;
	}

	public String buildAsterisk(int length) {
		String s = "";
	    for (int i = 0; i < length; i++) {
	    	s = s + "*";
	    }
	    return s;
	}
	
	public double getDistance(Player a, Player b)
	{
		double xPart = Math.pow(a.getX() - b.getX(), 2.0D);
		double yPart = Math.pow(a.getY() - b.getY(), 2.0D);
		double zPart = Math.pow(a.getZ() - b.getZ(), 2.0D);
		return Math.sqrt(xPart + yPart + zPart);
	}

	public void doLocal(Player player, String message)
	{
		int talkdistance = sChat.channels.getInt("local-distance");
		ArrayList<Player> recipients = new ArrayList<Player>();
		for (Player p : etc.getServer().getPlayerList())
		{
			if (getDistance(player, p) <= talkdistance && player.getLocation().dimension == p.getLocation().dimension)
			{
				recipients.add(p);
			}
		}
		if (recipients.size() > 1)
		{
			for (Player p : recipients)
			{
				p.sendMessage(message);
			}
		}
		else
		{
			player.sendMessage(message);
			player.sendMessage("\u00A77Nobody can hear you.");
		}
	}
	
	public void chat(Player player, String message)
	{
		for(Player p : sChat.listener.connected.keySet())
		{
			if(sChat.listener.connected.get(p).equalsIgnoreCase(sChat.listener.connected.get(player)))
			{
				p.sendMessage(message);
			}
		}
	}
	
	public void adminChat(Player player, String message)
	{
		String godChan = sChat.sconf.getString("god-channel");
		for(Player p : sChat.listener.connected.keySet())
		{
			if(sChat.listener.connected.get(p).equalsIgnoreCase("#"+godChan))
			{
				p.sendMessage(message);
			}
		}
	}
	
	public void part(Player player, String channel)
	{
		for(Player p : sChat.listener.connected.keySet())
		{
			if(sChat.listener.connected.get(p).equalsIgnoreCase(sChat.listener.connected.get(player)))
			{
				if(p == player)
				{
					player.sendMessage("[\u00A77Channels\u00A7f] You parted channel \u00A7e"+channel);
				}
				else
				{
					p.sendMessage("[\u00A77Channels\u00A7f] Player \u00A7e"+getNickname(player)+" \u00A7fhas parted channel \u00A7e"+channel);
				}
			}
		}
	}
	
	public void join(Player player, String channel)
	{
		for(Player p : sChat.listener.connected.keySet())
		{
			if(sChat.listener.connected.get(p).equalsIgnoreCase(sChat.listener.connected.get(player)))
			{
				if(p == player)
				{
					player.sendMessage("[\u00A77Channels\u00A7f] You join channel \u00A7e"+channel);
				}
				else
				{
					p.sendMessage("[\u00A77Channels\u00A7f] Player \u00A7e"+getNickname(player)+" \u00A7fhas joined channel \u00A7e"+channel);
				}
			}
		}
	}
	
	public String slap(Player player)
	{
		String line = "";
		Random generator = new Random();
		int num = 0;
		try {
			int number = count("plugins/sChat/slaps.txt");
			num = generator.nextInt(number);
	        FileReader fr = new FileReader("plugins/sChat/slaps.txt");
	        BufferedReader br = new BufferedReader(fr);
	    	for(int lineNo = 1; lineNo < number; lineNo++)
	    	{
	    		if (line.startsWith("#"))
	    		{
	  	          	break;
	  	        }
	    		if(lineNo == num)
	    		{
	    			line = br.readLine();
	    			break;
	    		}
	    		else br.readLine();
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
	        e.printStackTrace();
		}
		return line;
	}
	
	public String replace(String slap, String slapped)
	{
		Random generator = new Random();
	    int number = generator.nextInt(100) + 1;
	    String player = "%player";
	    String random = "%random";
	    if (slap.contains(player))
	    {
	      slap = slap.replaceAll(player, slapped);
	    }
	    if (slap.contains(random))
	    {
	      slap = slap.replaceAll(random, Integer.toString(number));
	    }
	    return slap;
	}

	public int count(String file) throws Exception {
		int lines = 0;
	    LineNumberReader lineCounter = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
	    String nextLine = null;
	    try {
	    	while ((nextLine = lineCounter.readLine()) != null)
	    	{
	    		if (nextLine.startsWith("#"))
	    		{
	    			continue;
	    		}
	    	if (nextLine != null) continue;
	    	}
	    	lines = lineCounter.getLineNumber();
	    } catch (Exception done) {
	    	done.printStackTrace();
	    }
	    return lines;
	}
	
	public String setColor(String text)
	{
		if (text.equals("red") || text.equals("4")) text = "4";
	    else if (text.equals("rose") || text.equals("c")) text = "c";
	    else if (text.equals("gold") || text.equals("6")) text = "6";
	    else if (text.equals("yellow") || text.equals("e")) text = "e";
	    else if (text.equals("green") || text.equals("2")) text = "2";
	    else if (text.equals("lime") || text.equals("a")) text = "a";
	    else if (text.equals("light-blue") || text.equals("b")) text = "b";
	    else if (text.equals("teal") || text.equals("3")) text = "3";
	    else if (text.equals("blue") || text.equals("1")) text = "1";
	    else if (text.equals("navy") || text.equals("9")) text = "9";
	    else if (text.equals("pink") || text.equals("d")) text = "d";
	    else if (text.equals("purple") || text.equals("5")) text = "5";
	    else if (text.equals("white") || text.equals("f")) text = "f";
	    else if (text.equals("light-grey") || text.equals("7")) text = "7";
	    else if (text.equals("dark-grey") || text.equals("8")) text = "8";
	    else if (text.equals("black") || text.equals("0")) text = "0";
	    else text = "false";
	    return text;
	}
	
	public String getColor(String text)
	{
		if (text.equals("4")) text = "red";
	    else if (text.equals("c")) text = "rose";
	    else if (text.equals("6")) text = "gold";
	    else if (text.equals("e")) text = "yellow";
	    else if (text.equals("2")) text = "green";
	    else if (text.equals("a")) text = "lime";
	    else if (text.equals("b")) text = "light-blue";
	    else if (text.equals("3")) text = "teal";
	    else if (text.equals("1")) text = "blue";
	    else if (text.equals("9")) text = "navy";
	    else if (text.equals("d")) text = "pink";
	    else if (text.equals("5")) text = "purple";
	    else if (text.equals("f")) text = "white";
	    else if (text.equals("7")) text = "light-grey";
	    else if (text.equals("8")) text = "dark-grey";
	    else if (text.equals("0")) text = "black";
	    else text = "white";
	    return text;
	}
}