package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetVersionsRequest implements WebSocketRequest {

    private GetVersionsRequest() {}

    public static GetVersionsRequest build() {
        return new GetVersionsRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_VERSIONS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
