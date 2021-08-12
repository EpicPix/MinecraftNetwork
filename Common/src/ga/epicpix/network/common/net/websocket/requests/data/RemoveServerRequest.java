package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class RemoveServerRequest implements WebSocketRequest {

    private final String serverName;

    private RemoveServerRequest(String serverName) {
        this.serverName = serverName;
    }

    public static RemoveServerRequest build(String serverName) {
        return new RemoveServerRequest(serverName);
    }

    public int getOpcode() {
        return Opcodes.REMOVE_SERVER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", serverName);
        return obj;
    }

}
