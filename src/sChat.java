import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class sChat extends Plugin {
	static final sListener listener = new sListener();
	public static Logger log = Logger.getLogger("Minecraft");
	public static String name = "sChat";
	public static String version = "3.0";
	public static String author = "Shadow386";
	
	public static String directory = "plugins/sChat/";
	public static String slaps = directory+"slaps.txt";
	public static PropertiesFile nicknames;
	public static PropertiesFile groupnames;
	public static PropertiesFile worldnames;
	public static PropertiesFile listnames;
	public static PropertiesFile grouplistnames;
	public static PropertiesFile sconf;
	public static PropertiesFile colors;
	public static PropertiesFile channels;
	
	public void disable() {
		log.log(Level.INFO, name + " plugin v" + version + " disabled");
	}

	public void enable() {
		log.log(Level.INFO, name + " plugin v" + version + " enabled");
	}

	public void initialize() {
	    new File(directory).mkdir();
	    sconf = new PropertiesFile(directory + "schat.properties");
	    nicknames = new PropertiesFile(directory + "nicknames.conf");
	    listnames = new PropertiesFile(directory + "listnames.conf");
	    worldnames = new PropertiesFile(directory + "worldnames.conf");
	    groupnames = new PropertiesFile(directory + "groupnames.conf");
	    grouplistnames = new PropertiesFile(directory + "listnames.conf");
	    colors = new PropertiesFile(directory + "colors.conf");
	    channels = new PropertiesFile(directory + "channels.conf");
	    
	    sconf.getString("chat-format", "[] %player: %message");
	    sconf.getString("color-symbol", "&");
	    
		loadHooks();
		sCore.loadChannels();
		sCore.loadSlaps();
	}
	
	private void loadHooks() {
		etc.getLoader().addListener(PluginLoader.Hook.CHAT, listener, this, PluginListener.Priority.MEDIUM);
	    etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
	    etc.getLoader().addListener(PluginLoader.Hook.DISCONNECT, listener, this, PluginListener.Priority.MEDIUM);
	    etc.getLoader().addListener(PluginLoader.Hook.PLAYER_MOVE, listener, this, PluginListener.Priority.MEDIUM);
	    etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.MEDIUM);
	}
}