package ga.epicpix.network.bungee;

import ga.epicpix.network.common.AbstractCommandContext;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

public abstract class CommandContext extends AbstractCommandContext<CommandSender, ProxiedPlayer> {

    public CommandContext(String commandName) {
        super(commandName);
    }

    public final boolean isPlayer() {
        return getSender() instanceof ProxiedPlayer;
    }

    public final boolean isConsole() {
        return getSender() instanceof ConsoleCommandSender;
    }

    public final void sendMessage(String message) {
        getSender().sendMessage(message);
    }

    public abstract void run();
}
