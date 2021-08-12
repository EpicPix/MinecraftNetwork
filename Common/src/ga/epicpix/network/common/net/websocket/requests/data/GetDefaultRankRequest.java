package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetDefaultRankRequest implements WebSocketRequest {

    private GetDefaultRankRequest() {}

    public static GetDefaultRankRequest build() {
        return new GetDefaultRankRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_DEFAULT_RANK;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
