package moduleloader;

import ga.epicpix.network.bukkit.Command;
import ga.epicpix.network.bukkit.CommandContext;

import java.util.Arrays;

public class ModuleCommand extends Command {

    public String getName() {
        return "module";
    }

    public String getRequiredPermission() {
        return "command.module";
    }

    public CommandContext createContext() {
        return new CommandContext(getName()) {
            public void run() {
                sendMessage("Arguments: " + Arrays.toString(getArguments()));
            }
        };
    }

}
