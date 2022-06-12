package moduleloader;

import ga.epicpix.network.bukkit.Command;
import ga.epicpix.network.common.modules.Module;

public class ModuleEntry extends Module {

    public void enable() {
        Command.registerCommand(this, new ModuleCommand());
        Command.registerCommand(this, new ModulesCommand());
    }

}
