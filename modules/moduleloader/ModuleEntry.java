package moduleloader;

import ga.epicpix.network.bukkit.Command;
import ga.epicpix.network.common.modules.Module;

public class ModuleEntry extends Module {

    static ModuleEntry instance;

    public void enable() {
        instance = this;

        Command.registerCommand(this, new ModuleCommand());
        Command.registerCommand(this, new ModulesCommand());
    }

}
