package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetModulesRequest implements WebSocketRequest {

    private GetModulesRequest() {}

    public static GetModulesRequest build() {
        return new GetModulesRequest();
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

    public int getOpcode() {
        return Opcodes.GET_MODULES;
    }

}
