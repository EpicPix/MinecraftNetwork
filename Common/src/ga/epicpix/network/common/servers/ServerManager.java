package ga.epicpix.network.common.servers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.Opcodes;
import ga.epicpix.network.common.net.websocket.requests.WebsocketRequest;
import ga.epicpix.network.common.net.websocket.requests.data.*;

import java.util.ArrayList;

public class ServerManager {

    public static Errorable<ServerInfo> updateServer(String server, UpdateServerDataRequest.Data data) {
        if(server==null) {
            throw new NullPointerException("Server is null");
        }
        if(server.isEmpty()) {
            throw new IllegalArgumentException("Server name is empty");
        }
        if(data==null) {
            throw new NullPointerException("Data is null");
        }
        JsonObject resp = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.UPDATE_SERVER_DATA, UpdateServerDataRequest.build(server, data)));
        if(!resp.get("ok").getAsBoolean()) {
            return new Errorable<>(resp.get("errno").getAsInt());
        }
        return new Errorable<>(ServerInfo.fromJson(resp.getAsJsonObject("server")));
    }

    public static Errorable<Boolean> removeServer(String server) {
        if(server==null) {
            throw new NullPointerException("Server is null");
        }
        if(server.isEmpty()) {
            throw new IllegalArgumentException("Server name is empty");
        }
        JsonObject data = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.REMOVE_SERVER, RemoveServerRequest.build(server)));
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        return new Errorable<>(true);
    }

    public static Errorable<Boolean> makeWebSocketServerOwner(String server) {
        JsonObject data = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.MAKE_WEB_SOCKET_SERVER_OWNER, MakeWebSocketServerOwnerRequest.build(server)));
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        return new Errorable<>(true);
    }

    public static Errorable<Boolean> sendSignal(String server, ServerInfo.ServerSignal signal) {
        JsonObject data = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.SEND_SIGNAL, SendSignalRequest.build(server, signal)));
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        return new Errorable<>(true);
    }

    public static Errorable<ArrayList<ServerInfo>> requestServerList() {
        JsonObject data = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.LIST_SERVERS, ListServersRequest.build()));
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        ArrayList<ServerInfo> infos = new ArrayList<>();
        for(JsonElement e : data.getAsJsonArray("servers")) {
            infos.add(ServerInfo.fromJson((JsonObject) e));
        }
        return new Errorable<>(infos);
    }

    public static Errorable<ServerInfo> getServerInfo(String server) {
        JsonObject data = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.GET_SERVER, GetServerRequest.build(server)));
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        return new Errorable<>(ServerInfo.fromJson(data.getAsJsonObject("server")));
    }

}
