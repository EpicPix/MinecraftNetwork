package ga.epicpix.network.bukkit.commands;

import ga.epicpix.network.bukkit.Command;
import ga.epicpix.network.bukkit.CommandContext;

public class TestCommand extends Command {

    public String getName() {
        return "test";
    }

    public String getRequiredPermission() {
        return "hello";
    }

    public CommandContext createContext() {
        return new CommandContext(getName()) {
            public void run() {
                if(isPlayer()) {
                    sendMessage("Player");
                }else if(isConsole()) {
                    sendMessage("Console");
                }else {
                    sendMessage("Unknown");
                }
            }
        };
    }

}
