package ga.epicpix.network.common.players;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.WebSocketRequester;
import ga.epicpix.network.common.net.websocket.requests.GetPlayerOrCreateRequest;
import ga.epicpix.network.common.net.websocket.requests.GetPlayerRequest;
import ga.epicpix.network.common.net.websocket.requests.UpdatePlayerOrCreateRequest;
import ga.epicpix.network.common.net.websocket.requests.UpdatePlayerRequest;

import java.util.UUID;

public final class PlayerManager {

    @ChecksPermission(ModulePermission.GET_PLAYER)
    public static Errorable<PlayerInfo> getPlayer(UUID uuid) {
        return getPlayer(uuid, null);
    }

    @ChecksPermission(ModulePermission.GET_PLAYER)
    public static Errorable<PlayerInfo> getPlayer(String username) {
        return getPlayer(null, username);
    }

    @ChecksPermission(ModulePermission.GET_PLAYER)
    public static Errorable<PlayerInfo> getPlayer(UUID uuid, String username) {
        JsonObject obj = WebSocketRequester.sendRequest(GetPlayerRequest.build(uuid, username));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new Gson().fromJson(obj.getAsJsonObject("player"), PlayerInfo.class));
    }

    @ChecksPermission(ModulePermission.UPDATE_PLAYER)
    public static Errorable<PlayerInfo> updatePlayer(UUID uuid, UpdatePlayerRequest.Data data) {
        return updatePlayer(uuid, null, data);
    }

    @ChecksPermission(ModulePermission.UPDATE_PLAYER)
    public static Errorable<PlayerInfo> updatePlayer(String username, UpdatePlayerRequest.Data data) {
        return updatePlayer(null, username, data);
    }

    @ChecksPermission(ModulePermission.UPDATE_PLAYER)
    public static Errorable<PlayerInfo> updatePlayer(UUID uuid, String username, UpdatePlayerRequest.Data data) {
        JsonObject obj = WebSocketRequester.sendRequest(UpdatePlayerRequest.build(uuid, username, data));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new Gson().fromJson(obj.getAsJsonObject("player"), PlayerInfo.class));
    }

    @ChecksPermission(ModulePermission.UPDATE_PLAYER)
    public static Errorable<PlayerInfo> updatePlayerOrCreate(UUID uuid, String username, UpdatePlayerRequest.Data data) {
        JsonObject obj = WebSocketRequester.sendRequest(UpdatePlayerOrCreateRequest.build(uuid, username, data));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new Gson().fromJson(obj.getAsJsonObject("player"), PlayerInfo.class));
    }

    @ChecksPermission({ModulePermission.GET_PLAYER, ModulePermission.UPDATE_PLAYER})
    public static Errorable<PlayerInfo> getPlayerOrCreate(UUID uuid, String username) {
        JsonObject obj = WebSocketRequester.sendRequest(GetPlayerOrCreateRequest.build(uuid, username));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new Gson().fromJson(obj.getAsJsonObject("player"), PlayerInfo.class));
    }
}
