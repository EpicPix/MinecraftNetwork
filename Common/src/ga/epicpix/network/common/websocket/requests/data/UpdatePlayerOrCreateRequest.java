package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.SerializableJson;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

import java.util.UUID;

public class UpdatePlayerOrCreateRequest extends RequestData {

    private final UUID uuid;
    private final String username;
    private final UpdatePlayerRequest.Data data;

    private UpdatePlayerOrCreateRequest(UUID uuid, String username, UpdatePlayerRequest.Data data) {
        this.uuid = uuid;
        this.username = username;
        this.data = data;
    }

    public static UpdatePlayerOrCreateRequest build(UUID uuid, String username, UpdatePlayerRequest.Data data) {
        if(!RequestPolicies.isAllowed(Opcodes.UPDATE_PLAYER_OR_CREATE, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
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
