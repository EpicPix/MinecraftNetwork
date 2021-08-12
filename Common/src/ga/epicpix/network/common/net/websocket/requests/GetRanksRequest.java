package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetRanksRequest implements WebSocketRequest {

    private GetRanksRequest() {}

    public static GetRanksRequest build() {
        return new GetRanksRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_RANKS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
