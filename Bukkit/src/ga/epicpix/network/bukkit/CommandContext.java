package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.AbstractCommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class CommandContext extends AbstractCommandContext<CommandSender, Player> {

    public CommandContext(String commandName) {
        super(commandName);
    }

    public final boolean isPlayer() {
        return getSender() instanceof Player;
    }

    public final boolean isConsole() {
        return getSender() instanceof ConsoleCommandSender;
    }

    public final void sendMessage(String message) {
        getSender().sendMessage(message);
    }

    public abstract void run();
}
