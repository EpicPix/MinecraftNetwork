package ga.epicpix.network.bungee.commands;

import ga.epicpix.network.bungee.Command;
import ga.epicpix.network.bungee.CommandContext;
import ga.epicpix.network.common.servers.ServerInfo;
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
                        ServerInfo serverInfo = ServerInfo.getServerInfo(getArguments()[0]);
                        if(serverInfo==null) {
                            sendMessage("/red/Unknown server!");
                        }else {
                            net.md_5.bungee.api.config.ServerInfo currentServer = getBungeePlayer().getServer().getInfo();
                            if(serverInfo.id.equals(currentServer.getName()) || new InetSocketAddress(serverInfo.details.ip(), serverInfo.details.port()).equals(currentServer.getSocketAddress())) {
                                sendMessage("/red/Already connected to this server!");
                            }else {
                                getBungeePlayer().connect(ServerConnectRequest.builder()
                                        .retry(false)
                                        .reason(ServerConnectEvent.Reason.COMMAND)
                                        .connectTimeout(5000)
                                        .target(ProxyServer.getInstance().constructServerInfo(serverInfo.id, new InetSocketAddress(serverInfo.details.ip(), serverInfo.details.port()), "", false))
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
