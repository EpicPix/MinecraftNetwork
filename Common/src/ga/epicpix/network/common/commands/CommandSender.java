package ga.epicpix.network.common.commands;

import ga.epicpix.network.common.text.ChatColor;

public abstract class CommandSender {

    public abstract String getName();

    public void sendMessage(String message) {
        System.out.println(ChatColor.removeColorCodes(message));
    }

}
