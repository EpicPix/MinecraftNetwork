package ga.epicpix.network.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class Entry extends JavaPlugin {

    public void onLoad() {
        System.out.println("Server Info: " + BukkitCommon.getThisServer());
    }

}
