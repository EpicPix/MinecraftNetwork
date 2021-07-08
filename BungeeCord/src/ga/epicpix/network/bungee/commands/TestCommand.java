package ga.epicpix.network.bungee.commands;

import ga.epicpix.network.bungee.Command;
import ga.epicpix.network.bungee.CommandContext;

public class TestCommand extends Command {

    public String getName() {
        return "testbungee";
    }

    public String getRequiredPermission() {
        return null;
    }

    public CommandContext createContext() {
        return new CommandContext() {
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
