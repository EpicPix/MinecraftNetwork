package ga.epicpix.network.bukkit;

import ga.epicpix.network.bukkit.commands.TestCommand;
import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Language;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Entry extends JavaPlugin {

    public void onLoad() {
        Language.loadLanguages();
        Language.startWatcher();
        System.out.println("Server Info: " + BukkitCommon.getThisServer());
        System.out.println("Database Server Info: " + CommonUtils.getServerInfo(BukkitCommon.getServerId()));
        System.out.println("Updating DB");
        CommonUtils.updateServerInfo(BukkitCommon.getThisServer());
        System.out.println("Database Server Info: " + CommonUtils.getServerInfo(BukkitCommon.getServerId()));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> CommonUtils.removeServerInfo(BukkitCommon.getServerId())));
        Command.registerCommand(new TestCommand());
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new PluginListener(), this);
    }

}
