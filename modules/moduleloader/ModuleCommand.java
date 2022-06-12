package moduleloader;

import ga.epicpix.network.bukkit.Command;
import ga.epicpix.network.bukkit.CommandContext;

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
                String[] arguments = getArguments();
                if(arguments.length>=1) {

                }else {
                    sendMessage("/red/Usage: /" + getCommandName() + " <list>");
                }
            }
        };
    }

}
