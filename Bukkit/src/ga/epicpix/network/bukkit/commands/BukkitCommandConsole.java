package ga.epicpix.network.bukkit.commands;

import ga.epicpix.network.common.commands.CommandConsole;
import org.bukkit.command.ConsoleCommandSender;

public class BukkitCommandConsole extends CommandConsole {

    private final ConsoleCommandSender sender;

    public BukkitCommandConsole(ConsoleCommandSender sender) {
        this.sender = sender;
    }

    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
}
