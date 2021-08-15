package ga.epicpix.network.bungee.commands;

import ga.epicpix.network.common.commands.CommandPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeCommandPlayer extends CommandPlayer {

    private final ProxiedPlayer player;

    public BungeeCommandPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    public String getName() {
        return player.getName();
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public void sendMessage(String message) {
        player.sendMessage(TextComponent.fromLegacyText(message));
    }
}
