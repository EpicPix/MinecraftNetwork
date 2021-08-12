package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class ListServersRequest implements WebSocketRequest {

    private ListServersRequest() {}

    public static ListServersRequest build() {
        return new ListServersRequest();
    }

    public int getOpcode() {
        return Opcodes.LIST_SERVERS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
