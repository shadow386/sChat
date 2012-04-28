import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class sChat extends Plugin
{
	static final sListener listener = new sListener();
	public static Logger log = Logger.getLogger("Minecraft");
	public static String name = "sChat";
	public static String author = "Shadow386";
	public static String version = "3.0";
	public static String directory = "plugins/sChat/";
	public static String slaps = directory + "slaps.txt";
	
	public static PropertiesFile sconf;	
	public static PropertiesFile nicknames;
	public static PropertiesFile groupnames;
	public static PropertiesFile channels;
	
	static final sCore core = new sCore();
	
	public void enable()
	{
		etc.getInstance().addCommand("/nick", "[Nickname] - Changes your nickname");
	    etc.getInstance().addCommand("/groupname", "[Group] [Group Nickname] - Changes the group's prefix.");
	    etc.getInstance().addCommand("/status", "[afk/list/back] - Changes your status");
	    etc.getInstance().addCommand("/slap", "[Player] - Slaps a player");
	    etc.getInstance().addCommand("/shout", "[message] - Announces a message to the server");
	    etc.getInstance().addCommand("/chan join", "#channel - Joins a channel");
	    etc.getInstance().addCommand("/chan part", "#channel - Parts a channel");
	    etc.getInstance().addCommand("/chan list", "#channel - Lists players in channel");
		log.log(Level.INFO, name+" by "+author+" plugin v"+version+" enabled");
	}
	
	public void disable()
	{
		etc.getInstance().removeCommand("/nick");
	    etc.getInstance().removeCommand("/groupname");
	    etc.getInstance().removeCommand("/status");
	    etc.getInstance().removeCommand("/slap");
	    etc.getInstance().removeCommand("/shout");
	    etc.getInstance().removeCommand("/chan");
		log.log(Level.INFO, name+" plugin v"+version+" disabled");
	}
	
	public void initialize() {
		new File(directory).mkdir();
		
		nicknames = new PropertiesFile(directory + "nicknames.properties");
		groupnames = new PropertiesFile(directory + "groupnames.properties");
		channels = new PropertiesFile(directory + "channels.properties");
		sconf = new PropertiesFile(directory + "schat.properties");
		
		sconf.getString("time-format", "hh:mm:ss");
		sconf.getString("chat-format", "[%time][%chan]%wc[%group%wc] %name: %msg");
		sconf.getString("log-format", "<%name> %msg");
		sconf.getString("normal-world-color", "f");
		sconf.getString("end-world-color", "8");
		sconf.getString("nether-world-color", "4");
		sconf.setString("censored-words", "fuck,shit,dick,ass,cunt,asshole,bitch,chink,cock,cum,douche,dyke,fag,faggot,kike,nigger,piss,pussy,queer,slut,whore,");
		sconf.getBoolean("color-format", true);
		sconf.getBoolean("use-censor", true);
		sconf.getInt("afk-delay", 30);
		
		channels.getString("channels", "lobby,local,global,admin,god");
	    channels.getString("default-channel", "lobby");
	    channels.getString("admin-channel", "admin");
	    channels.getString("local-channel", "local");
	    channels.getString("god-channel", "god");
	    channels.getBoolean("use-channels", true);
	    channels.getInt("local-distance", 35);
		
		etc.getLoader().addListener(PluginLoader.Hook.CHAT, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_MOVE, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_CONNECT, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_DISCONNECT, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.TIME_CHANGE, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.DISCONNECT, listener, this, PluginListener.Priority.MEDIUM);
		
		core.loadChannels();
	}
}