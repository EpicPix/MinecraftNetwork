package ga.epicpix.network.bungee;

import ga.epicpix.network.bungee.commands.TestCommand;
import ga.epicpix.network.common.Language;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Entry extends Plugin {

    public static Entry INSTANCE;

    public void onLoad() {
        INSTANCE = this;
        Language.loadLanguages();
        Language.startWatcher();

        Command.registerCommand(new TestCommand());
    }

    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginListener());
    }

}
