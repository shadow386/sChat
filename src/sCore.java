import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

	public static void loadChannels()
	{
		String[] chanList = sChat.channels.getString("channels").split(",");
		for (int i = 0; i < chanList.length; i++)
		{
			sListener.channels.add("#"+chanList[i]);
		}
	}

	public String getNickname(Player p)
	{
		if (sChat.nicknames.keyExists(p.getName()))
		{
			String nickname = sChat.nicknames.getString(p.getName());
			if (sChat.sconf.getBoolean("color-format")) nickname = p.getColor()+nickname+"\u00A7f";
			return nickname;
	    }
		return p.getName();
	}
	
	public String getGroupName(Player p)
	{
		if (sChat.groupnames.keyExists(p.getName()))
		{
			String groupname = sChat.groupnames.getString(p.getGroups()[0]);
			if (sChat.sconf.getBoolean("color-chat")) groupname = p.getColor()+groupname+"\u00A7f";
			return groupname;
	    }
		return p.getGroups()[0];
	}

	public String chanName(String channel)
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

	public String getColor(Player p)
	{
		if (sChat.colors.keyExists(p.getName()))
		{
			return sChat.colors.getString(p.getName());
		}
		return p.getName();
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
}