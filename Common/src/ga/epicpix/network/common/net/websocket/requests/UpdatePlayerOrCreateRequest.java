package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.net.websocket.Opcodes;

import java.util.UUID;

public class UpdatePlayerOrCreateRequest implements WebSocketRequest {

    private final UUID uuid;
    private final String username;
    private final UpdatePlayerRequest.Data data;

    private UpdatePlayerOrCreateRequest(UUID uuid, String username, UpdatePlayerRequest.Data data) {
        this.uuid = uuid;
        this.username = username;
        this.data = data;
    }

    public static UpdatePlayerOrCreateRequest build(UUID uuid, String username, UpdatePlayerRequest.Data data) {
        ModuleLoader.checkModulePermission(ModuleLoader.ModulePermission.UPDATE_PLAYER);
        if(uuid==null || username==null) {
            throw new IllegalArgumentException("UUID and Username must not be null");
        }
        return new UpdatePlayerOrCreateRequest(uuid, username, data);
    }

    public int getOpcode() {
        return Opcodes.UPDATE_PLAYER_OR_CREATE;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("uuid", uuid.toString());
        obj.addProperty("username", username);
        obj.add("data", data.toJson());
        return obj;
    }

}
