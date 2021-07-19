package ga.epicpix.network.bukkit;

import ga.epicpix.network.bukkit.commands.BukkitCommandConsole;
import ga.epicpix.network.bukkit.commands.BukkitCommandPlayer;
import ga.epicpix.network.common.commands.CommonCommandContext;
import org.bukkit.entity.Player;

public abstract class CommandContext extends CommonCommandContext {

    public CommandContext(String commandName) {
        super(commandName);
    }

    public final boolean isPlayer() {
        return getSender() instanceof BukkitCommandPlayer;
    }

    public final Player getBukkitPlayer() {
        return ((BukkitCommandPlayer) super.getPlayer()).getPlayer();
    }

    public final boolean isConsole() {
        return getSender() instanceof BukkitCommandConsole;
    }

    public final void sendMessage(String message) {
        getSender().sendMessage(message);
    }

    public abstract void run();
}
