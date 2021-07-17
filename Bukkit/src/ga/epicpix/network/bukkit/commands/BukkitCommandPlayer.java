package ga.epicpix.network.bukkit.commands;

import ga.epicpix.network.common.commands.CommandPlayer;
import org.bukkit.entity.Player;

public class BukkitCommandPlayer extends CommandPlayer {

    private final Player player;

    public BukkitCommandPlayer(Player player) {
        this.player = player;
    }

    public String getName() {
        return player.getName();
    }

    public Player getPlayer() {
        return player;
    }

}
