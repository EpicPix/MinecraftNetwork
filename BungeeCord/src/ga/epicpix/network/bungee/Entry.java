package ga.epicpix.network.bungee;

import ga.epicpix.network.bungee.commands.ServerCommand;
import ga.epicpix.network.common.Language;
import ga.epicpix.network.common.websocket.ClientType;
import ga.epicpix.network.common.websocket.WebSocketConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Entry extends Plugin {

    public static Entry INSTANCE;

    public void onLoad() {
        INSTANCE = this;
        WebSocketConnection.setClientType(ClientType.BUNGEE);
        WebSocketConnection.connect();
        Language.loadLanguages();

        Command.registerCommand(new ServerCommand());
    }

    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginListener());
    }

}
