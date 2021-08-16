package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetModuleRequest implements WebSocketRequest {

    private final String id;

    private GetModuleRequest(String id) {
        this.id = id;
    }

    public static GetModuleRequest build(String id) {
        return new GetModuleRequest(id);
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        return obj;
    }

    public int getOpcode() {
        return Opcodes.GET_MODULE;
    }

}
