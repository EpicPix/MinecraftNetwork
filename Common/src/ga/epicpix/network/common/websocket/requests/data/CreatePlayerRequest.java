package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

import java.util.UUID;

public class CreatePlayerRequest extends RequestData {

    private final UUID uuid;
    private final String username;

    private CreatePlayerRequest(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public static CreatePlayerRequest build(UUID uuid, String username) {
        if(!RequestPolicies.isAllowed(Opcodes.CREATE_PLAYER, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        if(uuid == null || username == null) {
            throw new IllegalArgumentException("UUID and Username must not be null");
        }
        return new CreatePlayerRequest(uuid, username);
    }

    public int getOpcode() {
        return Opcodes.CREATE_PLAYER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if(uuid!=null) obj.addProperty("uuid", uuid.toString());
        if(username!=null) obj.addProperty("username", username);
        return obj;
    }
}
