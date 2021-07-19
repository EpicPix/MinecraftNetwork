package ga.epicpix.network.bungee.commands;

import ga.epicpix.network.bungee.Command;
import ga.epicpix.network.bungee.CommandContext;
import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Mongo;
import ga.epicpix.network.common.servers.ServerInfo;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.event.ServerConnectEvent;
import org.bson.Document;

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
                        ServerInfo serverInfo = CommonUtils.documentToObject(Mongo.getCollection("data", "servers").find(new Document().append("id", getArguments()[0])).first(), ServerInfo.class);
                        if(serverInfo==null) {
                            sendTranslated("error.server.unknown");
                        }else {
                            net.md_5.bungee.api.config.ServerInfo currentServer = getBungeePlayer().getServer().getInfo();
                            if(serverInfo.id.equals(currentServer.getName()) || new InetSocketAddress(serverInfo.details.ip(), serverInfo.details.port()).equals(currentServer.getSocketAddress())) {
                                sendTranslated("error.server.already_connected");
                            }else {
                                getBungeePlayer().connect(ServerConnectRequest.builder()
                                        .retry(false)
                                        .reason(ServerConnectEvent.Reason.COMMAND)
                                        .connectTimeout(5000)
                                        .target(ProxyServer.getInstance().constructServerInfo(serverInfo.id, new InetSocketAddress(serverInfo.details.ip(), serverInfo.details.port()), "", false))
                                        .callback((result, throwable) -> {
                                            if (result == ServerConnectRequest.Result.FAIL) {
                                                sendTranslated("error.server.connect");
                                            } else if (result == ServerConnectRequest.Result.ALREADY_CONNECTING) {
                                                sendTranslated("error.server.connecting");
                                            }
                                        })
                                        .build());
                            }
                        }
                    }else {
                        sendUsage();
                    }
                }else {
                    sendTranslated("error.command.only_player");
                }
            }
        };
    }

}
