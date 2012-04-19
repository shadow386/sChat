import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class sChat extends Plugin
{
	static final sListener listener = new sListener();
	public static Logger log = Logger.getLogger("Minecraft");
	public static String name = "sChat";
	public static String version = "3.0";
	public static String directory = "plugins/sChat/";
	public static String slaps = directory + "slaps.txt";
	
	public static PropertiesFile sconf;	
	public static PropertiesFile nicknames;
	public static PropertiesFile groupnames;
	public static PropertiesFile channels;
	public static PropertiesFile colors;
	
	static final sCore core = new sCore();
	
	public void enable() {
		log.log(Level.INFO, name + " plugin v" + version + " enabled");
	}
	
	public void disable() {
		log.log(Level.INFO, name + " plugin v" + version + " disabled");
	}
	
	public void initialize() {
		new File(directory).mkdir();
		
		nicknames = new PropertiesFile(directory + "nicknames.properties");
		groupnames = new PropertiesFile(directory + "groupnames.properties");
		channels = new PropertiesFile(directory + "channels.properties");
		colors = new PropertiesFile(directory + "colors.properties");
		sconf = new PropertiesFile(directory + "schat.properties");
		
		sconf.getString("time-format", "hh:mm:ss");
		sconf.getString("chat-format", "[%time]%wc[&f%group%wc] %name: %msg");
		sconf.getString("log-format", "<%name> %msg");
		sconf.getString("normal-world-color", "f");
		sconf.getString("end-world-color", "9");
		sconf.getString("nether-world-color", "4");
		sconf.setString("censored-words", "fuck,shit,dick,ass,cunt,asshole,bitch,chink,cock,cum,douche,dyke,fag,faggot,kike,nigger,piss,pussy,queer,slut,whore,");
		sconf.getBoolean("color-format", true);
		
		channels.getString("channels", "lobby,local,global,admin,derp");
	    channels.setBoolean("use-channels", true);
	    channels.setInt("local-distance", 35);
		
		etc.getLoader().addListener(PluginLoader.Hook.CHAT, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_MOVE, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_CONNECT, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_DISCONNECT, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.TIME_CHANGE, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.DISCONNECT, listener, this, PluginListener.Priority.MEDIUM);
	}
}