package ga.epicpix.network.bungee;

import ga.epicpix.network.common.PlayerInfo;
import ga.epicpix.network.common.ranks.RankManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginListener implements Listener {

    @EventHandler
    public void onPlayerJoinNetwork(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        PlayerInfo info = PlayerInfo.getPlayerInfo(player.getUniqueId());
        if(info==null) {
            info = new PlayerInfo().populate(player.getUniqueId(), player.getName(), RankManager.getDefaultRank());
        }
        info.lastLogin = System.currentTimeMillis();
        PlayerInfo.updatePlayerInfo(info);
    }

}
