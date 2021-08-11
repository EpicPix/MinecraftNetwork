package ga.epicpix.network.common.players;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.Opcodes;
import ga.epicpix.network.common.net.websocket.requests.WebsocketRequest;
import ga.epicpix.network.common.net.websocket.requests.data.*;

import java.util.UUID;

public class PlayerManager {

    public static Errorable<PlayerInfo> getPlayer(UUID uuid) {
        return getPlayer(uuid, null);
    }

    public static Errorable<PlayerInfo> getPlayer(String username) {
        return getPlayer(null, username);
    }

    public static Errorable<PlayerInfo> getPlayer(UUID uuid, String username) {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(GetPlayerRequest.build(uuid, username)));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new Gson().fromJson(obj.getAsJsonObject("player"), PlayerInfo.class));
    }

    public static Errorable<PlayerInfo> updatePlayer(UUID uuid, UpdatePlayerRequest.Data data) {
        return updatePlayer(uuid, null, data);
    }

    public static Errorable<PlayerInfo> updatePlayer(String username, UpdatePlayerRequest.Data data) {
        return updatePlayer(null, username, data);
    }

    public static Errorable<PlayerInfo> updatePlayer(UUID uuid, String username, UpdatePlayerRequest.Data data) {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(UpdatePlayerRequest.build(uuid, username, data)));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new Gson().fromJson(obj.getAsJsonObject("player"), PlayerInfo.class));
    }

    public static Errorable<PlayerInfo> updatePlayerOrCreate(UUID uuid, String username, UpdatePlayerRequest.Data data) {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(UpdatePlayerOrCreateRequest.build(uuid, username, data)));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new Gson().fromJson(obj.getAsJsonObject("player"), PlayerInfo.class));
    }

    public static Errorable<PlayerInfo> getPlayerOrCreate(UUID uuid, String username) {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(GetPlayerOrCreateRequest.build(uuid, username)));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new Gson().fromJson(obj.getAsJsonObject("player"), PlayerInfo.class));
    }
}
