package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.CommonUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PluginListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        CommonUtils.updateServerInfo(BukkitCommon.getThisServer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        CommonUtils.updateServerInfo(BukkitCommon.getThisServer());
    }

}
