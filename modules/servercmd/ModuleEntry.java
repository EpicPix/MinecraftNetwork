package servercmd;

import ga.epicpix.network.bungee.Command;
import ga.epicpix.network.common.modules.Module;

public class ModuleEntry extends Module {

    public void enable() {
        Command.registerCommand(this, new ServerCommand());
    }

}
