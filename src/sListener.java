import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class sListener extends PluginListener {
	public static ArrayList<String> channels = new ArrayList<String>();
	public static Map<Player, String> connected = new HashMap<Player, String>();
	
	  public boolean onChat(Player player, String message){
		  etc.getServer().messageAll(sCore.getFormat(player, message));
		  return true;
	  }
	
	public boolean isConnected(Player p, String s){
		if(connected.containsKey(p) && connected.get(p) == s)
			return true;
		return false;
	}
}
