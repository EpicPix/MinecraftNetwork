package ga.epicpix.network.bungee.commands;

import ga.epicpix.network.bungee.Command;
import ga.epicpix.network.bungee.CommandContext;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.servers.ServerManager;
import ga.epicpix.network.common.websocket.Errorable;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.event.ServerConnectEvent;

import java.net.InetSocketAddress;

public class ServerCommand extends Command {

    public String getName() {
        return "server";
    }

    public String getRequiredPermission() {
        return "command.server";
    }

    public CommandContext createContext() {
        return new CommandContext(getName()) {
            public void run() {
                if(isPlayer()) {
                    if(getArguments().length>=1) {
                        Errorable<ServerInfo> rserverInfo = ServerManager.getServerInfo(getArguments()[0]);
                        if(rserverInfo.hasFailed()) {
                            sendMessage("/red/Unknown server!");
                        }else {
                            ServerInfo serverInfo = rserverInfo.getValue();
                            net.md_5.bungee.api.config.ServerInfo currentServer = getBungeePlayer().getServer().getInfo();
                            if(serverInfo.id.equals(currentServer.getName()) || new InetSocketAddress(serverInfo.details.getIp(), serverInfo.details.getPort()).equals(currentServer.getSocketAddress())) {
                                sendMessage("/red/Already connected to this server!");
                            }else {
                                getBungeePlayer().connect(ServerConnectRequest.builder()
                                        .retry(false)
                                        .reason(ServerConnectEvent.Reason.COMMAND)
                                        .connectTimeout(5000)
                                        .target(ProxyServer.getInstance().constructServerInfo(serverInfo.id, new InetSocketAddress(serverInfo.details.getIp(), serverInfo.details.getPort()), "", false))
                                        .callback((result, throwable) -> {
                                            if (result == ServerConnectRequest.Result.FAIL) {
                                                sendMessage("/red/Could not connect to this server!");
                                            } else if (result == ServerConnectRequest.Result.ALREADY_CONNECTING) {
                                                sendMessage("/red/Already trying to connect to this server!");
                                            }
                                        })
                                        .build());
                            }
                        }
                    }else {
                        sendUsage();
                    }
                }else {
                    sendMessage("/red/This command can only be run by players!");
                }
            }
        };
    }

}
