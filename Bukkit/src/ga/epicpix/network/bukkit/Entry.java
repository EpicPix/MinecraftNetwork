package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.CommonUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class Entry extends JavaPlugin {

    public void onLoad() {
        System.out.println("Server Info: " + BukkitCommon.getThisServer());
        System.out.println("Database Server Info: " + CommonUtils.getServerInfo(BukkitCommon.getServerId()));
        System.out.println("Updating DB");
        CommonUtils.updateServerInfo(BukkitCommon.getThisServer());
        System.out.println("Database Server Info: " + CommonUtils.getServerInfo(BukkitCommon.getServerId()));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> CommonUtils.removeServerInfo(BukkitCommon.getServerId())));
    }

}
