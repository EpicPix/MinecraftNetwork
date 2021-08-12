package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

import java.util.UUID;

public class GetPlayerRequest implements WebSocketRequest {

    private final UUID uuid;
    private final String username;

    private GetPlayerRequest(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public static GetPlayerRequest build(UUID uuid, String username) {
        if(uuid == null && username == null) {
            throw new IllegalArgumentException("UUID or Username must not be null");
        }
        return new GetPlayerRequest(uuid, username);
    }

    public int getOpcode() {
        return Opcodes.GET_PLAYER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if(uuid!=null) obj.addProperty("uuid", uuid.toString());
        if(username!=null) obj.addProperty("username", username);
        return obj;
    }
}
