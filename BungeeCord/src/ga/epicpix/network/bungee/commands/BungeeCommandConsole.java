package ga.epicpix.network.bungee.commands;

import ga.epicpix.network.common.commands.CommandConsole;
import net.md_5.bungee.command.ConsoleCommandSender;

public class BungeeCommandConsole extends CommandConsole {

    private final ConsoleCommandSender sender;

    public BungeeCommandConsole(ConsoleCommandSender sender) {
        this.sender = sender;
    }

    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
}
