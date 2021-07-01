package ga.epicpix.network.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class Entry extends JavaPlugin {

    public void onEnable() {
        System.out.println("Server Info: " + ServerInfo.getThisServer());
    }

}
