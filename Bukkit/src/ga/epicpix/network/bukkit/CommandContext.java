package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.PlayerInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class CommandContext {

    private boolean dataSet = false;
    private CommandSender sender;
    private PlayerInfo playerInfo;
    private String[] arguments;

    public final CommandContext setData(CommandSender sender, PlayerInfo playerInfo, String[] arguments) {
        if(!dataSet) {
            this.sender = sender;
            this.playerInfo = playerInfo;
            this.arguments = arguments;
            dataSet = true;
        }
        return this;
    }

    public final CommandSender getSender() {
        return sender;
    }

    public final String[] getArguments() {
        return arguments;
    }

    public final boolean isPlayer() {
        return sender instanceof Player;
    }

    public final Player getPlayer() {
        return (Player) sender;
    }

    public final boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    public final void sendMessage(String message) {
        getSender().sendMessage(message);
    }

    public final PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public abstract void run();
}
