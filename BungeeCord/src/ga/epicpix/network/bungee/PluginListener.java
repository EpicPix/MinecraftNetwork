package ga.epicpix.network.bungee;

import ga.epicpix.network.common.players.PlayerManager;
import ga.epicpix.network.common.net.websocket.requests.data.UpdatePlayerRequest;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginListener implements Listener {

    @EventHandler
    public void onPlayerJoinNetwork(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        PlayerManager.updatePlayerOrCreate(player.getUniqueId(), player.getName(), new UpdatePlayerRequest.Data().setLastLogin(System.currentTimeMillis()));
    }

}
