package ga.epicpix.network.common.servers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.requests.WebSocketRequest;
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
        JsonObject resp = WebSocketRequest.sendRequest(WebSocketRequest.createRequest(UpdateServerDataRequest.build(server, data)));
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
        JsonObject data = WebSocketRequest.sendRequest(WebSocketRequest.createRequest(RemoveServerRequest.build(server)));
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        return new Errorable<>(true);
    }

    public static Errorable<Boolean> makeWebSocketServerOwner(String server) {
        JsonObject data = WebSocketRequest.sendRequest(WebSocketRequest.createRequest(MakeWebSocketServerOwnerRequest.build(server)));
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        return new Errorable<>(true);
    }

    public static Errorable<Boolean> sendSignal(String server, ServerInfo.ServerSignal signal) {
        JsonObject data = WebSocketRequest.sendRequest(WebSocketRequest.createRequest(SendSignalRequest.build(server, signal)));
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        return new Errorable<>(true);
    }

    public static Errorable<ArrayList<ServerInfo>> requestServerList() {
        JsonObject data = WebSocketRequest.sendRequest(WebSocketRequest.createRequest(ListServersRequest.build()));
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
        JsonObject data = WebSocketRequest.sendRequest(WebSocketRequest.createRequest(GetServerRequest.build(server)));
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        return new Errorable<>(ServerInfo.fromJson(data.getAsJsonObject("server")));
    }

}
