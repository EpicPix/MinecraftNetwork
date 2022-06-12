package moduleloader;

import ga.epicpix.network.bukkit.Command;
import ga.epicpix.network.bukkit.CommandContext;
import ga.epicpix.network.common.modules.*;
import ga.epicpix.network.common.net.websocket.Errorable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ModulesCommand extends Command {

    public String getName() {
        return "modules";
    }

    public String getRequiredPermission() {
        return "command.module";
    }

    public CommandContext createContext() {
        return new CommandContext(getName()) {
            public void run() {
                String[] arguments = getArguments();
                if(arguments.length>=1) {
                    if(arguments[0].equals("remote")) {
                        var response = ModuleManager.getModules();
                        if(response.hasFailed()) {
                            sendMessage("/red/Failed to get modules on remote");
                            return;
                        }
                        var modules = response.getValue();
                        sendMessage("/green/Modules on remote (" + modules.size() + "):");
                        for(var data : modules) {
                            sendMessage(String.format("/green/ - Module /aqua/%s/green/ (/aqua/%s/green/) version /aqua/%s/green/, permissions: /aqua/%s/green/",
                                data.getName(),
                                data.getId(),
                                data.getVersion(),
                                Arrays.stream(data.getPermissions()).map(Enum::name).collect(Collectors.joining("/green/, /aqua/"))));
                        }
                    }else {
                        sendMessage("/red/Usage: /" + getCommandName() + " [remote]");
                    }
                    return;
                }

                var modules = ModuleLoader.getLoadedModules();
                sendMessage("/green/Loaded modules (" + modules.length + "):");
                for(var module : modules) {
                    var data = module.getData();
                    sendMessage(String.format("/green/ - Module /aqua/%s/green/ (/aqua/%s/green/) version /aqua/%s/green/, permissions: /aqua/%s/green/",
                        data.getName(),
                        data.getId(),
                        data.getVersion(),
                        Arrays.stream(data.getPermissions()).map(Enum::name).collect(Collectors.joining("/green/, /aqua/"))));
                }
            }
        };
    }

}
