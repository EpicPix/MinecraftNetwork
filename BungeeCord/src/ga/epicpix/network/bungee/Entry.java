package ga.epicpix.network.bungee;

import ga.epicpix.network.common.modules.ModuleEventManager;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.servers.ServerVersion;
import ga.epicpix.network.common.net.websocket.ClientType;
import ga.epicpix.network.common.net.websocket.WebSocketConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Entry extends Plugin {

    public static Entry INSTANCE;

    public void onLoad() {
        INSTANCE = this;
        WebSocketConnection.setClientType(ClientType.BUNGEE);
        WebSocketConnection.connect();
        ServerVersion.load();
    }

    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginListener());


        ModuleEventManager.onEnableModule((module) -> {
            Command.moduleToCommands.put(module, new ArrayList<>());
        });

        ModuleEventManager.onDisableModule((module) -> {
            for(var cmd : Command.moduleToCommands.get(module)) {
                ProxyServer.getInstance().getPluginManager().unregisterCommand(cmd);
            }
            Command.moduleToCommands.remove(module);
        });

        try {
            ModuleLoader.enableModules(ModuleLoader.loadModules(new File("modules")));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        try {
            ModuleLoader.unloadModules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
