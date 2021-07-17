package ga.epicpix.network.bungee;

import ga.epicpix.network.bungee.commands.BungeeCommandConsole;
import ga.epicpix.network.bungee.commands.BungeeCommandPlayer;
import ga.epicpix.network.common.commands.CommonCommandContext;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class CommandContext extends CommonCommandContext {

    public CommandContext(String commandName) {
        super(commandName);
    }

    public final boolean isPlayer() {
        return getSender() instanceof BungeeCommandPlayer;
    }

    public final ProxiedPlayer getBungeePlayer() {
        return ((BungeeCommandPlayer) super.getPlayer()).getPlayer();
    }

    public final boolean isConsole() {
        return getSender() instanceof BungeeCommandConsole;
    }

    public final void sendMessage(String message) {
        getSender().sendMessage(message);
    }

    public abstract void run();
}
