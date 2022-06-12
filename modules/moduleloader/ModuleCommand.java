package moduleloader;

import ga.epicpix.network.bukkit.Command;
import ga.epicpix.network.bukkit.CommandContext;
import ga.epicpix.network.common.modules.ModuleLibrary;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModuleManager;

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
                    if(arguments[0].equals("load")) {
                        if(arguments.length >= 3) {
                            var resp = ModuleManager.getModule(arguments[1], arguments[2]);
                            if(resp.hasFailed()) {
                                if(resp.getErrorCode() == 0x28) {
                                    sendMessage("/red/Module could not be found on remote");
                                    return;
                                }
                                sendMessage("/red/Failed to get module /aqua/" + arguments[1] + "/red/ version /aqua/" + arguments[2]);
                                return;
                            }
                            var file = resp.getValue();
                            if(file.getData().getLibrary() != ModuleLibrary.BUKKIT) {
                                sendMessage("/red/Cannot use a non bukkit library");
                                return;
                            }
                            if(ModuleEntry.instance.getData().getId().equals(file.getData().getId())) {
                                sendMessage("/red/Unable to reload /aqua/" + arguments[1] + "/green/ version /aqua/" + arguments[2]);
                                return;
                            }

                            var modules = ModuleLoader.getLoadedModules();
                            for(var module : modules) {
                                if(module.getData().getId().equals(file.getData().getId())) {
                                    ModuleLoader.unloadModule(module);
                                }
                            }
                            var module = ModuleLoader.loadModule(file);
                            sendMessage("/green/Loaded /aqua/" + file.getData().getName() + "/green/ version /aqua/" + file.getData().getVersion());
                            ModuleLoader.enableModule(module);
                            sendMessage("/green/Enabled /aqua/" + file.getData().getName() + "/green/ version /aqua/" + file.getData().getVersion());
                        }else {
                            sendMessage("/red/Usage: /" + getCommandName() + " load <id> <version>");
                        }
                    }else if(arguments[0].equals("unload")) {
                        if(arguments.length >= 2) {
                            if(ModuleEntry.instance.getData().getId().equals(arguments[1])) {
                                sendMessage("/red/Unable to unload /aqua/" + arguments[1] + "/green/ version /aqua/" + ModuleEntry.instance.getData().getVersion());
                                return;
                            }

                            var modules = ModuleLoader.getLoadedModules();
                            for(var module : modules) {
                                if(module.getData().getId().equals(arguments[1])) {
                                    ModuleLoader.unloadModule(module);
                                    sendMessage("/green/Unloaded /aqua/" + module.getData().getName() + "/green/ version /aqua/" + module.getData().getVersion());
                                    return;
                                }
                            }

                            sendMessage("/red/Module with id /aqua/" + arguments[1] + "/red/ not found");
                        }else {
                            sendMessage("/red/Usage: /" + getCommandName() + " unload <id>");
                        }
                    }else {
                        sendMessage("/red/Usage: /" + getCommandName() + " <load/unload>");
                    }
                }else {
                    sendMessage("/red/Usage: /" + getCommandName() + " <load/unload>");
                }
            }
        };
    }

}
