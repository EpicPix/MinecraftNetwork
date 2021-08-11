package ga.epicpix.network.bungee;

import ga.epicpix.network.bungee.commands.ServerCommand;
import ga.epicpix.network.common.servers.ServerVersion;
import ga.epicpix.network.common.http.websocket.ClientType;
import ga.epicpix.network.common.http.websocket.WebSocketConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Entry extends Plugin {

    public static Entry INSTANCE;

    public void onLoad() {
        INSTANCE = this;
        WebSocketConnection.setClientType(ClientType.BUNGEE);
        WebSocketConnection.connect();
        ServerVersion.load();

        Command.registerCommand(new ServerCommand());
    }

    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginListener());
    }

}
